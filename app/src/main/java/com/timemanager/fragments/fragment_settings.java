package com.timemanager.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.timemanager.R;


public class fragment_settings extends Fragment {

    public fragment_settings() {
        // Required empty public constructor
    }

    int color_pressed = 0;
    NumberPicker np;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        np = view.findViewById(R.id.fragment_settings_np);
        np.setMinValue(1);
        np.setMaxValue(12);
        np.setWrapSelectorWheel(true);


        SharedPreferences pref = getContext().getSharedPreferences("pref_Mudit", getContext().MODE_PRIVATE);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                SharedPreferences.Editor editor= pref.edit();
                editor.putInt("start_time", newVal);
                editor.commit();
            }
        });

        int new_k = pref.getInt("start_time", 8);
        np.setValue(new_k);



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

        return view;
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

}