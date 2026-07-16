package ui;

import model.Artifact;
import model.ArtifactCollection;
import java.util.Scanner;
import java.util.List;
import java.util.Set;

    // Represents the console-based user interface for managing an artifact collection.
public class ArtifactConsole {
    private ArtifactCollection collection;
    private Scanner input;

    // EFFECTS: runs the artifact application
    public ArtifactConsole() {
        collection = new ArtifactCollection();
        input = new Scanner(System.in);
        init();
        runArtifactConsole();
    }

    // MODIFIES: this
    // EFFECTS: initializes the collection and scanner
    private void init() {
        collection.addArtifact(new Artifact("Houmuwu Ding", "National Museum of China, Beijing",
                "It is a huge bronze sacrificial vessel from the Shang Dynasty unearthed in Anyang.",
                5));
        collection.addArtifact(new Artifact("Eagle-shaped Pottery Ding", "National Museum of China, Beijing",
                "This pottery work dates back over 6,000 years to the Yangshao Neolithic culture.",
                3));
        collection.addArtifact(new Artifact("Zeng Houyi Bianzhong", "Hubei Provincial Museum",
                "This is a complete set of chime bells buried with Marquis Zeng of the Warring States Period.",
                5));
        collection.addArtifact(new Artifact("Sword of Goujian, King of Yue", "Hubei Provincial Museum",
                "This bronze sword belonged to Goujian, king of Yue during the Spring and Autumn Period.",
                5));
        collection.addArtifact(new Artifact("Gilded Silver Pot with Dancing Horse", "Shaanxi History Museum",
                "This silver pot records the grand horse-dancing performance held in the Tang imperial palace.",
                4));
        collection.addArtifact(new Artifact("Gold-inlaid Bronze Rhinoceros Zun", "National Museum of China, Beijing",
                "It is a Western Han wine container shaped like a real rhinoceros covered with gold cloud patterns.", 
                4));
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
        System.out.print("Enter description: ");
        String description = input.nextLine();

        int rating = -1;
        while (rating < 0 || rating > 5) {
            System.out.print("Enter rating (0-5): ");
            try {
                rating = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 0 and 5.");
            }
        }

        collection.addArtifact(new Artifact(name, museum, description, rating));
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
}