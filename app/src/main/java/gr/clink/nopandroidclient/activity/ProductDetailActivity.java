package gr.clink.nopandroidclient.activity;

/**
 * Created by Thanos on 1/18/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.telerik.android.common.Procedure;
import com.telerik.widget.dataform.visualization.RadDataForm;
import com.telerik.widget.dataform.visualization.core.EntityPropertyViewer;

import org.fabiomsr.moneytextview.MoneyTextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import customfonts.MyTextView;
import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.fragment.ChildAnimationExample;
import gr.clink.nopandroidclient.fragment.SliderLayout;
import gr.clink.nopandroidclient.model.CartProduct;
import gr.clink.nopandroidclient.model.Product;
import gr.clink.nopandroidclient.model.UserInformation;
import gr.clink.nopandroidclient.other.Globals;

public class ProductDetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {
    SliderLayout mDemoSlider;

    Product product;
    //    Typeface fonts1,fonts2;
    LinearLayout linear1,linear2, linear3, linear4, linear5, linear6;

    private TextView discription3, productFullDescriptionTextView, productCategoryTextView, productNameTextView;
    private RadDataForm productAttributesDataForm;
    private MoneyTextView productPriceTextView;
    private MyTextView addToCart;
    private RelativeLayout back_dim_layout;

    public void enableDimBackground(){
        back_dim_layout.setVisibility(View.VISIBLE);
    }

    public void disableDimBackground(){
        back_dim_layout.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        disableDimBackground();
        if(resultCode == Activity.RESULT_OK){
            String result = data.getExtras().getString(Globals.POPUPRESULT);
            switch (result){
                case Globals.PopupResponses.CONTINUESHOPPING:
                    break;
                case Globals.PopupResponses.GOTOCART:
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(Globals.POPUPRESULT,Globals.PopupResponses.GOTOCART);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        back_dim_layout = (RelativeLayout) findViewById(R.id.bac_dim_layout);
        // Get Product Details from prev screen
        product = getIntent().getExtras().getParcelable(Globals.PRODUCT);
        // Set Category TextView
        productCategoryTextView  = (TextView)findViewById(R.id.productCategory);
        productCategoryTextView.setText(product.getCategoriesToString());
        // Set Product Name TextView
        productNameTextView = (TextView)findViewById(R.id.itemname);
        productNameTextView.setText(product.getProductName());
        // Set Product Price TextView
        productPriceTextView = (MoneyTextView)findViewById(R.id.price);
        productPriceTextView.setAmount(product.getProductPrice());


        ImageView backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addToCart = (MyTextView) findViewById(R.id.textview_add_to_cart) ;
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInformation.getInstance().addCartProduct(new CartProduct(product,1));
                Intent i = new Intent(ProductDetailActivity.this, AddToCartPopUpActivity.class);
                i.putExtra(Globals.PRODUCT,product);
                enableDimBackground();
                startActivityForResult(i,Globals.REQUEST_CODE_ADD_TO_CART);
            }
        });

        //System.out.println(productAttributes.size() + "<<<<<<<------------------------");

//                ***********description**********

        linear1 = (LinearLayout)findViewById(R.id.linear1);
        linear2 = (LinearLayout)findViewById(R.id.linear2);
        productFullDescriptionTextView = (TextView)findViewById(R.id.discription1);
        // Set Product Description
        productFullDescriptionTextView.setText(product.getFullDescription());

        linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linear2.setVisibility(View.VISIBLE);
                linear1.setVisibility(View.GONE);
                productFullDescriptionTextView.setVisibility(View.VISIBLE);

            }
        });

        linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linear2.setVisibility(View.GONE);
                linear1.setVisibility(View.VISIBLE);
                productFullDescriptionTextView.setVisibility(View.GONE);


            }
        });



//                ***********Attributes**********

        linear3 = (LinearLayout)findViewById(R.id.linear3);
        linear4 = (LinearLayout)findViewById(R.id.linear4);


        productAttributesDataForm = (RadDataForm)findViewById(R.id.attributesDataForm);
        String productAttributesJSON = new Gson().toJson(product.getProductAttributes());

        try {
            JSONObject jsonObject = new JSONObject(productAttributesJSON);
            productAttributesDataForm.setEntity(jsonObject);
        } catch (JSONException e) {
            Log.e("json", "error parsing json", e);
        }

        //productAttributesDataForm.setLabelPosition(LabelPosition.LEFT);
        productAttributesDataForm.setIsReadOnly(true);


        linear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linear4.setVisibility(View.VISIBLE);
                linear3.setVisibility(View.GONE);
                productAttributesDataForm.setVisibility(View.VISIBLE);

            }
        });

        linear4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linear4.setVisibility(View.GONE);
                linear3.setVisibility(View.VISIBLE);
                productAttributesDataForm.setVisibility(View.GONE);


            }
        });



//                ***********design**********

        linear5 = (LinearLayout)findViewById(R.id.linear5);
        linear6 = (LinearLayout)findViewById(R.id.linear6);
        discription3 = (TextView)findViewById(R.id.discription3);


        linear5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linear6.setVisibility(View.VISIBLE);
                linear5.setVisibility(View.GONE);
                discription3.setVisibility(View.VISIBLE);

            }
        });

        linear6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linear6.setVisibility(View.GONE);
                linear5.setVisibility(View.VISIBLE);
                discription3.setVisibility(View.GONE);


            }
        });



//        fonts1 =  Typeface.createFromAsset(MainActivity.this.getAssets(),
//                "fonts/Lato-Light.ttf");
//
//
//
//        fonts2 =  Typeface.createFromAsset(MainActivity.this.getAssets(),
//                "fonts/Lato-Regular.ttf");
//
//
//        TextView t5 =(TextView)findViewById(R.id.title);
//        t5.setTypeface(fonts1);



//         ********Product Image Slider*********

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,String> file_maps = new HashMap<String, String>();

        for (int i = 0; i < product.getPictureURLS().size(); i++){
            file_maps.put(String.valueOf(i), product.getPictureURLS().get(i));
        }



        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    //  .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);


            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new ChildAnimationExample());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);


        productAttributesDataForm.setEditorCustomizations(new Procedure<EntityPropertyViewer>() {
            @Override
            public void apply(EntityPropertyViewer entityPropertyViewer) {
                TextView headerView = (TextView)entityPropertyViewer.getHeaderView();
                Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                headerView.setTypeface(boldTypeface);
            }
        });
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

    }


}
