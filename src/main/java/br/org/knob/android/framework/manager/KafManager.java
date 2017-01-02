package br.org.knob.android.framework.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import br.org.knob.android.framework.adapter.KafSettingsAdapter;
import br.org.knob.android.framework.database.DatabaseHelper;
import br.org.knob.android.framework.service.SettingsService;
import br.org.knob.android.framework.settings.KafSettings;
import br.org.knob.android.framework.util.Util;

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
        // Settings
        initializeSettings();

        // Database
        initializeDb();
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

    private void initializeDb() {
        // Framework database
    }
}
