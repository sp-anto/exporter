package gr.aueb.as.exporter.util;

import org.apache.commons.io.IOUtils;

public class ShellCommandExecutor {
    private static ShellCommandExecutor ourInstance = new ShellCommandExecutor();

    public static ShellCommandExecutor getInstance() {
        return ourInstance;
    }

    private ShellCommandExecutor() {
    }

    public String executeCommand(String command) {
        String output = null;

        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();

            output = IOUtils.toString(process.getInputStream(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;

    }

}
