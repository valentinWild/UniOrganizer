package com.example.uniorganizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import com.example.uniorganizer.Stundenplan.EditTimetableActivity;
import com.example.uniorganizer.Friendtransaction.FriendsActivity;
import com.example.uniorganizer.Stundenplan.TimetableDataElement;
import com.example.uniorganizer.Stundenplan.TimetableDatabase;
import com.example.uniorganizer.Stundenplan.TimetableEntryItemAdapter;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //Code By: Vincent Eichenseher
    //Konstanten zur Erstellung der Datenbank und des NotificationChannels
    private  static  final String DATABASE_NAME = "Stundenplan";
    private static final String REMINDER_CHANNEL_ID = "reminder_channel";
    private static final String CHANNEL_NAME = "My_reminder_channel";
    private static final String CHANNEL_DESCRIPTION = "channel_for_reminder_notifications";

    private TimetableDatabase timetableDatabase;
    private TimetableEntryItemAdapter adapter;
    private ArrayList<TimetableDataElement> timetable;
    //Ende Code By: Vincent Eichenseher

    private ListView timetableListView;
    private TextView dayTextView;

    protected Button buttonTimetable;
    protected Button buttonFriends;
    protected Button buttonSwitchDayBackward;
    protected Button buttonSwitchDayForward;
    private String weekday;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatabase();
        createNotificationChannel(CHANNEL_NAME,CHANNEL_DESCRIPTION);
        getCurrentDay();


        //Setzen von Referenzen der Objektvariablen auf die definierten Views des Layouts der Acitivity
        buttonTimetable = (Button) findViewById(R.id.button_timetable);
        buttonFriends = (Button) findViewById(R.id.button_friends);
        buttonSwitchDayBackward = (Button) findViewById(R.id.button_switch_day_backward);
        buttonSwitchDayForward = (Button) findViewById(R.id.button_switch_day_forward);
        timetableListView = (ListView) findViewById(R.id.dayList_listView);
        dayTextView = (TextView) findViewById(R.id.textView_day);
        dayTextView.setText(weekday);
        timetable = new ArrayList<>();
        adapter = new TimetableEntryItemAdapter(this, timetable);
        timetableListView.setAdapter(adapter);

        buttonTimetable.setOnClickListener(this);
        buttonFriends.setOnClickListener(this);
        buttonSwitchDayBackward.setOnClickListener(this);
        buttonSwitchDayForward.setOnClickListener(this);




    }

    @Override
    protected void onResume() {
        super.onResume();
        timetable.clear();
        initTimetableList();
        adapter.notifyDataSetChanged();
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "listContent", );
    }*/

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_timetable){
            buttonTimetableClicked();
        }
        else if(v.getId() == R.id.button_friends){
            buttonFriendsClicked();
        }else if(v.getId() == R.id.button_switch_day_backward){
            buttonSwitchDayBackwardClicked();
        }else if(v.getId() == R.id.button_switch_day_forward){
            buttonSwitchDayForwardClicked();
        }
    }
    private void buttonTimetableClicked(){
        //Button ruft EditTimetableActivity über Intent auf - Code by Julian Högerl
        Intent intentTimetable = new Intent(MainActivity.this, EditTimetableActivity.class);
        startActivity(intentTimetable);
    }
    private void buttonFriendsClicked(){
        //Button ruft EditTimetableActivity über Intent auf - Code by Julian Högerl
        Intent intentFriends = new Intent(MainActivity.this, FriendsActivity.class);
        startActivity(intentFriends);
    }
    private void buttonSwitchDayBackwardClicked(){
        //Abfrage welcher Tag auf Montag folgt etc. - Code by Julian Högerl
        switch (weekday){
            case "Monday":
                this.weekday = "Friday";
                break;
            case "Tuesday":
                this.weekday = "Monday";
                break;
            case "Wednesday":
                this.weekday = "Tuesday";
                break;
            case "Thursday":
                this.weekday = "Wednesday";
                break;
            case "Friday":
                this.weekday = "Thursday";
                break;
        }
        //Den dementsprechenden Tag und zugehörige Liste aus der Datenbank anzeigen lassen - Code by Julian Högerl
        dayTextView.setText(weekday);
        timetable.clear();
        initTimetableList();
        adapter.notifyDataSetChanged();
    }
    private void buttonSwitchDayForwardClicked(){
        //Abfrage welcher Tag auf Montag folgt etc. - Code by Julian Högerl
        switch (weekday){
            case "Monday":
                this.weekday = "Tuesday";
                break;
            case "Tuesday":
                this.weekday = "Wednesday";
                break;
            case "Wednesday":
                this.weekday = "Thursday";
                break;
            case "Thursday":
                this.weekday = "Friday";
                break;
            case "Friday":
                this.weekday = "Monday";
                break;
        }
        //Den dementsprechenden Tag und zugehörige Liste aus der Datenbank anzeigen lassen - Code by Julian Högerl
        dayTextView.setText(weekday);
        timetable.clear();
        initTimetableList();
        adapter.notifyDataSetChanged();
    }

    //Code By: Vincent Eichenseher

    //Methode zum erstellen der Datenbank, welche sich durch .fallbackToDestructiveMigration() selbst beendet wenn zu einer anderen Activity gewechselt wird
    private void initDatabase() {
        timetableDatabase = Room.databaseBuilder(this.getApplicationContext(),
                TimetableDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    //gibt eine Liste von TimetableDataElements zurück und informiert den Adapter das sich die anzuzeigenden Daten verändert haben
    private void initTimetableList() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<TimetableDataElement> entrylist = timetableDatabase.daoAccess().findLecturesByWeekday(weekday);
                timetable.addAll(entrylist);


            }

        }).start();
    }

    //erstellen eines notificationChannels, damit die Notifications mit API level 26 und höher compatible sind, da hier die NotficationChannel classe nicht in der Support Library ist
    private void createNotificationChannel (String channel_name, String
            channel_description){
        //if abfrage damit der notification Channel nur auf Geräten mit API level 26+ erstellt wird
        //Quelle:https://developer.android.com/training/notify-user/channels
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CharSequence name = channel_name;
                        String description = channel_description;
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel channel = new NotificationChannel(REMINDER_CHANNEL_ID, name, importance);
                        channel.setDescription(description);
                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);
                    }
    }

    //Methode die den aktuellen Wochentag zurückgibt, damit die MainActivity gleich bei den Einträgen für diesen wochentag startet
    private void getCurrentDay(){
        Calendar calendar = Calendar.getInstance();
        int day= calendar.get(Calendar.DAY_OF_WEEK);
        switch (day){
            // Weekday=Monday bei Samstag und Sonntag, da man sich am Wochenende nur auf dem Kommenden montag einstellt und es dafür keine eigenen Stundenplaneinträge gibt
            case Calendar.SUNDAY:
                this.weekday = "Monday";
                break;
            case Calendar.MONDAY:
                this.weekday = "Monday";
                break;
            case Calendar.TUESDAY:
                this.weekday = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                this.weekday = "Wednesday";
                break;
            case Calendar.THURSDAY:
                this.weekday = "Thursday";
                break;
            case Calendar.FRIDAY:
                this.weekday = "Friday";
                break;
            case Calendar.SATURDAY:
                this.weekday = "Monday";
        }


    }
    //Ende Code By: Vincent Eichenseher








}
