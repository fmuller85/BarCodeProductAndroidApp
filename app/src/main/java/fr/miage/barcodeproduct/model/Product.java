package fr.miage.barcodeproduct.model;


import android.support.annotation.Nullable;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import java.util.Date;

import fr.miage.barcodeproduct.utils.RealmListParcelConverter;
import io.realm.DocumentRealmProxy;
import io.realm.ProductRealmProxy;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@Parcel(implementations = {ProductRealmProxy.class}, value = Parcel.Serialization.FIELD, analyze = {Product.class})
public class Product extends RealmObject {
    @PrimaryKey
    String id;
    @Nullable
    String syncId; // ID from server, can be null if not sent
    String codebar;
    String name;
    double price;
    Date purchaseDate;
    Date commercialWarrantyDate;
    Date constructorWarrantyDate;
    String attachedFilesPath;
    Store store;
    @ParcelPropertyConverter(RealmListParcelConverter.class)
    RealmList<Document> documents;

    boolean send;

    public String getCodebar() {
        return codebar;
    }

    public void setCodebar(String codebar) {
        this.codebar = codebar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getCommercialWarrantyDate() {
        return commercialWarrantyDate;
    }

    public void setCommercialWarrantyDate(Date commercialWarrantyDate) {
        this.commercialWarrantyDate = commercialWarrantyDate;
    }

    public Date getConstructorWarrantyDate() {
        return constructorWarrantyDate;
    }

    public void setConstructorWarrantyDate(Date constructorWarrantyDate) {
        this.constructorWarrantyDate = constructorWarrantyDate;
    }

    public String getAttachedFilesPath() {
        return attachedFilesPath;
    }

    public void setAttachedFilesPath(String attachedFilesPath) {
        this.attachedFilesPath = attachedFilesPath;
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }

    @Nullable
    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void addDocument(Document document) {
        this.documents.add(document);
    }

    public RealmList<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(RealmList<Document> documents) {
        this.documents = documents;
    }

    @Nullable
    public String getSyncId() {
        return syncId;
    }

    public void setSyncId(@Nullable String syncId) {
        this.syncId = syncId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "codebar='" + codebar + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", purchaseDate=" + purchaseDate +
                ", commercialWarrantyDate=" + commercialWarrantyDate +
                ", constructorWarrantyDate=" + constructorWarrantyDate +
                ", attachedFilesPath='" + attachedFilesPath + '\'' +
                ", store=" + store +
                ", send=" + send +
                '}';
    }
}
