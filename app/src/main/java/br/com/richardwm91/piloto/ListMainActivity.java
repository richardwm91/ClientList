package br.com.richardwm91.piloto;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import br.com.richardwm91.piloto.dao.ClientDAO;
import br.com.richardwm91.piloto.model.Client;

public class ListMainActivity extends AppCompatActivity {

    private ListView listClients;
    private ItemAdapter mItemAdapter;
    private EditText etQuery;

    private ClientDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);
        listClients = (ListView) findViewById(R.id.listClients);
        etQuery = (EditText) findViewById(R.id.etQuery);

        dao = new ClientDAO(this);

        listClients.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View item, int position, long id) {
                Client client = (Client) listClients.getItemAtPosition(position);

                Intent intentGoToForm = new Intent(ListMainActivity.this, FormActivity.class);
                intentGoToForm.putExtra("client", client);

                startActivity(intentGoToForm);
            }
        });

        etQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    loadList();
                } else {
                    mItemAdapter = new ItemAdapter(ListMainActivity.this, dao.findClientsLike(s.toString()));
                    listClients.setAdapter(mItemAdapter);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new, menu);
        registerForContextMenu(listClients);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intentGoToForm = new Intent(ListMainActivity.this, FormActivity.class);
        startActivity(intentGoToForm);

        return false;
    }

    private void loadList() {
        List<Client> clients = dao.findClients();

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
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                final Client client = (Client) listClients.getItemAtPosition(info.position);

                new AlertDialog.Builder(ListMainActivity.this).setMessage("Deletar cliente: " + client.getName()).setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ClientDAO dao = new ClientDAO(ListMainActivity.this);
                        dao.deleteClient(client);
                        loadList();

                        dialog.dismiss();
                    }
                }).create().show();
                return false;
            }
        });
    }
}
