package fr.miage.barcodeproduct;

import junit.framework.Assert;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.miage.barcodeproduct.model.Product;
import fr.miage.barcodeproduct.model.ProductMapper;
import fr.miage.barcodeproduct.model.remote.ProductSuggestion;
import fr.miage.barcodeproduct.model.remote.UserProduct;
import fr.miage.barcodeproduct.utils.Util;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testListToString(){
        List<String> stringList = new ArrayList<>();
        stringList.add("one");
        stringList.add("two");
        stringList.add("three");
        String res = Util.listToString(stringList);
        assertEquals("one,two,three",res);

        res = Util.listToString(Collections.<String>emptyList());
        assertEquals("",res);
    }

    @Test
    public void StringToList(){
        String string = "one,two,three";

        List<String> stringList = new ArrayList<>();
        stringList.add("one");
        stringList.add("two");
        stringList.add("three");

        assertEquals(stringList, Util.stringToList(string));

        string = "";
        stringList = Util.stringToList(string);
        assertEquals(0, stringList.size());
    }

    @Test
    public void productMapperTest(){
        ProductSuggestion productSuggestion = new ProductSuggestion();
        productSuggestion.setGtin("565123151531");
        productSuggestion.setName("Coca Cola Zero");

        UserProduct userProduct = new UserProduct();
        userProduct.setName("Coca Cola Zero");
        userProduct.setGtin(productSuggestion);
        userProduct.setDateEndCommercialWarranty(Long.valueOf("1516665600000"));
        userProduct.setDateEndConstructorWarranty(null);

        ProductMapper productMapper = new ProductMapper();
        Product product = productMapper.from(userProduct);

        Assert.assertNull(product.getConstructorWarrantyDate());
        Assert.assertNotNull(product.getCommercialWarrantyDate());
        Assert.assertEquals(userProduct.getName(), product.getName());
    }
}