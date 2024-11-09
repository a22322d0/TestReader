package com.example.testreader.ui.notifications;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.testreader.R;

public class NotificationsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_notifications, rootKey);
    }
}