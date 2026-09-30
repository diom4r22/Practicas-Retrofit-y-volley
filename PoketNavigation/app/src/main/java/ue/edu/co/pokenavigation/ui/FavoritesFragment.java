package ue.edu.co.pokenavigation.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ue.edu.co.pokenavigation.R;
import ue.edu.co.pokenavigation.data.local.FavoritosStore;
import ue.edu.co.pokenavigation.data.model.Pokemon;
import ue.edu.co.pokenavigation.ui.adapter.PokemonAdapter;

// Pantalla de Favoritos.
// Muestra los Pokemon que el usuario guardo desde la pantalla de detalle.
// No pide nada a internet, los saca de FavoritosStore.
public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerFavoritos;

    // Mensaje que se muestra cuando todavia no hay favoritos
    private TextView tvVacio;

    private PokemonAdapter adapter;

    public FavoritesFragment() {
        super(R.layout.fragment_favorites);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        recyclerFavoritos = view.findViewById(R.id.recyclerFavoritos);
        tvVacio = view.findViewById(R.id.tvVacio);

        // Se usa el mismo adaptador de la pantalla de Inicio,
        // asi las tarjetas se ven iguales y no toca hacer otro
        adapter = new PokemonAdapter(this::abrirDetalle);

        recyclerFavoritos.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );
        recyclerFavoritos.setAdapter(adapter);

        mostrarFavoritos();
    }

    // onResume se ejecuta cada vez que la pantalla se vuelve a ver.
    // Se refresca aqui porque el usuario pudo haber guardado
    // un favorito nuevo mientras estaba en otra pantalla.
    @Override
    public void onResume() {
        super.onResume();
        mostrarFavoritos();
    }

    // Llena la lista y decide si mostrar las tarjetas o el mensaje de vacio
    private void mostrarFavoritos() {

        List<Pokemon> favoritos = FavoritosStore.getInstance().getFavoritos();

        adapter.actualizarDatos(favoritos);

        if (favoritos.isEmpty()) {
            tvVacio.setVisibility(View.VISIBLE);
            recyclerFavoritos.setVisibility(View.GONE);
        } else {
            tvVacio.setVisibility(View.GONE);
            recyclerFavoritos.setVisibility(View.VISIBLE);
        }
    }

    // Desde favoritos tambien se puede abrir el detalle
    private void abrirDetalle(Pokemon pokemon) {

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragmentContainer,
                        DetailFragment.newInstance(pokemon.getName(), pokemon.getUrl())
                )
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        recyclerFavoritos.setAdapter(null);
        super.onDestroyView();
    }
}
