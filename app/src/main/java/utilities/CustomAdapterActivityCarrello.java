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

import model.Pizza;

import static com.google.android.gms.analytics.internal.zzy.p;
import static java.security.AccessController.getContext;

/**
 * Created by Lovrenco on 06/09/2016.
 */
public class CustomAdapterActivityCarrello extends ArrayAdapter {
/*
    Context context;
    private HashMap<Pizza, Integer> map = new HashMap<Pizza, Integer>();
    //Pizza[] arrayPizze;
    //private ArrayList<Pizza> elencoPizze;

    public CustomAdapterActivityCarrello(Context context, HashMap<Pizza, Integer> data){
        this.context = context;
        map  = data;
        //arrayPizze = map.keySet().toArray(new Pizza[data.size()]);
        //this.elencoPizze = elencoPizze;
        //for(int i=0; i<arrayPizze.length;i++){
            //elencoPizze.add(i, arrayPizze[i];
            //arrayPizze[i] = this.elencoPizze.get(i);
        //}
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Pizza p = arrayPizze[position];
        Pizza p = map.
        Integer qty = (Integer) getItem(position);

        //do your view stuff here
        LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.rowitem_carrello, null);

        TextView txtNomePizza = (TextView)convertView.findViewById(R.id.txtRowNomePizza);
        TextView txtPrezzoPizza = (TextView)convertView.findViewById(R.id.txtRowPrezzoPizza);
        txtNomePizza.setText(p.getNomePizza());
        txtPrezzoPizza.setText(p.getPrezzoPizza()+"0 €  (x "+qty+")");


        LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.rowitem_linearlayout);
        ImageView imgRmvCarrello = new ImageView(context);
        imgRmvCarrello.setImageResource(R.drawable.rmv_carrello);
        //imgRmvCarrello.setId(p.getIdPizza());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(190,190);
        params.gravity = Gravity.RIGHT;
        imgRmvCarrello.setLayoutParams(params);
        ll.addView(imgRmvCarrello);


        return convertView;
    }



    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public Integer getItem(int position) {
        return map.get(arrayPizze[position]);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    /************************************************************************************************************
     * ***********************************************************************************************************
*/

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
        txtPrezzoPizza.setText(p.getPrezzoPizza()+"0 €  (x "+qty+")");


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
