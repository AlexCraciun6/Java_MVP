package view;

import presenter.IMuseumGUI;
import presenter.MuseumPresenter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MuseumGUI extends JFrame implements IMuseumGUI {
    private JTextField txtArtistName, txtArtistBirthDate, txtArtistBirthPlace, txtArtistNationality, txtArtistPhoto;
    private JTextField txtArtworkTitle, txtArtworkType, txtArtworkDescription, txtArtworkImage1, txtArtworkImage2, txtArtworkImage3;
    private JButton btnAddArtist, btnUpdateArtist, btnDeleteArtist, btnSearchArtist;
    private JButton btnAddArtwork, btnUpdateArtwork, btnDeleteArtwork, btnSearchArtwork;
    private JButton btnSaveArtworksToCSV, btnSaveArtworksToDOC;
    private JButton btnLoadArtists, btnLoadArtworks;
    private JTable tblArtists, tblArtworks;
    private JComboBox<String> cbArtistFilter, cbArtworkFilter;
    private JTextField txtFilterArtist, txtFilterArtworkType;
    private JButton btnFilterArtworks;
    private JList<String> lstArtistArtworks;
    private DefaultListModel<String> artistArtworksListModel;


    private MuseumPresenter museumPresenter;

    public MuseumGUI() {
        // Configurare frame
        setTitle("Museum Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inițializare componente
        initComponents();

        // Adăugare componente în frame
        add(createArtistPanel(), BorderLayout.WEST);
        add(createArtworkPanel(), BorderLayout.CENTER);
        add(createFilterPanel(), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.SOUTH);

        // Inițializare Presenter
        museumPresenter = new MuseumPresenter(this);

        // Adăugare listeneri pentru butoane
        registerListeners();


        // Afișare frame
        setVisible(true);
    }

    private void initComponents() {
        // Câmpuri de text pentru artist
        txtArtistName = new JTextField(20);
        txtArtistBirthDate = new JTextField(20);
        txtArtistBirthPlace = new JTextField(20);
        txtArtistNationality = new JTextField(20);
        txtArtistPhoto = new JTextField(20);

        // Câmpuri de text pentru operă de artă
        txtArtworkTitle = new JTextField(20);
        txtArtworkType = new JTextField(20);
        txtArtworkDescription = new JTextField(20);
        txtArtworkImage1 = new JTextField(20);
        txtArtworkImage2 = new JTextField(20);
        txtArtworkImage3 = new JTextField(20);

        // Butoane
        btnAddArtist = new JButton("Add Artist");
        btnUpdateArtist = new JButton("Update Artist");
        btnDeleteArtist = new JButton("Delete Artist");
        btnSearchArtist = new JButton("Search Artist");

        btnAddArtwork = new JButton("Add Artwork");
        btnUpdateArtwork = new JButton("Update Artwork");
        btnDeleteArtwork = new JButton("Delete Artwork");
        btnSearchArtwork = new JButton("Search Artwork");

        btnSaveArtworksToCSV = new JButton("Save to CSV");
        btnSaveArtworksToDOC = new JButton("Save to DOC");


        // Butoane pentru încărcarea datelor
        btnLoadArtists = new JButton("Load Artists");
        btnLoadArtworks = new JButton("Load Artworks");

        // Tabele
        tblArtists = new JTable();
        tblArtworks = new JTable();

        // Combobox-uri pentru filtrare
        cbArtistFilter = new JComboBox<>(new String[]{"All", "By Name", "By Nationality"});
        cbArtworkFilter = new JComboBox<>(new String[]{"All", "By Title", "By Type"});

        // Filter components
        txtFilterArtist = new JTextField(15);
        txtFilterArtworkType = new JTextField(15);
        btnFilterArtworks = new JButton("Apply Filters");

        artistArtworksListModel = new DefaultListModel<>();
        lstArtistArtworks = new JList<>(artistArtworksListModel);
        lstArtistArtworks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void registerListeners() {
        // Adăugare listeneri pentru butoanele legate de Artist
        btnAddArtist.addActionListener(e -> addArtistButtonClicked());
        btnUpdateArtist.addActionListener(e -> updateArtistButtonClicked());
        btnDeleteArtist.addActionListener(e -> deleteArtistButtonClicked());
        btnSearchArtist.addActionListener(e -> searchArtistButtonClicked());

        // Adăugare listeneri pentru butoanele legate de Artwork
        btnAddArtwork.addActionListener(e -> addArtworkButtonClicked());
        btnUpdateArtwork.addActionListener(e -> updateArtworkButtonClicked());
        btnDeleteArtwork.addActionListener(e -> deleteArtworkButtonClicked());
        btnSearchArtwork.addActionListener(e -> searchArtworkButtonClicked());

        // Adăugare listeneri pentru butoanele de salvare
        btnSaveArtworksToCSV.addActionListener(e -> saveArtworksToCSVButtonClicked());
        btnSaveArtworksToDOC.addActionListener(e -> saveArtworksToDOCButtonClicked());

        btnLoadArtists.addActionListener(e -> loadArtistsButtonClicked());

        // Listener pentru butonul de încărcare opere de artă
        btnLoadArtworks.addActionListener(e -> loadArtworksButtonClicked());

        // Add listener for filter button
        btnFilterArtworks.addActionListener(e -> filterArtworksButtonClicked());

        // Implementare selectare rând în tabele pentru a popula câmpurile
        // Listener for artist selection to display their artworks
        tblArtists.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblArtists.getSelectedRow() != -1) {
                int row = tblArtists.getSelectedRow();
                txtArtistName.setText(tblArtists.getValueAt(row, 1).toString());
                txtArtistBirthDate.setText(tblArtists.getValueAt(row, 2).toString());
                txtArtistBirthPlace.setText(tblArtists.getValueAt(row, 3).toString());
                txtArtistNationality.setText(tblArtists.getValueAt(row, 4).toString());
                txtArtistPhoto.setText(tblArtists.getValueAt(row, 5).toString());

                // Get the artist ID and load their artworks
                int artistId = Integer.parseInt(tblArtists.getValueAt(row, 0).toString());
                loadArtistArtworks(artistId);
            }
        });

        tblArtworks.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblArtworks.getSelectedRow() != -1) {
                int row = tblArtworks.getSelectedRow();
                txtArtworkTitle.setText(tblArtworks.getValueAt(row, 2).toString());
                txtArtworkType.setText(tblArtworks.getValueAt(row, 3).toString());
                txtArtworkDescription.setText(tblArtworks.getValueAt(row, 4).toString());
                txtArtworkImage1.setText(tblArtworks.getValueAt(row, 5).toString());
                txtArtworkImage2.setText(tblArtworks.getValueAt(row, 6).toString());
                txtArtworkImage3.setText(tblArtworks.getValueAt(row, 7).toString());
            }
        });
    }


    private JPanel createArtistPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Artists"));

        // Formular pentru artist
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        formPanel.add(new JLabel("Name:"));
        formPanel.add(txtArtistName);
        formPanel.add(new JLabel("Birth Date:"));
        formPanel.add(txtArtistBirthDate);
        formPanel.add(new JLabel("Birth Place:"));
        formPanel.add(txtArtistBirthPlace);
        formPanel.add(new JLabel("Nationality:"));
        formPanel.add(txtArtistNationality);
        formPanel.add(new JLabel("Photo:"));
        formPanel.add(txtArtistPhoto);

        // Butoane pentru artist
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAddArtist);
        buttonPanel.add(btnUpdateArtist);
        buttonPanel.add(btnDeleteArtist);
        buttonPanel.add(btnSearchArtist);

        // Create a panel for the artist details and artworks list
        JPanel artistDetailsPanel = new JPanel(new BorderLayout());
        artistDetailsPanel.add(formPanel, BorderLayout.NORTH);

        // Artist's artworks list
        JPanel artworksListPanel = new JPanel(new BorderLayout());
        artworksListPanel.setBorder(BorderFactory.createTitledBorder("Artist's Artworks"));
        artworksListPanel.add(new JScrollPane(lstArtistArtworks), BorderLayout.CENTER);

        artistDetailsPanel.add(artworksListPanel, BorderLayout.CENTER);
        artistDetailsPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Tabel pentru artiști
        JScrollPane scrollPane = new JScrollPane(tblArtists);

        // Adăugare componente în panoul principal
        panel.add(artistDetailsPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createArtworkPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Artworks"));

        // Formular pentru operă de artă
        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        formPanel.add(new JLabel("Title:"));
        formPanel.add(txtArtworkTitle);
        formPanel.add(new JLabel("Type:"));
        formPanel.add(txtArtworkType);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(txtArtworkDescription);
        formPanel.add(new JLabel("Image 1:"));
        formPanel.add(txtArtworkImage1);
        formPanel.add(new JLabel("Image 2:"));
        formPanel.add(txtArtworkImage2);
        formPanel.add(new JLabel("Image 3:"));
        formPanel.add(txtArtworkImage3);

        // Butoane pentru operă de artă
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAddArtwork);
        buttonPanel.add(btnUpdateArtwork);
        buttonPanel.add(btnDeleteArtwork);
        buttonPanel.add(btnSearchArtwork);

        // Tabel pentru opere de artă
        JScrollPane scrollPane = new JScrollPane(tblArtworks);

        // Adăugare componente în panoul principal
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.add(btnLoadArtists);
        panel.add(btnLoadArtworks);
        panel.add(btnSaveArtworksToCSV);
        panel.add(btnSaveArtworksToDOC);
        return panel;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Filter Artworks"));

        panel.add(new JLabel("Artist Name:"));
        panel.add(txtFilterArtist);
        panel.add(new JLabel("Artwork Type:"));
        panel.add(txtFilterArtworkType);
        panel.add(btnFilterArtworks);

        return panel;
    }

    @Override
    public void filterArtworksButtonClicked() {
        String artistName = txtFilterArtist.getText().trim();
        String artworkType = txtFilterArtworkType.getText().trim();
        museumPresenter.filterArtworks(artistName, artworkType);
    }

    // Add getter methods for the filter fields
    public String getFilterArtistName() {
        return txtFilterArtist.getText().trim();
    }

    public String getFilterArtworkType() {
        return txtFilterArtworkType.getText().trim();
    }

    // Implementarea metodelor din IMuseumGUI
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
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        tblArtists.setModel(model);
    }

    @Override
    public void setArtworkTable(Object[][] data, String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        tblArtworks.setModel(model);
    }

    @Override
    public void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void addArtistButtonClicked() {
        museumPresenter.addArtist();
    }

    @Override
    public void updateArtistButtonClicked() {
        museumPresenter.updateArtist();
    }

    @Override
    public void deleteArtistButtonClicked() {
        museumPresenter.deleteArtist();
    }

    @Override
    public void searchArtistButtonClicked() {
        museumPresenter.searchArtist();
    }

    @Override
    public void addArtworkButtonClicked() {
        museumPresenter.addArtwork();
    }

    @Override
    public void updateArtworkButtonClicked() {
        museumPresenter.updateArtwork();
    }

    @Override
    public void deleteArtworkButtonClicked() {
        museumPresenter.deleteArtwork();
    }

    @Override
    public void searchArtworkButtonClicked() {
        museumPresenter.searchArtwork();
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

    // Add method to load artworks for a specific artist
    private void loadArtistArtworks(int artistId) {
        museumPresenter.loadArtistArtworks(artistId);
    }

    // Add method to update the artist artworks list
    @Override
    public void displayArtistArtworks(List<String> artworkTitles) {
        artistArtworksListModel.clear();
        for (String title : artworkTitles) {
            artistArtworksListModel.addElement(title);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MuseumGUI::new);
    }
}