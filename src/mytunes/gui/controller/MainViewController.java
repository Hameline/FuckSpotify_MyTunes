package mytunes.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class MainViewController {

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
    }


    @FXML
    private void HandleNewPlaylist(ActionEvent actionEvent) {

    }

    @FXML
    private void HandleMainMenu(ActionEvent actionEvent) {
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
            }
            // Looks TO SEE if the TXT SEARCH FIELD is EMPTY
            if (txtSearchField.getText().isEmpty()) {
                // MAKES the TBT VIEW INVISIBLE
                tblViewSearch.setVisible(false);
                // MAKES the VBOX VISIBLE
                vBoxDefault.setVisible(true);
            }
        }
    }

    @FXML
    private void handelCreate(ActionEvent actionEvent) {

    }

    @FXML
    private void handleUpdate(ActionEvent actionEvent) {
    }
}
