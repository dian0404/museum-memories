package ui;

import model.Artifact;
import model.ArtifactCollection;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.ImageIcon;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

// Code in this class is based in part on the AlarmSystem application, AlarmControllerUI.java file.
// Code in this class is based in part on the YouTube tutorial "JFrame | Java Swing Tutorial for Beginners".

// Represents the graphical user interface for Museum Memories.
@ExcludeFromJacocoGeneratedReport
public class MuseumMemoriesGUI extends JFrame {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 650;
    private static final String JSON_STORE = "./data/artifacts.json";

    private ArtifactCollection artifactCollection;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JTextArea artifactDisplay;

    private static final String BACKGROUND_IMAGE = "./data/museum-background.png";

    private ImageIcon icon;

    // EFFECTS: constructs and displays the Museum Memories GUI
    public MuseumMemoriesGUI() {
        initializeFields();
        configureFrame();
        addTitlePanel();
        addArtifactDisplayPanel();
        addButtonPanel();

        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes the fields used by the GUI
    private void initializeFields() {
        artifactCollection = new ArtifactCollection();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        artifactDisplay = new JTextArea();

        icon = new ImageIcon("./data/artifact-icon.png");
    }

    // MODIFIES: this
    // EFFECTS: configures the main application window
    private void configureFrame() {
        setTitle("Museum Memories");
        setSize(WIDTH, HEIGHT);
        setContentPane(new BackgroundPanel());
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // MODIFIES: this
    // EFFECTS: adds the title panel to the window
    private void addTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(
                new Color(67, 48, 38));
        titlePanel.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 10, 18, 10));

