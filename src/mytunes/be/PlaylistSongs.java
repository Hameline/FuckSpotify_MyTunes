package mytunes.be;

public class PlaylistSongs {

    private int songID, time, playlistID;
    private String fPath, formatedTime, title;
    private Artist artist;
    private Genre type;
    private Song song;

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getfPath() {
        return fPath;
    }

    public void setfPath(String fPath) {
        this.fPath = fPath;
    }

    public void setFormatedTime(String formatedTime) {
        this.formatedTime = formatedTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Genre getType() {
        return type;
    }

    public void setType(Genre type) {
        this.type = type;
    }



    public PlaylistSongs(int songID, int playlistID, String title, int time, Artist artist, Genre type, String formatedTime, String fPath) {
        this.playlistID = playlistID;
        this.songID = songID;
        this.time = time;
        this.formatedTime = getFormatedTime();
        this.title = title;
        this.artist = artist;
        this.type = type;
        this.fPath = fPath;

    }

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



    /**
     * return null - skal have fixet det.
     * @return
     */
    public String getFormatedTime() {
        if (song != null) {
            return song.getConvertedTime();
        } else {
            return "no time";
        }

    }
}
