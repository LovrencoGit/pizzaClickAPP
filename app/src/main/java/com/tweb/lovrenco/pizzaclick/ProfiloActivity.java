package com.tweb.lovrenco.pizzaclick;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
import java.util.List;
import java.util.Locale;

import model.Carrello;
import model.Ordine;
import model.PizzaPrenotata;
import model.Utente;
import utilities.GsonRequest;

public class ProfiloActivity extends AppCompatActivity {

    Gson gson = new Gson();
    Utente utente;
    Carrello carrello;
    ArrayList<Ordine> elencoOrdini;

    TextView txtProfiloUsername;
    TextView txtProfiloIndirizzo;
    TextView txtProfiloNumOrdini;
    TextView txtProfiloStatoCarrelloNumPizze;
    TextView txtProfiloStatoCarrelloPrezzo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);



        utente = getIntent().getParcelableExtra("utenteLoggato");
        carrello = getIntent().getParcelableExtra("carrello");

        txtProfiloUsername = (TextView) findViewById(R.id.txtProfiloUsername);
        txtProfiloUsername.setText(utente.getUsername());
        txtProfiloIndirizzo = (TextView) findViewById(R.id.txtProfiloIndirizzo);
        txtProfiloIndirizzo.setText(utente.getIndirizzo());
        txtProfiloStatoCarrelloNumPizze = (TextView) findViewById(R.id.txtProfiloStatoCarrelloNumPizze);
        txtProfiloStatoCarrelloNumPizze.setText(carrello.getElencoPizze().size()+" pizze");
        txtProfiloStatoCarrelloPrezzo = (TextView) findViewById(R.id.txtProfiloStatoCarrelloPrezzo);
        txtProfiloStatoCarrelloPrezzo.setText(String.format(Locale.US, "%1$.2f", carrello.getPrezzoTotale())+" €");
        txtProfiloNumOrdini = (TextView) findViewById(R.id.txtProfiloNumOrdini);
        richiestaHTTPforNumOrdini();        //txtProfiloNumOrdini.setText("999");



        TextView txtLabelStatoCarrello = (TextView) findViewById(R.id.txtProfiloLabelStatoCarrello);
        txtLabelStatoCarrello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfiloActivity.this, CarrelloActivity.class);
                Bundle bundle = new Bundle();
                //bundle.putParcelableArrayList("menu", menuToPizza);
                bundle.putParcelable("utenteLoggato", utente);
                bundle.putParcelable("carrello", carrello);
                i.putExtras(bundle);
                startActivity(i);
            }
        });


        TextView txtProfiloLabelNumOrdini = (TextView) findViewById(R.id.txtProfiloLabelNumOrdini);
        txtProfiloLabelNumOrdini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richiestaHTTPgetElencoOrdini();
            }
        });



    }

    /************************************************************************************************************/
    /************************************************************************************************************/
    /************************************************************************************************************/

    private void richiestaHTTPforNumOrdini(){
        Response.Listener<List> listener= new Response.Listener<List>() {
            @Override
            public void onResponse(List response) {
                int numOrdini = Integer.parseInt(gson.fromJson((String)response.get(0), String.class));
                txtProfiloNumOrdini.setText(""+numOrdini);
            }
        };
        Response.ErrorListener errorListener =new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "<errorListener>: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        String utenteJSON = gson.toJson(utente);
        String localhost = getString(R.string.localhost);
        String url ="http://"+localhost+":8084/PIZZACLICK/Dispatcher?srcMobile=mobile&cmdMobile=numOrdini&utente="+utenteJSON;
        url = url.replace(" ","+");         // PROBLEMA: spazi in url
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        GsonRequest<List> gsonRequest = new GsonRequest<>(
                url,List.class, null, listener, errorListener
        );
        queue.add(gsonRequest);
    }



    private void richiestaHTTPgetElencoOrdini(){
        Response.Listener<List> listener= new Response.Listener<List>() {
            @Override
            public void onResponse(List response) {
                ArrayList<Ordine> elencoOrdini = gson.fromJson((String)response.get(0), ArrayList.class);
                if(elencoOrdini == null){
                    Toast.makeText(getApplicationContext(), "c'è stato un problema, riprovare più tardi (elencoOrdini=null)", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(ProfiloActivity.this, OrdiniActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("elencoOrdini", elencoOrdini);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        };
        Response.ErrorListener errorListener =new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "<errorListener>: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
        String utenteJSON = gson.toJson(utente);
        String localhost = getString(R.string.localhost);
        String url ="http://"+localhost+":8084/PIZZACLICK/Dispatcher?srcMobile=mobile&cmdMobile=getOrdini&utente="+utenteJSON;
        url = url.replace(" ","+");         // PROBLEMA: spazi in url
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        GsonRequest<List> gsonRequest = new GsonRequest<>(
                url,List.class, null, listener, errorListener
        );
        queue.add(gsonRequest);
    }



}
