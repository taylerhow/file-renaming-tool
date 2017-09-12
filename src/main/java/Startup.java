import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Startup extends Application {

    public static Startup INSTANCE;

    public Startup() {
        INSTANCE = this;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setOnCloseRequest(e -> shutdown());
        primaryStage.setTitle("File Renaming Utility");
        MainPageController mainPageController = new MainPageController();
        primaryStage.setScene(new Scene(mainPageController));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void shutdown() {
        Platform.exit();
    }
}
