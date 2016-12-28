package br.org.knob.android.framework.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

import br.org.knob.android.framework.adapter.AbstractSettingsAdapter;
import br.org.knob.android.framework.model.Setting;
import br.org.knob.android.framework.settings.AbstractSettings;
import br.org.knob.android.framework.util.Util;

public class SettingsService extends GenericService {
    protected static final String TAG = "SettingsService";

    private Context context;
    private AbstractSettings settings;
    private AbstractSettingsAdapter settingsAdapter;

    public SettingsService(Context context, AbstractSettingsAdapter settingsAdapter) {
        this.context = context;
        this.settingsAdapter = settingsAdapter;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        settings = settingsAdapter.getSettings(sharedPreferences);
    }

    public AbstractSettings getSettings() {
        return settings;
    }

    public void setSettings(AbstractSettings settings) {
        this.settings = settings;
    }

    public AbstractSettingsAdapter getSettingsAdapter() {
        return settingsAdapter;
    }

    public void setSettingsAdapter(AbstractSettingsAdapter settingsAdapter) {
        this.settingsAdapter = settingsAdapter;
    }

    public boolean isInitialized() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if(sharedPreferences.contains(AbstractSettings.PARAM_SETTINGS_IS_INITIALIZED)){
            return sharedPreferences.getBoolean(AbstractSettings.PARAM_SETTINGS_IS_INITIALIZED, false);
        }

        return false;
    }

    public void commitToSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if(sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            for(String key : settings.getSettings().keySet()) {
                 if(settings.get(key) instanceof String) {
                    editor.putString(key, (String) settings.get(key));
                } else if(settings.get(key) instanceof Integer) {
                    editor.putInt(key, (Integer) settings.get(key));
                } else if(settings.get(key) instanceof Long) {
                    editor.putLong(key, (Long) settings.get(key));
                }  else if(settings.get(key) instanceof Float) {
                    editor.putFloat(key, (Float) settings.get(key));
                } else if(settings.get(key) instanceof Boolean) {
                    editor.putBoolean(key, (Boolean) settings.get(key));
                } else {
                    Util.log(TAG, "Could not determine type of setting setting");
                }
            }

            editor.commit();
        }
    }

}
