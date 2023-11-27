package mytunes.bll;

import mytunes.be.Song;
import mytunes.dal.ISongDataAccess;
import mytunes.dal.db.DAO_DB_Songs;

import java.io.IOException;
import java.util.List;

public class SongManager {
    private ISongDataAccess DAO_DB;

    public SongManager() throws IOException {
        //DAO_DB = new DAO_DB_Songs();
    }

    public List<Song> getAllSongs() throws Exception {
        return DAO_DB.getAllSongs();
    }
}
