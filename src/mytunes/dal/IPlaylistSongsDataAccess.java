package mytunes.dal;

import mytunes.be.Playlist;
import mytunes.be.PlaylistSongs;

import java.util.List;

public interface IPlaylistSongsDataAccess {

    List<PlaylistSongs> getAllPlaylistSongs() throws Exception;

    public PlaylistSongs addSongToPlaylist(PlaylistSongs playlistSongs) throws Exception;

    public PlaylistSongs removeSongFromPlaylist(PlaylistSongs playlistSongs) throws Exception;
}