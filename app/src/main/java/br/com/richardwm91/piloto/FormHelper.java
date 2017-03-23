package br.com.richardwm91.piloto;

import android.widget.EditText;
import android.widget.RatingBar;

import br.com.richardwm91.piloto.model.Client;

public class FormHelper {

    private final EditText name;
    private final EditText cel;
    private final RatingBar points;

    private Client client;

    public FormHelper(FormActivity activity){
        name = (EditText) activity.findViewById(R.id.etName);
        cel = (EditText) activity.findViewById(R.id.etCel);
        points = (RatingBar) activity.findViewById(R.id.rbPoints);

        points.setMax(5);
        client = new Client();
    }

    public void fillOfForm(Client client) {
        name.setText(client.getName());
        cel.setText(client.getCel());
        points.setProgress(client.getPoints());
        this.client = client;
    }

    public Client getClient() {
        client.setName(name.getText().toString());
        client.setCel(cel.getText().toString());
        client.setPoints(points.getProgress());

        return client;
    }
}
