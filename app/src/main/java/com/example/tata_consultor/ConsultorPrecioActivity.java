package com.example.tata_consultor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tata_consultor.Clases.Producto;
import com.example.tata_consultor.Constantes.Constante;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
    private TextView version;
    private OkHttpClient Pickinghttp;
    private String sucursal = "2";
    private String m_ip = "200.40.253.210";
    private Handler m_handler = new Handler(); // Main thread
    private Request RequestPicking;
    private ProgressDialog dialog;
    private EditText editcodigobarramanual;
    private Button btnconfig;

    private TextView txtdescripcion1, txtdescripcion2, txtprecioproducto, txtcodigoproducto, txtcodigobarraproducto, txtsimbolo;

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
        version = findViewById(R.id.txtversion);

        PackageInfo pinfo;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            version.setText("Version: " + pinfo.versionCode);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


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

                            if (mPassword.getText().toString().equals(PASSWORD) || mPassword.getText().toString().equals(Constante.PASSWORDROOT)) {

                                Intent intent2 = new Intent(ConsultorPrecioActivity.this, MainActivity.class);

                                startActivityForResult(intent2, REQUEST_PICK_CONFIGURACION);
                                dialog.dismiss();

                            } else {
                                Toast.makeText(ConsultorPrecioActivity.this, "Acceso Denegado", Toast.LENGTH_SHORT).show();
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
        txtsimbolo = findViewById(R.id.txt_simbolo);

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

                    String a = editcodigobarramanual.getText().toString();

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

          //  requestPruebaMejorado(codigoimprimir);

            conexionApi2023(codigoimprimir);

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

                        // https://preciosprd.tata.com.uy/articulos.php?tipocodigo=2&codigo=7730241003920
                       // .url("https://precioonline.tata.com.uy/articulos.php?tipocodigo= " + tipocodigo + "&codigo=" + codigoverificar)
                        .url("https://preciosprd.tata.com.uy/articulos.php?tipocodigo= " + tipocodigo + "&codigo=" + codigoverificar)
                        .method("GET", body)
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

                                if (!a.getNombre().equals(" ") || !a.getPrecio().equals(" ") || !a.getNombre().isEmpty() || !a.getPrecio().isEmpty()) {

                                    mostrar_datos_view(a);

                                } else {
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

    /*

        private RequestQueue requestQueue;
        public void requestConvariosrequest(){

            requestQueue = Volley.newRequestQueue(this);


            StringRequest request = new StringRequest(
                    com.android.volley.Request.Method.GET,
                    "https://apidmr.azurewebsites.net/api/v1/datetime/CAA35482-6B71-4F57-BE93-5E0436C481B4",
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject json = new JSONObject(response);
                                String fechajson = json.getString("fecha");
                                String horajson = json.getString("hora");

                                SimpleDateFormat dateFormatcorta = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                SimpleDateFormat horaFormatcorta = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                Date date = new Date();

                                String fechaCortaLocal = dateFormatcorta.format(date);

                                String horaLOCAL = horaFormatcorta.format(date);
                                String horajsonsinsegundos = horajson.substring(0, 5);

                                if (fechajson.equals(fechaCortaLocal) && horajsonsinsegundos.equals(horaLOCAL)) {
                                    fechaCorrecta = true;
                                    Log.e("fechajson: ", "" + fechajson + " = " + fechaCortaLocal);
                                    Log.e("horajsonsinsegundos: ", "" + horaLOCAL + " = " + horaLOCAL);

                                } else {
                                    fechaCorrecta = false;
                                    Log.e("fechajson: ", "" + fechajson + " = " + fechaCortaLocal);
                                    Log.e("horajsonsinsegundos: ", "" + horaLOCAL + " = " + horaLOCAL);
                                    registrarError(Efecha);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (!isNetDisponible()){
                                registrarError(EapiFecha);
                            }

                        }
                    });
            request.setRetryPolicy(new DefaultRetryPolicy(1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);

        }
     */
    public void requestPruebaMejorado(final String codigoverificar) {

        OkHttpClient Pickinghttpnew = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES) // read timeout
                .build();

        Request RequestPickingnew = new Request.Builder()
                .url("https://preciosprd.tata.com.uy/articulos.php?tipocodigo= " + tipocodigo + "&codigo=" + codigoverificar)
               // .url("https://precioonline.tata.com.uy/articulos.php?tipocodigo= " + tipocodigo + "&codigo=" + codigoverificar)
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
            public void onResponse(Call call, final Response response) {

                if (response.isSuccessful()) {

                    try {
                        final String myResponse = response.body().string();
                        Gson g = new Gson();
                        Producto a = g.fromJson(myResponse, Producto.class);
                        a.setEstado("NO");
                        if (!a.getNombre().equals("") || !a.getPrecio().equals("") || !a.getNombre().isEmpty() || !a.getPrecio().isEmpty()) {

                            mostrar_datos_view(a);
                            EnableDialog(false, "mostrando");
                        } else {
                            DisplayPrintingStatusMessage("Código sin información");
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

    private RequestQueue requestQueue;
    void conexionApi2023(String codigoverificar) {

        requestQueue = Volley.newRequestQueue(this);
        EnableDialog(true, "cargando");

        StringRequest request = new StringRequest(
                com.android.volley.Request.Method.GET,
                "https://preciosprd.tata.com.uy/articulos.php?tipocodigo= " + tipocodigo + "&codigo=" + codigoverificar,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject json = new JSONObject(response);
                            Producto a = new Producto();
                            a.setNombre(json.getString("Nombre"));
                            a.setPrecio(json.getString("Precio"));
                            a.setCodBarras(json.getString("CodBarras"));
                            a.setMoneda(json.getString("Moneda"));
                            a.setEstado("NO");

                                if (!a.getNombre().equals("") || !a.getPrecio().equals("") || !a.getNombre().isEmpty() || !a.getPrecio().isEmpty()) {
                                    mostrar_datos_view(a);
                                    EnableDialog(false, "mostrando");
                                } else {
                                    DisplayPrintingStatusMessage("Código sin información");
                                    EnableDialog(false, "limpiando");
                                }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            DisplayPrintingStatusMessage("Error Interno ");
                            EnableDialog(false, "limpiando");
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        DisplayPrintingStatusMessage("Error con la conexion Wifi.. Reintentar");
                        EnableDialog(false, "limpiando");

                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

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

                    Log.e("ENTRO A", "ENTRO1");

                } else if (mensaje.equals("mostrando")) {

                    editcodigobarramanual.setEnabled(true);
                    animationview.setVisibility(View.INVISIBLE);
                    animationbusquedaview.setVisibility(View.INVISIBLE);
                    linearprecio.setVisibility(View.VISIBLE);

                    boleananimationview = false;
                    boleananimationbusqeudaview = false;
                    boleanlinearprecio = true;

                    Log.e("ENTRO B", "ENTRO1");
                    ocultando();

                } else {
                    //limpiando
                    editcodigobarramanual.setEnabled(true);
                    animationview.setVisibility(View.VISIBLE);
                    animationbusquedaview.setVisibility(View.INVISIBLE);
                    linearprecio.setVisibility(View.INVISIBLE);

                    boleananimationview = true;
                    boleananimationbusqeudaview = false;
                    boleanlinearprecio = false;

                    Log.e("ENTRO C", "ENTRO1");
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

            Log.e("ENTRO H2", "ENTRO1");
        }
    };

    private void cargardatospreference() {
        Bundle parametros = getIntent().getExtras();
        if (parametros != null) {
            sucursal = (parametros.getString("suc"));
            m_ip = (parametros.getString("ip"));

        } else {

            pref = getSharedPreferences("USUARIO", Context.MODE_PRIVATE);
            String ipp = pref.getString("IP", "200.40.253.210");
            String succ = pref.getString("SUC", "2");


        }
    }

    private void ocultando() {
        Log.e("ENTRO H", "ENTRO1");
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
                        txtsimbolo.setText(a.getMoneda());

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