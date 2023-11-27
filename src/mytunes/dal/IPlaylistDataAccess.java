package mytunes.dal;

import mytunes.be.Playlist;

import java.util.List;

public interface IPlaylistDataAccess {

    List<Playlist> getAllPlaylists() throws Exception;

    public Playlist createPlaylist(Playlist playlist) throws Exception;

    public Playlist updatePlaylist(Playlist playlist) throws Exception;

    public Playlist deletePlaylist(Playlist playlist) throws Exception;
}
