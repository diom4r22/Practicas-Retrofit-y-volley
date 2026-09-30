# Prácticas de Consumo de Servicios Web en Android: Retrofit y Volley

Este repositorio contiene dos proyectos prácticos e independientes desarrollados en Android Studio, cuyo propósito es aprender e implementar el consumo de servicios web tipo REST API mediante dos de las bibliotecas de red más utilizadas en el ecosistema Android: **Retrofit** y **Volley**.

---

## 📁 Estructura del Repositorio

El repositorio está organizado con una arquitectura multi-proyecto (Monorepo), donde cada práctica se encuentra en su propio directorio con su configuración de Gradle correspondiente:

```text
Practicas-Retrofit-y-volley/
├── PoketNavigation/          # Práctica 1: Consumo de PokeAPI con Retrofit
│   ├── app/
│   ├── build.gradle.kts
│   ├── PokeNavigation_Informe_Correcciones_y_Actividad.pdf
│   └── ...
├── PracticaVolley/           # Práctica 2: Consumo de API REST con Volley
│   ├── app/
│   ├── build.gradle.kts
│   └── ...
├── .gitignore                # Reglas de exclusión de artefactos y cachés
└── README.md                 # Documentación del repositorio
```

---

## 🚀 Proyectos Incluidos

### 1. PoketNavigation (Retrofit)
Aplicación Android que consume datos en tiempo real de la [PokéAPI](https://pokeapi.co/) para consultar Pokémon, listar información detallada y gestionar favoritos.

- **Tecnologías y Librerías:**
  - **Retrofit 2**: Cliente HTTP para la comunicación y tipado de endpoints REST.
  - **Gson Converter**: Deserialización automática de JSON a modelos de datos Java.
  - **OkHttp Logging Interceptor**: Depuración e inspección de peticiones y respuestas HTTP.
  - **Glide**: Carga y renderizado eficiente de imágenes y sprites de Pokémon en caché.
  - **Android Jetpack Navigation & Fragments**: Flujo de navegación modular (Home, Detalle, Favoritos, Info).
  - **RecyclerView & CardView**: Listados dinámicos con interfaz visual moderna.
- **Documentación adicional:** Incluye el informe `PokeNavigation_Informe_Correcciones_y_Actividad.pdf` con la explicación de actividades y correcciones.

### 2. PracticaVolley (Volley)
Aplicación Android orientada a la práctica del consumo de APIs REST mediante la biblioteca oficial de Google **Volley**.

- **Tecnologías y Librerías:**
  - **Volley**: Gestión de colas de peticiones HTTP en segundo plano (`RequestQueue`), peticiones de cadenas y objetos JSON.
  - **RecyclerView**: Presentación dinámica de los datos consumidos.
  - **Android Jetpack / AppCompat / Material Components**: Componentes UI estándar de Android.

---

## 🛠️ Cómo Abrir y Ejecutar los Proyectos en Android Studio

Dado que son dos proyectos independientes dentro de un mismo repositorio:

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/diom4r22/Practicas-Retrofit-y-volley.git
   ```

2. **Abrir un proyecto específico:**
   - Abre **Android Studio**.
   - Haz clic en **File > Open...** (o *Open Project* en la pantalla de bienvenida).
   - Navega hasta la carpeta del repositorio clonado y **selecciona únicamente la subcarpeta del proyecto que desees abrir**:
     - Para la práctica de Retrofit: selecciona la carpeta `PoketNavigation`.
     - Para la práctica de Volley: selecciona la carpeta `PracticaVolley`.
   - Haz clic en **OK**.

3. **Sincronización:**
   - Android Studio detectará el archivo `build.gradle.kts` del proyecto seleccionado y descargará las dependencias necesarias de Gradle.
   - Conecta un emulador o dispositivo físico con depuración USB y presiona **Run ('app')** (`Shift + F10`).
