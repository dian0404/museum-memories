package persistence;

import model.Artifact;
import model.ArtifactCollection;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Code in this class is based on the JsonSerializationDemo provided by the UBC CPSC 210 course.

// Tests for the JsonReader class
@ExcludeFromJacocoGeneratedReport
public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");

        try {
            reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testReaderEmptyArtifactCollection() {
        JsonReader reader = new JsonReader(
                "./data/testReaderEmptyArtifactCollection.json");

        try {
            ArtifactCollection artifactCollection = reader.read();
            assertEquals(0, artifactCollection.getArtifacts().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralArtifactCollection() {
        JsonReader reader = new JsonReader(
                "./data/testReaderGeneralArtifactCollection.json");

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

    @Test
    void testReaderArtifactWithVisitDetails() {
        JsonReader reader = new JsonReader(
                "./data/testReaderArtifactWithVisitDetails.json");

        try {
            ArtifactCollection artifactCollection = reader.read();

            List<Artifact> artifacts = artifactCollection.getArtifacts();
            assertEquals(2, artifacts.size());

            Artifact firstArtifact = artifacts.get(0);

            checkArtifact(
                    "Terracotta Warrior",
                    "Emperor Qinshihuang's Mausoleum Site Museum",
                    "A life-sized clay soldier from the Qin Dynasty",
                    5,
                    firstArtifact);

            assertEquals(
                    "2026-07-01",
                    firstArtifact.getVisitDate());

            assertEquals(
                    "Seeing the terracotta warriors in person was very memorable.",
                    firstArtifact.getPersonalNote());
            assertEquals(
                    "/example/photos/terracotta-warrior.jpg",
                    firstArtifact.getImagePath());

            Artifact secondArtifact = artifacts.get(1);

            checkArtifact(
                    "The Starry Night",
                    "Museum of Modern Art",
                    "An oil painting created by Vincent van Gogh",
                    4,
                    secondArtifact);

            assertEquals(
                    "2026-07-05",
                    secondArtifact.getVisitDate());

            assertEquals(
                    "I was impressed by the movement and colors in the painting.",
                    secondArtifact.getPersonalNote());
            assertEquals(
                    "/example/photos/starry-night.png",
                    secondArtifact.getImagePath());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderOldArtifactWithoutVisitDetails() {
        JsonReader reader = new JsonReader(
                "./data/testReaderOldArtifactCollection.json");

        try {
            ArtifactCollection artifactCollection = reader.read();

            List<Artifact> artifacts = artifactCollection.getArtifacts();
            assertEquals(1, artifacts.size());

            Artifact artifact = artifacts.get(0);

            checkArtifact(
                    "Houmuwu Ding",
                    "National Museum of China, Beijing",
                    "A bronze ritual vessel from the Shang Dynasty",
                    5,
                    artifact);

            assertEquals("", artifact.getVisitDate());
            assertEquals("", artifact.getPersonalNote());
            assertEquals("", artifact.getImagePath());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
