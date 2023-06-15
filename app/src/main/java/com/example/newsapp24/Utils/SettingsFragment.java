package com.example.newsapp24.Utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.example.newsapp24.Activities.MainActivity;
import com.example.newsapp24.Activities.SaveForLaterActivity;
import com.example.newsapp24.Activities.SearchActivity;
import com.example.newsapp24.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SettingsFragment extends PreferenceFragmentCompat {

    BottomNavigationView bottomNavigationView;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_settings);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch(item.getItemId()) {
                    case R.id.bottom_nav_home:
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().overridePendingTransition(0,0);
                        return true;
                    case R.id.bottom_nav_search:
                        startActivity(new Intent(getActivity(), SearchActivity.class));
                        getActivity().overridePendingTransition(0,0);
                        return true;
                    case R.id.bottom_nav_save_for_later:
                        startActivity(new Intent(getActivity(), SaveForLaterActivity.class));
                        getActivity().overridePendingTransition(0,0);
                        return true;
                    case R.id.bottom_nav_settings:

                        return true;

                }
                return false;
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}