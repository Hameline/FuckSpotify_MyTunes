package mytunes.be;

public class Song {
    private String title;
    private double time;
    private String genre;

    public Song(String title, double time, String genre) {
        this.title = title;
        this.time = time;
        this.genre = genre;
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

    public void setTime(double time) {
        this.time = time;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}