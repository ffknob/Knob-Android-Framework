package br.org.knob.android.framework.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import br.org.knob.android.framework.R;
import br.org.knob.android.framework.settings.AbstractSettings;
import br.org.knob.android.framework.util.Util;

public class SettingsFragment extends PreferenceFragment {
    protected static final String TAG = "SettingsFragment";

    private AbstractSettings settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int settingsResource = bundle.getInt(AbstractSettings.PARAM_SETTINGS_RESOURCE_ID, 0);

            if(settingsResource == 0) {
                Util.log(TAG, getResources().getString(R.string.kaf_settings_error_resource_not_found));
            }
            else {
                addPreferencesFromResource(settingsResource);
            }
        }


    }
}