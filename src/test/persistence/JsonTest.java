package persistence;

import model.Artifact;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

// Code in this class is based on the JsonSerializationDemo provided by the UBC CPSC 210 course.
@ExcludeFromJacocoGeneratedReport
public class JsonTest {
    protected void checkArtifact(String name, String museum, String description, int rating, Artifact artifact) {
        assertEquals(name, artifact.getName());
        assertEquals(museum, artifact.getMuseum());
        assertEquals(description, artifact.getDescription());
        assertEquals(rating, artifact.getRating());
    }
}

