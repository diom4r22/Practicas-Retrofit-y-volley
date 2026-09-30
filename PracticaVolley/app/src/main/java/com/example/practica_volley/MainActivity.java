package com.example.practica_volley;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.practica_volley.modelo.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String URL_API = "https://jsonplaceholder.typicode.com/posts";
    private static final String REQUEST_TAG = "GET_POSTS";
    private ListView lvTodos;
    private ProgressBar progressBar;
    private TextView tvEstado;
    private TextView tvContador;
    private RequestQueue requestQueue;
    private final List<Post> listaPosts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializarVistas();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        consumirApi();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(REQUEST_TAG);
        }
    }

    private void inicializarVistas(){
        lvTodos = findViewById(R.id.lvTodos);
        progressBar = findViewById(R.id.progressBar);
        tvEstado = findViewById(R.id.tvEstado);
        tvContador = findViewById(R.id.tvContador);
    }

    private void mostrarCargando(boolean cargando){
        progressBar.setVisibility(cargando ? View.VISIBLE : View.GONE);
        lvTodos.setVisibility(cargando ? View.GONE : View.VISIBLE);
    }

    private void mostrarError(String mensaje){
        mostrarCargando(false);
        tvEstado.setText(mensaje);
        tvEstado.setVisibility(View.VISIBLE);
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    private void procesarError(VolleyError error){
        String mensaje = error.getMessage();
        if(mensaje == null || mensaje.isBlank()){
            mensaje = "Verifique la conexion a internet";
        }
        mostrarError("Error en la solicitud: " + mensaje);
    }

    private void consumirApi(){
        mostrarCargando(true);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL_API,
                null,
                response -> {
                    listaPosts.clear();
                    try {
                        for(int i = 0; i < response.length(); i++ ){
                            JSONObject item = response.getJSONObject(i);

                            int userId = item.getInt("userId");
                            int id = item.getInt("id");
                            String titulo = item.getString("title");
                            String cuerpo = item.getString("body");
                            Post post = new Post(userId, id, titulo, cuerpo);
                            listaPosts.add(post);
                        }
                        ArrayAdapter<Post> adapter = new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_list_item_1,
                                listaPosts
                        );
                        lvTodos.setAdapter(adapter);
                        mostrarCargando(false);
                        tvEstado.setVisibility(View.GONE);
                        tvContador.setText(getString(R.string.registros_recibidos, listaPosts.size()));
                        tvContador.setVisibility(View.VISIBLE);
                    } catch (JSONException e){
                        mostrarError("No fue posible procesar la respuesta. ");
                    }
                },
                this::procesarError
        );
        request.setTag(REQUEST_TAG);
        requestQueue.add(request);
    }

}
