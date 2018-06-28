package gr.aueb.as.exporter;

import gr.aueb.as.exporter.model.Option;
import gr.aueb.as.exporter.util.Constants;
import gr.aueb.as.exporter.util.ShellCommandExecutor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OptionDescriptionExporter {
    private static OptionDescriptionExporter ourInstance = new OptionDescriptionExporter();
    private static final String INFO_INVOCATION = "info --show-options %s | tee \"-\"";

    public static OptionDescriptionExporter getInstance() {
        return ourInstance;
    }

    private OptionDescriptionExporter() {
    }

    public void getTextForOption(String command, Option option) {
        String infoInvocation = String.format(INFO_INVOCATION, command);
        String infoResult = ShellCommandExecutor.getInstance().executeCommand(infoInvocation);
//        String infoResult = Constants.MV_INFO;

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

            if (description.contains("*Note")) {
                String noteText = description.substring(description.indexOf("*Note"), description.indexOf("::.") + 3);
                description = description.replace(noteText, "");
            }
            description = description.trim();
            option.setDescription(description);
        }
    }
}
