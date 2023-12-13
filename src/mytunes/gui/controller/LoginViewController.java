package mytunes.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mytunes.be.Users;
import mytunes.bll.UsersManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable {

    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtUsername;
    @FXML
    private Button btnLogIn;
    private UsersManager usersManager = new UsersManager();
    private boolean rememberMe = false;
    private static final String PROP_FILE = "resources/user/userInfo";

    public LoginViewController() throws IOException {
        usersManager = new UsersManager();
    }

    @FXML
    private void HandleRememberMe(ActionEvent actionEvent) throws IOException {
        rememberMe = true;
    }
    @FXML
    private void handleLogIn(ActionEvent actionEvent) throws Exception {
        String userName = txtUsername.getText();
        String userPassword = txtPassword.getText();
        // Here safe the data input in the fields of the user, so that the
        // program will remember the user next time. This is saved in a file.
        // Here we save the data input in the fields of the user, so that the
        // program will remember the user next time. This is safed in a file.
        if (rememberMe == true) {
            Properties login = new Properties();

            login.setProperty("username", userName);
            login.setProperty("password", userPassword);
            try (FileOutputStream username = new FileOutputStream("resources/user/userInfo")) {
                login.store(username, userName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // here the method is run that validates the user credentials.
        Users userLogged = usersManager.validateUser(userName, userPassword);
        // if it matches you are logged in
        if (userLogged != null ) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
            Parent secondWindow = loader.load();
            MainViewController mainViewController = loader.getController();
            mainViewController.setPlaylistForUser(userLogged);
            Stage newStage = new Stage();
            Image image = new Image("/images/logo.png");
            newStage.getIcons().add(image);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Properties login = new Properties();
        try {
            login.load(new FileInputStream(PROP_FILE));
            txtPassword.setText(login.getProperty("password"));
            txtUsername.setText(login.getProperty("username"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
