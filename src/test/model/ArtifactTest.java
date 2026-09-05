package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

// Tests for the Artifact class
@ExcludeFromJacocoGeneratedReport
public class ArtifactTest {
    private Artifact testArtifact;

    @BeforeEach
    public void runBefore() {
        testArtifact = new Artifact(
                "Gold-mounted Agate Rhinoceros Cup",
                "Shaanxi History Museum",
                5,
                "2026-07-15",
                "I was impressed by how detailed and well-preserved it was.");
    }

    @Test
    public void testConstructor() {
        assertEquals("Gold-mounted Agate Rhinoceros Cup", testArtifact.getName());
        assertEquals("Shaanxi History Museum", testArtifact.getMuseum());
        assertEquals(5, testArtifact.getRating());
        assertEquals("2026-07-15", testArtifact.getVisitDate());
        assertEquals(
                "I was impressed by how detailed and well-preserved it was.",
                testArtifact.getPersonalNote());
        assertEquals("", testArtifact.getImagePath());
    }

    @Test
    public void testIsFiveStarTrue() {
        assertTrue(testArtifact.isFiveStar());
    }

    @Test
    public void testIsFiveStarFalse() {
        Artifact testArtifactWith4Star = new Artifact(
                "Houmuwu Ding",
                "National Museum of China, Beijing",
                4,
                "2026-07-20",
                "The scale of the bronze vessel was especially memorable.");

        assertFalse(testArtifactWith4Star.isFiveStar());
    }

    @Test
    public void testBelongsToMuseumTrue() {
        assertTrue(testArtifact.belongsToMuseum("Shaanxi History Museum"));
    }

    @Test
    public void testBelongsToMuseumFalse() {
        assertFalse(testArtifact.belongsToMuseum("National Museum of China, Beijing"));
    }

    @Test
    public void testToString() {
        assertEquals(
                "Gold-mounted Agate Rhinoceros Cup — Shaanxi History Museum",
                testArtifact.toString());
    }

    @Test
    public void testSetImagePath() {
        testArtifact.setImagePath("data/images/artifact.jpg");
        assertEquals(
                "data/images/artifact.jpg",
                testArtifact.getImagePath());
    }
}
