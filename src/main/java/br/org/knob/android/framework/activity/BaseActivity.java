package br.gov.rs.tce.inventario.activity;

import br.gov.rs.tce.inventario.R;
import br.gov.rs.tce.inventario.util.Util;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseActivity extends DebugActivity {
    protected DrawerLayout menuLateralLayout;

    public BaseActivity() {
        super();
    }

    protected Context getContext() {
        return this;
    }

    protected void setUpBarraFerramentas() {
        Toolbar barraFerramentas = (Toolbar) findViewById(R.id.barra_ferramentas);
        if(barraFerramentas != null)
            setSupportActionBar(barraFerramentas);
    }

    protected ActionBar getBarraFerramentas() {
        return getSupportActionBar();
    }


}