package mytunes.dal;

import mytunes.be.Song;

import java.util.List;

public interface ISongDataAccess {
    List<Song> getAllSongs() throws Exception;

    public Song createSong(Song song) throws Exception;

    public Song updateSong(Song song) throws Exception;

    public Song deleteSong(Song song) throws Exception;
}