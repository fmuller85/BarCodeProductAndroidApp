package fr.miage.barcodeproduct.model.remote;


public class ProductCompagny {
    private String name;

    ProductCompagny(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
