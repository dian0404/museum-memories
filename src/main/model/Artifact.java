package model;

import org.json.JSONObject;

import persistence.Writable;

// JSON serialization code in this class is based on the
// JsonSerializationDemo provided by the UBC CPSC 210 course.

// Represents an artifact with a name, museum, description, rating,
// visit date, and personal note.
public class Artifact implements Writable {
    private String name;
    private String museum;
    private String description;
    private int rating;
    private String visitDate;
    private String personalNote;
    private String imagePath;

    // REQUIRES: rating is between 0 and 5, inclusive
    // EFFECTS: constructs an artifact with a name, museum, description, rating,
    // visit date, and personal note
    public Artifact(String name, String museum, String description, int rating,
                    String visitDate, String personalNote) {
        this(name, museum, description, rating, visitDate, personalNote, "");
    }

    // EFFECTS: constructs an artifact with an optional local image path
    public Artifact(String name, String museum, String description, int rating,
                    String visitDate, String personalNote, String imagePath) {
        this.name = name;
        this.museum = museum;
        this.description = description;
        this.rating = rating;
        this.visitDate = visitDate;
        this.personalNote = personalNote;
        this.imagePath = imagePath;
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

    // EFFECTS: returns the visit date of the artifact
    public String getVisitDate() {
        return visitDate;
    }

    // EFFECTS: returns the personal note of the artifact
    public String getPersonalNote() {
        return personalNote;
    }

    // EFFECTS: returns the local image path of the artifact
    public String getImagePath() {
        return imagePath;
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
        json.put("visitDate", visitDate);
        json.put("personalNote", personalNote);
        json.put("imagePath", imagePath);
        return json;
    }
}
