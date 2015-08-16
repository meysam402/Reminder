package com.meysam.reminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Hashtable;


public class Main extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] days = new String[]{"شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنج شنبه", "جمعه"};
        final boolean[] daysBinary = new boolean[]{false, false, false, false, false, false, false};
        final Hashtable choices = new Hashtable();
        for(int i=0; i<7; i++)
            choices.put(days[i], false);

        final ListView listView = (ListView) findViewById(R.id.listOfDays);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Main.this,
                android.R.layout.select_dialog_multichoice, android.R.id.text1, days);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String itemValue = (String) listView.getItemAtPosition(position);
                for(int i=0; i<7; i++)
                    if(days[i].equals(itemValue))
                        daysBinary[i] = !daysBinary[i];
                //choices.put(itemValue, !(boolean) choices.get(itemValue));
            }

        });

        Button done = (Button) findViewById(R.id.btnDone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Set<String> keys = choices.keySet();
                int length = 0;
                for (String key : keys) {
                    if ((boolean) choices.get(key))
                        length++;
                }

                String[] finalChoices = new String[length];
                int index = 0;
                for (String key : keys) {
                    if ((boolean) choices.get(key))
                        finalChoices[index++] = key;
                }
                */
                //chosenDays = finalChoices;
                stopAlarm();
                writeToFile(daysBinary);
                activateAlarm();
                Toast.makeText(Main.this, "ثبت شد", Toast.LENGTH_SHORT).show();
                //activateAlarm();
                //Intent intent = new Intent(Main.this, AlarmService.class);
                //startService(intent);
            }
        });

        activateAlarm();
    }

    private void activateAlarm(){
        Intent intent = new Intent(Main.this, AlarmService.class);
        PendingIntent pending = PendingIntent.getService(Main.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);
        //calendar.set(Calendar.SECOND, 1);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pending);
    }

    private void stopAlarm(){
        stopService(new Intent(this, AlarmService.class));
    }

    private void writeToFile(boolean[] days){
        String content="0000000";
        for(int i=0; i<days.length; i++)
            if(days[i])
                content = content.substring(0,i) + '1' + content.substring(i+1);
        try {
            FileOutputStream fOut = openFileOutput("days.txt", MODE_PRIVATE);//In memory (Assets folder is read-only!)
            fOut.write(content.getBytes());
            fOut.close();
        }
        catch (IOException e) {
            Toast.makeText(this, "Error: file corrupted!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
