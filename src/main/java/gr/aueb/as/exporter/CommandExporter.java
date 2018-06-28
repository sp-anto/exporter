package gr.aueb.as.exporter;

import gr.aueb.as.exporter.enumeration.ArgumentType;
import gr.aueb.as.exporter.model.Command;
import gr.aueb.as.exporter.model.Option;
import gr.aueb.as.exporter.util.Constants;
import gr.aueb.as.exporter.util.ShellCommandExecutor;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandExporter {
    private static CommandExporter ourInstance = new CommandExporter();
//    private static final String LTRACE = "ltrace -x 'getopt*' -L -F ltrace.conf -A 100 -o tmp.txt %s";
    private static final String CALL_PREFIX = "getopt_long@libc.so.6(1";


    private static final String MAN_INVOCATION = "man %s | tee \"-\"";
    private static final String WHATIS_INVOCATION = "whatis --long %s";

    public static CommandExporter getInstance() {
        return ourInstance;
    }

    private CommandExporter() {
    }

    public Command exportCommand(String commandName) throws Exception {
        System.out.println("############## > Exporting command " + commandName);
        Command command = new Command();
        command.setName(commandName);
//
        handleWhatIsInvocation(command);
        handleManInvocation(command);

//        final String shellCommand = String.format(LTRACE, commandName);

//        try {
//            Process process = Runtime.getRuntime().exec(shellCommand);
//            process.waitFor();
//
//            process.destroy();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        String[] ltrace = new String[] { "ltrace", "-x", "'getopt*'", "-L", "-F", "ltrace.conf", "-A", "100", "-o", "tmp.txt", commandName };

        final String result = ShellCommandExecutor.getInstance().executeCommand("./ltrace.sh " + commandName);
//        ShellCommandExecutor.getInstance().executeCommand(shellCommand);
//        ShellCommandExecutor.getInstance().executeCommand("ls");
//        final String result = ShellCommandExecutor.getInstance().executeCommand("less tmp.txt | tee \"-\"");
//        try (FileInputStream fileInputStream = new FileInputStream("tmp.out")) {
//            result = IOUtils.toString(fileInputStream, "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//         final String result = Constants.MV_GETOPT;

        // get line of getopt_long call
        System.out.println("############## > GetOpt Call\n" + result);
        String getoptCall = result.substring(0, result.indexOf("\n"));

        // exclude text until optString param
        Pattern patterUntilOptString = Pattern.compile("getopt_long@libc.so.6\\(\\d+, \\[\\s+], ");
        Matcher mathcerUntilOptString = patterUntilOptString.matcher(getoptCall);
        if (mathcerUntilOptString.find()) {
            getoptCall = getoptCall.replace(mathcerUntilOptString.group(0), "");
        }

        String optString = null;
        Pattern optStringPattern = Pattern.compile("\"[a-zA-Z+-:]+\", ");
        Matcher optStringMatcher = optStringPattern.matcher(getoptCall);
        if (optStringMatcher.find()) {
            optString = optStringMatcher.group(0);
            getoptCall = getoptCall.replace(optString, "");
            optString = optString.substring(1, optString.length() - 3);
        }

        String longOptions = null;
        Pattern longOptionsPattern = Pattern.compile("\\[.+]");
        Matcher longOptionsMatcher = longOptionsPattern.matcher(getoptCall);
        if (longOptionsMatcher.find()) {
            longOptions = longOptionsMatcher.group(0);
        }

        if (longOptions.contains("{ nil, 0, nil, 0 }")) {
            longOptions = longOptions.substring(0, longOptions.indexOf(", { nil, 0, nil, 0 }"));
            longOptions += " ]";
        } else {
            System.out.println("END OF OPTIONS was not found");
        }


        List<Option> options = new ArrayList<>();
        parseOptString(optString, options);
        parseLongOptions(longOptions, options);



        options.forEach(option -> OptionDescriptionExporter.getInstance().getTextForOption(commandName, option));
//        options.forEach(System.out::println);

        command.setOptions(options);

        System.out.println(command);

        return command;
    }

    private void parseOptString(String optString, List<Option> options) throws Exception {
        if (optString == null) {
            throw new Exception("OptString is null!");
        }
        boolean read = optString.length() > 0;
        int i = 0;
        while (read) {
            char currentChar = optString.charAt(i);
            if (currentChar != '+' && currentChar != '-' && currentChar != ':') {
                int colons = 0;
                boolean canReadNext = i + 1 < optString.length();
                if (canReadNext) {
                    char nextChar = optString.charAt(i + 1);
                    if (nextChar == ':') {
                        colons ++;
                        canReadNext = i + 2 < optString.length();
                        if (canReadNext) {
                            nextChar = optString.charAt(i + 2);
                            if (nextChar == ':') {
                                colons++;
                            }
                        }
                    }
                }
                Option option = new Option();
                option.setName(currentChar + "");
                option.setArgumentType(ArgumentType.fromColonCount(colons));
                options.add(option);
                i = i + colons;
            }
            i++;
            read = i < optString.length();
        }
    }

    private void parseLongOptions(String longOptions, List<Option> options) throws Exception {
        if (longOptions == null) {
            throw new Exception("LongOptions is null!");
        }
        Pattern optionPattern = Pattern.compile("\\{.+?,.+?,.+?,.+?}");
        Matcher optStringMatcher = optionPattern.matcher(longOptions);
        while (optStringMatcher.find()) {
            String longOption = optStringMatcher.group(0);
            longOption = longOption.substring(1, longOption.length() -2);
            String[] elements = longOption.split(",");
            String name = elements[0].trim().replace("\"", "");
            int argType = Integer.parseInt(elements[1].trim());
            ArgumentType argumentType = ArgumentType.fromColonCount(argType);
            String ptr = elements[2].trim();
            String equivalent = null;
            if ("nil".equals(ptr)) {
                int val = Integer.parseInt(elements[3].trim().replace("'", ""));
                char ch = (char) val;
                boolean hasEq = options.stream().map(Option::getName).anyMatch(s -> s.equals(ch + ""));
                if (hasEq) {
                    equivalent = ch + "";
                }
            }
            Option option = new Option(name, argumentType, equivalent, true);
            options.add(option);
        }
    }

    private static final String SYNOPSIS_ROW = "SYNOPSIS";
    private static final String DESCRIPTION_ROW = "DESCRIPTION";

    private void handleManInvocation(Command command) {
        String manInvocation = String.format(MAN_INVOCATION, command.getName());
        String result = ShellCommandExecutor.getInstance().executeCommand(manInvocation);
//        String result = Constants.MAN_INVOCATION;

        // TODO: check if man excuted successfully

        String[] rows = result.split("\n");
        for(int i = 0 ; i < rows.length; i++) {
            String row = rows[i];
            if (SYNOPSIS_ROW.equals(row)) {
                List<String> syntaxes = new ArrayList<>();
                row = rows[++i];
                while (!row.equals("")) {
                    String syntax = row.trim();
                    syntaxes.add(syntax);
                    row = rows[++i];
                }
                command.setSyntaxes(syntaxes);
            }

            if (DESCRIPTION_ROW.equals(row)) {
                String description = "";
                row = rows[++i];
                while (!row.equals("")) {
                    String descRow = row.trim();
                    description += descRow + " ";
                    row = rows[++i];
                }
                command.setLongDescription(description.trim());
                return;
            }
        }
    }

    private void handleWhatIsInvocation(Command command) {
        String whatisInvocation = String.format(WHATIS_INVOCATION, command.getName());
        String result = ShellCommandExecutor.getInstance().executeCommand(whatisInvocation);
//        String result = "mv  (1)                - move (rename) files";
System.out.println("What is result\n" + result);
        if (result.contains("nothing appropriate")) {
            System.out.println("System can not extract documentation about command " + command.getName());
            // TODO: throw an exception in order to stop execution
        }

        String[] rows = result.split("\n");
        String txt = rows[0].substring(rows[0].indexOf("-") + 1);
        String shortDescription = txt.trim();

        command.setShortDescription(shortDescription);
    }
}
