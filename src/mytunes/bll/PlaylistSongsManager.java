package mytunes.bll;

import mytunes.be.PlaylistSongs;
import mytunes.bll.util.SongSearcher;
import mytunes.dal.IPlaylistSongsDataAccess;
import mytunes.dal.db.DAO_DB_PlaylistSongs;

import java.io.IOException;
import java.util.List;

public class PlaylistSongsManager {
    private SongSearcher songSearcher = new SongSearcher();
    private IPlaylistSongsDataAccess DAO_DB;

    public PlaylistSongsManager() throws IOException {
        DAO_DB = new DAO_DB_PlaylistSongs();
    }

    public List<PlaylistSongs> getAllSongsFromPlaylist() throws Exception {
        return DAO_DB.getAllPlaylistSongs();
    }


    public PlaylistSongs addSongToPlaylist(PlaylistSongs newPlaylistSong) throws Exception {
        return DAO_DB.addSongToPlaylist(newPlaylistSong);
    }

    public PlaylistSongs removeSongFromPlaylist(PlaylistSongs removedPlaylistSong) throws Exception {
        return DAO_DB.removeSongFromPlaylist(removedPlaylistSong);
    }
}