package utilities;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tweb.lovrenco.pizzaclick.R;

import model.Pizza;

/**
 * Created by Lovrenco on 06/09/2016.
 */
public class CustomAdapterActivityCarrello extends ArrayAdapter<Pizza> {


    public CustomAdapterActivityCarrello(Context context, int textViewResourceId, Pizza[] objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.rowitem_carrello, null);

        Pizza p = getItem(position);
        TextView txtNomePizza = (TextView)convertView.findViewById(R.id.txtRowNomePizza);
        TextView txtPrezzoPizza = (TextView)convertView.findViewById(R.id.txtRowPrezzoPizza);
        //TextView txtIngredientiPizza = (TextView)convertView.findViewById(R.id.txtRowIngredientiPizza);
        txtNomePizza.setText(p.getNomePizza());
        txtPrezzoPizza.setText(p.getPrezzoPizza()+"0 €");
        //txtIngredientiPizza.setText(p.getIngredienti());

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

}
