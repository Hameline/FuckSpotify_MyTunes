package mytunes.be;

public class PlaylistSongs {

    private int playlistID;
    private int songID;

    public PlaylistSongs(int playlistID, int songID) {
        this.playlistID = playlistID;
        this.songID = songID;
    }

    public int getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(int id) {
        this.playlistID = id;
    }

    public int getSongID() {
        return songID;
    }

    public void setSongID(int id) {
        this.songID = id;
    }
}
