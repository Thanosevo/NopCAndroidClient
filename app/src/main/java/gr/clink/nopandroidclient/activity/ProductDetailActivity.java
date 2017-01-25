package gr.clink.nopandroidclient.activity;

/**
 * Created by Thanos on 1/18/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.fabiomsr.moneytextview.MoneyTextView;

import java.util.HashMap;
import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.fragment.ChildAnimationExample;
import gr.clink.nopandroidclient.fragment.SliderLayout;
import gr.clink.nopandroidclient.other.Globals;

public class ProductDetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {
    SliderLayout mDemoSlider;

    private Float productPrice;
    private String productFullDescription;
    private String productName;
    private String productCategory;

    //    Typeface fonts1,fonts2;
    LinearLayout linear1,linear2, linear3, linear4, linear5, linear6;

    TextView discription3, discription2, discription1, productCategoryTextView, productNameTextView, productFullDescriptionTextView;
    MoneyTextView productPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Get Product Details from prev screen
        Intent intent = getIntent();
        productName = intent.getStringExtra(Globals.ProductDetails.PRODUCT_NAME);
        productPrice = intent.getFloatExtra(Globals.ProductDetails.PRODUCT_PRICE, 0.0f);
        productFullDescription = intent.getStringExtra(Globals.ProductDetails.PRODUCT_DESCRIPTION);
        productCategory = intent.getStringExtra(Globals.ProductDetails.PRODUCT_CATEGORY);
        // Set Category TextView
        productCategoryTextView  = (TextView)findViewById(R.id.productCategory);
        productCategoryTextView.setText(productCategory);
        // Set Product Name TextView
        productNameTextView = (TextView)findViewById(R.id.itemname);
        productNameTextView.setText(productName);
        // Set Product Price TextView
        productPriceTextView = (MoneyTextView)findViewById(R.id.price);
        productPriceTextView.setAmount(productPrice);
        // Set Product Description
        //productFullDescriptionTextView = (TextView)findViewById(R.id.discription);
        //productFullDescriptionTextView.setText(productFullDescription);


//                ***********discription**********

        linear1 = (LinearLayout)findViewById(R.id.linear1);
        linear2 = (LinearLayout)findViewById(R.id.linear2);
        discription1 = (TextView)findViewById(R.id.discription1);


        linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linear2.setVisibility(View.VISIBLE);
                linear1.setVisibility(View.GONE);
                discription1.setVisibility(View.VISIBLE);

            }
        });

        linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linear2.setVisibility(View.GONE);
                linear1.setVisibility(View.VISIBLE);
                discription1.setVisibility(View.GONE);


            }
        });



//                ***********use and care**********

        linear3 = (LinearLayout)findViewById(R.id.linear3);
        linear4 = (LinearLayout)findViewById(R.id.linear4);
        discription2 = (TextView)findViewById(R.id.discription2);


        linear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linear4.setVisibility(View.VISIBLE);
                linear3.setVisibility(View.GONE);
                discription2.setVisibility(View.VISIBLE);

            }
        });

        linear4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linear4.setVisibility(View.GONE);
                linear3.setVisibility(View.VISIBLE);
                discription2.setVisibility(View.GONE);


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



//         ********Slider*********

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("1", R.drawable.rain4);
        file_maps.put("2",R.drawable.rain5);
        file_maps.put("3",R.drawable.rain6);


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

    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}
