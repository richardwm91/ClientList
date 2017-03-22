package br.com.richardwm91.piloto;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import br.com.richardwm91.piloto.dao.ClientDAO;
import br.com.richardwm91.piloto.model.Client;

public class ListMainActivity extends AppCompatActivity {

    private ListView listClients;

    private ItemAdapter mItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);
        listClients = (ListView) findViewById(R.id.listClients);

        listClients.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View item, int position, long id) {
                Client client = (Client) listClients.getItemAtPosition(position);

                Intent intentGoToForm = new Intent(ListMainActivity.this, FormActivity.class);
                intentGoToForm.putExtra("client", client);

                startActivity(intentGoToForm);
            }
        });

        Button newClient = (Button) findViewById(R.id.btNewClient);
        newClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGoToForm = new Intent(ListMainActivity.this, FormActivity.class);
                startActivity(intentGoToForm);
            }
        });
        registerForContextMenu(listClients);
    }

    private void loadList() {
        ClientDAO dao = new ClientDAO(this);
        List<Client> clients = dao.findClients();
        dao.close();

        mItemAdapter = new ItemAdapter(this, clients);
        listClients.setAdapter(mItemAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem delete = menu.add("Deletar");

        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                new AlertDialog.Builder(ListMainActivity.this).setTitle("Delete").setMessage("Deseja deletar?").setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                        Client client = (Client) listClients.getItemAtPosition(info.position);

                        ClientDAO dao = new ClientDAO(ListMainActivity.this);
                        dao.deleteClient(client);
                        dao.close();

                        loadList();

                        dialog.dismiss();
                    }
                }).create().show();
                return false;
            }
        });
    }
}
