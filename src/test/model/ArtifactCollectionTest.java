package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

// Tests for the ArtifactCollection class
@ExcludeFromJacocoGeneratedReport
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
                5,
                "2026-07-10",
                "I was especially impressed by the storytelling and symbolism in the painting.");

        a2 = new Artifact(
                "Four Sheep Square Zun",
                "Hunan Museum",
                4,
                "2026-07-10",
                "The bronze details and the four sheep design were very memorable.");

        a3 = new Artifact(
                "Water Margin Portrait Scroll",
                "Liaoning Museum",
                5,
                "2026-07-15",
                "I liked how the historical figures were represented through traditional painting.");

    }

    @Test
    public void testConstructor() {
        checkCollectionSize(0);
    }
    
    @Test
    public void testAddArtifact() {
        checkCollectionSize(0);
        testCollection.addArtifact(a1);
        checkCollectionSize(1);
        assertTrue(testCollection.getArtifacts().contains(a1));
    }

    @Test
    public void testAddSameArtifactMultiplierTimes() {
        checkCollectionSize(0);
        testCollection.addArtifact(a1);
        checkCollectionSize(1);
        testCollection.addArtifact(a1);
        checkCollectionSize(1);
    }

    @Test
    public void testAddDifferentArtifactMultiplierTimes() {
        checkCollectionSize(0);
        testCollection.addArtifact(a1);
        checkCollectionSize(1);
        testCollection.addArtifact(a2);
        checkCollectionSize(2);
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
        testCollection.addArtifact(a2);
        List<Artifact> fiveStars = testCollection.getFiveStarArtifacts();
        assertEquals(0, fiveStars.size());

        testCollection.addArtifact(a1);
        fiveStars = testCollection.getFiveStarArtifacts();
        assertEquals(1, fiveStars.size());

        testCollection.addArtifact(a3);
        fiveStars = testCollection.getFiveStarArtifacts();
        assertEquals(2, fiveStars.size());

        assertTrue(fiveStars.contains(a1));
        assertTrue(fiveStars.contains(a3));
        assertFalse(fiveStars.contains(a2));
    }

    @Test
    void testGetMuseumNamesSameNames() {
        testCollection.addArtifact(a1);
        testCollection.addArtifact(a2);

        Set<String> names = testCollection.getMuseumNames();
        assertEquals(1, names.size());
        assertTrue(names.contains("Hunan Museum"));
    }
    
    @Test
    void testGetMuseumNamesDifferentNames() {
        testCollection.addArtifact(a2);
        testCollection.addArtifact(a3);

        Set<String> names = testCollection.getMuseumNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("Hunan Museum"));
        assertTrue(names.contains("Liaoning Museum"));
    }

    @Test
    public void testGetArtifactsByMuseum() {
        testCollection.addArtifact(a3);
        List<Artifact> hunanArtifacts = testCollection.getArtifactsByMuseum("Hunan Museum");
        assertEquals(0, hunanArtifacts.size());

        testCollection.addArtifact(a1);
        testCollection.addArtifact(a2);

        hunanArtifacts = testCollection.getArtifactsByMuseum("Hunan Museum");
        assertEquals(2, hunanArtifacts.size());
        assertTrue(hunanArtifacts.contains(a1));
        assertTrue(hunanArtifacts.contains(a2));
    }

    @Test
    public void testUpdateArtifact() {
        testCollection.addArtifact(a1);
        a1.setImagePath("data/images/artifact.jpg");

        assertTrue(testCollection.updateArtifact(
                a1,
                "Updated Artifact",
                "Updated Museum",
                3,
                "2026-09-05",
                "Updated personal note",
                "data/images/updated-artifact.jpg"));

        assertEquals("Updated Artifact", a1.getName());
        assertEquals("Updated Museum", a1.getMuseum());
        assertEquals(3, a1.getRating());
        assertEquals("2026-09-05", a1.getVisitDate());
        assertEquals("Updated personal note", a1.getPersonalNote());
        assertEquals("data/images/updated-artifact.jpg", a1.getImagePath());
    }

    @Test
    public void testUpdateArtifactNotInCollection() {
        assertFalse(testCollection.updateArtifact(
                a1,
                "Updated Artifact",
                "Updated Museum",
                3,
                "2026-09-05",
                "Updated personal note",
                "data/images/updated-artifact.jpg"));

        assertEquals("T-shaped Silk Painting of Mawangdui", a1.getName());
    }

    @Test
    public void testUpdateArtifactRemovesPhoto() {
        testCollection.addArtifact(a1);
        a1.setImagePath("data/images/artifact.jpg");

        assertTrue(testCollection.updateArtifact(
                a1,
                a1.getName(),
                a1.getMuseum(),
                a1.getRating(),
                a1.getVisitDate(),
                a1.getPersonalNote(),
                ""));

        assertEquals("", a1.getImagePath());
    }

    @Test
    public void testRemoveArtifact() {
        testCollection.addArtifact(a1);
        testCollection.addArtifact(a2);

        assertTrue(testCollection.removeArtifact(a1));
        checkCollectionSize(1);
        assertFalse(testCollection.getArtifacts().contains(a1));
        assertTrue(testCollection.getArtifacts().contains(a2));
    }

    @Test
    public void testRemoveArtifactNotInCollection() {
        testCollection.addArtifact(a1);

        assertFalse(testCollection.removeArtifact(a2));
        checkCollectionSize(1);
        assertTrue(testCollection.getArtifacts().contains(a1));
    }

    @Test
    public void testSearchArtifactsByName() {
        addAllTestArtifacts();

        List<Artifact> matches = testCollection.searchArtifacts("silk painting");

        assertEquals(1, matches.size());
        assertTrue(matches.contains(a1));
    }

    @Test
    public void testSearchArtifactsByMuseumIgnoringCase() {
        addAllTestArtifacts();

        List<Artifact> matches = testCollection.searchArtifacts("HUNAN");

        assertEquals(2, matches.size());
        assertTrue(matches.contains(a1));
        assertTrue(matches.contains(a2));
    }

    @Test
    public void testSearchArtifactsByPersonalNote() {
        addAllTestArtifacts();

        List<Artifact> matches = testCollection.searchArtifacts("bronze details");

        assertEquals(1, matches.size());
        assertTrue(matches.contains(a2));
    }

    @Test
    public void testSearchArtifactsNoMatches() {
        addAllTestArtifacts();
        assertTrue(testCollection.searchArtifacts("not in collection").isEmpty());
    }

    @Test
    public void testSearchArtifactsEmptyKeyword() {
        addAllTestArtifacts();
        assertEquals(3, testCollection.searchArtifacts("   ").size());
    }

    private void addAllTestArtifacts() {
        testCollection.addArtifact(a1);
        testCollection.addArtifact(a2);
        testCollection.addArtifact(a3);
    }

    private void checkCollectionSize(int expectedSize) {
        assertEquals(expectedSize, testCollection.getArtifacts().size());
    }
}
