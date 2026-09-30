package ue.edu.co.pokenavigation.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// Esta clase guarda los datos de un solo Pokemon.
// Es lo que devuelve la API cuando pedimos pokemon/{nombre}.
// Solo pusimos los campos que vamos a mostrar en la pantalla de detalle,
// los demas que manda la API se ignoran.
public class PokemonDetail {

    private String name;

    // Ojo: la API manda la altura en decimetros
    private int height;

    // Y el peso en hectogramos
    private int weight;

    // En el JSON esta llave se llama base_experience con guion bajo.
    // En Java no podemos usar ese nombre asi que con SerializedName
    // le decimos a Gson como se llama de verdad.
    @SerializedName("base_experience")
    private int baseExperience;

    // Aqui viene la imagen, pero metida en varios niveles
    private Sprites sprites;

    // Lista de tipos (agua, fuego, planta, etc)
    private List<TypeSlot> types;

    public String getName() { return name; }

    public int getHeight() { return height; }

    public int getWeight() { return weight; }

    public int getBaseExperience() { return baseExperience; }

    // Saca la url de la imagen oficial.
    // Hay que bajar por sprites, other y official-artwork.
    // Si alguno viene vacio devolvemos null para que no se caiga la app.
    public String getImagenOficial() {

        if (sprites == null
                || sprites.other == null
                || sprites.other.officialArtwork == null) {
            return null;
        }

        return sprites.other.officialArtwork.frontDefault;
    }

    // Arma un solo texto con todos los tipos separados por coma.
    // Por ejemplo: grass, poison
    public String getTiposTexto() {

        if (types == null || types.isEmpty()) {
            return "";
        }

        StringBuilder texto = new StringBuilder();

        for (TypeSlot slot : types) {

            if (slot.type == null) {
                continue;
            }

            // Si ya hay algo escrito le ponemos la coma antes
            if (texto.length() > 0) {
                texto.append(", ");
            }

            texto.append(slot.type.name);
        }

        return texto.toString();
    }

    // De aqui para abajo son clases pequenas que copian
    // como viene armado el JSON de la API.
    // Son privadas porque solo se usan dentro de esta clase.

    private static class Sprites {
        Other other;
    }

    private static class Other {
        // Esta llave lleva guion en el medio y en Java no se puede,
        // por eso otra vez usamos SerializedName
        @SerializedName("official-artwork")
        OfficialArtwork officialArtwork;
    }

    private static class OfficialArtwork {
        @SerializedName("front_default")
        String frontDefault;
    }

    private static class TypeSlot {
        Type type;
    }

    private static class Type {
        String name;
    }
}
