package mytunes.bll;

import mytunes.be.Playlist;
import mytunes.bll.util.PlaylistSearcher;
import mytunes.dal.IPlaylistDataAccess;
import mytunes.dal.db.DAO_DB_Playlists;

import java.io.IOException;
import java.util.List;

public class PlaylistManager {
    private PlaylistSearcher playlistSearcher = new PlaylistSearcher();
    private IPlaylistDataAccess DAO_DB;

    public PlaylistManager() throws IOException {
        DAO_DB = new DAO_DB_Playlists();
    }

    public List<Playlist> getAllPlaylists() throws Exception {
        return DAO_DB.getAllPlaylists();
    }

    public List<Playlist> searchPlaylist(String query) throws Exception {
        List<Playlist> allPlaylists = getAllPlaylists();
        List<Playlist> searchResult = playlistSearcher.search(allPlaylists, query);
        return searchResult;
    }

    public Playlist createPlaylist(Playlist newPlaylist, int userID) throws Exception {
        return DAO_DB.createPlaylist(newPlaylist, userID);
    }

    public void deletePlaylist(Playlist deletedPlaylist) throws Exception {
        DAO_DB.deletePlaylist(deletedPlaylist);
    }

    public Playlist updatePlaylist(Playlist selectedPlaylist) throws Exception {
        return DAO_DB.updatePlaylist(selectedPlaylist);
    }

    public List<Playlist> getUserPlaylist(int userID) throws Exception {
        return DAO_DB.getUserPlaylist(userID);
    }
}