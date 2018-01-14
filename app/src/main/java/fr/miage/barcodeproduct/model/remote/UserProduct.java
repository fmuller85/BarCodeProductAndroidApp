package fr.miage.barcodeproduct.model.remote;


import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class UserProduct {
    private String id;
    private ProductSuggestion gtin;
    private String name;
    private Double price;
    @Nullable
    private Long datePurchase;
    @Nullable
    private Long dateEndCommercialWarranty;
    @Nullable
    private Long dateEndConstructorWarranty;
    private ArrayList<ProductDocument> documents;
    @Nullable
    private ProductStore purchaseLocation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProductSuggestion getGtin() {
        return gtin;
    }

    public void setGtin(ProductSuggestion gtin) {
        this.gtin = gtin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        if(price == null){
            return 0.0;
        }
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Nullable
    public Date getDatePurchase() {
        if(datePurchase != null){
            return new Date(datePurchase);
        }
        return null;
    }

    public void setDatePurchase(@Nullable Long datePurchase) {
        this.datePurchase = datePurchase;
    }

    @Nullable
    public Date getDateEndCommercialWarranty() {
        if(dateEndCommercialWarranty != null){
            return new Date(dateEndCommercialWarranty);
        }
        return null;
    }

    public void setDateEndCommercialWarranty(@Nullable Long dateEndCommercialWarranty) {
        this.dateEndCommercialWarranty = dateEndCommercialWarranty;
    }

    @Nullable
    public Date getDateEndConstructorWarranty() {
        if(dateEndConstructorWarranty != null){
            return new Date(dateEndConstructorWarranty);
        }
        return null;
    }

    public void setDateEndConstructorWarranty(@Nullable Long dateEndConstructorWarranty) {
        this.dateEndConstructorWarranty = dateEndConstructorWarranty;
    }

    public ArrayList<ProductDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<ProductDocument> documents) {
        this.documents = documents;
    }

    @Nullable
    public ProductStore getPurchaseLocation() {
        return purchaseLocation;
    }

    public void setPurchaseLocation(@Nullable ProductStore purchaseLocation) {
        this.purchaseLocation = purchaseLocation;
    }
}
