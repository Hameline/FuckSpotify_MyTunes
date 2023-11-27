package mytunes.dal;

import mytunes.be.Playlist;

import java.util.List;

public interface IPlaylistDataAccess {

    List<Playlist> getAllPlaylists();

    public Playlist createMovie(Playlist playlist) throws Exception;

    public Playlist updateMovie(Playlist playlist) throws Exception;

    public Playlist deleteMovie(Playlist playlist) throws Exception;
}
