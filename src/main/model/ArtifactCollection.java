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
    // EFFECTS: if artifact is not already in the collection, adds it
    // to the collection and logs the addition
    public void addArtifact(Artifact artifact) {
        if (!artifacts.contains(artifact)) {
            artifacts.add(artifact);

            EventLog.getInstance().logEvent(
                    new Event("Artifact added to Museum Memories: " + artifact.getName()));
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

        EventLog.getInstance().logEvent(
                new Event("Viewed all five-star artifacts."));

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
    // collection and logs this action
    public List<Artifact> getArtifactsByMuseum(String museumName) {
        List<Artifact> museumArtifacts = new ArrayList<>();
        for (Artifact artifact : artifacts) {
            if (artifact.belongsToMuseum(museumName)) {
                museumArtifacts.add(artifact);
            }
        }

        EventLog.getInstance().logEvent(
                new Event("Viewed artifacts from museum: " + museumName));

        return museumArtifacts;
    }

    // EFFECTS: returns all artifacts in this collection and logs that
    // all artifacts were viewed
    public List<Artifact> viewAllArtifacts() {
        EventLog.getInstance().logEvent(
                new Event("Viewed all artifacts."));
        return artifacts;
    }

    // MODIFIES: this
    // EFFECTS: if artifact is not already in the collection, adds it
    // without logging the addition
    public void addArtifactFromFile(Artifact artifact) {
        if (!artifacts.contains(artifact)) {
            artifacts.add(artifact);
        }
    }

    @Override
    // EFFECTS: returns this artifact collection as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("artifacts", artifactsToJson());
        return json;
    }

    // EFFECTS: logs that this artifact collection was loaded
    public void logCollectionLoaded() {
        EventLog.getInstance().logEvent(
                new Event("Artifact collection loaded."));
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