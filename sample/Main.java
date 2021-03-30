package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Controller controller = new Controller();
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("sample.fxml"));
        fxmlLoader.setController(controller);
        primaryStage.setTitle("Jeu Moulin");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }


}
