// Represents the entry point for the artifact management application.

package ui;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

//EFFECTS: starts the artifact console application
@ExcludeFromJacocoGeneratedReport
// This class is based on the TellerApp provided in CPSC 210.
public class Main {
    public static void main(String[] args) {
        new ArtifactConsole();
    }
}