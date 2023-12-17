package com.timemanager.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import com.timemanager.communitypage_user;

import java.util.ArrayList;

public class fragment_subscriptions extends Fragment {


    public fragment_subscriptions() {
    }

    ListView list;
    ArrayList<String> comm_name, temp;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference ref;
    ProgressDialog pg;

    String a[] = {"dsa", "das"};


    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subscriptions, container, false);

        list = v.findViewById(R.id.frag_subs_list);

//        my_adapter adapter = new my_adapter(getContext(), string_com);
//        list.setAdapter(adapter);

        pg = new ProgressDialog(getContext());
        pg.setMessage("Loading");
        pg.show();
        pg.setCancelable(false);

        comm_name = new ArrayList<>();
        temp = new ArrayList<>();

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        ref = db.getReference().child("Community");

        fetchdata();

        return v;

    }


    private void fetchdata() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comm_name.clear();

                for (DataSnapshot mydata : snapshot.getChildren()) {
                    if (mydata != null) {
                        comm_name.add(mydata.getKey());
                    }

                    my_adapter adapter = new my_adapter(getContext(), comm_name.toArray(new String[comm_name.size()]));
                    list.setAdapter(adapter);
                }

                pg.dismiss();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public class my_adapter extends ArrayAdapter<String> {
        String[] mcom;
        Context mContext;

        public my_adapter(Context context, String[] arcom) {
            super(context, R.layout.item_for_subscription_page_communities, R.id.subs_community_name_txt, comm_name.toArray(new String[comm_name.size()]));

            mcom = arcom;
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
                row = inflater.inflate(R.layout.item_for_subscription_page_communities, parent, false);

                vholder = new VHolder(row);
                row.setTag(vholder);
            } else {
                vholder = (VHolder) row.getTag();

                if(temp.contains(vholder.join_btn.getTag().toString())){
                    vholder.join_btn.setChecked(true);
                } else {
                    vholder.join_btn.setOnCheckedChangeListener(null); // to exec next line of changing state without triggring listner
                    vholder.join_btn.setChecked(false);

                }
            }


            vholder.com_name.setText(mcom[position]);
            vholder.info_btn.setTag(mcom[position]);
            vholder.join_btn.setTag(mcom[position]);


//////////////////////////////////////////getting subscribed community names from firebase
            auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            ref = db.getReference().child("Users").child(sanitizeEmail(user.getEmail())).child("Community");
            ref.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    temp.clear();
                    for (DataSnapshot mydata : snapshot.getChildren()) {
                        String communityName = mydata.getKey();
                        boolean switchState = (boolean) mydata.getValue();
                        String viewHolderCommunityName = vholder.join_btn.getTag().toString();

                        if (communityName != null && communityName.equals(viewHolderCommunityName)) {
                            vholder.join_btn.setChecked(switchState);
                            temp.add(communityName);
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

//////////////////////////////////////////////////////////////////
            vholder.join_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        db = FirebaseDatabase.getInstance();
                        auth = FirebaseAuth.getInstance();
                        FirebaseUser user = auth.getCurrentUser();

                        try {
                            ref = db.getReference().child("Users").child(sanitizeEmail(user.getEmail())).child("Community");
                            ref.child(vholder.join_btn.getTag().toString()).setValue(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        db = FirebaseDatabase.getInstance();
                        auth = FirebaseAuth.getInstance();
                        FirebaseUser user = auth.getCurrentUser();

                        try {
                            ref = db.getReference().child("Users").child(sanitizeEmail(user.getEmail())).child("Community");
                            ref.child(vholder.join_btn.getTag().toString()).setValue(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


            vholder.info_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), communitypage_user.class);
                    intent.putExtra("commname", v.getTag().toString());
                    startActivity(intent);
                }
            });


            return row;
        }
    }


    public class VHolder {
        TextView com_name;
        Button info_btn;
        Switch join_btn;

        public VHolder(View r) {

            com_name = r.findViewById(R.id.subs_community_name_txt);
            join_btn = r.findViewById(R.id.subs_com_join_joined);
            info_btn = r.findViewById(R.id.subs_com_info);
        }

    }


    private String sanitizeEmail(String email) {
        return email != null ? email.replace(".", "_dot_").replace("@", "_at_") : "error";
    }

}