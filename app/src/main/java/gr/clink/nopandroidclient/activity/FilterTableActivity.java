package gr.clink.nopandroidclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.view.View;

import com.telerik.android.common.Function;
import com.telerik.widget.list.RadListView;

import java.util.ArrayList;

import customfonts.MyTextView;
import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.adapters.FilterTableAdapter;
import gr.clink.nopandroidclient.model.Option;
import gr.clink.nopandroidclient.other.Globals;

/**
 * Created by themisp on 18/4/2017.
 */

public class FilterTableActivity extends AppCompatActivity {
    RadListView listView;
    ArrayList<Option> options;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_table);
        options = getIntent().getParcelableArrayListExtra(Globals.FILTERS);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int)(dm.widthPixels * 0.92);
        int height = (int)(dm.heightPixels * 0.92);
        getWindow().setLayout(width,height);

        listView = (RadListView) findViewById(R.id.listView);

        GridLayoutManager gridLayout = new GridLayoutManager(this, 1);
        listView.setLayoutManager(gridLayout);

        FilterTableAdapter adapter = new FilterTableAdapter(options, this, R.layout.filtertable_list_item_layout );

        adapter.addGroupDescriptor(new Function<Object, Object>() {
            @Override
            public Object apply(Object argument) {
                Option container = (Option) argument;
                return container.getGroup();
            }
        });
        listView.setAdapter(adapter);

        MyTextView okBtn = (MyTextView) findViewById(R.id.ok_button);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putParcelableArrayListExtra(Globals.FILTERS,options);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        MyTextView cancelBtn = (MyTextView) findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        });

    }
}
