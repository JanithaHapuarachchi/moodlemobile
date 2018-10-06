package mrt.lk.moodlemobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mrt.lk.moodlemobile.CourseProjectsActivity;
import mrt.lk.moodlemobile.GroupDetailsActivity;
import mrt.lk.moodlemobile.R;
import mrt.lk.moodlemobile.data.CourseGroupItem;
import mrt.lk.moodlemobile.data.LoggedUser;
import mrt.lk.moodlemobile.utils.Utility;

/**
 * Created by janithah on 9/21/2018.
 */

public class CourseGroupsAdapter extends ArrayAdapter {

    public ArrayList<CourseGroupItem> items ;
    private final LayoutInflater mInflater;
    private final Context context;
    public static final String EVALUATION_GROUP = "EVALUATION_GROUP";
    public static final String GENERAL_GROUP = "GENERAL_GROUP";
    private final String grouptype;


    public CourseGroupsAdapter(Context context, ArrayList<CourseGroupItem> items, String grouptype) {
        super(context, R.layout.layout_course_group_item,items);
        this.items = items;
        this.mInflater = LayoutInflater.from(context);
        this.context =context;
        this.grouptype = grouptype;
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View v = view;
        TextView txt_course_group;
        ImageView img_view_students,img_view_projects;
        if (v == null) {
            v = mInflater.inflate(R.layout.layout_course_group_item, viewGroup, false);
            v.setTag(R.id.txt_course_group, v.findViewById(R.id.txt_course_group));
            v.setTag(R.id.img_view_students, v.findViewById(R.id.img_view_students));
            v.setTag(R.id.img_view_projects, v.findViewById(R.id.img_view_projects));
        }
        txt_course_group = (TextView) v.getTag(R.id.txt_course_group);
        img_view_students = (ImageView) v.getTag(R.id.img_view_students);
        img_view_projects = (ImageView) v.getTag(R.id.img_view_projects);

        img_view_projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseGroupItem item = items.get(position);
                if(item.is_confirmed) {
                    Intent i = new Intent(context, CourseProjectsActivity.class);
                    i.putExtra("SELECTED_GROUP_ID", item.group_id);
                    i.putExtra("SELECTED_GROUP_NAME", item.group_name);
                    i.putExtra("IS_EVALUATE", true);
                    LoggedUser.status = LoggedUser.AS_EVALUATE;
                    context.startActivity(i);
                }
                else{
                    Utility.showMessage("Wait Until Confirm the Group By Teacher",context);
                }
            }
        });
        img_view_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CourseGroupItem item = items.get(position);
                if(item.is_confirmed){
                Intent i = new Intent(context, GroupDetailsActivity.class);
                i.putExtra("SELECTED_GROUP_ID",item.group_id);
                i.putExtra("SELECTED_GROUP_NAME",item.group_name);
                i.putExtra("IS_EVALUATE",true);
                LoggedUser.status = LoggedUser.AS_EVALUATE;
                context.startActivity(i);
                }
                else{
                    Utility.showMessage("Wait Until Confirm the Group By Teacher",context);
                }
            }
        });

        if(grouptype.equals(EVALUATION_GROUP)){
            img_view_projects.setVisibility(View.GONE);
            img_view_students.setVisibility(View.GONE);
        }


        CourseGroupItem item = items.get(position);
        txt_course_group.setText(item.group_name);
        return  v;
    }
}
