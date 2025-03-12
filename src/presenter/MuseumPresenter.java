package presenter;

import model.Artist;
import model.Artwork;
import model.repository.ArtistRepository;
import model.repository.ArtworkRepository;

import java.util.List;

public class MuseumPresenter {
    private IMuseumGUI museumGUI;
    private ArtistRepository artistRepository;
    private ArtworkRepository artworkRepository;

    public MuseumPresenter(IMuseumGUI museumGUI) {
        this.museumGUI = museumGUI;
        this.artistRepository = new ArtistRepository();
        this.artworkRepository = new ArtworkRepository();
    }

    // Metode pentru gestionarea artiștilor

    public void addArtist() {
        try {
            String name = museumGUI.getArtistName();
            String birthDate = museumGUI.getArtistBirthDate();
            String birthPlace = museumGUI.getArtistBirthPlace();
            String nationality = museumGUI.getArtistNationality();
            String photo = museumGUI.getArtistPhoto();

            if (name.isEmpty() || birthDate.isEmpty() || birthPlace.isEmpty() || nationality.isEmpty()) {
                museumGUI.showMessage("Error", "All fields are required!");
                return;
            }

            Artist artist = new Artist(0, name, birthDate, birthPlace, nationality, photo);
            boolean result = artistRepository.addArtist(artist);

            if (result) {
                museumGUI.showMessage("Success", "Artist added successfully!");
                museumGUI.setArtistList(artistRepository.getAllArtists());
            } else {
                museumGUI.showMessage("Error", "Failed to add artist!");
            }
        } catch (Exception e) {
            museumGUI.showMessage("Error", e.getMessage());
        }
    }

    public void updateArtist() {
        try {
            // Obține datele artistului din interfață
            String name = museumGUI.getArtistName();
            String birthDate = museumGUI.getArtistBirthDate();
            String birthPlace = museumGUI.getArtistBirthPlace();
            String nationality = museumGUI.getArtistNationality();
            String photo = museumGUI.getArtistPhoto();

            if (name.isEmpty() || birthDate.isEmpty() || birthPlace.isEmpty() || nationality.isEmpty()) {
                museumGUI.showMessage("Error", "All fields are required!");
                return;
            }

            // Caută artistul după nume
            List<Artist> artists = artistRepository.searchArtistByName(name);
            if (artists.isEmpty()) {
                museumGUI.showMessage("Error", "Artist not found!");
                return;
            }

            // Actualizează artistul
            Artist artist = artists.get(0);
            artist.setNume(name);
            artist.setDataNasterii(birthDate);
            artist.setLocNasterii(birthPlace);
            artist.setNationalitate(nationality);
            artist.setFotografie(photo);

            boolean result = artistRepository.updateArtist(artist);

            if (result) {
                museumGUI.showMessage("Success", "Artist updated successfully!");
                museumGUI.setArtistList(artistRepository.getAllArtists());
            } else {
                museumGUI.showMessage("Error", "Failed to update artist!");
            }
        } catch (Exception e) {
            museumGUI.showMessage("Error", e.getMessage());
        }
    }

    public void deleteArtist() {
        try {
            String name = museumGUI.getArtistName();
            if (name.isEmpty()) {
                museumGUI.showMessage("Error", "Artist name is required!");
                return;
            }

            // Caută artistul după nume
            List<Artist> artists = artistRepository.searchArtistByName(name);
            if (artists.isEmpty()) {
                museumGUI.showMessage("Error", "Artist not found!");
                return;
            }

            // Șterge artistul
            Artist artist = artists.get(0);
            boolean result = artistRepository.deleteArtist(artist.getIdArtist());

            if (result) {
                museumGUI.showMessage("Success", "Artist deleted successfully!");
                museumGUI.setArtistList(artistRepository.getAllArtists());
            } else {
                museumGUI.showMessage("Error", "Failed to delete artist!");
            }
        } catch (Exception e) {
            museumGUI.showMessage("Error", e.getMessage());
        }
    }

    public void searchArtist() {
        try {
            String name = museumGUI.getArtistName();
            if (name.isEmpty()) {
                museumGUI.showMessage("Error", "Artist name is required!");
                return;
            }

            List<Artist> artists = artistRepository.searchArtistByName(name);
            if (artists.isEmpty()) {
                museumGUI.showMessage("Info", "No artists found!");
            } else {
                museumGUI.setArtistList(artists);
            }
        } catch (Exception e) {
            museumGUI.showMessage("Error", e.getMessage());
        }
    }

    // Metode pentru gestionarea operelor de artă

    public void getAllArtworks() {
        List<Artwork> artworks = artworkRepository.getAllArtworks();
        museumGUI.setArtworkList(artworks);
    }

    public void addArtwork() {
        try {
            String title = museumGUI.getArtworkTitle();
            String type = museumGUI.getArtworkType();
            String description = museumGUI.getArtworkDescription();
            String image1 = museumGUI.getArtworkImage1();
            String image2 = museumGUI.getArtworkImage2();
            String image3 = museumGUI.getArtworkImage3();

            if (title.isEmpty() || type.isEmpty() || description.isEmpty()) {
                museumGUI.showMessage("Error", "Title, type, and description are required!");
                return;
            }

            // Presupunem că artistul este selectat din interfață
            int artistId = 1; // Trebuie să obții ID-ul artistului selectat
            Artwork artwork = new Artwork(0, artistId, title, type, description, image1, image2, image3);
            boolean result = artworkRepository.addArtwork(artwork);

            if (result) {
                museumGUI.showMessage("Success", "Artwork added successfully!");
                museumGUI.setArtworkList(artworkRepository.getAllArtworks());
            } else {
                museumGUI.showMessage("Error", "Failed to add artwork!");
            }
        } catch (Exception e) {
            museumGUI.showMessage("Error", e.getMessage());
        }
    }

