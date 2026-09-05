package ui;

import model.Artifact;
import model.ArtifactCollection;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

import model.Event;
import model.EventLog;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

// Code in this class is based in part on the AlarmSystem application, AlarmControllerUI.java file.
// Code in this class is based in part on the YouTube tutorial "JFrame | Java Swing Tutorial for Beginners".

// Represents the graphical user interface for Museum Memories.
@ExcludeFromJacocoGeneratedReport
public class MuseumMemoriesGUI extends JFrame {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 650;
    private static final int PHOTO_WIDTH = 320;
    private static final int PHOTO_HEIGHT = 210;
    private static final int EDIT_FIELD_COLUMNS = 28;
    private static final String JSON_STORE = "./data/artifacts.json";
    private static final String DEMO_JSON_STORE = "./data/demo-artifacts.json";
    private static final String IMAGE_DIRECTORY = "./data/images";
    private static final String FONT_FAMILY = "Serif";
    private static final Font TITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 30);
    private static final Font SECTION_FONT = new Font(FONT_FAMILY, Font.BOLD, 18);
    private static final Font BODY_FONT = new Font(FONT_FAMILY, Font.PLAIN, 16);
    private static final Font BUTTON_FONT = new Font(FONT_FAMILY, Font.BOLD, 14);

    private ArtifactCollection artifactCollection;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JsonReader demoJsonReader;
    private DefaultListModel<Artifact> artifactListModel;
    private JList<Artifact> artifactList;
    private JLabel artifactListMessage;
    private JTextField searchField;
    private JTextArea artifactDetails;
    private JLabel photoPreview;

    private static final String BACKGROUND_IMAGE = "./data/museum-background.png";

    private ImageIcon icon;

    // EFFECTS: constructs and displays the Museum Memories GUI
    public MuseumMemoriesGUI() {
        initializeFields();
        configureFrame();
        addTitlePanel();
        addArtifactDisplayPanel();
        addButtonPanel();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printEventLog();
                dispose();
            }
        });

        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes the fields used by the GUI
    private void initializeFields() {
        artifactCollection = new ArtifactCollection();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        demoJsonReader = new JsonReader(DEMO_JSON_STORE);
        artifactListModel = new DefaultListModel<>();
        artifactList = new JList<>(artifactListModel);
        artifactListMessage = new JLabel(
                "Select an artifact to view its details.");
        searchField = new JTextField();
        artifactDetails = new JTextArea();
        photoPreview = new JLabel();

        icon = new ImageIcon("./data/artifact-icon.png");
    }

    // MODIFIES: this
    // EFFECTS: configures the main application window
    private void configureFrame() {
        setTitle("Museum Memories");
        setSize(WIDTH, HEIGHT);
        setContentPane(new BackgroundPanel());
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
        title.setFont(TITLE_FONT);
        title.setHorizontalAlignment(
                SwingConstants.CENTER);

        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);
    }

    // MODIFIES: this
    // EFFECTS: adds the artifact list and detail area to the window
    private void addArtifactDisplayPanel() {
        configureArtifactList();
        configureArtifactDetails();

        JScrollPane listScrollPane = new JScrollPane(artifactList);
        makeScrollPaneTransparent(listScrollPane);
        listScrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setOpaque(false);
        listPanel.setBorder(createSectionBorder("Artifacts"));
        listPanel.add(createListHeader(), BorderLayout.NORTH);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        JScrollPane detailScrollPane = new JScrollPane(artifactDetails);
        makeScrollPaneTransparent(detailScrollPane);
        detailScrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.setOpaque(false);
        detailPanel.setBorder(createSectionBorder("Artifact Details"));
        detailPanel.add(photoPreview, BorderLayout.NORTH);
        detailPanel.add(detailScrollPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                listPanel,
                detailPanel);

        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.35);
        splitPane.setOpaque(false);

        add(splitPane, BorderLayout.CENTER);
    }

    // EFFECTS: returns the instruction and search controls above the list
    private JPanel createListHeader() {
        JPanel header = new JPanel(new BorderLayout(6, 6));
        header.setOpaque(false);
        header.add(artifactListMessage, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new BorderLayout(6, 0));
        searchPanel.setOpaque(false);
        searchField.setFont(BODY_FONT);
        searchField.setToolTipText("Search by name, museum, or personal note");
        searchField.addActionListener(event -> performSearch());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(createButton(new SearchArtifactAction()), BorderLayout.EAST);

        header.add(searchPanel, BorderLayout.SOUTH);
        return header;
    }

    // EFFECTS: returns a consistently styled section border
    private TitledBorder createSectionBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleFont(SECTION_FONT);
        return border;
    }

    // MODIFIES: scrollPane
    // EFFECTS: makes a scroll pane transparent so the background remains visible
    private void makeScrollPaneTransparent(JScrollPane scrollPane) {
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
    }

    // MODIFIES: this
    // EFFECTS: configures the selectable artifact list
    private void configureArtifactList() {
        artifactList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        artifactList.setFont(BODY_FONT);
        artifactList.setOpaque(false);
        artifactList.setCellRenderer(new TransparentArtifactRenderer());
        artifactListMessage.setFont(BODY_FONT);
        artifactListMessage.setBorder(
                BorderFactory.createEmptyBorder(4, 4, 10, 4));

        artifactList.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                displayArtifactDetails(
                        artifactList.getSelectedValue());
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: configures the artifact detail area
    private void configureArtifactDetails() {
        photoPreview.setHorizontalAlignment(SwingConstants.CENTER);
        photoPreview.setPreferredSize(
                new Dimension(PHOTO_WIDTH, PHOTO_HEIGHT));
        photoPreview.setFont(BODY_FONT);
        photoPreview.setVisible(false);

        artifactDetails.setEditable(false);
        artifactDetails.setOpaque(false);
        artifactDetails.setFont(BODY_FONT);
        artifactDetails.setLineWrap(true);
        artifactDetails.setWrapStyleWord(true);
        artifactDetails.setText("");
    }

    // MODIFIES: this
    // EFFECTS: adds the action buttons to the window
    private void addButtonPanel() {
        GridLayout layout = new GridLayout(1, 8, 8, 8);
        JPanel buttonPanel = new JPanel(layout);

        buttonPanel.setBackground(
                new Color(218, 203, 181));
        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 12, 12, 12));

        buttonPanel.add(createButton(new AddArtifactAction()));
        buttonPanel.add(createButton(new EditArtifactAction()));
        buttonPanel.add(createButton(new DeleteArtifactAction()));
        buttonPanel.add(createButton(new ShowAllAction()));
        buttonPanel.add(createButton(new FiveStarAction()));
        buttonPanel.add(createButton(new LoadDemoAction()));
        buttonPanel.add(createButton(new SaveAction()));
        buttonPanel.add(createButton(new LoadAction()));

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // EFFECTS: returns a consistently styled action button
    private JButton createButton(AbstractAction action) {
        JButton button = new JButton(action);
        button.setFont(BUTTON_FONT);
        return button;
    }

    // MODIFIES: this
    // EFFECTS: displays the given artifacts in the artifact list
    private void displayArtifacts(
            List<Artifact> artifacts) {

        artifactListModel.clear();

        if (artifacts.isEmpty()) {
            artifactListMessage.setText("No artifacts found.");
            artifactDetails.setText("");
            displayArtifactImage(null);
        } else {
            artifactListMessage.setText(
                    "Select an artifact to view its details.");
            for (Artifact artifact : artifacts) {
                artifactListModel.addElement(artifact);
            }
            artifactList.setSelectedIndex(0);
        }
    }

    // MODIFIES: this
    // EFFECTS: displays the details of the selected artifact
    private void displayArtifactDetails(Artifact artifact) {
        if (artifact == null) {
            artifactDetails.setText("");
            displayArtifactImage(null);
            return;
        }

        String details = "Name: "
                + artifact.getName()
                + "\n\nMuseum: "
                + artifact.getMuseum()
                + "\n\nVisit Date: "
                + artifact.getVisitDate()
                + "\n\nPersonal Note: "
                + artifact.getPersonalNote()
                + "\n\nRating: "
                + artifact.getRating()
                + "/5";

        artifactDetails.setText(details);
        artifactDetails.setCaretPosition(0);
        displayArtifactImage(artifact);
    }

    // MODIFIES: this
    // EFFECTS: displays a scaled preview of the artifact's local image
    private void displayArtifactImage(Artifact artifact) {
        photoPreview.setIcon(null);

        if (artifact == null) {
            photoPreview.setText("");
            photoPreview.setVisible(false);
            return;
        }

        photoPreview.setVisible(true);

        if (artifact.getImagePath().isBlank()) {
            photoPreview.setText("No photo added.");
            return;
        }

        File imageFile = new File(artifact.getImagePath());

        if (!imageFile.isFile()) {
            photoPreview.setText("Photo file not found.");
            return;
        }

        ImageIcon originalIcon = new ImageIcon(imageFile.getAbsolutePath());
        int originalWidth = originalIcon.getIconWidth();
        int originalHeight = originalIcon.getIconHeight();

        if (originalWidth <= 0 || originalHeight <= 0) {
            photoPreview.setText("Unable to display photo.");
            return;
        }

        double scale = Math.min(
                (double) PHOTO_WIDTH / originalWidth,
                (double) PHOTO_HEIGHT / originalHeight);
        int scaledWidth = (int) (originalWidth * scale);
        int scaledHeight = (int) (originalHeight * scale);
        Image scaledImage = originalIcon.getImage().getScaledInstance(
                scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

        photoPreview.setText("");
        photoPreview.setIcon(new ImageIcon(scaledImage));
    }

    // Represents a list cell that leaves the background visible when unselected.
    @ExcludeFromJacocoGeneratedReport
    private class TransparentArtifactRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            Component component = super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);

            setOpaque(isSelected);
            return component;
        }
    }

    // Stores pending photo changes while the edit dialog is open.
    @ExcludeFromJacocoGeneratedReport
    private class PhotoEditState {
        private final String originalImagePath;
        private final JLabel statusLabel;
        private File selectedFile;
        private boolean removeRequested;

        // EFFECTS: constructs photo edit state from the current image path
        PhotoEditState(String originalImagePath) {
            this.originalImagePath = originalImagePath;
            statusLabel = new JLabel(getPhotoName(originalImagePath));
            statusLabel.setFont(BODY_FONT);
        }

        // MODIFIES: this
        // EFFECTS: records a newly selected photo without importing it yet
        void chooseNewPhoto() {
            File file = chooseImageFile();

            if (file != null) {
                selectedFile = file;
                removeRequested = false;
                statusLabel.setText(file.getName());
            }
        }

        // MODIFIES: this
        // EFFECTS: marks the current photo for removal
        void removePhoto() {
            selectedFile = null;
            removeRequested = true;
            statusLabel.setText("No photo");
        }

        // EFFECTS: returns the status label displayed in the edit form
        JLabel getStatusLabel() {
            return statusLabel;
        }

        // EFFECTS: imports a selected photo or returns the requested path
        String resolveImagePath() {
            if (selectedFile != null) {
                return importSelectedImage(selectedFile);
            } else if (removeRequested) {
                return "";
            } else {
                return originalImagePath;
            }
        }

        // EFFECTS: returns true when a new photo still needs importing
        boolean hasUnimportedPhoto() {
            return selectedFile != null;
        }

        // EFFECTS: returns a display name for the current photo
        private String getPhotoName(String imagePath) {
            if (imagePath.isBlank()) {
                return "No photo";
            }

            return new File(imagePath).getName();
        }
    }

    // EFFECTS: prompts the user for artifact information;
    // returns an artifact, or null if input is cancelled
    private Artifact getArtifactFromUser() {
        String name = askForInput("Enter artifact name:");

        if (name == null) {
            return null;
        }

        String museum = askForInput("Enter museum name:");
        String visitDate = askForInput("Enter visit date (YYYY-MM-DD):");
        String personalNote = askForInput("Enter personal note:");
        String ratingText = askForInput("Enter rating from 0 to 5:");

        Artifact artifact = createArtifact(
                name,
                museum,
                ratingText,
                visitDate,
                personalNote);

        if (artifact != null) {
            artifact.setImagePath(chooseImagePath());
        }

        return artifact;
    }

    // EFFECTS: prompts the user to choose and import an optional image
    private String chooseImagePath() {
        File selectedFile = chooseImageFile();

        if (selectedFile == null) {
            return "";
        }

        return importSelectedImage(selectedFile);
    }

    // EFFECTS: prompts the user to choose an optional JPG or PNG image
    private File chooseImageFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Artifact Photo (Optional)");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Image files (JPG, JPEG, PNG)",
                "jpg", "jpeg", "png"));

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }

        return null;
    }

    // EFFECTS: copies an image into the application image directory and
    // returns its relative path; returns an empty string if copying fails
    private String importSelectedImage(File selectedFile) {
        try {
            Path imageDirectory = Paths.get(IMAGE_DIRECTORY);
            Files.createDirectories(imageDirectory);

            String extension = getFileExtension(selectedFile.getName());
            String fileName = UUID.randomUUID() + extension;
            Path destination = imageDirectory.resolve(fileName);

            Files.copy(selectedFile.toPath(), destination);
            return "data/images/" + fileName;
        } catch (IOException e) {
            showError("Unable to import the selected photo.");
            return "";
        }
    }

    // EFFECTS: returns the file extension, including the dot
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex < 0) {
            return "";
        }

        return fileName.substring(dotIndex).toLowerCase();
    }

    // MODIFIES: this
    // EFFECTS: shows a pre-filled form for editing the selected artifact
    private void editSelectedArtifact() {
        Artifact artifact = artifactList.getSelectedValue();

        if (artifact == null) {
            showError("Please select an artifact to edit.");
            return;
        }

        JTextField nameField = new JTextField(
                artifact.getName(), EDIT_FIELD_COLUMNS);
        JTextField museumField = new JTextField(
                artifact.getMuseum(), EDIT_FIELD_COLUMNS);
        JTextField visitDateField = new JTextField(
                artifact.getVisitDate(), EDIT_FIELD_COLUMNS);
        JTextField personalNoteField = new JTextField(
                artifact.getPersonalNote(), EDIT_FIELD_COLUMNS);
        JTextField ratingField = new JTextField(
                String.valueOf(artifact.getRating()), EDIT_FIELD_COLUMNS);
        PhotoEditState photoState = new PhotoEditState(
                artifact.getImagePath());
        JPanel editPanel = createEditPanel(
                nameField,
                museumField,
                visitDateField,
                personalNoteField,
                ratingField,
                photoState);

        showEditDialog(
                artifact,
                editPanel,
                nameField,
                museumField,
                visitDateField,
                personalNoteField,
                ratingField,
                photoState);
    }

    // EFFECTS: returns a form containing the editable artifact fields
    private JPanel createEditPanel(
            JTextField nameField,
            JTextField museumField,
            JTextField visitDateField,
            JTextField personalNoteField,
            JTextField ratingField,
            PhotoEditState photoState) {

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        addEditField(panel, "Name:", nameField);
        addEditField(panel, "Museum:", museumField);
        addEditField(panel, "Visit Date (YYYY-MM-DD):", visitDateField);
        addEditField(panel, "Personal Note:", personalNoteField);
        addEditField(panel, "Rating (0-5):", ratingField);
        addPhotoEditControls(panel, photoState);
        return panel;
    }

    // MODIFIES: panel
    // EFFECTS: adds controls for changing or removing the artifact photo
    private void addPhotoEditControls(
            JPanel panel, PhotoEditState photoState) {
        JLabel label = new JLabel("Photo:");
        label.setFont(BODY_FONT);

        JPanel controls = new JPanel(new BorderLayout(6, 4));
        JButton changeButton = new JButton("Change Photo");
        JButton removeButton = new JButton("Remove Photo");
        changeButton.setFont(BUTTON_FONT);
        removeButton.setFont(BUTTON_FONT);
        changeButton.addActionListener(event -> photoState.chooseNewPhoto());
        removeButton.addActionListener(event -> photoState.removePhoto());

        JPanel buttons = new JPanel(new GridLayout(1, 2, 6, 0));
        buttons.add(changeButton);
        buttons.add(removeButton);
        controls.add(photoState.getStatusLabel(), BorderLayout.NORTH);
        controls.add(buttons, BorderLayout.CENTER);

        panel.add(label);
        panel.add(controls);
    }

    // MODIFIES: panel
    // EFFECTS: adds a consistently styled label and field to the edit form
    private void addEditField(
            JPanel panel, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(BODY_FONT);
        field.setFont(BODY_FONT);
        panel.add(label);
        panel.add(field);
    }

    // MODIFIES: this, artifact
    // EFFECTS: repeatedly displays the edit form until saved or cancelled
    private void showEditDialog(
            Artifact artifact,
            JPanel editPanel,
            JTextField nameField,
            JTextField museumField,
            JTextField visitDateField,
            JTextField personalNoteField,
            JTextField ratingField,
            PhotoEditState photoState) {

        while (true) {
            int result = JOptionPane.showOptionDialog(
                    this,
                    editPanel,
                    "Edit Artifact",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    icon,
                    null,
                    null);

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            if (saveArtifactEdits(
                    artifact,
                    nameField.getText(),
                    museumField.getText(),
                    visitDateField.getText(),
                    personalNoteField.getText(),
                    ratingField.getText(),
                    photoState)) {
                return;
            }
        }
    }

    // MODIFIES: this, artifact
    // EFFECTS: validates and saves edits; returns true when successful
    private boolean saveArtifactEdits(
            Artifact artifact,
            String name,
            String museum,
            String visitDate,
            String personalNote,
            String ratingText,
            PhotoEditState photoState) {

        if (!areArtifactFieldsValid(name, museum, visitDate)) {
            return false;
        }

        try {
            int rating = Integer.parseInt(ratingText.trim());

            if (rating < 0 || rating > 5) {
                showError("Rating must be between 0 and 5.");
                return false;
            }

            String imagePath = photoState.resolveImagePath();

            if (photoState.hasUnimportedPhoto() && imagePath.isEmpty()) {
                return false;
            }

            return applyArtifactEdits(
                    artifact,
                    name.trim(),
                    museum.trim(),
                    rating,
                    visitDate.trim(),
                    personalNote.trim(),
                    imagePath);
        } catch (NumberFormatException e) {
            showError("Rating must be a whole number.");
            return false;
        }
    }

    // MODIFIES: this, artifact
    // EFFECTS: applies valid edits and refreshes the list and detail view
    private boolean applyArtifactEdits(
            Artifact artifact,
            String name,
            String museum,
            int rating,
            String visitDate,
            String personalNote,
            String imagePath) {

        boolean updated = artifactCollection.updateArtifact(
                artifact, name, museum, rating, visitDate, personalNote,
                imagePath);

        if (updated) {
            artifactList.repaint();
            artifactList.setSelectedValue(artifact, true);
            displayArtifactDetails(artifact);
            showInformation("Artifact updated successfully!");
        }

        return updated;
    }

    // EFFECTS: returns true if required text and date fields are valid
    private boolean areArtifactFieldsValid(
            String name, String museum, String visitDate) {
        if (name.isBlank() || museum.isBlank()) {
            showError("Name and museum are required.");
            return false;
        }

        try {
            LocalDate.parse(visitDate.trim());
            return true;
        } catch (DateTimeParseException e) {
            showError("Visit date must use YYYY-MM-DD format.");
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: displays artifacts matching the current search text
    private void performSearch() {
        displayArtifacts(
                artifactCollection.searchArtifacts(searchField.getText()));
    }

    // MODIFIES: this
    // EFFECTS: confirms and deletes the selected artifact
    private void deleteSelectedArtifact() {
        Artifact artifact = artifactList.getSelectedValue();

        if (artifact == null) {
            showError("Please select an artifact to delete.");
            return;
        }

        int result = JOptionPane.showOptionDialog(
                this,
                "Delete \"" + artifact.getName() + "\"?",
                "Delete Artifact",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                icon,
                null,
                null);

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        int selectedIndex = artifactList.getSelectedIndex();

        if (artifactCollection.removeArtifact(artifact)) {
            artifactListModel.removeElement(artifact);
            selectArtifactAfterDeletion(selectedIndex);
            showInformation("Artifact deleted successfully!");
        }
    }

    // MODIFIES: this
    // EFFECTS: selects a nearby artifact or clears the detail view
    private void selectArtifactAfterDeletion(int deletedIndex) {
        if (artifactListModel.isEmpty()) {
            artifactListMessage.setText("No artifacts found.");
            displayArtifactDetails(null);
            return;
        }

        int nextIndex = Math.min(deletedIndex, artifactListModel.size() - 1);
        artifactList.setSelectedIndex(nextIndex);
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
            String ratingText,
            String visitDate,
            String personalNote) {

        if (museum == null
                || ratingText == null
                || visitDate == null
                || personalNote == null) {
            return null;
        }

        if (!areArtifactFieldsValid(name, museum, visitDate)) {
            return null;
        }

        return createArtifactWithRating(
                name.trim(),
                museum.trim(),
                ratingText,
                visitDate.trim(),
                personalNote.trim());
    }

    // EFFECTS: parses the rating and creates an artifact;
    // returns null if the rating is not a whole number
    private Artifact createArtifactWithRating(
            String name,
            String museum,
            String ratingText,
            String visitDate,
            String personalNote) {

        try {
            int rating = Integer.parseInt(ratingText);

            return createArtifactIfRatingValid(
                    name,
                    museum,
                    rating,
                    visitDate,
                    personalNote);
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
            int rating,
            String visitDate,
            String personalNote) {

        if (rating < 0 || rating > 5) {
            showError(
                    "Rating must be between 0 and 5.");
            return null;
        }

        return new Artifact(
                name,
                museum,
                rating,
                visitDate,
                personalNote);
    }

    // EFFECTS: displays an error message
    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Input Error",
                JOptionPane.ERROR_MESSAGE,
                icon);
    }

    // EFFECTS: displays an information message
    private void showInformation(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Museum Memories",
                JOptionPane.INFORMATION_MESSAGE,
                icon);
    }

    // EFFECTS: prints all logged events to the console
    private void printEventLog() {
        for (Event event : EventLog.getInstance()) {
            System.out.println(event);
        }
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
            super("Add Artifact");
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
                artifactList.setSelectedValue(artifact, true);

                showInformation(
                        "Artifact added successfully!");
            }
        }
    }

    // Represents the action for editing the selected artifact.
    @ExcludeFromJacocoGeneratedReport
    private class EditArtifactAction extends AbstractAction {

        // EFFECTS: constructs an Edit Artifact action
        EditArtifactAction() {
            super("Edit");
        }

        @Override
        // MODIFIES: MuseumMemoriesGUI.this
        // EFFECTS: edits the selected artifact
        public void actionPerformed(ActionEvent event) {
            editSelectedArtifact();
        }
    }

    // Represents the action for deleting the selected artifact.
    @ExcludeFromJacocoGeneratedReport
    private class DeleteArtifactAction extends AbstractAction {

        // EFFECTS: constructs a Delete Artifact action
        DeleteArtifactAction() {
            super("Delete");
        }

        @Override
        // MODIFIES: MuseumMemoriesGUI.this
        // EFFECTS: deletes the selected artifact after confirmation
        public void actionPerformed(ActionEvent event) {
            deleteSelectedArtifact();
        }
    }

    // Represents the action for searching artifacts.
    @ExcludeFromJacocoGeneratedReport
    private class SearchArtifactAction extends AbstractAction {

        // EFFECTS: constructs a Search Artifact action
        SearchArtifactAction() {
            super("Search");
        }

        @Override
        // MODIFIES: MuseumMemoriesGUI.this
        // EFFECTS: displays artifacts matching the search text
        public void actionPerformed(ActionEvent event) {
            performSearch();
        }
    }

    // Represents the action for displaying all artifacts.
    @ExcludeFromJacocoGeneratedReport
    private class ShowAllAction
            extends AbstractAction {

        // EFFECTS: constructs a Show All action
        ShowAllAction() {
            super("Show All");
        }

        @Override
        // MODIFIES: MuseumMemoriesGUI.this
        // EFFECTS: displays all stored artifacts
        public void actionPerformed(
                ActionEvent event) {

            searchField.setText("");
            displayArtifacts(
                    artifactCollection.viewAllArtifacts());
        }
    }

    // Represents the action for displaying five-star artifacts.
    @ExcludeFromJacocoGeneratedReport
    private class FiveStarAction
            extends AbstractAction {

        // EFFECTS: constructs a Five Stars action
        FiveStarAction() {
            super("Five Stars");
        }

        @Override
        // MODIFIES: MuseumMemoriesGUI.this
        // EFFECTS: displays all five-star artifacts
        public void actionPerformed(
                ActionEvent event) {

            searchField.setText("");
            displayArtifacts(
                    artifactCollection
                            .getFiveStarArtifacts());
        }
    }

    // Represents the action for loading the public demo collection.
    @ExcludeFromJacocoGeneratedReport
    private class LoadDemoAction extends AbstractAction {

        // EFFECTS: constructs a Load Demo action
        LoadDemoAction() {
            super("Demo");
        }

        @Override
        // MODIFIES: MuseumMemoriesGUI.this
        // EFFECTS: loads and displays the public demo collection
        public void actionPerformed(ActionEvent event) {
            try {
                artifactCollection = demoJsonReader.read();
                artifactCollection.logCollectionLoaded();
                searchField.setText("");
                displayArtifacts(artifactCollection.getArtifacts());
                showInformation("Demo collection loaded successfully!");
            } catch (IOException e) {
                showError("Unable to read from " + DEMO_JSON_STORE);
            }
        }
    }

    // Represents the action for saving the collection.
    @ExcludeFromJacocoGeneratedReport
    private class SaveAction
            extends AbstractAction {

        // EFFECTS: constructs a Save action
        SaveAction() {
            super("Save");
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
            super("Load");
        }

        @Override
        // MODIFIES: MuseumMemoriesGUI.this
        // EFFECTS: loads and displays the collection
        public void actionPerformed(
                ActionEvent event) {

            try {
                artifactCollection = jsonReader.read();
                artifactCollection.logCollectionLoaded();
                searchField.setText("");

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
