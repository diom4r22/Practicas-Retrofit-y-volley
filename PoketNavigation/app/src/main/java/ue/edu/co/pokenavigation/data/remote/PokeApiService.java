package ue.edu.co.pokenavigation.data.remote;

import ue.edu.co.pokenavigation.data.model.PokemonDetail;
import ue.edu.co.pokenavigation.data.model.PokemonResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

// Aqui se escriben las peticiones que le vamos a hacer a la API.
// Es una interface, o sea solo se dice que se pide, no como.
// Retrofit se encarga solo de armar el codigo por dentro.
public interface PokeApiService {

    // Trae la lista de Pokemon.
    // Query sirve para los datos que van despues del signo de pregunta,
    // o sea queda asi: pokemon?limit=30&offset=0
    @GET("pokemon")
    Call<PokemonResponse> getPokemon(
            @Query("limit") int limit,
            @Query("offset") int offset
    );

    // Trae la informacion de un solo Pokemon.
    // Aqui el nombre va dentro de la direccion, no despues del signo
    // de pregunta, por eso se usa Path y no Query.
    // Queda asi: pokemon/pikachu
    @GET("pokemon/{name}")
    Call<PokemonDetail> getPokemonDetail(
            @Path("name") String name
    );

}
