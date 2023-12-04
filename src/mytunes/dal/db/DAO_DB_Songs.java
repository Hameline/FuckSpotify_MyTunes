package mytunes.dal.db;

import mytunes.be.Artist;
import mytunes.be.Genre;
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
            String sql = "SELECT * from FSpotify.dbo.Songs\n" +
                    "join FSpotify.dbo.Genre G on Songs.GenreID = G.GenreID\n" +
                    "left join FSpotify.dbo.Artist A on A.ArtistID = Songs.ArtistID";
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {

                //Map DB row to Song object
                int id = rs.getInt("SongID");
                String title = rs.getString("SongTitle");
                int time = rs.getInt("SongDuration");
                String formatedTime = rs.getString("SongDuration");
                String artistName = rs.getString("ArtistName");
                int artistID = rs.getInt("ArtistID");
                String type = rs.getString("GenreType");
                int genre = rs.getInt("GenreID");

                Artist artist = new Artist(artistName, artistID);
                Genre genreType = new Genre(type, genre, artistID);
                Song song = new Song(id, title, time, artist, genreType, formatedTime);
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
        String sql = "INSERT INTO FSpotify.dbo.Songs (SongTitle, SongDuration, ArtistID, GenreID, FormatedTime) VALUES (?,?,?,?,?);";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Bind parameters
            stmt.setString(1, song.getTitle());
            stmt.setInt(2, song.getTime());
            stmt.setString(3, String.valueOf(song.getArtist()));
            stmt.setString(4, String.valueOf(song.getGenre()));
            stmt.setString(5, song.getFormatedTime());


            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }

            // Create song object and send up the layers
            Song createdSong = new Song(id, song.getTitle(), song.getTime(), song.getArtist(), song.getGenre(), song.getFormatedTime());

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
        String sql = "UPDATE FSpotify.dbo.Songs SET SongTitle = ?, SongDuration = ?, Artist = ?, GenreID = ? WHERE ID = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            // Bind parameters
            stmt.setString(1,song.getTitle());
            stmt.setInt(2, song.getTime());
            //stmt.setString(3, song.getArtist());
            //stmt.setString(4, song.getGenre());
            stmt.setInt(5, song.getId());

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
        String sql = "delete from FSpotify.dbo.Songs WHERE ID = ?;";

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