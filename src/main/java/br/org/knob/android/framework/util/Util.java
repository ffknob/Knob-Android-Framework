package br.gov.rs.tce.inventario.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import br.gov.rs.tce.inventario.config.Configuracoes;

public class Util {
    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void log(String tag, String mensagem) {
        if(Configuracoes.getInstance().isLog()) {
            // TODO: registrar no BD

            Log.d(tag, mensagem);
        }
    }
}
