package br.com.richardwm91.piloto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import br.com.richardwm91.piloto.dao.ClientDAO;
import br.com.richardwm91.piloto.model.Client;

public class ItemAdapter extends BaseAdapter {

    private List<Client> list;
    private Context context;
    private LayoutInflater layoutInflater;

    public ItemAdapter(Context context, List<Client> list){
        this.list = list;
        this.context = context;

        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.item_adapter, null);

        final Client client = list.get(position);

        RelativeLayout rlStars = (RelativeLayout) convertView.findViewById(R.id.rlStar);
        TextView tvNome = (TextView) convertView.findViewById(R.id.tvNome);
        TextView tvPoint = (TextView) convertView.findViewById(R.id.tvPoint);

        tvNome.setText(client.getName());
        tvPoint.setText(String.valueOf(client.getPoints()));

        rlStars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(client.getPoints() < 5) {
                    client.setPoints(client.getPoints() + 1);


                    new ClientDAO(context).insertClient(client);
                    list = new ClientDAO(context).findClients();
                }

                notifyDataSetChanged();
            }
        });

        if(client.getPoints() == 5)
            rlStars.setBackgroundResource(R.drawable.ic_starfull);
        return convertView;
    }
}