        JLabel title = new JLabel("MUSEUM MEMORIES");
        title.setForeground(Color.WHITE);
        title.setFont(
                new Font("Serif", Font.BOLD, 30));
        title.setHorizontalAlignment(
                SwingConstants.CENTER);

        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);
    }

    // MODIFIES: this
    // EFFECTS: adds the display area with a titled border to the centre of the
    // window
    // configures the artifact display area to be non-editable, transparent,
    // word-wrapped, and scrollable;
    private void addArtifactDisplayPanel() {
        artifactDisplay.setEditable(false);
        artifactDisplay.setOpaque(false);
        artifactDisplay.setForeground(Color.BLACK);
        artifactDisplay.setFont(
                new Font("Serif", Font.PLAIN, 16));
        artifactDisplay.setLineWrap(true);
        artifactDisplay.setWrapStyleWord(true);
        artifactDisplay.setText(
                "No artifacts are currently displayed.");

        JScrollPane scrollPane = new JScrollPane(artifactDisplay);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                "Artifact Collection");
        titledBorder.setTitleFont(
                new Font("Serif", Font.BOLD, 20));

        scrollPane.setBorder(titledBorder);
        add(scrollPane, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: adds the action buttons to the window
    private void addButtonPanel() {
        GridLayout layout = new GridLayout(1, 5, 8, 8);
        JPanel buttonPanel = new JPanel(layout);

        buttonPanel.setBackground(
                new Color(218, 203, 181));
        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 12, 12, 12));

        buttonPanel.add(
                new JButton(new AddArtifactAction()));
        buttonPanel.add(
                new JButton(new ShowAllAction()));
        buttonPanel.add(
                new JButton(new FiveStarAction()));
        buttonPanel.add(
                new JButton(new SaveAction()));
        buttonPanel.add(
                new JButton(new LoadAction()));

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: displays the given artifacts
    private void displayArtifacts(
            List<Artifact> artifacts) {

        artifactDisplay.setText("");

        if (artifacts.isEmpty()) {
            artifactDisplay.setText(
                    "No artifacts found.");
        } else {
            for (Artifact artifact : artifacts) {
                appendArtifact(artifact);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: appends one artifact to the display area
    private void appendArtifact(Artifact artifact) {
        artifactDisplay.append(
                "Name: "
                        + artifact.getName()
                        + "\n");

        artifactDisplay.append(
                "Museum: "
                        + artifact.getMuseum()
                        + "\n");

        artifactDisplay.append(
                "Description: "
                        + artifact.getDescription()
                        + "\n");

        artifactDisplay.append(
                "Rating: "
                        + artifact.getRating()
                        + "/5\n\n");
    }

    // EFFECTS: prompts the user for artifact information;
    // returns an artifact, or null if input is cancelled
    private Artifact getArtifactFromUser() {
        String name = askForInput("Enter artifact name:");

        if (name == null) {
            return null;
        }

        String museum = askForInput("Enter museum name:");
        String description = askForInput("Enter description:");
        String ratingText = askForInput("Enter rating from 0 to 5:");

        return createArtifact(
                name,
                museum,
                description,
                ratingText);
    }

    // EFFECTS: displays a dialog and returns user input
    private String askForInput(String message) {
        return (String) JOptionPane.showInputDialog(
                this,
                message,
                "Add Artifact",
                JOptionPane.PLAIN_MESSAGE,
                icon,
                null,
                null);
    }

    // EFFECTS: creates an artifact from the given input;
    // returns null if input is missing or invalid
    private Artifact createArtifact(
            String name,
            String museum,
            String description,
            String ratingText) {

        if (museum == null
                || description == null
                || ratingText == null) {
            return null;
        }

        return createArtifactWithRating(
                name,
                museum,
                description,
                ratingText);
    }

    // EFFECTS: parses the rating and creates an artifact;
    // returns null if the rating is not a whole number
    private Artifact createArtifactWithRating(
            String name,
            String museum,
            String description,
            String ratingText) {

        try {
            int rating = Integer.parseInt(ratingText);

            return createArtifactIfRatingValid(
                    name,
                    museum,
                    description,
                    rating);
        } catch (NumberFormatException e) {
            showError(
                    "Rating must be a whole number.");
            return null;
        }
    }

    // EFFECTS: creates an artifact if the rating is valid;
    // returns null otherwise
    private Artifact createArtifactIfRatingValid(
            String name,
            String museum,
            String description,
            int rating) {

        if (rating < 0 || rating > 5) {
            showError(
                    "Rating must be between 0 and 5.");
            return null;
        }

        return new Artifact(
                name,
                museum,
                description,
                rating);
    }

    // EFFECTS: displays an error message
    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // EFFECTS: displays an information message
    private void showInformation(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Museum Memories",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Represents a panel with a museum background image.
    @ExcludeFromJacocoGeneratedReport
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        // EFFECTS: constructs a panel with a background image
        BackgroundPanel() {
            ImageIcon imageIcon = new ImageIcon(BACKGROUND_IMAGE);
            backgroundImage = imageIcon.getImage();
        }

        @Override
        // MODIFIES: graphics
        // EFFECTS: paints the background image on this panel
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            graphics.drawImage(
                    backgroundImage,
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    this);
        }
    }

    // Represents the action for adding an artifact.
    @ExcludeFromJacocoGeneratedReport
    private class AddArtifactAction
            extends AbstractAction {

        // EFFECTS: constructs an Add Artifact action
        AddArtifactAction() {
            super("🏺 Add Artifact");
        }

        @Override
        // MODIFIES: MuseumMemoriesGUI.this
        // EFFECTS: prompts for and adds an artifact
        public void actionPerformed(
                ActionEvent event) {

            Artifact artifact = getArtifactFromUser();

            if (artifact != null) {
                artifactCollection.addArtifact(
                        artifact);

                displayArtifacts(
                        artifactCollection 
                                .getArtifacts());

                showInformation(
                        "Artifact added successfully!");
            }
        }
    }

    // Represents the action for displaying all artifacts.
    @ExcludeFromJacocoGeneratedReport
    private class ShowAllAction
            extends AbstractAction {

        // EFFECTS: constructs a Show All action
        ShowAllAction() {
            super("🗂️ Show All");
        }

        @Override
        // MODIFIES: MuseumMemoriesGUI.this
        // EFFECTS: displays all stored artifacts
        public void actionPerformed(
                ActionEvent event) {

            displayArtifacts(
                    artifactCollection.getArtifacts());
        }
    }

    // Represents the action for displaying five-star artifacts.
    @ExcludeFromJacocoGeneratedReport
    private class FiveStarAction
            extends AbstractAction {

        // EFFECTS: constructs a Five Stars action
        FiveStarAction() {
            super("⭐️ Five Stars");
        }

        @Override
        // MODIFIES: MuseumMemoriesGUI.this
        // EFFECTS: displays all five-star artifacts
        public void actionPerformed(
                ActionEvent event) {

            displayArtifacts(
                    artifactCollection
                            .getFiveStarArtifacts());
        }
    }

    // Represents the action for saving the collection.
    @ExcludeFromJacocoGeneratedReport
    private class SaveAction
            extends AbstractAction {

        // EFFECTS: constructs a Save action
        SaveAction() {
            super("💾 Save");
        }

        @Override
        // EFFECTS: saves the artifact collection to file
        public void actionPerformed(
                ActionEvent event) {

            try {
                jsonWriter.open();
                jsonWriter.write(artifactCollection);
                jsonWriter.close();

                showInformation(
                        "Artifact collection " + "saved successfully!");
            } catch (FileNotFoundException e) {
                showError(
                        "Unable to write to "
                                + JSON_STORE);
            }
        }
    }

    // Represents the action for loading the collection.
    @ExcludeFromJacocoGeneratedReport
    private class LoadAction
            extends AbstractAction {

        // EFFECTS: constructs a Load action
        LoadAction() {
            super("📂 Load");
        }

        @Override
        // MODIFIES: MuseumMemoriesGUI.this
        // EFFECTS: loads and displays the collection
        public void actionPerformed(
                ActionEvent event) {

            try {
                artifactCollection = jsonReader.read();

                displayArtifacts(
                        artifactCollection
                                .getArtifacts());

                showInformation(
                        "Artifact collection " + "loaded successfully!");
            } catch (IOException e) {
                showError(
                        "Unable to read from "
                                + JSON_STORE);
            }
        }
    }
}