package br.org.knob.android.framework.settings;

public class Settings {
    public static final String TAG = "Settings";

    public static final Boolean DEFAULT_ONLINE = false;
    public static final Boolean DEFAULT_LOG = true;

    private Boolean isOnline;
    private Boolean isLog;

    private static Settings settings;

    private Settings() {
        this.isOnline = DEFAULT_ONLINE;
        this.isLog = DEFAULT_LOG;
    }

    private Settings(Boolean isOnline, Boolean isLog) {
        this.isOnline = isOnline != null ? isOnline : DEFAULT_ONLINE;
        this.isLog = isLog != null ? isLog : DEFAULT_LOG;
    }

    public static Settings getInstance() {
        if(settings == null) {
            settings = new Settings();
        }

        return settings;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Boolean isLog() {
        return isLog;
    }

    public void setLog(Boolean log) {
        isLog = log;
    }
}
