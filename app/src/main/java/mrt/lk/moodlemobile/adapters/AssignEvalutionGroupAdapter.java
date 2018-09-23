package mrt.lk.moodlemobile.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import mrt.lk.moodlemobile.AssignEvaluationGroupActivity;
import mrt.lk.moodlemobile.R;
import mrt.lk.moodlemobile.data.CourseGroupItem;

/**
 * Created by janithah on 9/23/2018.
 */

public class AssignEvalutionGroupAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> header;
    private HashMap<String, ArrayList<CourseGroupItem>> child;

    public AssignEvalutionGroupAdapter(Context context, ArrayList<String> header,HashMap<String, ArrayList<CourseGroupItem>> child){
        this.context =context;
        this.header =header;
        this.child = child;
    }

    @Override
    public int getGroupCount() {
        return this.header.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.child.get(this.header.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.header.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.child.get(this.header.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        // Getting header title
        String headerTitle = (String) getGroup(groupPosition);

        // Inflating header layout and setting text
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.header, parent, false);
        }

        TextView header_text = (TextView) convertView.findViewById(R.id.header);
        header_text.setText(headerTitle);
        header_text.setPadding(0,50,0,50);

        // If group is expanded then change the text into bold and change the
        // icon
        if (isExpanded) {
            header_text.setTypeface(null, Typeface.BOLD);
            header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up, 0);

        } else {
            // If group is not expanded then change the text back into normal
            // and change the icon

            header_text.setTypeface(null, Typeface.NORMAL);
            header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
        }
        if (isExpanded)
            convertView.setPadding(0, 30, 0, 30);
        else
            convertView.setPadding(0, 0, 0, 20);


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        CourseGroupItem childData = (CourseGroupItem) getChild(groupPosition, childPosition);
        final String group_name = childData.group_name;
        LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.layout_add_remove_evaluationgroup, parent, false);
        LinearLayout layout_add_remove = (LinearLayout) convertView.findViewById(R.id.layout_add_remove);
        TextView txt_course_group  = convertView.findViewById(R.id.txt_course_group);
        txt_course_group.setText(group_name);
        ImageView img_add_group  = convertView.findViewById(R.id.img_add_group);
        ImageView img_remove_group = convertView.findViewById(R.id.img_remove_group);
        if(groupPosition == 0 ){
            layout_add_remove.setBackgroundResource(R.color.white);
            img_add_group.setVisibility(View.GONE);
            img_remove_group.setVisibility(View.VISIBLE);
        }
        else if(groupPosition == 1){
            img_remove_group.setVisibility(View.GONE);
            img_add_group.setVisibility(View.VISIBLE);
            layout_add_remove.setBackgroundResource(R.color.app_background);
        }

        img_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_group(childPosition);
            }
        });

        img_remove_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove_group(childPosition);
            }
        });

        return convertView;
    }

    private void remove_group(int childposition){
        ArrayList<CourseGroupItem> childN = child.get(header.get(0));
        ArrayList<CourseGroupItem> childN2 = child.get(header.get(1));
        childN2.add(childN.get(childposition));
        childN.remove(childposition);
        //setItems(driverId);
        notifyDataSetChanged();
        Toast toaster = Toast.makeText(context, "Succesfully Removed", Toast.LENGTH_SHORT);
        toaster.show();
    }

    private void add_group(int childposition){
        ArrayList<CourseGroupItem> childN = child.get(header.get(1));
        ArrayList<CourseGroupItem> childN2 = child.get(header.get(0));
        childN2.add(childN.get(childposition));
        childN.remove(childposition);
        //setItems(driverId);
        notifyDataSetChanged();
        Toast toaster = Toast.makeText(context, "Succesfully Added", Toast.LENGTH_SHORT);
        toaster.show();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
