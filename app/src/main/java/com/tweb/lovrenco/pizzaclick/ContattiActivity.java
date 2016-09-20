package com.tweb.lovrenco.pizzaclick;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ContattiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatti);



        TextView txtIndirizzo = (TextView) findViewById(R.id.txtIndirizzoContatti);
        txtIndirizzo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.google.it/maps/place/Via+Giosu%C3%A8+Borsi,+99,+10149+Torino/@45.0986424,7.651498,17z/data=!3m1!4b1!4m5!3m4!1s0x47886c49cf74d3a3:0xc91196795658d40!8m2!3d45.0986386!4d7.6536867";
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                startActivity(viewIntent);
            }
        });

        TextView txtMail = (TextView) findViewById(R.id.txtMailContatti);
        final String mail = txtMail.getText().toString();
        txtMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mail});
                startActivity(Intent.createChooser(emailIntent, "Scrivici una mail"));
            }
        });

        TextView txtTelefono = (TextView) findViewById(R.id.txtTelefonoContatti);
        txtTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //011731137
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:011731137"));
                    startActivity(intent);
                }catch (RuntimeException exc){
                    Toast.makeText(getApplicationContext(), "{EXC}"+exc.getMessage() , Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
