package ue.edu.co.pokenavigation.data.remote;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Esta clase prepara Retrofit para poder hablar con la API.
// Se hace una sola vez y se reusa en toda la app.
public class RetrofitClient {

    // Direccion principal de la API.
    // Tiene que terminar en / si no Retrofit da error.
    private static final String BASE_URL = "https://pokeapi.co/api/v2/";

    // Aqui se guarda el Retrofit ya armado
    private static Retrofit retrofit;

    // Constructor privado para que nadie pueda crear objetos de esta clase.
    // Todo se usa de forma estatica.
    private RetrofitClient() {
    }

    // Devuelve el servicio listo para hacer las peticiones
    public static PokeApiService getService() {

        // Solo se arma la primera vez que se llama
        if (retrofit == null) {

            // Esto sirve para ver en el Logcat las peticiones que salen
            // y la respuesta que llega. Ayuda mucho para saber si algo fallo.
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

            // Este es el que hace las peticiones a internet
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    // La direccion de la API
                    .baseUrl(BASE_URL)
                    // El cliente de arriba
                    .client(client)
                    // Gson convierte el JSON que llega en objetos de Java
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        // Retrofit arma solo el codigo de la interface
        return retrofit.create(PokeApiService.class);
    }
}
