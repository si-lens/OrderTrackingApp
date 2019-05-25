package orderManager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import orderManager.dal.Properties.PropertyReader;

public class orderManager extends Application {

  String loginWindowPath = "gui/view/loginWindow.fxml";
  String mainWindowPath = "gui/view/mainWindow.fxml";

  public static void main(final String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(final Stage primaryStage) throws Exception {
    PropertyReader pr = new PropertyReader();
    if (pr.hasDepartment()) {
      new windowOpener(loginWindowPath, 265, 200, false);
    } else {
      new windowOpener(mainWindowPath, 651, 496, true);
    }
  }


}
