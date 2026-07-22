package persistence;

import model.ArtifactCollection;

import org.json.JSONObject;

import java.io.*;

// Code in this class is based on the JsonSerializationDemo provided by the UBC CPSC 210 course

// Represents a writer that writes a JSON representation of an artifact collection to a file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs a writer to write to the destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens the writer;
    // throws FileNotFoundException if the destination file cannot be opened
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes a JSON representation of artifactCollection to file
    public void write(ArtifactCollection artifactCollection) {
        JSONObject json = artifactCollection.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes the writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes the given JSON string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}