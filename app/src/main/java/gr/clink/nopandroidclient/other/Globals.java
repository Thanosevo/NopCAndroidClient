package gr.clink.nopandroidclient.other;

/**
 * Created by themisp on 9/1/2017.
 */

public class Globals {
    public final static String HOST = "http://10.0.2.2:15536";
    public final static String GET_CATEGORIES = "/Plugins/RestApi/GetCategories";
    public final static String GET_PRODUCTS = "/Plugins/RestApi/GetProductsOfCategory";
    public final static String PARAM_CATEGORY_ID = "categoryId";
    public final static String POST_CUSTOMER_LOGIN = "/Plugins/RestApi/checkCustomerLogin";


    public static class ProductsByCatagoryIdActivityProperties{
        public static String ID = "categoryID";
        public static String CATEGORY_NAME = "categoryName";
    }

    public static class ProductDetails{
        public static String PRODUCT_CATEGORY = "productCategory";
        public static String PRODUCT_PRICE = "productPrice";
        public static String PRODUCT_DESCRIPTION = "productDescription";
        public static String PRODUCT_NAME = "productName";
        public static String PRODUCT_IMAGES = "productImages";
    }

}
