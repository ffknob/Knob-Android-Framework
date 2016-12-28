package br.org.knob.android.framework.activity;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

public class BaseActivity extends DebugActivity {
    private static final String TAG = "BaseActivity";

    public BaseActivity() {
        super();
    }

    protected Context getContext() {
        return this;
    }

    protected void setUpToolbar(Toolbar toolbar) {
        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    protected ActionBar getToolbar() {
        return getSupportActionBar();
    }
}