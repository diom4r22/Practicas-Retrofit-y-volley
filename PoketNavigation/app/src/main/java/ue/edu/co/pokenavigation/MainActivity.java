package ue.edu.co.pokenavigation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import ue.edu.co.pokenavigation.ui.FavoritesFragment;
import ue.edu.co.pokenavigation.ui.HomeFragment;
import ue.edu.co.pokenavigation.ui.InfoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


// Esta es la unica Activity de la app.
// Su trabajo es mostrar el menu de abajo y cambiar la pantalla
// que se ve arriba segun la opcion que toque el usuario.
public class MainActivity extends AppCompatActivity {

    // El menu de abajo con las 3 opciones
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hace que la app use toda la pantalla
        EdgeToEdge.enable(this);

        // Se carga el diseno activity_main.xml
        setContentView(R.layout.activity_main);

        // Esto deja espacio para la barra de arriba y la de abajo del celular
        // para que el contenido no quede tapado
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Se busca el menu en el diseno
        initObjects();

        // 2. Se le dice al menu que hacer cuando toquen una opcion
        configurarBottomNavigation();

        // 3. Se abre la pantalla de Inicio apenas arranca la app.
        // El if es para que no se vuelva a abrir cuando se gira el celular.
        if (savedInstanceState == null) {
            bottomNavigation.setSelectedItemId(R.id.navigation_home);
        }
    }


    // Aqui se queda escuchando los toques del menu de abajo
    private void configurarBottomNavigation() {

        bottomNavigation.setOnItemSelectedListener(item -> {

            // Se pregunta que pantalla corresponde a la opcion tocada
            Fragment fragment = obtenerFragment(item.getItemId());

            // Si no hay ninguna, no se hace nada
            if (fragment == null) {
                return false;
            }

            cargarFragment(fragment);

            // true significa que la opcion queda marcada
            return true;
        });
    }

    // Cambia la pantalla que se ve dentro del contenedor del activity_main
    private void cargarFragment(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    // Recibe el id de la opcion del menu y devuelve la pantalla que le toca
    private Fragment obtenerFragment(int itemId) {

        if (itemId == R.id.navigation_home) {
            return new HomeFragment();
        }

        if (itemId == R.id.navigation_favorites) {
            return new FavoritesFragment();
        }

        if (itemId == R.id.navigation_info) {
            return new InfoFragment();
        }

        // Si llega un id raro se devuelve null
        return null;
    }

    // Busca el menu de abajo dentro del diseno
    private void initObjects() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }
}
