package gr.clink.nopandroidclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import customfonts.MyTextView;
import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.model.Product;
import gr.clink.nopandroidclient.other.Globals;

/**
 * Created by themisp on 6/4/2017.
 */

public class AddToCartPopUpActivity extends AppCompatActivity {
    private Product product;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product = getIntent().getExtras().getParcelable(Globals.PRODUCT);;
        setContentView(R.layout.add_to_cart_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = Math.min((int)(dm.widthPixels * 0.85),1220);
        int height = Math.min((int)(dm.heightPixels * 0.6),1400);
        getWindow().setLayout(width,height);


        View topContainer = findViewById(R.id.top_container);
        topContainer.setBackgroundDrawable(Globals.createInfoBackground(true));

        SimpleDraweeView itemImage = (SimpleDraweeView) findViewById(R.id.imageView);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(product.getDefaultPuctureURL()))

                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(itemImage.getController())
                .build();
        itemImage.setController(controller);


        MyTextView btnGoToCart =(MyTextView) findViewById(R.id.btn_go_to_cart);
        btnGoToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Globals.POPUPRESULT,Globals.PopupResponses.GOTOCART);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        MyTextView btnContinueShopping =(MyTextView) findViewById(R.id.btn_continue_shopping);
        btnContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Globals.POPUPRESULT,Globals.PopupResponses.CONTINUESHOPPING);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });


    }

}
