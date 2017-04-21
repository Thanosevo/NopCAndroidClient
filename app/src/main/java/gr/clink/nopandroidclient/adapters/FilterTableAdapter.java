package gr.clink.nopandroidclient.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.telerik.widget.list.ListViewDataSourceAdapter;
import com.telerik.widget.list.ListViewHolder;

import java.util.List;

import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.model.Option;

/**
 * Created by themisp on 18/4/2017.
 */

public class FilterTableAdapter extends ListViewDataSourceAdapter {
    private Context context;
    private int itemLayoutId;

    public FilterTableAdapter(List items, Context context, int itemLayoutId) {
        super(items);
        this.context = context;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public ListViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int viewType) {
        return new FilterTableItemViewHolder(LayoutInflater.from(this.context).inflate(itemLayoutId, viewGroup, false));
    }

    @Override
    public void onBindItemViewHolder(ListViewHolder holder, Object entity) {
        FilterTableItemViewHolder typedViewHolder = (FilterTableItemViewHolder) holder;
        final Option opt = (Option) entity;
        View editOption;
        if(opt.getValue() instanceof Boolean){
            final CheckBox c = new CheckBox(context);
            c.setChecked((Boolean) opt.getValue());
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    opt.setValue(c.isChecked());
                }
            });
            editOption = c;
        }else{
            TextView t = new TextView(context);
            t.setText("not set");
            editOption = t;
        }
        typedViewHolder.bind((Option) entity, editOption);
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
