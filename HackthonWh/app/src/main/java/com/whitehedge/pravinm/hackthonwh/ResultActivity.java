package com.whitehedge.pravinm.hackthonwh;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by PravinM on 6/17/2016.
 */
public class ResultActivity extends IntentService {
    public ResultActivity()
    {
        super("pp");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
Log.d("pm","in resultAtivity");
    }
}
