package mrt.lk.moodlemobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import mrt.lk.moodlemobile.R;
import mrt.lk.moodlemobile.data.GroupProjectItem;
import mrt.lk.moodlemobile.data.ParticipantItem;

/**
 * Created by janithah on 9/22/2018.
 */

public class GroupAddStudentsAdapter extends ArrayAdapter {

    public ArrayList<ParticipantItem> items ;
    private final LayoutInflater mInflater;
    private final Context context;

    public GroupAddStudentsAdapter(Context context,ArrayList<ParticipantItem> items) {
        super(context, R.layout.layout_text,items);
        this.items = items;
        this.mInflater = LayoutInflater.from(context);
        this.context =context;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;
        TextView text1;
        LinearLayout layout_text;
        if (v == null) {
            v = mInflater.inflate(R.layout.layout_text, viewGroup, false);
            v.setTag(R.id.text1, v.findViewById(R.id.text1));
            v.setTag(R.id.layout_text, v.findViewById(R.id.layout_text));
        }
        text1 = (TextView) v.getTag(R.id.text1);
        layout_text = (LinearLayout) v.getTag(R.id.layout_text);
        ParticipantItem item = items.get(position);
        text1.setText(item.name);

        if (item.isSelected) {
            layout_text.setBackgroundResource(R.color.app_text_color_warning);
        } else {
            layout_text.setBackgroundResource(R.color.white);
        }
        return  v;
    }
}
