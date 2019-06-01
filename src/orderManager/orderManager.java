package orderManager;

import javafx.application.Application;
import javafx.stage.Stage;
import orderManager.dal.Properties.PropertyReader;

public class orderManager extends Application {

  String loginWindowPath = "gui/view/loginWindow.fxml";
  String mainWindowPath = "gui/view/mainWindow.fxml";

  public static void main(final String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(final Stage primaryStage) throws Exception {
    DirectoryWatcher dw = new DirectoryWatcher();
    dw.start();

    PropertyReader pr = new PropertyReader();
    if (pr.hasDepartment()) {
      new windowOpener(loginWindowPath, 265, 200, false);
    } else {
      new windowOpener(mainWindowPath, 651, 496, true);
    }


  }


}
