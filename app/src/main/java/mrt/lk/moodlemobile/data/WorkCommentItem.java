package mrt.lk.moodlemobile.data;

import java.util.ArrayList;

/**
 * Created by janithah on 9/24/2018.
 */

public class WorkCommentItem {
    public String comment_id;
    public ParticipantItem participant;
    public String time;
    public String comment_type;
    public String comment;
    public String comment_location;
    public boolean isDiary;
    public boolean isProject =true;
    public String project_name;
    public ArrayList<WorkSeenItem> seen_list;
}
