package gr.clink.nopandroidclient.adapters;

import android.content.Context;
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
import com.telerik.widget.numberpicker.RadNumberPicker;

import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.model.Category;


class ShoppingCartItemViewHolder extends ListViewHolder {

    public SimpleDraweeView itemImage;
    public TextView subtitle;
    public TextView title;
    public Category entity;
    public RadNumberPicker nPicker;
    private String prefix;

    public ShoppingCartItemViewHolder(View itemView, Context context) {
        super(itemView);
        prefix = itemView.getResources().getString(R.string.category_prefix);
        this.itemImage = (SimpleDraweeView) itemView.findViewById(R.id.imageView);
        this.title = (TextView) itemView.findViewById(R.id.title);
        this.subtitle = (TextView) itemView.findViewById(R.id.subtitle);
        this.nPicker = (RadNumberPicker) itemView.findViewById(R.id.number_picker);
        this.nPicker.setMinimum(1);
        this.nPicker.setMaximum(200);
        this.nPicker.setPluralFormatString("%.0f " + context.getResources().getString(R.string.cart_products));
        this.nPicker.setSingleFormatString("%.0f " + context.getResources().getString(R.string.cart_product));

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

    public void bind(Category entity) {
        this.entity = entity;

        this.title.setText(entity.getName());
        this.subtitle.setText(prefix +" "+entity.getParentCategoryName());

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(entity.getImageURL()))

                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(itemImage.getController())
                .build();
        itemImage.setController(controller);

    }
}