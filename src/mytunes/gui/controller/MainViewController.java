package mytunes.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewController {

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

    // sets the DEFAULT MAIN VIEW is called in the MAIN CLASS
    public void initialize() {
        // MAKES the TBT VIEW INVISIBLE
        tblViewSearch.setVisible(false);

        // MAKES the BUTTON BAR INVISIBLE
        btnBarSong.setVisible(false);
    }


    @FXML
    private void HandleNewPlaylist(ActionEvent actionEvent) {

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CreateUpdateSongView.fxml"));
        Parent popupWindow = loader.load();
        Stage PopupWindow = new Stage();
        PopupWindow.setScene(new Scene(popupWindow));
        PopupWindow.initModality(Modality.APPLICATION_MODAL);
        PopupWindow.showAndWait();
    }

    @FXML
    private void handleUpdate(ActionEvent actionEvent) {
    }
}
