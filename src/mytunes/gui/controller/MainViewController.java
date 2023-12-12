package mytunes.gui.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import java.util.*;

@SuppressWarnings("ALL")
public class MainViewController<songPath> extends BaseController implements Initializable {

    @FXML
    private ProgressBar songProgress;
    @FXML
    private ListView vboxlistArtist;
    @FXML
    private TableColumn tblViewSongInPlaylistSongUser, tblViewSongInPlaylistDurationUser, tblViewSongInPlaylistGenreUser, tblViewSongInPlaylistArtistUser;
    @FXML
    private Label txtTotalTime, songTimer, songTimer2, lblSelectPlaylist, lblPlaylistName, recommendedArtist;
    @FXML
    private Button btnPlay, btnDelete, nextSong, previousSong, handlePlaySong, btnMoveUp, btnMoveDown, btnShuffle, btnCreate, btnUpdate
            ,btnMainMenu ,btnNewPlaylist, removeFromPlaylist;
    @FXML
    private Slider volumeSlider;
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
    private TableView tblViewPlaylist;
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
    private Users userID;
    private int clicker;
    public MainViewController() throws IOException {
        try {
            songPlaylistModel = new SongPlaylistModel();
            userID = new Users();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setup();
            defaultMenu();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tblViewSearchDuration.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getTimeStamp()));

        tblViewPlaylistPlaylist.setCellValueFactory(new PropertyValueFactory<>("name"));

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

    public void setUserID(Users id) {
        userID = id;
    }

