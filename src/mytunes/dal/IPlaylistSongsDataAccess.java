package mytunes.dal;

import mytunes.be.PlaylistSongs;
import mytunes.be.Song;

import java.util.List;

public interface IPlaylistSongsDataAccess {

    List<Song> getPlaylistSong(int playlistId) throws Exception;

    PlaylistSongs addSongToPlaylist(PlaylistSongs playlistSongs) throws Exception;

    PlaylistSongs removeSongFromPlaylist(PlaylistSongs playlistSongs) throws Exception;


    List<Song> fetchSongsForPlaylist(int playlistId) throws Exception;
}