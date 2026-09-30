package ue.edu.co.pokenavigation.data.repository;

import ue.edu.co.pokenavigation.data.model.PokemonDetail;
import ue.edu.co.pokenavigation.data.model.PokemonResponse;
import ue.edu.co.pokenavigation.data.remote.PokeApiService;
import ue.edu.co.pokenavigation.data.remote.RetrofitClient;
import retrofit2.Call;

// Esta clase es la que va a buscar los datos.
// Sirve para que los Fragment no tengan que saber nada de Retrofit,
// ellos solo le piden los datos a esta clase.
public class PokemonRepository {

    private final PokeApiService service;

    // Al crearse pide el servicio ya listo
    public PokemonRepository() {
        service = RetrofitClient.getService();
    }

    // Pide la lista de Pokemon.
    // limit es cuantos queremos y offset desde cual empezamos.
    public Call<PokemonResponse> obtenerPokemon(
            int limit,
            int offset
    ) {
        return service.getPokemon(limit, offset);
    }

    // Pide la informacion de un Pokemon por su nombre
    public Call<PokemonDetail> obtenerDetalle(String nombre) {
        return service.getPokemonDetail(nombre);
    }

}
