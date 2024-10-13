package model;

/** InHouse Class inherits Part Class. */

public class InHouse extends Part {

    private int machineId;

    /**
     * Constructor for InHouse Part objects. Calls Part superclass constructor for id, name, price, stock, min, and max.
     * @param id part ID
     * @param name part name
     * @param price part price
     * @param stock inventory
     * @param min minimum inv
     * @param max maximum inv
     * @param machineId machine ID
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        // Call Part superclass constructor
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Getter.
     * @return InHouse part machineId
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * Setter.
     * @param machineId part machineId to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * Method to format price for Tableviews
     * @return Price formatted to two decimal places.
     */

    public String getFormattedPrice() {
        return String.format("%.2f", getPrice());
    }
}
