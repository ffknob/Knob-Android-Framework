package br.org.knob.android.framework.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import br.org.knob.android.framework.adapter.SettingsAdapter;
import br.org.knob.android.framework.settings.KafSettings;

public class SettingsService extends GenericService {
    protected static final String TAG = "SettingsService";

    private Context context;
    private KafSettings kafSettings;

    public SettingsService(Context context) {
        this.context = context;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        kafSettings = SettingsAdapter.getSettings(sharedPreferences);
    }

    public KafSettings getKafSettings() {
        return kafSettings;
    }

    public void setKafSettings(KafSettings kafSettings) {
        this.kafSettings = kafSettings;
    }

}
