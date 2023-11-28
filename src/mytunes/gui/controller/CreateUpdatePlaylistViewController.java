package mytunes.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mytunes.be.Playlist;
import mytunes.gui.model.SongPlaylistModel;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateUpdatePlaylistViewController extends BaseController implements Initializable {

    @FXML
    private TextField txtPlaylistName;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnCreate;

    private SongPlaylistModel songPlaylistModel;

    private MainViewController mainViewController;

    @FXML
    private void handleUpdate(ActionEvent actionEvent) {
    }

    @FXML
    private void handleCreate(ActionEvent actionEvent) {

        Playlist newPlaylist = new Playlist(-1, txtPlaylistName.getText());

        try {
            songPlaylistModel.createPlaylist(newPlaylist);

            mainViewController.refreshLists();
        }
        catch (Exception e) {
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

    @Override
    public void setup() {
        songPlaylistModel = getModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
