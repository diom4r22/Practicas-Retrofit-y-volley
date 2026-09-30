package ue.edu.co.pokenavigation.data.local;

import ue.edu.co.pokenavigation.data.model.Pokemon;

import java.util.ArrayList;
import java.util.List;

// Aqui se guardan los Pokemon favoritos mientras la app esta abierta.
// Se usa una sola instancia para toda la app, asi la pantalla de detalle
// y la de favoritos trabajan sobre la misma lista.
// Nota: como se guarda en memoria, la lista se borra al cerrar la app.
public class FavoritosStore {

    // Aqui se guarda la unica instancia
    private static FavoritosStore instancia;

    // La lista de favoritos
    private final List<Pokemon> favoritos = new ArrayList<>();

    // Constructor privado para que nadie cree otra instancia por fuera
    private FavoritosStore() {
    }

    // Con este metodo se pide la instancia.
    // La primera vez la crea y de ahi en adelante devuelve la misma.
    public static FavoritosStore getInstance() {

        if (instancia == null) {
            instancia = new FavoritosStore();
        }

        return instancia;
    }

    // Devuelve toda la lista de favoritos
    public List<Pokemon> getFavoritos() {
        return favoritos;
    }

    // Revisa si un Pokemon ya esta guardado.
    // Recorre la lista comparando por nombre.
    public boolean esFavorito(String nombre) {

        for (Pokemon pokemon : favoritos) {
            if (pokemon.getName().equals(nombre)) {
                return true;
            }
        }

        return false;
    }

    // Agrega un Pokemon, pero solo si no estaba ya guardado
    public void agregar(Pokemon pokemon) {

        if (!esFavorito(pokemon.getName())) {
            favoritos.add(pokemon);
        }
    }

    // Busca el Pokemon por nombre y lo saca de la lista
    public void quitar(String nombre) {

        for (int i = 0; i < favoritos.size(); i++) {

            if (favoritos.get(i).getName().equals(nombre)) {
                favoritos.remove(i);
                return;
            }
        }
    }
}
