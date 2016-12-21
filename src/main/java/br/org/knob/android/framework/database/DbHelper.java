package br.gov.rs.tce.inventario.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.gov.rs.tce.inventario.util.Util;

public class InventarioDbHelper extends SQLiteOpenHelper {
    public static final String TAG = "InventarioDbHelper";

    public static final String NOME_BANCO = "INVENTARIOS";
    public static final int VERSAO_BANCO = 1;

    public InventarioDbHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação do banco de dados

        // Tabela Configurações
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS configuracoes " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, in_online TEXT, in_log TEXT, ws_base_url TEXT );");

        Util.log(TAG, "Criada a tabela configuracoes");

        // Tabela Setores
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS setores " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT, cod_setor TEXT, sigla TEXT, nome TEXT);");

        Util.log(TAG, "Criada a tabela setores");

        // Tabela Inventários
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS inventarios " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT);");

        Util.log(TAG, "Criada a tabela inventarios");

        // Tabela Itens
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS itens " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT);");

        Util.log(TAG, "Criada a tabela itens");

        // Tabela Inventários/Itens
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS inventarios_itens " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT, cd_inventario INTEGER, cd_item INTEGER);");

        Util.log(TAG, "Criada a tabela inventarios_itens");

        // Tabela Logs
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS logs " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT);");

        Util.log(TAG, "Criada a tabela logs");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
