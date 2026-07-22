package ui;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

// This class is based on the Teller application provided in CPSC 210.

// Represents the entry point for the artifact management application
@ExcludeFromJacocoGeneratedReport
public class Main {
    // EFFECTS: starts the artifact console application
    public static void main(String[] args) {
        new ArtifactConsole();
    }
}