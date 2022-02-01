package com.streak.tweaks.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.development.OverlayCategoryPreferenceController;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.development.OverlayCategoryPreferenceController;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;

import android.content.om.IOverlayManager;
import android.os.RemoteException;
import android.os.ServiceManager;

public class ThemesSettings extends DashboardFragment implements
        OnPreferenceChangeListener {

    public static final String TAG = "ThemesSettings";
    private SwitchPreference mQSPreference;
    IOverlayManager mOverlayManager;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.streak_tweaks_themes;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mQSPreference) {
            setOverlay("com.streak.a11qsoverlay", (Boolean) newValue);
            return true;
        }
        return false;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreatePreferences(savedInstanceState, rootKey);
        mOverlayManager = IOverlayManager.Stub.asInterface(ServiceManager.getService("overlay"));
        mQSPreference = findPreference("a11qs");
        try {
            mQSPreference.setChecked(mOverlayManager.getOverlayInfo("com.streak.a11qsoverlay", UserHandle.USER_CURRENT).isEnabled());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mQSPreference.setOnPreferenceChangeListener(this);
    }

    private void setOverlay(String overlay, boolean status) {
        try {
            mOverlayManager.setEnabled(overlay, status, UserHandle.USER_CURRENT);
        } catch (RemoteException | IllegalStateException | SecurityException e) {
            e.printStackTrace();
        }
    }


    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle(), this);
    }

    private static List<AbstractPreferenceController> buildPreferenceControllers(
            Context context, Lifecycle lifecycle, Fragment fragment) {
        final List<AbstractPreferenceController> controllers = new ArrayList<>();
        controllers.add(new OverlayCategoryPreferenceController(context,
                "android.theme.customization.font"));
        controllers.add(new OverlayCategoryPreferenceController(context,
                "android.theme.customization.icon_pack"));
        controllers.add(new OverlayCategoryPreferenceController(context,
                "android.theme.customization.adaptive_icon_shape"));
        controllers.add(new OverlayCategoryPreferenceController(context,
                "com.streak.qs"));
        return controllers;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.STREAK;
    }
}
