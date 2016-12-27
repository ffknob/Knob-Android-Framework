package br.org.knob.android.framework.settings;

import java.util.HashMap;

public abstract class AbstractSettings {
    public static final String TAG = "AbstractSettings";

    public static final String PARAM_SETTINGS_RESOURCE_ID = "settings-resource-id";

    protected HashMap settings;

    public AbstractSettings() {
        settings = new HashMap();
    }

    public abstract int getSettingsResource();

    public void set(String key, Object value) {
        if(settings != null) {
            if(settings.containsKey(key)) {
                settings.put(key, value);
            }
        }
    }

    public HashMap getSettings() {
        return settings;
    }

    public void setSettings(HashMap settings) {
        this.settings = settings;
    }
}
