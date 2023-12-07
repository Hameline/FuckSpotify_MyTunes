package mytunes.dal;

import mytunes.be.Users;

import java.util.List;

public interface IUsersDataAccess {
    List<Users> getAllUsers() throws Exception;

    Users createUser(Users newUser) throws Exception;

    Users updateUser(Users user) throws Exception;

    Users deleteUser(Users user) throws Exception;

    boolean validateUser(String userName, String password) throws Exception;
}