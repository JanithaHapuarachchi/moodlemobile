package mrt.lk.moodlemobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mrt.lk.moodlemobile.R;
import mrt.lk.moodlemobile.data.GroupProjectItem;
import mrt.lk.moodlemobile.data.GroupSubProjectItem;

/**
 * Created by janithah on 9/23/2018.
 */

public class GroupSubProjectAdapter extends ArrayAdapter {


    public ArrayList<GroupSubProjectItem> items ;
    private final LayoutInflater mInflater;
    private final Context context;
    public GroupSubProjectAdapter(Context context,ArrayList<GroupSubProjectItem> items) {
        super(context, R.layout.layout_text,items);
        this.items = items;
        this.mInflater = LayoutInflater.from(context);
        this.context =context;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;
        TextView text1;
        if (v == null) {
            v = mInflater.inflate(R.layout.layout_text, viewGroup, false);
            v.setTag(R.id.text1, v.findViewById(R.id.text1));
        }
        text1 = (TextView) v.getTag(R.id.text1);
        GroupSubProjectItem item = items.get(position);
        text1.setText(item.project_name);
        return  v;
    }
}
