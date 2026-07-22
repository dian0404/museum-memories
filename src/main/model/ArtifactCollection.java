package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

// JSON serialization code in this class is based on the
// JsonSerializationDemo provided by the UBC CPSC 210 course.

// Represents a collection of artifacts.
public class ArtifactCollection implements Writable {
    private List<Artifact> artifacts;

    // EFFECTS: constructs an empty artifact collection
    public ArtifactCollection() {
        artifacts = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds an artifact to the collection
    public void addArtifact(Artifact artifact) {
        if (!artifacts.contains(artifact)) {
            artifacts.add(artifact);
        }
    }

    // EFFECTS: returns the list of all artifacts in the collection
    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    // EFFECTS: returns a list of all 5-star artifacts in the collection
    public List<Artifact> getFiveStarArtifacts() {
        List<Artifact> fiveStarArtifacts = new ArrayList<>();
        for (Artifact artifact : artifacts) {
            if (artifact.isFiveStar()) {
                fiveStarArtifacts.add(artifact);
            }
        }
        return fiveStarArtifacts;
    }

    // EFFECTS: Returns a set containing all unique museum names associated
    // with the artifacts in the collection.
    public Set<String> getMuseumNames() {
        Set<String> names = new HashSet<>();
        for (Artifact a : artifacts) {
            names.add(a.getMuseum());
        }
        return names;
    }

    // EFFECTS: returns a list of all artifacts from the specified museum in the
    // collection
    public List<Artifact> getArtifactsByMuseum(String museumName) {
        List<Artifact> museumArtifacts = new ArrayList<>();
        for (Artifact artifact : artifacts) {
            if (artifact.belongsToMuseum(museumName)) {
                museumArtifacts.add(artifact);
            }
        }
        return museumArtifacts;
    }

    @Override
    // EFFECTS: returns this artifact collection as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("artifacts", artifactsToJson());
        return json;
    }

    // EFFECTS: returns artifacts in this collection as a JSON array
    private JSONArray artifactsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Artifact artifact : artifacts) {
            jsonArray.put(artifact.toJson());
        }

        return jsonArray;
    }
}