package mytunes.bll;

import mytunes.be.PlaylistSongs;
import mytunes.be.Song;
import mytunes.dal.IPlaylistSongsDataAccess;
import mytunes.dal.db.DAO_DB_PlaylistSongs;

import java.io.IOException;
import java.util.List;

public class PlaylistSongsManager {

    private static IPlaylistSongsDataAccess DAO_DB;

    public PlaylistSongsManager() throws IOException {
        DAO_DB = new DAO_DB_PlaylistSongs();
    }


    public PlaylistSongs addSongToPlaylist(PlaylistSongs newPlaylistSong) throws Exception {
        return DAO_DB.addSongToPlaylist(newPlaylistSong);
    }

    public static Song removeSongFromPlaylist(Song removedPlaylistSong) throws Exception {
        return DAO_DB.removeSongFromPlaylist(removedPlaylistSong);
    }

    public List<Song> fetchSongsForPlaylist(int playlistId) throws Exception {
        // Assuming playlistId is the ID of the playlist for which you want to fetch songs
        return DAO_DB.getPlaylistSong(playlistId);
    }
}