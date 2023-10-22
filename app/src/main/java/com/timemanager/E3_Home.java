package com.timemanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.timemanager.fragments.fragment_chart;
import com.timemanager.fragments.fragment_list;
import com.timemanager.fragments.fragment_settings;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class E3_Home extends AppCompatActivity {

    //    FloatingActionButton addbtn;
//    BottomNavigationView bnv;
    SQLiteDatabase db;
    int bnv_pos = 0;
    int spinner_pos = 0;

    int hr_from, min_from, hr_to, min_to;
    Button addnewbtn;
    MeowBottomNavigation meownav;
    int color_pressed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e3_home);


//        addbtn = findViewById(R.id.E3_addbtn);
//        bnv = findViewById(R.id.E3_bnv);
        addnewbtn = findViewById(R.id.E3_btn_2);

        db = openOrCreateDatabase("dba_Mudit", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_main (RegId Integer PRIMARY KEY AUTOINCREMENT, time varchar(200), task varchar(400), day varchar(200), colora varchar(200))");


        loadfrag(new fragment_list(), 0);

        SharedPreferences pref = getSharedPreferences("pref_Mudit", MODE_PRIVATE);
        int b = pref.getInt("theme", 0);
        if (b == 1) {
            changetheme(R.style.Base_Theme_TimeManager1, 1);
        } else if (b == 2) {
            changetheme(R.style.Base_Theme_TimeManager2, 2);
        } else if (b == 3) {
            changetheme(R.style.Base_Theme_TimeManager3, 3);
        } else if (b == 4) {
            changetheme(R.style.Base_Theme_TimeManager4, 4);
        } else if (b == 5) {
            changetheme(R.style.Base_Theme_TimeManager5, 5);
        } else if (b == 6) {
            changetheme(R.style.Base_Theme_TimeManager6, 6);
        } else{
            changetheme(R.style.Base_Theme_TimeManager5, 5);
        }


        meownav = findViewById(R.id.E3_bnv_2);

        meownav.show(2, true);

        meownav.add(new MeowBottomNavigation.Model(1, R.drawable.baseline_settings_24));
        meownav.add(new MeowBottomNavigation.Model(2, R.drawable.baseline_home_24));
        meownav.add(new MeowBottomNavigation.Model(3, R.drawable.baseline_bar_chart_24));

        meownav.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                switch (model.getId()) {
                    case 1:
                        addnewbtn.setVisibility(View.GONE);
                        loadfrag(new fragment_settings(), 1);
                        break;

                    case 2:
                        addnewbtn.setVisibility(View.VISIBLE);
                        bnv_pos = 0;
                        loadfrag(new fragment_list(), 1);
                        break;

                    case 3:
                        addnewbtn.setVisibility(View.VISIBLE);
                        bnv_pos = 1;
                        loadfrag(new fragment_chart(), 1);
                        break;

                }


                return null;
            }
        });


//        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                int id = item.getItemId();
//
//                if (id == R.id.bnv_menu_list) {
//                    bnv_pos = 0;
//                    loadfrag(new fragment_list(), 1);
//
//                } else if (id == R.id.bnv_menu_chart) {
//                    bnv_pos = 1;
//                    loadfrag(new fragment_chart(), 1);
//
//                }
//                return true;
//            }
//        });
//
//

