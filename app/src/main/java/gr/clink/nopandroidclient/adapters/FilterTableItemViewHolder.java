package gr.clink.nopandroidclient.adapters;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.telerik.widget.list.ListViewHolder;

import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.model.Option;

/**
 * Created by themisp on 18/4/2017.
 */

class FilterTableItemViewHolder extends ListViewHolder {
    public Option entity;
    public TextView name;
    private RelativeLayout editView;
    public FilterTableItemViewHolder(View itemView) {
        super(itemView);
        this.editView =(RelativeLayout)  itemView.findViewById(R.id.filter_item_view_holder);
        this.name = (TextView) itemView.findViewById(R.id.filter_name);
    }


    public void bind(Option entity, View editOption) {
        this.entity = entity;
        name.setText(entity.getName());
        editView.addView(editOption);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) editOption.getLayoutParams();
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        editOption.setLayoutParams(lp);

    }
}
