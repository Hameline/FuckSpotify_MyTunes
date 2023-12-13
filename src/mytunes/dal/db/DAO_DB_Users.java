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

    /**
     * retrives all users from the database.
     * @return
     * @throws Exception
     */
    public List<Users> getAllUsers() throws Exception {
        ArrayList<Users> allUsers = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM FSpotify.dbo.Users;";
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {

                //Map DB row to User object
                int id = rs.getInt("UserID");
                String userName = rs.getString("UserName");
                String email = rs.getString("UserEmail");
                String password = rs.getString("UserPassword");

                Users user = new Users(id, userName, email, password);
                allUsers.add(user);
            }
            return allUsers;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get users from database", ex);
        }
    }

    /**
     * Here we use are able to create a new user in the database, and use him for the
     * application. This is not implemented, do to the missing of email and hash encrypting password.
     * @param users
     * @return newUser (createdUser)
     * @throws Exception
     */
    public Users createUser(Users users) throws Exception {
        // SQL statement
        String sql = "INSERT INTO FSpotify.dbbo.Users (UserName, UserEmail, UserPassword) VALUES (?, ?, ?)";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Binds the parameters
            stmt.setString(1, users.getUserName());
            stmt.setString(2, users.getUserEmail());
            stmt.setString(3, users.getUserPassword());

            // executes the statement
            stmt.executeUpdate();

            // Here we get the id that's generated in the database.
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }
            // Creates a user that is sent through the layers
            Users createdUser = new Users(id, users.getUserName(), users.getUserEmail(), users.getUserPassword());
            return createdUser;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create user", ex);
        }
    }

    /**
     *
     * @param user
     * @return
     * @throws Exception
     */
    public Users updateUser(Users user) throws Exception {
        // SQL statement
        String sql = "update FSpotify.dbo.Users set UserName = ?, UserEmail = ?, UserPassword = ? where UserID = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            // Bind parameters
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getUserEmail());
            stmt.setString(3, user.getUserPassword());
            stmt.setInt(4, user.getUserID());


            // Run SQL statement
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not update user", ex);
        }
        return user;
    }

    /**
     * method to validate the user with the password
     * @param userName
     * @param password
     * @return
     */
    public Users validateUser(String userName, String password){
        Users user = null; // starts the user as null
        /**
         * get the userid from the user that is trying to log in, and is checking for
         * if the password  matches that user.
          */
        String sql = "SELECT * from FSpotify.dbo.Users where UserName = ? and UserPassword = ?";

        try (Connection conn = databaseConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, userName);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                int id = rs.getInt("UserID");
                String email = rs.getString("UserEmail");
                user = new Users(id, userName, email, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
