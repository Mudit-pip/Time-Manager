package com.timemanager.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.timemanager.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class fragment_chart extends Fragment {


    public fragment_chart() {
        // Required empty public constructor
    }


    ConstraintLayout constlay;
    SQLiteDatabase db;
    TextView tx[];
    TextView bartx[];
    int width_bw2bar;
    String ids[], time[], task[], col[];

    TextView mon, tue, wed, thu, fri, sat, sun;

    MenuBuilder menuBuilder;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chart, container, false);

        constlay = v.findViewById(R.id.fragment_chart_constraintlay);

        db = getActivity().openOrCreateDatabase("dba_Mudit", getActivity().MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_main (RegId Integer PRIMARY KEY AUTOINCREMENT, time varchar(200), task varchar(400), day varchar(200), colora varchar(200))");


        SharedPreferences pref = getContext().getSharedPreferences("pref_Mudit", getContext().MODE_PRIVATE);
        int new_k = pref.getInt("start_time", 8);


        int starthr = new_k;
        load_day_time_bar(starthr);

//        showgantt("monday", starthr);
//        showgantt("tuesday", starthr);
//        showgantt("wednesday", starthr);
//        showgantt("thursday", starthr);
//        showgantt("friday", starthr);
//        showgantt("saturday", starthr);
//        showgantt("sunday", starthr);


        return v;
    }


    @SuppressLint("Range")
    public void showgantt(String day, int starthr) {

        Cursor cur;
        String qry;
        int n;


        qry = "SELECT * FROM tbl_main WHERE day = '" + day + "'";
        cur = db.rawQuery(qry, null);

        cur.moveToLast();
        n = cur.getCount();
        cur.moveToFirst();


        ids = new String[n];
        time = new String[n];
        task = new String[n];
        col = new String[n];

        for (int i = 0; i < n; i++) {

            ids[i] = cur.getString(cur.getColumnIndex("RegId"));
            time[i] = cur.getString(cur.getColumnIndex("time"));
            task[i] = cur.getString(cur.getColumnIndex("task"));
            col[i] = cur.getString(cur.getColumnIndex("colora"));
            cur.moveToNext();
        }

        ConstraintSet constraintSet = new ConstraintSet();


        TextView label[] = new TextView[n];

        for (int i = 0; i < n; i++) {
            label[i] = new TextView(getContext());
            label[i].setId(View.generateViewId());
            label[i].setGravity(Gravity.CENTER_VERTICAL);
            label[i].setTag(ids[i]);
//            label[i].setTag(time[i]);

            if (Integer.parseInt(col[i]) == 1) {
                label[i].setBackgroundColor(getContext().getColor(R.color.custom_color1));
            } else if (Integer.parseInt(col[i]) == 2) {
                label[i].setBackgroundColor(getContext().getColor(R.color.custom_color2));
            } else if (Integer.parseInt(col[i]) == 3) {
                label[i].setBackgroundColor(getContext().getColor(R.color.custom_color3));
            } else if (Integer.parseInt(col[i]) == 4) {
                label[i].setBackgroundColor(getContext().getColor(R.color.custom_color4));
            } else if (Integer.parseInt(col[i]) == 5) {
                label[i].setBackgroundColor(getContext().getColor(R.color.custom_color5));
            } else if (Integer.parseInt(col[i]) == 6) {
                label[i].setBackgroundColor(getContext().getColor(R.color.custom_color6));
            } else {
                label[i].setBackgroundColor(getContext().getColor(R.color.md_grey_400));
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

//                Toast.makeText(getContext(), a[0]+" - "+a[1], Toast.LENGTH_SHORT).show();


                int s_hr_24 = 8;
                if (s_am_pm[1].equals("am") && !(shm[0].equals("12"))) {
                    s_hr_24 = Integer.parseInt(shm[0]);
                } else if (s_am_pm[1].equals("pm") && shm[0].equals("12")) {
                    s_hr_24 = Integer.parseInt(shm[0]);
                } else if (s_am_pm[1].equals("pm")) {
                    s_hr_24 = Integer.parseInt(shm[0]) + 12;
                } else if (s_am_pm[1].equals("am") && shm[0].equals("12")) {
                    s_hr_24 = 24;
                }

                int e_hr_24 = 8;
                if (e_am_pm[1].equals("am") && !(ehm[0].equals("12"))) {
                    e_hr_24 = Integer.parseInt(ehm[0]);
                } else if (e_am_pm[1].equals("pm") && ehm[0].equals("12")) {
                    e_hr_24 = Integer.parseInt(ehm[0]);
                } else if (e_am_pm[1].equals("pm")) {
                    e_hr_24 = Integer.parseInt(ehm[0]) + 12;
                } else if (e_am_pm[1].equals("am") && ehm[0].equals("12")) {
                    e_hr_24 = 24;
                }

//                int e_hr_24 = 9;
//                if (!(ehm[0].equals("12") && e_am_pm[1].equals("pm"))) {
//                    if (e_am_pm[1].equals("am")) {
//                        e_hr_24 = Integer.parseInt(ehm[0]);
//                    } else {
//                        e_hr_24 = Integer.parseInt(ehm[0]) + 12;
//                    }
//                } else {
//                    e_hr_24 = 12;
//                }


                ///////////////////// for start counters
                int counter = 0;
                double per_min = (width_bw2bar * 1.0) / 60;
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, starthr);
                cal.set(Calendar.MINUTE, 00);
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");

                Calendar ch_cal = Calendar.getInstance();
                ch_cal.set(Calendar.HOUR_OF_DAY, s_hr_24);
                ch_cal.set(Calendar.MINUTE, 0);


                while (!(timeFormat.format(ch_cal.getTime()).equals(timeFormat.format(cal.getTime())))) {
                    cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
                    counter++;
                }


                int start_count_min = 0;
                cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, s_hr_24);
                cal.set(Calendar.MINUTE, 00);

                ch_cal = Calendar.getInstance();
                ch_cal.set(Calendar.HOUR_OF_DAY, s_hr_24);
                ch_cal.set(Calendar.MINUTE, Integer.parseInt(s_am_pm[0]));


                while (!(timeFormat.format(ch_cal.getTime()).equals(timeFormat.format(cal.getTime())))) {
                    cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 1);
                    start_count_min++;
                }
                ////////////////////////////////


                /////////////////////////////////for end counters
                int end_counter = 0;

                cal.set(Calendar.HOUR_OF_DAY, starthr);
                cal.set(Calendar.MINUTE, 00);


                ch_cal.set(Calendar.HOUR_OF_DAY, e_hr_24);
                ch_cal.set(Calendar.MINUTE, 0);


                while (!(timeFormat.format(ch_cal.getTime()).equals(timeFormat.format(cal.getTime())))) {
                    cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
                    end_counter++;
                }


                int end_count_min = 0;
                cal.set(Calendar.HOUR_OF_DAY, e_hr_24);
                cal.set(Calendar.MINUTE, 00);

                ch_cal.set(Calendar.HOUR_OF_DAY, e_hr_24);
                ch_cal.set(Calendar.MINUTE, Integer.parseInt(e_am_pm[0]));


                while (!(timeFormat.format(ch_cal.getTime()).equals(timeFormat.format(cal.getTime())))) {
                    cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 1);
                    end_count_min++;
                }
                ///////////////////////////////////////////////////////////


                int id_day = 0;
                if (day == "monday") {
                    id_day = mon.getId();
                } else if (day == "tuesday") {
                    id_day = tue.getId();
                } else if (day == "wednesday") {
                    id_day = wed.getId();
                } else if (day == "thursday") {
                    id_day = thu.getId();
                } else if (day == "friday") {
                    id_day = fri.getId();
                } else if (day == "saturday") {
                    id_day = sat.getId();
                } else if (day == "sunday") {
                    id_day = sun.getId();
                }

                double abc = start_count_min * per_min;
                int kb = (int) abc;
                constraintSet.connect(label[i].getId(), ConstraintSet.START, bartx[counter].getId(), ConstraintSet.END, kb - (int) per_min);

                abc = end_count_min * per_min;
                kb = (int) abc;
                constraintSet.connect(label[i].getId(), ConstraintSet.END, bartx[end_counter].getId(), ConstraintSet.START, -kb - (int) per_min);
                constraintSet.connect(label[i].getId(), ConstraintSet.TOP, id_day, ConstraintSet.TOP);
                constraintSet.connect(label[i].getId(), ConstraintSet.BOTTOM, id_day, ConstraintSet.BOTTOM);


            }

        }

        constraintSet.applyTo(constlay);


        ////on click's
        //////on click's
        for (int i = 0; i < n; i++) {
//            registerForContextMenu(label[i]);
            label[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String query;
                    Cursor cry;
                    int k;

                    query = "SELECT * FROM tbl_main";
                    cry = db.rawQuery(query, null);

                    cry.moveToLast();
                    k = cry.getCount();
                    cry.moveToFirst();

                    String ids_m[], time_m[], task_m[], col_m[];
                    ids_m = new String[k];
                    time_m = new String[k];
                    task_m = new String[k];
                    col_m = new String[k];

                    for (int i = 0; i < k; i++) {

                        ids_m[i] = cry.getString(cry.getColumnIndex("RegId"));
                        time_m[i] = cry.getString(cry.getColumnIndex("time"));
                        task_m[i] = cry.getString(cry.getColumnIndex("task"));
                        col_m[i] = cry.getString(cry.getColumnIndex("colora"));
                        cry.moveToNext();
                    }

                    int id = Integer.parseInt(v.getTag().toString());

                    for (int i = 0; i < k; i++) {
                        if (Integer.parseInt(ids_m[i]) == id) {
                            id = i;
                            break;
                        }
                    }

//                    Toast.makeText(getContext(), "" + task_m[id], Toast.LENGTH_SHORT).show();
//                    v.showContextMenu();

                    showCustomContextMenu(v, time_m[id], col_m[id]);

                }
            });


        }

        ////////////////////on click's over
        ////////////////////////////////////////////////////////

    }


