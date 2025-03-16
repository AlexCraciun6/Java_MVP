package presenter;

import java.util.List;

public interface IMuseumGUI {
    // Metode pentru obținerea datelor din interfață
    String getArtistName();
    String getArtistBirthDate();
    String getArtistBirthPlace();
    String getArtistNationality();
    String getArtistPhoto();
    String getArtworkTitle();
    int getArtworkArtistId();
    String getArtworkType();
    String getArtworkDescription();
    String getArtworkImage1();
    String getArtworkImage2();
    String getArtworkImage3();

    // Metode pentru afișarea datelor în interfață
    void setArtistTable(Object[][] data, String[] columnNames);
    void setArtworkTable(Object[][] data, String[] columnNames);
    void showMessage(String title, String message);

    // Metode pentru gestionarea evenimentelor
    void addArtistButtonClicked();
    void updateArtistButtonClicked();
    void deleteArtistButtonClicked();
    void searchArtistButtonClicked();
    void addArtworkButtonClicked();
    void updateArtworkButtonClicked();
    void deleteArtworkButtonClicked();
    void searchArtworkButtonClicked();
    void saveArtworksToCSVButtonClicked();
    void saveArtworksToDOCButtonClicked();

    // Metode pentru încărcarea datelor
    void loadArtistsButtonClicked();
    void loadArtworksButtonClicked();

    // filtering
    void filterArtworksButtonClicked();
    String getFilterArtistName();
    String getFilterArtworkType();

    void displayArtistArtworks(List<String> artworkTitles);
}