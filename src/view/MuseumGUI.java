package view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import presenter.IMuseumGUI;
import presenter.MuseumPresenter;

import java.awt.Desktop;
import java.net.URI;
import java.util.List;

public class MuseumGUI extends Application implements IMuseumGUI {
    private TextField txtArtistName, txtArtistBirthDate, txtArtistBirthPlace, txtArtistNationality, txtArtistPhoto;
    private TextField txtArtworkTitle, txtArtworkArtistId, txtArtworkType, txtArtworkDescription, txtArtworkImage1, txtArtworkImage2, txtArtworkImage3;
    private Button btnAddArtist, btnUpdateArtist, btnDeleteArtist, btnSearchArtist;
    private Button btnAddArtwork, btnUpdateArtwork, btnDeleteArtwork, btnSearchArtwork;
    private Button btnSaveArtworksToCSV, btnSaveArtworksToDOC;
    private Button btnLoadArtists, btnLoadArtworks;
    private TableView<Object[]> tblArtists;
    private TableView<Object[]> tblArtworks;
    private ComboBox<String> cbArtistFilter, cbArtworkFilter;
    private TextField txtFilterArtist, txtFilterArtworkType;
    private Button btnFilterArtworks;
    private ListView<String> lstArtistArtworks;
    private ObservableList<String> artistArtworksListModel;

    private MuseumPresenter museumPresenter;

    @Override
    public void start(Stage primaryStage) {
        // Configure stage
        primaryStage.setTitle("Museum Management System");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);

        // Initialize components
        initComponents();

        // Create main layout - change to SplitPane for horizontal division
        SplitPane mainSplitPane = new SplitPane();
        mainSplitPane.setOrientation(javafx.geometry.Orientation.HORIZONTAL);

        BorderPane leftPane = new BorderPane();
        leftPane.setTop(createFilterPanel());
        leftPane.setCenter(createArtistPanel());

        // Add components to layout
        mainSplitPane.getItems().addAll(leftPane, createArtworkPanel());
        mainSplitPane.setDividerPositions(0.5);

        // Bottom panel for global buttons
        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(mainSplitPane);
        mainLayout.setBottom(createButtonPanel());

