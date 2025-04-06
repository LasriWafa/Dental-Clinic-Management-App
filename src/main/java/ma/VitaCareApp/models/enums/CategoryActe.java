package ma.VitaCareApp.models.enums;

public enum CategoryActe {

    PREVENTION("Prévention"),
    PROTHESES_DENTAIRES("Prothèses Dentaires"),
    ORTHODONTIE("Orthodontie"),
    IMPLANTOLOGIE("Implantologie"),
    CHIRURGIE("Chirurgie"),
    SOINS_DES_TISSUS_MOUS("Soins des Tissus Mous"),
    SOINS_DES_CARIES("Soins des Caries"),
    DIAGNOSTIC("Diagnostic");

    private final String libeller;

    // Constructor to initialize the libeller
    CategoryActe(String libeller) {
        this.libeller = libeller;
    }

    // Getter for libeller
    public String getLibeller() {
        return libeller;
    }

    // Override toString to return the libeller
    @Override
    public String toString() {
        return libeller;
    }
}