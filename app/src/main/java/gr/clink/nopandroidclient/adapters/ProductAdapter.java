package gr.clink.nopandroidclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.telerik.widget.list.ListViewDataSourceAdapter;
import com.telerik.widget.list.ListViewHolder;

import java.util.List;

import gr.clink.nopandroidclient.model.Product;

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
        typedViewHolder.bind((Product) entity);
    }
}
