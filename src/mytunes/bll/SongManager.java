package mytunes.bll;

import mytunes.be.Song;
import mytunes.dal.ISongDataAccess;
import mytunes.dal.db.DAO_DB_Songs;

import java.io.IOException;
import java.util.List;

public class SongManager {
    private ISongDataAccess DAO_DB;

    public SongManager() throws IOException {
        DAO_DB = new DAO_DB_Songs();
    }

    public List<Song> getAllSongs() throws Exception {
        return DAO_DB.getAllSongs();
    }

    /*public List<Song> searchSongs(String query) throws Exception {
        List<Song> allSongs = getAllSongs();
        /*List<Song> searchResult = songSearcher.search(allSongs, query);
        return searchResult;
    }*/

    public Song createSong(Song newSong) throws Exception {
        return DAO_DB.createSong(newSong);
    }

    public Song deleteSong(Song deletedSong) throws Exception {
        return DAO_DB.deleteSong(deletedSong);
    }

    public Song updateSong(Song selectedSong) throws Exception {
        return DAO_DB.updateSong(selectedSong);
    }
}