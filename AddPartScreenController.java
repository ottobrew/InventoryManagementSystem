package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class handles data for Add and Modify Part screen views.
 */
public class AddPartScreenController implements Initializable {
    @FXML
    public AnchorPane addPartWindow;
    @FXML
    public RadioButton inHouseRadio;
    @FXML
    public RadioButton outsourcedRadio;
    @FXML
    public Label addPartId;
    @FXML
    public TextField addPartIDTxtFld;
    @FXML
    public Label addPartName;
    @FXML
    public TextField addPartNameTxtFld;
    @FXML
    public Label addPartInv;
    @FXML
    public TextField addPartInvTxtFld;
    @FXML
    public Label addPartPrice;
    @FXML
    public TextField addPartPriceTxtFld;
    @FXML
    public Label addPartMax;
    @FXML
    public TextField addPartMaxTxtFld;
    @FXML
    public Label addPartMin;
    @FXML
    public TextField addPartMinTxtFld;
    @FXML
    public Label addPartInOut;
    @FXML
    public TextField addPartInOutTxtFld;
    @FXML
    public Label addPartTitle;
    @FXML
    public Button saveAddPartBtn;
    @FXML
    public Button cancelAddPartBtn;

    /** Auto-generated Part ID counter.  */
    public static int partId = 1;
    Part replacePart;

    /**
     * Button Handler to select InHouse object.
     * @param event Radio button select.
     */
    public void onInHouse(ActionEvent event) {
        if (inHouseRadio.isSelected()) {
            addPartInOut.setText("Machine ID");
        }
    }

    /**
     * Button Handler to select OutSourced object.
     * @param event Radio button select.
     */
    public void onOutsourced(ActionEvent event) {
        if (outsourcedRadio.isSelected()) {
            addPartInOut.setText("Company Name");
        }
    }

    /**
     * Button Handler to save changes -- to Add Part or Modify Part.
     * Part is added or updated in allParts ObservableList. Then returns to Main Screen.
     * @param event Button click.
     * @throws IOException Input/Output exception.
     *
     * RUNTIME ERROR
     * My initial design to Modify a Part in this method was similar to Modify Product,
     * in that a part whose index is found in the allParts list would have its fields
     * updated by accessing each of the fields of the current part (MainScreenController.currentPart).
     * However, when attempting to modify an In-House part to designate it as an Outsourced part,
     * a ClassCastException was thrown because it was attempting to re-cast the object from one
     * subclass to another.  As a solution, and in order to avoid this issue altogether, I chose to instantiate a new
     * object (replacePart) and replace the old part object at that index with the new In-House or Outsourced object,
     * keeping the Part ID the same.
     */
    public void onSaveAddPart(ActionEvent event) throws IOException {
        try {
            // Set Part ID to auto-generated part ID
            int id = partId;

            // Get InHouse or Outsourced Part object attribute data from user input in text fields.
            String name = addPartNameTxtFld.getText();
            double price = Double.parseDouble(addPartPriceTxtFld.getText());
            int stock = Integer.parseInt(addPartInvTxtFld.getText());
            int min = Integer.parseInt(addPartMinTxtFld.getText());
            int max = Integer.parseInt(addPartMaxTxtFld.getText());

            if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Part Name Error");
                alert.setContentText("Please enter Part name");
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

            // If currentPart index exists, new replacePart object will replace old one with current text field input.
            if (Inventory.getAllParts().contains(MainScreenController.currentPart)) {
                int currentIndex = Inventory.getAllParts().indexOf(MainScreenController.currentPart);

                // Confirm whether InHouse or Outsourced button is selected.
                if (inHouseRadio.isSelected()) {
                    int machineId = Integer.parseInt(addPartInOutTxtFld.getText());

                    if (machineId < 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Machine ID Error");
                        alert.setContentText("Please enter valid number greater than zero.");
                        alert.showAndWait();
                        return;
                    }

                    replacePart = new InHouse(MainScreenController.currentPart.getId(), name, price, stock, min, max, machineId);
                }

                else if (outsourcedRadio.isSelected()) {
                    String companyName = addPartInOutTxtFld.getText();

                    if (companyName.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Company Name Error");
                        alert.setContentText("Please enter Company Name");
                        alert.showAndWait();
                        return;
                    }

                    replacePart = new Outsourced(MainScreenController.currentPart.getId(), name, price, stock, min, max, companyName);
                }

                // Call updatePart to replace old object with new object at current Index and current Part selection
                Inventory.updatePart(currentIndex, replacePart);

                // Clear data from current Part including currentIndex variable
                MainScreenController.currentPart = null;
            }

            // If no index exists, a new InHouse or Outsourced object will be instantiated and added to the allParts ObservableList
            else {

                if (inHouseRadio.isSelected()) {
                    int machineId = Integer.parseInt(addPartInOutTxtFld.getText());

                    if (machineId < 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Machine ID Error");
                        alert.setContentText("Please enter valid number greater than zero.");
                        alert.showAndWait();
                        return;
                    }

                    Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineId));
                    partId++;
                }
                else if (outsourcedRadio.isSelected()) {
                    String companyName = addPartInOutTxtFld.getText();

                    if (companyName.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Company Name Error");
                        alert.setContentText("Please enter Company Name");
                        alert.showAndWait();
                        return;
                    }

                    Inventory.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
                    partId++;
                }
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
     * Button Handler to cancel changes made to Part form.
     * Prompts user to confirm Cancel. If confirmed, returns to Main Screen.
     * @param event Button click.
     * @throws IOException Input/Output exception.
     */
    public void onCancelAddPart(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Any changes will be lost. Do you wish to continue?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {

            // Clear data from current Part including currentIndex variable
            MainScreenController.currentPart = null;

            Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreenView.fxml"));
            Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 880, 450);
            stage.setTitle("Inventory Management System");
            stage.setScene(scene);
            stage.show();
        }

    }

    /**
     * Method to send data from Main Screen Controller to Part Screen Controller in order to populate editable text fields.
     * @param partToMod Part user selects to be modified.
     */
    public void sendPart(Part partToMod) {
        addPartTitle.setText("Modify Part");
        addPartIDTxtFld.setText(String.valueOf(partToMod.getId()));
        addPartNameTxtFld.setText(partToMod.getName());
        addPartPriceTxtFld.setText(String.format("%.2f", partToMod.getPrice()));
        addPartInvTxtFld.setText(String.valueOf(partToMod.getStock()));
        addPartMinTxtFld.setText(String.valueOf(partToMod.getMin()));
        addPartMaxTxtFld.setText(String.valueOf(partToMod.getMax()));

        if (partToMod instanceof InHouse) {
            addPartInOut.setText("Machine ID");
            addPartInOutTxtFld.setText(String.valueOf(((InHouse) partToMod).getMachineId()));
            inHouseRadio.setSelected(true);
        }
        else if (partToMod instanceof Outsourced) {
            addPartInOut.setText("Company Name");
            addPartInOutTxtFld.setText(String.valueOf(((Outsourced) partToMod).getCompanyName()));
            outsourcedRadio.setSelected(true);
        }
    }

    /**
     * Method to initialize Add/Modify Part Screen Controller.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
