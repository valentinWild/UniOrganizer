package com.example.uniorganizer.Stundenplan;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.uniorganizer.R;

import java.util.Calendar;


public class MondayActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    TextView textViewDay;
    TextView textViewHintAddLecture;
    TextView textViewHintName;
    TextView textViewHintRoom;
    TextView textViewHintBeginning;
    TextView textViewHintEnd;
    EditText inputLectureName;
    EditText inputRoomNumber;
    EditText inputStartTime;
    EditText inputEndTime;
    Button buttonAddLecture;
    Button buttonBack;
    Button buttonAddDay;
    ListView listViewDay;
    int hourOfDay;
    int minute;
    private boolean start;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monday);


        findViews();
        setupViews();

    }

    private void findViews(){
        textViewDay = (TextView) findViewById(R.id.textView_day);
        textViewHintAddLecture = (TextView) findViewById(R.id.textView_hint_add_lecture);
        textViewHintName = (TextView) findViewById(R.id.textView_hint_name);
        textViewHintRoom = (TextView) findViewById(R.id.textView_hint_room);
        textViewHintBeginning = (TextView) findViewById(R.id.textView_hint_beginning_lecture);
        textViewHintEnd = (TextView) findViewById(R.id.textView_hint_end_lecture);
        inputLectureName = (EditText) findViewById(R.id.editText_lectureName);
        inputRoomNumber = (EditText) findViewById(R.id.editText_room_number);
        inputStartTime = (EditText) findViewById(R.id.editText_start_time);
        inputEndTime = (EditText) findViewById(R.id.editText_end_time);
        buttonAddLecture = (Button) findViewById(R.id.button_add_lecture);
        buttonBack = (Button) findViewById(R.id.button_back);
        buttonAddDay = (Button) findViewById(R.id.button_add_day);
        listViewDay = (ListView) findViewById(R.id.listView_day);
    }
    private void setupViews(){
        initTimeView();
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        buttonAddDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDayToDatabase();
            }
        });
        buttonAddLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readInput();
            }
        });
    }



    private void initTimeView(){
        inputStartTime.setFocusable(false);
        inputStartTime.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v){
                createTimePickerDialogStartTime().show();
            }
        });

        inputEndTime.setFocusable(false);
        inputEndTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                    createTimePickerDialogEndTime().show();
                }
        });
    }


    private TimePickerDialog createTimePickerDialogStartTime(){

        Calendar c = Calendar.getInstance();
        hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        start = true;
        return new TimePickerDialog(this,this,hourOfDay, minute, DateFormat.is24HourFormat(this));
    }
    private TimePickerDialog createTimePickerDialogEndTime(){

        Calendar c = Calendar.getInstance();
        hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        start = false;
        return new TimePickerDialog(this,this,hourOfDay, minute, DateFormat.is24HourFormat(this));
    }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (start){
            inputStartTime.setText(hourOfDay + ":" + minute);
        }else if(!start){
            inputEndTime.setText(hourOfDay + ":" + minute);
        }
    }


    private void addDayToDatabase(){

    }

    private void readInput(){
        // Auslesen des Nutzer Inputs und speichern in lokale Variable

    }



    private void insertEntryIntoDatabase(String lecturename, String roomname, int starttime, int endtime ){
        TimetableElement newEntry = new TimetableElement();
        newEntry.setLectureName(lecturename);
        newEntry.setLectureLocation(roomname);
        newEntry.setBeginn(starttime);
        newEntry.setEnding(endtime);
        //TimetableDatabase.daoAccess().insertOnlyOneElement(newEntry);
    }
}