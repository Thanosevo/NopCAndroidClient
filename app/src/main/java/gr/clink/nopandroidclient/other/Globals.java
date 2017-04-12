package gr.clink.nopandroidclient.other;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

/**
 * Created by themisp on 9/1/2017.
 */

public class Globals {
    //region server-related
    public final static String HOST = "http://10.0.2.2:15536";
    public final static String GET_CATEGORIES = "/Plugins/RestApi/GetCategories";
    public final static String GET_PRODUCTS = "/Plugins/RestApi/GetProductsOfCategory";
    public final static String PARAM_CATEGORY_ID = "categoryId";
    public final static String POST_CUSTOMER_LOGIN = "/Plugins/RestApi/checkCustomerLogin";
    //endregion

    //region activity-intents
    public static class ProductsByCatagoryIdActivityProperties{
        public static String ID = "categoryID";
        public static String CATEGORY_NAME = "categoryName";
    }
    public final static String PRODUCT = "ParcelableProduct";

    public static String POPUPRESULT = "popupResult";
    public static class PopupResponses{
        public final static  String GOTOCART= "goToCart";
        public final  static String CONTINUESHOPPING = "continueShopping";
    }

    //endregion

    public static Drawable createInfoBackground(Boolean upDown) {
        ShapeDrawable.ShaderFactory shaderFactory;
        if (!upDown) {
            shaderFactory = new ShapeDrawable.ShaderFactory() {
                @Override
                public Shader resize(int width, int height) {
                    return new LinearGradient(width / 2.0f, 0, width / 2.0f, height,
                            new int[]{0x00ffffff, 0xccffffff, 0xccffffff},
                            new float[]{0, 0.3f, 1}, Shader.TileMode.REPEAT);
                }
            };
        }else{
            shaderFactory = new ShapeDrawable.ShaderFactory() {
                @Override
                public Shader resize(int width, int height) {
                    return new LinearGradient(width / 2.0f, height, width / 2.0f, 0,
                            new int[]{0x00ffffff, 0xccffffff, 0xccffffff},
                            new float[]{0, 0.3f, 1}, Shader.TileMode.REPEAT);
                }
            };
        }

        PaintDrawable background = new PaintDrawable();
        background.setShape(new RectShape());
        background.setShaderFactory(shaderFactory);

        return background;
    }
}
