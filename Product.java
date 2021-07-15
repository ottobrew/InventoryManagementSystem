package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Product Class.
 */
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Constructor for Product objects.
     * @param id product id
     * @param name product name
     * @param price product price
     * @param stock product inventory
     * @param min minimum inv
     * @param max maximum inv
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     *
     * @return product id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return product name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return product price
     */
    public double getPrice() {
        return price;
    }

    /**
     *
     * @param price price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     *
     * @return product stock or inventory
     */
    public int getStock() {
        return stock;
    }

    /**
     *
     * @param stock stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     *
     * @return minimum inv
     */
    public int getMin() {
        return min;
    }

    /**
     *
     * @param min minimum to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     *
     * @return maximum inv
     */
    public int getMax() {
        return max;
    }

    /**
     *
     * @param max max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Method to add part as a Product's Associated Part
     * @param part part to add to Associated Parts ObservableLIst
     */
    public void addAssociatedPart(Part part) {
            associatedParts.add(part);
        }

    /**
     * Confirm delete part will also delete from AssociatedPart ObservableList connected with products
     * @param selectedAssociatedPart Associated Part selected by user to be removed
     * @return boolean true
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        getAllAssociatedParts().remove(selectedAssociatedPart);
        return true;
    }

    /**
     * Getter for Associated Parts ObservableList
     * @return associatedParts ObservableList
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    /**
     * Method to format price for Tableviews
     * @return Price formatted to two decimal places.
     */
    public String getFormattedPrice() {
        return String.format("%.2f", getPrice());
    }
}
