package mytunes.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import mytunes.be.Song;
import mytunes.gui.model.SongPlaylistModel;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateUpdateSongViewController extends BaseController implements Initializable {
    private SongPlaylistModel songPlaylistModel;
    public ToggleButton tgglBtnIfAlbum;
    public Button btnUpdate, btnCreate;
    @FXML
    private TextField txtSongName, txtArtist, txtGenre, txtAlbumName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        Song newSong = new Song(-1, title, -1, artist, genre);
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