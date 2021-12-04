package com.streak.tweaks.fragments;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class StatusbarSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.streak_tweaks_statusbar);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.STREAK;
    }
}
