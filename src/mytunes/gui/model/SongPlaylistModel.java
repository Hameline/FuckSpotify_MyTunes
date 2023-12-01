package mytunes.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.be.Artist;
import mytunes.be.Playlist;
import mytunes.be.PlaylistSongs;
import mytunes.be.Song;
import mytunes.bll.ArtistManager;
import mytunes.bll.PlaylistManager;
import mytunes.bll.PlaylistSongsManager;
import mytunes.bll.SongManager;

import java.util.List;

public class SongPlaylistModel {

    private ObservableList<Song> listOfSongs;
    private ObservableList<Song> songsToBePlayed;
    private ObservableList<Artist> searchedArtist;
    private ObservableList<Playlist> listOfPlaylists;
    private ObservableList<PlaylistSongs> listSongsFromPlaylist;
    private Song selectedSong;
    private Playlist selectedPlaylist;
    private SongManager songManager;
    private ArtistManager artistManager;
    private PlaylistManager playlistManager;
    private PlaylistSongsManager playlistSongsManager;

    public SongPlaylistModel() throws Exception {
        songManager = new SongManager();
        songsToBePlayed = FXCollections.observableArrayList();
        songsToBePlayed.addAll(songManager.getAllSongs());

        playlistManager = new PlaylistManager();
        listOfPlaylists = FXCollections.observableArrayList();
        listOfPlaylists.addAll(playlistManager.getAllPlaylists());

        artistManager = new ArtistManager();
        searchedArtist = FXCollections.observableArrayList();
        searchedArtist.addAll(artistManager.getAllArtist());

        playlistSongsManager = new PlaylistSongsManager();
        listSongsFromPlaylist = FXCollections.observableArrayList();
        listSongsFromPlaylist.addAll(playlistSongsManager.getAllSongsFromPlaylist());

    }

    public ObservableList<Song> getListOfSongs() {
        return songsToBePlayed;
    }
    public ObservableList<PlaylistSongs> getSongsFromPlaylist() {
        return listSongsFromPlaylist;
    }

    public void addSongToPlaylist(PlaylistSongs playlistSongs) throws Exception {
        PlaylistSongs pS = playlistSongsManager.addSongToPlaylist(playlistSongs);
        listSongsFromPlaylist.add(pS);
    }

    public ObservableList<Playlist> getListOfPlaylists() {
        return listOfPlaylists;
    }
    public ObservableList<Artist> getSearchedArtist(){return searchedArtist;}

    public void searchSong(String query) throws Exception {
        List<Song> searchResults = songManager.searchSong(query);
        songsToBePlayed.clear();
        songsToBePlayed.addAll(searchResults);
    }

    public void searchArtist(String query) throws Exception {
        List<Artist> searchArtist = artistManager.searchArtist(query);
        searchedArtist.clear();
        searchedArtist.addAll(searchArtist);
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