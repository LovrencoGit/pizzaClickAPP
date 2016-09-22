package utilities;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tweb.lovrenco.pizzaclick.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import model.Pizza;

import static com.google.android.gms.analytics.internal.zzy.p;
import static java.security.AccessController.getContext;

/**
 * Created by Lovrenco on 06/09/2016.
 */
public class CustomAdapterActivityCarrello extends ArrayAdapter {


    private Pizza[] elencoPizze;    //elencoPizzeQTY
    //private HashMap<Integer, Integer> quantities;       // < id , qty >

    public CustomAdapterActivityCarrello(Context context, int textViewResourceId, Pizza[] objects) {
        super(context, textViewResourceId, objects);
        this.elencoPizze = objects;
        //this.quantities = getQuantities(this.elencoPizze);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.rowitem_carrello, null);

        Pizza p = elencoPizze[position];
        int qty = p.getQuantita();
        TextView txtNomePizza = (TextView)convertView.findViewById(R.id.txtRowNomePizza);
        TextView txtPrezzoPizza = (TextView)convertView.findViewById(R.id.txtRowPrezzoPizza);
        txtNomePizza.setText(p.getNomePizza());
        txtPrezzoPizza.setText(String.format(Locale.US, "%1$.2f", p.getPrezzoPizza())+" €  (x "+qty+")");


        LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.rowitem_linearlayout);
        ImageView imgRmvCarrello = new ImageView(getContext());
        imgRmvCarrello.setImageResource(R.drawable.rmv_carrello);
        //imgRmvCarrello.setId(p.getIdPizza());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(190,190);
        params.gravity = Gravity.RIGHT;
        imgRmvCarrello.setLayoutParams(params);
        ll.addView(imgRmvCarrello);


        return convertView;
    }
/************************************************************************************************************
    *******************************************************************************************************/
}
