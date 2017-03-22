package br.com.richardwm91.piloto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.richardwm91.piloto.dao.ClientDAO;
import br.com.richardwm91.piloto.model.Client;

public class FormActivity extends AppCompatActivity {

    private FormHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        helper = new FormHelper(this);

        Intent intent = getIntent();
        Client client = (Client) intent.getSerializableExtra("client");
        if(client != null)
            helper.fillOfForm(client);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_form_confirm:

                Client client = helper.catchCLient();
                ClientDAO dao = new ClientDAO(this);
                dao.insertClient(client);
                dao.close();

                Toast.makeText(FormActivity.this, client.getName()+" salvo com sucesso.", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
