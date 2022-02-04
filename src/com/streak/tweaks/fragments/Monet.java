package com.streak.tweaks.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.om.IOverlayManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import android.provider.Settings;
import com.android.settings.R;
import com.streak.settings.color.WallpaperColorActivity;
import android.content.Intent;

import java.util.Arrays;
import java.util.HashSet;

import com.android.settings.SettingsPreferenceFragment;

public class Monet extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    IOverlayManager mOverlayManager;
    private SwitchPreference mPitchPreference;
    private Preference mWallPreference;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.monet;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreatePreferences(savedInstanceState, rootKey);
        mOverlayManager = IOverlayManager.Stub.asInterface(ServiceManager.getService("overlay"));
        mPitchPreference = findPreference("pitch_theme");
        mWallPreference = findPreference("monet_wall");
        try {
            mPitchPreference.setChecked(mOverlayManager.getOverlayInfo("com.radiant.pitchsystem", UserHandle.USER_CURRENT).isEnabled());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mPitchPreference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mPitchPreference) {
            setOverlay("com.radiant.pitchsystem", (Boolean) newValue);
            setOverlay("com.radiant.pitchsettings", (Boolean) newValue);
            setOverlay("com.radiant.pitchsystemui", (Boolean) newValue);
            return true;
        } else if(preference == mWallPreference){
            Intent intent = new Intent(getActivity(), WallpaperColorActivity.class);
            getActivity().startActivity(intent);
            return true;
        }
        return false;
    }

    private void setOverlay(String overlay, boolean status) {
        try {
            mOverlayManager.setEnabled(overlay, status, UserHandle.USER_CURRENT);
        } catch (RemoteException | IllegalStateException | SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.STREAK;
    }
}
