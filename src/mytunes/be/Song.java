package mytunes.be;

public class Song {
    private int id;
    private double time;
    private String formatedTime, title;
    private Artist artist;
    private Genre type;


    public Song(int id,String title, double time, Artist artist, Genre type) {
        this.title = title;
        this.time = time;
        this.artist = artist;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getTime() {
        return time;
    }

    public String getConvertedTime() {
        int minutes = (int) time / 60;
        int seconds = (int) time % 60;
        formatedTime = String.format("%d:%02d", minutes, seconds);
        return formatedTime;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Artist getArtist() {
        return this.artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Genre getGenre() {
        return this.type;
    }

    public void setGenre(Genre type) {
        this.type = type;
    }
}