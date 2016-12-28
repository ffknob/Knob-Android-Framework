package br.org.knob.android.framework.settings;

import android.content.SharedPreferences;

import java.util.HashMap;

import br.org.knob.android.framework.model.Setting;
import br.org.knob.android.framework.util.Util;

public abstract class AbstractSettings {
    public static final String TAG = "AbstractSettings";

    public static final String PARAM_SETTINGS_RESOURCE_ID = "settings-resource-id";
    public static final String PARAM_SETTINGS_IS_INITIALIZED = "settings-is-initialized";

    protected HashMap<String, Object> settings;

    public AbstractSettings() {
        settings = new HashMap();
    }

    public abstract void initialize();

    public abstract int getSettingsResource();

    public Object get(String key) {
        if (settings != null) {
            if (settings.containsKey(key)) {
                Setting setting = (Setting) settings.get(key);

                if (setting != null) {
                    return setting.getValue();
                }
            }
        }

        return null;
    }

    public void set(String key, Object value) {
        if(settings != null && key != null) {
            settings.put(key, new Setting(key, value));
        }
    }

    public HashMap<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(HashMap<String, Object> settings) {
        this.settings = settings;
    }
}
