package br.org.knob.android.framework.adapter;

import android.content.SharedPreferences;

import br.org.knob.android.framework.settings.AbstractSettings;

public abstract class AbstractSettingsAdapter {
    protected static final String TAG = "AbstractSettingsAdapter";

    public abstract AbstractSettings getSettings(SharedPreferences sharedPreferences);
}
