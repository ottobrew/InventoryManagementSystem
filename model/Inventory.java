package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Inventory class interacts with Part and Product objects.
 *  Includes all static methods.
 */

public class Inventory {
    /**
     * ObservableList variable holds Part objects.
     */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    /**
     * ObservableList variable holds Product objects.
     */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Add new part to allParts ObservableList
     * @param newPart New Part to be added
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Add new product to allProducts ObservableList
     * @param newProduct New product to be added
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Search for Part by part ID.
     * @param partId an integer entered into search bar
     * @return Part with matching part ID
     */

    public static Part lookupPart(int partId) {

        for(Part part : getAllParts()) {
            if (part.getId() == partId) {
                return part;
            }
        }

        return null;
    }

    /**
     * Search for Product by product ID.
     * @param productId an integer entered into search bar
     * @return Product with matching product ID.
     */
    public static Product lookupProduct(int productId) {

        for(Product product : getAllProducts()) {
            if (product.getId() == productId) {
                return product;
            }
        }

        return null;
    }

    /**
     * Search for Part by full or partial name.
     * @param partName String entered into searchbar
     * @return List of parts matching search string
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> partSearchList = FXCollections.observableArrayList();

            for (Part part : getAllParts()) {
                if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                    partSearchList.add(part);
                }
            }

            return partSearchList;
        }

    /**
     * Search for Product by full or partial name.
     * @param productName String entered into search bar
     * @return List of products matching search string
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> productSearchList = FXCollections.observableArrayList();

            for (Product product : getAllProducts()) {
                if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                    productSearchList.add(product);
                }
            }

            return productSearchList;
    }

    /**
     * Method called when saving updates to Modify Part.
     * @param index Index of part to be replaced.
     * @param selectedPart Part selected by user to be modified.
     */
    public static void updatePart(int index, Part selectedPart) {
        getAllParts().set(index, selectedPart);
    }

    /**
     * Method called when saving updates to Modify Product.
     * @param index Index of product to be replaced.
     * @param selectedProduct Product selected by user to be modified.
     */
    public static void updateProduct(int index, Product selectedProduct) {
        getAllProducts().set(index, selectedProduct);
    }

    /**
     * Method to remove specified part from allParts ObservableList.
     * @param selectedPart Part selected by user.
     * @return boolean returns true.
     */

    public static boolean deletePart(Part selectedPart) {
        getAllParts().remove(selectedPart);
        return true;
    }

    /**
     * Method to remove specified product from allProducts ObservableList.
     * @param selectedProduct Product selected by user.
     * @return boolean returns true.
     */

    public static boolean deleteProduct(Product selectedProduct) {
        getAllProducts().remove(selectedProduct);
        return true;
    }

    /**
     * Getter for allParts.
     * @return allParts ObservableList.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Getter for allProducts.
     * @return allProducts ObservableList.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
