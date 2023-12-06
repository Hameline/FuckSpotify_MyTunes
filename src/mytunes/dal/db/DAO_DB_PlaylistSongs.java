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

    /**
     *
     * @param playlistid is the id of the playlist that the user selects.
     * @return all the songs that is connected to the playlist with the id selected.
     * @throws Exception
     */
    public List<Song> getPlaylistSong(int playlistid) throws Exception {
        List<Song> allSongsInPlaylist = new ArrayList<>();
        String sql =
                /**
                 * This is the SQL statement used to get the information from
                 * the database that we want.
                 */
                "SELECT S.*, A.ArtistName, G.GenreType FROM Songs S\n" + // selects the values from column Song
                "INNER JOIN PlaylistSongs PS ON S.SongID = PS.SongID\n" + // joins so that we can get the song title
                "LEFT JOIN Artist A ON A.ArtistID = S.ArtistID\n" + // joins so that we can get the artist name
                "LEFT JOIN Genre G ON G.GenreID = S.GenreID\n" + // joins so that we can get the genre type.
                "WHERE PS.PlaylistID = ?"; // from the playlist that the user selects


        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(sql)){


                 pStmt.setInt(1, playlistid); // uses the playlist id here, to get the rest.
                 ResultSet rs = pStmt.executeQuery();

                // Loop through rows from the database result set
                 while (rs.next()) {

                    //Map DB row to Playlist object
                     int songID = rs.getInt("SongID");
                     String songTitle = rs.getString("SongTitle");
                     int duration = rs.getInt("SongDuration");
                     String formatedTime = rs.getString("SongDuration");
                     String artistName = rs.getString("ArtistName");
                     int artistID = rs.getInt("ArtistID");
                     String type = rs.getString("GenreType");
                     int id = rs.getInt("GenreID");
                     String fPath = rs.getString("songPath");

                    Artist artist = new Artist(artistName, artistID); // creates an object to bind to the playlistSong
                    Genre genreType = new Genre(type, id, artistID); // same as above..
                    Song playlistSongs = new Song(songID, songTitle, duration, artist, genreType, formatedTime, fPath);
                    // this object is added to the list.
                    allSongsInPlaylist.add(playlistSongs);
                }
                    return allSongsInPlaylist;


                } catch (SQLException ex) {
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

    @Override
    public List<Song> fetchSongsForPlaylist(int playlistId) throws Exception {
        return null;
    }
}