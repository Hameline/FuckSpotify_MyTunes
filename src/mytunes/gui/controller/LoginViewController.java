package mytunes.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunes.bll.UsersManager;

import java.io.IOException;

public class LoginViewController {

    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtUsername;
    @FXML
    private Button btnLogIn;
    private UsersManager usersManager = new UsersManager();

    public LoginViewController() throws IOException {
        usersManager = new UsersManager();
    }

    @FXML
    private void HandleRememberMe(ActionEvent actionEvent) {
    }
    @FXML
    private void handleLogIn(ActionEvent actionEvent) throws Exception {
        String userName = txtUsername.getText();
        String userPassword = txtPassword.getText();

        if (usersManager.validateUser(userName, userPassword)) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
            Parent secondWindow = loader.load();
            Stage newStage = new Stage();
            newStage.setTitle("FSpotify");
            Scene scene = new Scene(secondWindow);
            newStage.setScene(scene);
            newStage.show();
            btnLogIn.getScene().getWindow().hide();
        }
        else {
            txtUsername.setText("Invalid username or password.");
        }
    }
}
