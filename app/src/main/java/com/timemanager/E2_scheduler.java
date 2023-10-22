package com.timemanager;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class E2_scheduler extends AppCompatActivity {


    ConstraintLayout constlay;
    SQLiteDatabase db;
    TextView tx[], bartx[];
    TextView mon, tue, wed, thu, fri, sat, sun;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e2_scheduler);

        constlay = findViewById(R.id.E2_constraintlay);

        db = openOrCreateDatabase("db_Mudit", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_main (RegId Integer PRIMARY KEY AUTOINCREMENT, time varchar(200), task varchar(20))");


        ConstraintSet constraintSet = new ConstraintSet();

        ///adding All 48 TimeStamps
        tx = new TextView[48];


        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.AM_PM, Calendar.AM);
        cal.set(Calendar.HOUR, 8);
        cal.set(Calendar.MINUTE, 00);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");

        for (int i = 0; i < 48; i++) {
            tx[i] = new TextView(E2_scheduler.this);
            tx[i].setId(View.generateViewId());
            tx[i].setTextSize(20);

            constlay.addView(tx[i]);

            constraintSet.constrainWidth(tx[i].getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainHeight(tx[i].getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.setHorizontalWeight(tx[i].getId(), 1);
            constraintSet.setMargin(tx[i].getId(), ConstraintSet.START, 100);


            tx[i].setText(timeFormat.format(cal.getTime()));
            cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 30);

        }


        constraintSet.connect(tx[0].getId(), ConstraintSet.START, R.id.E2_time, ConstraintSet.END);
        constraintSet.connect(tx[0].getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);

        for (int i = 1; i < 47; i++) {
            constraintSet.connect(tx[i].getId(), ConstraintSet.START, tx[i - 1].getId(), ConstraintSet.END);
            constraintSet.connect(tx[i].getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        }

        constraintSet.connect(tx[47].getId(), ConstraintSet.START, tx[46].getId(), ConstraintSet.END);
        constraintSet.connect(tx[47].getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(tx[47].getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);

        //////////////////////


        //adding | to all 48 TimeStapms
        bartx = new TextView[48];


        for (int i = 0; i < 48; i++) {
            bartx[i] = new TextView(E2_scheduler.this);
            bartx[i].setId(View.generateViewId());
            bartx[i].setTextSize(20);

            constlay.addView(bartx[i]);

            constraintSet.constrainWidth(bartx[i].getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainHeight(bartx[i].getId(), ConstraintSet.WRAP_CONTENT);


            bartx[i].setText("|");

        }


        for (int i = 0; i < 48; i++) {
            constraintSet.connect(bartx[i].getId(), ConstraintSet.START, tx[i].getId(), ConstraintSet.START);
            constraintSet.connect(bartx[i].getId(), ConstraintSet.END, tx[i].getId(), ConstraintSet.END);
            constraintSet.connect(bartx[i].getId(), ConstraintSet.TOP, tx[i].getId(), ConstraintSet.BOTTOM);

        }


        //adding MONDAY..TUE.......and all
        mon = new TextView(E2_scheduler.this);
        mon.setId(View.generateViewId());
        mon.setTextSize(20);
        mon.setText("Monday");
        constlay.addView(mon);
        constraintSet.constrainWidth(mon.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(mon.getId(), ConstraintSet.WRAP_CONTENT);

        tue = new TextView(E2_scheduler.this);
        tue.setId(View.generateViewId());
        tue.setTextSize(20);
        tue.setText("Tuesday");
        constlay.addView(tue);
        constraintSet.constrainWidth(tue.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(tue.getId(), ConstraintSet.WRAP_CONTENT);

        wed = new TextView(E2_scheduler.this);
        wed.setId(View.generateViewId());
        wed.setTextSize(20);
        wed.setText("Wednesday");
        constlay.addView(wed);
        constraintSet.constrainWidth(wed.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(wed.getId(), ConstraintSet.WRAP_CONTENT);

        thu = new TextView(E2_scheduler.this);
        thu.setId(View.generateViewId());
        thu.setTextSize(20);
        constlay.addView(thu);
        constraintSet.constrainWidth(thu.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(thu.getId(), ConstraintSet.WRAP_CONTENT);
        thu.setText("Thursday");

        fri = new TextView(E2_scheduler.this);
        fri.setId(View.generateViewId());
        fri.setTextSize(20);
        constlay.addView(fri);
        constraintSet.constrainWidth(fri.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(fri.getId(), ConstraintSet.WRAP_CONTENT);
        fri.setText("Friday");

        sat = new TextView(E2_scheduler.this);
        sat.setId(View.generateViewId());
        sat.setTextSize(20);
        constlay.addView(sat);
        constraintSet.constrainWidth(sat.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(sat.getId(), ConstraintSet.WRAP_CONTENT);
        sat.setText("Saturday");

        sun = new TextView(E2_scheduler.this);
        sun.setId(View.generateViewId());
        sun.setTextSize(20);
        constlay.addView(sun);
        constraintSet.constrainWidth(sun.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(sun.getId(), ConstraintSet.WRAP_CONTENT);
        sun.setText("Sunday");


        constraintSet.connect(mon.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(tue.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(wed.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(thu.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(fri.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(sat.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(sun.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);


        constraintSet.connect(mon.getId(), ConstraintSet.TOP, tx[0].getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(tue.getId(), ConstraintSet.TOP, mon.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(mon.getId(), ConstraintSet.BOTTOM, tue.getId(), ConstraintSet.TOP);

        constraintSet.connect(tue.getId(), ConstraintSet.BOTTOM, wed.getId(), ConstraintSet.TOP);
        constraintSet.connect(wed.getId(), ConstraintSet.TOP, tue.getId(), ConstraintSet.BOTTOM);

        constraintSet.connect(wed.getId(), ConstraintSet.BOTTOM, thu.getId(), ConstraintSet.TOP);
        constraintSet.connect(thu.getId(), ConstraintSet.TOP, wed.getId(), ConstraintSet.BOTTOM);

        constraintSet.connect(thu.getId(), ConstraintSet.BOTTOM, fri.getId(), ConstraintSet.TOP);
        constraintSet.connect(fri.getId(), ConstraintSet.TOP, thu.getId(), ConstraintSet.BOTTOM);

        constraintSet.connect(fri.getId(), ConstraintSet.BOTTOM, sat.getId(), ConstraintSet.TOP);
        constraintSet.connect(sat.getId(), ConstraintSet.TOP, fri.getId(), ConstraintSet.BOTTOM);

        constraintSet.connect(sat.getId(), ConstraintSet.BOTTOM, sun.getId(), ConstraintSet.TOP);
        constraintSet.connect(sun.getId(), ConstraintSet.TOP, sat.getId(), ConstraintSet.BOTTOM);

        constraintSet.connect(sun.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        ////////////////////////////


        constraintSet.applyTo(constlay);


        showgantt();


//        ConstraintSet constraintSet = new ConstraintSet();
//
//
//
//        TextView ed = new TextView(E2_scheduler.this);
//        ed.setId(View.generateViewId());
//        ed.setBackgroundColor(getColor(R.color.red));
//        LinearLayout.LayoutParams ly = new LinearLayout.LayoutParams(0, 0, 1f);
//        ed.setLayoutParams(ly);
//
//        constlay.addView(ed);
//
//        constraintSet.connect(ed.getId(), ConstraintSet.START, R.id.E2_bar830, ConstraintSet.END);
//        constraintSet.connect(ed.getId(), ConstraintSet.END, R.id.E2_bar10, ConstraintSet.START);
//        constraintSet.connect(ed.getId(), ConstraintSet.TOP, R.id.E2_wed, ConstraintSet.TOP);
//        constraintSet.connect(ed.getId(), ConstraintSet.BOTTOM, R.id.E2_wed, ConstraintSet.BOTTOM);
//
//
//        constraintSet.applyTo(constlay);


    }


    @SuppressLint("Range")
    public void showgantt() {

        Cursor cur;
        String qry;
        int n;


        qry = "SELECT * FROM tbl_main";
        cur = db.rawQuery(qry, null);

        cur.moveToLast();
        n = cur.getCount();
        cur.moveToFirst();

        String ids[], time[], task[];
        ids = new String[n];
        time = new String[n];
        task = new String[n];

        for (int i = 0; i < n; i++) {

            ids[i] = cur.getString(cur.getColumnIndex("RegId"));
            time[i] = cur.getString(cur.getColumnIndex("time"));
            task[i] = cur.getString(cur.getColumnIndex("task"));
            cur.moveToNext();
        }

        ConstraintSet constraintSet = new ConstraintSet();


        TextView label[] = new TextView[n];

        for (int i = 0; i < n; i++) {

            label[i] = new TextView(E2_scheduler.this);
            label[i].setId(View.generateViewId());
            label[i].setGravity(Gravity.CENTER_VERTICAL);

            if (i % 2 == 0) {
                label[i].setBackgroundColor(getColor(R.color.red));
            } else {
                label[i].setBackgroundColor(getColor(R.color.green));
            }

            LinearLayout.LayoutParams ly = new LinearLayout.LayoutParams(0, 0, 1f);
            label[i].setLayoutParams(ly);
            label[i].setText(task[i]);

            constlay.addView(label[i]);
        }


        for (int i = 0; i < n; i++) {

            if (time[i] != null) {

                String a[] = time[i].split(" - ");
                //a[0] = 10:00 am   a[1] = 11:30 am

                String shm[] = a[0].split(":");
                String ehm[] = a[1].split(":");
                //shm[0] = 10   shm[1] = 00 am;
                //ehm[0] = 11   ehm[1] = 30 am;

                String s_am_pm[] = shm[1].split(" ");
                String e_am_pm[] = ehm[1].split(" ");
                // s_am_pm[0] = 00   s_am_pm[1] = am;
                // e_am_pm[0] = 30   e_am_pm[1] = am;

                int s_ap = 0, e_ap = 0;
                if (s_am_pm[1].equals("am")) {
                    s_ap = Calendar.AM;
                } else {
                    s_ap = Calendar.PM;
                }

                if (e_am_pm[1].equals("am")) {
                    e_ap = Calendar.AM;
                } else {
                    e_ap = Calendar.PM;
                }


                int counter = 0;
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR, 8);
                cal.set(Calendar.MINUTE, 00);
                cal.set(Calendar.AM_PM, Calendar.AM);
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");

                Calendar ch_cal = Calendar.getInstance();
                ch_cal.set(Calendar.HOUR, Integer.parseInt(shm[0]));
                ch_cal.set(Calendar.MINUTE, Integer.parseInt(s_am_pm[0]));
                ch_cal.set(Calendar.AM_PM, s_ap);


                while (!(timeFormat.format(ch_cal.getTime()).equals(timeFormat.format(cal.getTime())))) {
                    cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 30);
                    counter++;
                }


                int end_counter = 0;

                cal.set(Calendar.HOUR, 8);
                cal.set(Calendar.MINUTE, 00);
                cal.set(Calendar.AM_PM, Calendar.AM);


                int k = 8;
                if (Integer.parseInt(ehm[0].trim()) == 12) {
                    k = 0;
                } else {
                    k = Integer.parseInt(ehm[0].trim());
                }

                ch_cal.set(Calendar.HOUR, k);
                ch_cal.set(Calendar.MINUTE, Integer.parseInt(e_am_pm[0]));
                ch_cal.set(Calendar.AM_PM, e_ap);


//                Toast.makeText(this, "" + timeFormat.format(ch_cal.getTime()), Toast.LENGTH_SHORT).show();

                while (!(timeFormat.format(ch_cal.getTime()).equals(timeFormat.format(cal.getTime())))) {
                    cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 30);
                    end_counter++;
                }


                constraintSet.connect(label[i].getId(), ConstraintSet.START, bartx[counter].getId(), ConstraintSet.END);
                constraintSet.connect(label[i].getId(), ConstraintSet.END, bartx[end_counter].getId(), ConstraintSet.START);
                constraintSet.connect(label[i].getId(), ConstraintSet.TOP, wed.getId(), ConstraintSet.TOP);
                constraintSet.connect(label[i].getId(), ConstraintSet.BOTTOM, wed.getId(), ConstraintSet.BOTTOM);


            }

        }
        constraintSet.applyTo(constlay);


    }


}