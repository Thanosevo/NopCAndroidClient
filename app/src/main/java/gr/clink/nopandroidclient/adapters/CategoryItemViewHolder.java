package gr.clink.nopandroidclient.adapters;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.telerik.widget.list.ListViewHolder;

import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.model.Category;
import gr.clink.nopandroidclient.other.Globals;


class CategoryItemViewHolder extends ListViewHolder {

    public SimpleDraweeView itemImage;
    public TextView subtitle;
    public TextView title;
    public Category entity;
    private String prefix;
    public CategoryItemViewHolder(View itemView) {
        super(itemView);
        prefix = itemView.getResources().getString(R.string.category_prefix);
        this.itemImage = (SimpleDraweeView) itemView.findViewById(R.id.imageView);
        this.title = (TextView) itemView.findViewById(R.id.title);
        this.subtitle = (TextView) itemView.findViewById(R.id.subtitle);

        View recipeInfoBackground = itemView.findViewById(R.id.recipeInfo);
        if (recipeInfoBackground != null) {
            recipeInfoBackground.setBackgroundDrawable(Globals.createInfoBackground(false));
        }
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