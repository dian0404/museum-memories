// Represents an object that can be converted to a JSON object
package persistence;

import org.json.JSONObject;

// Code in this interface is based on the JsonSerializationDemo provided by the UBC CPSC 210 course.
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}