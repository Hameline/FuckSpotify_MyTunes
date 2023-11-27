package mytunes.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.bll.PlaylistManager;
import mytunes.bll.SongManager;

public class MRSModel {

    private ObservableList<Song> listOfSongs;
    private ObservableList<Playlist> listOfPlaylists;
    private Song selectedSong;
    private Playlist selectedPlaylist;
    private SongManager songManager;
    private PlaylistManager playlistManager;

    public MRSModel() throws Exception {
        songManager = new SongManager();
        listOfSongs = FXCollections.observableArrayList();
        listOfSongs.addAll(songManager.getAllSongs());

        playlistManager = new PlaylistManager();
        listOfPlaylists = FXCollections.observableArrayList();
        listOfPlaylists.addAll(playlistManager.getAllPlaylists());
    }

    public ObservableList<Song> getListOfSongs() {
        return listOfSongs;
    }
}