package ue.edu.co.pokenavigation.ui;

import ue.edu.co.pokenavigation.R;

import androidx.fragment.app.Fragment;

// Pantalla de Informacion.
// Es la mas sencilla de las tres, solo muestra un texto fijo
// que esta escrito en el diseno fragment_info.xml.
// No necesita codigo extra porque no tiene botones ni listas.
public class InfoFragment extends Fragment {

    // Solo se le indica cual es su diseno
    public InfoFragment() {
        super(R.layout.fragment_info);
    }
}
