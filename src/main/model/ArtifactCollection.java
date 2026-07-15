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

    // EFFECTS: returns a list of all artifacts from the specified museum in the collection
    public List<Artifact> getArtifactsByMuseum(String museumName) {
        List<Artifact> museumArtifacts = new ArrayList<>();
        for (Artifact artifact : artifacts) {
            if (artifact.belongsToMuseum(museumName)) {
                museumArtifacts.add(artifact);
            }
        }
        return museumArtifacts;
    }
}