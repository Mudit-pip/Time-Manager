package com.timemanagerweek.fragments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.timemanagerweek.AlarmRecevier;
import com.timemanagerweek.R;
import com.timemanagerweek.usermodal;

import java.util.ArrayList;
import java.util.Calendar;


public class fragment_saturday extends Fragment {

    ListView list;

    FirebaseDatabase db;
    DatabaseReference ref;
    ArrayList<String> time_arraylist, task_arraylist,  nodename_arraylist, color_arraylist;
    ArrayList<String> alarm_list, hr_from, min_from;
    AlarmManager alarmManager;
    FirebaseAuth auth;
    my_adapter myadapter;
    ProgressBar pb;
    int color_pressed=0;
    TextView emptytext;

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_saturday, container, false);
        list = v.findViewById(R.id.frag_saturday_list);
        pb = v.findViewById(R.id.frag_saturday_pb);
        emptytext = v.findViewById(R.id.frag_saturday_emptytext);


        pb.setVisibility(View.VISIBLE);
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        ref = db.getReference().child("Users").child(sanitizeEmail(user.getEmail())).child("personal_time_info").child("saturday");

        time_arraylist = new ArrayList<>();
        task_arraylist = new ArrayList<>();
        nodename_arraylist = new ArrayList<>();
        color_arraylist = new ArrayList<>();
        alarm_list = new ArrayList<>();
        hr_from = new ArrayList<>();
        min_from = new ArrayList<>();

        fetchdata();

        return v;
    }

    private String sanitizeEmail(String email) {
        return email != null ? email.replace(".", "_dot_").replace("@", "_at_") : "error";
    }


    private void fetchdata() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                time_arraylist.clear();
                task_arraylist.clear();
                nodename_arraylist.clear();
                color_arraylist.clear();
                alarm_list.clear();
                hr_from.clear();
                min_from.clear();

                for (DataSnapshot mydata : snapshot.getChildren()) {
                    usermodal um = mydata.getValue(usermodal.class);
                    if (um != null) {
                        time_arraylist.add(um.getTime());
                        task_arraylist.add(um.getTask());
                        nodename_arraylist.add(mydata.getKey());
                        color_arraylist.add(um.getColor()+"");
                        alarm_list.add(um.getRequestcode());

                        String s = um.getTime(); //11:27 am - 12:35 pm
                        String a[] = s.split(" "); // 11:27{0} ...
                        String b[] = a[0].split(":"); //11{0}   27{1} ...
                        hr_from.add(b[0]);
                        min_from.add(b[1]);
                    }
                }

                if(time_arraylist.size() == 0){
                    emptytext.setVisibility(View.VISIBLE);
                }

                if (time_arraylist.size() == task_arraylist.size()) {
                    try {
                        myadapter = new my_adapter(getContext(), task_arraylist.toArray(new String[task_arraylist.size()]), time_arraylist.toArray(new String[time_arraylist.size()]));
                        list.setAdapter(myadapter);

                        pb.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                android.app.AlertDialog.Builder ab = new AlertDialog.Builder(getContext());


                ab.setNegativeButton("Cancel", null);

                View view_inflate = getLayoutInflater().inflate(R.layout.e1_dialogbox_final, null);

                EditText task = view_inflate.findViewById(R.id.E1_dialog_task);
                Spinner sp = view_inflate.findViewById(R.id.E1_dialog_spinner_for_day);
                Button btn_from = view_inflate.findViewById(R.id.E1_dialog_btn_from);
                Button btn_to = view_inflate.findViewById(R.id.E1_dialog_btn_to);
                TextView txt_from = view_inflate.findViewById(R.id.E1_dialog_from_txt);
                TextView txt_to = view_inflate.findViewById(R.id.E1_dialog_to_txt);
                CheckBox checkbox_alarm = view_inflate.findViewById(R.id.E1_dialog_checkbox_alarm);


                sp.setVisibility(View.GONE);
                btn_from.setVisibility(View.GONE);
                btn_to.setVisibility(View.GONE);
                txt_from.setVisibility(View.GONE);
                txt_to.setVisibility(View.GONE);

                task.setText(task_arraylist.get(position));
                if(Integer.parseInt(alarm_list.get(position)) == -1){
                    checkbox_alarm.setChecked(false);
                } else {
                    checkbox_alarm.setChecked(true);
                }

                ////////////////////////////////////////////////////////toogling colour buttons start
                ToggleButton tb1, tb2, tb3, tb4, tb5, tb6;
                tb1 = view_inflate.findViewById(R.id.E1_dialog_tb1);
                tb2 = view_inflate.findViewById(R.id.E1_dialog_tb2);
                tb3 = view_inflate.findViewById(R.id.E1_dialog_tb3);
                tb4 = view_inflate.findViewById(R.id.E1_dialog_tb4);
                tb5 = view_inflate.findViewById(R.id.E1_dialog_tb5);
                tb6 = view_inflate.findViewById(R.id.E1_dialog_tb6);

                color_pressed = 0;

                if(Integer.parseInt(color_arraylist.get(position).toString()) == 1){
                    tb1.setChecked(true);
                } else if(Integer.parseInt(color_arraylist.get(position).toString()) == 2){
                    tb2.setChecked(true);
                } else if(Integer.parseInt(color_arraylist.get(position).toString()) == 3){
                    tb3.setChecked(true);
                } else if(Integer.parseInt(color_arraylist.get(position).toString()) == 4){
                    tb4.setChecked(true);
                } else if(Integer.parseInt(color_arraylist.get(position).toString()) == 5){
                    tb5.setChecked(true);
                } else if(Integer.parseInt(color_arraylist.get(position).toString()) == 6){
                    tb6.setChecked(true);
                }
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


                ab.setView(view_inflate);

                ab.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ////////////////////////////////////////

                        //seeting up alarms
                        int uniqueRequestCode = Integer.parseInt(alarm_list.get(position));
                        if(Integer.parseInt(alarm_list.get(position)) == -1 && checkbox_alarm.isChecked()){
                            //schedule alarm
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                            cal.set(Calendar.HOUR, Integer.parseInt(hr_from.get(position)));
                            cal.set(Calendar.MINUTE, Integer.parseInt(min_from.get(position)));
                            cal.set(Calendar.SECOND, 0);
                            uniqueRequestCode = (int) System.currentTimeMillis(); // Generate a unique request code
                            uniqueRequestCode = setAlarm(cal, task.getText().toString(), uniqueRequestCode);
                        } else if(Integer.parseInt(alarm_list.get(position)) != -1 && !checkbox_alarm.isChecked()) {
                            cancelAlarm(Integer.parseInt(alarm_list.get(position)));
                            uniqueRequestCode = -1;
                        }


                        usermodal user_new = new usermodal();
                        user_new.setDay("saturday");
                        user_new.setColor(color_pressed+"");
                        user_new.setTime(time_arraylist.get(position));
                        user_new.setTask(task.getText().toString().trim());
                        user_new.setRequestcode(uniqueRequestCode+"");
                        ref.child(nodename_arraylist.get(position)).setValue(user_new);
                        fetchdata();
                        ///////////////////////////////////////
                    }
                });

                ab.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelAlarm(Integer.parseInt(alarm_list.get(position)));
                        ref.child(nodename_arraylist.get(position)).removeValue();
                        fetchdata();
                    }
                });

                ab.create().show();

            }
        });
    }


    private int setAlarm(Calendar cal, String desc, int requestCode) {
        Calendar now = Calendar.getInstance();
        if (cal.before(now)) {
            Toast.makeText(getContext(), "Cannot set an alarm for a past time\nAlarm not set", Toast.LENGTH_SHORT).show();
            return -1;
        }

        PendingIntent pendingIntent;

        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getContext(), AlarmRecevier.class);
        intent.putExtra("desc", desc);
        pendingIntent = PendingIntent.getBroadcast(getContext(), requestCode, intent, PendingIntent.FLAG_IMMUTABLE);

        long nextTrigger = cal.getTimeInMillis();
        AlarmManager.AlarmClockInfo ac = new AlarmManager.AlarmClockInfo(nextTrigger, null);
        alarmManager.setAlarmClock(ac, pendingIntent);

        Toast.makeText(getContext(), "Alarm set", Toast.LENGTH_SHORT).show();

        return requestCode;
    }

    private void cancelAlarm(int requestCode) {
        if (requestCode != -1) {
            Intent intent = new Intent(getContext(), AlarmRecevier.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            if (alarmManager == null) {
                alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            }

            alarmManager.cancel(pendingIntent);

            Toast.makeText(getContext(), "Alarm canceled", Toast.LENGTH_SHORT).show();
        }
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
            if(Integer.parseInt(alarm_list.get(position)) == -1){
                vholder.alarm_img.setImageResource(R.drawable.baseline_alarm_off_24);
            } else {
                vholder.alarm_img.setImageResource(R.drawable.baseline_alarm_on_24);
            }


            return row;
        }
    }


    public class VHolder {
        TextView txt_time, txt_task;
        ImageView alarm_img;

        public VHolder(View r) {

            txt_time = r.findViewById(R.id.e1_mainlist_time_txt);
            txt_task = r.findViewById(R.id.e1_mainlist_task_txt);
            alarm_img = r.findViewById(R.id.e1_mainlit_alarm_img);
        }

    }


}