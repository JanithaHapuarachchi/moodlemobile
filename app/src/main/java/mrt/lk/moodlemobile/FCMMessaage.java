package mrt.lk.moodlemobile;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


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
//
//    public FCMMessaage() {
//        super("FCMMessaage");
//    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("Moodle",remoteMessage.getData().toString());
    }
}
