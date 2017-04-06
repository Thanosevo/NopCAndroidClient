package gr.clink.nopandroidclient.adapters;

/**
 * Created by themisp on 10/1/2017.
 */

import android.view.View;
import android.widget.TextView;

import com.telerik.widget.list.ListViewHolder;

import gr.clink.nopandroidclient.R;

/**
 * Created by ginev on 2/17/2015.
 */
public class GroupItemViewHolder extends ListViewHolder {

    public TextView txtGroupTitle;

    public GroupItemViewHolder(View itemView) {
        super(itemView);
        this.txtGroupTitle = (TextView) itemView.findViewById(R.id.txtGroupName);
    }
}