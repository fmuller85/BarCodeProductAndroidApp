package fr.miage.barcodeproduct.model.remote;


public class ProductSuggestion {
    private Long id;
    private String name;
    private ProductCompagny compagny = new ProductCompagny("");

    public String getGtin() {
        return String.valueOf(id);
    }

    public void setGtin(String gtin) {
        this.id = Long.valueOf(gtin);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
