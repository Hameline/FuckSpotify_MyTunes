package mytunes.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.bll.PlaylistManager;
import mytunes.bll.SongManager;

import java.util.List;

public class SongPlaylistModel {

    private ObservableList<Song> listOfSongs;
    private ObservableList<Playlist> listOfPlaylists;
    private Song selectedSong;
    private Playlist selectedPlaylist;
    private SongManager songManager;
    private PlaylistManager playlistManager;

    public SongPlaylistModel() throws Exception {
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

    public ObservableList<Playlist> getListOfPlaylists() {
        return listOfPlaylists;
    }

    public void searchSong(String query) throws Exception {
        List<Song> searchResults = songManager.searchSong(query);
        listOfSongs.clear();
        listOfSongs.addAll(searchResults);
    }

    public void searchPlaylist(String query) throws Exception {
        List<Playlist> searchResults = playlistManager.searchPlaylist(query);
        listOfPlaylists.clear();
        listOfPlaylists.addAll(searchResults);
    }

    public void createSong(Song newSong) throws Exception {
        Song s = songManager.createSong(newSong);
        listOfSongs.add(s);
    }

    public void deleteSong(Song deletedSong) throws Exception {
        Song s = songManager.deleteSong(deletedSong);
        listOfSongs.remove(s);
    }

    public void updateSong(Song selectedSong) throws Exception {
        songManager.updateSong(selectedSong);

        Song s = listOfSongs.get(listOfSongs.indexOf(selectedSong));
        s.setTitle(selectedSong.getTitle());
        s.setTime(selectedSong.getTime());
        s.setGenre(selectedSong.getGenre());
    }

    public void createPlaylist(Playlist newPlaylist) throws Exception {
        Playlist p = playlistManager.createPlaylist(newPlaylist);
        listOfPlaylists.add(p);
    }

    public void deletePlaylist(Playlist deletedPlaylist) throws Exception {
        Playlist p = playlistManager.deletePlaylist(deletedPlaylist);
        listOfPlaylists.remove(p);
    }

    public void updatePlaylist(Playlist selectedPlaylist) throws Exception {
        playlistManager.updatePlaylist(selectedPlaylist);

        Playlist p = listOfPlaylists.get(listOfPlaylists.indexOf(selectedPlaylist));
        p.setName(selectedPlaylist.getName());
    }

    public void setSelectedSong(Song selectedSong) {
        this.selectedSong = selectedSong;
    }

    public Song getSelectedSong() {
        return selectedSong;
    }

    public void setSelectedPlaylist(Playlist selectedPlaylist) {
        this.selectedPlaylist = selectedPlaylist;
    }

    public Playlist getSelectedPlaylist() {
        return selectedPlaylist;
    }
}