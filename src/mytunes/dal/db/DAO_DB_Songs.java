package mytunes.dal.db;

import mytunes.be.Song;
import mytunes.dal.ISongDataAccess;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO_DB_Songs implements ISongDataAccess {

    private MyTunesDataBaseConnector databaseConnector;

    public DAO_DB_Songs() throws IOException {
        databaseConnector = new MyTunesDataBaseConnector();
    }

    public List<Song> getAllSongs() throws Exception {

        ArrayList<Song> allMovies = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM dbo.Movie;";
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {

                //Map DB row to Movie object
                String title = rs.getString("title");
                double time = rs.getDouble("time");
                String genre = rs.getString("genre");

                Song song = new Song(title, time, genre);
                allMovies.add(song);
            }
            return allMovies;

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get songs from database", ex);
        }
    }

    public Song createSong(Song song) throws Exception {

        // SQL command
        String sql = "INSERT INTO dbo.FSpotify (Title,Year) VALUES (?,?);";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


            // Bind parameters
            stmt.setString(1,movie.getTitle());
            stmt.setInt(2, movie.getYear());

            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }

            // Create movie object and send up the layers
            Movie createdMovie = new Movie(id, movie.getYear(), movie.getTitle());

            return createdMovie;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create movie", ex);
        }
    }



    public void updateSong(Song song) throws Exception {

// SQL command
        String sql = "UPDATE dbo.Movie SET Title = ?, Year = ? WHERE ID = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            // Bind parameters
            stmt.setString(1,movie.getTitle());
            stmt.setInt(2, movie.getYear());
            stmt.setInt(3, movie.getId());

            // Run the specified SQL statement
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not update movie", ex);
        }
    }

    public void deleteSong(Song song) throws Exception {
        // SQL command
        String sql = "delete from dbo.Movie WHERE ID = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Bind parameters
            stmt.setInt(1, Song.getId());

            // Run the specified SQL statement
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create movie", ex);
        }

    }
}
