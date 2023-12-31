package mytunes.dal.db;

import javafx.beans.property.StringProperty;
import mytunes.be.Artist;
import mytunes.be.Genre;
import mytunes.be.PlaylistSongs;
import mytunes.be.Song;
import mytunes.dal.ISongDataAccess;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO_DB_Songs implements ISongDataAccess {

    private PlaylistSongs playlistSongs;
    private MyTunesDataBaseConnector databaseConnector;
    private static StringProperty fPath;
    public static String getFpath() {
        return fPath.get();
    }
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
                String fPath = rs.getString("songPath");

                Artist artist = new Artist(artistName, artistID);
                Genre genreType = new Genre(type, genre, artistID);
                Song song = new Song(id, title, time, artist, genreType, formatedTime, fPath);
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

    /**
     * Create a new song and insert it into out database
     * @param song
     * @return
     * @throws Exception
     */
    public Song createSong(Song song) throws Exception {
        // SQL statement
        String sql = "INSERT INTO FSpotify.dbo.Songs (SongTitle, SongDuration, ArtistID, GenreID, FormatedTime, songPath) VALUES (?,?,?,?,?,?);";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Bind parameters
            stmt.setString(1, song.getTitle());
            stmt.setInt(2, song.getTime());
            stmt.setInt(3, song.getArtist().getId());
            stmt.setInt(4, song.getGenre().getId());
            stmt.setString(5, song.getFormatedTime()); // Formatted time converts the time to min and sec
            stmt.setString(6, song.getFPath());

            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }
            // Create song object and send up the layers
            Song createdSong = new Song(id, song.getTitle(), song.getTime(), song.getArtist(), song.getGenre(), song.getFormatedTime(), song.getFPath());

            return createdSong;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create song", ex);
        }
    }

    /**
     * Update the selected song on the database
     * @param song
     * @return
     * @throws Exception
     */
    public Song updateSong(Song song) throws Exception {
        // SQL statement
        String sql = "UPDATE FSpotify.dbo.Songs SET SongTitle = ?, SongDuration = ?, ArtistID = ?, GenreID = ? WHERE SongID = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Bind parameters
            stmt.setString(1,song.getTitle());
            stmt.setInt(2, song.getTime());
            stmt.setInt(3, song.getArtist().getId());
            stmt.setInt(4, song.getGenre().getId());
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

    /**
     *  Here we delete a song from the database.
     *  We have two separate prepared statements in case the song is in a playlist
     * @param song
     * @return
     * @throws Exception
     */
    public Song deleteSong(Song song) throws Exception {
        // SQL statement
        String deletePlaylistSongSQL = "delete from FSpotify.dbo.PlaylistSongs where SongID = ?";
        String deleteSongSQL = "delete from FSpotify.dbo.Songs WHERE SongID = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement deletePlaylistSongStmt = conn.prepareStatement(deletePlaylistSongSQL);
             PreparedStatement deleteSongStmt = conn.prepareStatement(deleteSongSQL)) {
            // Start a transaction
            conn.setAutoCommit(false);

            try {
                // Delete the song from the playlist
                deletePlaylistSongStmt.setInt(1, song.getId());
                deletePlaylistSongStmt.executeUpdate();
                // Delete the song itself
                deleteSongStmt.setInt(1, song.getId());
                deleteSongStmt.executeUpdate();
                // Commit the transaction if everything is successful
                conn.commit();
            } catch (SQLException ex) {
                // Rollback the transaction if an error occurs
                conn.rollback();
                throw new Exception("Could not delete song", ex);
            } finally {
                // Reset auto-commit to true
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not delete song", ex);
        }
        return song;
    }
}