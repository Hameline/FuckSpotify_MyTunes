package mytunes.gui.controller;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mytunes.be.*;
import mytunes.bll.PlaylistSongsManager;
import mytunes.gui.model.SongPlaylistModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class MainViewController<songPath> extends BaseController implements Initializable {

    @FXML
    private ListView vboxlistArtist;
    @FXML
    private TableColumn tblViewSongInPlaylistSongUser, tblViewSongInPlaylistDurationUser, tblViewSongInPlaylistGenreUser, tblViewSongInPlaylistArtistUser;

    @FXML
    private Label txtTotalTime;
    @FXML
    private Button btnPlay, btnDelete, nextSong, previousSong, handlePlaySong, btnMoveUp, btnMoveDown, btnShuffle;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Label songTimer, songTimer2;
    @FXML
    private Label lblSelectPlaylist;
    @FXML
    private TableColumn tblViewSongInPlaylistSong;
    @FXML
    private TableColumn tblViewSongInPlaylistDuration;
    @FXML
    private TableColumn tblViewSongInPlaylistArtist;
    @FXML
    private TableColumn tblViewSongInPlaylistGenre;
    @FXML
    private GridPane btnMenuPlaylist;
    @FXML
    private TableView<UserPlaylist> tblViewSongsInPlaylistUser;
    @FXML
    private TableView<Song> tblViewSongsInPlaylist;
    @FXML
    private Label lblPlaylistName;
    @FXML
    private Button btnCreate, btnUpdate;
    @FXML
    private TableView tblViewPlaylist;
    @FXML
    private Button btnNewPlaylist;
    @FXML
    private Button btnMainMenu;
    @FXML
    private TableColumn tblViewSearchSong;
    @FXML
    private TableColumn tblViewSearchArtist;
    @FXML
    private TableColumn tblViewSearchGenre;
    @FXML
    private ButtonBar btnBarSong;
    @FXML
    private VBox vBoxDefault;
    @FXML
    private TableView<Song> tblViewSearch;
    @FXML
    private TableColumn tblViewPlaylistPlaylist;
    @FXML
    private TableColumn tblViewPlaylistUser;
    @FXML
    private TextField txtSearchField;
    @FXML
    private ListView vboxlistSuggested;
    private SongPlaylistModel songPlaylistModel;
    private CreateUpdatePlaylistViewController createUpdatePlaylistViewController;
    private Playlist selectedPlaylist = null;
    private Playlist storePlaylist = null;
    private Song selectedSong;
    private Song storeSong;
    private boolean allowSongsInPlaylistView = true;
    private MediaPlayer mediaPlayer;
    @FXML
    private TableColumn<Song, String> tblViewSearchDuration;
    private PlaylistSongsManager playlistSongsManager = new PlaylistSongsManager();
    private int switchFromPlayAndPause = 1;

    public MainViewController() throws IOException {
        try {
            songPlaylistModel = new SongPlaylistModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setup();
        defaultMenu();
        tblViewSearchDuration.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getTimeStamp()));


        txtSearchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                songPlaylistModel.searchSong(newValue);
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        });
    }

    // An error message that shows up when something goes wrong
    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }

    // A setup method that comes with the abstract class "BaseController". Here we setup how the program should look once opened
    @Override
    public void setup() {
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        if (songPlaylistModel != null) {
            tblViewSearch.setItems(songPlaylistModel.getListOfSongs());
            vboxlistSuggested.setItems(songPlaylistModel.getListOfSongs());
            tblViewPlaylist.setItems(songPlaylistModel.getListOfPlaylists());

        }

        tblViewSearchArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        tblViewSearchGenre.setCellValueFactory(new PropertyValueFactory<>("Genre"));
        tblViewSearchSong.setCellValueFactory(new PropertyValueFactory<>("title"));
        tblViewSearchDuration.setCellValueFactory(new PropertyValueFactory<>("formatedTime"));

        volumeSlider.setMin(0);
        volumeSlider.setMax(100);

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue() / 100.0);
            }
        });

        tblViewPlaylistPlaylist.setCellValueFactory(new PropertyValueFactory<>("name"));
        tblViewSearch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Song>() {
            @Override
            public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
                btnUpdate.setDisable(newValue == null);
            }
        });

        tblViewSearch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Song>() {
            @Override
            public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
                btnDelete.setDisable(newValue == null);
            }
        });
    }

    private void defaultMenu() {
        // MAKES the TBL VIEW INVISIBLE
        tblViewSearch.setVisible(false);

        // MAKES the BUTTON BAR INVISIBLE
        btnBarSong.setVisible(false);

        lblPlaylistName.setVisible(false);

        tblViewSongsInPlaylist.setVisible(false);

        txtSearchField.setText("");

        vBoxDefault.setVisible(true);

        btnMenuPlaylist.setVisible(false);

        btnNewPlaylist.setText("+ New Playlist");

        allowSongsInPlaylistView = true;
        storeSong = null;
        storePlaylist = null;
        lblSelectPlaylist.setVisible(false);

        tblViewSearch.setItems(songPlaylistModel.getListOfSongs());
        //tblViewPlaylist.setItems(songPlaylistModel.getListOfPlaylists());

    }

    @FXML
    private void HandleNewPlaylist(ActionEvent actionEvent) throws IOException {
        Playlist selectedPlaylist = (Playlist) tblViewPlaylist.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null) {

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CreateUpdatePlaylistView.fxml"));
            Parent popupWindow = loader.load();

            CreateUpdatePlaylistViewController controller = loader.getController();
            controller.setModel(songPlaylistModel);
            controller.setPlaylist(selectedPlaylist);
            controller.setup();

            Stage PopupWindow = new Stage();
            PopupWindow.setTitle("Create/Update Playlist");
            PopupWindow.initModality(Modality.APPLICATION_MODAL);
            PopupWindow.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

            PopupWindow.setScene(new Scene(popupWindow));
            PopupWindow.showAndWait();

            tblViewPlaylist.refresh();
        }
        if (selectedPlaylist == null) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CreateUpdatePlaylistView.fxml"));
            Parent popupWindow = loader.load();

            CreateUpdatePlaylistViewController controller = loader.getController();
            controller.setModel(songPlaylistModel);
            controller.setup();

            Stage PopupWindow = new Stage();
            PopupWindow.setTitle("Create/Update Playlist");
            PopupWindow.initModality(Modality.APPLICATION_MODAL);
            PopupWindow.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

            PopupWindow.setScene(new Scene(popupWindow));
            PopupWindow.showAndWait();

            tblViewPlaylist.refresh();
        }
    }

    // A button that goes back to the front screen (the button is the logo in the top left corner
    @FXML
    private void HandleMainMenu(ActionEvent actionEvent) {
        defaultMenu();
        setup();
    }

    @FXML
    private void handleSearchField(KeyEvent keyEvent) {
        // Detects if the ENTER KEY have been PRESSED
        if (keyEvent.getCode() == KeyCode.ENTER) {
            // Looks TO SEE if the TXT SEARCH FIELD is NOT EMPTY
            if (!(txtSearchField.getText().isEmpty())) {

                // MAKES the TBL VIEW VISIBLE                tblViewSearch.setVisible(true);

                // MAKES the VBOX INVISIBLE
                vBoxDefault.setVisible(false);

                // MAKES the BUTTON BAR VISIBLE
                btnBarSong.setVisible(true);

                tblViewSongsInPlaylist.setVisible(false);

                btnMenuPlaylist.setVisible(false);
            }
            // Looks TO SEE if the TXT SEARCH FIELD is EMPTY
            if (txtSearchField.getText().isEmpty()) {

                // MAKES the TBT VIEW INVISIBLE
                tblViewSearch.setVisible(false);

                // MAKES the VBOX VISIBLE
                vBoxDefault.setVisible(true);

                // MAKES the BUTTON BAR INVISIBLE
                btnBarSong.setVisible(false);
            }
        }
    }

    // A button that shows up once you are on the search table. When pressing the button a window will pop up with all
    // the information you have to insert and here you will also put in the file for the song.
    // The window will automatically close when you press the create button in the new window.
    @FXML
    private void handelCreate(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CreateUpdateSongView.fxml"));
        Parent popupWindow = loader.load();

        CreateUpdateSongViewController controller = loader.getController();
        controller.setModel(songPlaylistModel);
        controller.setup();

        Stage PopupWindow = new Stage();
        PopupWindow.setTitle("Create/Update Song");
        PopupWindow.initModality(Modality.APPLICATION_MODAL);
        PopupWindow.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

        PopupWindow.setScene(new Scene(popupWindow));
        PopupWindow.showAndWait();

        tblViewSearch.refresh();

    }

    @FXML
    private void handleUpdate(ActionEvent actionEvent) throws IOException {

        Song selectedSong = (Song) tblViewSearch.getSelectionModel().getSelectedItem();
        if (selectedSong == null) {
            displayError(new Throwable("No song selected"));
            return;
        }

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CreateUpdateSongView.fxml"));
        Parent popupWindow = loader.load();

        CreateUpdateSongViewController controller = loader.getController();
        controller.setModel(songPlaylistModel);
        controller.setSelectedSong(selectedSong);
        controller.setup();

        Stage PopupWindow = new Stage();
        PopupWindow.setTitle("Create/Update Song");
        PopupWindow.initModality(Modality.APPLICATION_MODAL);
        PopupWindow.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

        PopupWindow.setScene(new Scene(popupWindow));
        PopupWindow.showAndWait();

        tblViewSearch.refresh();

    }

    // Deletes the selected song and calls the confirmationAlertSOng method
    public void handleDelete(ActionEvent actionEvent) throws Exception {
        try {
            confirmationAlertSong();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Could not delete song", e);
        }
    }

    // Gives you an alert for when you have selected and song and pressed the delete button
    public void confirmationAlertSong() throws Exception {
        /**
         * skal have lavet en if statement der kontrollere om
         * sang er i en playliste før den skal slettes.
         */
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("You are about to delete a Song");
        alert.setContentText("Are you sure you want to delete?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Song deletedSong = tblViewSearch.getSelectionModel().getSelectedItem();
            songPlaylistModel.deleteSong(deletedSong);
        } else {
        }
    }

    // Deletes the selected playlist first the go to the playlist then select the dropdown menu  above the songs
    // titled the playlists name and then clicking delete playlist
    @FXML
    private void handleDeletePlaylist(ActionEvent actionEvent) {
        selectedPlaylist = (Playlist) tblViewPlaylist.getSelectionModel().getSelectedItem();

        if (selectedPlaylist != null) {
            try {
                // Delete movie in DAL layer (through the layers)
                songPlaylistModel.deletePlaylist(selectedPlaylist);
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        }
    }

    /**
     * Here we handle the event whem the user selects a playlist.
     * The user can see what song are in the selected playlist,
     * the same way as the user sees them in the search window.
     *
     * @param mouseEvent
     * @throws Exception
     */
    @FXML
    private void handlePlaylist(MouseEvent mouseEvent) throws Exception {
        if (allowSongsInPlaylistView == true) {
            selectedPlaylist = (Playlist) tblViewPlaylist.getSelectionModel().getSelectedItem();
            if (selectedPlaylist != null) {
                vBoxDefault.setVisible(false);
                tblViewSearch.setVisible(false);
                btnBarSong.setVisible(false);
                btnMenuPlaylist.setVisible(true);
                tblViewSongsInPlaylist.setVisible(true);
                lblPlaylistName.setVisible(true);

                btnNewPlaylist.setText("Change Playlist Name");

                int playlistId = selectedPlaylist.getId(); //get the id of the selected playlist.
                try {
                    // here we fetch the songs from the database that is connected the the playlist with the id.
                    List<Song> songs = playlistSongsManager.fetchSongsForPlaylist(playlistId);
                    updateTotalTimeTextField(songs);
                    ObservableList<Song> songObservableList = FXCollections.observableArrayList(songs);
                    tblViewSongsInPlaylist.setItems(songObservableList); // set the items(songs) in the the view.
                    tblViewSongsInPlaylist.refresh();
                } catch (Exception e) {
                    displayError(e);
                    e.printStackTrace();
                }

                lblPlaylistName.setText(selectedPlaylist.getName());
                tblViewSongsInPlaylist.setItems(songPlaylistModel.getSongsFromPlaylist(selectedPlaylist.getId()));
                tblViewSongInPlaylistArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
                tblViewSongInPlaylistGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
                tblViewSongInPlaylistSong.setCellValueFactory(new PropertyValueFactory<>("title"));
                tblViewSongInPlaylistDuration.setCellValueFactory(new PropertyValueFactory<>("formatedTime"));
                tblViewSongsInPlaylist.refresh();


            } else {
                defaultMenu();
            }
        }
        if (allowSongsInPlaylistView == false) {
            selectedPlaylist = (Playlist) tblViewPlaylist.getSelectionModel().getSelectedItem();
            storePlaylist = selectedPlaylist;
            if (storeSong != null && storePlaylist != null) {
                PlaylistSongs newPlaylistSongs = new PlaylistSongs(storePlaylist.getId(), storeSong.getId());
                try {

                    songPlaylistModel.addSongToPlaylist(newPlaylistSongs);

                } catch (Exception e) {
                    displayError(e);
                    e.printStackTrace();
                } finally {
                    defaultMenu();
                    tblViewSearch.setVisible(true);
                    btnBarSong.setVisible(true);
                    vBoxDefault.setVisible(false);
                }
            } else {
                System.out.println("stored playlist is empty same goes for stored song");
            }
        }
    }

    // Adds the selected song to a selected playlist by first clicking on the song in the search table and then
    // clicking the desired playlist
    @FXML
    private void handleAddSongToPlaylist(MouseEvent mouseEvent) {
        selectedSong = (Song) tblViewSearch.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            allowSongsInPlaylistView = false;
            storeSong = selectedSong;
            lblSelectPlaylist.setVisible(true);
        }
    }

    // Play method that will be used in out "handlePlaySong" method
    public void play() throws Exception {
        // Plays the song in the search table
        Song songToPlaySearch = tblViewSearch.getSelectionModel().getSelectedItem();
        if (songToPlaySearch != null) {
            playSong(songToPlaySearch.getFPath());
        }
        // Plays the song in the playlist table
        Song songToPlayPlaylist = tblViewSongsInPlaylist.getSelectionModel().getSelectedItem();
        if (songToPlayPlaylist != null) {
            playSong(songToPlayPlaylist.getFPath());
        }
        // Plays the song in the suggested table
        Song songToPlaySuggested = (Song) vboxlistSuggested.getSelectionModel().getSelectedItem();
        if (songToPlaySuggested != null) {
            playSong(songToPlaySuggested.getFPath());
        }
    }

    // Pause method that will be used in out "handlePlaySong" method
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    // ActionEvent for our play button that plays a song and changes the image of the button to a pause symbol that then
    // when pressed again pauses the song
    public void handlePlaySong(ActionEvent actionEvent) throws Exception {
        if (switchFromPlayAndPause == 1) {
            btnPlay.setText("||");
            switchFromPlayAndPause = 2;
            play();
        } else if (switchFromPlayAndPause == 2) {
            btnPlay.setText("▶");
            switchFromPlayAndPause = 1;
            pause();
        }
    }

    public void playSong(String songPath) throws Exception {
        File file = new File(songPath);// Creates a new file with the parameter songPath
        Media mSong = new Media(file.getAbsoluteFile().toURI().toString());// Creates a new media with the name mSong,
        // and takes the previous file, gets an abstract pathname, changes it to URI and then to string, so the program can read it.
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.stop(); // If the media player isn't null, and the media player is playing, it'll stop.
        }
        try {
            mediaPlayer = new MediaPlayer(mSong);
            playingTimer();
            mediaPlayer.play();
            mediaPlayer.setOnEndOfMedia(() -> {
                Song song;
                if (tblViewSearch.getSelectionModel().getSelectedIndex() != -1) {
                    int nextSongIndex = tblViewSearch.getSelectionModel().getSelectedIndex() + 1;
                    tblViewSearch.getSelectionModel().select(nextSongIndex);
                    song = tblViewSearch.getSelectionModel().getSelectedItem();

                } else if (tblViewSongsInPlaylist.getSelectionModel().getSelectedIndex() != -1) {
                    int nextSongIndex = tblViewSongsInPlaylist.getSelectionModel().getSelectedIndex() + 1;
                    tblViewSongsInPlaylist.getSelectionModel().select(nextSongIndex);
                    song = tblViewSongsInPlaylist.getSelectionModel().getSelectedItem();
                } else {
                    return;
                }
                try {
                    playSong(song.getFPath());

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            });
        } catch (Exception exc) {
            exc.printStackTrace();
            throw new Exception("Could not play song", exc);
        }
    }

    // Simply stops the currently playing song
    public void handleStopSong(ActionEvent actionEvent) throws Exception {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    // A button that when pressed skips to the next song on the list of songs or on the playlist whichever you have selected
    public void handleNextSong(ActionEvent actionEvent) throws Exception {
        int indexSearch = tblViewSearch.getSelectionModel().getSelectedIndex();
        int indexPlaylist = tblViewSongsInPlaylist.getSelectionModel().getSelectedIndex();

        if (indexSearch != -1 && indexSearch < tblViewSearch.getItems().size() - 1) {
            // Select the next song in tblViewSearch
            tblViewSearch.getSelectionModel().clearAndSelect(indexSearch + 1);
            Song songToPlay = tblViewSearch.getSelectionModel().getSelectedItem();
            if (songToPlay != null) {
                playSong(songToPlay.getFPath());
            }
        } else if (indexPlaylist != -1 && indexPlaylist < tblViewSongsInPlaylist.getItems().size() - 1) {
            // Select the next song in tblViewSongsInPlaylist
            tblViewSongsInPlaylist.getSelectionModel().clearAndSelect(indexPlaylist + 1);
            Song songToPlay = tblViewSongsInPlaylist.getSelectionModel().getSelectedItem();
            if (songToPlay != null) {
                playSong(songToPlay.getFPath());
            }
        }
    }

     // A button that goes back to the previous song
    public void handlePreviousSong(ActionEvent actionEvent) throws Exception {
        int indexSearch = tblViewSearch.getSelectionModel().getSelectedIndex();
        int indexPlaylist = tblViewSongsInPlaylist.getSelectionModel().getSelectedIndex();

        if (indexSearch > 0) {
            // Select the previous song in tblViewSearch
            tblViewSearch.getSelectionModel().clearAndSelect(indexSearch - 1);
            Song songToPlay = tblViewSearch.getSelectionModel().getSelectedItem();
            if (songToPlay != null) {
                playSong(songToPlay.getFPath());
            }
        } else if (indexPlaylist > 0) {
            // Select the previous song in tblViewSongsInPlaylist
            tblViewSongsInPlaylist.getSelectionModel().clearAndSelect(indexPlaylist - 1);
            Song songToPlay = tblViewSongsInPlaylist.getSelectionModel().getSelectedItem();
            if (songToPlay != null) {
                playSong(songToPlay.getFPath());
            }
        }
    }

    // Puts the time of the whole playlist together and shows it on top of the playlist tableview
    public void updateTotalTimeTextField(List<Song> songs) {
        int totalTime = songPlaylistModel.calculateTotalTime(songs);
        // Assuming txtTotalTime is the TextField where you want to display the total time
        txtTotalTime.setText(songPlaylistModel.formatDuration(totalTime)); // You can create a method to format the duration as needed
    }

    // Shows the length the current song has played and counts up till the next song starts and the timer starts over
    private void playingTimer() {
        songTimer.textProperty().bind(
                new StringBinding() {
                    {
                        super.bind(mediaPlayer.currentTimeProperty());
                    }
                    // Makes the timer show in minutes and seconds
                    @Override
                    protected String computeValue() {
                        int time = (int) (mediaPlayer.getCurrentTime().toMillis() / 1000);
                        int minutes = time / 60;
                        int seconds = time % 60;
                        String textSeconds;
                        if (seconds <= 9) {
                            textSeconds = "0" + seconds;
                        } else {
                            textSeconds = "" + seconds;
                        }
                        return minutes + ":" + textSeconds;
                    }
                });
    }

    public void songLength() {
    }

    /**
     * Set the items(playlists) in the the tableview that is linked to the user.
     *
     * @param userLogged the current user of the application
     * @throws Exception
     */
    public void setPlaylistForUser(Users userLogged) throws Exception {
        try {
            //makes a list from playlist that is linked to a user on the database.
            List<Playlist> userPlaylists = songPlaylistModel.getUserPlaylist(userLogged.getUserID());
            tblViewPlaylist.setItems(FXCollections.observableList(userPlaylists));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Moves the selected song up on the playlist
    public void handleMoveUp (ActionEvent actionEvent){
        int index = tblViewSongsInPlaylist.getSelectionModel().getSelectedIndex();
        if (index != -1 && index > 0) { // As long as the selected song is on index 0 (first line) or above you can move a song up
            tblViewSongsInPlaylist.getItems().add(index - 1, tblViewSongsInPlaylist.getItems().remove(index));
            tblViewSongsInPlaylist.getSelectionModel().clearAndSelect(index - 1);
        }
    }

    // Moves the selected song down on the playlist
    public void handleMoveDown (ActionEvent actionEvent){
        int index = tblViewSongsInPlaylist.getSelectionModel().getSelectedIndex();
        int lastIndex = tblViewSongsInPlaylist.getItems().size() - 1;
        if (index != -1 && index < lastIndex) { // Also here you can keep moving down till you reach the last index
            tblViewSongsInPlaylist.getItems().add(index + 1, tblViewSongsInPlaylist.getItems().remove(index));
            tblViewSongsInPlaylist.getSelectionModel().clearAndSelect(index + 1);
        }
    }

    // A shuffle button that you press to get a random song (its not a toggle button so you have to press it again for
    // a new random song) it then removes the last played song temporarily from the playlist
    public void handleShuffle () {
        if (!tblViewSongsInPlaylist.getItems().isEmpty()) {
            // Get the index of the last played song
            int lastPlayedIndex = tblViewSongsInPlaylist.getSelectionModel().getSelectedIndex();

            // Remove the last played song from the playlist
            tblViewSongsInPlaylist.getItems().remove(lastPlayedIndex);

            // Check if there are still songs in the playlist
            if (!tblViewSongsInPlaylist.getItems().isEmpty()) {
                // Play a random song from the updated playlist
                int randomIndex = new Random().nextInt(tblViewSongsInPlaylist.getItems().size());
                tblViewSongsInPlaylist.getSelectionModel().select(randomIndex);
                Song randomSong = tblViewSongsInPlaylist.getSelectionModel().getSelectedItem();

                try {
                    playSong(randomSong.getFPath());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                mediaPlayer.stop();
            }
        }
    }

    // Shows the search table (our list of songs)
    public void handleShowSearch(ActionEvent actionEvent) {
        tblViewSearch.setVisible(true);
        btnBarSong.setVisible(true);
    }
}