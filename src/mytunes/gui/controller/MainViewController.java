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


    @FXML
    private void HandleNewPlaylist(ActionEvent actionEvent) {
    }

    @FXML
    private void HandleMainMenu(ActionEvent actionEvent) {
    }

    @FXML
    private void handleSearchField(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (!(txtSearchField.getText().isEmpty())) {
                tblViewSearch.setVisible(true);
                vBoxDefault.setVisible(false);
            }

            if (txtSearchField.getText().isEmpty()) {
                tblViewSearch.setVisible(false);
                vBoxDefault.setVisible(true);
            }
        }
    }

    public void initialize() {
       tblViewSearch.setVisible(false);
    }

}
