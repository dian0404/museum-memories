// Represents a collection of artifacts.
package model;

import java.util.ArrayList;
import java.util.List;

public class ArtifactCollection {
    private List<Artifact> artifacts;

    // EFFECTS: constructs an empty artifact collection
    public ArtifactCollection() {
        artifacts = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds an artifact to the collection
    public void addArtifact(Artifact artifact) {
        
    }

    // EFFECTS: returns the list of all artifacts in the collection
    public List<Artifact> getArtifacts() {
        return null;
    }

    // EFFECTS: returns a list of all 5-star artifacts in the collection
    public List<Artifact> getFiveStarArtifacts() {
        return null;
    }

    // EFFECTS: returns a list of all artifacts from the specified museum in the collection
    public List<Artifact> getArtifactsByMuseum(String museumName) {
        return null;
    }
}