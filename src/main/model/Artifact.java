package model;

import org.json.JSONObject;

import persistence.Writable;

// Represents an artifact with a name, museum, description, and rating.
public class Artifact implements Writable {
    private String name;
    private String museum;
    private String description;
    private int rating;

    // REQUIRES: rating is between 0 and 5, inclusive
    // EFFECTS: constructs an artifact with a name, museum, description and rating
    public Artifact(String name, String museum, String description, int rating) {
        this.name = name;
        this.museum = museum;
        this.description = description;
        this.rating = rating;
    }

    // EFFECTS: returns the name of the artifact
    public String getName() {
        return name;
    }

    // EFFECTS: returns the museum of the artifact
    public String getMuseum() {
        return museum;
    }

    // EFFECTS: returns the description of the artifact
    public String getDescription() {
        return description;
    }

    // EFFECTS: returns the rating of the artifact
    public int getRating() {
        return rating;
    }

    // EFFECTS: returns true if the artifact has a 5-star rating
    // false otherwise
    public boolean isFiveStar() {
        if (this.rating == 5) {
            return true;
        } else {
            return false;
        }
    }

    // EFFECTS: returns true if the artifact belongs to the specified museum
    // false otherwise
    public boolean belongsToMuseum(String museumName) {
        if (this.museum.equals(museumName)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    // EFFECTS: returns this artifact as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("museum", museum);
        json.put("description", description);
        json.put("rating", rating);
        return json;
    }
}
