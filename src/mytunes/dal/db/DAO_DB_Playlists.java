package mytunes.dal.db;

import mytunes.be.Playlist;
import mytunes.be.PlaylistSongs;
import mytunes.be.UserPlaylist;
import mytunes.dal.IPlaylistDataAccess;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO_DB_Playlists implements IPlaylistDataAccess {

    private MyTunesDataBaseConnector databaseConnector;

    public DAO_DB_Playlists() throws IOException {
        databaseConnector = new MyTunesDataBaseConnector();
    }

    public List<Playlist> getAllPlaylists() throws Exception {
        ArrayList<Playlist> allPlaylists = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM FSpotify.dbo.Playlist;";
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {

                //Map DB row to Playlist object
                int id = rs.getInt("PlaylistID");
                String name = rs.getString("PlaylistName");

                Playlist playlist = new Playlist(id, name);
                allPlaylists.add(playlist);
            }
            return allPlaylists;

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get playlist from database", ex);
        }
    }

    /*public Playlist createPlaylist(Playlist playlist) throws Exception {
        // SQL command
        String sql = "INSERT INTO FSpotify.dbo.Playlist (PlaylistName) VALUES (?)";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Bind parameters
            stmt.setString(1, playlist.getName());

            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }

            // Create playlist object and send up the layers
            Playlist createdPlaylist = new Playlist(id, playlist.getName());

            return createdPlaylist;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create playlist", ex);
        }
    }*/

    /**
     * This method creates a playlist and locks it to a user. This will have the effect
     * that different users will only see the playlist they have made.
     * @param playlist
     * @param userId
     * @return makes it possible for us to bind created playlist to users.
     * @throws Exception
     */
    public Playlist createPlaylist(Playlist playlist, int userId) throws Exception {
        // SQL statement for creating a new playlist
        String sqlPlaylist = "INSERT INTO FSpotify.dbo.Playlist (PlaylistName) VALUES (?);";
        // SQL statement for linking the playlist with a user
        String sqlUserPlaylist = "INSERT INTO FSpotify.dbo.UserPlaylist (UserID, PlaylistID) VALUES (?, ?);";

        try (Connection conn = databaseConnector.getConnection()) {
            // this makes it able for us to run one statement at the time, so that both will have an effect on the database.
            conn.setAutoCommit(false);

            try (PreparedStatement stmtPlaylist = conn.prepareStatement(sqlPlaylist, Statement.RETURN_GENERATED_KEYS)) {
                // Bind parameters for playlist
                stmtPlaylist.setString(1, playlist.getName());
                // Run the SQL statement for playlist.
                stmtPlaylist.executeUpdate();

                // Get the playlist ID from the DB
                try (ResultSet rs = stmtPlaylist.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newPlaylistId = rs.getInt(1);
                        playlist.setPlaylistID(newPlaylistId); // set the ID of the playlist

                        try (PreparedStatement stmtUserPlaylist = conn.prepareStatement(sqlUserPlaylist)) {
                            // Bind parameters for UserPlaylist
                            stmtUserPlaylist.setInt(1, userId);
                            stmtUserPlaylist.setInt(2, newPlaylistId);
                            // Run the SQL statement for UserPlaylist
                            stmtUserPlaylist.executeUpdate();
                        }

                        // Commit the transaction
                        conn.commit();

                        return playlist; // The playlist now has an ID set
                    } else {
                        // If we didn't get an ID, something went wrong
                        conn.rollback();
                        throw new SQLException("Creating playlist failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                // If something goes wrong, roll back the transaction
                conn.rollback();
                throw e;
            } finally {
                // here we make the effect to database.
                conn.setAutoCommit(true);
            }
        }
    }

    public Playlist updatePlaylist(Playlist playlist) throws Exception {
        // SQL command
        String sql = "UPDATE FSpotify.dbo.Playlist SET PlaylistName = ? WHERE PlaylistID = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            // Bind parameters
            stmt.setString(1, playlist.getName());
            stmt.setInt(2, playlist.getId());

            // Run the specified SQL statement
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not update playlist", ex);
        }
        return playlist;
    }

    public void deletePlaylist(Playlist playlist) throws Exception {
        // SQL command
        String sql = "delete from FSpotify.dbo.Playlist WHERE PlaylistID = ?;";
        String sql1 = "DELETE From FSpotify.dbo.PlaylistSongs WHERE PlaylistID = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql1))
        {
            // Bind parameters
            stmt.setInt(1, playlist.getId());

            // Run the specified SQL statement
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            // create entry in log file
            ex.printStackTrace();
            throw new Exception("Could not delete playlist", ex);
        }

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            // Bind parameters
            stmt.setInt(1, playlist.getId());

            // Run the specified SQL statement
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            // create entry in log file
            ex.printStackTrace();
            throw new Exception("Could not delete playlist", ex);
        }
    }
    private void deletePlayListWithSongs(PlaylistSongs playlistSongs) throws Exception {
        // SQL command
        String sql = "delete from FSpotify.dbo.PlaylistSongs WHERE PlaylistID = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            // Bind parameters
            stmt.setInt(1, playlistSongs.getPlaylistID());

            // Run the specified SQL statement
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            // create entry in log file
            ex.printStackTrace();
            throw new Exception("Could not delete playlist", ex);
        }
    }

    public List<Playlist> getUserPlaylist(int userID) throws Exception {
        ArrayList<Playlist> allUsersPlaylist = new ArrayList<>();
        String sql =
                "SELECT p.PlaylistID, p.PlaylistName " +
                        "FROM FSpotify.dbo.Playlist p " +
                        "JOIN FSpotify.dbo.UserPlaylist up ON p.PlaylistID = up.PlaylistID " +
                        "WHERE up.UserID = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, userID);


            // Loop through rows from the database result set
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    //Map DB row to User object
                    int playlistIDid = rs.getInt("PlaylistID");
                    String playlistName = rs.getString("PlaylistName");

                    Playlist userPlaylist = new Playlist(playlistIDid, playlistName);
                    allUsersPlaylist.add(userPlaylist);
                }
            }
            return allUsersPlaylist;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get playlist from database", ex);
        }
    }
}