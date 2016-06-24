package com.whitehedge.pravinm.hackthonwh;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PravinM on 6/23/2016.
 */

public class DealsAdapater extends BaseAdapter {
    String[] result;
    String[] _discArray;
    String[] _locArray;
    String[] _likeArray;
    String[] _discIdArray;
    Context context;
    int[] imageId;
    private static LayoutInflater inflater = null;

    public DealsAdapater(DiscBuzzLanding mainActivity, String[] prgmNameList, int[] prgmImages, String[] discArray, String[] locArray, String[] likeArray,String[] discIdArray) {

        result = prgmNameList;
        _discIdArray=discIdArray;
        _locArray = locArray;
        _discArray = discArray;
        _likeArray = likeArray;
        context = mainActivity;
        imageId = prgmImages;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView tv;
        TextView txtBanner;

        ImageView img;
        TextView txtLatLong;
        TextView txtLikeCount;
        ImageButton btnNav;
        ImageButton btnLike;
        TextView txtID;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.dealslist, null);
        holder.tv = (TextView) rowView.findViewById(R.id.txtShopName);
        holder.txtBanner = (TextView) rowView.findViewById(R.id.txtBanner);
        holder.txtLatLong = (TextView) rowView.findViewById(R.id.txtLatLong);
        holder.txtLikeCount = (TextView) rowView.findViewById(R.id.txtLikeCount);
        holder.txtID = (TextView) rowView.findViewById(R.id.txtDiscId);

        holder.btnLike = (ImageButton) rowView.findViewById(R.id.btnLike);
        holder.btnLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ///check if alreay liked
                // if(String.valueOf(holder.btnLike.getTag())== )
                new PostlikeClass(holder.txtID.getText().toString()).execute("like");
                holder.btnLike.setImageResource(R.drawable.red_heart);
            }
        });

        holder.btnNav = (ImageButton) rowView.findViewById(R.id.btnNavigate);
        holder.btnNav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(context, holder.txtLatLong.getText(), Toast.LENGTH_SHORT);
                toast.show();

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + holder.txtLatLong.getText() + "&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });

        holder.txtID.setText(_discIdArray[position]);
        holder.txtLikeCount.setText(_likeArray[position]);
        holder.txtBanner.setText(_discArray[position]);
        holder.tv.setText(result[position]);
        holder.txtLatLong.setText(_locArray[position]);
        //holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        // holder.img.setImageResource(imageId[position]);

        return rowView;
    }

    private class PostlikeClass extends AsyncTask<String, Void, Void> {

        //  private final Context context;
        private final String strId;

        public PostlikeClass(String a) {
            //this.context = c;
            strId = a;
            Log.d("strid",strId);
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

                // final TextView outputView = (TextView) findViewById(R.id.showOutput);
                URL url = new URL("https://buzz-dis.herokuapp.com/tags/update-likes");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(500000);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");

                String s = "{\n" +
                        "   “id”: “" + strId + "”\n" +
                        "}";
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(s);
                dStream.flush();
                dStream.close();

                int responseCode = connection.getResponseCode();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                final StringBuilder responseOutput = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

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