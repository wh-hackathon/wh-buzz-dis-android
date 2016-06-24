package com.whitehedge.pravinm.hackthonwh;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
            Toast toast = Toast.makeText(getApplicationContext(), "You got entered in range",Toast.LENGTH_LONG );
            toast.show();
            Log.d("tag","entered in event");
          NotiFy();

        }else
            if((geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) )
            {

                Log.d("tag","exit in event");                // exit event
            }

        }

    private void NotiFy() {
        Intent intent = new Intent(this, DiscBuzzLanding.class);
        // ListView l=   (ListView) findViewById(R.id.listView);
        final int pendingIntentId = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, pendingIntentId, intent, PendingIntent.FLAG_ONE_SHOT);

        // Instantiate the builder and set notification elements:
        Notification.Builder builder = new Notification.Builder (this)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle ("Discount buzz")
                .setDefaults (Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentText ("You haven't yet checked these fabulous discounts. Check out now..")
                .setSmallIcon (R.drawable.ic_menu_camera);

        // Build the notification:
        Notification notification = builder.build();

        // Get the notification manager:
        NotificationManager notificationManager = ( NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        // Publish the notification:
        final int notificationId = 0;
        notificationManager.notify(notificationId, notification);
    }
}
