package in.mobile.connectree.collegefeed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by vidit on 28/12/14.
 */
public class AlaramReceiver extends BroadcastReceiver{

    public static String ACTION_ALARM = "in.mobile.connectree.collegefeed";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        String action = bundle.getString(ACTION_ALARM);
        if (action.equals(ACTION_ALARM)) {
            Intent inService = new Intent(context, ReminderService.class);
            context.startService(inService);
        } else {
            //Log.i("Alarm Receiver", "Else loop");
            Toast.makeText(context, "Else loop", Toast.LENGTH_SHORT).show();
        }
    }
}
