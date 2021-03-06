package com.example.tata_consultor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.airbnb.lottie.LottieAnimationView;
import com.example.tata_consultor.Clases.Producto;
import com.example.tata_consultor.Constantes.Constante;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ConsultorPrecioActivity extends AppCompatActivity {

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    SharedPreferences pref;
    private int tipocodigo = 2;
    private boolean boleananimationview = true;
    private boolean boleananimationbusqeudaview = false;
    private boolean boleanlinearprecio = false;

    private OkHttpClient Pickinghttp;
    private String sucursal = "2";
    private String m_ip = "200.40.253.210";
    private Handler m_handler = new Handler(); // Main thread
    private Request RequestPicking;
    private ProgressDialog dialog;
    private EditText editcodigobarramanual;
    private Button btnconfig;

    private TextView txtdescripcion1, txtdescripcion2, txtprecioproducto, txtcodigoproducto, txtcodigobarraproducto;

    ActionBar actionBar;
    ConstraintLayout constrain;
    LinearLayout linearprecio;
    ImageView imgescaner;
    LottieAnimationView animationview;
    LottieAnimationView animationbusquedaview;
    boolean visible = false;

    private static final int REQUEST_PICK_CONFIGURACION = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultor_precio);

        constrain = findViewById(R.id.constainlayot);
        actionBar = getSupportActionBar();
        linearprecio = findViewById(R.id.linear_precio);
        animationview = findViewById(R.id.animation_view);
        animationbusquedaview = findViewById(R.id.animationbusqueda_view);

        Button btnon = findViewById(R.id.btn_on);
        Button btnoff = findViewById(R.id.btn_off);
        editcodigobarramanual = findViewById(R.id.edit_codigo_barra_manual);

        btnconfig = findViewById(R.id.btn_config);

        btnconfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ConsultorPrecioActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.alerdiaglog, null);
                final EditText mEmail = (EditText) mView.findViewById(R.id.etEmail);
                final EditText mPassword = (EditText) mView.findViewById(R.id.etPassword);
                final TextView text = (TextView) mView.findViewById(R.id.txt_sucursal);
                Button mLogin = (Button) mView.findViewById(R.id.btnLogin);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!mPassword.getText().toString().isEmpty()) {

                            pref = getSharedPreferences(Constante.LOGINNAME, Context.MODE_PRIVATE);

                            String PASSWORD = pref.getString(Constante.PASSWORD, Constante.PASSWORDROOT);

                            if (mPassword.getText().toString().equals(PASSWORD) || mPassword.getText().toString().equals(Constante.PASSWORDROOT) ){


                                Intent intent2 = new Intent(ConsultorPrecioActivity.this, MainActivity.class);

                                startActivityForResult(intent2, REQUEST_PICK_CONFIGURACION);
                                dialog.dismiss();

                            }else{

                                Toast.makeText(ConsultorPrecioActivity.this,"Acceso Denegado", Toast.LENGTH_SHORT).show();

                            }

                        } else {

                            mPassword.setError("Faltan Datos");
                            mPassword.requestFocus();

                        }
                    }
                });




            }
        });


        Button test = findViewById(R.id.btntest);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                                EnableDialog(true, "cargando");
                                String a = "{\n" +
                                        "\"Codigo\": \"250490000\",\n" +
                                        "\"Nombre\": \"YERBA COMPUESTA SERENA CANARIA 1.00 K\",\n" +
                                        "\"Moneda\": \"moneda\",\n" +
                                        "\"Precio\": \"195.00\",\n" +
                                        "\"CodBarras\": \"7730241010294\",\n" +
                                        "\"Prioridad\": \"C\",\n" +
                                        "\"Aux\": \"\"\n" +
                                        "}";
                                Gson g = new Gson();
                                Producto p = g.fromJson(a, Producto.class);
                                p.setEstado("NO");
                                mostrar_datos_view(p);

                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                                 EnableDialog(false, "mostrando");


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                thread.start();


            }
        });
        txtdescripcion1 = findViewById(R.id.txt_descripcion_1);
        txtdescripcion2 = findViewById(R.id.txt_descripcion_2);
        txtprecioproducto = findViewById(R.id.txt_precio_producto);
        txtcodigoproducto = findViewById(R.id.txt_codigo_producto);
        txtcodigobarraproducto = findViewById(R.id.txt_codigo_barra_producto);

        constrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidebarras();
            }
        });

        btnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  showlinearprecio();

             // EnableDialog(true,"cargando",false);

            }
        });

        btnoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  hidelinearprecio();
               // delayedHide(AUTO_HIDE_DELAY_MILLIS);
               // EnableDialog(true,"mostrando",false);

            }
        });


        editcodigobarramanual.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean procesado = false;

                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_TAB || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {


                    presionarboton();
                    procesado = true;

                }
                return procesado;
            }
        });

        hidebarras();
        cargardatospreference();
    }

    private void presionarboton() {


        String codigoimprimir = editcodigobarramanual.getText().toString().trim();
        if (!codigoimprimir.equals("")) {
           // request(codigoimprimir);
            editcodigobarramanual.setEnabled(false);
           requestPruebaMejorado(codigoimprimir);
            editcodigobarramanual.setText("");
            ocultarteclado();
        }
        if (editcodigobarramanual.isFocused()) {
            ocultarteclado();
        }
    }

    public void ocultarteclado() {

        View view = this.getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        hidebarras();
    }

    public void request(final String codigoverificar) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Pickinghttp = new OkHttpClient();
                MediaType mediaType = MediaType.parse("text/xml");

                RequestBody body = RequestBody.create(mediaType, "");

                RequestPicking = new Request.Builder()
                        .url("https://precioonline.tata.com.uy/articulos.php?tipocodigo= "+ tipocodigo+ "&codigo=" + codigoverificar)
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .build();

                EnableDialog(true, "Cargando");

                Pickinghttp.newCall(RequestPicking).enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        EnableDialog(false, "Limpiando");
                        DisplayPrintingStatusMessage("Conexion Fallo");
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        if (response.isSuccessful()) {
                            try {
                                final String myResponse = response.body().string();
                                Gson g = new Gson();
                                Producto a = g.fromJson(myResponse, Producto.class);
                                a.setEstado("NO");

                                if (!a.getNombre().equals(" ") || !a.getPrecio().equals(" ")|| !a.getNombre().isEmpty() || !a.getPrecio().isEmpty()){

                                    mostrar_datos_view(a);

                                }else{
                                    DisplayPrintingStatusMessage("Producto NO registrado");
                                }

                                EnableDialog(false, "Mostrando");

                            } catch (IOException e) {
                                e.printStackTrace();
                                EnableDialog(false, "Limpiando");
                            }
                        } else {
                            DisplayPrintingStatusMessage("Error con la conexion Wifi.. Reintentar");
                            EnableDialog(false, "Limpiando");

                        }
                    }

                });
            }

        });

    }


    public void requestPruebaMejorado(final String codigoverificar) {

        OkHttpClient Pickinghttpnew = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES) // read timeout
                .build();;

        Request RequestPickingnew = new Request.Builder()
                        .url("https://precioonline.tata.com.uy/articulos.php?tipocodigo= "+ tipocodigo+ "&codigo=" + codigoverificar)
                        .method("GET", null)
                        .addHeader("Content-Type", "text/xml")
                        .build();

                EnableDialog(true, "cargando");

        Pickinghttpnew.newCall(RequestPickingnew).enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        EnableDialog(false, "limpiando");
                        DisplayPrintingStatusMessage("Conexion Fallo");
                    }

                    @Override
                    public void onResponse(Call call, final Response response){

                        if (response.isSuccessful()) {

                            try {
                                final String myResponse = response.body().string();
                                Gson g = new Gson();
                                Producto a = g.fromJson(myResponse, Producto.class);
                                a.setEstado("NO");
                                if (!a.getNombre().equals("") || !a.getPrecio().equals("")|| !a.getNombre().isEmpty() || !a.getPrecio().isEmpty()){

                                    mostrar_datos_view(a);
                                    EnableDialog(false, "mostrando");
                                }else{
                                    DisplayPrintingStatusMessage("C??digo sin informaci??n");
                                    EnableDialog(false, "limpiando");
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                                DisplayPrintingStatusMessage("Error Interno ");
                                EnableDialog(false, "limpiando");

                            }


                        } else {
                            DisplayPrintingStatusMessage("Error con la conexion Wifi.. Reintentar");
                            EnableDialog(false, "limpiando");

                        }
                    }

                });
    }
    public void requestPruebaInternet(final String codigoverificar) throws IOException {

        // issue the Get request
        requesttata example = new requesttata();
        String getResponse = example.doGetRequest("https://www.vogella.com/");
        System.out.println(getResponse);


        // issue the post request
        String json = example.bowlingJson("Jesse", "Jake");
        String postResponse = example.doPostRequest("http://www.roundsapp.com/post", json);
        System.out.println(postResponse);

    }


    void hidebarras() {
        constrain.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        if (actionBar != null) {
            actionBar.hide();
        }
        visible = true;

    }


    public void EnableDialog(final boolean value, final String mensaje) {
        m_handler.post(new Runnable() {
            @Override
            public void run() {
                if (mensaje.equals("cargando")) {

                    editcodigobarramanual.setEnabled(false);
                    animationview.setVisibility(View.INVISIBLE);
                    animationbusquedaview.setVisibility(View.VISIBLE);
                    linearprecio.setVisibility(View.INVISIBLE);

                    boleananimationview = false;
                    boleananimationbusqeudaview = true;
                    boleanlinearprecio = false;

                    Log.e("ENTRO A","ENTRO1");

                } else if (mensaje.equals("mostrando")){

                    editcodigobarramanual.setEnabled(true);
                    animationview.setVisibility(View.INVISIBLE);
                    animationbusquedaview.setVisibility(View.INVISIBLE);
                    linearprecio.setVisibility(View.VISIBLE);

                    boleananimationview = false;
                    boleananimationbusqeudaview = false;
                    boleanlinearprecio = true;

                    Log.e("ENTRO B","ENTRO1");
                    ocultando();

                }else{
                    //limpiando
                    editcodigobarramanual.setEnabled(true);
                    animationview.setVisibility(View.VISIBLE);
                    animationbusquedaview.setVisibility(View.INVISIBLE);
                    linearprecio.setVisibility(View.INVISIBLE);

                    boleananimationview = true;
                    boleananimationbusqeudaview = false;
                    boleanlinearprecio = false;

                    Log.e("ENTRO C","ENTRO1");
                }
            }
        });
    }

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {

            animationview.setVisibility(View.VISIBLE);
            animationbusquedaview.setVisibility(View.INVISIBLE);
            linearprecio.setVisibility(View.INVISIBLE);

            boleananimationview = true;
            boleananimationbusqeudaview = false;
            boleanlinearprecio = false;

            Log.e("ENTRO H2","ENTRO1");
        }
    };

    private void cargardatospreference() {
        Bundle parametros = getIntent().getExtras();
        if (parametros != null) {
            sucursal = (parametros.getString("suc"));
            m_ip = (parametros.getString("ip"));

        } else{

            pref = getSharedPreferences("USUARIO", Context.MODE_PRIVATE);
            String ipp = pref.getString("IP", "200.40.253.210");
            String succ = pref.getString("SUC", "2");


        }
    }

    private void ocultando() {
        Log.e("ENTRO H","ENTRO1");
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, 20000);
    }

    public void DisplayPrintingStatusMessage(final String MsgStr) {

        m_handler.post(new Runnable() {
            public void run() {
                showToast(MsgStr);//2018 PH
            }// run()
        });

    }

    public void showToast(final String toast) {

        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();

    }


    private void mostrar_datos_view(final Producto a) {
        m_handler.post(new Runnable() {
            public void run() {

                if (a != null) {

                    try {

                        txtdescripcion1.setText(a.getNombre());
                        txtcodigoproducto.setText(a.getCodigo());
                        txtcodigobarraproducto.setText(a.getCodBarras());
                        txtprecioproducto.setText(a.getPrecio());



                        linearprecio.setBackground(ContextCompat.getDrawable(ConsultorPrecioActivity.this, R.drawable.ic_tag_60x30_amarillo));


                    } catch (Exception e) {
                        DisplayPrintingStatusMessage("No disponible");
                        txtdescripcion1.setText("");
                        txtdescripcion2.setText("");
                        txtcodigoproducto.setText("");
                        txtcodigobarraproducto.setText("");
                        txtprecioproducto.setText("");

                        // hidelinearprecio();
                    }


                }
            }// run()
        });


    }








}