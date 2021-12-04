package com.streak.tweaks;

import android.os.Bundle;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class StreakTweaks extends SettingsPreferenceFragment {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.streak_tweaks);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.STREAK;
    }
}
