package mytunes.be;

public class Playlist {

    private int id;
    private String name;

    /**
     *
     * @param id = id from database
     * @param name = name from database
     */
    public Playlist(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Playlist() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlaylistID(int newPlaylistId) {
    }
}
