package mytunes.be;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import mytunes.dal.db.DAO_DB_Songs;

import java.io.File;

public class MusicPlayer {
    protected MediaPlayer mediaPlayer;
    protected Media media;
    protected Song song;
    //Making some get and set methods for our mediaplayer class.
    public Media getMedia() {
        return media;
    }

    public Song getSong() {
        return song;
    }

    //sets the song that should be played
    public void setSong(Song song) {
        if (song != null && this.song != song) {
            this.song =song;

            //gets filepath to play song
            if (!song.getFPath().isBlank()) {
                media = new Media(new File(DAO_DB_Songs.getFpath()).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
            }
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
