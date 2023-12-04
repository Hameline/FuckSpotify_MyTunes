package mytunes.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mytunes.be.Artist;
import mytunes.be.Genre;
import mytunes.be.Song;
import mytunes.gui.model.SongPlaylistModel;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateUpdateSongViewController extends BaseController implements Initializable {
    @FXML
    private ComboBox<String> menuGenre;
    private SongPlaylistModel songPlaylistModel;
    private MainViewController mainViewController;
    public ToggleButton tgglBtnIfAlbum;
    public Button btnUpdate, btnCreate;
    @FXML
    private TextField txtSongName, txtArtist, txtGenre, txtAlbumName, txtTime;

    public CreateUpdateSongViewController() throws Exception {
        try {
            songPlaylistModel = new SongPlaylistModel();
            mainViewController = new MainViewController();
        }
        catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuGenre.getItems().addAll("Pop", "Country", "Rock", "Techno", "Jazz", "HipHop", "Dance", "Blues", "Phunk");
        menuGenre.getSelectionModel().selectFirst();
    }
    public void setup() {
        songPlaylistModel = getModel();
    }

    @FXML
    private void handleIfAlbum(ActionEvent actionEvent) {
    }
    @FXML
    private void handleUpdate(ActionEvent actionEvent) {
    }
    @FXML
    private void handleCreate(ActionEvent actionEvent) {
        String title = txtSongName.getText();
        String artist = txtArtist.getText();
        String genre = txtGenre.getText();
        int time = Integer.parseInt(txtTime.getText());

        Artist artist1 = new Artist(artist, -1);
        Genre genre1 = new Genre(genre, -1, -1);
        Song newSong = new Song(-1, title, time, artist1, genre1, "");
        try {
            songPlaylistModel.createSong(newSong);
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
        finally {
            btnCreate.getScene().getWindow().hide();
        }
    }

    private void displayError(Throwable t)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }
}