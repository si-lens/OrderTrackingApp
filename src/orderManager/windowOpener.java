package orderManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import orderManager.gui.controller.taskWindowController;
import java.io.IOException;

public class windowOpener {
   private final Stage stage = new Stage();
   FXMLLoader loader;
   public windowOpener(final String path,int minWidth, int minHeight, boolean resizable) throws IOException {
      loader = new FXMLLoader(getClass().getResource(path));
      Parent root = loader.load();
      stage.setTitle("OrderManager");
      stage.setMinHeight(minHeight);
      stage.setMinWidth(minWidth);
      stage.setResizable(resizable);
      stage.setScene(new Scene(root));
      stage.show();

   }

   public void passStage(){
      taskWindowController twc = loader.getController();
      twc.setStage(this.stage);
   }
}
