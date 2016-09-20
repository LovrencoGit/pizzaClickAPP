package com.tweb.lovrenco.pizzaclick;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.IllegalFormatConversionException;
import java.util.List;

import model.Carrello;
import model.Ordine;
import model.Pizza;
import model.Utente;
import utilities.CustomAdapterActivityCarrello;
import utilities.CustomAdapterActivityMain;
import utilities.GsonRequest;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Carrello carrello;
    Gson gson = new Gson();
    Utente utente;
    ArrayList<Pizza> menuJSON;
    ArrayList<Pizza> menuToPizza;
    ArrayList<String> menu;

    ListView menuListView;
    TextView txtNomePizza;
    TextView txtIngredienti;
    TextView txtPrezzo;
    TextView txtPrezzoCorrenteCarrello;
    Button btnAddCarrello;
    //Spinner spinnerQuantita;
    EditText txtQuantita;
    TextView txtSizeCarrello;
    ImageView imgCarrello;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        carrello = getIntent().getParcelableExtra("carrello");
        if(carrello == null){
            carrello = new Carrello();
        }

        utente = getIntent().getParcelableExtra("utenteLoggato");
        ArrayList<Pizza> menuFromCarrello = getIntent().getParcelableArrayListExtra("menuFromCarrello");
        ArrayList<Pizza> menuFromOrdini = getIntent().getParcelableArrayListExtra("menuFromOrdini");
        if(menuFromCarrello != null){
            menuToPizza = menuFromCarrello;
        }else if(menuFromOrdini != null){
            menuToPizza = menuFromOrdini;
        }else {

            menuToPizza = new ArrayList<>();
            menuJSON = getIntent().getParcelableArrayListExtra("menu");
            JSONArray jsonArray = new JSONArray(menuJSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    String pizzaJSON = jsonArray.getJSONObject(i).toString();
                    Pizza p = gson.fromJson(pizzaJSON, Pizza.class);
                    menuToPizza.add(p);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        Pizza[] array = new Pizza[menuToPizza.size()];
        for(int i=0; i<array.length;i++){
            array[i] = menuToPizza.get(i);
        }
        CustomAdapterActivityMain customadapter = new CustomAdapterActivityMain(getApplicationContext(), R.layout.rowitem_home, array);
        menuListView =(ListView)findViewById(R.id.menu);
        menuListView.setAdapter(customadapter);

        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Pizza pizzaSelected = menuToPizza.get(position);

                /****** POP UP ******/
                dialog = new Dialog(MainActivity.this);
                dialog.setTitle("         Pizza selezionata");
                dialog.setContentView(R.layout.popup_addcarrello);
                dialog.setCancelable(true);

                txtNomePizza = (TextView) dialog.findViewById(R.id.textViewNomePizza);
                txtIngredienti = (TextView) dialog.findViewById(R.id.textViewIngredienti);
                txtPrezzo = (TextView) dialog.findViewById(R.id.textViewPrezzo);
                txtNomePizza.setText(pizzaSelected.getNomePizza().toUpperCase());
                txtIngredienti.setText(pizzaSelected.getIngredienti());
                txtPrezzo.setText(pizzaSelected.getPrezzoPizza()+"0 €");


                btnAddCarrello = (Button) dialog.findViewById(R.id.btnAddCarrello);
                btnAddCarrello.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtQuantita = (EditText) dialog.findViewById(R.id.edtxtQuantita);
                        String qty = txtQuantita.getText().toString();
                        if(qty.equals("")){
                            Toast.makeText(getApplicationContext(), "campo QUANTITA' vuoto", Toast.LENGTH_SHORT).show();
                        }else {
                            int quantity = 0;
                            try {
                                quantity = Integer.parseInt(qty);
                            } catch (IllegalFormatConversionException exc) {
                                Toast.makeText(getApplicationContext(), "QUANTITA' non valida. \nInserire un numero", Toast.LENGTH_SHORT).show();
                            }
                            if (quantity == 0) {
                                Toast.makeText(getApplicationContext(), "digitare la quantità desiderata", Toast.LENGTH_SHORT).show();
                            } else {
                                carrello.addCarrello(pizzaSelected, quantity);
                                txtSizeCarrello = (TextView) findViewById(R.id.txtSizeCarrello);
                                txtSizeCarrello.setText("" + carrello.getElencoPizze().size());
                                txtPrezzoCorrenteCarrello = (TextView) findViewById(R.id.txtPrezzoCorrenteCarrello);
                                txtPrezzoCorrenteCarrello.setText(carrello.getPrezzoTotale() + "0 €");
                                Toast.makeText(getApplicationContext(), "aggiunto nel carrello", Toast.LENGTH_SHORT).show();
                                dialog.hide();
                            }
                        }
                    }
                });

                dialog.show();
                /********************/

            }
        });


        txtPrezzoCorrenteCarrello = (TextView)findViewById(R.id.txtPrezzoCorrenteCarrello);
        txtPrezzoCorrenteCarrello.setText(carrello.getPrezzoTotale()+"0 €");
        txtSizeCarrello = (TextView) findViewById(R.id.txtSizeCarrello);
        txtSizeCarrello.setText("" + carrello.getElencoPizze().size());

        imgCarrello = (ImageView) findViewById(R.id.imageCarrello);
        imgCarrello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carrello.getElencoPizze().size() == 0) {
                    Toast.makeText(getApplicationContext(), "carrello vuoto \nE' necessario riempirlo per procedere all'acquisto", Toast.LENGTH_SHORT).show();
                }else {
                    Intent i = new Intent(MainActivity.this, CarrelloActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("utenteLoggato", utente);
                    bundle.putParcelable("carrello", carrello);
                    bundle.putParcelableArrayList("menu", menuToPizza);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            /*
            Intent i = new Intent(MainActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("menuToPizza", menuToPizza);
            bundle.putParcelable("utenteLoggato", utente);
            bundle.putParcelable("carrello", carrello);
            i.putExtras(bundle);
            startActivity(i);
            */

        } else if (id == R.id.nav_carrello) {

            if(carrello.getElencoPizze().size() == 0){
                Toast.makeText(getApplicationContext(), "carrello vuoto \nE' necessario riempirlo per procedere all'acquisto", Toast.LENGTH_SHORT).show();
            }else {
                Intent i = new Intent(MainActivity.this, CarrelloActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("utenteLoggato", utente);
                bundle.putParcelable("carrello", carrello);
                bundle.putParcelableArrayList("menu", menuToPizza);
                i.putExtras(bundle);
                startActivity(i);
            }

        } else if (id == R.id.nav_ordini) {

            Response.Listener<List> listener= new Response.Listener<List>() {
                @Override
                public void onResponse(List response) {
                    ArrayList<Ordine> elencoOrdini = gson.fromJson((String)response.get(0), ArrayList.class);
                    if(elencoOrdini == null){
                        Toast.makeText(getApplicationContext(), "c'è stato un problema, riprovare più tardi (elencoOrdini=null)", Toast.LENGTH_LONG).show();
                    }else {
                        Intent intent = new Intent(MainActivity.this, OrdiniActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("utenteLoggato", utente);
                        bundle.putParcelableArrayList("elencoOrdini", elencoOrdini);
                        bundle.putParcelableArrayList("menu", menuToPizza);
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
/*
        } else if (id == R.id.nav_maps) {

            Intent i = new Intent(MainActivity.this, MapsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("utenteLoggato", utente);
            i.putExtras(bundle);
            startActivity(i);
*/
        } else if (id == R.id.nav_profilo) {

            Intent i = new Intent(MainActivity.this, ProfiloActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("utenteLoggato", utente);
            bundle.putParcelable("carrello", carrello);
            i.putExtras(bundle);
            startActivity(i);

        } else if (id == R.id.nav_contatti) {

            Intent i = new Intent(MainActivity.this, ContattiActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_logout) {

            utente = null;
            carrello = null;
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