        // Create scene
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);

        // Initialize Presenter
        museumPresenter = new MuseumPresenter(this);

        // Register listeners
        registerListeners();

        // Show stage
        primaryStage.show();
    }

    private void initComponents() {
        // Text fields for artist
        txtArtistName = new TextField();
        txtArtistBirthDate = new TextField();
        txtArtistBirthPlace = new TextField();
        txtArtistNationality = new TextField();
        txtArtistPhoto = new TextField();

        // Text fields for artwork
        txtArtworkTitle = new TextField();
        txtArtworkArtistId = new TextField();
        txtArtworkType = new TextField();
        txtArtworkDescription = new TextField();
        txtArtworkImage1 = new TextField();
        txtArtworkImage2 = new TextField();
        txtArtworkImage3 = new TextField();

        // Buttons
        btnAddArtist = new Button("Add Artist");
        btnUpdateArtist = new Button("Update Artist");
        btnDeleteArtist = new Button("Delete Artist");
        btnSearchArtist = new Button("Search Artist");

        btnAddArtwork = new Button("Add Artwork");
        btnUpdateArtwork = new Button("Update Artwork");
        btnDeleteArtwork = new Button("Delete Artwork");
        btnSearchArtwork = new Button("Search Artwork");

        btnSaveArtworksToCSV = new Button("Save to CSV");
        btnSaveArtworksToDOC = new Button("Save to DOC");

        // Buttons for loading data
        btnLoadArtists = new Button("Load Artists");
        btnLoadArtworks = new Button("Load Artworks");

        // Tables
        tblArtists = new TableView<>();
        tblArtworks = new TableView<>();

        // ComboBoxes for filtering
        cbArtistFilter = new ComboBox<>(FXCollections.observableArrayList("All", "By Name", "By Nationality"));
        cbArtworkFilter = new ComboBox<>(FXCollections.observableArrayList("All", "By Title", "By Type"));

        // Filter components
        txtFilterArtist = new TextField();
        txtFilterArtist.setPrefWidth(150);
        txtFilterArtworkType = new TextField();
        txtFilterArtworkType.setPrefWidth(150);
        btnFilterArtworks = new Button("Apply Filters");

        // Artist artworks list
        artistArtworksListModel = FXCollections.observableArrayList();
        lstArtistArtworks = new ListView<>(artistArtworksListModel);
    }

    private void registerListeners() {
        // Artist buttons
        btnAddArtist.setOnAction(e -> addArtistButtonClicked());
        btnUpdateArtist.setOnAction(e -> updateArtistButtonClicked());
        btnDeleteArtist.setOnAction(e -> deleteArtistButtonClicked());
        btnSearchArtist.setOnAction(e -> searchArtistButtonClicked());

        // Artwork buttons
        btnAddArtwork.setOnAction(e -> addArtworkButtonClicked());
        btnUpdateArtwork.setOnAction(e -> updateArtworkButtonClicked());
        btnDeleteArtwork.setOnAction(e -> deleteArtworkButtonClicked());
        btnSearchArtwork.setOnAction(e -> searchArtworkButtonClicked());

        // Save buttons
        btnSaveArtworksToCSV.setOnAction(e -> saveArtworksToCSVButtonClicked());
        btnSaveArtworksToDOC.setOnAction(e -> saveArtworksToDOCButtonClicked());

        // Load buttons
        btnLoadArtists.setOnAction(e -> loadArtistsButtonClicked());
        btnLoadArtworks.setOnAction(e -> loadArtworksButtonClicked());

        // Filter button
        btnFilterArtworks.setOnAction(e -> filterArtworksButtonClicked());

        // Table selection listeners
        tblArtists.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtArtistName.setText((String) newSelection[1]);
                txtArtistBirthDate.setText((String) newSelection[2]);
                txtArtistBirthPlace.setText((String) newSelection[3]);
                txtArtistNationality.setText((String) newSelection[4]);
                txtArtistPhoto.setText((String) newSelection[5]);

                // Load artist's artworks
                loadArtistArtworks((Integer) newSelection[0]);
            }
        });

        tblArtworks.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtArtworkArtistId.setText(String.valueOf(newSelection[1]));
                txtArtworkTitle.setText((String) newSelection[2]);
                txtArtworkType.setText((String) newSelection[3]);
                txtArtworkDescription.setText((String) newSelection[4]);
                txtArtworkImage1.setText((String) newSelection[5]);
                txtArtworkImage2.setText((String) newSelection[6]);
                txtArtworkImage3.setText((String) newSelection[7]);

                // Open images in browser
                openImagesInBrowser((String) newSelection[5], (String) newSelection[6], (String) newSelection[7]);
            }
        });
    }

    private BorderPane createArtistPanel() {
        BorderPane panel = new BorderPane();
        panel.setPadding(new Insets(10));

        // Create a split pane for form and table
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);

        // Artist form
        GridPane formPanel = new GridPane();
        formPanel.setHgap(10);
        formPanel.setVgap(5);
        formPanel.setPadding(new Insets(10));

        formPanel.add(new Label("Name:"), 0, 0);
        formPanel.add(txtArtistName, 1, 0);
        formPanel.add(new Label("Birth Date:"), 0, 1);
        formPanel.add(txtArtistBirthDate, 1, 1);
        formPanel.add(new Label("Birth Place:"), 0, 2);
        formPanel.add(txtArtistBirthPlace, 1, 2);
        formPanel.add(new Label("Nationality:"), 0, 3);
        formPanel.add(txtArtistNationality, 1, 3);
        formPanel.add(new Label("Photo:"), 0, 4);
        formPanel.add(txtArtistPhoto, 1, 4);

        // Artist buttons
        HBox buttonPanel = new HBox(10);
        buttonPanel.setPadding(new Insets(10));
        buttonPanel.getChildren().addAll(btnAddArtist, btnUpdateArtist, btnDeleteArtist, btnSearchArtist);

        // Artist's artworks list
        VBox artworksListPanel = new VBox(5);
        artworksListPanel.setPadding(new Insets(10));
        artworksListPanel.getChildren().addAll(new Label("Artist's Artworks"), lstArtistArtworks);
        lstArtistArtworks.setPrefHeight(100);

        // Combine form and artworks list into top section
        VBox artistDetailsPanel = new VBox(10);
        artistDetailsPanel.getChildren().addAll(formPanel, artworksListPanel, buttonPanel);

        // Configure artists table
        configureArtistsTable();

        // Set the divider position
        splitPane.getItems().addAll(artistDetailsPanel, new ScrollPane(tblArtists));
        splitPane.setDividerPositions(0.4);

        panel.setCenter(splitPane);

        return panel;
    }

    private BorderPane createArtworkPanel() {
        BorderPane panel = new BorderPane();
        panel.setPadding(new Insets(10));

        // Split pane for form/buttons and table
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);

        // Artwork form
        GridPane formPanel = new GridPane();
        formPanel.setHgap(10);
        formPanel.setVgap(5);
        formPanel.setPadding(new Insets(10));

        formPanel.add(new Label("Artist ID:"), 0, 0);
        formPanel.add(txtArtworkArtistId, 1, 0);
        formPanel.add(new Label("Title:"), 0, 1);
        formPanel.add(txtArtworkTitle, 1, 1);
        formPanel.add(new Label("Type:"), 0, 2);
        formPanel.add(txtArtworkType, 1, 2);
        formPanel.add(new Label("Description:"), 0, 3);
        formPanel.add(txtArtworkDescription, 1, 3);
        formPanel.add(new Label("Image 1:"), 0, 4);
        formPanel.add(txtArtworkImage1, 1, 4);
        formPanel.add(new Label("Image 2:"), 0, 5);
        formPanel.add(txtArtworkImage2, 1, 5);
        formPanel.add(new Label("Image 3:"), 0, 6);
        formPanel.add(txtArtworkImage3, 1, 6);

        // Artwork buttons
        HBox buttonPanel = new HBox(10);
        buttonPanel.setPadding(new Insets(10));
        buttonPanel.getChildren().addAll(btnAddArtwork, btnUpdateArtwork, btnDeleteArtwork, btnSearchArtwork);

        VBox topSection = new VBox(10);
        topSection.getChildren().addAll(formPanel, buttonPanel);

        // Artwork table
        configureArtworksTable();

        splitPane.getItems().addAll(topSection, new ScrollPane(tblArtworks));
        splitPane.setDividerPositions(0.4);

        panel.setCenter(splitPane);

        return panel;
    }

    private HBox createButtonPanel() {
        HBox panel = new HBox(10);
        panel.setPadding(new Insets(10));
        panel.setAlignment(Pos.CENTER);
        panel.getChildren().addAll(btnLoadArtists, btnLoadArtworks, btnSaveArtworksToCSV, btnSaveArtworksToDOC);
        return panel;
    }

    private HBox createFilterPanel() {
        HBox panel = new HBox(10);
        panel.setPadding(new Insets(10));
        panel.setAlignment(Pos.CENTER_LEFT);

        panel.getChildren().addAll(
                new Label("Artist Name:"), txtFilterArtist,
                new Label("Artwork Type:"), txtFilterArtworkType,
                btnFilterArtworks
        );

        return panel;
    }

    private void configureArtistsTable() {
        // Create columns with explicit type casts
        TableColumn<Object[], Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> {
            Integer value = (Integer) data.getValue()[0];
            return new javafx.beans.property.SimpleIntegerProperty(value).asObject();
        });

        TableColumn<Object[], String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> {
            String value = (String) data.getValue()[1];
            return new javafx.beans.property.SimpleStringProperty(value);
        });

        TableColumn<Object[], String> birthDateCol = new TableColumn<>("Birth Date");
        birthDateCol.setCellValueFactory(data -> {
            String value = (String) data.getValue()[2];
            return new javafx.beans.property.SimpleStringProperty(value);
        });

        TableColumn<Object[], String> birthPlaceCol = new TableColumn<>("Birth Place");
        birthPlaceCol.setCellValueFactory(data -> {
            String value = (String) data.getValue()[3];
            return new javafx.beans.property.SimpleStringProperty(value);
        });

        TableColumn<Object[], String> nationalityCol = new TableColumn<>("Nationality");
        nationalityCol.setCellValueFactory(data -> {
            String value = (String) data.getValue()[4];
            return new javafx.beans.property.SimpleStringProperty(value);
        });

        TableColumn<Object[], String> photoCol = new TableColumn<>("Photo");
        photoCol.setCellValueFactory(data -> {
            String value = (String) data.getValue()[5];
            return new javafx.beans.property.SimpleStringProperty(value);
        });

        tblArtists.getColumns().addAll(idCol, nameCol, birthDateCol, birthPlaceCol, nationalityCol, photoCol);
    }

    private void configureArtworksTable() {
        // Create columns with explicit type casts
        TableColumn<Object[], Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> {
            Integer value = (Integer) data.getValue()[0];
            return new javafx.beans.property.SimpleIntegerProperty(value).asObject();
        });

        TableColumn<Object[], Integer> artistIdCol = new TableColumn<>("Artist ID");
        artistIdCol.setCellValueFactory(data -> {
            Integer value = (Integer) data.getValue()[1];
            return new javafx.beans.property.SimpleIntegerProperty(value).asObject();
        });

        TableColumn<Object[], String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> {
            String value = (String) data.getValue()[2];
            return new javafx.beans.property.SimpleStringProperty(value);
        });

        TableColumn<Object[], String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> {
            String value = (String) data.getValue()[3];
            return new javafx.beans.property.SimpleStringProperty(value);
        });

        TableColumn<Object[], String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(data -> {
            String value = (String) data.getValue()[4];
            return new javafx.beans.property.SimpleStringProperty(value);
        });

        TableColumn<Object[], String> image1Col = new TableColumn<>("Image 1");
        image1Col.setCellValueFactory(data -> {
            String value = (String) data.getValue()[5];
            return new javafx.beans.property.SimpleStringProperty(value);
        });

        TableColumn<Object[], String> image2Col = new TableColumn<>("Image 2");
        image2Col.setCellValueFactory(data -> {
            String value = (String) data.getValue()[6];
            return new javafx.beans.property.SimpleStringProperty(value);
        });

        TableColumn<Object[], String> image3Col = new TableColumn<>("Image 3");
        image3Col.setCellValueFactory(data -> {
            String value = (String) data.getValue()[7];
            return new javafx.beans.property.SimpleStringProperty(value);
        });

        tblArtworks.getColumns().addAll(idCol, artistIdCol, titleCol, typeCol, descriptionCol, image1Col, image2Col, image3Col);
    }

    @Override
    public void filterArtworksButtonClicked() {
        String artistName = txtFilterArtist.getText().trim();
        String artworkType = txtFilterArtworkType.getText().trim();
        museumPresenter.filterArtworks(artistName, artworkType);
    }

    @Override
    public String getFilterArtistName() {
        return txtFilterArtist.getText().trim();
    }

    @Override
    public String getFilterArtworkType() {
        return txtFilterArtworkType.getText().trim();
    }

    @Override
    public String getArtistName() {
        return txtArtistName.getText();
    }

    @Override
    public String getArtistBirthDate() {
        return txtArtistBirthDate.getText();
    }

    @Override
    public String getArtistBirthPlace() {
        return txtArtistBirthPlace.getText();
    }

    @Override
    public String getArtistNationality() {
        return txtArtistNationality.getText();
    }

    @Override
    public String getArtistPhoto() {
        return txtArtistPhoto.getText();
    }

    @Override
    public String getArtworkTitle() {
        return txtArtworkTitle.getText();
    }

    @Override
    public int getArtworkArtistId() {
        try {
            return Integer.parseInt(txtArtworkArtistId.getText());
        } catch (NumberFormatException e) {
            return 0; // Default value if parsing fails
        }
    }

    @Override
    public String getArtworkType() {
        return txtArtworkType.getText();
    }

    @Override
    public String getArtworkDescription() {
        return txtArtworkDescription.getText();
    }

    @Override
    public String getArtworkImage1() {
        return txtArtworkImage1.getText();
    }

    @Override
    public String getArtworkImage2() {
        return txtArtworkImage2.getText();
    }

    @Override
    public String getArtworkImage3() {
        return txtArtworkImage3.getText();
    }

    @Override
    public void setArtistTable(Object[][] data, String[] columnNames) {
        ObservableList<Object[]> artists = FXCollections.observableArrayList();
        for (Object[] row : data) {
            artists.add(row);
        }
        tblArtists.setItems(artists);
    }

    @Override
    public void setArtworkTable(Object[][] data, String[] columnNames) {
        ObservableList<Object[]> artworks = FXCollections.observableArrayList();
        for (Object[] row : data) {
            artworks.add(row);
        }
        tblArtworks.setItems(artworks);
    }

    @Override
    public void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void addArtistButtonClicked() {
        museumPresenter.addArtist();
        clearFilterFields();
    }

    @Override
    public void updateArtistButtonClicked() {
        museumPresenter.updateArtist();
        clearFilterFields();
    }

    @Override
    public void deleteArtistButtonClicked() {
        museumPresenter.deleteArtist();
        clearFilterFields();
    }

    @Override
    public void searchArtistButtonClicked() {
        museumPresenter.searchArtist();
        clearFilterFields();
    }

    @Override
    public void addArtworkButtonClicked() {
        museumPresenter.addArtwork();
        clearFilterFields();
    }

    @Override
    public void updateArtworkButtonClicked() {
        museumPresenter.updateArtwork();
        clearFilterFields();
    }

    @Override
    public void deleteArtworkButtonClicked() {
        museumPresenter.deleteArtwork();
        clearFilterFields();
    }

    @Override
    public void searchArtworkButtonClicked() {
        museumPresenter.searchArtwork();
        clearFilterFields();
    }

    @Override
    public void saveArtworksToCSVButtonClicked() {
        museumPresenter.saveArtworksToCSV();
    }

    @Override
    public void saveArtworksToDOCButtonClicked() {
        museumPresenter.saveArtworksToDOC();
    }

    @Override
    public void loadArtistsButtonClicked() {
        museumPresenter.loadArtists();
    }

    @Override
    public void loadArtworksButtonClicked() {
        museumPresenter.loadArtworks();
    }

    private void loadArtistArtworks(int artistId) {
        museumPresenter.loadArtistArtworks(artistId);
    }

    @Override
    public void displayArtistArtworks(List<String> artworkTitles) {
        artistArtworksListModel.clear();
        artistArtworksListModel.addAll(artworkTitles);
    }

    private void openImagesInBrowser(String... imageUrls) {
        for (String url : imageUrls) {
            if (url != null && !url.isEmpty() && !url.equals("w")) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        // Check if the URL starts with http:// or https://
                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            url = "https://" + url;
                        }
                        desktop.browse(new URI(url));
                    } else {
                        this.showMessage("Error", "Desktop browsing is not supported on this platform.");
                    }
                } catch (Exception e) {
                    this.showMessage("Error", "Failed to open URL: " + e.getMessage());
                }
            }
        }
    }

    private void clearArtistFields() {
        txtArtistName.clear();
        txtArtistBirthDate.clear();
        txtArtistBirthPlace.clear();
        txtArtistNationality.clear();
        txtArtistPhoto.clear();
    }

    private void clearArtworkFields() {
        txtArtworkTitle.clear();
        txtArtworkArtistId.clear();
        txtArtworkType.clear();
        txtArtworkDescription.clear();
        txtArtworkImage1.clear();
        txtArtworkImage2.clear();
        txtArtworkImage3.clear();
    }

    public void clearFilterFields() {
        clearArtistFields();
        clearArtworkFields();
    }

    public static void main(String[] args) {
        launch(args);
    }
}