package mytunes.be;

public class Genre {

    private String type;
    private int id, artistID;

    public Genre (String type, int id, int artistID){
        this.type = type;
        this.id = id;
        this.artistID = artistID;
    }

    @Override
    public String toString() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArtistID() {
        return artistID;
    }

    public void setArtistID(int artistID) {
        this.artistID = artistID;
    }

    public String toLowerCase() {
        return toString();
    }
}
