package utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tweb.lovrenco.pizzaclick.R;

import model.Pizza;

/**
 * Created by Lovrenco on 07/09/2016.
 */
public class CustomAdapterActivityMain extends ArrayAdapter<Pizza> {


    public CustomAdapterActivityMain(Context context, int textViewResourceId, Pizza[] objects) {
        super(context, textViewResourceId, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.rowitem_home, null);

        TextView txtNomePizza = (TextView)convertView.findViewById(R.id.txtRowNomePizzaHOME);
        TextView txtPrezzoPizza = (TextView)convertView.findViewById(R.id.txtRowPrezzoPizzaHOME);
        TextView txtIngredientiPizza = (TextView)convertView.findViewById(R.id.txtRowIngredientiPizzaHOME);

        Pizza p = getItem(position);
        txtNomePizza.setText(p.getNomePizza());
        txtPrezzoPizza.setText(p.getPrezzoPizza()+"0 €");
        txtIngredientiPizza.setText(p.getIngredienti());
        return convertView;
    }

    






}
