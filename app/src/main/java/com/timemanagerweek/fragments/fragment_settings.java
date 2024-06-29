package com.timemanagerweek.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.timemanagerweek.E6_studentlogin_page;
import com.timemanagerweek.E7_accountdetails;
import com.timemanagerweek.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class fragment_settings extends Fragment {

    public fragment_settings() {
        // Required empty public constructor
    }

    int color_pressed = 0;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    FirebaseAuth auth;
    CircleImageView profilepic;

    ImageView size_add, size_dec, size_add_task, size_dec_task;
    TextView task_box, task_text;
    AppCompatButton reset_btn;
    FirebaseDatabase db;
    DatabaseReference ref;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        size_add = view.findViewById(R.id.frag_settings_addbtn);
        size_dec = view.findViewById(R.id.frag_settings_decbtn);
        size_add_task = view.findViewById(R.id.frag_settings_addbtn_task);
        size_dec_task = view.findViewById(R.id.frag_settings_decbtn_task);
        task_box = view.findViewById(R.id.frag_settings_task_box);
        task_text = view.findViewById(R.id.frag_settings_task1);
        reset_btn = view.findViewById(R.id.fragment_settings_resetbtn);


        SharedPreferences pref = getContext().getSharedPreferences("pref_Mudit", getContext().MODE_PRIVATE);

        task_box.setHeight(pref.getInt("taskbox_height", 90));

        task_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, pref.getFloat("text_size", 40));

        size_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task_box.setHeight(task_box.getHeight()+10);

                SharedPreferences.Editor editor= pref.edit();
                editor.putInt("taskbox_height", task_box.getHeight());
                editor.commit();
            }
        });

        size_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task_box.setHeight(task_box.getHeight()-10);

                SharedPreferences.Editor editor= pref.edit();
                editor.putInt("taskbox_height", task_box.getHeight());
                editor.commit();
            }
        });

        size_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, task_text.getTextSize()+5);

                SharedPreferences.Editor editor= pref.edit();
                editor.putFloat("text_size", task_text.getTextSize());
                editor.commit();
            }
        });


        size_dec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, task_text.getTextSize()-5);

                SharedPreferences.Editor editor= pref.edit();
                editor.putFloat("text_size", task_text.getTextSize());
                editor.commit();
            }
        });


        profilepic = view.findViewById(R.id.frag_settings_profilepic);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getContext(), gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();



        if(acct!=null){
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            Uri uri = acct.getPhotoUrl();
            Picasso.get().load(uri).into(profilepic);
//            Toast.makeText(getContext(), "Welcome back "+personName+"!!", Toast.LENGTH_SHORT).show();
            //////////////////////////////////////////////////////////////////////////
        }

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), profilepic);
                popupMenu.getMenuInflater().inflate(R.menu.accountpopupmenu_forsettings, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.details){
                            Intent inte = new Intent(getContext(), E7_accountdetails.class);
                            inte.putExtra("uri", acct.getPhotoUrl().toString());
                            inte.putExtra("name", acct.getDisplayName());
                            inte.putExtra("email", acct.getEmail());
                            startActivity(inte);
                        } else if(item.getItemId() == R.id.signout){
                            signOut();
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });




        ToggleButton tb1, tb2, tb3, tb4, tb5, tb6;
        tb1 = view.findViewById(R.id.fragsetting_dialog_tb1);
        tb2 = view.findViewById(R.id.fragsetting_dialog_tb2);
        tb3 = view.findViewById(R.id.fragsetting_dialog_tb3);
        tb4 = view.findViewById(R.id.fragsetting_dialog_tb4);
        tb5 = view.findViewById(R.id.fragsetting_dialog_tb5);
        tb6 = view.findViewById(R.id.fragsetting_dialog_tb6);


        int b = pref.getInt("theme", 0);
        if(b == 1){
            tb1.setChecked(true);
            tb2.setChecked(false);
            tb3.setChecked(false);
            tb4.setChecked(false);
            tb5.setChecked(false);
            tb6.setChecked(false);
        } else if(b == 2){
            tb1.setChecked(false);
            tb2.setChecked(true);
            tb3.setChecked(false);
            tb4.setChecked(false);
            tb5.setChecked(false);
            tb6.setChecked(false);
        } else if(b == 3){
            tb1.setChecked(false);
            tb2.setChecked(false);
            tb3.setChecked(true);
            tb4.setChecked(false);
            tb5.setChecked(false);
            tb6.setChecked(false);
        } else if(b == 4){
            tb1.setChecked(false);
            tb2.setChecked(false);
            tb3.setChecked(false);
            tb4.setChecked(true);
            tb5.setChecked(false);
            tb6.setChecked(false);
        } else if(b == 5){
            tb1.setChecked(false);
            tb2.setChecked(false);
            tb3.setChecked(false);
            tb4.setChecked(false);
            tb5.setChecked(true);
            tb6.setChecked(false);
        } else if(b == 6){
            tb1.setChecked(false);
            tb2.setChecked(false);
            tb3.setChecked(false);
            tb4.setChecked(false);
            tb5.setChecked(false);
            tb6.setChecked(true);
        }


        tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    changetheme(R.style.Base_Theme_TimeManager1, 1);
                    color_pressed = 1;
                    tb2.setChecked(false);
                    tb3.setChecked(false);
                    tb4.setChecked(false);
                    tb5.setChecked(false);
                    tb6.setChecked(false);
                    SharedPreferences.Editor editor= pref.edit();
                    editor.putInt("theme", 1);
                    editor.commit();
                }
            }
        });

        tb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    changetheme(R.style.Base_Theme_TimeManager2, 2);
                    color_pressed = 2;
                    tb1.setChecked(false);
                    tb3.setChecked(false);
                    tb4.setChecked(false);
                    tb5.setChecked(false);
                    tb6.setChecked(false);
                    SharedPreferences.Editor editor= pref.edit();
                    editor.putInt("theme", 2);
                    editor.commit();
                }
            }
        });

        tb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    changetheme(R.style.Base_Theme_TimeManager3, 3);
                    color_pressed = 3;
                    tb2.setChecked(false);
                    tb1.setChecked(false);
                    tb4.setChecked(false);
                    tb5.setChecked(false);
                    tb6.setChecked(false);
                    SharedPreferences.Editor editor= pref.edit();
                    editor.putInt("theme", 3);
                    editor.commit();
                }
            }
        });

        tb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    changetheme(R.style.Base_Theme_TimeManager4, 4);
                    color_pressed = 4;
                    tb2.setChecked(false);
                    tb3.setChecked(false);
                    tb1.setChecked(false);
                    tb5.setChecked(false);
                    tb6.setChecked(false);
                    SharedPreferences.Editor editor= pref.edit();
                    editor.putInt("theme", 4);
                    editor.commit();
                }
            }
        });

        tb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    changetheme(R.style.Base_Theme_TimeManager5, 5);
                    color_pressed = 5;
                    tb2.setChecked(false);
                    tb3.setChecked(false);
                    tb4.setChecked(false);
                    tb1.setChecked(false);
                    tb6.setChecked(false);
                    SharedPreferences.Editor editor= pref.edit();
                    editor.putInt("theme", 5);
                    editor.commit();
                }
            }
        });

        tb6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    changetheme(R.style.Base_Theme_TimeManager6, 6);
                    color_pressed = 6;
                    tb2.setChecked(false);
                    tb3.setChecked(false);
                    tb4.setChecked(false);
                    tb5.setChecked(false);
                    tb1.setChecked(false);
                    SharedPreferences.Editor editor= pref.edit();
                    editor.putInt("theme", 6);
                    editor.commit();
                }
            }
        });
        ////////////////////////////////////////////////////////toogling colour buttons end
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        ref = db.getReference().child("Users").child(sanitizeEmail(user.getEmail())).child("personal_time_info").child("monday");


        ref = db.getReference().child("Users").child(sanitizeEmail(user.getEmail()));

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                ab.setTitle("Are you sure?");
                ab.setMessage("All Weekly data will be deleted");
                ab.setNegativeButton("Cancel", null);
                ab.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ref.child("personal_time_info").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Your New Week Starts Now", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getContext(), "Best of luck\nbe Productive this week", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), "Error Loading new Week", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

                ab.create().show();
            }
        });

        return view;
    }

    private String sanitizeEmail(String email) {
        return email != null ? email.replace(".", "_dot_").replace("@", "_at_") : "error";
    }

    public void changetheme(int theme, int colour) {
        MeowBottomNavigation meownav = getActivity().findViewById(R.id.E3_bnv_2);
        Button btn = getActivity().findViewById(R.id.E3_btn_2);

        getActivity().setTheme(theme);

        if(colour == 1) {
            meownav.setSelectedIconColor(getContext().getColor(R.color.custom_color1));
            btn.setTextColor(getContext().getColor(R.color.custom_color1));
            if (Build.VERSION.SDK_INT >= 21) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.custom_color1));
            }
        } else if(colour == 2) {
            meownav.setSelectedIconColor(getContext().getColor(R.color.custom_color2));
            btn.setTextColor(getContext().getColor(R.color.custom_color2));
            if (Build.VERSION.SDK_INT >= 21) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.custom_color2));
            }
        } else if(colour == 3) {
            meownav.setSelectedIconColor(getContext().getColor(R.color.custom_color3));
            btn.setTextColor(getContext().getColor(R.color.custom_color3));
            if (Build.VERSION.SDK_INT >= 21) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.custom_color3));
            }
        } else if(colour == 4) {
            meownav.setSelectedIconColor(getContext().getColor(R.color.custom_color4));
            btn.setTextColor(getContext().getColor(R.color.custom_color4));
            if (Build.VERSION.SDK_INT >= 21) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.custom_color4));
            }
        } else if(colour == 5) {
            meownav.setSelectedIconColor(getContext().getColor(R.color.custom_color5));
            btn.setTextColor(getContext().getColor(R.color.custom_color5));
            if (Build.VERSION.SDK_INT >= 21) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.custom_color5));
            }
        } else if(colour == 6) {
            meownav.setSelectedIconColor(getContext().getColor(R.color.custom_color6));
            btn.setTextColor(getContext().getColor(R.color.custom_color6));
            if (Build.VERSION.SDK_INT >= 21) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.custom_color6));
            }
        }


    }


    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                startActivity(new Intent(getActivity() , E6_studentlogin_page.class));
                getActivity().finish();
            }
        });
    }
}