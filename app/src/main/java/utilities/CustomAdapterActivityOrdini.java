package utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tweb.lovrenco.pizzaclick.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import model.Ordine;

/**
 * Created by Lovrenco on 08/09/2016.
 */
public class CustomAdapterActivityOrdini extends ArrayAdapter<Ordine> {

    public CustomAdapterActivityOrdini(Context context, int textViewResourceId, Ordine[] objects) {
        super(context, textViewResourceId, objects);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.rowitem_ordine, null);

        TextView txtRowDataORDINI = (TextView)convertView.findViewById(R.id.txtRowDataORDINI);
        TextView txtRowPrezzoORDINI = (TextView)convertView.findViewById(R.id.txtRowPrezzoORDINI);
        TextView txtRowOraORDINI = (TextView)convertView.findViewById(R.id.txtRowOraORDINI);
        TextView txtRowNumPizzeORDINI = (TextView)convertView.findViewById(R.id.txtRowNumPizzeORDINI);

        Ordine o = getItem(position);
        List<String> datetimeSplitted = splitDateTime(o.getData());
        txtRowDataORDINI.setText(datetimeSplitted.get(0));
        txtRowNumPizzeORDINI.setText(o.getQuantitaTotale()+" pizze");
        txtRowOraORDINI.setText(datetimeSplitted.get(1));
        txtRowPrezzoORDINI.setText(String.format(Locale.US, "%1$.2f", o.getPrezzoTotale())+" €");

        return convertView;
    }


    /*********************************************************************************************/

    private List<String> splitDateTime(String datetime){    // 2016-08-22 16:01:00.0
        List<String> output = new ArrayList<>();
        output.add(datetime.substring(0,10));
        output.add(datetime.substring(11,16));
        return output;
    }



}
