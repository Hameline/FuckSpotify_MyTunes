package mytunes.dal.db;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import mytunes.be.Genre;
import mytunes.be.Song;
import mytunes.be.Users;
import mytunes.dal.IGenreDataAccess;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO_DB_Users  {

    private MyTunesDataBaseConnector databaseConnector;

    public DAO_DB_Users() throws IOException {
        databaseConnector = new MyTunesDataBaseConnector();
    }

    public List<Users> getAllUsers() throws Exception {
        ArrayList<Users> allUsers = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM FSpotify.dbo.Users;";
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {

                //Map DB row to Playlist object
                int id = rs.getInt("UserID");
                String userName = rs.getString("UserName");
                String email = rs.getNString("UserEmail");
                String password = rs.getNString("UserPassword");

                Users user = new Users(id, userName, email, password);
                allUsers.add(user);
            }
            return allUsers;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get playlist from database", ex);
        }
    }

    public Users createUser(Users users) throws Exception {
        // SQL command
        String sql = "INSERT INTO FSpotify.dbbo.Users (UserName, UserEmail, UserPassword) VALUES (?, ?, ?)";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Bind parameters
            stmt.setString(1, users.getUserName());
            stmt.setString(2, users.getUserEmail());
            stmt.setString(3, users.getUserPassword());

            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }

            // Create user object and send up the layers
            Users createdUser = new Users(id, users.getUserName(), users.getUserEmail(), users.getUserPassword());

            return createdUser;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create user", ex);
        }
    }

    public Users updateUser(Users user) throws Exception {
        // SQL command
        String sql = "update FSpotify.dbo.Users set UserName = ?, UserEmail = ?, UserPassword = ? where UserID = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            // Bind parameters
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getUserEmail());
            stmt.setString(3, user.getUserPassword());
            stmt.setInt(4, user.getUserID());


            // Run the specified SQL statement
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not update user", ex);
        }
        return user;
    }

    public boolean validateUser(String userName, String password){

        String sql = "SELECT * from FSpotify.dbo.Users where UserName = ? and UserPassword = ?";

        try (Connection conn = databaseConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, userName);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
