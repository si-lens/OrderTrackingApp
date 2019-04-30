package main.java.orderManager;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class windowOpener {
   public windowOpener(final String path) throws IOException {
      final Parent root = FXMLLoader.load(getClass().getResource(path));
      final Stage stage = new Stage();
      stage.setTitle("OrderManager");
      stage.setScene(new Scene(root));
      stage.setOnCloseRequest((event) -> {
         Platform.exit();
         System.exit(0);
      });
      stage.show();
   }

}
