package ue.edu.co.pokenavigation.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ue.edu.co.pokenavigation.R;
import ue.edu.co.pokenavigation.data.model.Pokemon;
import ue.edu.co.pokenavigation.data.model.PokemonResponse;
import ue.edu.co.pokenavigation.data.repository.PokemonRepository;
import ue.edu.co.pokenavigation.ui.adapter.PokemonAdapter;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Pantalla de Inicio.
// Pide la lista de Pokemon a la API y la muestra en un RecyclerView.
// La pantalla tiene 3 estados: cargando, lista o mensaje de error.
public class HomeFragment extends Fragment {

    // Vistas del diseno fragment_home.xml
    private RecyclerView recyclerPokemon;
    private CircularProgressIndicator progressIndicator;
    private LinearLayout errorContainer;
    private TextView tvError;
    private MaterialButton btnRetry;

    private PokemonAdapter adapter;
    private PokemonRepository repository;

    // Aqui guardamos la peticion para poder cancelarla despues
    private Call<PokemonResponse> currentCall;

    // Asi se le dice al Fragment cual diseno tiene que usar
    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    // Esto se ejecuta cuando la pantalla ya esta creada
    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        // Se buscan las vistas del diseno
        recyclerPokemon = view.findViewById(R.id.recyclerPokemon);
        progressIndicator = view.findViewById(R.id.progressIndicator);
        errorContainer = view.findViewById(R.id.errorContainer);
        tvError = view.findViewById(R.id.tvError);
        btnRetry = view.findViewById(R.id.btnRetry);

        // Se crea el adaptador. Lo que va entre parentesis es el metodo
        // que se va a ejecutar cuando el usuario toque una tarjeta.
        adapter = new PokemonAdapter(this::mostrarPokemonSeleccionado);

        repository = new PokemonRepository();

        // El LayoutManager es el que decide como se acomodan las tarjetas.
        // Con LinearLayoutManager quedan una debajo de otra.
        recyclerPokemon.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        // Le decimos que todas las tarjetas miden lo mismo, asi va mas rapido
        recyclerPokemon.setHasFixedSize(true);
        recyclerPokemon.setAdapter(adapter);

        // Boton para volver a intentar cuando hay error
        btnRetry.setOnClickListener(v -> cargarPokemon());

        // Se pide la lista apenas abre la pantalla
        cargarPokemon();
    }

    // Pide los Pokemon a la API
    private void cargarPokemon() {

        mostrarCargando();

        // Pedimos 30 Pokemon empezando desde el primero
        currentCall = repository.obtenerPokemon(30, 0);

        // enqueue hace la peticion en segundo plano.
        // Si se hiciera en el hilo principal la app se congelaria.
        currentCall.enqueue(new Callback<PokemonResponse>() {

            // Entra aqui cuando la API contesta
            @Override
            public void onResponse(
                    @NonNull Call<PokemonResponse> call,
                    @NonNull Response<PokemonResponse> response
            ) {
                // Si el usuario ya se fue de la pantalla no hacemos nada
                if (!isAdded()) {
                    return;
                }

                PokemonResponse body = response.body();

                // Se revisa que la respuesta sirva y que traiga datos
                if (response.isSuccessful()
                        && body != null
                        && body.getResults() != null) {

                    adapter.actualizarDatos(body.getResults());
                    mostrarContenido();

                } else {
                    mostrarError(
                            "No fue posible obtener los Pokémon. "
                                    + "Código HTTP: " + response.code()
                    );
                }
            }

            // Entra aqui cuando ni siquiera se pudo conectar
            @Override
            public void onFailure(
                    @NonNull Call<PokemonResponse> call,
                    @NonNull Throwable throwable
            ) {
                if (call.isCanceled() || !isAdded()) {
                    return;
                }

                mostrarError(
                        "Error de conexión. Verifique internet "
                                + "e intente nuevamente."
                );
            }
        });
    }

    // Se ejecuta cuando el usuario toca una tarjeta.
    // Abre la pantalla de detalle mandandole el nombre y la url.
    private void mostrarPokemonSeleccionado(Pokemon pokemon) {

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragmentContainer,
                        DetailFragment.newInstance(pokemon.getName(), pokemon.getUrl())
                )
                // Esto hace que el boton atras devuelva a la lista
                .addToBackStack(null)
                .commit();
    }

    // Los 3 metodos de abajo solo prenden y apagan vistas
    // segun el estado en que este la pantalla.

    private void mostrarCargando() {
        progressIndicator.setVisibility(View.VISIBLE);
        recyclerPokemon.setVisibility(View.GONE);
        errorContainer.setVisibility(View.GONE);
    }

    private void mostrarContenido() {
        progressIndicator.setVisibility(View.GONE);
        recyclerPokemon.setVisibility(View.VISIBLE);
        errorContainer.setVisibility(View.GONE);
    }

    private void mostrarError(String mensaje) {
        progressIndicator.setVisibility(View.GONE);
        recyclerPokemon.setVisibility(View.GONE);
        errorContainer.setVisibility(View.VISIBLE);
        tvError.setText(mensaje);
    }

    // Se ejecuta cuando la pantalla se va a destruir
    @Override
    public void onDestroyView() {

        // Se cancela la peticion para que la respuesta no llegue
        // despues y trate de escribir en vistas que ya no existen
        if (currentCall != null) {
            currentCall.cancel();
        }

        recyclerPokemon.setAdapter(null);
        super.onDestroyView();
    }
}
