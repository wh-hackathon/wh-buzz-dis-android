package com.whitehedge.pravinm.hackthonwh;

import android.app.Activity;
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

import org.json.JSONArray;
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
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class DiscBuzzLanding extends Activity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView lv;
    Context context;

    ArrayList prgmName;
    public static int[] prgmImages = {R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera};
    public static String[] prgmNameList = {"jayhind selection, Pune", "Mcdonalds, kothrud, pune", "jayhind selection, Aundh", "Ramesh dying, Pune", "Park Avenue, Kothrud, Pune", "KFC ,Deccan, Pune", "CityPride multiplex, Kothrud, Pune", "Dominos pizza, Bavdhan, Pune"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_disc_buzz_landing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        context = this;

        try {
            new PostClass(getApplicationContext(), this).execute("pm");

            lv = (ListView) findViewById(R.id.listView);
            //  lv.setOnItemClickListener(onItemClickListener);

            //   lv.setAdapter(new DealsAdapater(this, prgmNameList, prgmImages,));

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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

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
        private final DiscBuzzLanding activity;

        public PostClass(Context c, DiscBuzzLanding act) {
            this.context = c;
            activity = act;
        }

        protected void onPreExecute() {
            /*progress= new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        */
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                // Thread.sleep(1000);
                Log.d("pm", "enter in async" + params[0]);

                // final TextView outputView = (TextView) findViewById(R.id.showOutput);
                URL url = new URL("https://buzz-dis.herokuapp.com/tags/get-all-by-location-open");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(500000);
                connection.setRequestProperty("Content-Type", "application/json");
                ContentValues params1 = new ContentValues();
                params1.put("latitude", "18.523331");
                params1.put("longitude", "73.7775214");
                params1.put("distance", "2");
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");

                String s = "{" +
                        "  \"latitude\": 18.523331, \n" +
                        "  \"longitude\": 73.7775214, \n" +
                        "  \"distance\": 20\n" +
                        "}";
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(s);
                dStream.flush();
                dStream.close();

                int responseCode = connection.getResponseCode();

                // Log.d("pm",getString(responseCode));
                final StringBuilder output = new StringBuilder("Request URL " + url);
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                final StringBuilder responseOutput = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                Log.d("pm", responseOutput.toString());

                DiscBuzzLanding.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Log.d("op", output.toString());
                        String a = responseOutput.toString();
                        try {
                            JSONArray TotalArray = new JSONArray(a);

                            //   JSONArray jsonArray = jsonRootObject.getJSONArray("Value");

                            List<String> cordinateList = new ArrayList<String>();
                            List<String> shopDetails = new ArrayList<String>();
                            List<String> discountDetails = new ArrayList<String>();
                            List<String> LikeList = new ArrayList<String>();
                            List<String> DiscIdlist = new ArrayList<String>();
                            for (int i = 0; i < TotalArray.length(); i++) {
                                JSONObject jsonObject = TotalArray.getJSONObject(i);
                                // JSONObject jsonObject1= jsonObject.getJSONObject("");
                                String DiscId = jsonObject.optString("_id").toString();
                                DiscIdlist.add(DiscId);
                                JSONObject info = jsonObject.getJSONObject("info");
                                JSONObject location = jsonObject.getJSONObject("location");
                                String ShopInfo = info.optString("shopName").toString() + " ," + info.optString("shopAddress").toString();
                                String disc = info.optString("discountBanner").toString();
                                String Like = info.optString("likes").toString();

                                JSONArray Locdisc = location.getJSONArray("coordinates");

                                String locValue = Locdisc.get(1) + "," + Locdisc.get(0);
                                cordinateList.add(locValue);
                                LikeList.add(Like);
                                // push in  array
                                shopDetails.add(ShopInfo);
                                discountDetails.add(disc);
                            }

                            prgmNameList = shopDetails.toArray(new String[0]);
                            String[] locArray = cordinateList.toArray(new String[0]);
                            String[] discArray = discountDetails.toArray(new String[0]);
                            String[] likeArray = LikeList.toArray(new String[0]);
String[] discIdArray=DiscIdlist.toArray(new String[0]);
                            lv.setAdapter(new DealsAdapater(activity, prgmNameList, prgmImages, discArray, locArray, likeArray,discIdArray));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                Log.d("pm", "err");
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("pm", "err");
            }
            return null;
        }


    }
}
