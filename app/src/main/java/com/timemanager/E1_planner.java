package com.timemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class E1_planner extends AppCompatActivity {

    Button add;
    boolean min_checked = false;
    boolean hr_checked = false;
    SQLiteDatabase db;

    ListView mainlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e1_planner);


        add = findViewById(R.id.E1_addbtn);
        mainlist = findViewById(R.id.E1_main_list);


        db = openOrCreateDatabase("db_Mudit", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_main (RegId Integer PRIMARY KEY AUTOINCREMENT, time varchar(200), task varchar(400), day varchar(200))");


        filllist();


//        add.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("MissingInflatedId")
//            @Override
//            public void onClick(View v) {
//
//                AlertDialog.Builder ab = new AlertDialog.Builder(E1_planner.this);
//                View view = getLayoutInflater().inflate(R.layout.e1_dialogbox_final, null);
//
//                EditText task = view.findViewById(R.id.E1_dialog_task);
//                TimePicker tp_from, tp_to;
//                tp_from = view.findViewById(R.id.E1_tp_from);
//                tp_to = view.findViewById(R.id.E1_tp_to);
//
//                View minute = tp_from.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
//                NumberPicker minutePicker = (NumberPicker) minute;
//                minutePicker.setMinValue(0);
//                minutePicker.setMaxValue(1);
//                minutePicker.setDisplayedValues(new String[]{"00", "30"});
//
//                View min = tp_to.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
//                NumberPicker minutePicker_to = (NumberPicker) min;
//                minutePicker_to.setMinValue(0);
//                minutePicker_to.setMaxValue(1);
//                minutePicker_to.setDisplayedValues(new String[]{"00", "30"});
//
//
//                ab.setView(view);
//
//                ab.setNeutralButton("Cancel", null);
//
//                ab.setPositiveButton("Add", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
//                        String time = "";
//
//                        Calendar cal = Calendar.getInstance();
//                        cal.set(Calendar.HOUR_OF_DAY, tp_from.getHour());
//                        cal.set(Calendar.MINUTE, tp_from.getMinute()*30);
//                        time = timeFormat.format(cal.getTime()) + " - ";
//
//                        cal.set(Calendar.HOUR_OF_DAY, tp_to.getHour());
//                        cal.set(Calendar.MINUTE, tp_to.getMinute()*30);
//
//                        time += timeFormat.format(cal.getTime());
//
//
//                        String qry;
//                        qry = "INSERT INTO tbl_main (time, task) VALUES ('" + time + "','" + task.getText().toString() + "')";
//                        db.execSQL(qry);
//
//
//                        filllist();
//                    }
//                });
//
//                ab.create().show();
//            }
//        });



    }


    @SuppressLint("Range")
    public void filllist() {

        Cursor cur;
        String qry;
        int n;


        qry = "SELECT * FROM tbl_main";
        cur = db.rawQuery(qry, null);

        cur.moveToLast();
        n = cur.getCount();
        cur.moveToFirst();

        String[] time = new String[n];
        String[] task = new String[n];

        for (int i = 0; i < n; i++) {
            time[i] = cur.getString(cur.getColumnIndex("time"));
            task[i] = cur.getString(cur.getColumnIndex("task"));
            cur.moveToNext();
        }

        my_adapter adapter = new my_adapter(E1_planner.this, task, time);
        mainlist.setAdapter(adapter);


        mainlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder ab = new AlertDialog.Builder(E1_planner.this);

                EditText ed = new EditText(E1_planner.this);
                ed.setHint("Enter Task Here...");
                ed.setText(task[position]);

                ab.setView(ed);
                ab.setTitle("Change Task");

                ab.setNeutralButton("Cancel", null);

                ab.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String qur;
                        qur = "UPDATE tbl_main SET task = '" + ed.getText().toString() + "' WHERE  task =  '" + task[position] + "'";
                        db.execSQL(qur);

                        filllist();
                    }
                });

                ab.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String qry = "DELETE FROM tbl_main WHERE task ='" + task[position] + "'";
                        db.execSQL(qry);

                        filllist();
                    }
                });

                ab.create().show();
            }
        });


    }


    public class my_adapter extends ArrayAdapter<String> {
        String[] mtask;
        String[] mtime;
        Context mContext;

        public my_adapter(Context context, String[] artask, String[] artime) {
            super(context, R.layout.main_item_for_list_task_time, R.id.e1_mainlist_task_txt, artask);

            mtask = artask;
            mtime = artime;
            mContext = context;

        }


        @SuppressLint("ResourceAsColor")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            VHolder vholder;

            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.main_item_for_list_task_time, parent, false);

                vholder = new VHolder(row);
                row.setTag(vholder);
            } else {
                vholder = (VHolder) row.getTag();
            }


            vholder.txt_time.setText(mtime[position]);
            vholder.txt_task.setText(mtask[position]);


            return row;
        }
    }

    public class VHolder {
        TextView txt_time, txt_task;

        public VHolder(View r) {

            txt_time = r.findViewById(R.id.e1_mainlist_time_txt);
            txt_task = r.findViewById(R.id.e1_mainlist_task_txt);
        }

    }


}