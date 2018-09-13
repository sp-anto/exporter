package gr.aueb.as.exporter;

import gr.aueb.as.exporter.enumeration.ArgumentType;
import gr.aueb.as.exporter.model.Command;
import gr.aueb.as.exporter.model.Option;
import gr.aueb.as.exporter.util.ShellCommandExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandExporter {
    private static CommandExporter ourInstance = new CommandExporter();
    private static final String INFO_INVOCATION = "info --show-options %s | tee \"-\"";
    private static final String MAN_INVOCATION = "man %s | tee \"-\"";
    private static final String WHATIS_INVOCATION = "whatis --long %s";
    private static final String SYNOPSIS_ROW = "SYNOPSIS";
    private static final String DESCRIPTION_ROW = "DESCRIPTION";

    public static CommandExporter getInstance() {
        return ourInstance;
    }

    private CommandExporter() {
    }

    protected Command exportCommand(String commandName) throws Exception {
        System.out.println("############## > Exporting command " + commandName);
        Command command = new Command();
        command.setName(commandName);

        handleWhatIsInvocation(command);
        handleManInvocation(command);
        handleLtraceInvocation(command);
        command.getOptions().forEach(option -> this.getTextForOption(commandName, option));
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
        System.out.println(longOptions);
        Matcher optStringMatcher = optionPattern.matcher(longOptions);
        while (optStringMatcher.find()) {
            String longOption = optStringMatcher.group(0);
            System.out.println(longOption);
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

    private void handleManInvocation(Command command) {
        String manInvocation = String.format(MAN_INVOCATION, command.getName());
        String result = ShellCommandExecutor.getInstance().executeCommand(manInvocation);

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
                row = rows[++i].trim();
                System.out.println(row);
                while (!row.equals("")) {
                    description += row + " ";
                    row = rows[++i].trim();
                }
                command.setLongDescription(description.trim());
                return;
            }
        }
    }

    private void handleWhatIsInvocation(Command command) {
        String whatisInvocation = String.format(WHATIS_INVOCATION, command.getName());
        String result = ShellCommandExecutor.getInstance().executeCommand(whatisInvocation);
        System.out.println("What is result:\n" + result);
        if (result.contains("nothing appropriate")) {
            System.out.println("System can not extract documentation about command " + command.getName());
        }

        String[] rows = result.split("\n");
        String txt = rows[0].substring(rows[0].indexOf("-") + 1);
        String shortDescription = txt.trim();

        command.setShortDescription(shortDescription);
    }

    private void handleLtraceInvocation(Command command) throws Exception {
        String commandName = command.getName();

        System.out.println("Proceed with tracing getopt_log call for command " + commandName);
        final String result = ShellCommandExecutor.getInstance().executeCommand("./work/ltrace.sh " + commandName);

        // get line of getopt_long call
        System.out.println("############## > GetOpt Call\n" + result);
        String getoptCall = result.substring(0, result.indexOf("\n"));

        // exclude text until optString param
        Pattern patterUntilOptString = Pattern.compile("getopt_long@libc.so.6\\(\\d+, \\[\\s+], ");
        Matcher mathcerUntilOptString = patterUntilOptString.matcher(getoptCall);
        if (mathcerUntilOptString.find()) {
            String replace = mathcerUntilOptString.group(0);
            getoptCall = getoptCall.replace(replace, "");
            System.out.println("After replacement!: " + getoptCall);
        }

        String optString = null;
        Pattern optStringPattern = Pattern.compile("\".*?\", ");
        Matcher optStringMatcher = optStringPattern.matcher(getoptCall);
        if (optStringMatcher.find()) {
            optString = optStringMatcher.group(0);
            System.out.println("OptString: " + optString);
            getoptCall = getoptCall.replace(optString, "");
            optString = optString.substring(1, optString.length() - 3);
        }

        String longOptions = null;
        Pattern longOptionsPattern = Pattern.compile("\\[.+]");
        System.out.println("Getopt:" + getoptCall);
        Matcher longOptionsMatcher = longOptionsPattern.matcher(getoptCall);
        if (longOptionsMatcher.find()) {
            longOptions = longOptionsMatcher.group(0);
        }

        List<Option> options = new ArrayList<>();
        parseOptString(optString, options);
        parseLongOptions(longOptions, options);

        // remove equivalent options
        List<Option> optionsToRemove = new ArrayList<>();
        for (Option option : options) {
            if (option.getEquivalent() != null) {
                for (Option option1 : options) {
                    if (option1.getName().equals(option.getEquivalent())) {
                        optionsToRemove.add(option1);
                        break;
                    }
                }
            }
        }
        options.removeAll(optionsToRemove);
        command.setOptions(options);
    }

    private void getTextForOption(String command, Option option) {
        String infoInvocation = String.format(INFO_INVOCATION, command);
        String infoResult = ShellCommandExecutor.getInstance().executeCommand(infoInvocation);

        System.out.println(option);

        String textToFind = "-" + option.getName();
        if (option.isLong()) {
            textToFind = "-" + textToFind;
        }

        Pattern optionPattern = Pattern.compile("‘" + textToFind + ".*?’\n");
        Matcher optStringMatcher = optionPattern.matcher(infoResult);
        if (optStringMatcher.find()) {
            textToFind = optStringMatcher.group(0);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ >> " + textToFind);
        } else {
            textToFind = "‘" + textToFind + "’\n";
        }

        int indexOfOption = infoResult.indexOf(textToFind);
        if (indexOfOption > 0) {
            String info = infoResult.substring(infoResult.indexOf(textToFind));
            String[] rows = info.split("\n");
            String description = "";
            int pos = 0;
            boolean read = true;
            while (read && pos < rows.length) {
                String text = rows[pos];
                if (text.equals("")) {
                    read = false;
                } else if (!text.startsWith("‘-")) {
                    text = text.trim();
                    description += text + " ";
                    pos++;
                } else {
                    pos++;
                }
            }

            if (description.contains("*Note") && description.contains("::.")) {
                String noteText = description.substring(description.indexOf("*Note"), description.indexOf("::.") + 3);
                description = description.replace(noteText, "");
            }
            description = description.trim();
            option.setDescription(description);
        }
    }
}
