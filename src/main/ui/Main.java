package ui;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

// EFFECTS: starts the Museum Memories graphical user interface
@ExcludeFromJacocoGeneratedReport
public class Main {

    // EFFECTS: starts the artifact console application; prints an error message if
    // a file cannot be found
    public static void main(String[] args) {
        new MuseumMemoriesGUI();
    }
}
