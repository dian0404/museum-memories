package ui;

import java.io.FileNotFoundException;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

// This class is based on the Teller application provided in CPSC 210.

// Represents the entry point for the artifact management application
@ExcludeFromJacocoGeneratedReport
public class Main {

    // EFFECTS: starts the artifact console application; prints an error message if a file cannot be found
    public static void main(String[] args) {
        try {
            new ArtifactConsole();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}