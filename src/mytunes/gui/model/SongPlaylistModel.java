package mytunes.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.be.*;
import mytunes.bll.*;

import java.util.List;

public class SongPlaylistModel {

    // ObservableLists to store different types of data
    private ObservableList<Song> listOfSongs;
    private ObservableList<Song> songsToBePlayed;
    private ObservableList<Artist> searchedArtist;
    private ObservableList<Playlist> listOfPlaylists;
    private ObservableList<PlaylistSongs> listSongsFromPlaylist;

    private ObservableList<Song> listOfSongInPlaylist = FXCollections.observableArrayList();

    // Selected song and playlist
    private Song selectedSong;
    private Playlist selectedPlaylist;

    // Managers for handling data operations
    private SongManager songManager;
    private ArtistManager artistManager;
    private GenreManager genreManager;
    private PlaylistManager playlistManager;
    private PlaylistSongsManager playlistSongsManager;

    public SongPlaylistModel() throws Exception {
        songManager = new SongManager();
        genreManager = new GenreManager();
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
        //listSongsFromPlaylist.addAll(playlistSongsManager.getAllSongsFromPlaylist());

    }

    // Getter for the list of songs to be played
    public ObservableList<Song> getListOfSongs() {
        return songsToBePlayed;
    }

    // Getter for the list of songs in a playlist

    /**
     *
     * @param playlistid the id of the selected playlist
     * @return
     * @throws Exception
     */
    public ObservableList<Song> getSongsFromPlaylist(int playlistid) throws Exception {
        List<Song> songs = playlistSongsManager.fetchSongsForPlaylist(playlistid);
        listOfSongInPlaylist.setAll(songs);
        return listOfSongInPlaylist;
    }

    // Method to add a song to a playlist
    public void addSongToPlaylist(PlaylistSongs playlistSongs) throws Exception {
        PlaylistSongs pS = playlistSongsManager.addSongToPlaylist(playlistSongs);
        listSongsFromPlaylist.add(pS);
    }

    // Getter for the list of playlists
    public ObservableList<Playlist> getListOfPlaylists() {
        return listOfPlaylists;
    }

    // Getter for the list of searched artists
    public ObservableList<Artist> getSearchedArtist() {
        return searchedArtist;
    }

    // Method to search for songs based on a query
    public void searchSong(String query) throws Exception {
        List<Song> searchResults = songManager.searchSong(query);
        songsToBePlayed.clear();
        songsToBePlayed.addAll(searchResults);
    }

    // Method to search for artists based on a query
    public void searchArtist(String query) throws Exception {
        List<Artist> searchArtist = artistManager.searchArtist(query);
        searchedArtist.clear();
        searchedArtist.addAll(searchArtist);
    }

    // Method to search for playlists based on a query
    public void searchPlaylist(String query) throws Exception {
        List<Playlist> searchResults = playlistManager.searchPlaylist(query);
        listOfPlaylists.clear();
        listOfPlaylists.addAll(searchResults);
    }

    // Method to create a new song
    public void createSong(Song newSong) throws Exception {
        Song s = songManager.createSong(newSong);
        if (listOfSongs != null) {
            listOfSongs.add(s); // Note: listOfSongs is missing initialization
        }
    }

    // Method to delete a song
    public void deleteSong(Song deletedSong) throws Exception {
        Song s = songManager.deleteSong(deletedSong);
        listOfSongs.remove(s);
    }

    // Method to update song information
    public void updateSong(Song selectedSong) throws Exception {
        songManager.updateSong(selectedSong);

        if (listOfSongs != null) {
            Song s = listOfSongs.get(listOfSongs.indexOf(selectedSong));
            s.setTitle(selectedSong.getTitle());
            s.setTime(selectedSong.getTime());
            s.setGenre(selectedSong.getGenre());
        }
    }

    // Method to create a new playlist
    public void createPlaylist(Playlist newPlaylist) throws Exception {
        Playlist p = playlistManager.createPlaylist(newPlaylist);
        listOfPlaylists.add(p);
    }

    // Method to delete a playlist
    public void deletePlaylist(Playlist deletedPlaylist) throws Exception {
        playlistManager.deletePlaylist(deletedPlaylist);
        listOfPlaylists.remove(deletedPlaylist);
    }

    // Method to update playlist information
    public void updatePlaylist(Playlist selectedPlaylist) throws Exception {
        playlistManager.updatePlaylist(selectedPlaylist);

        Playlist p = listOfPlaylists.get(listOfPlaylists.indexOf(selectedPlaylist));
        p.setName(selectedPlaylist.getName());
    }

    // Setter for the selected song
    public void setSelectedSong(Song selectedSong) {
        this.selectedSong = selectedSong;
    }

    // Getter for the selected song
    public Song getSelectedSong() {
        return selectedSong;
    }

    // Setter for the selected playlist
    public void setSelectedPlaylist(Playlist selectedPlaylist) {
        this.selectedPlaylist = selectedPlaylist;
    }

    // Getter for the selected playlist
    public Playlist getSelectedPlaylist() {
        return selectedPlaylist;
    }

    public ObservableList<Genre> getAllGenres() throws Exception {
        List<Genre> genreType = genreManager.getAllGenre();
        return FXCollections.observableArrayList(genreType);
    }


    public Artist findArtistName(String artistNameLowercase) throws Exception {
            return artistManager.findArtistByName(artistNameLowercase);
    }

    public Artist createArtist(Artist artist) throws Exception {
        return artistManager.createArtist(artist);
    }

}