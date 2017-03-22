package br.com.richardwm91.piloto.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.richardwm91.piloto.model.Client;


public class ClientDAO extends SQLiteOpenHelper {

    public ClientDAO(Context context){
        super(context, "continuity", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Client (id INTEGER PRIMARY KEY, name TEXT NOT NULL, cel TEXT, points INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Client";
        db.execSQL(sql);
        onCreate(db);
    }

    public void insertClient(Client client) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = new ContentValues();
        dados.put("name", client.getName());
        dados.put("cel", client.getCel());
        dados.put("points", client.getPoints());

        db.insert("Client", null, dados);
    }

    public List<Client> findClients() {
        String sql = "select * from Client;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        List<Client> clients = new ArrayList<Client>();
        while (c.moveToNext()){
            Client client = new Client();

            client.setId(c.getLong(c.getColumnIndex("id"))); // ou 0 que é a posição
            client.setName(c.getString(c.getColumnIndex("name")));
            client.setCel(c.getString(c.getColumnIndex("cel")));
            client.setPoints(c.getInt(c.getColumnIndex("points")));

            clients.add(client);
        }
        c.close();
        return clients;
    }

    public void deleteClient(Client client) {
        SQLiteDatabase db = getWritableDatabase();

        String [] params = {client.getId().toString()};
        db.delete("Client", "id=?", params);
    }
}
