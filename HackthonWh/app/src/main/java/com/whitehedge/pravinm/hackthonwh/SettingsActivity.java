package com.whitehedge.pravinm.hackthonwh;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SettingsActivity extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    private GoogleApiClient mGoogleApiClient;
    private List<Geofence> mGeofenceList = new ArrayList<Geofence>();
    private PendingIntent mGeofencePendingIntent;
    public static final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settings = getSharedPreferences(Constants.APP_NAME, 0);
        editor = settings.edit();
        this.layoutSettings();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Landing Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.whitehedge.pravinm.hackthonwh/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    protected void layoutSettings() {
        String selectedRegions = settings.getString(Constants.SETTINGS_REGIONS_STR, "");
        String selectedCategories = settings.getString(Constants.SETTINGS_CATEGORIES_STR, "");
        Log.d("settings", "SETTING_REGIONS_STR = " + selectedRegions);
        Log.d("settings", "SETTING_CATEGORIES_STR = " + selectedCategories);
        LinearLayout layout = (LinearLayout) findViewById(R.id.regions);
        TextView regionsText = new TextView(this);
        regionsText.setText("Select Regions -");
        regionsText.setTypeface(null, Typeface.BOLD);
        regionsText.setTextSize(18);

        regionsText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(regionsText);

        int i = 0;
        for (Map.Entry<String, TreeMap<Float, Float>> entry : Constants.regions.entrySet()) {
            CheckBox chkBox = new CheckBox(this);
            if (selectedRegions.contains(entry.getKey()))
                chkBox.setChecked(true);

            chkBox.setText(entry.getKey());
            chkBox.setId(Constants.REGIONS_CHECKBOX_START_INDEX + i++);
            chkBox.setTextColor(Color.BLACK);
            layout.addView(chkBox);
        }

        TextView nline = new TextView(this);
        nline.setText(Html.fromHtml("<br>")); //i also tried:  nline.setText("\n");
        layout.addView(nline);

        TextView catText = new TextView(this);
        catText.setText("Select Discount Categories -");
        catText.setTypeface(null, Typeface.BOLD);
        catText.setTextSize(18);

        catText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(catText);

        i = 0;
        for (String s: Constants.disCategories) {
            CheckBox chkBox = new CheckBox(this);
            if (selectedCategories.contains(s))
                chkBox.setChecked(true);

            chkBox.setText(s);
            chkBox.setId(Constants.DISCOUNT_CATEGORIES_CHECKBOX_START_INDEX + i++);
            chkBox.setTextColor(Color.BLACK);
            layout.addView(chkBox);
        }

        TextView nline1 = new TextView(this);
        nline1.setText(Html.fromHtml("<br>")); //i also tried:  nline.setText("\n");
        layout.addView(nline1);

        Button btn1 = new Button(this);
        btn1.setText("Save");
        btn1.setOnClickListener(this);
        layout.addView(btn1);
    }

    @Override
    public void onClick(View v) {
        Toast toast = null;
        TreeMap<Float, Float> tmp;
        StringBuilder regionStr = new StringBuilder();
        StringBuilder categoriesStr = new StringBuilder();

        removeGeofences();
        for (int i = 0; i < Constants.regions.size(); i++) {
            CheckBox ch = (CheckBox) findViewById(Constants.REGIONS_CHECKBOX_START_INDEX + i);
            toast = Toast.makeText(getApplicationContext(), "Checkbox " + ch.getText() + " pressed = " + ch.isChecked(), Toast.LENGTH_LONG);
            toast.show();
            if (ch.isChecked()) {
                tmp = Constants.regions.get(ch.getText());
                addToGeofenceList(ch.getText().toString(), tmp.firstKey(), tmp.get(tmp.firstKey()));
                if (regionStr.toString().isEmpty()) {
                    regionStr = regionStr.append(ch.getText());
                }
                else {
                    regionStr.append("*"+ch.getText());
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.GeofencingApi.addGeofences(
                mGoogleApiClient,
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(this);

        for (int i = 0; i < Constants.disCategories.size(); i++) {
            CheckBox ch = (CheckBox) findViewById(Constants.DISCOUNT_CATEGORIES_CHECKBOX_START_INDEX + i);
            toast = Toast.makeText(getApplicationContext(), "Checkbox " + ch.getText() + " pressed = " + ch.isChecked(), Toast.LENGTH_LONG);
            toast.show();
            if (ch.isChecked()) {
                if (categoriesStr.toString().isEmpty()) {
                    categoriesStr = categoriesStr.append(ch.getText());
                }
                else {
                    categoriesStr.append("*"+ch.getText());
                }
            }
        }
        editor.putString(Constants.SETTINGS_REGIONS_STR, regionStr.toString());
        editor.putString(Constants.SETTINGS_CATEGORIES_STR, categoriesStr.toString());
        editor.commit();
        Log.d("settings", "Added Geofences");
        Intent discScreen = new Intent(getBaseContext(), DiscBuzzLanding.class);
        this.startActivity(discScreen);
    }

    private GeofencingRequest getGeofencingRequest() {
        try {
            GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
            builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
            builder.addGeofences(mGeofenceList);
            return builder.build();
        } catch (Exception x) {
            Log.d("settings", x.toString());
            return null;
        }
    }

    private PendingIntent getGeofencePendingIntent() {

        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);

        PendingIntent x = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return x;
    }

    protected void removeGeofences() {
        LocationServices.GeofencingApi.removeGeofences(
                mGoogleApiClient,
                // This is the same pending intent that was used in addGeofences().
                getGeofencePendingIntent()
        ).setResultCallback(this); // Result processed in onResult().

        Log.d("settings", "Geofences Deleted");
    }

    public void addToGeofenceList(String region, Float lat, Float lang) {
        try {
            mGeofenceList.add(new Geofence.Builder().setRequestId(region)
                    .setCircularRegion(
                            lat, lang,
                            Constants.GEOFENCE_RADIUS_KM * 1000
                    )
                    .setExpirationDuration(99999999)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        } catch (Exception x) { //Log.d()
            Log.d("settings", x.toString());
            Log.d("settings", x.getMessage());

        }
        Log.d("settings", "Added entry key = " + region + " lat = " + lat + " lang = " + lang);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("settings", "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("settings", "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("settings", "onConnectionFailed");
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.i("settings", "onResult");
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction2 = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Settings Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.whitehedge.pravinm.hackthonwh/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction2);
        mGoogleApiClient.disconnect();
        Log.i("settings", "onStop");
    }
}