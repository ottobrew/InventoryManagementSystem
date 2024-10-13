package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Javadoc resides in folder at /RyanOttoInventorySystem/Javadoc
 * @author Ryan Otto
 */

/**
 * Main class creates an Inventory System application.
 *
 * FUTURE ENHANCEMENT: At prompt before deleting a part, one could include not only the number
 * of products associated with the part but also add a list of names of products
 * with which parts are associated. Similarly, when attempting to delete a Product with
 * associated parts, a pop-up window could show the associated Parts and give the option
 * to remove them along with the option to delete the Product.
 */
public class Main extends Application {

    /**
     * Start method initiates JavaFX stage. Start method is abstract so it is overridden.
     * @param primaryStage Instance of Stage where JavaFX scenes are created.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/MainScreenView.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 880, 450));
        primaryStage.show();
    }

    /**
     * Main method where JavaFX application is launched.
     * @param args
     */
    public static void main(String[] args) {

        launch(args);
    }

}
