package br.org.knob.android.framework.adapter;

import android.content.SharedPreferences;

import br.org.knob.android.framework.settings.KafSettings;

public class KafSettingsAdapter extends AbstractSettingsAdapter {
    protected static final String TAG = "KafSettingsAdapter";

    @Override
    public KafSettings getSettings(SharedPreferences sharedPreferences) {
        KafSettings kafSettings = KafSettings.getInstance();

        if(sharedPreferences != null) {
            kafSettings.set(KafSettings.SETTINGS_LOG, sharedPreferences.getBoolean(KafSettings.SETTINGS_LOG, KafSettings.DEFAULT_LOG));
            kafSettings.set(KafSettings.SETTINGS_ONLINE, sharedPreferences.getBoolean(KafSettings.SETTINGS_ONLINE, KafSettings.DEFAULT_ONLINE));
        }

        return kafSettings;
    }
}