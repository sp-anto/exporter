package gr.aueb.as.exporter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gr.aueb.as.exporter.model.Command;
import gr.aueb.as.exporter.model.ShortCommand;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Exporter {
    private static final String OUTPUT_FOLDER = "results/";
    private static final String INVALID_OPTIONS_TEXT = "Invalid options\nPress -h for help";
    private static final String HELP_TEXT = "This program extracts information about unix commands.\n" +
            "The program will create and delete a folder /work/ in\n" +
            "the current directory while working. The results will\n" +
            "be stored in /results/ folder. Files in this folder will\n" +
            "be overwritten.\n" +
            "\n" +
            "Possible syntaxes of the programm are displayed below:\n" +
            "\t* -h             prints the help page of the programm\n" +
            "\t* -f file        extracts information about the \n" +
            "\t                 commands included in file. This file\n" +
            "\t\t\t\t\t must contain the commands separated\n" +
            "\t\t\t\t\t new line character\n" +
            "\t* -c command...  extracts information for the given \n" +
            "\t                 commands\n";

    public static void main(String[] args) throws Exception {
        List<String> commandsToExport = validateArguments(args);
        if (commandsToExport == null) {
            return;
        }
        createWorkDirectory();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<ShortCommand> allCommands = new ArrayList<>();
        for (String commandName: commandsToExport) {
            Command command = CommandExporter.getInstance().exportCommand(commandName);
            File outputFile = new File(OUTPUT_FOLDER + commandName + ".json");
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }
            FileWriter fileWriter = new FileWriter(outputFile);
            gson.toJson(command, fileWriter);
            fileWriter.close();

            ShortCommand shortCommand = new ShortCommand(command);
            allCommands.add(shortCommand);
        }

        FileWriter fileWriter = new FileWriter(OUTPUT_FOLDER + "allCommands.json");
        gson.toJson(allCommands, fileWriter);
        fileWriter.close();

        deleteWorkDirectory();
    }

    private static List<String> validateArguments(String[] args) throws Exception {
        List<String> commands = null;
        if (args.length == 1 && "-h".equals(args[0])) {
            System.out.println(HELP_TEXT);
        } else if (args.length == 2 && "-f".equals(args[0])) {
            String fileName = args[1];
            File file = new File(fileName);
            String fileContent;
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                fileContent = IOUtils.toString(fileInputStream);
            } catch (Exception e) {
                System.out.println("Error while reading file");
                throw e;
            }
            commands = Stream.of(fileContent.split("\n")).map(String::trim).filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        } else if (args.length > 1 && "-c".equals(args[0])) {
            commands = Stream.of(Arrays.copyOfRange(args, 1, args.length))
                    .collect(Collectors.toList());
        } else {
            System.out.println(INVALID_OPTIONS_TEXT);
        }
        return commands;
    }

    private static void createWorkDirectory() throws Exception {
        System.out.println("Creating work directory");

        File workDirectory = new File("work/");
        workDirectory.mkdir();

        copyFileToWorkDirectory("ltrace.conf");
        copyFileToWorkDirectory("ltrace.sh");
    }

    private static void copyFileToWorkDirectory(String fileName) throws Exception {
        System.out.println("Copying " + fileName + " to work directory");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        File copiedFile = new File("work/" + fileName);
        try (InputStream inputStream = classloader.getResourceAsStream(fileName);
             FileOutputStream fileOutputStream = new FileOutputStream(copiedFile)) {
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (Exception e) {
            System.out.println("Error while copying resource " + fileName);
            throw e;
        }
    }

    private static void deleteWorkDirectory() throws Exception {
        System.out.println("Deleting work directory");
        FileUtils.deleteDirectory(new File("work/"));
    }
}
