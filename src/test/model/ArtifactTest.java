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
                "Tang Dynasty silk road national treasure, top cultural relic",
                5,
                "2026-07-15",
                "I was impressed by how detailed and well-preserved it was.");
    }

    @Test
    public void testConstructor() {
        assertEquals("Gold-mounted Agate Rhinoceros Cup", testArtifact.getName());
        assertEquals("Shaanxi History Museum", testArtifact.getMuseum());
        assertEquals("Tang Dynasty silk road national treasure, top cultural relic", testArtifact.getDescription());
        assertEquals(5, testArtifact.getRating());
        assertEquals("2026-07-15", testArtifact.getVisitDate());
        assertEquals(
                "I was impressed by how detailed and well-preserved it was.",
                testArtifact.getPersonalNote());
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
                "Largest and heaviest bronze sacrificial vessel of Shang Dynasty",
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
}
