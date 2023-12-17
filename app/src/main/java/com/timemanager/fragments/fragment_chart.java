package com.timemanager.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.timemanager.R;
import com.timemanager.usermodal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

public class fragment_chart extends Fragment {


    public fragment_chart() {

    }


    ConstraintLayout constlay;
    //    SQLiteDatabase db;
    TextView tx[];
    TextView bartx[];
    int width_bw2bar;
    String ids[], time[], task[], col[], days[];
    TextView label_teacher[];

    TextView mon, tue, wed, thu, fri, sat, sun;


    FirebaseDatabase db_fire;
    DatabaseReference ref;
    ArrayList<String> time_arraylist, task_arraylist, nodename_arraylist, color_arraylist, day_arraylist;
    FirebaseAuth auth;
    int starthr = 100;


    ProgressDialog pg;

    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chart, container, false);

        constlay = v.findViewById(R.id.fragment_chart_constraintlay);

        pg = new ProgressDialog(getContext());
        pg.setMessage("Loading");
        pg.show();
        pg.setCancelable(false);


        //firebase
        db_fire = FirebaseDatabase.getInstance();
        time_arraylist = new ArrayList<>();
        task_arraylist = new ArrayList<>();
        color_arraylist = new ArrayList<>();
        nodename_arraylist = new ArrayList<>();
        day_arraylist = new ArrayList<>();


        asynk_mudit asynk_mudit = new asynk_mudit();
        asynk_mudit.execute();


        return v;
    }


    class asynk_mudit extends AsyncTask<Void, Void, Void> {
        private final CountDownLatch latch = new CountDownLatch(7);

        @Override
        protected void onPreExecute() {
        calc_min_time("monday");
        calc_min_time("tuesday");
        calc_min_time("wednesday");
        calc_min_time("thursday");
        calc_min_time("friday");
        calc_min_time("saturday");
        calc_min_time("sunday");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // Wait for all tasks to complete
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            load_day_time_bar(starthr);
        }


        public void calc_min_time(String day) {
            auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();


            ref = db_fire.getReference().child("Users").child(sanitizeEmail(user.getEmail())).child("personal_time_info").child(day);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot mydata : snapshot.getChildren()) {
                        usermodal um = mydata.getValue(usermodal.class);
                        if (um != null) {
                            String a[] = new String[2];
                            a = um.getTime().split(":");
                            String b = a[1].substring(3);
                            String c[] = new String[2];
                            c = b.split(" ");

                            int current = Integer.parseInt(a[0]);
                            if (c[0].equals("pm")) {
                                current += 12;
                            }
                            if (starthr > current) {
                                starthr = current;
                            }

                        }
                    }
                    latch.countDown();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }



    }




    @SuppressLint("Range")
    public void showgantt(String day, int starthr) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        ref = db_fire.getReference().child("Users").child(sanitizeEmail(user.getEmail())).child("personal_time_info").child(day);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                time_arraylist.clear();
                task_arraylist.clear();
                color_arraylist.clear();
                nodename_arraylist.clear();
                day_arraylist.clear();

                for (DataSnapshot mydata : snapshot.getChildren()) {
                    usermodal um = mydata.getValue(usermodal.class);
                    if (um != null) {
                        time_arraylist.add(um.getTime());
                        task_arraylist.add(um.getTask());
                        color_arraylist.add(um.getColor() + "");
                        nodename_arraylist.add(mydata.getKey());
                        day_arraylist.add(um.getDay());
                    }
                }

                if (time_arraylist.size() == task_arraylist.size()) {
                    try {
                        time = time_arraylist.toArray(new String[time_arraylist.size()]);
                        task = task_arraylist.toArray(new String[task_arraylist.size()]);
                        col = color_arraylist.toArray(new String[color_arraylist.size()]);
                        ids = nodename_arraylist.toArray(new String[nodename_arraylist.size()]);
                        days = day_arraylist.toArray(new String[day_arraylist.size()]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                int n = task.length;
                ConstraintSet constraintSet = new ConstraintSet();


                TextView[] label = new TextView[n];


                for (int i = 0; i < n && getContext() != null; i++) {
                    label[i] = new TextView(getContext());
                    label[i].setId(View.generateViewId());

                    String for_tag = time[i] + "~" + col[i] + "~" + days[i];
                    label[i].setTag(for_tag);


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

                    SharedPreferences pref = getContext().getSharedPreferences("pref_Mudit", getContext().MODE_PRIVATE);
                    float text_size = pref.getFloat("text_size", 40);
                    label[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size);

                    // Setting padding (left, top, right, bottom) in pixels
                    label[i].setPadding(16, 0, 0, 0);

                    label[i].setGravity(Gravity.CENTER_VERTICAL);
                    constlay.addView(label[i]);


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

//                        if(label[i]!=null) {
                        constraintSet.connect(label[i].getId(), ConstraintSet.START, bartx[counter].getId(), ConstraintSet.END, kb - (int) per_min);

                        abc = end_count_min * per_min;
                        kb = (int) abc;


                        int height_taskbox = pref.getInt("taskbox_height", 90);

                        constraintSet.constrainHeight(label[i].getId(), height_taskbox);

                        constraintSet.connect(label[i].getId(), ConstraintSet.END, bartx[end_counter].getId(), ConstraintSet.START, -kb - (int) per_min);
                        constraintSet.connect(label[i].getId(), ConstraintSet.TOP, id_day, ConstraintSet.TOP);
                        constraintSet.connect(label[i].getId(), ConstraintSet.BOTTOM, id_day, ConstraintSet.BOTTOM);
                    }

                }

                constraintSet.applyTo(constlay);


                ////on click's
                //////on click's
                for (int i = 0; i < n; i++) {

                    if (label[i] != null) {
                        label[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String tag_recv = v.getTag().toString();
                                String split[] = tag_recv.split("~");

                                showCustomContextMenu(v, split[0], split[2], split[1]);
                            }
                        });

                        ////////////////////on click's over
                        ////////////////////////////////////////////////////////


                    }
                }

                if (day.equals("sunday")) {
                    pg.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


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
            tx[i].setText(timeFormat.format(cal.getTime()));
            tx[i].setTextSize(15);

            constraintSet.constrainWidth(tx[i].getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainHeight(tx[i].getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.setHorizontalWeight(tx[i].getId(), 1);
            constraintSet.setMargin(tx[i].getId(), ConstraintSet.START, 100);

            constlay.addView(tx[i]);

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

//                pg.dismiss();

                test_lbl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        ////////////////////////////////////////////


        constraintSet.applyTo(constlay);

    }

    private String sanitizeEmail(String email) {
        return email != null ? email.replace(".", "_dot_").replace("@", "_at_") : "error";
    }


    //teachers data
//    private void fetchteachersdata(String coursename, int del) {
//        ref = db_fire.getReference().child("coursetimes").child(coursename);
//        listener = ref.addValueEventListener(new ValueEventListener() {
//            int kill=del;
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if(kill != 0) {
//                    for (int i = 0; i < counter_aa; i++) {
//                        constlay.removeView(label_teacher[i]);
//                    }
//                }
//                counter_aa = 0;
//                for (DataSnapshot mydata : snapshot.getChildren()) {
//                    counter_aa++;
//                }
//
//                label_teacher = new TextView[counter_aa];
//
//                int i = 0;
//                for (DataSnapshot mydata : snapshot.getChildren()) {
//                    dataclass data = mydata.getValue(dataclass.class);
////                    Toast.makeText(getContext(), data.getDay() + ":" + data.getTime(), Toast.LENGTH_SHORT).show();
//
//                    ///////////////////////////////////////////////////////////////////////////////////////
//                    ///////////////////////////////////////////////////////////////////////////////////////
//                    ConstraintSet constraintSet = new ConstraintSet();
//
//                    label_teacher[i] = new TextView(getContext());
//                    label_teacher[i].setId(View.generateViewId());
//                    label_teacher[i].setGravity(Gravity.CENTER_VERTICAL);
//
//                    label_teacher[i].setBackgroundColor(getContext().getColor(R.color.md_grey_400));
//
//                    LinearLayout.LayoutParams ly = new LinearLayout.LayoutParams(0, 0, 1f);
//                    label_teacher[i].setLayoutParams(ly);
//                    label_teacher[i].setText(coursename);
//
//                    constlay.addView(label_teacher[i]);
//
//
//                    if (data.getTime() != null) {
//
//                        String a[] = data.getTime().split(" - ");
//                        //a[0] = 10:00 am   a[1] = 11:30 am
//
//                        String shm[] = a[0].split(":");
//                        String ehm[] = a[1].split(":");
//                        //shm[0] = 10   shm[1] = 00 am;
//                        //ehm[0] = 11   ehm[1] = 30 am;
//
//                        String s_am_pm[] = shm[1].split(" ");
//                        String e_am_pm[] = ehm[1].split(" ");
//                        // s_am_pm[0] = 00   s_am_pm[1] = am;
//                        // e_am_pm[0] = 30   e_am_pm[1] = am;
//
//                        int s_hr_24 = 8;
//                        if (s_am_pm[1].equals("am") && !(shm[0].equals("12"))) {
//                            s_hr_24 = Integer.parseInt(shm[0]);
//                        } else if (s_am_pm[1].equals("pm") && shm[0].equals("12")) {
//                            s_hr_24 = Integer.parseInt(shm[0]);
//                        } else if (s_am_pm[1].equals("pm")) {
//                            s_hr_24 = Integer.parseInt(shm[0]) + 12;
//                        } else if (s_am_pm[1].equals("am") && shm[0].equals("12")) {
//                            s_hr_24 = 24;
//                        }
//
//                        int e_hr_24 = 8;
//                        if (e_am_pm[1].equals("am") && !(ehm[0].equals("12"))) {
//                            e_hr_24 = Integer.parseInt(ehm[0]);
//                        } else if (e_am_pm[1].equals("pm") && ehm[0].equals("12")) {
//                            e_hr_24 = Integer.parseInt(ehm[0]);
//                        } else if (e_am_pm[1].equals("pm")) {
//                            e_hr_24 = Integer.parseInt(ehm[0]) + 12;
//                        } else if (e_am_pm[1].equals("am") && ehm[0].equals("12")) {
//                            e_hr_24 = 24;
//                        }
//
//                        ///////////////////// for start counters
//                        int counter = 0;
//                        double per_min = (width_bw2bar * 1.0) / 60;
//                        Calendar cal = Calendar.getInstance();
//                        cal.set(Calendar.HOUR_OF_DAY, starthr);
//                        cal.set(Calendar.MINUTE, 00);
//                        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
//
//                        Calendar ch_cal = Calendar.getInstance();
//                        ch_cal.set(Calendar.HOUR_OF_DAY, s_hr_24);
//                        ch_cal.set(Calendar.MINUTE, 0);
//
//
//                        while (!(timeFormat.format(ch_cal.getTime()).equals(timeFormat.format(cal.getTime())))) {
//                            cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
//                            counter++;
//                        }
//
//
//                        int start_count_min = 0;
//                        cal = Calendar.getInstance();
//                        cal.set(Calendar.HOUR_OF_DAY, s_hr_24);
//                        cal.set(Calendar.MINUTE, 00);
//
//                        ch_cal = Calendar.getInstance();
//                        ch_cal.set(Calendar.HOUR_OF_DAY, s_hr_24);
//                        ch_cal.set(Calendar.MINUTE, Integer.parseInt(s_am_pm[0]));
//
//
//                        while (!(timeFormat.format(ch_cal.getTime()).equals(timeFormat.format(cal.getTime())))) {
//                            cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 1);
//                            start_count_min++;
//                        }
//                        ////////////////////////////////
//
//
//                        /////////////////////////////////for end counters
//                        int end_counter = 0;
//
//                        cal.set(Calendar.HOUR_OF_DAY, starthr);
//                        cal.set(Calendar.MINUTE, 00);
//
//
//                        ch_cal.set(Calendar.HOUR_OF_DAY, e_hr_24);
//                        ch_cal.set(Calendar.MINUTE, 0);
//
//
//                        while (!(timeFormat.format(ch_cal.getTime()).equals(timeFormat.format(cal.getTime())))) {
//                            cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
//                            end_counter++;
//                        }
//
//
//                        int end_count_min = 0;
//                        cal.set(Calendar.HOUR_OF_DAY, e_hr_24);
//                        cal.set(Calendar.MINUTE, 00);
//
//                        ch_cal.set(Calendar.HOUR_OF_DAY, e_hr_24);
//                        ch_cal.set(Calendar.MINUTE, Integer.parseInt(e_am_pm[0]));
//
//
//                        while (!(timeFormat.format(ch_cal.getTime()).equals(timeFormat.format(cal.getTime())))) {
//                            cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 1);
//                            end_count_min++;
//                        }
//                        ///////////////////////////////////////////////////////////
//
//
//                        int id_day = 0;
//                        if (data.getDay().equals("monday")) {
//                            id_day = mon.getId();
//                        } else if (data.getDay().equals("tuesday")) {
//                            id_day = tue.getId();
//                        } else if (data.getDay().equals("wednesday")) {
//                            id_day = wed.getId();
//                        } else if (data.getDay().equals("thursday")) {
//                            id_day = thu.getId();
//                        } else if (data.getDay().equals("friday")) {
//                            id_day = fri.getId();
//                        } else if (data.getDay().equals("saturday")) {
//                            id_day = sat.getId();
//                        } else if (data.getDay().equals("sunday")) {
//                            id_day = sun.getId();
//                        }
//
//                        double abc = start_count_min * per_min;
//                        int kb = (int) abc;
//                        constraintSet.connect(label_teacher[i].getId(), ConstraintSet.START, bartx[counter].getId(), ConstraintSet.END, kb - (int) per_min);
//
//                        abc = end_count_min * per_min;
//                        kb = (int) abc;
//                        constraintSet.connect(label_teacher[i].getId(), ConstraintSet.END, bartx[end_counter].getId(), ConstraintSet.START, -kb - (int) per_min);
//                        constraintSet.connect(label_teacher[i].getId(), ConstraintSet.TOP, id_day, ConstraintSet.TOP);
//                        constraintSet.connect(label_teacher[i].getId(), ConstraintSet.BOTTOM, id_day, ConstraintSet.BOTTOM);
//
//                        i++;
//
//                    }
//
//                    constraintSet.applyTo(constlay);
//                    kill=1;
//                    /////////////////////////////////////////////////////////////////////////////////////////////////
//                    //////////////////////////////////////////////////////////////////////////////////////////
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }


//normal
//    @SuppressLint("Range")
//    public void showgantt(String day, int starthr) {
//        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_main (RegId Integer PRIMARY KEY AUTOINCREMENT, time varchar(200), task varchar(400), day varchar(200), colora varchar(200))");
//
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
//        col = new String[n];
//
//        for (int i = 0; i < n; i++) {
//
//            ids[i] = cur.getString(cur.getColumnIndex("RegId"));
//            time[i] = cur.getString(cur.getColumnIndex("time"));
//            task[i] = cur.getString(cur.getColumnIndex("task"));
//            col[i] = cur.getString(cur.getColumnIndex("colora"));
//            cur.moveToNext();
//        }
//
//        ConstraintSet constraintSet = new ConstraintSet();
//
//
//        TextView label[] = new TextView[n];
//
//        for (int i = 0; i < n; i++) {
//            label[i] = new TextView(getContext());
//            label[i].setId(View.generateViewId());
//            label[i].setGravity(Gravity.CENTER_VERTICAL);
//            label[i].setTag(ids[i]);
////            label[i].setTag(time[i]);
//
//            if (Integer.parseInt(col[i]) == 1) {
//                label[i].setBackgroundColor(getContext().getColor(R.color.custom_color1));
//            } else if (Integer.parseInt(col[i]) == 2) {
//                label[i].setBackgroundColor(getContext().getColor(R.color.custom_color2));
//            } else if (Integer.parseInt(col[i]) == 3) {
//                label[i].setBackgroundColor(getContext().getColor(R.color.custom_color3));
//            } else if (Integer.parseInt(col[i]) == 4) {
//                label[i].setBackgroundColor(getContext().getColor(R.color.custom_color4));
//            } else if (Integer.parseInt(col[i]) == 5) {
//                label[i].setBackgroundColor(getContext().getColor(R.color.custom_color5));
//            } else if (Integer.parseInt(col[i]) == 6) {
//                label[i].setBackgroundColor(getContext().getColor(R.color.custom_color6));
//            } else {
//                label[i].setBackgroundColor(getContext().getColor(R.color.md_grey_400));
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
//
//
//                int s_hr_24 = 8;
//                if (s_am_pm[1].equals("am") && !(shm[0].equals("12"))) {
//                    s_hr_24 = Integer.parseInt(shm[0]);
//                } else if (s_am_pm[1].equals("pm") && shm[0].equals("12")) {
//                    s_hr_24 = Integer.parseInt(shm[0]);
//                } else if (s_am_pm[1].equals("pm")) {
//                    s_hr_24 = Integer.parseInt(shm[0]) + 12;
//                } else if (s_am_pm[1].equals("am") && shm[0].equals("12")) {
//                    s_hr_24 = 24;
//                }
//
//                int e_hr_24 = 8;
//                if (e_am_pm[1].equals("am") && !(ehm[0].equals("12"))) {
//                    e_hr_24 = Integer.parseInt(ehm[0]);
//                } else if (e_am_pm[1].equals("pm") && ehm[0].equals("12")) {
//                    e_hr_24 = Integer.parseInt(ehm[0]);
//                } else if (e_am_pm[1].equals("pm")) {
//                    e_hr_24 = Integer.parseInt(ehm[0]) + 12;
//                } else if (e_am_pm[1].equals("am") && ehm[0].equals("12")) {
//                    e_hr_24 = 24;
//                }
//
//
//                ///////////////////// for start counters
//                int counter = 0;
//                double per_min = (width_bw2bar * 1.0) / 60;
//                Calendar cal = Calendar.getInstance();
//                cal.set(Calendar.HOUR_OF_DAY, starthr);
//                cal.set(Calendar.MINUTE, 00);
//                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
//
//                Calendar ch_cal = Calendar.getInstance();
//                ch_cal.set(Calendar.HOUR_OF_DAY, s_hr_24);
//                ch_cal.set(Calendar.MINUTE, 0);
//
//
//                while (!(timeFormat.format(ch_cal.getTime()).equals(timeFormat.format(cal.getTime())))) {
//                    cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
//                    counter++;
//                }
//
//
//                int start_count_min = 0;
//                cal = Calendar.getInstance();
//                cal.set(Calendar.HOUR_OF_DAY, s_hr_24);
//                cal.set(Calendar.MINUTE, 00);
//
//                ch_cal = Calendar.getInstance();
//                ch_cal.set(Calendar.HOUR_OF_DAY, s_hr_24);
//                ch_cal.set(Calendar.MINUTE, Integer.parseInt(s_am_pm[0]));
//
//
//                while (!(timeFormat.format(ch_cal.getTime()).equals(timeFormat.format(cal.getTime())))) {
//                    cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 1);
//                    start_count_min++;
//                }
//                ////////////////////////////////
//
//
//                /////////////////////////////////for end counters
//                int end_counter = 0;
//
//                cal.set(Calendar.HOUR_OF_DAY, starthr);
//                cal.set(Calendar.MINUTE, 00);
//
//
//                ch_cal.set(Calendar.HOUR_OF_DAY, e_hr_24);
//                ch_cal.set(Calendar.MINUTE, 0);
//
//
//                while (!(timeFormat.format(ch_cal.getTime()).equals(timeFormat.format(cal.getTime())))) {
//                    cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
//                    end_counter++;
//                }
//
//
//                int end_count_min = 0;
//                cal.set(Calendar.HOUR_OF_DAY, e_hr_24);
//                cal.set(Calendar.MINUTE, 00);
//
//                ch_cal.set(Calendar.HOUR_OF_DAY, e_hr_24);
//                ch_cal.set(Calendar.MINUTE, Integer.parseInt(e_am_pm[0]));
//
//
//                while (!(timeFormat.format(ch_cal.getTime()).equals(timeFormat.format(cal.getTime())))) {
//                    cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 1);
//                    end_count_min++;
//                }
//                ///////////////////////////////////////////////////////////
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
//                double abc = start_count_min * per_min;
//                int kb = (int) abc;
//                constraintSet.connect(label[i].getId(), ConstraintSet.START, bartx[counter].getId(), ConstraintSet.END, kb - (int) per_min);
//
//                abc = end_count_min * per_min;
//                kb = (int) abc;
//                constraintSet.connect(label[i].getId(), ConstraintSet.END, bartx[end_counter].getId(), ConstraintSet.START, -kb - (int) per_min);
//                constraintSet.connect(label[i].getId(), ConstraintSet.TOP, id_day, ConstraintSet.TOP);
//                constraintSet.connect(label[i].getId(), ConstraintSet.BOTTOM, id_day, ConstraintSet.BOTTOM);
//
//
//            }
//
//        }
//
//        constraintSet.applyTo(constlay);
//
//
//        ////on click's
//        //////on click's
//        for (int i = 0; i < n; i++) {
//            label[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String query;
//                    Cursor cry;
//                    int k;
//
//                    query = "SELECT * FROM tbl_main";
//                    cry = db.rawQuery(query, null);
//
//                    cry.moveToLast();
//                    k = cry.getCount();
//                    cry.moveToFirst();
//
//                    String ids_m[], time_m[], task_m[], col_m[];
//                    ids_m = new String[k];
//                    time_m = new String[k];
//                    task_m = new String[k];
//                    col_m = new String[k];
//
//                    for (int i = 0; i < k; i++) {
//
//                        ids_m[i] = cry.getString(cry.getColumnIndex("RegId"));
//                        time_m[i] = cry.getString(cry.getColumnIndex("time"));
//                        task_m[i] = cry.getString(cry.getColumnIndex("task"));
//                        col_m[i] = cry.getString(cry.getColumnIndex("colora"));
//                        cry.moveToNext();
//                    }
//
//                    int id = Integer.parseInt(v.getTag().toString());
//
//                    for (int i = 0; i < k; i++) {
//                        if (Integer.parseInt(ids_m[i]) == id) {
//                            id = i;
//                            break;
//                        }
//                    }
//
//                    showCustomContextMenu(v, time_m[id], col_m[id]);
//
//                }
//            });
//
//
//        }
//
//        ////////////////////on click's over
//        ////////////////////////////////////////////////////////
//
//    }
//


    public void showCustomContextMenu(View v, String time, String day, String colora) {
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

        TextView day_custom_context = customMenuView.findViewById(R.id.custom_dropdown_day_txt);
        day_custom_context.setText(day);

        LinearLayout layout = customMenuView.findViewById(R.id.custom_dropdown_lay);

        if (colora.equals("1")) {
            layout.setBackgroundColor(getContext().getColor(R.color.custom_color1));
        } else if (colora.equals("2")) {
            layout.setBackgroundColor(getContext().getColor(R.color.custom_color2));
        } else if (colora.equals("3")) {
            layout.setBackgroundColor(getContext().getColor(R.color.custom_color3));
        } else if (colora.equals("4")) {
            layout.setBackgroundColor(getContext().getColor(R.color.custom_color4));
        } else if (colora.equals("5")) {
            layout.setBackgroundColor(getContext().getColor(R.color.custom_color5));
        } else if (colora.equals("6")) {
            layout.setBackgroundColor(getContext().getColor(R.color.custom_color6));
        } else {
            layout.setBackgroundColor(getContext().getColor(R.color.md_grey_400));
        }

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


}