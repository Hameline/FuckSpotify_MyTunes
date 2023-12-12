package mytunes.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mytunes.be.Playlist;
import mytunes.gui.model.SongPlaylistModel;

import java.net.URL;
import java.util.List;
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

    private Playlist selectedPlaylist;

    public CreateUpdatePlaylistViewController() throws Exception {
        try {
            songPlaylistModel = new SongPlaylistModel();
            mainViewController = new MainViewController();
        }
        catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }
    @FXML
    private void handleUpdate(ActionEvent actionEvent) throws  Exception {
        try {
            int userID = songPlaylistModel.getUserIDs().get(0);

            selectedPlaylist.setName(txtPlaylistName.getText());

            songPlaylistModel.updatePlaylist(selectedPlaylist, userID);
            btnUpdate.getScene().getWindow().hide();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCreate(ActionEvent actionEvent) throws Exception {
        Integer userID = songPlaylistModel.getUserIDs().get(0);
        Playlist newPlaylist = new Playlist(-1, txtPlaylistName.getText());

        try {
            songPlaylistModel.createPlaylist(newPlaylist, userID);

            btnCreate.getScene().getWindow().hide();
        }
        catch (Exception e) {
            displayError(e);
            e.printStackTrace();
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
        if (selectedPlaylist != null) {
            txtPlaylistName.setText(selectedPlaylist.getName());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setPlaylist (Playlist playlist) {
        selectedPlaylist = playlist;
    }
}
