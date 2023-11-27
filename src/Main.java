import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mytunes.gui.controller.MainViewController;

import java.util.Objects;

public class Main extends Application {

    private static MainViewController mainView = new MainViewController();
    private static boolean start = true;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/MainView.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        setMainView();
    }
    private static void setMainView() {
        if (start == true) {
            mainView.initialize();
            start = false;
        }
    }
}
