package com.whitehedge.pravinm.hackthonwh;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class DiscBuzzLanding extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView lv;
    Context context;

    ArrayList prgmName;
    public static int[] prgmImages = {R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera};
    public static String[] prgmNameList = {"jayhind selection, Pune", "Mcdonalds, kothrud, pune", "jayhind selection, Aundh", "Ramesh dying, Pune", "Park Avenue, Kothrud, Pune", "KFC ,Deccan, Pune", "CityPride multiplex, Kothrud, Pune", "Dominos pizza, Bavdhan, Pune", "JavaScript"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disc_buzz_landing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        lv = (ListView) findViewById(R.id.listView);
        //  lv.setOnItemClickListener(onItemClickListener);

        lv.setAdapter(new DealsAdapater(this, prgmNameList, prgmImages));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }


    public void FetchData(View view) {
        try {

            new PostClass(getApplicationContext()).execute("pm");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    private String getPostDataString(ContentValues params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> entry : params.valueSet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
        }

        return result.toString();
    }

    public void Like(View view) {
        ImageButton likebtn = (ImageButton) findViewById(R.id.btnLike);
        likebtn.setImageResource(R.drawable.red_heart);
        Toast toast = Toast.makeText(context, "test Like ", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void Navigate(View view) {
        Toast toast = Toast.makeText(context, "test navigate ", Toast.LENGTH_SHORT);
        toast.show();

        Uri gmmIntentUri = Uri.parse("google.navigation:q=18.5074,73.8077&mode=d");
        //            String s =result[position].replace(" ","+");
        //              Uri gmmIntentUri = Uri.parse("google.navigation:q="+s+"&mode=d");
        // Uri gmmIntentUri = Uri.parse("google.navigation:q=Jayhind+Selection,+Pune,+Maharashtra &mode=d");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.disc_buzz_landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private class PostClass extends AsyncTask<String, Void, Void> {

        private final Context context;

        public PostClass(Context c){
            this.context = c;
        }

        protected void onPreExecute(){
            /*progress= new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        */}

        @Override
        protected Void doInBackground(String... params) {

            try {
               // Thread.sleep(1000);
                Log.d("pm","enter in async"+ params[0]);

                // final TextView outputView = (TextView) findViewById(R.id.showOutput);
                URL url = new URL("https://buzz-dis.herokuapp.com/tags/get-all-by-location-open");

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                connection.setRequestProperty("Content-Type","application/json");
                ContentValues params1 = new ContentValues();
                params1.put("latitude", "18.523331");
                params1.put("longitude", "73.7775214");
                params1.put("distance", "2");

                connection.setRequestMethod("POST");
              //   connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
              //  connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(getPostDataString(params1));
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();

                final StringBuilder output = new StringBuilder("Request URL " + url);
           //     output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator")  + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();
                Log.d("pm",responseOutput.toString());
                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                DiscBuzzLanding.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Log.d("op",output.toString());
                        //outputView.setText(output);
                        //progress.dismiss();
                    }
                });

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                Log.d("pm","err");
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("pm","err");
            }
            return null;
        }
        private String getPostDataString(ContentValues params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, Object> entry : params.valueSet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
            }

            return result.toString();
        }


    }
}
