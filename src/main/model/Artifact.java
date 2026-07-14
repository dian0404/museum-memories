// Represents an artifact with a name, museum, description, and rating.
package model;

public class Artifact {
    private String name;
    private String museum;
    private String description;
    private int rating;

    // EFFECTS: constructs an artifact with a name, museum, description and rating
    public Artifact(String name, String museum, String description, int rating) {

    }

    // EFFECTS: returns the name of the artifact
    public String getName() {
        return null;
    }

    // EFFECTS: returns the museum of the artifact
    public String getMuseum() {
        return null;
    }

    // EFFECTS: returns the description of the artifact
    public String getDescription() {
        return null;
    }

    // EFFECTS: returns the rating of the artifact
    public int getRating() {
        return 0;
    }

    // EFFECTS: returns true if the artifact has a 5-star rating
    // false otherwise
    public boolean isFiveStar() {
        return false;
    }

    // EFFECTS: returns true if the artifact belongs to the specified museum
    // false otherwise
    public boolean belongsToMuseum(String museumName) {
        return false;
}