package mytunes.be;

public class Users {

    private int userID;
    private String userName, userEmail, userPassword;


    /**
     *
     * @param userID = id in database
     * @param userName = username in database
     * @param userEmail = user email in database
     * @param userPassword = password from database.
     */
    public Users (int userID, String userName, String userEmail, String userPassword){
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }


    public int getUserID() {
        return userID;
    }

    private void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    private void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    private void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    private void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
