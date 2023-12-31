package mytunes.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.be.*;
import mytunes.bll.*;

import java.util.ArrayList;
import java.util.List;

public class SongPlaylistModel {

    // ObservableLists to store different types of data
    private ObservableList<Song> listOfSongs;
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
    private UsersManager usersManager;


    public SongPlaylistModel() throws Exception {
        songManager = new SongManager();
        genreManager = new GenreManager();
        usersManager = new UsersManager();
        listOfSongs = FXCollections.observableArrayList();
        listOfSongs.addAll(songManager.getAllSongs());

        playlistManager = new PlaylistManager();
        listOfPlaylists = FXCollections.observableArrayList();
        listOfPlaylists.addAll(playlistManager.getAllPlaylists());

        artistManager = new ArtistManager();
        searchedArtist = FXCollections.observableArrayList();
        searchedArtist.addAll(artistManager.getAllArtist());

        playlistSongsManager = new PlaylistSongsManager();
        listSongsFromPlaylist = FXCollections.observableArrayList();

    }

    // Getter for the list of songs to be played
    public ObservableList<Song> getListOfSongs() {
        return listOfSongs;
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
        listOfSongs.clear();
        listOfSongs.addAll(searchResults);
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

    public void removeSongFromPlaylist(PlaylistSongs removedSong) throws Exception {
        PlaylistSongsManager.removeSongFromPlaylist(removedSong);
        listSongsFromPlaylist.remove(removedSong);
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
    public void createPlaylist(Playlist newPlaylist, int userID) throws Exception {
        Playlist p = playlistManager.createPlaylist(newPlaylist, userID);
        listOfPlaylists.add(p);
    }

    // Method to delete a playlist
    public void deletePlaylist(Playlist deletedPlaylist, int userID) throws Exception {
        playlistManager.deletePlaylist(deletedPlaylist, userID);
        listOfPlaylists.remove(deletedPlaylist);
    }

    // Method to update playlist information
    public void updatePlaylist(Playlist selectedPlaylist, int userID) throws Exception {
        playlistManager.updatePlaylist(selectedPlaylist, userID);

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

    public int calculateTotalTime(List<Song> songs) {
        int totalTime = 0;
        for (Song song : songs) {
            totalTime += song.getTime(); // getTime() should return the duration of the song in seconds
        }
        return totalTime;
    }

    /**
     * This method calculates the total time of the songs i the selected playlist and shower it to the user.
     * @param totalTime = of all the songs in the playlist.
     * @return
     */
    public String formatDuration(int totalTime) {
        int hours = totalTime / 3600;
        int minutes = (totalTime % 3600) / 60;
        int seconds = totalTime % 60;

        if (hours > 0) {
            // shows the time as h:min:sec
            return String.format("%d h. %02d min. %02d sec.", hours, minutes, seconds);
        } else if (minutes > 0) {
            // shows time as min:sec
            return String.format("%02d min. %02d sec.", minutes, seconds);
        } else {
            // shows time as sec
            return String.format("%02d sec.", seconds);
        }
    }

    public List<Playlist> getUserPlaylist(int userID) throws Exception {
        return playlistManager.getUserPlaylist(userID);
    }

    /**
     * This method collect and put the user's id in a list that we can use in other methods.
     * @return
     * @throws Exception
     */
    public List<Integer> getUserIDs() throws Exception {
        List<Users> allUsers = usersManager.getAllUsers();
        List<Integer> userIDs = new ArrayList<>();
        for (Users user : allUsers) {
            userIDs.add(user.getUserID());
        }
        return userIDs;
    }
}