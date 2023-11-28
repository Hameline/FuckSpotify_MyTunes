package mytunes.be;

public class Song {
    private int id;
    private double time;
    private String genre, artist, formatedTime, title;

    public Song(int id,String title, double time, String genre) {
        this.title = title;
        this.time = time;
        this.genre = genre;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}