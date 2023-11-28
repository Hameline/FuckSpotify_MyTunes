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

        ArrayList<Song> allSongs = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM dbo.Songs;";
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {

                //Map DB row to Song object
                int id = rs.getInt("id");
                String title = rs.getString("title");
                double time = rs.getDouble("time");
                String genre = rs.getString("genre");

                Song song = new Song(id, title, time, genre);
                allSongs.add(song);
            }
            return allSongs;

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get songs from database", ex);
        }
    }

    public Song createSong(Song song) throws Exception {
        // SQL command
        String sql = "INSERT INTO dbo.Songs (Title, Time, Genre) VALUES (?,?);";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


            // Bind parameters
            stmt.setString(1,song.getTitle());
            stmt.setDouble(2, song.getTime());
            stmt.setString(3, song.getGenre());

            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }

            // Create song object and send up the layers
            Song createdSong = new Song(id, song.getTitle(), song.getTime(), song.getGenre());

            return createdSong;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create song", ex);
        }
    }

    public Song updateSong(Song song) throws Exception {
        // SQL command
        String sql = "UPDATE dbo.Songs SET Title = ?, Time = ?, Genre = ? WHERE ID = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            // Bind parameters
            stmt.setString(1,song.getTitle());
            stmt.setDouble(2, song.getTime());
            stmt.setString(3, song.getGenre());
            stmt.setInt(4, song.getId());

            // Run the specified SQL statement
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not update song", ex);
        }
        return song;
    }

    public Song deleteSong(Song song) throws Exception {
        // SQL command
        String sql = "delete from dbo.Songs WHERE ID = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Bind parameters
            stmt.setInt(1, song.getId());

            // Run the specified SQL statement
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create song", ex);
        }
        return song;
    }
}