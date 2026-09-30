package ue.edu.co.pokenavigation.data.model;

// Esta clase guarda un Pokemon de la lista.
// La API solo nos manda el nombre y una url por cada uno.
public class Pokemon {

    private String name;

    private String url;

    // Constructor vacio. Gson lo necesita para crear los objetos
    // cuando convierte el JSON que llega de internet.
    public Pokemon() {
    }

    // Este lo usamos nosotros para crear un Pokemon a mano
    // cuando lo vamos a guardar en favoritos.
    public Pokemon(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() { return name; }


    public String getUrl() { return url; }
}
