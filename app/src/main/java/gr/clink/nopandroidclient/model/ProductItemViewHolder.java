package gr.clink.nopandroidclient.model;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.telerik.widget.list.ListViewHolder;

import org.fabiomsr.moneytextview.MoneyTextView;

import gr.clink.nopandroidclient.R;

/**
 * Created by themisp on 16/1/2017.
 */

public class ProductItemViewHolder extends ListViewHolder {

        public SimpleDraweeView itemImage;
        public TextView title;
        public TextView subtitle;
        public Product entity;
        public MoneyTextView price;

        public ProductItemViewHolder(View itemView) {
            super(itemView);

            this.itemImage = (SimpleDraweeView) itemView.findViewById(R.id.imageView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            this.price = (MoneyTextView) itemView.findViewById(R.id.moneyTextView);

            View recipeInfoBackground = itemView.findViewById(R.id.recipeInfo);
            if (recipeInfoBackground != null) {
                recipeInfoBackground.setBackgroundDrawable(this.createInfoBackground());
            }
    }

    private Drawable createInfoBackground() {
        ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                return new LinearGradient(width / 2.0f, 0, width / 2.0f, height,
                        new int[]{0x00ffffff, 0xccffffff, 0xccffffff},
                        new float[]{0, 0.3f, 1}, Shader.TileMode.REPEAT);
            }
        };

        PaintDrawable background = new PaintDrawable();
        background.setShape(new RectShape());
        background.setShaderFactory(shaderFactory);

        return background;
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
