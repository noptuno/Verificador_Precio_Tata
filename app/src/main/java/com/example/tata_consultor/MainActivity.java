package com.example.tata_consultor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button iniciar,cerrar;
    EditText ip,suc;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //EDITADO 20/07/2021

        iniciar = findViewById(R.id.btniniciar);
        cerrar = findViewById(R.id.btn_Cerrar);

        cerrar. setOnClickListener(new View.OnClickListener() {
        @Override
         public void onClick(View view) {
         finish();
         }
});
        ip = findViewById(R.id.edit_ip);
        suc = findViewById(R.id.edit_sucursal);

        pref = getSharedPreferences("USUARIO", Context.MODE_PRIVATE);
        String ipp = pref.getString("IP", "200.40.253.210");
        String succ = pref.getString("SUC", "2");

        ip.setText(ipp);
        suc.setText(succ);
/*
        Intent intent2 = new Intent(MainActivity.this, ConsultorPrecioActivity.class);
        intent2.putExtra("ip", ip.getText().toString());
        intent2.putExtra("suc", suc.getText().toString());

        pref = getSharedPreferences("USUARIO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("IP", ip.getText().toString());
        editor.putString("SUC", suc.getText().toString());
        editor.apply();
        startActivity(intent2);

*/

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ip.getText().equals(" ") || suc.getText().equals(" ")) {
                    return;
                }

                Intent intent2 = new Intent(MainActivity.this, ConsultorPrecioActivity.class);

                intent2.putExtra("ip", ip.getText().toString());
                intent2.putExtra("suc", suc.getText().toString());

                pref = getSharedPreferences("USUARIO", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("IP", ip.getText().toString());
                editor.putString("SUC", suc.getText().toString());
                editor.apply();

                startActivity(intent2);

            }
        });
    }

}