    public void updateArtwork() {
        try {
            String title = museumGUI.getArtworkTitle();
            String type = museumGUI.getArtworkType();
            String description = museumGUI.getArtworkDescription();
            String image1 = museumGUI.getArtworkImage1();
            String image2 = museumGUI.getArtworkImage2();
            String image3 = museumGUI.getArtworkImage3();

            if (title.isEmpty() || type.isEmpty() || description.isEmpty()) {
                museumGUI.showMessage("Error", "Title, type, and description are required!");
                return;
            }

            // Caută opera de artă după titlu
            List<Artwork> artworks = artworkRepository.searchArtworkByTitle(title);
            if (artworks.isEmpty()) {
                museumGUI.showMessage("Error", "Artwork not found!");
                return;
            }

            // Actualizează opera de artă
            Artwork artwork = artworks.get(0);
            artwork.setTitlu(title);
            artwork.setTip(type);
            artwork.setDescriere(description);
            artwork.setImagine1(image1);
            artwork.setImagine2(image2);
            artwork.setImagine3(image3);

            boolean result = artworkRepository.updateArtwork(artwork);

            if (result) {
                museumGUI.showMessage("Success", "Artwork updated successfully!");
                museumGUI.setArtworkList(artworkRepository.getAllArtworks());
            } else {
                museumGUI.showMessage("Error", "Failed to update artwork!");
            }
        } catch (Exception e) {
            museumGUI.showMessage("Error", e.getMessage());
        }
    }

    public void deleteArtwork() {
        try {
            String title = museumGUI.getArtworkTitle();
            if (title.isEmpty()) {
                museumGUI.showMessage("Error", "Artwork title is required!");
                return;
            }

            // Caută opera de artă după titlu
            List<Artwork> artworks = artworkRepository.searchArtworkByTitle(title);
            if (artworks.isEmpty()) {
                museumGUI.showMessage("Error", "Artwork not found!");
                return;
            }

            // Șterge opera de artă
            Artwork artwork = artworks.get(0);
            boolean result = artworkRepository.deleteArtwork(artwork.getIdArtwork());

            if (result) {
                museumGUI.showMessage("Success", "Artwork deleted successfully!");
                museumGUI.setArtworkList(artworkRepository.getAllArtworks());
            } else {
                museumGUI.showMessage("Error", "Failed to delete artwork!");
            }
        } catch (Exception e) {
            museumGUI.showMessage("Error", e.getMessage());
        }
    }

    public void searchArtwork() {
        try {
            String title = museumGUI.getArtworkTitle();
            if (title.isEmpty()) {
                museumGUI.showMessage("Error", "Artwork title is required!");
                return;
            }

            List<Artwork> artworks = artworkRepository.searchArtworkByTitle(title);
            if (artworks.isEmpty()) {
                museumGUI.showMessage("Info", "No artworks found!");
            } else {
                museumGUI.setArtworkList(artworks);
            }
        } catch (Exception e) {
            museumGUI.showMessage("Error", e.getMessage());
        }
    }

    public void loadArtists() {
        try {
            List<Artist> artists = artistRepository.getAllArtists();
            if (artists.isEmpty()) {
                museumGUI.showMessage("Info", "No artists found in the database!");
            } else {
                museumGUI.setArtistList(artists);
            }
        } catch (Exception e) {
            museumGUI.showMessage("Error", "Failed to load artists: " + e.getMessage());
        }
    }

    public void loadArtworks() {
        try {
            List<Artwork> artworks = artworkRepository.getAllArtworks();
            if (artworks.isEmpty()) {
                museumGUI.showMessage("Info", "No artworks found in the database!");
            } else {
                museumGUI.setArtworkList(artworks);
            }
        } catch (Exception e) {
            museumGUI.showMessage("Error", "Failed to load artworks: " + e.getMessage());
        }
    }

    public void saveArtworksToCSV() {
        return;
//        try {
//            List<Artwork> artworks = artworkRepository.getAllArtworks();
//            if (artworks.isEmpty()) {
//                museumGUI.showMessage("Error", "No artworks to save!");
//                return;
//            }
//
//            String filePath = "artworks.csv";
//            boolean result = artworkRepository.saveArtworksToCSV(artworks, filePath);
//
//            if (result) {
//                museumGUI.showMessage("Success", "Artworks saved to CSV successfully!");
//            } else {
//                museumGUI.showMessage("Error", "Failed to save artworks to CSV!");
//            }
//        } catch (Exception e) {
//            museumGUI.showMessage("Error", e.getMessage());
//        }
    }

    public void saveArtworksToDOC() {
        return;
//        try {
//            List<Artwork> artworks = artworkRepository.getAllArtworks();
//            if (artworks.isEmpty()) {
//                museumGUI.showMessage("Error", "No artworks to save!");
//                return;
//            }
//
//            String filePath = "artworks.doc";
//            boolean result = artworkRepository.saveArtworksToDOC(artworks, filePath);
//
//            if (result) {
//                museumGUI.showMessage("Success", "Artworks saved to DOC successfully!");
//            } else {
//                museumGUI.showMessage("Error", "Failed to save artworks to DOC!");
//            }
//        } catch (Exception e) {
//            museumGUI.showMessage("Error", e.getMessage());
//        }
    }
}