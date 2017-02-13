package moco.schleppo;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseObject;
import com.parse.ParseQuery;

public class DeleteWarningIntentService extends IntentService {

    public static final String EXTRA_PARAM1 = "moco.schleppo.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "moco.schleppo.extra.PARAM2";

    public final int timeTillWarningDeleted = 120;

    public DeleteWarningIntentService() {
        super("DeleteWarningIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Bundle extras = intent.getExtras();
            String warningObjectId = extras.getString(EXTRA_PARAM1);

            ParseQuery query = ParseQuery.getQuery("WarningDriver");
            ParseObject warning = null;

            try{
                warning = query.get(warningObjectId);
            } catch (Exception e) {
                Log.d("DeleteWarningGetQuery", e.getMessage());
            }
            if(warning!=null) {
                try {
                    int millisecondsTillDelete = timeTillWarningDeleted * 60000;
                    Thread.sleep(millisecondsTillDelete);
                } catch (Exception e) {
                    Log.d("DeleteWarningThread", e.getMessage());
                }
                warning.deleteInBackground();
            }
            stopSelf();
        }
    }
}
