package fr.miage.barcodeproduct.model;

import org.parceler.Parcel;

import io.realm.RealmObject;

@Parcel
public class Store extends RealmObject{
    String name;
    String city;
    String address;
    String website;
    boolean online;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "Store{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", website='" + website + '\'' +
                ", online=" + online +
                '}';
    }
}
