package com.cloudformers.bae;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.Parse;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Michael on 5/22/2015.
 */
public class StopwatchActivity extends AppCompatActivity {
    ToggleButton stopwatchS;
    Button stopB;
    ListView timesLV;
    long timeElapsed;
    String currentTime;
    ArrayAdapter<String> timeAdapter;
    private Chronometer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        timeElapsed = 0;
        stopwatchS = (ToggleButton) findViewById(R.id.stopwatchSwitch);
        stopB = (Button) findViewById(R.id.stopButton);
        stopB.setOnClickListener(new StopButtonListener(this));
        timer = (Chronometer) findViewById(R.id.chronometer);
        timer.setOnChronometerTickListener(new OnTickListener());
        timer.setText("00:00:00");
        timesLV = (ListView) findViewById(R.id.timesList);
        ParseHelper.resetCounter();
        List<String> timeList = ParseHelper.getTimes();
        timeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, timeList);
        timesLV.setAdapter(timeAdapter);
        registerForContextMenu(timesLV);
    }
    public void onToggleClicked(View view) {
        boolean on = ((ToggleButton) view).isChecked();
        if (on) {
            timer.setBase(SystemClock.elapsedRealtime() - timeElapsed);
            timer.start();
        } else {
            timer.stop();
        }
    }
    private class OnTickListener implements Chronometer.OnChronometerTickListener {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            long time = SystemClock.elapsedRealtime() - chronometer.getBase();
            int h   = (int)(time /3600000);
            int m = (int)(time - h*3600000)/60000;
            int s= (int)(time - h*3600000- m*60000)/1000 ;
            currentTime = String.format("%02d:%02d:%02d", h, m, s);
            chronometer.setText(currentTime);
            timeElapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
        }
    }
    @Override
    public void onCreateContextMenu(android.view.ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.timesList) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.add("Delete Time");
        }
    }
    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ParseHelper.deleteTime(info.position);
        ParseHelper.resetCounter();
        List<String> timeList = ParseHelper.getTimes();
        timeAdapter.clear();
        timeAdapter.addAll(timeList);
        return true;
    }
    private class StopButtonListener implements View.OnClickListener {
        StopwatchActivity context;
        public StopButtonListener(StopwatchActivity context){
            this.context = context;
        }
        @Override
        public void onClick(View v) {
            if(timeElapsed > 0) {
                ParseObject time = new ParseObject(ParseHelper.timesObj);
                time.put(ParseHelper.userID, ParseHelper.getCurrentId().toString());
                time.put(ParseHelper.time, currentTime);
                time.saveInBackground();
                timeElapsed = 0;
            }
            if(timer.getText() != "00:00:00" && currentTime != null) {
                timeAdapter.add("Time " + ParseHelper.incrementCounter() + ": " + currentTime);
            }
            timer.stop();
            timer.setText("00:00:00");
            stopwatchS.setChecked(false);
        }
    }
}
