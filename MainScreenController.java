package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the Main Screen controller which handles data in Main view and sends data to Part and Product controllers.
 */
public class MainScreenController implements Initializable {
    @FXML
    public Label invSystemTitle;
    @FXML
    public TextField partSearchBar;
    @FXML
    public TableView<Part> allPartsTable;
    @FXML
    public TableColumn<Part, Integer> partIDCol;
    @FXML
    public TableColumn<Part, String> partNameCol;
    @FXML
    public TableColumn<Part, Integer> partInvCol;
    @FXML
    public TableColumn<Part, Double> partUnitPriceCol;
    @FXML
    public Label partsLabel;
    @FXML
    public Button addPartBtn;
    @FXML
    public Button modPartBtn;
    @FXML
    public Button delPartBtn;
    @FXML
    public Label productsLabel;
    @FXML
    public TextField productSearchBar;
    @FXML
    public TableView<Product> allProductsTable;
    @FXML
    public TableColumn<Product, Integer> productIDCol;
    @FXML
    public TableColumn<Product, String> productNameCol;
    @FXML
    public TableColumn<Product, Integer> productInvCol;
    @FXML
    public TableColumn<Product, Double> productUnitPriceCol;
    @FXML
    public Button addProductBtn;
    @FXML
    public Button modProductBtn;
    @FXML
    public Button delProductBtn;
    @FXML
    public Button exitProgram;

    public static Part currentPart;
    public static Product currentProduct;

    /**
     * Button handler to Add Part to Parts table. Opens Add Part form.
     * @param event Button click
     * @throws IOException Input/Output exception
     */
    // When clicking Add button on Parts table
    public void onAddPart(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPartScreen.fxml"));
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 510, 480);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Button handler to Modify Part in Parts table. Opens Modify Part form.
     * @param event Button click
     * @throws IOException Input/Output exception
     */

    public void onModPart(ActionEvent event) throws IOException {

//        Instantiate FXML Loader with specified View
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddPartScreen.fxml"));
        loader.load();

//        Get selected item from Tableview and assign to currentPart var
        currentPart = allPartsTable.getSelectionModel().getSelectedItem();

//        Ignore Modify click if no Part is selected
        if (currentPart == null)
            return;

//        Create instance of Controller Class
        AddPartScreenController ModifyPartController = loader.getController();
        ModifyPartController.sendPart(currentPart);

        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setTitle("Modify Part");
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Button handler to delete part from Parts table. Prompts user to confirm delete.
     * @param event Button click
     */
        public void onDelPart(ActionEvent event) {
            currentPart = allPartsTable.getSelectionModel().getSelectedItem();

            if (currentPart == null) {
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                int i = 0;

                for (Product product : Inventory.getAllProducts()) {
                    if (product.getAllAssociatedParts().contains(currentPart)) {
                        i++;
                    }
                }
                if (i > 0) {
                    Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION, "This part is associated with " + i + " product(s). Do you wish to delete this part and remove from associated product(s)?");
                    Optional<ButtonType> result2 = alert2.showAndWait();

                    if (result2.isPresent() && result2.get() == ButtonType.OK) {
                        Inventory.deletePart(currentPart);
                        for (Product product : Inventory.getAllProducts()) {
                            product.deleteAssociatedPart(currentPart);
                        }
                    }
                }
                else {
                    Inventory.deletePart(currentPart);
                }
            }

            // Clear data from current Part
            MainScreenController.currentPart = null;
    }

    /**
     * Button handler to Add Product to Products table. Opens Add Product form.
     * @param event Button click
     * @throws IOException Input/Output exception
     */
    public void onAddProduct(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProductScreen.fxml"));
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 880, 480);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Button handler to Modify Product in Products table. Opens Modify Product form.
     * @param event Button click
     * @throws IOException Input/Output exception
     */
    public void onModProduct(ActionEvent event) throws IOException {

//        Instantiate FXML Loader with specified View

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddProductScreen.fxml"));
        loader.load();

//        Get selected item from Tableview and assign to currentProduct var
        currentProduct = allProductsTable.getSelectionModel().getSelectedItem();

//        Ignore Modify click if no Product is selected
        if (currentProduct == null)
            return;

//        Create instance of Controller Class
        AddProductScreenController ModifyProductController = loader.getController();
        ModifyProductController.sendProduct(currentProduct);

        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setTitle("Modify Product");
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Button handler to delete product from Products table. Prompts user with error if
     * Product has parts associated with it, else prompts user to confirm delete.
     * @param event Button click
     */
    public void onDelProduct(ActionEvent event) {

        currentProduct = allProductsTable.getSelectionModel().getSelectedItem();

        if (currentProduct == null) {
            return;
        }

        if (!(currentProduct.getAllAssociatedParts().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Unable to Delete");
            alert.setContentText("This product has associated parts and cannot be deleted.");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete this product?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deleteProduct(currentProduct);
            }
        }

        // Clear data from currentProduct
        MainScreenController.currentProduct = null;

    }

    /**
     * Button handler to Exit program. Prompts user to confirm Exiting.
     * @param event Button click
     */
    public void onExitProgram(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to exit the program?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }

    }

    /**
     * Event handler to search for Part by name or part ID
     * @param event on press Return
     */
    public void onPartSearch(ActionEvent event) {
        ObservableList<Part> partsSearch = Inventory.lookupPart(partSearchBar.getText());
        allPartsTable.setItems(partsSearch);

        try {
            int partId = Integer.parseInt(partSearchBar.getText());
            Part partById = (Inventory.lookupPart(partId));
            if ((partById) != null) {
                partsSearch.add(partById);
            }
        } catch (NumberFormatException e) {
            // ignore Number Format Exception
        }

        if (partsSearch.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Part Not Found");
            alert.setContentText("Search produced zero results.");
            alert.showAndWait();
        }

    }

    /**
     * Event handler to search for Product by name or product ID
     * @param event on press Return
     */
    public void onProductSearch(ActionEvent event) {
        ObservableList<Product> productsSearch = Inventory.lookupProduct(productSearchBar.getText());
        allProductsTable.setItems(productsSearch);

        try {
            int productId = Integer.parseInt(productSearchBar.getText());
            Product productById = (Inventory.lookupProduct(productId));

            if ((productById) != null)
            {
                productsSearch.add(productById);
            }
        }
        catch (NumberFormatException e)
        {
            // ignore Number Format Exception
        }

        if (productsSearch.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product Not Found");
            alert.setContentText("Search produced zero results.");
            alert.showAndWait();
        }
    }

    /**
     * Initialize Main Screen Controller class
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // allParts and allProducts Observable lists set to populate tableview

        allPartsTable.setItems(Inventory.getAllParts());
        allProductsTable.setItems(Inventory.getAllProducts());

        // Mapping Part class attributes to appropriate table columns

        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partUnitPriceCol.setCellValueFactory(new PropertyValueFactory<>("formattedPrice"));

        // Mapping Product class attributes to appropriate table columns

        productIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productUnitPriceCol.setCellValueFactory(new PropertyValueFactory<>("formattedPrice"));

    }
}
