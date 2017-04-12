package gr.clink.nopandroidclient.adapters;

import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.telerik.widget.list.ListViewHolder;

import org.fabiomsr.moneytextview.MoneyTextView;

import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.model.Product;
import gr.clink.nopandroidclient.other.Globals;

/**
 * Created by themisp on 16/1/2017.
 */

public class ProductItemViewHolder extends ListViewHolder {

        public SimpleDraweeView itemImage;
        public TextView title;
        public TextView subtitle;
        public Product entity;
        public MoneyTextView price;
        public ImageButton addToCartButton;

        public ProductItemViewHolder(View itemView) {
            super(itemView);

            this.itemImage = (SimpleDraweeView) itemView.findViewById(R.id.imageView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            this.price = (MoneyTextView) itemView.findViewById(R.id.moneyTextView);
            this.addToCartButton = (ImageButton) itemView.findViewById(R.id.btn_add_to_cart);

            View recipeInfoBackground = itemView.findViewById(R.id.recipeInfo);
            if (recipeInfoBackground != null) {
                recipeInfoBackground.setBackgroundDrawable(Globals.createInfoBackground(false));
            }
    }


    public void bind(Product entity) {
        this.entity = entity;

        this.title.setText(entity.getProductName());
        this.subtitle.setText(entity.getShortDescription());
        this.price.setAmount(entity.getProductPrice());

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(entity.getDefaultPuctureURL()))

                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(itemImage.getController())
                .build();
        itemImage.setController(controller);

    }
}
