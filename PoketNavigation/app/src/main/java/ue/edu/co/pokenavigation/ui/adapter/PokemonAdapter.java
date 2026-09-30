package ue.edu.co.pokenavigation.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ue.edu.co.pokenavigation.R;
import ue.edu.co.pokenavigation.data.model.Pokemon;

import java.util.ArrayList;
import java.util.List;

// El adaptador es el que llena el RecyclerView.
// Agarra cada Pokemon de la lista y lo pinta en una tarjeta
// usando el diseno item_pokemon.xml.
// Este mismo adaptador se usa en la pantalla de Inicio y en Favoritos.
public class PokemonAdapter
        extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    // Con esto le avisamos al Fragment que tocaron una tarjeta.
    // El Fragment decide que hacer, el adaptador no.
    public interface OnPokemonClickListener {
        void onPokemonClick(Pokemon pokemon);
    }

    // Lista que se esta mostrando en pantalla
    private final List<Pokemon> pokemonList = new ArrayList<>();

    private final OnPokemonClickListener listener;

    public PokemonAdapter(OnPokemonClickListener listener) {
        this.listener = listener;
    }

    // Borra lo que habia y mete los nuevos datos.
    // notifyDataSetChanged avisa que hay que volver a dibujar la lista.
    public void actualizarDatos(List<Pokemon> nuevosPokemon) {

        pokemonList.clear();

        if (nuevosPokemon != null) {
            pokemonList.addAll(nuevosPokemon);
        }

        notifyDataSetChanged();
    }

    // Se llama cuando hay que crear una tarjeta nueva.
    // Aqui se infla el diseno item_pokemon.xml.
    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pokemon, parent, false);

        return new PokemonViewHolder(view);
    }

    // Se llama para llenar una tarjeta con los datos de una posicion
    @Override
    public void onBindViewHolder(
            @NonNull PokemonViewHolder holder,
            int position
    ) {
        Pokemon pokemon = pokemonList.get(position);
        holder.bind(pokemon);
    }

    // Cuantas tarjetas hay que mostrar en total
    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    // El ViewHolder guarda los TextView de una tarjeta
    // para no tener que buscarlos cada vez, que seria mas lento.
    class PokemonViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvPokemonName;
        private final TextView tvPokemonUrl;

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);

            // Se buscan una sola vez
            tvPokemonName = itemView.findViewById(R.id.tvPokemonName);
            tvPokemonUrl = itemView.findViewById(R.id.tvPokemonUrl);
        }

        // Pone los datos del Pokemon en la tarjeta
        public void bind(Pokemon pokemon) {

            tvPokemonName.setText(pokemon.getName());
            tvPokemonUrl.setText(pokemon.getUrl());

            // El clic manda el objeto Pokemon y no la posicion,
            // porque la posicion puede cambiar si la lista se mueve
            // y terminariamos abriendo el Pokemon equivocado.
            itemView.setOnClickListener(
                    view -> listener.onPokemonClick(pokemon)
            );
        }
    }
}
