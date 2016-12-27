package br.org.knob.android.framework.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import br.org.knob.android.framework.settings.KafSettings;


public class Util {
    public static final String TAG = "Util";

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
	
	/*public static void snack(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
    }*/
	
	
    public static void log(String tag, String mensagem) {
        if(KafSettings.getInstance().isLog()) {
            // TODO: registrar no BD
            Log.d(tag, mensagem);
        }
    }
}
