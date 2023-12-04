package mytunes.dal.db;

import mytunes.be.Playlist;
import mytunes.be.PlaylistSongs;
import mytunes.be.Song;
import mytunes.dal.IPlaylistSongsDataAccess;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO_DB_PlaylistSongs implements IPlaylistSongsDataAccess {

    private MyTunesDataBaseConnector databaseConnector;
    private Playlist playlist;
    private Song song;

    public DAO_DB_PlaylistSongs() throws IOException {
        databaseConnector = new MyTunesDataBaseConnector();
    }

    public List<PlaylistSongs> getAllPlaylistSongs() throws Exception {
        ArrayList<PlaylistSongs> allPlaylistSongs = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM FSpotify.dbo.PlaylistSongs;";
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {

                //Map DB row to Playlist object

                int playlistID = rs.getInt("PlaylistID");
                int songID = rs.getInt("SongID");

                PlaylistSongs playlistSongs = new PlaylistSongs(playlistID, songID);
                allPlaylistSongs.add(playlistSongs);
            }
            return allPlaylistSongs;

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get playlist songs from database", ex);
        }
    }

    @Override
    public PlaylistSongs addSongToPlaylist(PlaylistSongs playlistSongs) throws Exception {
        // SQL command
        String sql = "INSERT INTO FSpotify.dbo.PlaylistSongs (PlaylistID, SongID) VALUES (?, ?)";

        try (Connection conn = databaseConnector.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                // Bind parameters
                stmt.setInt(1, playlistSongs.getPlaylistID());
                stmt.setInt(2, playlistSongs.getSongID());

                // Run the specified SQL statement
                stmt.executeUpdate();

                // Create playlist object and send up the layers
                PlaylistSongs addSongToPlaylist = new PlaylistSongs(playlistSongs.getPlaylistID(), playlistSongs.getSongID());

                return addSongToPlaylist;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not create playlist", ex);
        }
    }


    @Override
    public PlaylistSongs removeSongFromPlaylist(PlaylistSongs playlistSongs) throws Exception {
        // SQL command
        String sql = "delete from FSpotify.dbo.PlaylistSongs WHERE PlaylistID = ?, AND WHERE SongID = ?;";

        try (Connection conn = databaseConnector.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                // Bind parameters
                stmt.setInt(1, playlist.getId());
                stmt.setInt(2, song.getId());

                // Run the specified SQL statement
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not create playlist", ex);
        }
        return playlistSongs;
    }
}