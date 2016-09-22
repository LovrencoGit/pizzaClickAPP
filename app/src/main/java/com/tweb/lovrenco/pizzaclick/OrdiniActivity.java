package com.tweb.lovrenco.pizzaclick;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import model.Ordine;
import model.Pizza;
import model.PizzaPrenotata;
import model.Utente;
import utilities.CustomAdapterActivityOrdini;
import utilities.GsonRequest;

public class OrdiniActivity extends AppCompatActivity {

    Gson gson = new Gson();
    Utente utente;
    boolean feedbackAcquista;
    ArrayList<Ordine> elencoOrdiniJSON;
    ArrayList<Ordine> elencoOrdiniToOrdine;
    ArrayList<Pizza> menu;
    Ordine ordine;
    ArrayList<PizzaPrenotata> elencoPizzePrenotate;

    Dialog dialog;
    ListView listOrdini;
    TextView txtData;
    TextView txtOra;
    TextView txtPrezzo;
    TextView txtIndirizzo;
    RatingBar ratingbar;
    Button btnAnnullaOrdine;
    Button btnConsegnato;
    LinearLayout llPizzePrenotate;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OrdiniActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("utenteLoggato", utente);
        bundle.putParcelableArrayList("menuFromOrdini", menu);
        intent.putExtras(bundle);
        startActivity(intent);
        //super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordini);

        listOrdini = (ListView) findViewById(R.id.listOrdini);

        utente = getIntent().getParcelableExtra("utenteLoggato");
        menu = getIntent().getParcelableArrayListExtra("menu");
        //feedbackAcquista = getIntent().getBooleanExtra("feedbackAcquista");

        elencoOrdiniJSON = getIntent().getParcelableArrayListExtra("elencoOrdini");
        elencoOrdiniToOrdine = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(elencoOrdiniJSON);

        for (int i = jsonArray.length()-1 ; i >=0 ; i--) {
            try {
                String ordineJSON = jsonArray.getJSONObject(i).toString();
                Ordine o = gson.fromJson(ordineJSON, Ordine.class);
                if(!o.isAnnullato()){
                    elencoOrdiniToOrdine.add(o);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        Ordine[] arrayOrdini = new Ordine[elencoOrdiniToOrdine.size()];
        for(int i=0; i<elencoOrdiniToOrdine.size();i++){
            arrayOrdini[i] = elencoOrdiniToOrdine.get(i);
        }
        final CustomAdapterActivityOrdini customadapter = new CustomAdapterActivityOrdini(getApplicationContext(), R.layout.rowitem_ordine, arrayOrdini);
        listOrdini.setAdapter(customadapter);

        listOrdini.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ordine = elencoOrdiniToOrdine.get(position);
                List<String> splitDateTime = splitDateTime(ordine.getData());
                String data = splitDateTime.get(0);
                String time = splitDateTime.get(1);

                richiestaHTTPforGetPizzeByIdOrdine(ordine.getIdOrdine());

                /****** POP UP start ******/
                dialog = new Dialog(OrdiniActivity.this);
                dialog.setTitle("      ORDINE SELEZIONATO");
                dialog.setContentView(R.layout.popup_ordinemore);
                dialog.setCancelable(true);

                llPizzePrenotate = (LinearLayout) dialog.findViewById(R.id.linearlayoutPizzePrenotate);
                txtData = (TextView) dialog.findViewById(R.id.txtData);
                txtData.setText(data);
                txtOra = (TextView) dialog.findViewById(R.id.txtOra);
                txtOra.setText(time);
                txtPrezzo = (TextView) dialog.findViewById(R.id.txtPrezzo);
                txtPrezzo.setText(String.format(Locale.US, "%1$.2f", ordine.getPrezzoTotale())+" €");
                txtIndirizzo = (TextView) dialog.findViewById(R.id.txtIndirizzo);
                txtIndirizzo.setText(ordine.getIndirizzo());
                ratingbar = (RatingBar) dialog.findViewById(R.id.ratingBar);
                ratingbar.setEnabled(false);
                if(ordine.isConsegnato()){
                    ratingbar.setEnabled(true);
                    ratingbar.setFocusable(true);
                    ratingbar.setClickable(true);
                    ratingbar.setRating(ordine.getValutazione());
                }

                ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        if(ordine.isConsegnato()) {
                            int rating = (int) ratingbar.getRating();
                            richiestaHTTPforRating(rating);
                        }else{
                            Toast.makeText(getApplicationContext(), "E' possibile valutare l'ordine solo se è stato 'CONSEGNATO'", Toast.LENGTH_LONG).show();
                            ratingbar.setRating(0);
                        }
                    }
                });



