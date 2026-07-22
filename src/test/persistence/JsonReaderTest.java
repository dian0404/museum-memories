package persistence;

import model.Artifact;
import model.ArtifactCollection;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Code in this class is based on the JsonSerializationDemo provided by the UBC CPSC 210 course.

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");

        try {
            ArtifactCollection artifactCollection = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyArtifactCollection() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyArtifactCollection.json");

        try {
            ArtifactCollection artifactCollection = reader.read();
            assertEquals(0, artifactCollection.getArtifacts().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralArtifactCollection() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralArtifactCollection.json");

        try {
            ArtifactCollection artifactCollection = reader.read();

            List<Artifact> artifacts = artifactCollection.getArtifacts();
            assertEquals(2, artifacts.size());

            checkArtifact(
                    "Terracotta Warrior",
                    "Emperor Qinshihuang's Mausoleum Site Museum",
                    "A life-sized clay soldier from the Qin Dynasty",
                    5,
                    artifacts.get(0));

            checkArtifact(
                    "The Starry Night",
                    "Museum of Modern Art",
                    "An oil painting created by Vincent van Gogh",
                    4,
                    artifacts.get(1));

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