//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        String s = v.getTag().toString();
//        menu.add(s);
//    }

    public void showCustomContextMenu(View v, String time, String colora) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View customMenuView = inflater.inflate(R.layout.custom_dropdown_for_chart_onclick, null);


        PopupWindow popupWindow = new PopupWindow(
                customMenuView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        popupWindow.setOutsideTouchable(true);

        popupWindow.showAsDropDown(v, 0, 0);

        TextView item1 = customMenuView.findViewById(R.id.custom_dropdown_text1);
        item1.setText(time);

        LinearLayout layout = customMenuView.findViewById(R.id.custom_dropdown_lay);

        if(colora.equals("1")){
            layout.setBackgroundColor(getContext().getColor(R.color.custom_color1));
        } else if(colora.equals("2")){
            layout.setBackgroundColor(getContext().getColor(R.color.custom_color2));
        } else if(colora.equals("3")){
            layout.setBackgroundColor(getContext().getColor(R.color.custom_color3));
        } else if(colora.equals("4")){
            layout.setBackgroundColor(getContext().getColor(R.color.custom_color4));
        } else if(colora.equals("5")){
            layout.setBackgroundColor(getContext().getColor(R.color.custom_color5));
        } else if(colora.equals("6")){
            layout.setBackgroundColor(getContext().getColor(R.color.custom_color6));
        } else{
            layout.setBackgroundColor(getContext().getColor(R.color.md_grey_400));
        }

    }


    public void load_day_time_bar(int starthr) {

        ConstraintSet constraintSet = new ConstraintSet();

        ///adding All 24 TimeStamps
        tx = new TextView[24];

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, starthr);


        cal.set(Calendar.MINUTE, 00);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");

        for (int i = 0; i < 24; i++) {
            tx[i] = new TextView(getContext());
            tx[i].setId(View.generateViewId());
            tx[i].setTextSize(15);

            constlay.addView(tx[i]);

            constraintSet.constrainWidth(tx[i].getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainHeight(tx[i].getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.setHorizontalWeight(tx[i].getId(), 1);
            constraintSet.setMargin(tx[i].getId(), ConstraintSet.START, 100);


            tx[i].setText(timeFormat.format(cal.getTime()));
            cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);

        }


        constraintSet.connect(tx[0].getId(), ConstraintSet.START, R.id.fragment_chart_time, ConstraintSet.END);
        constraintSet.connect(tx[0].getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);

        for (int i = 1; i < 23; i++) {
            constraintSet.connect(tx[i].getId(), ConstraintSet.START, tx[i - 1].getId(), ConstraintSet.END);
            constraintSet.connect(tx[i].getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        }

        constraintSet.connect(tx[23].getId(), ConstraintSet.START, tx[22].getId(), ConstraintSet.END);
        constraintSet.connect(tx[23].getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(tx[23].getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);

        //////////////////////


        //adding | to all 24 TimeStapms
        bartx = new TextView[24];


        for (int i = 0; i < 24; i++) {
            bartx[i] = new TextView(getContext());
            bartx[i].setId(View.generateViewId());
            bartx[i].setTextSize(15);

            constlay.addView(bartx[i]);

            constraintSet.constrainWidth(bartx[i].getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainHeight(bartx[i].getId(), ConstraintSet.WRAP_CONTENT);


            bartx[i].setTextColor(getContext().getResources().getColor(R.color.md_grey_300));
            bartx[i].setText("|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|");

        }


        for (int i = 0; i < 24; i++) {
            constraintSet.connect(bartx[i].getId(), ConstraintSet.START, tx[i].getId(), ConstraintSet.START);
            constraintSet.connect(bartx[i].getId(), ConstraintSet.END, tx[i].getId(), ConstraintSet.END);
            constraintSet.connect(bartx[i].getId(), ConstraintSet.TOP, tx[i].getId(), ConstraintSet.BOTTOM);

        }
        /////////////////////////////////////////////


        ///////////////////////////////////////////////
        int day_size = 15;
        //adding MONDAY..TUE.......and all
        mon = new TextView(getContext());
        mon.setId(View.generateViewId());
        mon.setTextSize(day_size);
        mon.setText("Monday");
        constlay.addView(mon);
        constraintSet.constrainWidth(mon.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(mon.getId(), ConstraintSet.WRAP_CONTENT);

        tue = new TextView(getContext());
        tue.setId(View.generateViewId());
        tue.setTextSize(day_size);
        tue.setText("Tuesday");
        constlay.addView(tue);
        constraintSet.constrainWidth(tue.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(tue.getId(), ConstraintSet.WRAP_CONTENT);

        wed = new TextView(getContext());
        wed.setId(View.generateViewId());
        wed.setTextSize(day_size);
        wed.setText("Wednesday");
        constlay.addView(wed);
        constraintSet.constrainWidth(wed.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(wed.getId(), ConstraintSet.WRAP_CONTENT);

        thu = new TextView(getContext());
        thu.setId(View.generateViewId());
        thu.setTextSize(day_size);
        constlay.addView(thu);
        constraintSet.constrainWidth(thu.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(thu.getId(), ConstraintSet.WRAP_CONTENT);
        thu.setText("Thursday");

        fri = new TextView(getContext());
        fri.setId(View.generateViewId());
        fri.setTextSize(day_size);
        constlay.addView(fri);
        constraintSet.constrainWidth(fri.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(fri.getId(), ConstraintSet.WRAP_CONTENT);
        fri.setText("Friday");

        sat = new TextView(getContext());
        sat.setId(View.generateViewId());
        sat.setTextSize(day_size);
        constlay.addView(sat);
        constraintSet.constrainWidth(sat.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(sat.getId(), ConstraintSet.WRAP_CONTENT);
        sat.setText("Saturday");

        sun = new TextView(getContext());
        sun.setId(View.generateViewId());
        sun.setTextSize(day_size);
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
        ////////////////////////////////////


        ///////////////////////////////////////////finding width between 2 bartx 0 and 1
        TextView test_lbl;
        test_lbl = new TextView(getContext());
        test_lbl.setId(View.generateViewId());
        test_lbl.setGravity(Gravity.CENTER_VERTICAL);

//        test_lbl.setBackgroundColor(getContext().getColor(R.color.white));

        LinearLayout.LayoutParams ly = new LinearLayout.LayoutParams(0, 0, 1f);
        test_lbl.setLayoutParams(ly);

        constlay.addView(test_lbl);


        constraintSet.connect(test_lbl.getId(), ConstraintSet.START, bartx[0].getId(), ConstraintSet.END);
        constraintSet.connect(test_lbl.getId(), ConstraintSet.END, bartx[1].getId(), ConstraintSet.START);
        constraintSet.connect(test_lbl.getId(), ConstraintSet.TOP, mon.getId(), ConstraintSet.TOP);
        constraintSet.connect(test_lbl.getId(), ConstraintSet.BOTTOM, mon.getId(), ConstraintSet.BOTTOM);


        test_lbl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = test_lbl.getWidth();
                width_bw2bar = width;

                showgantt("monday", starthr);
                showgantt("tuesday", starthr);
                showgantt("wednesday", starthr);
                showgantt("thursday", starthr);
                showgantt("friday", starthr);
                showgantt("saturday", starthr);
                showgantt("sunday", starthr);

                test_lbl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        ////////////////////////////////////////////


        constraintSet.applyTo(constlay);

    }


    public void loadfrag(Fragment fragment, int flag) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (flag == 0) {
            ft.add(R.id.E3_container, fragment);
        } else if (flag == 1) {
            ft.replace(R.id.E3_container, fragment);
        }

        ft.commit();
    }


//
//    @SuppressLint("Range")
//    public void showgantt(String day) {
//
//        Cursor cur;
//        String qry;
//        int n;
//
//
//        qry = "SELECT * FROM tbl_main WHERE day = '" + day + "'";
//        cur = db.rawQuery(qry, null);
//
//        cur.moveToLast();
//        n = cur.getCount();
//        cur.moveToFirst();
//
//
//        ids = new String[n];
//        time = new String[n];
//        task = new String[n];
//
//        for (int i = 0; i < n; i++) {
//
//            ids[i] = cur.getString(cur.getColumnIndex("RegId"));
//            time[i] = cur.getString(cur.getColumnIndex("time"));
//            task[i] = cur.getString(cur.getColumnIndex("task"));
//            cur.moveToNext();
//        }
//
//        ConstraintSet constraintSet = new ConstraintSet();
//
//
//        TextView label[] = new TextView[n];
//
//        for (int i = 0; i < n; i++) {
//
//            label[i] = new TextView(getContext());
//            label[i].setId(View.generateViewId());
//            label[i].setGravity(Gravity.CENTER_VERTICAL);
//            label[i].setTag(ids[i]);
//
//            if (i % 2 == 0) {
//                label[i].setBackgroundColor(getContext().getColor(R.color.red));
//            } else {
//                label[i].setBackgroundColor(getContext().getColor(R.color.green));
//            }
//
//            LinearLayout.LayoutParams ly = new LinearLayout.LayoutParams(0, 0, 1f);
//            label[i].setLayoutParams(ly);
//            label[i].setText(task[i]);
//
//            constlay.addView(label[i]);
//        }
//
//
//        for (int i = 0; i < n; i++) {
//
//            if (time[i] != null) {
//
//                String a[] = time[i].split(" - ");
//                //a[0] = 10:00 am   a[1] = 11:30 am
//
//                String shm[] = a[0].split(":");
//                String ehm[] = a[1].split(":");
//                //shm[0] = 10   shm[1] = 00 am;
//                //ehm[0] = 11   ehm[1] = 30 am;
//
//                String s_am_pm[] = shm[1].split(" ");
//                String e_am_pm[] = ehm[1].split(" ");
//                // s_am_pm[0] = 00   s_am_pm[1] = am;
//                // e_am_pm[0] = 30   e_am_pm[1] = am;
//
//                int s_ap = 0, e_ap = 0;
//                if (s_am_pm[1].equals("am")) {
//                    s_ap = Calendar.AM;
//                } else {
//                    s_ap = Calendar.PM;
//                }
//
//                if (e_am_pm[1].equals("am")) {
//                    e_ap = Calendar.AM;
//                } else {
//                    e_ap = Calendar.PM;
//                }
//
//
//                int counter = 0;
//                Calendar cal = Calendar.getInstance();
//                cal.set(Calendar.HOUR, 8);
//                cal.set(Calendar.MINUTE, 00);
//                cal.set(Calendar.AM_PM, Calendar.AM);
//                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
//
//                Calendar ch_cal = Calendar.getInstance();
//                ch_cal.set(Calendar.HOUR, Integer.parseInt(shm[0]));
//                ch_cal.set(Calendar.MINUTE, Integer.parseInt(s_am_pm[0]));
//                ch_cal.set(Calendar.AM_PM, s_ap);
//
//
//                while (!(timeFormat.format(ch_cal.getTime()).equals(timeFormat.format(cal.getTime())))) {
//                    cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 30);
//                    counter++;
//                }
//
//
//                int end_counter = 0;
//
//                cal.set(Calendar.HOUR, 8);
//                cal.set(Calendar.MINUTE, 00);
//                cal.set(Calendar.AM_PM, Calendar.AM);
//
//
//                int k = 8;
//                if (Integer.parseInt(ehm[0].trim()) == 12) {
//                    k = 0;
//                } else {
//                    k = Integer.parseInt(ehm[0].trim());
//                }
//
//                ch_cal.set(Calendar.HOUR, k);
//                ch_cal.set(Calendar.MINUTE, Integer.parseInt(e_am_pm[0]));
//                ch_cal.set(Calendar.AM_PM, e_ap);
//
//
//                while (!(timeFormat.format(ch_cal.getTime()).equals(timeFormat.format(cal.getTime())))) {
//                    cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 30);
//                    end_counter++;
//                }
//
//
//                int id_day = 0;
//                if (day == "monday") {
//                    id_day = mon.getId();
//                } else if (day == "tuesday") {
//                    id_day = tue.getId();
//                } else if (day == "wednesday") {
//                    id_day = wed.getId();
//                } else if (day == "thursday") {
//                    id_day = thu.getId();
//                } else if (day == "friday") {
//                    id_day = fri.getId();
//                } else if (day == "saturday") {
//                    id_day = sat.getId();
//                } else if (day == "sunday") {
//                    id_day = sun.getId();
//                }
//
//                constraintSet.connect(label[i].getId(), ConstraintSet.START, bartx[counter].getId(), ConstraintSet.END);
//                constraintSet.connect(label[i].getId(), ConstraintSet.END, bartx[end_counter].getId(), ConstraintSet.START);
//                constraintSet.connect(label[i].getId(), ConstraintSet.TOP, id_day, ConstraintSet.TOP);
//                constraintSet.connect(label[i].getId(), ConstraintSet.BOTTOM, id_day, ConstraintSet.BOTTOM);
//
//
//            }
//
//        }
//        constraintSet.applyTo(constlay);
//
//        //on click's
////
////        for (int i = 0; i < n; i++) {
////            label[i].setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////
////                    String query;
////                    Cursor cry;
////                    int k;
////
////                    query = "SELECT * FROM tbl_main";
////                    cry = db.rawQuery(query, null);
////
////                    cry.moveToLast();
////                    k = cry.getCount();
////                    cry.moveToFirst();
////
////                    String ids_m[], time_m[], task_m[];
////                    ids_m = new String[k];
////                    time_m = new String[k];
////                    task_m = new String[k];
////
////                    for (int i = 0; i < k; i++) {
////
////                        ids_m[i] = cry.getString(cry.getColumnIndex("RegId"));
////                        time_m[i] = cry.getString(cry.getColumnIndex("time"));
////                        task_m[i] = cry.getString(cry.getColumnIndex("task"));
////                        cry.moveToNext();
////                    }
////
////                    int id = Integer.parseInt(v.getTag().toString()) - 1;
////
////                    AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
////                    View view = getLayoutInflater().inflate(R.layout.e1_dialogbox_final, null);
////
////                    TimePicker tp_from, tp_to;
////                    Spinner sp;
////
////                    EditText task = view.findViewById(R.id.E1_dialog_task);
////                    tp_from = view.findViewById(R.id.E1_tp_from);
////                    tp_to = view.findViewById(R.id.E1_tp_to);
////                    sp = view.findViewById(R.id.E1_dialog_spinner_for_day);
////
////                    sp.setVisibility(View.GONE);
////
////                    task.setHint("Enter Task Here...");
////                    task.setText(task_m[id]);
////
////
////                    String time_clicked = time_m[id];
////
////                    String time_s_e[] = time_clicked.split(" - ");
////                    // time_s_e[0] = 8:00 am  time_s_e[1] = 8:30 am
////
////                    String start_time[] = time_s_e[0].split(" ");
////                    // start_time[0] = 8:00   start_time[1] = am
////
////                    String end_time[] = time_s_e[1].split(" ");
////                    // end_time[0] = 8:30   end_time[1] = am
////
////                    String start_time_h_m[] = start_time[0].split(":");
////                    // start_time_h_m[0] = 8   start_time_h_m[1] = 00
////
////                    String end_time_h_m[] = end_time[0].split(":");
////                    // end_time_h_m[0] = 8   end_time_h_m[1] = 30
////
////
////                    int z = 0;
////                    if (start_time[1].equals("am")) {
////                        z = Integer.parseInt(start_time_h_m[0]);
////                    } else if (start_time[1].equals("pm")) {
////                        z = Integer.parseInt(start_time_h_m[0]) + 12;
////                    }
////
////                    tp_from.setHour(z);
////
////                    View minute = tp_from.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
////                    NumberPicker minutePicker = (NumberPicker) minute;
////                    minutePicker.setMinValue(0);
////                    minutePicker.setMaxValue(1);
////                    minutePicker.setDisplayedValues(new String[]{"00", "30"});
////
////                    if (start_time_h_m[1].equals("00")) {
////                        minutePicker.setValue(0);
////                    } else if (start_time_h_m[1].equals("30")) {
////                        minutePicker.setValue(1);
////                    }
////
////
/////////////////////////////////////////////////////////////////////
////
////
////                    int ze = 0;
////                    if (end_time[1].equals("am")) {
////                        ze = Integer.parseInt(end_time_h_m[0]);
////                    } else if (end_time[1].equals("pm")) {
////                        ze = Integer.parseInt(end_time_h_m[0]) + 12;
////                    }
////
////                    tp_to.setHour(ze);
////
////                    View min = tp_to.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
////                    NumberPicker minutePicker_to = (NumberPicker) min;
////                    minutePicker_to.setMinValue(0);
////                    minutePicker_to.setMaxValue(1);
////                    minutePicker_to.setDisplayedValues(new String[]{"00", "30"});
////
////                    if (end_time_h_m[1].equals("00")) {
////                        minutePicker_to.setValue(0);
////                    } else if (end_time_h_m[1].equals("30")) {
////                        minutePicker_to.setValue(1);
////                    }
////
////
////                    ab.setNeutralButton("Cancel", null);
////
////                    ab.setPositiveButton("Update", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////
////                            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
////                            String time_new = "";
////
////                            Calendar cal = Calendar.getInstance();
////                            cal.set(Calendar.HOUR_OF_DAY, tp_from.getHour());
////                            cal.set(Calendar.MINUTE, tp_from.getMinute() * 30);
////                            time_new = timeFormat.format(cal.getTime()) + " - ";
////
////                            cal.set(Calendar.HOUR_OF_DAY, tp_to.getHour());
////                            cal.set(Calendar.MINUTE, tp_to.getMinute() * 30);
////
////                            time_new += timeFormat.format(cal.getTime());
////
////                            String qur;
////                            qur = "UPDATE tbl_main SET task = '" + task.getText().toString() + "', time = '" + time_new + "' WHERE  RegId =  '" + ids_m[id] + "'";
////                            db.execSQL(qur);
////
////                            loadfrag(new fragment_chart(), 1);
////
////                        }
////                    });
////
////
////                    ab.setView(view);
////                    ab.create().show();
////                }
////            });
////
////
////        }
//
//    }


    ////// old load_day_time_bar() for 30 min interval and no permin time
//    public void load_day_time_bar() {
//
//        ConstraintSet constraintSet = new ConstraintSet();
//
//        ///adding All 48 TimeStamps
//        tx = new TextView[48];
//
//
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.AM_PM, Calendar.AM);
//        cal.set(Calendar.HOUR, 8);
//        cal.set(Calendar.MINUTE, 00);
//        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
//
//        for (int i = 0; i < 48; i++) {
//            tx[i] = new TextView(getContext());
//            tx[i].setId(View.generateViewId());
//            tx[i].setTextSize(20);
//
//            constlay.addView(tx[i]);
//
//            constraintSet.constrainWidth(tx[i].getId(), ConstraintSet.WRAP_CONTENT);
//            constraintSet.constrainHeight(tx[i].getId(), ConstraintSet.WRAP_CONTENT);
//            constraintSet.setHorizontalWeight(tx[i].getId(), 1);
//            constraintSet.setMargin(tx[i].getId(), ConstraintSet.START, 100);
//
//
//            tx[i].setText(timeFormat.format(cal.getTime()));
//            cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 30);
//
//        }
//
//
//        constraintSet.connect(tx[0].getId(), ConstraintSet.START, R.id.fragment_chart_time, ConstraintSet.END);
//        constraintSet.connect(tx[0].getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
//
//        for (int i = 1; i < 47; i++) {
//            constraintSet.connect(tx[i].getId(), ConstraintSet.START, tx[i - 1].getId(), ConstraintSet.END);
//            constraintSet.connect(tx[i].getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
//        }
//
//        constraintSet.connect(tx[47].getId(), ConstraintSet.START, tx[46].getId(), ConstraintSet.END);
//        constraintSet.connect(tx[47].getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
//        constraintSet.connect(tx[47].getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
//
//        //////////////////////
//
//
//        //adding | to all 48 TimeStapms
//        bartx = new TextView[48];
//
//
//        for (int i = 0; i < 48; i++) {
//            bartx[i] = new TextView(getContext());
//            bartx[i].setId(View.generateViewId());
//            bartx[i].setTextSize(20);
//
//            constlay.addView(bartx[i]);
//
//            constraintSet.constrainWidth(bartx[i].getId(), ConstraintSet.WRAP_CONTENT);
//            constraintSet.constrainHeight(bartx[i].getId(), ConstraintSet.WRAP_CONTENT);
//
//
//            bartx[i].setTextColor(getContext().getResources().getColor(R.color.md_grey_300));
//            bartx[i].setText("|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|");
//
//        }
//
//
//        for (int i = 0; i < 48; i++) {
//            constraintSet.connect(bartx[i].getId(), ConstraintSet.START, tx[i].getId(), ConstraintSet.START);
//            constraintSet.connect(bartx[i].getId(), ConstraintSet.END, tx[i].getId(), ConstraintSet.END);
//            constraintSet.connect(bartx[i].getId(), ConstraintSet.TOP, tx[i].getId(), ConstraintSet.BOTTOM);
//
//        }
//        /////////////////////////////////////////////
//
//
//        //adding MONDAY..TUE.......and all
//        mon = new TextView(getContext());
//        mon.setId(View.generateViewId());
//        mon.setTextSize(20);
//        mon.setText("Monday");
//        constlay.addView(mon);
//        constraintSet.constrainWidth(mon.getId(), ConstraintSet.WRAP_CONTENT);
//        constraintSet.constrainHeight(mon.getId(), ConstraintSet.WRAP_CONTENT);
//
//        tue = new TextView(getContext());
//        tue.setId(View.generateViewId());
//        tue.setTextSize(20);
//        tue.setText("Tuesday");
//        constlay.addView(tue);
//        constraintSet.constrainWidth(tue.getId(), ConstraintSet.WRAP_CONTENT);
//        constraintSet.constrainHeight(tue.getId(), ConstraintSet.WRAP_CONTENT);
//
//        wed = new TextView(getContext());
//        wed.setId(View.generateViewId());
//        wed.setTextSize(20);
//        wed.setText("Wednesday");
//        constlay.addView(wed);
//        constraintSet.constrainWidth(wed.getId(), ConstraintSet.WRAP_CONTENT);
//        constraintSet.constrainHeight(wed.getId(), ConstraintSet.WRAP_CONTENT);
//
//        thu = new TextView(getContext());
//        thu.setId(View.generateViewId());
//        thu.setTextSize(20);
//        constlay.addView(thu);
//        constraintSet.constrainWidth(thu.getId(), ConstraintSet.WRAP_CONTENT);
//        constraintSet.constrainHeight(thu.getId(), ConstraintSet.WRAP_CONTENT);
//        thu.setText("Thursday");
//
//        fri = new TextView(getContext());
//        fri.setId(View.generateViewId());
//        fri.setTextSize(20);
//        constlay.addView(fri);
//        constraintSet.constrainWidth(fri.getId(), ConstraintSet.WRAP_CONTENT);
//        constraintSet.constrainHeight(fri.getId(), ConstraintSet.WRAP_CONTENT);
//        fri.setText("Friday");
//
//        sat = new TextView(getContext());
//        sat.setId(View.generateViewId());
//        sat.setTextSize(20);
//        constlay.addView(sat);
//        constraintSet.constrainWidth(sat.getId(), ConstraintSet.WRAP_CONTENT);
//        constraintSet.constrainHeight(sat.getId(), ConstraintSet.WRAP_CONTENT);
//        sat.setText("Saturday");
//
//        sun = new TextView(getContext());
//        sun.setId(View.generateViewId());
//        sun.setTextSize(20);
//        constlay.addView(sun);
//        constraintSet.constrainWidth(sun.getId(), ConstraintSet.WRAP_CONTENT);
//        constraintSet.constrainHeight(sun.getId(), ConstraintSet.WRAP_CONTENT);
//        sun.setText("Sunday");
//
//
//        constraintSet.connect(mon.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
//        constraintSet.connect(tue.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
//        constraintSet.connect(wed.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
//        constraintSet.connect(thu.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
//        constraintSet.connect(fri.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
//        constraintSet.connect(sat.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
//        constraintSet.connect(sun.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
//
//
//        constraintSet.connect(mon.getId(), ConstraintSet.TOP, tx[0].getId(), ConstraintSet.BOTTOM);
//        constraintSet.connect(tue.getId(), ConstraintSet.TOP, mon.getId(), ConstraintSet.BOTTOM);
//        constraintSet.connect(mon.getId(), ConstraintSet.BOTTOM, tue.getId(), ConstraintSet.TOP);
//
//        constraintSet.connect(tue.getId(), ConstraintSet.BOTTOM, wed.getId(), ConstraintSet.TOP);
//        constraintSet.connect(wed.getId(), ConstraintSet.TOP, tue.getId(), ConstraintSet.BOTTOM);
//
//        constraintSet.connect(wed.getId(), ConstraintSet.BOTTOM, thu.getId(), ConstraintSet.TOP);
//        constraintSet.connect(thu.getId(), ConstraintSet.TOP, wed.getId(), ConstraintSet.BOTTOM);
//
//        constraintSet.connect(thu.getId(), ConstraintSet.BOTTOM, fri.getId(), ConstraintSet.TOP);
//        constraintSet.connect(fri.getId(), ConstraintSet.TOP, thu.getId(), ConstraintSet.BOTTOM);
//
//        constraintSet.connect(fri.getId(), ConstraintSet.BOTTOM, sat.getId(), ConstraintSet.TOP);
//        constraintSet.connect(sat.getId(), ConstraintSet.TOP, fri.getId(), ConstraintSet.BOTTOM);
//
//        constraintSet.connect(sat.getId(), ConstraintSet.BOTTOM, sun.getId(), ConstraintSet.TOP);
//        constraintSet.connect(sun.getId(), ConstraintSet.TOP, sat.getId(), ConstraintSet.BOTTOM);
//
//        constraintSet.connect(sun.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
//        ////////////////////////////
//
//
//        constraintSet.applyTo(constlay);
//
//    }


}