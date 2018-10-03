package mrt.lk.moodlemobile;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrt.lk.moodlemobile.adapters.ProjectWorksAdapter;
import mrt.lk.moodlemobile.data.LoggedUser;
import mrt.lk.moodlemobile.data.ParticipantItem;
import mrt.lk.moodlemobile.data.WorkCommentItem;
import mrt.lk.moodlemobile.data.WorkSeenItem;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class FCMMessaage extends FirebaseMessagingService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "mrt.lk.moodlemobile.action.FOO";
    public static final String ACTION_BAZ = "mrt.lk.moodlemobile.action.BAZ";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "mrt.lk.moodlemobile.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "mrt.lk.moodlemobile.extra.PARAM2";
    private static final int FCM_MESSAGE_ID = 125;
    public static String function_project ="Project";
    public static String function_subproject ="Subproject";
    public boolean show_notification_only =false;
    WorkCommentItem item;
    String prj_id;
//
//    public FCMMessaage() {
//        super("FCMMessaage");
//    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("Moodle",remoteMessage.getData().toString());
        String body  = remoteMessage.getData().get("body");
        String title  = remoteMessage.getData().get("title");
        String function  = remoteMessage.getData().get("function");
        Log.e("Moodle body",body+" "+function);
        show_notification_only =false;
        prj_id =  remoteMessage.getData().get("project_id");
        try {
            JSONObject jc = new JSONObject(body);
            //JSONObject jc ;
        if(ProjectWorksActivity.isActivityStarted){
            if((ProjectWorksActivity.PROJECT_ID != null && prj_id.equals(ProjectWorksActivity.PROJECT_ID ))&&((function.equals(function_project) && ProjectWorksActivity.IS_PROJECT) ||(function.equals(function_subproject) && !ProjectWorksActivity.IS_PROJECT))){
                show_notification_only =false;
                item = new WorkCommentItem();
               // jc = jo.getJSONObject()
                item.comment_id = jc.getString("comment_id");
                item.comment_type = jc.getString("comment_type");
                item.seen_list = new ArrayList<WorkSeenItem>();
                item.isDiary = jc.getBoolean("is_diary");
                item.comment = jc.getString("comment");
                item.comment_location = jc.getString("comment");
                item.time =  jc.getString("time");
                ParticipantItem p = new ParticipantItem();
                p.id  =jc.getString("participant_id");
                p.name = jc.getString("participant_name");
                item.participant = p;
                ProjectWorksActivity.populate_data_from_msg(item);
                ProjectWorksActivity.runOnUI(new Runnable() {
                    public void run() {
                        ProjectWorksActivity.works.add(item);
                        ProjectWorksActivity.adapter = new ProjectWorksAdapter(getApplicationContext(),ProjectWorksActivity.works, LoggedUser.id);
                        ProjectWorksActivity.list_works.setAdapter(ProjectWorksActivity.adapter);
                        // Toast.makeText(getApplicationContext(), "from inside thread", Toast.LENGTH_SHORT).show();
                    }
                });

               // ProjectWorksActivity.works.add(item);
                //ProjectWorksActivity.adapter = new ProjectWorksAdapter(getApplicationContext(),ProjectWorksActivity.works, LoggedUser.id);

//                if(item.comment_type.equals(ProjectWorksAdapter.TEXT)){
//
//                }

            }
            else{
                show_notification_only =true;
            }
        }
        else {
            show_notification_only =true;

        }
        if(show_notification_only){
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "");
            mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title).setContentText("By " + jc.getString("participant_name"))
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            notificationManager.notify(FCM_MESSAGE_ID, mBuilder.build());
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
