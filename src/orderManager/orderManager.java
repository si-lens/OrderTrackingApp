package orderManager;

import javafx.application.Application;
import javafx.stage.Stage;

public class orderManager extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        new windowOpener("gui/view/mainWindow.fxml");
    }

  public static void main(String[] args) {
    launch(args);
  }


}
