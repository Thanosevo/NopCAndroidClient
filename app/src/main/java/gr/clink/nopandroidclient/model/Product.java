package gr.clink.nopandroidclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by themisp on 16/1/2017.
 */

public class Product implements Parcelable {

    private Integer productId;
    private String productName;
    private String shortDescription;
    private String fullDescription;
    private Integer stockQuantity;
    private Float   productPrice;
    private ArrayList<String> pictureURLS;
    private String CategoryName;
    private HashMap<String, String> productAttributes;

    //region CTOR-GETTERS-SETTERS
    public Product(){

    }

    public Product(String productName , String categoryName, Integer productId, String shortDescription, String fullDescription, Integer stockQuantity, Float productPrice, ArrayList<String> pictureURLS, HashMap<String, String> attributes) {
        this.productName = productName;
        this.productId = productId;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.stockQuantity = stockQuantity;
        this.productPrice = productPrice;
        this.pictureURLS = pictureURLS;
        this.productAttributes = attributes;

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

    public HashMap<String, String> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(HashMap<String, String> productAttributes) {
        this.productAttributes = productAttributes;
    }
    //endregion

    //region Parcellable

    protected Product(Parcel in) {
        productId = in.readByte() == 0x00 ? null : in.readInt();
        productName = in.readString();
        shortDescription = in.readString();
        fullDescription = in.readString();
        stockQuantity = in.readByte() == 0x00 ? null : in.readInt();
        productPrice = in.readByte() == 0x00 ? null : in.readFloat();
        if (in.readByte() == 0x01) {
            pictureURLS = new ArrayList<String>();
            in.readList(pictureURLS, String.class.getClassLoader());
        } else {
            pictureURLS = null;
        }
        CategoryName = in.readString();
        productAttributes = (HashMap) in.readValue(HashMap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (productId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(productId);
        }
        dest.writeString(productName);
        dest.writeString(shortDescription);
        dest.writeString(fullDescription);
        if (stockQuantity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(stockQuantity);
        }
        if (productPrice == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(productPrice);
        }
        if (pictureURLS == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(pictureURLS);
        }
        dest.writeString(CategoryName);
        dest.writeValue(productAttributes);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    //endregion


}
