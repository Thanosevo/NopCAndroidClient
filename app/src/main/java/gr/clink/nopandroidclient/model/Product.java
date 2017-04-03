package gr.clink.nopandroidclient.model;

import java.util.ArrayList;


/**
 * Created by themisp on 16/1/2017.
 */

public class Product {

    private String productName;
    private Integer productId;
    private String shortDescription;
    private String fullDescription;
    private Integer stockQuantity;
    private Float   productPrice;
    private ArrayList<String> pictureURLS;
    private String CategoryName;

    public Product(){

    }

    public Product(String productName , String categoryName, Integer productId, String shortDescription, String fullDescription, Integer stockQuantity, Float productPrice, ArrayList<String> pictureURLS) {
        this.productName = productName;
        this.productId = productId;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.stockQuantity = stockQuantity;
        this.productPrice = productPrice;
        this.pictureURLS = pictureURLS;
        CategoryName = categoryName;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Float productPrice) {
        this.productPrice = productPrice;
    }

    public ArrayList<String> getPictureURLS() {
        return pictureURLS;
    }

    public String getDefaultPuctureURL(){
        if (pictureURLS != null && !pictureURLS.isEmpty())
            return  pictureURLS.get(0);
        else
            return null;
    }

    public void setPictureURLS(ArrayList<String> pictureURLS) {
        this.pictureURLS = pictureURLS;
    }
}
