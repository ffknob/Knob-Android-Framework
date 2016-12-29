package br.org.knob.android.framework.manager;

import android.content.Context;

import br.org.knob.android.framework.adapter.KafSettingsAdapter;
import br.org.knob.android.framework.service.SettingsService;
import br.org.knob.android.framework.settings.KafSettings;

public class KafManager {
    public static final String TAG = "KafManager";

    private Context context;
    private static KafManager kafManager;

    private KafManager(Context context) {
        this.context = context;
    }

    public static KafManager getInstance(Context context) {
        if(kafManager == null) {
            kafManager = new KafManager(context);
        }

        return kafManager;
    }

    public void initialize() {
        initializeSettings();
    }

    private void initializeSettings() {
        // Framework settings
        KafSettings kafSettings = KafSettings.getInstance();
        SettingsService kafSettingsService = new SettingsService(context, new KafSettingsAdapter());
        if(!kafSettingsService.isInitialized()) {
            kafSettings.initialize();
            kafSettingsService.commitToSharedPreferences();
        }
    }
}
