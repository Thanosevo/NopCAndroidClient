package gr.clink.nopandroidclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.telerik.widget.list.ListViewDataSourceAdapter;
import com.telerik.widget.list.ListViewHolder;

import java.util.List;

import gr.clink.nopandroidclient.activity.AddToCartPopUpActivity;
import gr.clink.nopandroidclient.activity.ProductsByCategoryIdActivity;
import gr.clink.nopandroidclient.model.CartProduct;
import gr.clink.nopandroidclient.model.Product;
import gr.clink.nopandroidclient.model.UserInformation;
import gr.clink.nopandroidclient.other.Globals;

/**
 * Created by themisp on 16/1/2017.
 */

public class ProductAdapter extends ListViewDataSourceAdapter {
    private Context context;
    private int itemLayoutId;

    public ProductAdapter(List items, Context context, int itemLayoutId) {
        super(items);
        this.context = context;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public ListViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int viewType) {
        return new ProductItemViewHolder(LayoutInflater.from(this.context).inflate(itemLayoutId, viewGroup, false));
    }

    @Override
    public void onBindItemViewHolder(ListViewHolder holder, Object entity) {
        ProductItemViewHolder typedViewHolder = (ProductItemViewHolder) holder;
        final Product p = (Product) entity ;
        typedViewHolder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    ProductsByCategoryIdActivity activity = (ProductsByCategoryIdActivity)context;
                    activity.enableDimBackground();
                    UserInformation.getInstance().addCartProduct(new CartProduct(p,1));
                    Intent i = new Intent(context, AddToCartPopUpActivity.class);
                    i.putExtra(Globals.PRODUCT,p);
                    activity.startActivityForResult(i,1);
                }catch (Exception e){
                    Toast.makeText(context,"failed to add item to cart",Toast.LENGTH_SHORT).show();
                }

            }
        });

        typedViewHolder.bind(p);
    }
}
