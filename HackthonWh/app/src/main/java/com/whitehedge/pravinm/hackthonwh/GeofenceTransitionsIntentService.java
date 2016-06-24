package com.whitehedge.pravinm.hackthonwh;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

/**
 * Created by PravinM on 6/14/2016.
 */
public class GeofenceTransitionsIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GeofenceTransitionsIntentService(String name) {
        super(name);
    }
    public GeofenceTransitionsIntentService() {
        super("hack");
    }


        @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("tag","in event");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
       /* if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }*/

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            Log.d("tag","entered in event");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
// Creates an Intent for the Activity
            Intent notifyIntent =
                    new Intent(this, ResultActivity.class);
// Sets the Activity to start in a new, empty task
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
// Creates the PendingIntent
            PendingIntent notifyPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            notifyIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

// Puts the PendingIntent into the notification builder
            builder.setContentIntent(notifyPendingIntent);
// Notifications are issued by sending them to the
// NotificationManager system service.
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// Builds an anonymous Notification object from the builder, and
// passes it to the NotificationManager
            mNotificationManager.notify(5, builder.build());

        }else
            if((geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) )
            {

                Log.d("tag","exit in event");                // exit event
            }

        }
}
