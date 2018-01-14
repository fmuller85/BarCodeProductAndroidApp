package fr.miage.barcodeproduct.model;


import java.util.UUID;

import fr.miage.barcodeproduct.model.remote.ProductDocument;
import fr.miage.barcodeproduct.model.remote.ProductStore;
import fr.miage.barcodeproduct.model.remote.ProductSuggestion;
import fr.miage.barcodeproduct.model.remote.UserProduct;
import io.realm.RealmList;

public class ProductMapper {
    public Product from(UserProduct userProduct) {
        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setSyncId(userProduct.getId());
        product.setCodebar(userProduct.getGtin().getGtin());
        product.setName(userProduct.getName());
        product.setPrice(userProduct.getPrice());
        product.setPurchaseDate(userProduct.getDatePurchase());
        product.setCommercialWarrantyDate(userProduct.getDateEndCommercialWarranty());
        product.setConstructorWarrantyDate(userProduct.getDateEndConstructorWarranty());
        product.setDocuments(new RealmList<Document>());
        product.setSend(true);

        if (userProduct.getPurchaseLocation() != null) {
            ProductStore productStore = userProduct.getPurchaseLocation();
            Store store = new Store();
            store.setOnline(productStore.isOnline());
            store.setName(productStore.getName());
            store.setCity(productStore.getCity());
            store.setAddress(productStore.getAddress());
            store.setWebsite(productStore.getAddress());

            product.setStore(store);
        }

        Document doc;
        for (ProductDocument document : userProduct.getDocuments()) {
            doc = new Document();
            doc.setId(document.getId());
            doc.setFileName(document.getFileName());
            doc.setProductSyncId(product.getSyncId());
            product.addDocument(doc);
        }

        return product;
    }

    public UserProduct from(Product product) {
        ProductSuggestion productSuggestion = new ProductSuggestion();
        productSuggestion.setGtin(product.getCodebar());
        productSuggestion.setName(product.getName());

        UserProduct userProduct = new UserProduct();
        userProduct.setId(product.getSyncId());
        userProduct.setGtin(productSuggestion);
        userProduct.setPrice(product.getPrice());
        userProduct.setName(product.getName());

        if(product.getPurchaseDate() != null){
            userProduct.setDatePurchase(product.getPurchaseDate().getTime());
        }

        if(product.getCommercialWarrantyDate() != null){
            userProduct.setDateEndCommercialWarranty(product.getCommercialWarrantyDate().getTime());
        }

        if(product.getConstructorWarrantyDate() != null){
            userProduct.setDateEndConstructorWarranty(product.getConstructorWarrantyDate().getTime());
        }

        if(product.getStore() != null){
            ProductStore productStore = new ProductStore();
            if(product.getStore().isOnline()){
                productStore.setAddress(product.getStore().getWebsite());
            }else{
                productStore.setName(product.getStore().getName());
                productStore.setAddress(product.getStore().getAddress());
                productStore.setCity(product.getStore().getCity());
            }
            productStore.setOnline(product.getStore().isOnline());

            userProduct.setPurchaseLocation(productStore);
        }

        return userProduct;
    }
}
