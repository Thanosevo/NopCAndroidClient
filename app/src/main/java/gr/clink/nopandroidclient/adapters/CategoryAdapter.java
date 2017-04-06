package gr.clink.nopandroidclient.adapters;

/**
 * Created by Thanos on 1/10/2017.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.telerik.widget.list.ListViewDataSourceAdapter;
import com.telerik.widget.list.ListViewHolder;
import java.util.List;
import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.model.Category;


public class CategoryAdapter extends ListViewDataSourceAdapter {
    private Context context;
    private int itemLayoutId;

    public CategoryAdapter(List items, Context context, int itemLayoutId) {
        super(items);
        this.context = context;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public ListViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int viewType) {
        return new CategoryItemViewHolder(LayoutInflater.from(this.context).inflate(itemLayoutId, viewGroup, false));
    }

    @Override
    public void onBindItemViewHolder(ListViewHolder holder, Object entity) {
        CategoryItemViewHolder typedViewHolder = (CategoryItemViewHolder) holder;
        typedViewHolder.bind((Category) entity);
    }

    @Override
    public ListViewHolder onCreateGroupViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View rootView = inflater.inflate(R.layout.list_view_list_mode_group_item_layout, viewGroup, false);
        Typeface font = Typeface.createFromAsset(this.context.getAssets(), "fonts/RobotoSlab700.ttf");
        GroupItemViewHolder holder = new GroupItemViewHolder(rootView);
        holder.txtGroupTitle.setTypeface(font);
        return holder;
    }

    @Override
    public void onBindGroupViewHolder(ListViewHolder holder, Object groupKey) {
        GroupItemViewHolder typedViewHolder = (GroupItemViewHolder) holder;
        typedViewHolder.txtGroupTitle.setText(groupKey.toString().toUpperCase());
    }
}