package mytunes.be;

public class UserPlaylist {
    private int userID, playlistID;
    private String playlistName;

    public UserPlaylist(int userID, int playlistID, String playlistName) {
        this.userID = userID;
        this.playlistID = playlistID;
        this.playlistName = playlistName;
    }

    public int getUserID() {
        return userID;
    }

    private void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPlaylistID() {
        return playlistID;
    }

    private void setPlaylistID(int playlistID) {
        this.playlistID = playlistID;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    private void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
}
