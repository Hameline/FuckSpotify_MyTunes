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

    /**
     *
     * @param playlist
     * @param userID
     * @return
     * @throws Exception
     */
    public Playlist updatePlaylist(Playlist playlist, int userID) throws Exception {
        /**
         * This updates a playlist name to what is entered, on the selected playlist.
         * And checks if it avaible in the UserPlaylist table, where UserID and PlayistID is connected.
         */
        String sql = "UPDATE FSpotify.dbo.Playlist SET PlaylistName = ? WHERE PlaylistID = ? " +
                     "AND EXISTS (SELECT 1 FROM FSpotify.dbo.UserPlaylist WHERE UserID = ? AND PlaylistID = ?)";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            // Bind parameters
            stmt.setString(1, playlist.getName());
            stmt.setInt(2, playlist.getId());
            stmt.setInt(3, userID);
            stmt.setInt(4, playlist.getId());

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

    /**
     * Method to delete the playlist from the database.
     * @param playlist
     * @param userID = hold the user id from the database
     * @throws Exception
     */
    public void deletePlaylist(Playlist playlist, int userID) throws Exception {
        // SQL statement
        // first we delete from the playlist from the user.
        String sqlUserP = "DELETE FROM FSpotify.dbo.UserPlaylist WHERE UserID = ? AND PlaylistID = ?;";
        // then we delete from the table where playlist and songs are connected.
        String sqlPS = "DELETE FROM FSpotify.dbo.PlaylistSongs WHERE PlaylistID = ?;";
        // then we delete from the playlist from the database.
        String sqlPl = "DELETE FROM FSpotify.dbo.Playlist WHERE PlaylistID = ? ";

        try (Connection conn = databaseConnector.getConnection()) {
            // we start by deleting it from the connection with the user.
            try (PreparedStatement upstmt = conn.prepareStatement(sqlUserP)) {
                upstmt.setInt(1, userID);
                upstmt.setInt(2, playlist.getId());
                upstmt.executeUpdate();
            }
            // Here we delete the playlist from the connection to songs.
            try (PreparedStatement stmt = conn.prepareStatement(sqlPS)){
                stmt.setInt(1, playlist.getId());
                stmt.executeUpdate();
            }
            // here we delete the playlist from the database. No need to check for user id anymore.
            try (PreparedStatement pstmt = conn.prepareStatement(sqlPl)) {
                pstmt.setInt(1, playlist.getId());
                pstmt.executeUpdate();
            }
        } catch (SQLException se){
            se.printStackTrace();
            throw new Exception("Failed to delete.", se);
        }
    }

    /**
     * This method collects the playlist connected to a user.
     * Meaning that the user will only see the paylist they are linked to.
     * @param userID
     * @return
     * @throws Exception
     */
    public List<Playlist> getUserPlaylist(int userID) throws Exception {
        ArrayList<Playlist> allUsersPlaylist = new ArrayList<>();
        String sql =
                "SELECT p.PlaylistID, p.PlaylistName " + // get is the p ID and p Name.
                        "FROM FSpotify.dbo.Playlist p " + // from the table Playlist
                        "JOIN FSpotify.dbo.UserPlaylist up ON p.PlaylistID = up.PlaylistID " + // joins with userp
                        // checks playlist for connectivety with user, only display the one with the user.
                        "WHERE up.UserID = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, userID);


            // Collects data from the database
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    //Creates of User object
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