//add button is here
        addnewbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {


                Spinner sp;
                String[] days_for_sp = {"Monday", "Tuesday",
                        "Wednesday", "Thursday",
                        "Friday", "Saturday", "Sunday"};

                AlertDialog.Builder ab = new AlertDialog.Builder(E3_Home.this);
                View view = getLayoutInflater().inflate(R.layout.e1_dialogbox_final, null);

                EditText task = view.findViewById(R.id.E1_dialog_task);
                sp = view.findViewById(R.id.E1_dialog_spinner_for_day);
                Button btn_from = view.findViewById(R.id.E1_dialog_btn_from);
                Button btn_to = view.findViewById(R.id.E1_dialog_btn_to);


                ////////////////////////////////////////////////////////toogling colour buttons start
                ToggleButton tb1, tb2, tb3, tb4, tb5, tb6;
                tb1 = view.findViewById(R.id.E1_dialog_tb1);
                tb2 = view.findViewById(R.id.E1_dialog_tb2);
                tb3 = view.findViewById(R.id.E1_dialog_tb3);
                tb4 = view.findViewById(R.id.E1_dialog_tb4);
                tb5 = view.findViewById(R.id.E1_dialog_tb5);
                tb6 = view.findViewById(R.id.E1_dialog_tb6);

                color_pressed = 0;

                tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {
                            color_pressed = 1;
                            tb2.setChecked(false);
                            tb3.setChecked(false);
                            tb4.setChecked(false);
                            tb5.setChecked(false);
                            tb6.setChecked(false);
                        }
                    }
                });

                tb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {
                            color_pressed = 2;
                            tb1.setChecked(false);
                            tb3.setChecked(false);
                            tb4.setChecked(false);
                            tb5.setChecked(false);
                            tb6.setChecked(false);
                        }
                    }
                });

                tb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {
                            color_pressed = 3;
                            tb2.setChecked(false);
                            tb1.setChecked(false);
                            tb4.setChecked(false);
                            tb5.setChecked(false);
                            tb6.setChecked(false);
                        }
                    }
                });

                tb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {
                            color_pressed = 4;
                            tb2.setChecked(false);
                            tb3.setChecked(false);
                            tb1.setChecked(false);
                            tb5.setChecked(false);
                            tb6.setChecked(false);
                        }
                    }
                });

                tb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {
                            color_pressed = 5;
                            tb2.setChecked(false);
                            tb3.setChecked(false);
                            tb4.setChecked(false);
                            tb1.setChecked(false);
                            tb6.setChecked(false);
                        }
                    }
                });

                tb6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {
                            color_pressed = 6;
                            tb2.setChecked(false);
                            tb3.setChecked(false);
                            tb4.setChecked(false);
                            tb5.setChecked(false);
                            tb1.setChecked(false);
                        }
                    }
                });
                ////////////////////////////////////////////////////////toogling colour buttons end


                if (bnv_pos == 0) {
                    sp.setVisibility(View.GONE);
                } else if (bnv_pos == 1) {
                    sp.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(E3_Home.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, days_for_sp);
                    sp.setAdapter(adapter);
                }

                sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinner_pos = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                Calendar cal = Calendar.getInstance();

                /////
                btn_from.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog picker = new TimePickerDialog(E3_Home.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                btn_from.setText(get24(hourOfDay, minute));
                                hr_from = hourOfDay;
                                min_from = minute;

                            }
                        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);

                        picker.show();
                    }
                });

                btn_to.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog picker = new TimePickerDialog(E3_Home.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                btn_to.setText(get24(hourOfDay, minute));
                                hr_to = hourOfDay;
                                min_to = minute;
                            }
                        }, cal.get(Calendar.HOUR_OF_DAY)+1, cal.get(Calendar.MINUTE), false);

                        picker.show();

                    }
                });


                ab.setView(view);

                ab.setNeutralButton("Cancel", null);

                ab.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        String time = "";
                        time = get24(hr_from, min_from) + " - " + get24(hr_to, min_to);

                        String day = "";


                        SharedPreferences pref = getSharedPreferences("pref_Mudit", MODE_PRIVATE);


                        int frag_pos = 0;
                        if (bnv_pos == 0) {
                            frag_pos = pref.getInt("frag_pos", 0);
                        } else if (bnv_pos == 1) {
                            frag_pos = spinner_pos;
                        }

                        switch (frag_pos) {
                            case 0:
                                day = "monday";
                                break;
                            case 1:
                                day = "tuesday";
                                break;
                            case 2:
                                day = "wednesday";
                                break;
                            case 3:
                                day = "thursday";
                                break;
                            case 4:
                                day = "friday";
                                break;
                            case 5:
                                day = "saturday";
                                break;
                            case 6:
                                day = "sunday";
                                break;
                            default:
                                day = "monday";
                        }


                        String color_pressed_string = color_pressed + "";
                        String qry;
                        qry = "INSERT INTO tbl_main (time, task, day, colora) VALUES ('" + time + "','" + task.getText().toString() + "','" + day + "', '" + color_pressed_string + "')";
                        db.execSQL(qry);


                        if (bnv_pos == 0) {
                            loadfrag(new fragment_list(), 1);
                        } else if (bnv_pos == 1) {
                            loadfrag(new fragment_chart(), 1);
                        }


                    }
                });

                ab.create().show();
            }
        });


    }

    public void changetheme(int theme, int colour) {
        MeowBottomNavigation meownav = findViewById(R.id.E3_bnv_2);
        Button btn = findViewById(R.id.E3_btn_2);

        setTheme(theme);

        if (colour == 1) {
            meownav.setSelectedIconColor(getColor(R.color.custom_color1));
            btn.setTextColor(getColor(R.color.custom_color1));
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.custom_color1));
            }
        } else if (colour == 2) {
            meownav.setSelectedIconColor(getColor(R.color.custom_color2));
            btn.setTextColor(getColor(R.color.custom_color2));
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.custom_color2));
            }
        } else if (colour == 3) {
            meownav.setSelectedIconColor(getColor(R.color.custom_color3));
            btn.setTextColor(getColor(R.color.custom_color3));
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.custom_color3));
            }
        } else if (colour == 4) {
            meownav.setSelectedIconColor(getColor(R.color.custom_color4));
            btn.setTextColor(getColor(R.color.custom_color4));
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.custom_color4));
            }
        } else if (colour == 5) {
            meownav.setSelectedIconColor(getColor(R.color.custom_color5));
            btn.setTextColor(getColor(R.color.custom_color5));
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.custom_color5));
            }
        } else if (colour == 6) {
            meownav.setSelectedIconColor(getColor(R.color.custom_color6));
            btn.setTextColor(getColor(R.color.custom_color6));
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.custom_color6));
            }
        }


    }

    public String get24(int hours_24, int minutes) {
//        if (hours_24 > 12) {
//            return (hours_24 - 12) + ":" + minutes + " PM";
//        } else {
//            return hours_24 + ":" + minutes + " AM";
//        }


        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hours_24);
        cal.set(Calendar.MINUTE, minutes);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");

        String s = timeFormat.format(cal.getTime());
        return s;
    }

    public void loadfrag(Fragment fragment, int flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (flag == 0) {
            ft.add(R.id.E3_container, fragment);
        } else if (flag == 1) {
            ft.replace(R.id.E3_container, fragment);
        }

        ft.commit();
    }


}