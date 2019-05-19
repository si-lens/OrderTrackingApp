package orderManager;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class windowOpener {
   public windowOpener(final String path,int minWidth, int minHeight, boolean resizable) throws IOException {
      final Parent root = FXMLLoader.load(getClass().getResource(path));
      final Stage stage = new Stage();
      stage.setTitle("OrderManager");
      stage.setMinHeight(minHeight);
      stage.setMinWidth(minWidth);
      stage.setResizable(resizable);
      stage.setScene(new Scene(root));
      stage.show();
   }

}
