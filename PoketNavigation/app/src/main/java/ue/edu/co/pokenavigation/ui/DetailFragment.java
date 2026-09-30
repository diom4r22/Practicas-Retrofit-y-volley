package ue.edu.co.pokenavigation.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import ue.edu.co.pokenavigation.R;
import ue.edu.co.pokenavigation.data.local.FavoritosStore;
import ue.edu.co.pokenavigation.data.model.Pokemon;
import ue.edu.co.pokenavigation.data.model.PokemonDetail;
import ue.edu.co.pokenavigation.data.repository.PokemonRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Pantalla de detalle. Es la parte de la actividad propuesta.
// Se abre cuando el usuario toca un Pokemon en la lista.
// Pide a la API los datos de ese Pokemon y los muestra,
// y tiene un boton para guardarlo en favoritos.
public class DetailFragment extends Fragment {

    // Nombres de las llaves con las que mandamos los datos al Fragment
    private static final String ARG_NOMBRE = "nombre";
    private static final String ARG_URL = "url";

    // Vistas donde se muestra la informacion
    private ImageView imgPokemon;
    private TextView tvNombre;
    private TextView tvAltura;
    private TextView tvPeso;
    private TextView tvExperiencia;
    private TextView tvTipos;
    private MaterialButton btnFavorito;

    // Vistas de los 3 estados de la pantalla
    private View contenedorDatos;
    private CircularProgressIndicator progressIndicator;
    private LinearLayout errorContainer;
    private TextView tvError;
    private MaterialButton btnRetry;

    private PokemonRepository repository;
    private Call<PokemonDetail> currentCall;

    // Datos del Pokemon que llegan desde la pantalla anterior
    private String nombre;
    private String url;

    public DetailFragment() {
        super(R.layout.fragment_detail);
    }

    // A un Fragment no se le pueden mandar datos por el constructor,
    // toca hacerlo con un Bundle. Por eso se usa este metodo para crearlo.
    public static DetailFragment newInstance(String nombre, String url) {

        DetailFragment fragment = new DetailFragment();

        Bundle args = new Bundle();
        args.putString(ARG_NOMBRE, nombre);
        args.putString(ARG_URL, url);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        // Se sacan los datos que mando la pantalla anterior
        if (getArguments() != null) {
            nombre = getArguments().getString(ARG_NOMBRE);
            url = getArguments().getString(ARG_URL);
        }

        // Se buscan las vistas del diseno fragment_detail.xml
        imgPokemon = view.findViewById(R.id.imgPokemon);
        tvNombre = view.findViewById(R.id.tvNombre);
        tvAltura = view.findViewById(R.id.tvAltura);
        tvPeso = view.findViewById(R.id.tvPeso);
        tvExperiencia = view.findViewById(R.id.tvExperiencia);
        tvTipos = view.findViewById(R.id.tvTipos);
        btnFavorito = view.findViewById(R.id.btnFavorito);

        contenedorDatos = view.findViewById(R.id.contenedorDatos);
        progressIndicator = view.findViewById(R.id.progressIndicator);
        errorContainer = view.findViewById(R.id.errorContainer);
        tvError = view.findViewById(R.id.tvError);
        btnRetry = view.findViewById(R.id.btnRetry);

        repository = new PokemonRepository();

        btnRetry.setOnClickListener(v -> cargarDetalle());
        btnFavorito.setOnClickListener(v -> cambiarFavorito());

        // Se revisa si ya estaba en favoritos para poner el texto correcto
        actualizarBotonFavorito();

        cargarDetalle();
    }

    // Pide a la API la informacion del Pokemon
    private void cargarDetalle() {

        mostrarCargando();

        currentCall = repository.obtenerDetalle(nombre);

        currentCall.enqueue(new Callback<PokemonDetail>() {

            @Override
            public void onResponse(
                    @NonNull Call<PokemonDetail> call,
                    @NonNull Response<PokemonDetail> response
            ) {
                if (!isAdded()) {
                    return;
                }

                PokemonDetail body = response.body();

                if (response.isSuccessful() && body != null) {
                    mostrarDatos(body);
                    mostrarContenido();
                } else {
                    mostrarError(
                            "No fue posible obtener el detalle. "
                                    + "Código HTTP: " + response.code()
                    );
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<PokemonDetail> call,
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

    // Pone en pantalla los datos que llegaron de la API
    private void mostrarDatos(PokemonDetail detalle) {

        tvNombre.setText(detalle.getName());

        // La API manda la altura en decimetros y el peso en hectogramos,
        // por eso se dividen entre 10 para que quede en metros y en kilos
        tvAltura.setText("Altura: " + (detalle.getHeight() / 10.0) + " m");
        tvPeso.setText("Peso: " + (detalle.getWeight() / 10.0) + " kg");

        tvExperiencia.setText("Experiencia base: " + detalle.getBaseExperience());
        tvTipos.setText("Tipos: " + detalle.getTiposTexto());

        // Glide se encarga de bajar la imagen de internet y ponerla
        // en el ImageView. Retrofit solo trae texto, no imagenes.
        Glide.with(this)
                .load(detalle.getImagenOficial())
                .into(imgPokemon);
    }

    // El boton funciona como interruptor:
    // si no estaba guardado lo guarda, y si ya estaba lo quita
    private void cambiarFavorito() {

        FavoritosStore store = FavoritosStore.getInstance();

        if (store.esFavorito(nombre)) {

            store.quitar(nombre);
            Toast.makeText(
                    requireContext(),
                    "Se quitó de Favoritos",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            store.agregar(new Pokemon(nombre, url));
            Toast.makeText(
                    requireContext(),
                    "Se guardó en Favoritos",
                    Toast.LENGTH_SHORT
            ).show();
        }

        actualizarBotonFavorito();
    }

    // Cambia el texto del boton segun si el Pokemon ya esta guardado
    private void actualizarBotonFavorito() {

        if (FavoritosStore.getInstance().esFavorito(nombre)) {
            btnFavorito.setText("Quitar de Favoritos");
        } else {
            btnFavorito.setText("Guardar en Favoritos");
        }
    }

    // Los 3 metodos de abajo prenden y apagan vistas
    // igual que en HomeFragment

    private void mostrarCargando() {
        progressIndicator.setVisibility(View.VISIBLE);
        contenedorDatos.setVisibility(View.GONE);
        errorContainer.setVisibility(View.GONE);
    }

    private void mostrarContenido() {
        progressIndicator.setVisibility(View.GONE);
        contenedorDatos.setVisibility(View.VISIBLE);
        errorContainer.setVisibility(View.GONE);
    }

    private void mostrarError(String mensaje) {
        progressIndicator.setVisibility(View.GONE);
        contenedorDatos.setVisibility(View.GONE);
        errorContainer.setVisibility(View.VISIBLE);
        tvError.setText(mensaje);
    }

    // Se cancela la peticion al salir de la pantalla
    @Override
    public void onDestroyView() {

        if (currentCall != null) {
            currentCall.cancel();
        }

        super.onDestroyView();
    }
}
