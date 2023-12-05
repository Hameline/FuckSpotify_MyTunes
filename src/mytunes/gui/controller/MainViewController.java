package mytunes.gui.controller;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mytunes.be.Playlist;
import mytunes.be.PlaylistSongs;
import mytunes.be.Song;
import mytunes.dal.ISongDataAccess;
import mytunes.gui.model.SongPlaylistModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@SuppressWarnings("ALL")
public class MainViewController<songPath> extends BaseController implements Initializable {

    @FXML
    private Button btnPlay, btnDelete, nextSong, previousSong;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Label playingSong;
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
    private MenuButton btnMenuPlaylist;
    @FXML
    private TableView tblViewSongsInPlaylist;
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
    private SongPlaylistModel songPlaylistModel;
    private CreateUpdatePlaylistViewController createUpdatePlaylistViewController;
    private Playlist selectedPlaylist;
    private Playlist storePlaylist;
    private Song selectedSong;
    private Song storeSong;
    private boolean allowSongsInPlaylistView = true;
    private MediaPlayer mediaPlayer;
    @FXML
    private TableColumn<Song, String> tblViewSearchDuration;

    public MainViewController() {
        try {
            songPlaylistModel = new SongPlaylistModel();
        }
        catch (Exception e) {
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

    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }

    @Override
    public void setup() {
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        if (songPlaylistModel != null) {
            tblViewSearch.setItems(songPlaylistModel.getListOfSongs());
            tblViewPlaylist.setItems(songPlaylistModel.getListOfPlaylists());

        }
        tblViewSearchArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        tblViewSearchGenre.setCellValueFactory(new PropertyValueFactory<>("Genre"));
        tblViewSearchSong.setCellValueFactory(new PropertyValueFactory<>("title"));
        tblViewSearchDuration.setCellValueFactory(new PropertyValueFactory<>("formatedTime"));


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
        // MAKES the TBT VIEW INVISIBLE
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
        else {
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

                // MAKES the TBL VIEW VISIBLE
                tblViewSearch.setVisible(true);

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
        PopupWindow.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());

        PopupWindow.setScene(new Scene(popupWindow));
        PopupWindow.showAndWait();
        
        tblViewSearch.refresh();
    }

    @FXML
    private void handleUpdate(ActionEvent actionEvent) throws IOException {

        Song selectedSong = (Song) tblViewSearch.getSelectionModel().getSelectedItem();
        if (selectedSong == null){
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
        PopupWindow.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());

        PopupWindow.setScene(new Scene(popupWindow));
        PopupWindow.showAndWait();

        tblViewSearch.refresh();
    }

    public void handleDelete(ActionEvent actionEvent) throws Exception{
        try {
            confirmationAlertSong();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Could not delete song", e);
        }
    }

    public void confirmationAlertSong() throws Exception {
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

    @FXML
    private void handleDeletePlaylist(ActionEvent actionEvent) {
        selectedPlaylist = (Playlist) tblViewPlaylist.getSelectionModel().getSelectedItem();

        if (selectedPlaylist != null)
        {
            try {
                // Delete movie in DAL layer (through the layers)
                songPlaylistModel.deletePlaylist(selectedPlaylist);
            }
            catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handlePlaylist(MouseEvent mouseEvent) {
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

                lblPlaylistName.setText(selectedPlaylist.getName());
                tblViewSongsInPlaylist.setItems(songPlaylistModel.getSongsFromPlaylist());
                //tblViewSongInPlaylistArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
                //tblViewSongInPlaylistGenre.setCellValueFactory(new PropertyValueFactory<>("Genre"));
                tblViewSongInPlaylistSong.setCellValueFactory(new PropertyValueFactory<>("songID"));
                //tblViewSongInPlaylistDuration.setCellValueFactory(new PropertyValueFactory<>("formatedTime"));

            }
            else {
                defaultMenu();
            }
        }
        if (allowSongsInPlaylistView == false) {
            selectedPlaylist = (Playlist) tblViewPlaylist.getSelectionModel().getSelectedItem();
            storePlaylist = selectedPlaylist;
            if (storeSong != null && storePlaylist != null){
                PlaylistSongs newPlaylistSongs = new PlaylistSongs(storePlaylist.getId(), storeSong.getId());
                try {

                    songPlaylistModel.addSongToPlaylist(newPlaylistSongs);

                }
                catch (Exception e) {
                    displayError(e);
                    e.printStackTrace();
                }
                finally {
                    defaultMenu();
                    tblViewSearch.setVisible(true);
                    btnBarSong.setVisible(true);
                    vBoxDefault.setVisible(false);
                }
            }
            else {
                System.out.println("stored playlist is empty same goes for stored song");
            }
        }
    }

    @FXML
    private void handleAddSongToPlaylist(MouseEvent mouseEvent) {
        selectedSong = (Song) tblViewSearch.getSelectionModel().getSelectedItem();
        if (selectedSong != null){
            allowSongsInPlaylistView = false;
            storeSong = selectedSong;
            lblSelectPlaylist.setVisible(true);
        }
    }

    public void handlePlaySong(ActionEvent actionEvent) throws Exception {
        Song songToPlay = tblViewSearch.getSelectionModel().getSelectedItem();
        if (songToPlay != null) {
            playSong(songToPlay.getFPath());
        } else {
            Song songsToPlay = tblViewSearch.getSelectionModel().getSelectedItem();
            playSong(songsToPlay.getFPath());
        }
    }

    public void playSong(String songPath) throws Exception{
        File file = new File(songPath);
        Media mSong = new Media(file.getAbsoluteFile().toURI().toString());

        if(mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
        {
            mediaPlayer.stop();
        }
        try{
            mediaPlayer = new MediaPlayer(mSong);
            mediaPlayer.play();
            mediaPlayer.setOnEndOfMedia(()->{
                Song song;
                if(tblViewSearch.getSelectionModel().getSelectedIndex() != -1){
                    int nextSongIndex = tblViewSearch.getSelectionModel().getSelectedIndex() +1;
                    tblViewSearch.getSelectionModel().select(nextSongIndex);
                    song = tblViewSearch.getSelectionModel().getSelectedItem();

                }else if(tblViewSearch.getSelectionModel().getSelectedIndex() != -1){
                    int nextSongIndex = tblViewSearch.getSelectionModel().getSelectedIndex() +1;
                    tblViewSearch.getSelectionModel().select(nextSongIndex);
                    song = tblViewSearch.getSelectionModel().getSelectedItem();
                }else{
                    return;
                }
                try {
                    playSong(song.getFPath());

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            });
        }catch (Exception exc) {
            exc.printStackTrace();
            throw new Exception("Could not play song", exc);
        }
    }

    public void handleNextSong(ActionEvent actionEvent) {
    }

    public void handlePreviousSong(ActionEvent actionEvent) {
    }
}