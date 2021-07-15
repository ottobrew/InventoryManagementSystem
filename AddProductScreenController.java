package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class handles data for Add and Modify Product screen views.
 */
public class AddProductScreenController implements Initializable {

    @FXML
    public AnchorPane addProductWindow;
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
    public TableView<Part> assocPartsTable;
    @FXML
    public TableColumn<Part, Integer> assocPartIDCol;
    @FXML
    public TableColumn<Part, String> assocPartNameCol;
    @FXML
    public TableColumn<Part, Integer> assocPartInvCol;
    @FXML
    public TableColumn<Part, Double> assocPartUnitPriceCol;
    @FXML
    public Label addProdID;
    @FXML
    public TextField addProdIDTxtFld;
    @FXML
    public Label addProdName;
    @FXML
    public TextField addProdNameTxtFld;
    @FXML
    public Label addProdInv;
    @FXML
    public TextField addProdInvTxtFld;
    @FXML
    public Label addProdPrice;
    @FXML
    public TextField addProdPriceTxtFld;
    @FXML
    public Label addProdMax;
    @FXML
    public TextField addProdMaxTxtFld;
    @FXML
    public Label addProdMin;
    @FXML
    public TextField addProdMinTxtFld;
    @FXML
    public Label addProductTitle;
    @FXML
    public TextField partSearchBar;
    @FXML
    public Button addAssocPartBtn;
    @FXML
    public Button removeAssocPartBtn;
    @FXML
    public Button saveAddProductBtn;
    @FXML
    public Button cancelAddProductBtn;

    /** Auto-generated product ID counter.  */
    public static int productId = 1000;

    public ObservableList<Part> selectedParts = FXCollections.observableArrayList();

    /**
     * Button handler to Add Associated Part to Product and show in Associated Part table.
     * @param event Button click
     */
    public void onAddAssocPart(ActionEvent event) {

        Part selectedPart = allPartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null)
            return;

        selectedParts.add(selectedPart);
    }

    /**
     * Button handler to Remove Associated Part from Product and remove from Associated Part table.
     * Prompts user to confirm.
     * @param event Button click
     */
    public void onRemoveAssocPart(ActionEvent event) {

        Part selectedPart = assocPartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this associated part?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            selectedParts.remove(selectedPart);
        }
    }

    /**
     * Button handler to save changes -- to Add Product or Modify Product.
     * Product is added or updated in allProducts ObservableList. Then returns to Main Screen.
     * @param event Button click
     * @throws IOException Input/Output Exception
     */
    public void onSaveAddProduct(ActionEvent event) throws IOException {
        try {
            // Set Product ID to auto-generated product ID
            int id = productId;

            // Get Product object attribute data from user input in text fields
            String name = addProdNameTxtFld.getText();
            double price = Double.parseDouble(addProdPriceTxtFld.getText());
            int stock = Integer.parseInt(addProdInvTxtFld.getText());
            int min = Integer.parseInt(addProdMinTxtFld.getText());
            int max = Integer.parseInt(addProdMaxTxtFld.getText());

            if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Product Name Error");
                alert.setContentText("Please enter Product name");
                alert.showAndWait();
                return;
            }

            if (min >= max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Max must be greater than Min");
                alert.showAndWait();
                return;
            }

            if (stock > max || stock < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Inv must be between Min and Max");
                alert.showAndWait();
                return;
            }

            if (price < 0 || stock < 0 || min < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter zero or greater number");
                alert.showAndWait();
                return;
            }

            // If currentProduct index exists, attribute data of currentProduct will be set to current text field input

            if (Inventory.getAllProducts().contains(MainScreenController.currentProduct)) {
                int currentIndex = Inventory.getAllProducts().indexOf(MainScreenController.currentProduct);
                MainScreenController.currentProduct.setId(MainScreenController.currentProduct.getId());
                MainScreenController.currentProduct.setName(name);
                MainScreenController.currentProduct.setPrice(price);
                MainScreenController.currentProduct.setStock(stock);
                MainScreenController.currentProduct.setMin(min);
                MainScreenController.currentProduct.setMax(max);

//            Clear Associated Parts list and then add updates to currentProduct object
                MainScreenController.currentProduct.getAllAssociatedParts().clear();

                for (Part part : selectedParts) {
                    MainScreenController.currentProduct.addAssociatedPart(part);
                }

                // Call updateProduct to replace old data with new data at current Index and current Product selection
                Inventory.updateProduct(currentIndex, MainScreenController.currentProduct);

                // Clear data from currentProduct including currentIndex variable
                MainScreenController.currentProduct = null;

            } else {
        /*    If no index exists, create new Product object with data from text fields and add to allProducts ObservableList
              Add Associated Parts to newProduct object
         */
                Product newProduct = new Product(id, name, price, stock, min, max);
                for (Part part : selectedParts) {
                    newProduct.addAssociatedPart(part);
                }

                Inventory.addProduct(newProduct);

                // Increment auto-generated product ID
                productId++;

            }

            // Return to Main Screen
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreenView.fxml"));
            Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 880, 450);
            stage.setTitle("Inventory Management System");
            stage.setScene(scene);
            stage.show();

        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter valid input value for each text field");
            alert.showAndWait();
        }
    }

    /**
     * Button Handler to cancel changes made to Product form.
     * Prompts user to confirm Cancel. If confirmed, returns to Main Screen.
     * @param event Button click
     * @throws IOException Input/Output exception
     */
    public void onCancelAddProduct(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Any changes will be lost. Do you wish to continue?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            // Clear data from currentProduct including currentIndex variable
            MainScreenController.currentProduct = null;

            Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreenView.fxml"));
            Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 880, 450);
            stage.setTitle("Inventory Management System");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Event Handler to search for Part by name or part ID. Updates allPartsTable Tableview.
     * @param event on press Return
     */
    public void onPartSearch(ActionEvent event) {
        ObservableList<Part> partsSearch = Inventory.lookupPart(partSearchBar.getText());
        allPartsTable.setItems(partsSearch);

        try {
            int partId = Integer.parseInt(partSearchBar.getText());
            Part partById = (Inventory.lookupPart(partId));
            if ((partById) != null)
            {
                partsSearch.add(partById);
            }
        }
        catch (NumberFormatException e)
        {
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
     * Method to send data from Main Screen Controller to Product Screen Controller in order to populate editable text fields.
     * @param prodToMod Product user selects to be modified.
     */
    public void sendProduct(Product prodToMod) {
        addProductTitle.setText("Modify Product");
        addProdIDTxtFld.setText(String.valueOf(prodToMod.getId()));
        addProdNameTxtFld.setText(prodToMod.getName());
        addProdPriceTxtFld.setText(String.format("%.2f", prodToMod.getPrice()));
        addProdInvTxtFld.setText(String.valueOf(prodToMod.getStock()));
        addProdMinTxtFld.setText(String.valueOf(prodToMod.getMin()));
        addProdMaxTxtFld.setText(String.valueOf(prodToMod.getMax()));
        selectedParts.addAll(prodToMod.getAllAssociatedParts());
    }

    /**
     * Method to initialize Add/Modify Product Screen Controller.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assocPartsTable.setItems(selectedParts);
        allPartsTable.setItems(Inventory.getAllParts());

        // Set ObservableList data to show in Tableviews
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partUnitPriceCol.setCellValueFactory(new PropertyValueFactory<>("formattedPrice"));

        assocPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartUnitPriceCol.setCellValueFactory(new PropertyValueFactory<>("formattedPrice"));

    }
}
