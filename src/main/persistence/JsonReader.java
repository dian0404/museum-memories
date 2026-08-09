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
        this.source = source;
    }

    // EFFECTS: reads an artifact collection from file and returns it;
    // throws IOException if an error occurs while reading data from the file
    public ArtifactCollection read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseArtifactCollection(jsonObject);
    }

    // EFFECTS: reads the source file as a string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(line -> contentBuilder.append(line));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses an artifact collection from the given JSON object
    // and returns it
    private ArtifactCollection parseArtifactCollection(
            JSONObject jsonObject) {
        ArtifactCollection artifactCollection = new ArtifactCollection();
        addArtifacts(artifactCollection, jsonObject);
        return artifactCollection;
    }

    // MODIFIES: artifactCollection
    // EFFECTS: parses artifacts from the given JSON object and adds them
    // to artifactCollection
    private void addArtifacts(ArtifactCollection artifactCollection, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("artifacts");

        for (Object json : jsonArray) {
            JSONObject nextArtifact = (JSONObject) json;
            addArtifact(artifactCollection, nextArtifact);
        }
    }

    // MODIFIES: artifactCollection
    // EFFECTS: parses an artifact from the given JSON object and adds it
    // to artifactCollection
    private void addArtifact(ArtifactCollection artifactCollection, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String museum = jsonObject.getString("museum");
        String description = jsonObject.getString("description");
        int rating = jsonObject.getInt("rating");
        Artifact artifact = new Artifact(name, museum, description, rating);
        artifactCollection.addArtifactFromFile(artifact);
    }
}