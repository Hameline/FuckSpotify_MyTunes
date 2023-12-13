package mytunes.bll;


import mytunes.be.Users;
import mytunes.dal.db.DAO_DB_Users;

import java.io.IOException;
import java.util.List;

public class UsersManager {

    private DAO_DB_Users  DAO_DB;

    public UsersManager() throws IOException {
        DAO_DB = new DAO_DB_Users();
    }

    public List<Users> getAllUsers() throws Exception {
        return DAO_DB.getAllUsers();
    }

    /**
     * This method is not implemented yet. It would need a user email. This is not implemented in the
     * database - so we have left it out-
     * @param newUser
     * @return
     * @throws Exception
     */
    public Users createUser(Users newUser) throws Exception {
        return DAO_DB.createUser(newUser);
    }

    public Users updateUser(Users user) throws Exception {
        return DAO_DB.updateUser(user);
    }

    // this method is not implemented - it would need an email from the user, and this is not possible.
    /*public Users deleteUser(Users user) throws Exception {
        return DAO_DB.deleteUser(user);
    }*/

    /**
     *
     * @param userName
     * @param password
     * @return the sql statement here.
     * @throws Exception
     */
    public Users validateUser(String userName, String password) throws Exception {
        return DAO_DB.validateUser(userName, password);
    }
}
