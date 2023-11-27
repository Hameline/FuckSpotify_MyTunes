package mytunes.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginViewController {

    @FXML
    private Button btnLogIn;

    @FXML
    private void HandleRememberMe(ActionEvent actionEvent) {
    }
    @FXML
    private void handleLogIn(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
        Parent secondWindow = loader.load();
        Stage newStage = new Stage();
        newStage.setTitle("FSpotify");
        Scene scene = new Scene(secondWindow);
        newStage.setScene(scene);
        newStage.show();
        btnLogIn.getScene().getWindow().hide();
    }
}
