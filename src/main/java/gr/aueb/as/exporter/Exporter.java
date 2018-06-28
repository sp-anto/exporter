package gr.aueb.as.exporter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gr.aueb.as.exporter.model.Command;
import gr.aueb.as.exporter.model.ShortCommand;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Exporter {
    private static final String OUTPUT_FOLDER = "results/";

    public static void main(String[] args) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<ShortCommand> allCommands = new ArrayList<>();
        for (String arg: args) {
            Command command = CommandExporter.getInstance().exportCommand(arg);
            File outputFile = new File(OUTPUT_FOLDER + arg + ".json");
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
    }
}
