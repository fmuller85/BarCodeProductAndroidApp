package fr.miage.barcodeproduct.model;


import org.parceler.Parcel;

import io.realm.RealmObject;

@Parcel
public class Document extends RealmObject{
    String id;
    String fileName;
    String absolutePath;
    String productSyncId;

    boolean downloaded;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getProductSyncId() {
        return productSyncId;
    }

    public void setProductSyncId(String productSyncId) {
        this.productSyncId = productSyncId;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }
}
