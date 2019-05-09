package orderManager;

import javafx.application.Application;
import javafx.stage.Stage;

public class orderManager extends Application {
String loginWindowPath = "gui/view/loginWindow.fxml";
    int width=265;
    int height=200;
    @Override
    public void start(final Stage primaryStage) throws Exception {

        new windowOpener(loginWindowPath,width,height,false);
    }

  public static void main(final String[] args) {
    Application.launch(args);
  }


}