    // A setup method that comes with the abstract class "BaseController". Here we setup how the program should look once opened
    @Override
    public void setup() throws Exception {
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        if (songPlaylistModel != null) {
            tblViewSearch.setItems(songPlaylistModel.getListOfSongs());
            vboxlistSuggested.setItems(songPlaylistModel.getListOfSongs());
            // here we fetch the songs from the database that is connected the the playlist with the id.
            List<Song> songs = playlistSongsManager.fetchSongsForPlaylist(39);

            updateTotalTimeTextField(songs);
            ObservableList<Song> songObservableList = FXCollections.observableArrayList(songs);
            vboxlistArtist.setItems(songObservableList);
            recommendedArtist.setText("Skinz");

            vboxlistArtist.refresh();
            vboxlistSuggested.refresh();
            tblViewPlaylist.refresh();

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
    // Returns to the main menu by pressing the logo in the top left. Making the searchTable and the buttons to create,
    // update and delete invisible.
    private void defaultMenu() throws Exception {
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

        handlePlaylistButtons(); // Disables all Buttons To do with the playlist object


        // Stops all music from being played
        if (switchFromPlayAndPause == 2) {
            btnPlay.setText("▶");
            switchFromPlayAndPause = 1;
            pause();
        }

        // makes it so that refreshing the site the playlist will only show what is connected to each specefic user
        setPlaylistForUser(userID);
    }


    // // Disables all Buttons To do with the playlist object
    private void handlePlaylistButtons() {
        btnMoveDown.setVisible(false);
        btnMoveUp.setVisible(false);
        btnShuffle.setVisible(false);
    }

    // Opens a new window to create or update a playlist by pressing the button "Create Playlist"
    @FXML
    private void HandleNewPlaylist(ActionEvent actionEvent) throws Exception {
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
        }
        setPlaylistForUser(userID);
    }

    // A button that goes back to the front screen (the button is the logo in the top left corner
    @FXML
    private void HandleMainMenu(ActionEvent actionEvent) throws Exception {
        defaultMenu();
        setup();
    }


    // Shows the search table (our list of songs)
    @FXML
    private void handleShowSearch(ActionEvent actionEvent) {
        searchItems();
    }
    private void searchItems() {

            // MAKES the TBL VIEW VISIBLE

            tblViewSearch.setVisible(true);

            // MAKES the VBOX INVISIBLE
            vBoxDefault.setVisible(false);

            // MAKES the BUTTON BAR VISIBLE
            btnBarSong.setVisible(true);

            tblViewSongsInPlaylist.setVisible(false);

            btnMenuPlaylist.setVisible(false);

            handlePlaylistButtons();

    }

    // When you put in letters in the searchfield and press enter, the searchTable becomes visible along with the
    // buttons to create, update and delete
    @FXML
    private void handleSearchField(KeyEvent keyEvent) {
        // Detects if the ENTER KEY have been PRESSED
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (!(txtSearchField.getText().isEmpty())) {
                searchItems();
            }
            // Looks TO SEE if the TXT SEARCH FIELD is EMPTY
            if (txtSearchField.getText().isEmpty()) {
                searchItems();
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

    // Opens a new window where you can create or update a playlist
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
    @FXML
    private void handleDelete(ActionEvent actionEvent) throws Exception {
        try {
            confirmationAlertSong();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Could not delete song", e);
        }
    }

    // Gives you an alert for when you have selected and song and pressed the delete button
    @FXML
    private void confirmationAlertSong() throws Exception {
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

    // Deletes the selected playlist, first click on a playlist then select the dropdown menu above the songs
    // titled the playlists name and then clicking delete playlist
    @FXML
    private void handleDeletePlaylist(ActionEvent actionEvent) throws Exception {
        int userID = songPlaylistModel.getUserIDs().get(0);
        selectedPlaylist = (Playlist) tblViewPlaylist.getSelectionModel().getSelectedItem();

        if (selectedPlaylist != null) {
            try {
                // Delete movie in DAL layer (through the layers)
                songPlaylistModel.deletePlaylist(selectedPlaylist, userID);
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        }
        tblViewPlaylist.refresh();
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
        btnShuffle.setVisible(true);
        btnMoveUp.setVisible(true);
        btnMoveDown.setVisible(true);
        storePlaylist = selectedPlaylist;
    }
    // Adds the selected song to a selected playlist by first clicking on the song in the search table and then
    // clicking the desired playlist
    @FXML
    private void handleAddSongToPlaylist(MouseEvent mouseEvent) throws Exception {
        clicker += 1;
        if (clicker >= 2) {
            autoPlay(); // Auto Plays the Song when it is selected
            clicker = 0;
        }
        selectedSong = (Song) tblViewSearch.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            allowSongsInPlaylistView = false;
            storeSong = selectedSong;
            lblSelectPlaylist.setVisible(true);
        }
    }

    private void autoPlay() throws Exception {
        btnPlay.setText("||"); // makes sure that no matter what sate the play button is in it goes to the play state
        switchFromPlayAndPause = 2; // here it saves the information that the button is in the play state
        play(); // Plays the song
    }

    // Play method that will be used in out "handlePlaySong" method
    @FXML
    private void play() throws Exception {
        // Plays the song in the search table
        Song songToPlaySearch = tblViewSearch.getSelectionModel().getSelectedItem();
        // Plays the song in the playlist table
        Song songToPlayPlaylist = tblViewSongsInPlaylist.getSelectionModel().getSelectedItem();
        // Plays the song in the suggested table
        Song songToPlaySuggested = (Song) vboxlistSuggested.getSelectionModel().getSelectedItem();
        // Plays the song in the recommended artist table
        Song songtoPlayArtist = (Song) vboxlistArtist.getSelectionModel().getSelectedItem();

        if (songToPlaySearch != null) {
            playSong(songToPlaySearch.getFPath());
            songToPlayPlaylist = null; // makes it null so no 2 songs or more can play at the same time
            songToPlaySuggested = null; // makes it null so no 2 songs or more can play at the same time
            songtoPlayArtist = null; // makes it null so no 2 songs or more can play at the same time
        }

        if (songToPlayPlaylist != null) {
            playSong(songToPlayPlaylist.getFPath());
            songToPlaySearch = null; // makes it null so no 2 songs or more can play at the same time
            songToPlaySuggested = null; // makes it null so no 2 songs or more can play at the same time
            songtoPlayArtist = null; // makes it null so no 2 songs or more can play at the same time
        }

        if (songToPlaySuggested != null) {
            playSong(songToPlaySuggested.getFPath());
            songToPlaySearch = null; // makes it null so no 2 songs or more can play at the same time
            songToPlayPlaylist = null; // makes it null so no 2 songs or more can play at the same time
            songtoPlayArtist = null; // makes it null so no 2 songs or more can play at the same time
        }

        if (songtoPlayArtist != null) {
            playSong(songtoPlayArtist.getFPath());
            songToPlayPlaylist = null; // makes it null so no 2 songs or more can play at the same time
            songToPlaySuggested = null; // makes it null so no 2 songs or more can play at the same time
            songToPlaySearch = null; // makes it null so no 2 songs or more can play at the same time
        }
    }

    // Pause method that will be used in out "handlePlaySong" method
    @FXML
    private void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    // ActionEvent for our play button that plays a song and changes the image of the button to a pause symbol that then
    // when pressed again pauses the song
    @FXML
    private void handlePlaySong(ActionEvent actionEvent) throws Exception {
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

    @FXML
    private void playSong(String songPath) throws Exception {
        File file = new File(songPath);// Creates a new file with the parameter songPath
        Media mSong = new Media(file.getAbsoluteFile().toURI().toString());// Creates a new media with the name mSong,
        // and takes the previous file, gets an abstract pathname, changes it to URI and then to string, so the program can read it.
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.stop(); // If the media player isn't null, and the media player is playing, it'll stop.
        }
        try {
            mediaPlayer = new MediaPlayer(mSong);
            playingTimerUp();
            playingTimerDown();
            songProgress();
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
    @FXML
    private void handleStopSong(ActionEvent actionEvent) throws Exception {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    // A button that when pressed skips to the next song on the list of songs or on the playlist whichever you have selected
    @FXML
    private void handleNextSong(ActionEvent actionEvent) throws Exception {
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
    @FXML
    private void handlePreviousSong(ActionEvent actionEvent) throws Exception {
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
    @FXML
    private void updateTotalTimeTextField(List<Song> songs) {
        int totalTime = songPlaylistModel.calculateTotalTime(songs);
        // Assuming txtTotalTime is the TextField where you want to display the total time
        txtTotalTime.setText(songPlaylistModel.formatDuration(totalTime)); // You can create a method to format the duration as needed
    }

    // Shows the length the current song has played and counts up till the next song starts and the timer starts over
    private void playingTimerUp() {
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

    // Shows the full length of the song and counts down
    private void playingTimerDown() {
        songTimer2.textProperty().bind(
                new StringBinding() {
                    {
                        super.bind(mediaPlayer.currentTimeProperty());
                    }
                    // Makes the timer show in minutes and seconds, and count backwards.
                    @Override
                    protected String computeValue() {
                        int totalTime = (int) (mediaPlayer.getTotalDuration().toMillis() / 1000);
                        int currentTime = (int) (mediaPlayer.getCurrentTime().toMillis() / 1000);
                        int remainingTime = totalTime - currentTime;
                        int minutes = remainingTime / 60;
                        int seconds = remainingTime % 60;
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
    @FXML
    private void handleMoveUp (ActionEvent actionEvent){
        int index = tblViewSongsInPlaylist.getSelectionModel().getSelectedIndex();
        if (index != -1 && index > 0) { // As long as the selected song is on index 0 (first line) or above you can move a song up
            tblViewSongsInPlaylist.getItems().add(index - 1, tblViewSongsInPlaylist.getItems().remove(index));
            tblViewSongsInPlaylist.getSelectionModel().clearAndSelect(index - 1);
        }
    }

    // Moves the selected song down on the playlist
    @FXML
    private void handleMoveDown (ActionEvent actionEvent){
        int index = tblViewSongsInPlaylist.getSelectionModel().getSelectedIndex();
        int lastIndex = tblViewSongsInPlaylist.getItems().size() - 1;
        if (index != -1 && index < lastIndex) { // Also here you can keep moving down till you reach the last index
            tblViewSongsInPlaylist.getItems().add(index + 1, tblViewSongsInPlaylist.getItems().remove(index));
            tblViewSongsInPlaylist.getSelectionModel().clearAndSelect(index + 1);
        }
    }

    // A shuffle button that you press to get a random song (its not a toggle button so you have to press it again for
    // a new random song) it then removes the last played song temporarily from the playlist
    @FXML
    private void handleShuffle () {
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

    @FXML
    private void handlePlayFromVBoxSuggested(MouseEvent mouseEvent) throws Exception {
        selectedSong = (Song) vboxlistSuggested.getSelectionModel().getSelectedItem();
        doubleClickPlay();
    }

    @FXML
    private void handlePlaySongsFromPlaylist(MouseEvent mouseEvent) throws Exception {
        selectedSong = (Song) tblViewSongsInPlaylist.getSelectionModel().getSelectedItem();
        doubleClickPlay();
    }

    @FXML
    private void handlePlayFromVBoxArtist(MouseEvent mouseEvent) throws Exception {
        selectedSong = (Song) vboxlistArtist.getSelectionModel().getSelectedItem();
        doubleClickPlay();
    }

    private void doubleClickPlay() throws Exception {
        if (storeSong == selectedSong) { // checks to see if we have a stored song that is the same as the current selected song. IF it is the same it plays it
            autoPlay(); // Auto Plays the Song when it is selected
        }
        if (selectedSong != null) {
            storeSong = selectedSong;
        }
    }

    public void songProgress() {
        ReadOnlyObjectWrapper<Number> progressWrapper = new ReadOnlyObjectWrapper<>(0);

        // Bind the progress bar's progress to the progress of the mediaPlayer
        progressWrapper.bind(Bindings.createObjectBinding(
                () -> mediaPlayer.getCurrentTime().toMillis() / mediaPlayer.getTotalDuration().toMillis(),
                mediaPlayer.currentTimeProperty(),
                mediaPlayer.totalDurationProperty()
        ));

        // Bind the progress bar's progress property to the progress wrapper
        songProgress.progressProperty().bind(progressWrapper);
    }

    public void handleRemoveSong(ActionEvent actionEvent) throws Exception {
        try {
            confirmationAlertPlaylistSong();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Could not delete song from playlist", e);
        }
    }

    @FXML
    private void confirmationAlertPlaylistSong() throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("You are about to remove a Song");
        alert.setContentText("Are you sure you want to remove?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Song removedPlaylistSong = tblViewSongsInPlaylist.getSelectionModel().getSelectedItem();
            PlaylistSongs playlistSongs = new PlaylistSongs();
            playlistSongs.setSongID(removedPlaylistSong.getId());
            playlistSongs.setPlaylistID(storePlaylist.getId());
            songPlaylistModel.removeSongFromPlaylist(playlistSongs);
        } else {
        }
    }
}