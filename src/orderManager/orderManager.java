package orderManager;

import javafx.application.Application;
import javafx.stage.Stage;
import orderManager.dal.Properties.PropertyReader;

public class orderManager extends Application {
String loginWindowPath = "gui/view/loginWindow.fxml";
String mainWindowPath = "gui/view/mainWindow.fxml";
    int width=265;
    int height=200;
    @Override
    public void start(final Stage primaryStage) throws Exception {
        PropertyReader pr = new PropertyReader();
        if (pr.hasDepartment())
        {
            new windowOpener(loginWindowPath,width,height,false);
        } else
        {
            new windowOpener(mainWindowPath,651, 496, true);
        }
    }

  public static void main(final String[] args) {
    Application.launch(args);
  }


}
