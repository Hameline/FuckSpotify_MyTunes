package mytunes.gui.controller;

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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mytunes.be.Playlist;
import mytunes.gui.model.SongPlaylistModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController extends BaseController implements Initializable {

    @FXML
    private TableView tblViewPlaylist;
    @FXML
    private Button btnNewPlaylist;
    @FXML
    private Button btnMainMenu;
    @FXML
    private TableColumn tblViewSearchSong;
    @FXML
    private TableColumn tblViewSearchDuration;
    @FXML
    private TableColumn tblViewSearchArtist;
    @FXML
    private TableColumn tblViewSearchGenre;
    @FXML
    private ButtonBar btnBarSong;
    @FXML
    private VBox vBoxDefault;
    @FXML
    private TableView tblViewSearch;
    @FXML
    private TableColumn tblViewPlaylistPlaylist;
    @FXML
    private TableColumn tblViewPlaylistUser;
    @FXML
    private TextField txtSearchField;
    private SongPlaylistModel songPlaylistModel;

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
        // MAKES the TBT VIEW INVISIBLE
        tblViewSearch.setVisible(false);

        // MAKES the BUTTON BAR INVISIBLE
        btnBarSong.setVisible(false);

        txtSearchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                songPlaylistModel.searchSong(newValue);
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        });
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
        if (songPlaylistModel != null) {
            tblViewSearch.setItems(songPlaylistModel.getListOfSongs());
            tblViewPlaylist.setItems(songPlaylistModel.getListOfPlaylists());
        }
        tblViewSearchArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        tblViewSearchGenre.setCellValueFactory(new PropertyValueFactory<>("Genre"));
        tblViewSearchSong.setCellValueFactory(new PropertyValueFactory<>("title"));
        tblViewSearchDuration.setCellValueFactory(new PropertyValueFactory<>("time"));

        tblViewPlaylistPlaylist.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    @FXML
    private void HandleNewPlaylist(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CreateUpdatePlaylistView.fxml"));
        Parent popupWindow = loader.load();

        CreateUpdatePlaylistViewController controller = loader.getController();
        controller.setModel(songPlaylistModel);
        controller.setup();

        Stage PopupWindow = new Stage();
        PopupWindow.setTitle("Create/Update Playlist");
        PopupWindow.initModality(Modality.APPLICATION_MODAL);
        PopupWindow.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());

        PopupWindow.setScene(new Scene(popupWindow));
        PopupWindow.showAndWait();

    }

    @FXML
    private void HandleMainMenu(ActionEvent actionEvent) {
        // RESETS the SEARCH FIELD
        txtSearchField.setText(null);
        // MAKES the TBT VIEW INVISIBLE
        tblViewSearch.setVisible(false);
        // MAKES the VBOX VISIBLE
        vBoxDefault.setVisible(true);
        // MAKES the BUTTON BAR INVISIBLE
        btnBarSong.setVisible(false);
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
    }

    @FXML
    private void handleUpdate(ActionEvent actionEvent) {
    }

    public void refreshLists() {
        tblViewSearch.refresh();
        tblViewPlaylist.refresh();
    }
}
