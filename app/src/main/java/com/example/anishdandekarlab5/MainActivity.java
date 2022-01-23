package com.example.anishdandekarlab5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.Cursor;
import android.widget.EditText;
import android.widget.Button;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    //--------------------------------------------------------------------INIT VARIABLES---
    EditText programCode, programName, programDesc, programFees;
    Button submitRecord, showData;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    //--------------------------------------------------------------------ASSIGN VALUES TO VARIABLES---
        programCode = findViewById(R.id.PCodeET);
        programName = findViewById(R.id.PNameET);
        programDesc = findViewById(R.id.PDescET);
        programFees = findViewById(R.id.PFeesET);
        submitRecord = findViewById(R.id.submit);
        showData = findViewById(R.id.showdata);
        db = openOrCreateDatabase("College", Context.MODE_PRIVATE, null);
        //Query to Create Table in database if it does not exist
        db.execSQL("CREATE TABLE IF NOT EXISTS Program(programID INTEGER PRIMARY KEY AUTOINCREMENT ,progCode VARCHAR,progName VARCHAR ,progDescription DECIMAL,progFees DECIMAL) ;");

        submitRecord.setOnClickListener(view -> {
            db.execSQL("INSERT INTO Program(progCode,progName,progDescription,progFees)VALUES('" + programCode.getText().toString() + "','" + programName.getText().toString() + "','" + programDesc.getText().toString() + "','" + programFees.getText().toString() + "')");
            //Query to Select all data from program
            Cursor curs = db.rawQuery("SELECT * FROM Program", null);
            if(curs.getCount() != 0){
                StringBuffer buffer;
                buffer = getProgramRecord(curs);
                displayStatus("Success", "");
                curs.close();
            }
        });

        showData.setOnClickListener(view -> {
            StringBuffer buffer;
            //Query to Select all data from program
            Cursor curs = db.rawQuery("SELECT *  FROM Program", null);

            //Data is not present in database return error-No Data
            if(curs.getCount() == 0){
                displayStatus("Error", "No Data");
                return;
            }
            //When data is present in Database
                buffer = getProgramRecord(curs);
                displayStatus("Program Data", buffer.toString());
                curs.close();
        });
    }

    private StringBuffer getProgramRecord(Cursor cursor) {
        StringBuffer buffer = new StringBuffer();
        //Append output string
        cursor.moveToLast();
        buffer.append("Program Id : ").append(cursor.getString(0)).append("\n");
        buffer.append("Program Code : ").append(cursor.getString(1)).append("\n");
        buffer.append("Program Name : ").append(cursor.getString(2)).append("\n");
        buffer.append("Program Description : ").append(cursor.getString(3)).append("\n");
        buffer.append("Program Fees : ").append(cursor.getString(4)).append("\n");
        return buffer;
    }

    private void displayStatus(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder .setCancelable(true);
        builder .setTitle(title);
        builder .setMessage(message);
        builder .show();
    }
}