package persistence;

import model.Artifact;
import model.ArtifactCollection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Code in this class is based on the JsonSerializationDemo provided by the UBC CPSC 210 course

// Represents a reader that reads an artifact collection from JSON data stored in a file
public class JsonReader {
    private String source;

    // EFFECTS: constructs a reader to read from the given source file
    public JsonReader(String source) {
        // stub
    }

    // EFFECTS: reads an artifact collection from file and returns it;
    // throws IOException if an error occurs while reading data from the file
    public ArtifactCollection read() throws IOException {
        return null; // stub
    }

    // EFFECTS: parses an artifact collection from the given JSON object and returns it
    private ArtifactCollection parseArtifactCollection(JSONObject jsonObject) {
        return null; // stub
    }

    // MODIFIES: artifactCollection
    // EFFECTS: parses artifacts from the given JSON object and adds them to
    // artifactCollection
    private void addArtifacts(ArtifactCollection artifactCollection, JSONObject jsonObject) {
        // stub
    }

    // MODIFIES: artifactCollection
    // EFFECTS: parses an artifact from the given JSON object and adds it to
    // artifactCollection
    private void addArtifact(ArtifactCollection artifactCollection, JSONObject jsonObject) {
        // stub
    }
}