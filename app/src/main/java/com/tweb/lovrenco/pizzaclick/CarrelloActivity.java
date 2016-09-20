package com.tweb.lovrenco.pizzaclick;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import model.Carrello;
import model.Ordine;
import model.Pizza;
import model.Utente;
import utilities.CustomAdapterActivityCarrello;
import utilities.GsonRequest;

public class CarrelloActivity extends AppCompatActivity {

    Utente utente;
    Carrello carrello;
    ArrayList<Pizza> menu;

    ListView carrelloListView;
    Button btnProcediAcquisto;
    Button btnAcquista;
    Button btnSetDate;
    Button btnSetTime;
    TextView txtDate;
    TextView txtTime;
    TextView txtDateSelected;
    TextView txtTimeSelected;
    TextView txtIndirizzo;
    TextView txtCarrelloNumeroPizze;
    TextView txtCarrelloPrezzo;
    Dialog dialog;
    DatePickerDialog dpd;
    int oraNow;
    int minutiNow;
    String minuti;
    String ora;

    @Override
    public void onBackPressed() {
        //Toast.makeText(getApplicationContext(), "TASTO BACK premuto",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(CarrelloActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("utenteLoggato", utente);
        bundle.putParcelable("carrello", carrello);
        bundle.putParcelableArrayList("menuFromCarrello", menu);
        intent.putExtras(bundle);
        startActivity(intent);

        //super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrello);

        utente = getIntent().getParcelableExtra("utenteLoggato");
        carrello = getIntent().getParcelableExtra("carrello");
        menu = getIntent().getParcelableArrayListExtra("menu");

        TextView txtNumeroPizze = (TextView) findViewById(R.id.txtNumeroPizze);
        txtNumeroPizze.setText(carrello.getElencoPizze().size()+" pizze");
        TextView txtPrezzoTotale = (TextView) findViewById(R.id.txtPrezzoTotaleCarrello);
        txtPrezzoTotale.setText("TOTALE :     " + carrello.getPrezzoTotale() + "0 €");

        riempiListViewConCarrello();

        Calendar gc = new GregorianCalendar(Locale.ITALY);//oggetto per ora di oggi hh:mm:ss

        oraNow = gc.get(Calendar.HOUR_OF_DAY);
        oraNow+=2;
        ora=(oraNow<=9 ? "0"+oraNow : ""+oraNow);
        minutiNow = gc.get(Calendar.MINUTE);
        minutiNow = minutiNow-1 < 0? 60-((-1)*(minutiNow-1)): minutiNow-1 ;
        minuti=(minutiNow<=9 ? "0"+minutiNow : ""+minutiNow);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtTime.setText("1 ora di preavviso");
        btnProcediAcquisto = (Button) findViewById(R.id.btnProcediAcquisto);

        btnProcediAcquisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txtDate = (TextView) findViewById(R.id.txtDate);
                String data = txtDate.getText().toString();
                txtTime = (TextView) findViewById(R.id.txtTime);
                String time = txtTime.getText().toString();

                String datatime =data+","+time;
                boolean procedere = true;
                if(carrello.getElencoPizze().size()==0) {
                    procedere = false;
                    Toast.makeText(getApplicationContext(), "Carrello vuoto",Toast.LENGTH_LONG).show();
                }else if(data.equals("") || data == null){
                    procedere = false;
                    Toast.makeText(getApplicationContext(), "nessuna data selezionata",Toast.LENGTH_LONG).show();
                }else if(time.equals("") || time == null || time.equals("1 ora di preavviso")){
                    procedere = false;
                    Toast.makeText(getApplicationContext(), "nessuna ora selezionata",Toast.LENGTH_LONG).show();
                }

                btnSetDate = (Button) findViewById(R.id.btnSetDate);
                btnSetDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR);
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);

                        final DatePickerDialog dateDialog = new DatePickerDialog(getApplicationContext(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        String gg = ( dayOfMonth<=9 ? "0"+dayOfMonth : ""+dayOfMonth );
                                        String mm = ( ++monthOfYear<=9 ? "0"+monthOfYear : ""+monthOfYear );
                                        txtDate = (TextView) findViewById(R.id.txtDate);
                                        txtDate.setText(gg + " - " + mm + " - " + year);
                                    }
                                }, mYear, mMonth, mDay);

                        dateDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                        dateDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        dateDialog.show();
                    }
                });


                btnSetTime = (Button) findViewById(R.id.btnSetTime);
                btnSetTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar c = Calendar.getInstance();
                        int mHour = c.get(Calendar.HOUR_OF_DAY);
                        int mMinute = c.get(Calendar.MINUTE);

                        TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String hh = ( hourOfDay<=9 ? "0"+hourOfDay : ""+hourOfDay );
                                String mm = ( minute<=9 ? "0"+minute : ""+minute );
                                txtTime = (TextView) findViewById(R.id.txtTime);
                                txtTime.setText(hh + " : " + mm);
                            }
                        };

                        mHour = (mHour+1)%24;
                        mMinute = (mMinute+1)%60;
                        TimePickerDialog timeDialog = new TimePickerDialog(getApplicationContext(), timePickerListener, mHour, mMinute, true);
                        timeDialog.updateTime(mHour, mMinute);
                        timeDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        timeDialog.show();
                    }
                });

                if(procedere){
                    if(controlloValiditaData(datatime)){
                        /****** POP UP start ******/
                        dialog = new Dialog(CarrelloActivity.this);
                        dialog.setTitle("        IL TUO ORDINE");
                        dialog.setContentView(R.layout.popup_acquistaordine);
                        dialog.setCancelable(true);

                        txtIndirizzo = (TextView) dialog.findViewById(R.id.txtIndirizzo);
                        txtIndirizzo.setText(utente.getIndirizzo().toString());

                        txtDateSelected = (TextView) dialog.findViewById(R.id.txtDateSelected); // in overlay
                        txtDate = (TextView) findViewById(R.id.txtDate);                        // in CarrelloActivity
                        txtDateSelected.setText(txtDate.getText().toString());

                        txtTimeSelected = (TextView) dialog.findViewById(R.id.txtTimeSelected); // in overlay
                        txtTime = (TextView) findViewById(R.id.txtTime);                        // in CarrelloActivity
                        txtTimeSelected.setText(txtTime.getText().toString());

                        txtCarrelloNumeroPizze = (TextView) dialog.findViewById(R.id.txtCarrelloNumeroPizze);
                        txtCarrelloNumeroPizze.setText(carrello.getElencoPizze().size()+" pizze");

                        txtCarrelloPrezzo = (TextView) dialog.findViewById(R.id.txtCarrelloPrezzo);
                        txtCarrelloPrezzo.setText(carrello.getPrezzoTotale()+"0 €");

                        btnAcquista = (Button) dialog.findViewById(R.id.btnAcquista);
                        btnAcquista.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Gson gson = new Gson();

                                Response.Listener<List> listener= new Response.Listener<List>() {
                                    @Override
                                    public void onResponse(List response) {
                                        dialog.hide();
                                        Toast.makeText(getApplicationContext(),"ordine effettuato correttamente" , Toast.LENGTH_LONG).show();
                                        carrello = new Carrello();
                                        riempiListViewConCarrello();

                                        ArrayList<Ordine> elencoOrdini = gson.fromJson((String)response.get(0), ArrayList.class);
                                        Boolean feedbackAcquista  =gson.fromJson((String)response.get(1), Boolean.class);

                                        if(elencoOrdini == null || feedbackAcquista == null){
                                            Toast.makeText(getApplicationContext(), "invio ordine fallito, riprovare", Toast.LENGTH_LONG).show();
                                        }else {
                                            Intent intent = new Intent(CarrelloActivity.this, OrdiniActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putParcelable("utenteLoggato", utente);
                                            bundle.putParcelableArrayList("elencoOrdini", elencoOrdini);
                                            bundle.putParcelableArrayList("menu", menu);
                                            bundle.putBoolean("feedbackAcquista", feedbackAcquista);
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

                                String carrelloJSON = gson.toJson(carrello);
                                String utenteJSON = gson.toJson(utente);
                                TextView txtIndirizzo = (TextView) dialog.findViewById(R.id.txtIndirizzo);
                                String indirizzo = txtIndirizzo.getText().toString();

                                txtDateSelected = (TextView) dialog.findViewById(R.id.txtDateSelected);
                                String data = txtDateSelected.getText().toString();
                                txtTimeSelected = (TextView) dialog.findViewById(R.id.txtTimeSelected);
                                String time = txtTimeSelected.getText().toString();
                                String datetime = datetimeToDBFormat(data,time);

                                String localhost = getString(R.string.localhost);
                                String url ="http://"+localhost+":8084/PIZZACLICK/Dispatcher?srcMobile=mobile&cmdMobile=acquista&" +
                                        "utente="+utenteJSON +"&" +
                                        "carrello="+carrelloJSON+"&" +
                                        "indirizzo="+indirizzo+"&" +
                                        "datetime="+datetime;
                                url = url.replace(" ","+");         // PROBLEMA: spazi in url

                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                GsonRequest<List> gsonRequest = new GsonRequest<>(
                                        url,List.class, null, listener, errorListener
                                );
                                queue.add(gsonRequest);

                            }
                        });

                        dialog.show();
                        /********* POP UP end ***********/
                    }else{
                        Toast.makeText(getApplicationContext(), "Orario non valido!",Toast.LENGTH_LONG).show();
                    }


                }
            }
        });
        // aaaa-mm-gg,hh:mm
        btnSetDate = (Button) findViewById(R.id.btnSetDate);
        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog dateDialog = new DatePickerDialog(getApplicationContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String gg = ( dayOfMonth<=9 ? "0"+dayOfMonth : ""+dayOfMonth );
                                String mm = ( ++monthOfYear<=9 ? "0"+monthOfYear : ""+monthOfYear );
                                txtDate = (TextView) findViewById(R.id.txtDate);
                                txtDate.setText(gg + " - " + mm + " - " + year);
                            }
                        }, mYear, mMonth, mDay);

                dateDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dateDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dateDialog.show();
            }
        });


        btnSetTime = (Button) findViewById(R.id.btnSetTime);
        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.MONTH);
                int mMinute = c.get(Calendar.DAY_OF_MONTH);

                TimePickerDialog timeDialog = new TimePickerDialog(getApplicationContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String hh = ( hourOfDay<=9 ? "0"+hourOfDay : ""+hourOfDay );
                                String mm = ( minute<=9 ? "0"+minute : ""+minute );
                                txtTime = (TextView) findViewById(R.id.txtTime);
                                txtTime.setText(hh + " : " + mm);
                            }
                        }, mHour, mMinute, true);
                timeDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                timeDialog.show();
            }
        });






    }


    /***************************************************************************************/
    /***************************************************************************************/
    /***************************************************************************************/


    private static boolean controlloValiditaData(String dataOrdine) { // gg - mm - aaaa,hh : mm

        boolean output = false;
        int giornoOrdine = Integer.parseInt(dataOrdine.substring(0, 2));
        int meseOrdine = Integer.parseInt(dataOrdine.substring(5, 7));
        int oraOrdine = Integer.parseInt(dataOrdine.substring(15, 17));
        int minutiOrdine = Integer.parseInt(dataOrdine.substring(20));


        Calendar gc = new GregorianCalendar(Locale.ITALY);//oggetto per ora di oggi hh:mm:ss


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


    private String datetimeToDBFormat(String data, String time){
        //      data                time        ->          datetime
        //  gg - mm - aaaa         hh : mm      ->     aaaa-mm-gg hh:mm:00
        String anno = data.substring(10);
        String mese = data.substring(5,7);
        String giorno = data.substring(0,2);
        String ora = time.substring(0,2);
        String minuti = time.substring(5,7);
        return anno+"-"+mese+"-"+giorno+" "+ora+":"+minuti+":00";
    }


    private void riempiListViewConCarrello(){
        ArrayList<Pizza> elencoPizze = carrello.getElencoPizze();
        Pizza[] arrayPizze = new Pizza[elencoPizze.size()];
        //ImageView[] imagesRmv = new ImageView[elencoPizze.size()];
        Pizza pizza;
        for(int i=0; i<elencoPizze.size();i++){
            pizza = elencoPizze.get(i);
            arrayPizze[i] = pizza;
        }
        final CustomAdapterActivityCarrello customadapter = new CustomAdapterActivityCarrello(getApplicationContext(), R.layout.rowitem_carrello, arrayPizze);
        carrelloListView =(ListView)findViewById(R.id.carrelloListView);
        carrelloListView.setAdapter(customadapter);


        carrelloListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //final String titoloriga = (String) adapter.getItem(position);
                final Pizza pizzaSelected = carrello.getElencoPizze().get(position);
                //Toast.makeText(getApplicationContext(),pizzaSelected.getIngredienti(),Toast.LENGTH_LONG).show();

                AlertDialog.Builder miaAlert = new AlertDialog.Builder(CarrelloActivity.this);
                miaAlert.setTitle(pizzaSelected.getNomePizza());
                miaAlert.setMessage("Sei sicuro di voler eliminare questa pizza?");
                final int posizioneDaRimuovere = position;
                miaAlert.setNegativeButton("NO", null);
                miaAlert.setPositiveButton("SI", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        carrello.removeCarrello(pizzaSelected);
                        List<Pizza> carrelloNEW = carrello.getElencoPizze();
                        Pizza[] arrayPizzeNEW = new Pizza[carrello.getElencoPizze().size()];
                        for(int i=0; i<carrelloNEW.size();i++){
                            arrayPizzeNEW[i] = carrelloNEW.get(i);
                        }
                        CustomAdapterActivityCarrello customadapterNEW = new CustomAdapterActivityCarrello(getApplicationContext(), R.layout.rowitem_carrello, arrayPizzeNEW);
                        carrelloListView.setAdapter(customadapterNEW);
                        TextView txtNumeroPizze = (TextView) findViewById(R.id.txtNumeroPizze);
                        txtNumeroPizze.setText(carrello.getElencoPizze().size()+" pizze");
                        TextView txtPrezzoTotale = (TextView) findViewById(R.id.txtPrezzoTotaleCarrello);
                        txtPrezzoTotale.setText("TOTALE :     " + carrello.getPrezzoTotale() + "0 €");

                    }
                });
                AlertDialog alert = miaAlert.create();
                alert.show();


            }
        });



    }





}
