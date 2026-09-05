package persistence;

import model.Artifact;
import model.ArtifactCollection;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Code in this class is based on the JsonSerializationDemo provided by the UBC CPSC 210 course.

// Tests for the JsonWriter class
@ExcludeFromJacocoGeneratedReport
class JsonWriterTest extends JsonTest {
    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyArtifactCollection() {
        try {
            ArtifactCollection artifactCollection = new ArtifactCollection();

            JsonWriter writer = new JsonWriter(
                    "./data/testWriterEmptyArtifactCollection.json");
            writer.open();
            writer.write(artifactCollection);
            writer.close();

            JsonReader reader = new JsonReader(
                    "./data/testWriterEmptyArtifactCollection.json");
            artifactCollection = reader.read();

            assertEquals(0, artifactCollection.getArtifacts().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralArtifactCollection() {
        try {
            ArtifactCollection artifactCollection = createGeneralArtifactCollection();

            JsonWriter writer = new JsonWriter(
                    "./data/testWriterGeneralArtifactCollection.json");
            writer.open();
            writer.write(artifactCollection);
            writer.close();

            JsonReader reader = new JsonReader(
                    "./data/testWriterGeneralArtifactCollection.json");
            ArtifactCollection loadedCollection = reader.read();

            checkGeneralArtifactCollection(loadedCollection);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    private ArtifactCollection createGeneralArtifactCollection() {
        ArtifactCollection artifactCollection = new ArtifactCollection();

        artifactCollection.addArtifact(new Artifact(
                "Terracotta Warrior",
                "Emperor Qinshihuang's Mausoleum Site Museum",
                "A life-sized clay soldier from the Qin Dynasty",
                5,
                "2026-07-01",
                "Seeing the terracotta warriors in person was very memorable."));

        artifactCollection.addArtifact(new Artifact(
                "The Starry Night",
                "Museum of Modern Art",
                "An oil painting created by Vincent van Gogh",
                4,
                "2026-07-05",
                "I was impressed by the movement and colors in the painting."));

        return artifactCollection;
    }

    private void checkGeneralArtifactCollection(
            ArtifactCollection artifactCollection) {
        List<Artifact> artifacts = artifactCollection.getArtifacts();

        assertEquals(2, artifacts.size());

        checkArtifact(
                "Terracotta Warrior",
                "Emperor Qinshihuang's Mausoleum Site Museum",
                "A life-sized clay soldier from the Qin Dynasty",
                5,
                artifacts.get(0));

        assertEquals("2026-07-01", artifacts.get(0).getVisitDate());
        assertEquals(
                "Seeing the terracotta warriors in person was very memorable.",
                artifacts.get(0).getPersonalNote());

        checkArtifact(
                "The Starry Night",
                "Museum of Modern Art",
                "An oil painting created by Vincent van Gogh",
                4,
                artifacts.get(1));

        assertEquals("2026-07-05", artifacts.get(1).getVisitDate());
        assertEquals(
                "I was impressed by the movement and colors in the painting.",
                artifacts.get(1).getPersonalNote());
    }
}