package in.mobile.connectree.collegefeed;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;


public class TimeTableFragment extends Fragment {
	
	public TimeTableFragment(){}
	
	View rootView;
	ArrayAdapter<CharSequence> branchAdapter;
    private  JSONObject jz;
    int yearPosition;
    Spinner branchSpinner;
    Spinner yearSpinner;
    int startFlag = 0;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		rootView = inflater.inflate(R.layout.fragment_time_table, container, false);

        setHasOptionsMenu(true);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int branchPosition = sharedPref.getInt("branch", 0);
        yearPosition = sharedPref.getInt("year", 0);

        //Batch Spinner
		branchSpinner = (Spinner)rootView.findViewById(R.id.branch);
        branchAdapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.branch_items, R.layout.spinner_item);
        branchAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        branchSpinner.setAdapter(branchAdapter);
        branchSpinner.setSelection(branchPosition);

        yearSpinner = (Spinner) rootView.findViewById(R.id.year);
        if (branchPosition == 0 || branchPosition == 1 || branchPosition == 2) {
            ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.btech_year_items, R.layout.spinner_item);
            yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            yearSpinner.setAdapter(yearAdapter);
        } else {

            ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.mtech_year_items, R.layout.spinner_item);
            yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            yearSpinner.setAdapter(yearAdapter);
        }

        try {
            yearSpinner.setSelection(yearPosition);
        } catch (Exception e) {
            yearSpinner.setSelection(0);
        }

        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(startFlag==1) {
                    if (position == 0 || position == 1 || position == 2) {
                        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.btech_year_items, R.layout.spinner_item);
                        yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        yearSpinner.setAdapter(yearAdapter);
                    } else {

                        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.mtech_year_items, R.layout.spinner_item);
                        yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        yearSpinner.setAdapter(yearAdapter);
                    }
                }
                setTable(branchSpinner.getSelectedItemPosition(),yearSpinner.getSelectedItemPosition());
                startFlag=1;
                saveBatch(branchSpinner.getSelectedItemPosition(),yearSpinner.getSelectedItemPosition());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setTable(branchSpinner.getSelectedItemPosition(),yearSpinner.getSelectedItemPosition());
                saveBatch(branchSpinner.getSelectedItemPosition(),yearSpinner.getSelectedItemPosition());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return rootView;
    }
    private String selectFilename(int x, int y){
        if(x==0 && y==0)
            return "b1cse";
        if(x==0 && y==1)
            return "b2cse";
        if(x==0 && y==2)
            return "b3cse";
        if(x==0 && y==3)
            return "b4cse";
        if(x==1 && y==0)
            return "b1eee";
        if(x==1 && y==1)
            return "b2eee";
        if(x==1 && y==2)
            return "b3eee";
        if(x==1 && y==3)
            return "b4eee";
        if(x==2 && y==0)
            return "b1ece";
        if(x==2 && y==1)
            return "b2ece";
        if(x==2 && y==2)
            return "b3ece";
        if(x==2 && y==3)
            return "b4ece";
        if(x==3 && y==0)
            return "m1cse";
        if(x==3 && y==1)
            return "m2cse";
        if(x==4 && y==0)
            return "m1eee";
        if(x==4 && y==1)
            return "m2eee";
        if(x==5 && y==0)
            return "m1ece";
        if(x==5 && y==1)
            return "m2ece";
        else
            return "";
    }

    private void saveBatch(int x, int y){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("branch", x);
        editor.putInt("year", y);
        editor.commit();
    }

    private void makeRequest(final int x,final int y){

        RequestQueue queue = Volley.newRequestQueue(rootView.getContext());
        final String filename = selectFilename(x,y);
        String url = "http://nitg-app.appspot.com/text/"+filename+".txt";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, (JSONObject)null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        String string = response.toString();
                        FileOutputStream outputStream;


                        try {
                            outputStream = rootView.getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                            outputStream.write(string.getBytes());
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        setTable(x,y);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(rootView.getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                    }
                });

        jsObjRequest.setTag("TableRequest");
        queue.add(jsObjRequest);
    }

    private void setTable(int x, int y) {
        String filename = selectFilename(x, y);
        FileInputStream inputStream;
        byte[] buffer = null;
        try {
            inputStream = rootView.getContext().openFileInput(filename);
            buffer = new byte[300];
            inputStream.read(buffer, 0, 299);
            //jz = new JSONObject(buffer.toString());
        } catch (Exception e) {
            Toast.makeText(rootView.getContext(), "Data Missing, Click Refresh.", Toast.LENGTH_SHORT).show();

            TableRow tr[] = new TableRow[7];

            tr[0] = (TableRow) rootView.findViewById(R.id.tableRow2);
            tr[1] = (TableRow) rootView.findViewById(R.id.tableRow3);
            tr[2] = (TableRow) rootView.findViewById(R.id.tableRow4);
            tr[3] = (TableRow) rootView.findViewById(R.id.tableRow5);
            tr[4] = (TableRow) rootView.findViewById(R.id.tableRow6);
            tr[5] = (TableRow) rootView.findViewById(R.id.tableRow7);
            tr[6] = (TableRow) rootView.findViewById(R.id.tableRow8);

            for (int i = 0; i < 7; i++)
                for (int j = 1; j < tr[i].getChildCount(); j++) {
                    ((TextView) tr[i].getChildAt(j)).setText("");
                }
            return;
        }
        try {
            JSONObject readContent = new JSONObject(new String(buffer));
            JSONArray ja = readContent.getJSONArray("timetable");

            JSONArray jaa[] = new JSONArray[5];
            JSONObject jo[] = new JSONObject[5];

            jo[0] = ja.getJSONObject(0);
            jaa[0] = jo[0].getJSONArray("monday");

            jo[1] = ja.getJSONObject(1);
            jaa[1] = jo[1].getJSONArray("tuesday");

            jo[2] = ja.getJSONObject(2);
            jaa[2] = jo[2].getJSONArray("wednesday");

            jo[3] = ja.getJSONObject(3);
            jaa[3] = jo[3].getJSONArray("thursday");

            jo[4] = ja.getJSONObject(4);
            jaa[4] = jo[4].getJSONArray("friday");

            //Sample Cell in Table
            TableRow tr[] = new TableRow[7];

            tr[0] = (TableRow) rootView.findViewById(R.id.tableRow2);
            tr[1] = (TableRow) rootView.findViewById(R.id.tableRow3);
            tr[2] = (TableRow) rootView.findViewById(R.id.tableRow4);
            tr[3] = (TableRow) rootView.findViewById(R.id.tableRow5);
            tr[4] = (TableRow) rootView.findViewById(R.id.tableRow6);
            tr[5] = (TableRow) rootView.findViewById(R.id.tableRow7);
            tr[6] = (TableRow) rootView.findViewById(R.id.tableRow8);

            for (int i = 0; i < 7; i++)
                for (int j = 1; j < tr[i].getChildCount(); j++) {
                    ((TextView) tr[i].getChildAt(j)).setText(jaa[j - 1].getString(i));
                }
            //

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                makeRequest(branchSpinner.getSelectedItemPosition(),yearSpinner.getSelectedItemPosition());
                break;
            case R.id.action_remind:
                boolean rem;
                if (item.isChecked()){
                    item.setChecked(false);
                    cancelSchedule();
                    rem = false;
                }
                else {
                    startSchedule();
                    item.setChecked(true);
                    rem = true;
                }
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("reminder", rem);
                editor.commit();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void startSchedule() {

        try {

            Intent intent = new Intent(rootView.getContext().getApplicationContext(), AlaramReceiver.class);
            intent.putExtra(AlaramReceiver.ACTION_ALARM, AlaramReceiver.ACTION_ALARM);

            final PendingIntent pIntent = PendingIntent.getBroadcast(rootView.getContext(), 1234567, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 22);
            calendar.set(Calendar.MINUTE, 15);


            AlarmManager alarms = (AlarmManager) rootView.getContext().getSystemService(Context.ALARM_SERVICE);
            alarms.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),60000, pIntent);

            Toast.makeText(rootView.getContext(),"Reminder Service Started",Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void cancelSchedule() {

        Intent intent = new Intent(rootView.getContext().getApplicationContext(),
                AlaramReceiver.class);
        intent.putExtra(AlaramReceiver.ACTION_ALARM,
                AlaramReceiver.ACTION_ALARM);

        final PendingIntent pIntent = PendingIntent.getBroadcast(rootView.getContext(), 1234567,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarms = (AlarmManager) rootView.getContext().getSystemService(Context.ALARM_SERVICE);

        alarms.cancel(pIntent);
        Toast.makeText(rootView.getContext(),"Cancelled",Toast.LENGTH_SHORT).show();
    }

}

