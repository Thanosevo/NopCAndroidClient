package gr.clink.nopandroidclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
    private ArrayList<String> categories;
    private ArrayList<String> manufacturers;
    private HashMap<String, String> productAttributes;

    //region CTOR-GETTERS-SETTERS
    public Product(){

    }

    public Product(Integer productId, String productName, String shortDescription, String fullDescription, Integer stockQuantity, Float productPrice, ArrayList<String> pictureURLS, ArrayList<String> categories, ArrayList<String> manufacturers, HashMap<String, String> productAttributes) {
        this.productId = productId;
        this.productName = productName;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.stockQuantity = stockQuantity;
        this.productPrice = productPrice;
        this.pictureURLS = pictureURLS;
        this.categories = categories;
        this.manufacturers = manufacturers;
        this.productAttributes = productAttributes;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public String getCategoriesToString(){
        String result = "";
        if(!categories.isEmpty()){
            Boolean in = false;
            for (String category: categories) {
                if(in)
                    result += ", " + category;
                else
                    result += category;
                in = true;
            }
        }
        return result;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public ArrayList<String> getManufacturers() {
        return manufacturers;
    }

    public void setManufacturers(ArrayList<String> manufacturers) {
        this.manufacturers = manufacturers;
    }

    public void setCategoryName(String categoryName) {
        categoryName = categoryName;
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

    //region METHODS
    public static Set<String> getManufacturersFromProducts(List<Product> products){
        Set<String> result = new HashSet<>();

        for (Product p: products) {
            for (String manufacturer: p.manufacturers)
                result.add(manufacturer);
        }
        return result;
    }

    public static Set<String> getCategoriesFromProducts(List<Product> products){
        Set<String> result = new HashSet<>();

        for (Product p: products) {
            for(String category: p.categories)
            result.add(category);
        }
        return result;
    }

    public static Float[] getPriceRangeForProducts(List<Product> products){
        Float min=products.get(0).productPrice,max=products.get(0).productPrice;
        for (Product p: products) {
            Float price = p.getProductPrice();
            if(min > price)
                min = price;
            if(max < price)
                max = price;
        }
        return new Float[]{min,max};
    }

    public static List<Product> getListOfProducts(JSONArray arr) throws JSONException {
        List<Product> result = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject product = arr.getJSONObject(i);
            String name = product.getString("Name");
            Integer id = product.getInt("Id");
            String shortDescription = product.getString("ShortDescription");
            String fullDescription = product.getString("FullDescription");
            Integer stockQuantity = product.getInt("StockQuantity");
            Float price = (float) product.getDouble("Price");
            JSONArray pictureArray = product.getJSONArray("PictureURLs");
            ArrayList<String> pictureURLs = new ArrayList<>();
            for (int j = 0;j < pictureArray.length(); j++) {
                pictureURLs.add(pictureArray.getString(j));

            }
            JSONArray categoriesArray = product.getJSONArray("Categories");
            ArrayList<String> productCategories = new ArrayList<>();
            for (int j = 0;j < categoriesArray.length(); j++) {
                productCategories.add(categoriesArray.getString(j));

            }

            JSONArray manufacturersArray = product.getJSONArray("Manufacturers");
            ArrayList<String> productManufacturers = new ArrayList<>();
            for (int j = 0;j < manufacturersArray.length(); j++) {
                productManufacturers.add(manufacturersArray.getString(j));

            }


            JSONArray attributesArray = product.getJSONArray("ProductAttributes");
            HashMap<String, String> attributes = new HashMap<String, String>();
            for (int k = 0;k < attributesArray.length(); k++) {
                attributes.put(attributesArray.getJSONObject(k).getString("Attribute"), attributesArray.getJSONObject(k).getString("Value"));
            }

            result.add(new Product(id, name, shortDescription, fullDescription, stockQuantity, price, pictureURLs, productCategories, productManufacturers, attributes));
        }
        return result;
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
        if (in.readByte() == 0x01) {
            categories = new ArrayList<String>();
            in.readList(categories, String.class.getClassLoader());
        } else {
            categories = null;
        }
        if (in.readByte() == 0x01) {
            manufacturers = new ArrayList<String>();
            in.readList(manufacturers, String.class.getClassLoader());
        } else {
            manufacturers = null;
        }
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
        if (categories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(categories);
        }
        if (manufacturers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(manufacturers);
        }
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
