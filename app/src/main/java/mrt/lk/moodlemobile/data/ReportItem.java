package mrt.lk.moodlemobile.data;

import java.util.ArrayList;

/**
 * Created by janithah on 9/24/2018.
 */

public class ReportItem {

    public String id;
    public ParticipantItem participant;
    public String time;
    public String report_location;
    public ArrayList<WorkCommentItem> comments;
    public ArrayList<ReportLikeItem> likes;

}
