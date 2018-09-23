package mrt.lk.moodlemobile.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import mrt.lk.moodlemobile.R;
import mrt.lk.moodlemobile.data.CourseGroupItem;
import mrt.lk.moodlemobile.data.EvaluationResultItem;

/**
 * Created by janithah on 9/23/2018.
 */

public class EvaluationResultsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> header;
    private HashMap<String, ArrayList<EvaluationResultItem>> child;

    public EvaluationResultsAdapter(Context context, ArrayList<String> header,HashMap<String, ArrayList<EvaluationResultItem>> child){
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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        EvaluationResultItem childData = (EvaluationResultItem) getChild(groupPosition, childPosition);
        //final String group_name = childData.group_name;
        LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.layout_evaluation_result_item, parent, false);

        TextView txt_course_group  = convertView.findViewById(R.id.txt_title);
        TextView txt_marks  = convertView.findViewById(R.id.txt_marks);
        TextView txt_comment  = convertView.findViewById(R.id.txt_comment);
        txt_course_group.setText("Project "+childData.project_name+" by "+childData.participant_name);
        txt_marks.setText(childData.marks);
        txt_comment.setText(childData.comments);
        if(groupPosition == 0 ){

        }
        else if(groupPosition == 1){

        }



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
