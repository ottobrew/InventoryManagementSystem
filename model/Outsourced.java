package model;

/** Outsourced class inherits Part class.  */

public class Outsourced extends Part {

    private String companyName;

    /**
     * Constructor for Outsourced Part objects.
     * Calls Part superclass constructor for id, name, price, stock, min, and max.
     * @param id part ID
     * @param name part name
     * @param price part price
     * @param stock inventory
     * @param min minimum inv
     * @param max maximum inv
     * @param companyName Company Name
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
            // Call Part superclass constructor
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Getter.
     * @return get Outsourced company name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Setter.
     * @param companyName part company name to set
     */

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Method to format price for Tableviews
     * @return Price formatted to two decimal places.
     */

    public String getFormattedPrice() {
        return String.format("%.2f", getPrice());
    }
}
