package model.repository;

import model.Artist;
import model.Artwork;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArtistRepository {
    private Repository repository;

    // Constructor
    public ArtistRepository() {
        this.repository = new Repository();
    }

    // Metoda pentru adăugarea unui artist
    public boolean addArtist(Artist artist) {
        String sql = "INSERT INTO Artist(nume, data_nasterii, loc_nasterii, nationalitate, fotografie) VALUES('"
                + artist.getNume() + "','" + artist.getDataNasterii() + "','" + artist.getLocNasterii() + "','"
                + artist.getNationalitate() + "','" + artist.getFotografie() + "')";
        return repository.executeSQLCommand(sql);
    }

    // Metoda pentru ștergerea unui artist după id
    public boolean deleteArtist(int idArtist) {
        String sql = "DELETE FROM Artist WHERE id_artist = " + idArtist;
        return repository.executeSQLCommand(sql);
    }

    // Metoda pentru actualizarea unui artist
    public boolean updateArtist(Artist artist) {
        String sql = "UPDATE Artist SET nume = '" + artist.getNume() + "', data_nasterii = '" + artist.getDataNasterii()
                + "', loc_nasterii = '" + artist.getLocNasterii() + "', nationalitate = '" + artist.getNationalitate()
                + "', fotografie = '" + artist.getFotografie() + "' WHERE id_artist = " + artist.getIdArtist();
        return repository.executeSQLCommand(sql);
    }

    // Metoda pentru obținerea tuturor artiștilor
    public List<Artist> getAllArtists() {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT * FROM Artist";
        ResultSet resultSet = repository.executeSQLQuery(sql);

        try {
            while (resultSet.next()) {
                Artist artist = new Artist(
                        resultSet.getInt("id_artist"),
                        resultSet.getString("nume"),
                        resultSet.getString("data_nasterii"),
                        resultSet.getString("loc_nasterii"),
                        resultSet.getString("nationalitate"),
                        resultSet.getString("fotografie")
                );
                artists.add(artist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artists;
    }

    // Metoda pentru căutarea unui artist după nume
    public List<Artist> searchArtistByName(String name) {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT * FROM Artist WHERE nume LIKE '%" + name + "%'";
        ResultSet resultSet = repository.executeSQLQuery(sql);

        try {
            while (resultSet.next()) {
                Artist artist = new Artist(
                        resultSet.getInt("id_artist"),
                        resultSet.getString("nume"),
                        resultSet.getString("data_nasterii"),
                        resultSet.getString("loc_nasterii"),
                        resultSet.getString("nationalitate"),
                        resultSet.getString("fotografie")
                );
                artists.add(artist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artists;
    }

    // Metoda pentru filtrarea operelor de artă după artist sau tipul operei
    public List<Artwork> filterArtworks(String artistName, String artworkType) {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT * FROM Artwork WHERE 1=1";

        if (artistName != null && !artistName.isEmpty()) {
            sql += " AND artist_id IN (SELECT id_artist FROM Artist WHERE nume LIKE '%" + artistName + "%')";
        }

        if (artworkType != null && !artworkType.isEmpty()) {
            sql += " AND tip = '" + artworkType + "'";
        }

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

    // Metoda pentru salvarea listei de opere de artă în fișier CSV
    public boolean saveArtworksToCSV(List<Artwork> artworks, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("ID,Titlu,Tip,Descriere,Imagine1,Imagine2,Imagine3\n");
            for (Artwork artwork : artworks) {
                writer.write(artwork.getIdArtwork() + ",");
                writer.write(artwork.getTitlu() + ",");
                writer.write(artwork.getTip() + ",");
                writer.write(artwork.getDescriere() + ",");
                writer.write(artwork.getImagine1() + ",");
                writer.write(artwork.getImagine2() + ",");
                writer.write(artwork.getImagine3() + "\n");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Metoda pentru salvarea listei de opere de artă în fișier DOC
    public boolean saveArtworksToDOC(List<Artwork> artworks, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Lista Opere de Artă\n\n");
            for (Artwork artwork : artworks) {
                writer.write("ID: " + artwork.getIdArtwork() + "\n");
                writer.write("Titlu: " + artwork.getTitlu() + "\n");
                writer.write("Tip: " + artwork.getTip() + "\n");
                writer.write("Descriere: " + artwork.getDescriere() + "\n");
                writer.write("Imagine 1: " + artwork.getImagine1() + "\n");
                writer.write("Imagine 2: " + artwork.getImagine2() + "\n");
                writer.write("Imagine 3: " + artwork.getImagine3() + "\n\n");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}