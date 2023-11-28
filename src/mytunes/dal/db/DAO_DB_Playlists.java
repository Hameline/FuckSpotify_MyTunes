package mytunes.dal.db;

import mytunes.be.Playlist;
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
            String sql = "SELECT * FROM dbo.Playlist;";
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {

                //Map DB row to Playlist object
                int id = rs.getInt("id");
                String name = rs.getString("name");

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

    public Playlist createPlaylist(Playlist playlist) throws Exception {
        // SQL command
        String sql = "INSERT INTO dbo.Playlist (Name) VALUES (?,?);";

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
    }

    public Playlist updatePlaylist(Playlist playlist) throws Exception {
        // SQL command
        String sql = "UPDATE dbo.Playlist SET Name = ? WHERE ID = ?";

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

    public Playlist deletePlaylist(Playlist playlist) throws Exception {
        // SQL command
        String sql = "delete from dbo.Playlist WHERE ID = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Bind parameters
            stmt.setInt(1, playlist.getId());

            // Run the specified SQL statement
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not create playlist", ex);
        }
        return playlist;
    }
}