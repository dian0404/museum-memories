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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

// Code in this class is based in part on the AlarmSystem application, AlarmControllerUI.java file.
// Code in this class is based in part on the YouTube tutorial "JFrame | Java Swing Tutorial for Beginners".

// Represents the graphical user interface for Museum Memories.
@ExcludeFromJacocoGeneratedReport
public class MuseumMemoriesGUI extends JFrame {
    private static final int WIDTH = 1120;
    private static final int HEIGHT = 760;
    private static final int PHOTO_WIDTH = 430;
    private static final int PHOTO_HEIGHT = 280;
    private static final int EDIT_FIELD_COLUMNS = 28;
    private static final String JSON_STORE = "./data/artifacts.json";
    private static final String DEMO_JSON_STORE = "./data/demo-artifacts.json";
    private static final String IMAGE_DIRECTORY = "./data/images";
    private static final Font TITLE_FONT = new Font("Serif", Font.BOLD, 30);
    private static final Font SECTION_FONT = new Font("SansSerif", Font.BOLD, 18);
    private static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 15);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 13);
    private static final Font ARTIFACT_NAME_FONT = new Font("SansSerif", Font.BOLD, 14);
    private static final Font SECONDARY_FONT = new Font("SansSerif", Font.PLAIN, 12);
    private static final Color CARD_COLOR = new Color(255, 250, 244, 195);
    private static final Color CARD_BORDER = new Color(174, 151, 133);
    private static final Color ACCENT_COLOR = new Color(119, 72, 50);
    private static final Color SECONDARY_TEXT = new Color(99, 84, 74);
    private static final Color SELECTED_COLOR = new Color(226, 208, 193);
    private static final Color FORM_BACKGROUND = new Color(248, 243, 236);

    private ArtifactCollection artifactCollection;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JsonReader demoJsonReader;
    private DefaultListModel<Artifact> artifactListModel;
    private JList<Artifact> artifactList;
    private JLabel artifactListMessage;
    private JTextField searchField;
    private JLabel photoPreview;
    private JLabel detailName;
    private JLabel detailMuseum;
    private JLabel detailDate;
    private JLabel detailRating;
    private JTextArea detailNote;
    private JLabel statusLabel;
    private Timer statusResetTimer;

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
        artifactListMessage = new JLabel("0 memories");
        searchField = new JTextField();
        photoPreview = new JLabel();
        detailName = new JLabel();
        detailMuseum = new JLabel();
        detailDate = new JLabel();
        detailRating = new JLabel();
        detailNote = new JTextArea();
        statusLabel = new JLabel("Ready");
        statusResetTimer = new Timer(
                3000, event -> statusLabel.setText("Ready"));
        statusResetTimer.setRepeats(false);

        icon = new ImageIcon("./data/artifact-icon.png");
    }

    // MODIFIES: this
    // EFFECTS: configures the main application window
    private void configureFrame() {
        setTitle("Museum Memories");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(900, 650));
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

        JPanel listPanel = createCardPanel(new BorderLayout(0, 12));
        listPanel.add(createListHeader(), BorderLayout.NORTH);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel detailPanel = createCardPanel(new BorderLayout(0, 18));
        detailPanel.add(photoPreview, BorderLayout.NORTH);
        detailPanel.add(createDetailContent(), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                listPanel,
                detailPanel);

        splitPane.setDividerLocation(380);
        splitPane.setResizeWeight(0.34);
        splitPane.setDividerSize(12);
        splitPane.setOpaque(false);
        splitPane.setBorder(BorderFactory.createEmptyBorder(18, 18, 10, 18));

        add(splitPane, BorderLayout.CENTER);
    }

    // EFFECTS: returns the instruction and search controls above the list
    private JPanel createListHeader() {
        JPanel header = new JPanel(new BorderLayout(6, 12));
        header.setOpaque(false);

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        JLabel heading = new JLabel("Artifacts");
        heading.setFont(SECTION_FONT);
        heading.setForeground(ACCENT_COLOR);
        titleRow.add(heading, BorderLayout.WEST);
        titleRow.add(artifactListMessage, BorderLayout.EAST);
        header.add(titleRow, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new BorderLayout(6, 0));
        searchPanel.setOpaque(false);
        searchField.setFont(BODY_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)));
        searchField.setToolTipText("Search by name, museum, or personal note");
        searchField.addActionListener(event -> performSearch());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(createButton(new SearchArtifactAction()), BorderLayout.EAST);

        header.add(searchPanel, BorderLayout.SOUTH);
        return header;
    }

    // EFFECTS: returns a translucent card that keeps the background visible
    private JPanel createCardPanel(LayoutManager layout) {
        JPanel panel = new TranslucentPanel(layout, CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        return panel;
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
        artifactList.setFixedCellHeight(62);
        artifactList.setCellRenderer(new ArtifactListRenderer());
        artifactListMessage.setFont(SECONDARY_FONT);
        artifactListMessage.setForeground(SECONDARY_TEXT);

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
        photoPreview.setOpaque(true);
        photoPreview.setBackground(new Color(61, 48, 41));
        photoPreview.setForeground(Color.WHITE);
        photoPreview.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        photoPreview.setVisible(false);

        detailName.setFont(new Font("SansSerif", Font.BOLD, 22));
        detailMuseum.setFont(new Font("SansSerif", Font.BOLD, 14));
        detailMuseum.setForeground(ACCENT_COLOR);
        detailDate.setFont(BODY_FONT);
        detailDate.setForeground(SECONDARY_TEXT);
        detailRating.setFont(BODY_FONT);
        detailRating.setForeground(ACCENT_COLOR);
        detailNote.setEditable(false);
        detailNote.setOpaque(false);
        detailNote.setFont(BODY_FONT);
        detailNote.setForeground(SECONDARY_TEXT);
        detailNote.setLineWrap(true);
        detailNote.setWrapStyleWord(true);
        detailNote.setRows(5);
        clearArtifactDetails();
    }

    // EFFECTS: returns the structured detail area below the artifact photo
    private JPanel createDetailContent() {
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setOpaque(false);

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        detailMuseum.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailName.setAlignmentX(Component.LEFT_ALIGNMENT);
        heading.add(detailMuseum);
        heading.add(Box.createVerticalStrut(5));
        heading.add(detailName);

        JPanel metadata = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        metadata.setOpaque(false);
        metadata.add(detailDate);
        metadata.add(Box.createHorizontalStrut(24));
        metadata.add(detailRating);
        heading.add(Box.createVerticalStrut(12));
        heading.add(metadata);

        JPanel notePanel = new JPanel(new BorderLayout(0, 8));
        notePanel.setOpaque(false);
        JLabel noteHeading = new JLabel("Personal Note");
        noteHeading.setFont(SECTION_FONT);
        notePanel.add(noteHeading, BorderLayout.NORTH);
        JScrollPane noteScrollPane = new JScrollPane(detailNote);
        makeScrollPaneTransparent(noteScrollPane);
        noteScrollPane.setBorder(BorderFactory.createEmptyBorder());
        notePanel.add(noteScrollPane, BorderLayout.CENTER);

        content.add(heading, BorderLayout.NORTH);
        content.add(notePanel, BorderLayout.CENTER);
        return content;
    }

    // MODIFIES: this
    // EFFECTS: adds the action buttons to the window
    private void addButtonPanel() {
        JPanel bottomPanel = new TranslucentPanel(
                new BorderLayout(), new Color(238, 225, 210, 210));
        JPanel buttonPanel = new JPanel(new BorderLayout(12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 12, 12, 12));

        JPanel itemActions = createButtonGroup(FlowLayout.LEFT);
        itemActions.add(createPrimaryButton(new AddArtifactAction()));
        itemActions.add(createButton(new EditArtifactAction()));
        itemActions.add(createButton(new DeleteArtifactAction()));

        JPanel filters = createButtonGroup(FlowLayout.CENTER);
        filters.add(createButton(new ShowAllAction()));
        filters.add(createButton(new FiveStarAction()));

        JPanel dataActions = createButtonGroup(FlowLayout.RIGHT);
        dataActions.add(createButton(new LoadDemoAction()));
        dataActions.add(createButton(new LoadAction()));
        dataActions.add(createPrimaryButton(new SaveAction()));

        buttonPanel.add(itemActions, BorderLayout.WEST);
        buttonPanel.add(filters, BorderLayout.CENTER);
        buttonPanel.add(dataActions, BorderLayout.EAST);

        statusLabel.setFont(SECONDARY_FONT);
        statusLabel.setForeground(SECONDARY_TEXT);
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, CARD_BORDER),
                BorderFactory.createEmptyBorder(7, 18, 9, 18)));

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // EFFECTS: returns a transparent row for related buttons
    private JPanel createButtonGroup(int alignment) {
        JPanel group = new JPanel(new FlowLayout(alignment, 8, 0));
        group.setOpaque(false);
        return group;
    }

    // EFFECTS: returns a consistently styled action button
    private JButton createButton(AbstractAction action) {
        JButton button = new JButton(action);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setMargin(new java.awt.Insets(8, 14, 8, 14));
        return button;
    }

    // EFFECTS: returns a high-emphasis action button
    private JButton createPrimaryButton(AbstractAction action) {
        JButton button = createButton(action);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    // MODIFIES: this
    // EFFECTS: displays the given artifacts in the artifact list
    private void displayArtifacts(
            List<Artifact> artifacts) {

        artifactListModel.clear();

        if (artifacts.isEmpty()) {
            artifactListMessage.setText("No artifacts found.");
            clearArtifactDetails();
            displayArtifactImage(null);
        } else {
            artifactListMessage.setText(artifacts.size() + " memories");
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
            clearArtifactDetails();
            displayArtifactImage(null);
            return;
        }

        detailName.setText(artifact.getName());
        detailMuseum.setText(artifact.getMuseum());
        detailDate.setText("Visited  " + artifact.getVisitDate());
        detailRating.setText("Rating  " + createStarRating(artifact.getRating()));
        detailNote.setText(artifact.getPersonalNote());
        detailNote.setCaretPosition(0);
        displayArtifactImage(artifact);
    }

    // MODIFIES: this
    // EFFECTS: clears the structured detail area
    private void clearArtifactDetails() {
        detailName.setText("Select an artifact");
        detailMuseum.setText("");
        detailDate.setText("");
        detailRating.setText("");
        detailNote.setText("Choose an artifact from the list to view its story.");
    }

    // EFFECTS: returns a five-character filled and empty star rating
    private String createStarRating(int rating) {
        StringBuilder stars = new StringBuilder();

        for (int index = 1; index <= 5; index++) {
            stars.append(index <= rating ? "\u2605" : "\u2606");
        }

        return stars.toString();
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

    // Represents a two-line artifact entry in the collection list.
    @ExcludeFromJacocoGeneratedReport
    private class ArtifactListRenderer extends JPanel
            implements ListCellRenderer<Artifact> {
        private final JLabel nameLabel;
        private final JLabel metadataLabel;

        // EFFECTS: constructs an artifact list entry renderer
        ArtifactListRenderer() {
            super(new BorderLayout(0, 4));
            nameLabel = new JLabel();
            metadataLabel = new JLabel();
            nameLabel.setFont(ARTIFACT_NAME_FONT);
            metadataLabel.setFont(SECONDARY_FONT);
            metadataLabel.setForeground(SECONDARY_TEXT);
            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            add(nameLabel, BorderLayout.NORTH);
            add(metadataLabel, BorderLayout.SOUTH);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends Artifact> list,
                Artifact artifact,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
            nameLabel.setText(artifact.getName());
            metadataLabel.setText(artifact.getMuseum()
                    + "  \u00b7  " + artifact.getVisitDate());
            setOpaque(isSelected);
            setBackground(SELECTED_COLOR);
            return this;
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
        JTextField nameField = new JTextField(EDIT_FIELD_COLUMNS);
        JTextField museumField = new JTextField(EDIT_FIELD_COLUMNS);
        JTextField visitDateField = new JTextField(EDIT_FIELD_COLUMNS);
        JTextArea personalNoteField = createPersonalNoteArea("");
        JTextField ratingField = new JTextField(EDIT_FIELD_COLUMNS);
        PhotoEditState photoState = new PhotoEditState("");
        JPanel addPanel = createEditPanel(
                nameField,
                museumField,
                visitDateField,
                personalNoteField,
                ratingField,
                photoState);

        while (true) {
            boolean submitted = showArtifactFormDialog(
                    "Add Artifact",
                    "Record a museum object you want to remember.",
                    "Add Artifact",
                    addPanel);

            if (!submitted) {
                return null;
            }

            Artifact artifact = createArtifact(
                    nameField.getText(),
                    museumField.getText(),
                    ratingField.getText(),
                    visitDateField.getText(),
                    personalNoteField.getText());

            if (artifact != null && setNewArtifactPhoto(artifact, photoState)) {
                return artifact;
            }
        }
    }

    // MODIFIES: artifact
    // EFFECTS: imports and assigns the optional photo; returns false on failure
    private boolean setNewArtifactPhoto(
            Artifact artifact, PhotoEditState photoState) {
        String imagePath = photoState.resolveImagePath();

        if (photoState.hasUnimportedPhoto() && imagePath.isEmpty()) {
            return false;
        }

        artifact.setImagePath(imagePath);
        return true;
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
        JTextArea personalNoteField = createPersonalNoteArea(
                artifact.getPersonalNote());
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
            JTextArea personalNoteField,
            JTextField ratingField,
            PhotoEditState photoState) {

        nameField.setFont(BODY_FONT);
        museumField.setFont(BODY_FONT);
        visitDateField.setFont(BODY_FONT);
        ratingField.setFont(BODY_FONT);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(FORM_BACKGROUND);
        addFormRow(panel, "Name:", nameField, 0, false);
        addFormRow(panel, "Museum:", museumField, 1, false);
        addFormRow(panel, "Visit Date (YYYY-MM-DD):", visitDateField, 2, false);

        JScrollPane noteScrollPane = new JScrollPane(personalNoteField);
        noteScrollPane.setPreferredSize(new Dimension(360, 110));
        addFormRow(panel, "Personal Note:", noteScrollPane, 3, true);

        addFormRow(panel, "Rating (0-5):", ratingField, 4, false);
        addPhotoEditControls(panel, photoState, 5);
        return panel;
    }

    // EFFECTS: returns a wrapping multi-line field for personal notes
    private JTextArea createPersonalNoteArea(String text) {
        JTextArea textArea = new JTextArea(text, 4, EDIT_FIELD_COLUMNS);
        textArea.setFont(BODY_FONT);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return textArea;
    }

    // MODIFIES: panel
    // EFFECTS: adds controls for changing or removing the artifact photo
    private void addPhotoEditControls(
            JPanel panel, PhotoEditState photoState, int row) {
        JPanel controls = new JPanel(new BorderLayout(6, 4));
        controls.setBackground(FORM_BACKGROUND);
        JButton changeButton = new JButton("Change Photo");
        JButton removeButton = new JButton("Remove Photo");
        changeButton.setFont(BUTTON_FONT);
        removeButton.setFont(BUTTON_FONT);
        changeButton.addActionListener(event -> photoState.chooseNewPhoto());
        removeButton.addActionListener(event -> photoState.removePhoto());

        JPanel buttons = new JPanel(new GridLayout(1, 2, 6, 0));
        buttons.setBackground(FORM_BACKGROUND);
        buttons.add(changeButton);
        buttons.add(removeButton);
        controls.add(photoState.getStatusLabel(), BorderLayout.NORTH);
        controls.add(buttons, BorderLayout.CENTER);

        addFormRow(panel, "Photo:", controls, row, false);
    }

    // MODIFIES: panel
    // EFFECTS: adds one consistently aligned row to an artifact form
    private void addFormRow(
            JPanel panel,
            String labelText,
            Component field,
            int row,
            boolean expandsVertically) {
        JLabel label = new JLabel(labelText);
        label.setFont(BODY_FONT);
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.NORTHWEST;
        labelConstraints.insets = new java.awt.Insets(6, 4, 6, 14);
        panel.add(label, labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.weightx = 1.0;
        fieldConstraints.weighty = expandsVertically ? 1.0 : 0.0;
        fieldConstraints.fill = expandsVertically
                ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new java.awt.Insets(6, 4, 6, 4);
        panel.add(field, fieldConstraints);
    }

    // EFFECTS: shows a compact styled artifact form and returns true on submit
    private boolean showArtifactFormDialog(
            String title,
            String subtitle,
            String submitText,
            JPanel formPanel) {
        JDialog dialog = new JDialog(this, title, true);
        boolean[] submitted = {false};

        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(FORM_BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(24, 28, 22, 28));
        content.add(createFormHeading(title, subtitle), BorderLayout.NORTH);
        content.add(formPanel, BorderLayout.CENTER);
        content.add(createFormActions(
                dialog, submitText, submitted), BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        return submitted[0];
    }

    // EFFECTS: returns the title and supporting text for an artifact form
    private JPanel createFormHeading(String title, String subtitle) {
        JPanel heading = new JPanel();
        heading.setBackground(FORM_BACKGROUND);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(SECONDARY_FONT);
        subtitleLabel.setForeground(SECONDARY_TEXT);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        heading.add(titleLabel);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitleLabel);
        return heading;
    }

    // EFFECTS: returns the cancel and primary action row for an artifact form
    private JPanel createFormActions(
            JDialog dialog, String submitText, boolean[] submitted) {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setBackground(FORM_BACKGROUND);
        actions.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, CARD_BORDER),
                BorderFactory.createEmptyBorder(14, 0, 0, 0)));

        JButton cancelButton = new JButton("Cancel");
        styleDialogButton(cancelButton, false);
        cancelButton.addActionListener(event -> dialog.dispose());

        JButton submitButton = new JButton(submitText);
        styleDialogButton(submitButton, true);
        submitButton.addActionListener(event -> {
            submitted[0] = true;
            dialog.dispose();
        });
        dialog.getRootPane().setDefaultButton(submitButton);

        actions.add(cancelButton);
        actions.add(submitButton);
        return actions;
    }

    // MODIFIES: button
    // EFFECTS: applies the shared dialog button style
    private void styleDialogButton(JButton button, boolean primary) {
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setMargin(new java.awt.Insets(8, 18, 8, 18));

        if (primary) {
            button.setBackground(ACCENT_COLOR);
            button.setForeground(Color.WHITE);
            button.setOpaque(true);
            button.setBorderPainted(false);
        }
    }

    // MODIFIES: this, artifact
    // EFFECTS: repeatedly displays the edit form until saved or cancelled
    private void showEditDialog(
            Artifact artifact,
            JPanel editPanel,
            JTextField nameField,
            JTextField museumField,
            JTextField visitDateField,
            JTextArea personalNoteField,
            JTextField ratingField,
            PhotoEditState photoState) {

        while (true) {
            boolean submitted = showArtifactFormDialog(
                    "Edit Artifact",
                    "Update the details or photo for this memory.",
                    "Save Changes",
                    editPanel);

            if (!submitted) {
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
        statusLabel.setText(message);
        statusResetTimer.restart();
    }

    // EFFECTS: prints all logged events to the console
    private void printEventLog() {
        for (Event event : EventLog.getInstance()) {
            System.out.println(event);
        }
    }

    // Represents a rounded translucent panel over the museum background.
    @ExcludeFromJacocoGeneratedReport
    private class TranslucentPanel extends JPanel {
        private final Color fillColor;

        // EFFECTS: constructs a translucent panel with the given layout and color
        TranslucentPanel(LayoutManager layout, Color fillColor) {
            super(layout);
            this.fillColor = fillColor;
            setOpaque(false);
        }

        @Override
        // MODIFIES: graphics
        // EFFECTS: paints a smooth translucent card background
        protected void paintComponent(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(fillColor);
            graphics2D.fillRoundRect(
                    0, 0, getWidth(), getHeight(), 20, 20);
            graphics2D.dispose();
            super.paintComponent(graphics);
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
