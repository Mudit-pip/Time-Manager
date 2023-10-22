package com.timemanager.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.timemanager.R;


public class fragment_thursday extends Fragment {

    public fragment_thursday() {


    }

    ListView list;
    SQLiteDatabase db;

    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_thursday, container, false);

        list = v.findViewById(R.id.frag_thursday_list);


        db = getActivity().openOrCreateDatabase("dba_Mudit", getActivity().MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_main (RegId Integer PRIMARY KEY AUTOINCREMENT, time varchar(200), task varchar(400), day varchar(200), colora varchar(200))");

        filllist();



        return v;
    }



    @SuppressLint("Range")
    public void filllist(){
        Cursor cur;
        String qry;
        int n;


        qry = "SELECT * FROM tbl_main WHERE day = '" + "thursday" + "'";
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

        my_adapter adapter = new my_adapter(getContext(), task, time);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());

                EditText ed = new EditText(getContext());
                ed.setHint("Enter Task Here...");
                ed.setText(task[position]);

                ab.setView(ed);
                ab.setTitle("Change Task");

                ab.setNeutralButton("Cancel", null);

                ab.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String qur;
                        qur = "UPDATE tbl_main SET task = '" + ed.getText().toString() + "' WHERE  RegId =  '" + ids[position] + "'";
                        db.execSQL(qur);

                        filllist();
                    }
                });

                ab.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String qry = "DELETE FROM tbl_main WHERE RegId ='" + ids[position] + "'";
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