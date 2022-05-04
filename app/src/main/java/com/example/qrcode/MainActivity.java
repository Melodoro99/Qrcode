package com.example.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    Button scanbtn;
    EditText ed1;
    ImageView iv;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    FirebaseDatabase db;
  public static  LinearLayout presentbtn;
  public static ImageView tick;
    public static Typeface roboto;
    String datee,present,namee,strActivity,strLecturer,strRoom,late,strStudentidname,time;
    public static TextView name, status, date,activity,room,lecture,studentidname,timee; //static member variable so that I will access it on another code also
    DatabaseReference teachersDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences=getApplicationContext().getSharedPreferences("myPref",0);
        editor=preferences.edit();
        setContentView(R.layout.activity_main);
        scanbtn = (Button) findViewById(R.id.scanbtn);
        name = findViewById(R.id.name);
        tick=findViewById(R.id.tick);
        room=findViewById(R.id.room);
        timee=findViewById(R.id.time);
        studentidname=findViewById(R.id.studentidname);
        lecture=findViewById(R.id.lecture);
        presentbtn=findViewById(R.id.ln5);
        status = findViewById(R.id.status);
        activity=findViewById(R.id.activity);
        date = findViewById(R.id.date);
        db = FirebaseDatabase.getInstance();

        teachersDetails=db.getReference().child("TeachersDetails");
        roboto=Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Roboto-Regular.ttf");

          // this is firebase
        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), scannerActivity.class)); //this is intent that is intent use for going one activity to another activity after for scanning


            }
        });

    }


    public  void present(View view) {                          //Present button if i click on this button then with name date and present status it will save on firebase.
        datee=preferences.getString("date","");
        namee=preferences.getString("first","");
        strActivity=preferences.getString("second","");
        strStudentidname=preferences.getString("studentid","");
        strLecturer=preferences.getString("third","");
        strRoom=preferences.getString("fourth","");
        present=preferences.getString("present","");
        late=preferences.getString("late","");
        time=preferences.getString("five","");
        HashMap<String, String> myStudent = new HashMap<>();
        myStudent.put("Name", namee);
        myStudent.put("Student Id",strStudentidname);
        myStudent.put("Status", present);
        myStudent.put("Date and Time", datee);
        myStudent.put("Timing Status", late);
        myStudent.put("Activity",strActivity);
        myStudent.put("Lecturer",strLecturer);
        myStudent.put("Class Time",time);
        myStudent.put("Room",strRoom);
        teachersDetails.push().setValue(myStudent);

        name.setText("");
        status.setText("");
        studentidname.setText("");
        date.setText("");
        activity.setText("");
        lecture.setText("");
        room.setText("");
        presentbtn.setVisibility(View.GONE);
        tick.setVisibility(View.GONE);
        Toast.makeText(this, "Attendance successfully taken", Toast.LENGTH_SHORT).show();

         //this is toast to show text for few second
    }

    public void block(View view) {
        Toast.makeText(this, "block", Toast.LENGTH_SHORT).show();
    }
}