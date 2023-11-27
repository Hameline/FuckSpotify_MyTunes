package mytunes.bll;

import mytunes.be.Playlist;
import mytunes.dal.IPlaylistDataAccess;
import mytunes.dal.db.DAO_DB_Playlists;

import java.io.IOException;
import java.util.List;

public class PlaylistManager {

    private IPlaylistDataAccess DAO_DB;

    public PlaylistManager() throws IOException {
        //DAO_DB = new DAO_DB_Playlists();
    }
    public List<Playlist> getAllPlaylists() throws Exception {
        return DAO_DB.getAllPlaylists();
    }
}
