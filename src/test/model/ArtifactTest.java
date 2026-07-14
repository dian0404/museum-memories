package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArtifactTest {
    private Artifact testArtifact;

    @BeforeEach
    void runBefore() {
        testArtifact = new Artifact(
                "Gold-mounted Agate Rhinoceros Cup",
                "Shaanxi History Museum",
                "Tang Dynasty silk road national treasure, top cultural relic",
                5);
    }

    @Test
    void testConstructor() {
        assertEquals("Gold-mounted Agate Rhinoceros Cup", testArtifact.getName());
        assertEquals("Shaanxi History Museum", testArtifact.getMuseum());
        assertEquals("Tang Dynasty silk road national treasure, top cultural relic", testArtifact.getDescription());
        assertEquals(5, testArtifact.getRating());
    }

    @Test
    void testIsFiveStar() {
        assertTrue(testArtifact.isFiveStar());

        Artifact poorArtifact = new Artifact(
                "Houmuwu Ding",
                "National Museum of China, Beijing",
                "Largest and heaviest bronze sacrificial vessel of Shang Dynasty",
                4);
        assertFalse(poorArtifact.isFiveStar());
    }

    @Test
    void testBelongsToMuseum() {
        assertTrue(testArtifact.belongsToMuseum("Shaanxi History Museum"));
        assertFalse(testArtifact.belongsToMuseum("National Museum of China, Beijing"));
    }
}
