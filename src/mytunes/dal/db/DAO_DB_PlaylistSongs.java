package mytunes.dal.db;

import mytunes.be.*;
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
        List<Integer> playlist = new ArrayList<>();

        if (playlist.isEmpty()){
            return allPlaylistSongs;
        }

        StringBuilder sql = new StringBuilder("SELECT * FROM FSpotify.dbo.PlaylistSongs " +
                "left join FSpotify.dbo.Songs S on S.SongID = PlaylistSongs.SongID " +
                "left join FSpotify.dbo.Artist A on A.ArtistID = S.ArtistID " +
                "left outer join FSpotify.dbo.Genre G on A.ArtistID = G.ArtistID " +
                "where PlaylistID = ? ");

        for (int i = 0; i < playlist.size(); i++){
            sql.append("?");
            if (i < playlist.size() -1 ){
                sql.append(", ");
            }
        }
        sql.append(") ");

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < playlist.size(); i++) {
                pStmt.setInt(1, playlist.get(i));
            }

            try (ResultSet rs = pStmt.executeQuery()) {
                // Loop through rows from the database result set
                while (rs.next()) {

                    //Map DB row to Playlist object

                    int playlistID = rs.getInt("PlaylistID");
                    int songID = rs.getInt("SongID");
                    String songTitle = rs.getString("SongTitle");
                    int duration = rs.getInt("SongDuration");
                    String formatedTime = rs.getString("SongDuration");
                    String artistName = rs.getString("ArtistName");
                    int artistID = rs.getInt("ArtistID");
                    String type = rs.getString("GenreType");
                    int id = rs.getInt("GenreID");
                    String fPath = rs.getString("songPath");

                    Artist artist = new Artist(artistName, artistID);
                    Genre genreType = new Genre(type, id, artistID);
                    PlaylistSongs playlistSongs = new PlaylistSongs(playlistID, songID, songTitle, duration, artist, genreType, formatedTime, fPath);
                    allPlaylistSongs.add(playlistSongs);
                }
                return allPlaylistSongs;

            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new Exception("Could not get playlist songs from database", ex);
            }
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