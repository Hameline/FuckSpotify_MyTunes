package mytunes.be;

public class Song {
    private int id, time;
    private String fPath, formatedTime, title;
    private Artist artist;
    private Genre type;



    public Song(int id,String title, int time, Artist artist, Genre type, String formatedTime, String fPath) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.formatedTime = getConvertedTime();
        this.artist = artist;
        this.type = type;
        this.fPath = fPath;
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

    public int getTime() {
        return time;
    }

    public String getConvertedTime() {
        int hours = (int) time / 3600;
        int minutes = (int) (time % 3600) / 60;
        int seconds = (int) time % 60;
        formatedTime = hours > 0
                ? String.format("%d:%02d:%02d", hours, minutes, seconds)
                : String.format("%d:%02d", minutes, seconds);
        return formatedTime;
    }

    public String getFormatedTime(){
        return formatedTime;
    }

    public void setTime(int time) {
        this.time = time;
        this.formatedTime = getConvertedTime();
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

    public String getFPath() {
        return fPath;
    }

    public void setfPath(String fPath) {
        this.fPath = fPath;
    }
    public String getTimeStamp(){
        int minutes = time /60;
        int seconds = time %60;
        String textSeconds;
        if(seconds <= 9){
            textSeconds = "0" + seconds;
        }else{
            textSeconds = ""+ seconds;
        }
        return minutes + ":" + textSeconds;
    }
    public String toString() {
        return this.title;
    }


}