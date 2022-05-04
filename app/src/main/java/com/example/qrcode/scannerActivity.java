package com.example.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView scannerView;
    final Calendar myCalendar = Calendar.getInstance();  //THis method will add the calender object according to the calender of the device.
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        preferences=getApplicationContext().getSharedPreferences("myPref",0);  //this local data base to few data
        editor=preferences.edit();
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)      //this is the camera permission
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }


    @Override
    public void handleResult(Result rawResult) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());  //here date and time will be  show
        String currentDateandTime = sdf.format(new Date());
           MainActivity.date.setText(currentDateandTime);
//        editor.putString("date",currentDateandTime).commit();// this is for data saving is local storage
        String s=rawResult.getText().toString();
        StringTokenizer tokens = new StringTokenizer(s, "\n");  // this will contain whole data which is scanned by scanner so that the data can be shown in a new line  . (\n) by this we can split data..
        String first = tokens.nextToken();
        String studentid=tokens.nextToken();
        editor.putString("studentid",studentid).commit();
        editor.putString("first",first).commit();
        String second = tokens.nextToken();
        editor.putString("second",second).commit();
        String third = tokens.nextToken();
        editor.putString("third",third).commit();
        String forth = tokens.nextToken();
        editor.putString("fourth",forth).commit();
        String five=tokens.nextToken();
        editor.putString("five",five).commit();
        MainActivity.activity.setTypeface(MainActivity.roboto);
        MainActivity.lecture.setText(third);
        MainActivity.room.setText(forth);
        MainActivity.activity.setText(second);
        MainActivity.timee.setText(five);
        MainActivity.studentidname.setText(studentid);
        MainActivity.name.setText(first); //call static member variable
        editor.putString("name",rawResult.getText()).commit();
        if (MainActivity.name != null) {                 //here if user doesn't scanned Qr code then they will marked absent and if scanned then they will present.
            MainActivity.status.setText("Present");
            MainActivity.presentbtn.setVisibility(View.VISIBLE);
            MainActivity.tick.setVisibility(View.VISIBLE);
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());//This function will allow the scanning depend on time
            String timing=MainActivity.timee.getText().toString();
            if(currentTime.equals(timing)){
                editor.putString("late","On time"); //
            }else {
                editor.putString("late","Late");
            }
         //  MainActivity.date.setText(String.valueOf(currentTime));
           // Log.e("time", String.valueOf(currentTime));
         //   if (!currentTime.equals("9"))
            editor.putString("present","Present").commit();
        } else {
            Toast.makeText(this, "data not available", Toast.LENGTH_SHORT).show();
        }

        onBackPressed();
    }


    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}