package br.org.knob.android.framework.settings;

import java.util.HashMap;

import br.org.knob.android.framework.R;
import br.org.knob.android.framework.model.Setting;

public class KafSettings extends AbstractSettings {
    public static final String TAG = "KafSettings";

    // Framework kafSettings
    public static final String SETTINGS_ONLINE = "kaf-settings-online";
    public static final String SETTINGS_LOG = "kaf-settings-log";

    // Defaults
    public static final Boolean DEFAULT_ONLINE = true;
    public static final Boolean DEFAULT_LOG = true;

    private static KafSettings kafSettings;

    private KafSettings() {
        super();

        Setting isOnline = new Setting<Boolean>(SETTINGS_ONLINE, DEFAULT_ONLINE);
        settings.put(SETTINGS_ONLINE, isOnline);

        Setting isLog = new Setting<Boolean>(SETTINGS_LOG, DEFAULT_LOG);
        settings.put(SETTINGS_LOG, isLog);
    }

    public static KafSettings getInstance() {
        if(kafSettings == null) {
            kafSettings = new KafSettings();
        }

        return kafSettings;
    }

    public Boolean isOnline() {
        if(settings != null) {
            Setting setting = (Setting) settings.get(SETTINGS_ONLINE);

            if(setting != null) {
                return (Boolean) setting.getValue();
            }
        }

        return null;
    }

    public Boolean isLog() {
        if(settings != null) {
            Setting setting = (Setting) settings.get(SETTINGS_LOG);

            if(setting != null) {
                return (Boolean) setting.getValue();
            }
        }

        return null;
    }

    @Override
    public int getSettingsResource() {
        return R.xml.kaf_settings;
    }
}
