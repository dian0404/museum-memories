package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArtifactCollectionTest {

    private ArtifactCollection testCollection;
    private Artifact a1;
    private Artifact a2;
    private Artifact a3;

    @BeforeEach
    public void runBefore() {
        testCollection = new ArtifactCollection();
        a1 = new Artifact(
                "T-shaped Silk Painting of Mawangdui",
                "Hunan Museum",
                "Ancient Han Dynasty silk painting unearthed from Mawangdui tombs",
                5);
        a2 = new Artifact(
                "Four Sheep Square Zun",
                "Hunan Museum",
                "Shang Dynasty bronze ritual vessel with four sheep decorations",
                4);
        a3 = new Artifact(
                "Water Margin Portrait Scroll",
                "Liaoning Museum",
                "Ancient painting depicting heroes from Water Margin novel",
                5);
    }

    @Test
    public void testAddArtifact() {
        checkCollectionSize(0);
        testCollection.addArtifact(a1);
        checkCollectionSize(1);
        assertTrue(testCollection.getArtifacts().contains(a1));
    }

    @Test
    public void testAddArtifactMultiplierTimes() {
        checkCollectionSize(0);
        testCollection.addArtifact(a1);
        checkCollectionSize(1);
        testCollection.addArtifact(a1);
        checkCollectionSize(1);
    }

    @Test
    public void testGetArtifacts() {
        testCollection.addArtifact(a1);
        testCollection.addArtifact(a2);
        List<Artifact> artifacts = testCollection.getArtifacts();
        assertEquals(2, artifacts.size());
        assertTrue(artifacts.contains(a1));
        assertTrue(artifacts.contains(a2));
    }

    @Test
    public void testGetFiveStarArtifacts() {
        testCollection.addArtifact(a1);
        testCollection.addArtifact(a2);
        testCollection.addArtifact(a3);

        List<Artifact> fiveStars = testCollection.getFiveStarArtifacts();
        assertEquals(2, fiveStars.size());
        assertTrue(fiveStars.contains(a1));
        assertTrue(fiveStars.contains(a3));
        assertFalse(fiveStars.contains(a2));
    }

    @Test
    public void testGetArtifactsByMuseum() {
        testCollection.addArtifact(a1);
        testCollection.addArtifact(a2);
        testCollection.addArtifact(a3);

        List<Artifact> hunanArtifacts = testCollection.getArtifactsByMuseum("Hunan Museum");
        assertEquals(2, hunanArtifacts.size());
        assertTrue(hunanArtifacts.contains(a1));
        assertTrue(hunanArtifacts.contains(a2));
    }


    @Test
    public void testTotalArtifacts(){
        assertEquals(0, testCollection.totalArtifacts());
        testCollection.addArtifact(a1);
        assertEquals(1, testCollection.totalArtifacts());
        testCollection.addArtifact(a3);
        assertEquals(2, testCollection.totalArtifacts());
    }

    private void checkCollectionSize(int expectedSize) {
        assertEquals(expectedSize, testCollection.totalArtifacts());
    }
}
