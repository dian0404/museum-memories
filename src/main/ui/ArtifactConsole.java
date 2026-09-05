package ui;

import model.Artifact;
import model.ArtifactCollection;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

import java.io.IOException;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

// Code in this class is based on the Teller application and JsonSerializationDemo provided by the UBC CPSC 210 course.

// Represents the console-based user interface for managing an artifact collection.
@ExcludeFromJacocoGeneratedReport
public class ArtifactConsole {
    private static final String JSON_STORE = "./data/artifacts.json";

    private ArtifactCollection collection;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: constructs and runs the artifact application
    public ArtifactConsole() throws FileNotFoundException {
        collection = new ArtifactCollection();
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        init();
        runArtifactConsole();
    }

    // MODIFIES: this
    // EFFECTS: adds initial artifacts to the collection
    private void init() {
        collection.addArtifact(new Artifact(
                "Houmuwu Ding",
                "National Museum of China, Beijing",
                5,
                "",
                ""));

        collection.addArtifact(new Artifact(
                "Eagle-shaped Pottery Ding",
                "National Museum of China, Beijing",
                3,
                "",
                ""));

        collection.addArtifact(new Artifact(
                "Zeng Houyi Bianzhong",
                "Hubei Provincial Museum",
                5,
                "",
                ""));

        collection.addArtifact(new Artifact(
                "Sword of Goujian, King of Yue",
                "Hubei Provincial Museum",
                5,
                "",
                ""));

        collection.addArtifact(new Artifact(
                "Gilded Silver Pot with Dancing Horse",
                "Shaanxi History Museum",
                4,
                "",
                ""));

        collection.addArtifact(new Artifact(
                "Gold-inlaid Bronze Rhinoceros Zun",
                "National Museum of China, Beijing",
                4,
                "",
                ""));
    }

    // MODIFIES: this
    // EFFECTS: runs the artifact management loop
    private void runArtifactConsole() {
        boolean keepGoing = true;
        String command = null;

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("\nGoodbye!");
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add artifact");
        System.out.println("\tv -> view all artifacts");
        System.out.println("\tf -> view five-star artifacts");
        System.out.println("\ts -> search by museum");
        System.out.println("\tsave -> save artifact collection to file");
        System.out.println("\tload -> load artifact collection from file");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("a")) {
            addArtifact();
        } else if (command.equals("v")) {
            printArtifacts(collection.getArtifacts());
        } else if (command.equals("f")) {
            printArtifacts(collection.getFiveStarArtifacts());
        } else if (command.equals("s")) {
            searchByMuseum();
        } else if (command.equals("save")) {
            saveArtifactCollection();
        } else if (command.equals("load")) {
            loadArtifactCollection();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user to search by museum and prints results
    private void searchByMuseum() {
        Set<String> museumNames = collection.getMuseumNames();
        if (museumNames.isEmpty()) {
            System.out.println("No artifacts stored, so no museums found.");
            return;
        }

        System.out.println("Available museums:");
        for (String name : museumNames) {
            System.out.println("\t- " + name);
        }
        System.out.println();

        input.nextLine();
        System.out.print("Enter the museum name to search: ");
        String museumName = input.nextLine();

        List<Artifact> found = collection.getArtifactsByMuseum(museumName);
        printSearchResult(found, museumName);
    }

    // EFFECTS: prints the search result based on the size of the found list
    private void printSearchResult(List<Artifact> found, String museumName) {
        if (found.isEmpty()) {
            System.out.println("\nNo artifacts found in " + museumName + ".");
        } else {
            String msg = (found.size() == 1) ? "This is the artifact in " : "These are the artifacts in ";
            System.out.println("\n" + msg + museumName + ":");
            printArtifacts(found);
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user to add a new artifact
    private void addArtifact() {
        input.nextLine();

        System.out.print("Enter name: ");
        String name = input.nextLine();

        System.out.print("Enter museum: ");
        String museum = input.nextLine();

        System.out.print("Enter visit date: ");
        String visitDate = input.nextLine();

        System.out.print("Enter personal note: ");
        String personalNote = input.nextLine();

        int rating = -1;
        while (rating < 0 || rating > 5) {
            System.out.print("Enter rating (0-5): ");
            try {
                rating = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 0 and 5.");
            }
        }

        collection.addArtifact(new Artifact(
                name,
                museum,
                rating,
                visitDate,
                personalNote));

        System.out.println("Artifact added successfully!");
    }

    // EFFECTS: prints artifacts to the console
    private void printArtifacts(List<Artifact> artifacts) {
        if (artifacts.isEmpty()) {
            System.out.println("No artifacts found.");
        } else {
            for (Artifact a : artifacts) {
                System.out.println(a.getName() + " | " + a.getMuseum() + " | Rating: " + a.getRating());
            }
        }
    }

    // EFFECTS: saves the artifact collection to file
    private void saveArtifactCollection() {
        try {
            jsonWriter.open();
            jsonWriter.write(collection);
            jsonWriter.close();
            System.out.println(
                    "Saved artifact collection to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println(
                    "Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads the artifact collection from file
    private void loadArtifactCollection() {
        try {
            collection = jsonReader.read();
            System.out.println(
                    "Loaded artifact collection from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println(
                    "Unable to read from file: " + JSON_STORE);
        }
    }
}
