package com.timemanager.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.timemanager.R;
import com.timemanager.usermodal;

import java.util.ArrayList;


public class fragment_monday extends Fragment {


    public fragment_monday() {
        // Required empty public constructor
    }


    ListView list;
    //    SQLiteDatabase db;


    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseAuth auth;
    ArrayList<String> time_arraylist, task_arraylist, nodename_arraylist, color_arraylist;

    my_adapter myadapter;
    ProgressDialog pg;
    int color_pressed = 0;


    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_monday, container, false);


        list = v.findViewById(R.id.frag_monday_list);


//        db = getActivity().openOrCreateDatabase("dba_Mudit", getActivity().MODE_PRIVATE, null);
//        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_main (RegId Integer PRIMARY KEY AUTOINCREMENT, time varchar(200), task varchar(400), day varchar(200), colora varchar(200))");
//        filllist();

        pg = new ProgressDialog(getContext());
        pg.setMessage("Loading");
        pg.show();
        pg.setCancelable(false);
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        ref = db.getReference().child("Users").child(sanitizeEmail(user.getEmail())).child("personal_time_info").child("monday");

        time_arraylist = new ArrayList<>();
        task_arraylist = new ArrayList<>();
        nodename_arraylist = new ArrayList<>();
        color_arraylist = new ArrayList<>();


        fetchdata();
//        pg.dismiss();


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

                for (DataSnapshot mydata : snapshot.getChildren()) {
                    usermodal um = mydata.getValue(usermodal.class);
                    if (um != null) {
                        time_arraylist.add(um.getTime());
                        task_arraylist.add(um.getTask());
                        nodename_arraylist.add(mydata.getKey());
                        color_arraylist.add(um.getColor()+"");
                    }
                }

                if (time_arraylist.size() == task_arraylist.size()) {
                    try {
                        myadapter = new my_adapter(getContext(), task_arraylist.toArray(new String[task_arraylist.size()]), time_arraylist.toArray(new String[time_arraylist.size()]));
                        list.setAdapter(myadapter);

                        pg.dismiss();
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

                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                ab.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ref.child(nodename_arraylist.get(position)).removeValue();
                        fetchdata();
                    }
                });

                ab.setNegativeButton("Cancel", null);

                View view_inflate = getLayoutInflater().inflate(R.layout.e1_dialogbox_final, null);

                EditText task = view_inflate.findViewById(R.id.E1_dialog_task);
                Spinner sp = view_inflate.findViewById(R.id.E1_dialog_spinner_for_day);
                Button btn_from = view_inflate.findViewById(R.id.E1_dialog_btn_from);
                Button btn_to = view_inflate.findViewById(R.id.E1_dialog_btn_to);
                TextView txt_from = view_inflate.findViewById(R.id.E1_dialog_from_txt);
                TextView txt_to = view_inflate.findViewById(R.id.E1_dialog_to_txt);

                sp.setVisibility(View.GONE);
                btn_from.setVisibility(View.GONE);
                btn_to.setVisibility(View.GONE);
                txt_from.setVisibility(View.GONE);
                txt_to.setVisibility(View.GONE);


                task.setText(task_arraylist.get(position));

                ////////////////////////////////////////////////////////toogling colour buttons start
                ToggleButton tb1, tb2, tb3, tb4, tb5, tb6;
                tb1 = view_inflate.findViewById(R.id.E1_dialog_tb1);
                tb2 = view_inflate.findViewById(R.id.E1_dialog_tb2);
                tb3 = view_inflate.findViewById(R.id.E1_dialog_tb3);
                tb4 = view_inflate.findViewById(R.id.E1_dialog_tb4);
                tb5 = view_inflate.findViewById(R.id.E1_dialog_tb5);
                tb6 = view_inflate.findViewById(R.id.E1_dialog_tb6);

                color_pressed = 0;

                if (Integer.parseInt(color_arraylist.get(position).toString()) == 1) {
                    tb1.setChecked(true);
                } else if (Integer.parseInt(color_arraylist.get(position).toString()) == 2) {
                    tb2.setChecked(true);
                } else if (Integer.parseInt(color_arraylist.get(position).toString()) == 3) {
                    tb3.setChecked(true);
                } else if (Integer.parseInt(color_arraylist.get(position).toString()) == 4) {
                    tb4.setChecked(true);
                } else if (Integer.parseInt(color_arraylist.get(position).toString()) == 5) {
                    tb5.setChecked(true);
                } else if (Integer.parseInt(color_arraylist.get(position).toString()) == 6) {
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
                        usermodal user_new = new usermodal();
                        user_new.setDay("monday");
                        user_new.setColor(color_pressed+"");
                        user_new.setTime(time_arraylist.get(position));
                        user_new.setTask(task.getText().toString().trim());
                        ref.child(nodename_arraylist.get(position)).setValue(user_new);

                        fetchdata();
                        ///////////////////////////////////////
                    }
                });

                ab.create().show();

            }
        });
    }


//    @SuppressLint("Range")
//    public void filllist(){
//        Cursor cur;
//        String qry;
//        int n;
//
//
//        qry = "SELECT * FROM tbl_main WHERE day = '" + "monday" + "'";
//        cur = db.rawQuery(qry, null);
//
//        cur.moveToLast();
//        n = cur.getCount();
//        cur.moveToFirst();
//
//        String ids[], time[], task[];
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
//        my_adapter adapter = new my_adapter(getContext(), task, time);
//        list.setAdapter(adapter);
//
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
//
//                EditText ed = new EditText(getContext());
//                ed.setHint("Enter Task Here...");
//                ed.setText(task[position]);
//
//                ab.setView(ed);
//                ab.setTitle("Change Task");
//
//                ab.setNeutralButton("Cancel", null);
//
//                ab.setPositiveButton("Update", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String qur;
//                        qur = "UPDATE tbl_main SET task = '" + ed.getText().toString() + "' WHERE  RegId =  '" + ids[position] + "'";
//                        db.execSQL(qur);
//
//                        filllist();
//                    }
//                });
//
//                ab.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String qry = "DELETE FROM tbl_main WHERE RegId ='" + ids[position] + "'";
//                        db.execSQL(qry);
//
//                        filllist();
//                    }
//                });
//
//                ab.create().show();
//            }
//        });
//
//    }


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