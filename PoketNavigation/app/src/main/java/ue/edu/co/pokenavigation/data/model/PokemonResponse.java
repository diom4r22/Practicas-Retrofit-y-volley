package ue.edu.co.pokenavigation.data.model;

import java.util.List;

// La API no nos manda la lista de Pokemon directamente.
// Nos manda un objeto que trae count, next, previous y results,
// y la lista que nos interesa viene dentro de results.
// Por eso necesitamos esta clase, si no Gson da error.
public class PokemonResponse {

    // Cuantos Pokemon hay en total en la API
    private int count;

    // Link a la siguiente pagina de resultados
    private String next;

    // Link a la pagina anterior
    private String previous;

    // Esta es la lista que nos sirve
    private List<Pokemon> results;

    public int getCount() {
        return count;
    }

    public List<Pokemon> getResults() {
        return results;
    }
}
