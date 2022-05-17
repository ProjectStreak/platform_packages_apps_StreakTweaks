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

import com.streak.support.preferences.SecureSettingSwitchPreference;

public class StatusbarSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String SYSTEMUI_PACKAGE = "com.android.systemui";
    private static final String CONFIG_RESOURCE_NAME = "flag_combined_status_bar_signal_icons";

    private static final String COBINED_STATUSBAR_ICONS = "show_combined_status_bar_signal_icons";

    private SecureSettingSwitchPreference mCombinedIcons;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.streak_tweaks_statusbar);
        final ContentResolver resolver = getActivity().getContentResolver();

        mCombinedIcons = (SecureSettingSwitchPreference)
                findPreference(COBINED_STATUSBAR_ICONS);
        Resources sysUIRes = null;
        boolean def = false;
        int resId = 0;
        try {
            sysUIRes = getActivity().getPackageManager()
                    .getResourcesForApplication(SYSTEMUI_PACKAGE);
        } catch (Exception ignored) {
            // If you don't have system UI you have bigger issues
        }
        if (sysUIRes != null) {
            resId = sysUIRes.getIdentifier(
                    CONFIG_RESOURCE_NAME, "bool", SYSTEMUI_PACKAGE);
            if (resId != 0) def = sysUIRes.getBoolean(resId);
        }
        boolean enabled = Settings.Secure.getInt(resolver,
                COBINED_STATUSBAR_ICONS, def ? 1 : 0) == 1;
        mCombinedIcons.setChecked(enabled);
        mCombinedIcons.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mCombinedIcons) {
            boolean enabled = (boolean) objValue;
            Settings.Secure.putInt(resolver,
                    COBINED_STATUSBAR_ICONS, enabled ? 1 : 0);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.STREAK;
    }
}