                btnAnnullaOrdine = (Button) dialog.findViewById(R.id.btnAnnullaOrdine);
                btnAnnullaOrdine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(controlloDisplayAnnulla(ordine.getData()) == false){
                            Toast.makeText(getApplicationContext(), "E' possibile ANNULLARE l'ordine solo entro un'ora dalla consegna", Toast.LENGTH_LONG).show();
                        }else {

                            AlertDialog.Builder miaAlert = new AlertDialog.Builder(OrdiniActivity.this);
                            miaAlert.setTitle("ATTENZIONE");
                            miaAlert.setMessage("Sei sicuro di voler annullare questo ordine?");
                            miaAlert.setNegativeButton("NO", null);
                            miaAlert.setPositiveButton("SI", new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    richiestaHTTPforAnnullaOrdine();
                                }
                            });
                            AlertDialog alert = miaAlert.create();
                            alert.show();

                        }
                    }
                });


                btnConsegnato = (Button) dialog.findViewById(R.id.btnConsegnato);
                if(ordine.isAnnullato() || ordine.isConsegnato()){
                    btnAnnullaOrdine.setEnabled(false);
                    btnConsegnato.setEnabled(false);
                    btnConsegnato.setClickable(false);
                    btnAnnullaOrdine.setClickable(false);
                    String s = (ordine.isAnnullato() ? "annullato" : "consegnato");
                    btnAnnullaOrdine.setText("ordine "+ s);
                    btnConsegnato.setText("ordine " + s);

                    btnAnnullaOrdine.setEnabled(false);
                    btnAnnullaOrdine.setBackground(getDrawable(R.color.colorWhite));
                    btnAnnullaOrdine.setTextColor(Color.DKGRAY);
                    btnConsegnato.setEnabled(false);
                    btnConsegnato.setBackground(getDrawable(R.color.colorWhite));
                    btnConsegnato.setTextColor(Color.DKGRAY);

                    // visibilità btnAnnulla (false, se < 1 ora)
                }else if(controlloDisplayAnnulla(ordine.getData()) == false){
                    btnAnnullaOrdine.setEnabled(false);
                    btnAnnullaOrdine.setBackground(getDrawable(R.color.colorWhite));
                    //btnAnnullaOrdine.setClickable(false);
                    btnAnnullaOrdine.setText("non annullabile");
                    btnAnnullaOrdine.setTextColor(Color.DKGRAY);
                }
                btnConsegnato.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        richiestaHTTPforConsegnato();
                    }
                });


                dialog.show();
                /********* POP UP end ***********/
            }
        });



        /*****************************************************************************************************************/
        /*****************************************************************************************************************/
        /*****************************************************************************************************************/

    }

    private List<String> splitDateTime(String datetime){    // 2016-08-22 16:01:00.0
        List<String> output = new ArrayList<>();
        output.add(datetime.substring(0,10));
        output.add(datetime.substring(11,16));
        return output;
    }

    private void richiestaHTTPforRating(final int rating){
        Response.Listener<List> listener= new Response.Listener<List>() {
            @Override
            public void onResponse(List response) {
                Boolean feedback  =gson.fromJson((String)response.get(0), Boolean.class);
                if(feedback) {
                    //dialog.hide();
                    ordine.setValutazione(rating);
                    Toast.makeText(getApplicationContext(), "Grazie per aver valutato il nostro servizio \n "+rating+" stelle", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Server non raggiungibile, riprovare più tardi \n Ci scusiamo per il disagio tecnico", Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener =new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "<errorListener>: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        String localhost = getString(R.string.localhost);
        String url ="http://"+localhost+":8084/PIZZACLICK/Dispatcher?srcMobile=mobile&cmdMobile=rating&" +
                "idOrdine="+ordine.getIdOrdine()+"&"+
                "rating="+rating;
        url = url.replace(" ","+");         // PROBLEMA: spazi in url

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        GsonRequest<List> gsonRequest = new GsonRequest<>(
                url,List.class, null, listener, errorListener
        );
        queue.add(gsonRequest);

    }

    private void richiestaHTTPforAnnullaOrdine(){
        Response.Listener<List> listener= new Response.Listener<List>() {
            @Override
            public void onResponse(List response) {
                Boolean feedback  =gson.fromJson((String)response.get(0), Boolean.class);
                if(feedback) {
                    dialog.hide();
                    Toast.makeText(getApplicationContext(), "ordine annullato", Toast.LENGTH_LONG).show();
                    ordine.setAnnullato(true);

                    elencoOrdiniToOrdine.remove(ordine);

                    Ordine[] arrayOrdini = new Ordine[elencoOrdiniToOrdine.size()];
                    for(int i=0; i<elencoOrdiniToOrdine.size();i++){
                        arrayOrdini[i] = elencoOrdiniToOrdine.get(i);
                    }
                    final CustomAdapterActivityOrdini customadapter = new CustomAdapterActivityOrdini(getApplicationContext(), R.layout.rowitem_ordine, arrayOrdini);
                    listOrdini.setAdapter(customadapter);
                }else{
                    Toast.makeText(getApplicationContext(), "Server non raggiungibile, riprovare più tardi \n Ci scusiamo per il disagio tecnico", Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener =new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "<errorListener>: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        String localhost = getString(R.string.localhost);
        String url ="http://"+localhost+":8084/PIZZACLICK/Dispatcher?srcMobile=mobile&cmdMobile=annulla&" +
                "idOrdine="+ordine.getIdOrdine();
        url = url.replace(" ","+");         // PROBLEMA: spazi in url

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        GsonRequest<List> gsonRequest = new GsonRequest<>(
                url,List.class, null, listener, errorListener
        );
        queue.add(gsonRequest);

    }

    private void richiestaHTTPforConsegnato(){
        Response.Listener<List> listener= new Response.Listener<List>() {
            @Override
            public void onResponse(List response) {
                Boolean feedback  =gson.fromJson((String)response.get(0), Boolean.class);
                if(feedback) {
                    //dialog.hide();
                    Toast.makeText(getApplicationContext(), "ordine consegnato", Toast.LENGTH_LONG).show();
                    btnConsegnato.setText("ordine consegnato");
                    btnAnnullaOrdine.setText("ordine consegnato");
                    btnAnnullaOrdine.setEnabled(false);
                    btnAnnullaOrdine.setBackground(getDrawable(R.color.colorWhite));
                    btnAnnullaOrdine.setTextColor(Color.DKGRAY);
                    btnConsegnato.setEnabled(false);
                    btnConsegnato.setBackground(getDrawable(R.color.colorWhite));
                    btnConsegnato.setTextColor(Color.DKGRAY);
                    ordine.setConsegnato(true);
                    ratingbar.setEnabled(true);
                    ratingbar.setFocusable(true);
                    ratingbar.setClickable(true);
                }else{
                    Toast.makeText(getApplicationContext(), "Server non raggiungibile, riprovare più tardi \n Ci scusiamo per il disagio tecnico", Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener =new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "<errorListener>: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        String localhost = getString(R.string.localhost);
        String url ="http://"+localhost+":8084/PIZZACLICK/Dispatcher?srcMobile=mobile&cmdMobile=consegnato&" +
                "idOrdine="+ordine.getIdOrdine();
        url = url.replace(" ","+");         // PROBLEMA: spazi in url

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        GsonRequest<List> gsonRequest = new GsonRequest<>(
                url,List.class, null, listener, errorListener
        );
        queue.add(gsonRequest);

    }


    private static boolean controlloDisplayAnnulla(String dataOrdine) {
        boolean output = false;
        int annoOrdine = Integer.parseInt(dataOrdine.substring(0, 4));
        int meseOrdine = Integer.parseInt(dataOrdine.substring(5, 7));
        int giornoOrdine = Integer.parseInt(dataOrdine.substring(8, 10));
        int oraOrdine = Integer.parseInt(dataOrdine.substring(11, 13));
        int minutiOrdine = Integer.parseInt(dataOrdine.substring(14, 16));

        Calendar gc = new GregorianCalendar(Locale.ITALY);//oggetto per ora di oggi hh:mm:ss

        int annoNow = gc.get(Calendar.YEAR);
        int meseNow = gc.get(Calendar.MONTH) + 1;
        int giornoNow = gc.get(Calendar.DAY_OF_MONTH);
        int oraNow = gc.get(Calendar.HOUR_OF_DAY);
        int minutiNow = gc.get(Calendar.MINUTE);

        if (meseOrdine > meseNow) {//CASO OTTIMO
            output = true;
        } else if (meseOrdine == meseNow) {
            if (giornoOrdine > giornoNow) {//CASO OTTIMO
                output = true;
            } else if (giornoOrdine == giornoNow) {
                if (oraOrdine > oraNow) {
                    if ((oraOrdine - oraNow) == 1 && minutiOrdine<=minutiNow) {
                        output = false;
                    } else {
                        output = true;
                    }
                } else if (oraOrdine == oraNow) {
                    output = false;
                } else {
                    output = false;
                }
            } else {//CASO PESSIMO
                output = false;
            }
        } else {//CASO PESSIMO
            output = false;
        }

        return output;
    }

    private void richiestaHTTPforGetPizzeByIdOrdine(int idOrdine){
        Response.Listener<List> listener= new Response.Listener<List>() {
            @Override
            public void onResponse(List response) {
                ArrayList<PizzaPrenotata> elencoPizzePrenotateJSON = gson.fromJson((String)response.get(0), ArrayList.class);
                JSONArray jsonArray = new JSONArray(elencoPizzePrenotateJSON);
                elencoPizzePrenotate = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        String pizzaJSON = jsonArray.getJSONObject(i).toString();
                        PizzaPrenotata p = gson.fromJson(pizzaJSON, PizzaPrenotata.class);
                        elencoPizzePrenotate.add(p);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(elencoPizzePrenotate == null || elencoPizzePrenotate.size()==0) {
                    Toast.makeText(getApplicationContext(), "Server non raggiungibile, riprovare più tardi \n Ci scusiamo per il disagio tecnico", Toast.LENGTH_LONG).show();
                }else{

                    HashMap<PizzaPrenotata, Integer> mapNEW = new HashMap<PizzaPrenotata, Integer>();
                    for(int i=0; i<elencoPizzePrenotate.size();i++){
                        PizzaPrenotata pizza = elencoPizzePrenotate.get(i);
                        //pizza.setQuantita(0);

                        Integer qty = mapNEW.get(pizza);
                        mapNEW.put(pizza, ( qty==null ? 1 : qty+1) );
                    }

                    for(PizzaPrenotata pizza : mapToArray(mapNEW)){
                        LinearLayout llrow = new LinearLayout(getApplicationContext());
                        llrow.setOrientation(LinearLayout.HORIZONTAL);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                        params.weight = 1;

                        TextView txtNome = new TextView(getApplicationContext());
                        txtNome.setText(pizza.getNomePizzaPrenotata());
                        txtNome.setTextSize(18);
                        txtNome.setGravity(Gravity.CENTER);
                        txtNome.setTextColor(Color.BLACK);
                        txtNome.setVisibility(View.VISIBLE);
                        txtNome.setLayoutParams(params);

                        TextView txtPrezzo = new TextView(getApplicationContext());
                        txtPrezzo.setText(String.format(Locale.US, "%1$.2f", pizza.getPrezzoPizzaPrenotata())+" €  (x"+pizza.getQuantita()+")");
                        txtPrezzo.setTextSize(18);
                        txtPrezzo.setGravity(Gravity.CENTER);
                        txtPrezzo.setTextColor(Color.BLACK);
                        txtPrezzo.setVisibility(View.VISIBLE);
                        txtPrezzo.setLayoutParams(params);

                        llrow.addView(txtNome);
                        llrow.addView(txtPrezzo);


                        llrow.setEnabled(true);
                        llrow.setVisibility(View.VISIBLE);
                        //llPizzePrenotate.setPadding(10,10,10,10);
                        llPizzePrenotate.addView(llrow);
                    }
                }
            }
        };

        Response.ErrorListener errorListener =new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "<errorListener>: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        String localhost = getString(R.string.localhost);
        String url ="http://"+localhost+":8084/PIZZACLICK/Dispatcher?srcMobile=mobile&cmdMobile=getPizzePrenotateByIdOrdine&" +
                "idOrdine="+ordine.getIdOrdine();
        url = url.replace(" ","+");         // PROBLEMA: spazi in url

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        GsonRequest<List> gsonRequest = new GsonRequest<>(
                url,List.class, null, listener, errorListener
        );
        queue.add(gsonRequest);

    }





    private ArrayList<PizzaPrenotata> mapToArray(HashMap<PizzaPrenotata, Integer> map){
        ArrayList<PizzaPrenotata> arrayPizze = new ArrayList<>();
        for(Map.Entry<PizzaPrenotata, Integer> entry : map.entrySet()) {
            PizzaPrenotata pizza = entry.getKey();
            Integer qty = entry.getValue();
            pizza.setQuantita(qty);
            if(qty!=null && qty > 0) {
                arrayPizze.add(pizza);
            }
        }

        return arrayPizze;
    }

}

