package model.repository;

import model.Artwork;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArtworkRepository {
    private Repository repository;

    // Constructor
    public ArtworkRepository() {
        this.repository = new Repository();
    }

    // Metoda pentru adăugarea unei opere de artă
    public boolean addArtwork(Artwork artwork) {
        String sql = "INSERT INTO Artwork(artist_id, titlu, tip, descriere, imagine1, imagine2, imagine3) VALUES("
                + artwork.getArtistId() + ", '"
                + artwork.getTitlu() + "', '"
                + artwork.getTip() + "', '"
                + artwork.getDescriere() + "', '"
                + artwork.getImagine1() + "', '"
                + artwork.getImagine2() + "', '"
                + artwork.getImagine3() + "')";
        return repository.executeSQLCommand(sql);
    }

    // Metoda pentru ștergerea unei opere de artă după id
    public boolean deleteArtwork(int idArtwork) {
        String sql = "DELETE FROM Artwork WHERE id_artwork = " + idArtwork;
        return repository.executeSQLCommand(sql);
    }

    // Metoda pentru actualizarea unei opere de artă
    public boolean updateArtwork(Artwork artwork) {
        String sql = "UPDATE Artwork SET artist_id = " + artwork.getArtistId()
                + ", titlu = '" + artwork.getTitlu()
                + "', tip = '" + artwork.getTip()
                + "', descriere = '" + artwork.getDescriere()
                + "', imagine1 = '" + artwork.getImagine1()
                + "', imagine2 = '" + artwork.getImagine2()
                + "', imagine3 = '" + artwork.getImagine3()
                + "' WHERE id_artwork = " + artwork.getIdArtwork();
        return repository.executeSQLCommand(sql);
    }

    // Metoda pentru obținerea tuturor operelor de artă
    public List<Artwork> getAllArtworks() {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT * FROM Artwork";
        ResultSet resultSet = repository.executeSQLQuery(sql);

        try {
            while (resultSet.next()) {
                Artwork artwork = new Artwork(
                        resultSet.getInt("id_artwork"),
                        resultSet.getInt("artist_id"),
                        resultSet.getString("titlu"),
                        resultSet.getString("tip"),
                        resultSet.getString("descriere"),
                        resultSet.getString("imagine1"),
                        resultSet.getString("imagine2"),
                        resultSet.getString("imagine3")
                );
                artworks.add(artwork);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artworks;
    }

    // Metoda pentru căutarea unei opere de artă după titlu
    public List<Artwork> searchArtworkByTitle(String title) {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT * FROM Artwork WHERE titlu LIKE '%" + title + "%'";
        ResultSet resultSet = repository.executeSQLQuery(sql);

        try {
            while (resultSet.next()) {
                Artwork artwork = new Artwork(
                        resultSet.getInt("id_artwork"),
                        resultSet.getInt("artist_id"),
                        resultSet.getString("titlu"),
                        resultSet.getString("tip"),
                        resultSet.getString("descriere"),
                        resultSet.getString("imagine1"),
                        resultSet.getString("imagine2"),
                        resultSet.getString("imagine3")
                );
                artworks.add(artwork);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artworks;
    }

    // Metoda pentru obținerea operelor de artă ale unui artist specific
    public List<Artwork> getArtworksByArtistId(int artistId) {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT * FROM Artwork WHERE artist_id = " + artistId;
        ResultSet resultSet = repository.executeSQLQuery(sql);

        try {
            while (resultSet.next()) {
                Artwork artwork = new Artwork(
                        resultSet.getInt("id_artwork"),
                        resultSet.getInt("artist_id"),
                        resultSet.getString("titlu"),
                        resultSet.getString("tip"),
                        resultSet.getString("descriere"),
                        resultSet.getString("imagine1"),
                        resultSet.getString("imagine2"),
                        resultSet.getString("imagine3")
                );
                artworks.add(artwork);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artworks;
    }
}