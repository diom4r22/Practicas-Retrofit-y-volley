package com.example.practica_volley.modelo;

public class Post {

    private int userId;
    private int id;
    private String titulo;
    private String cuerpo;

    public Post(int userId, int id, String titulo, String cuerpo) {
        this.userId = userId;
        this.id = id;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
    }

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    // El ArrayAdapter usa este texto para mostrar cada Post en el ListView
    @Override
    public String toString() {
        return "User ID: " + userId
                + "\nID: " + id
                + "\nTitulo: " + titulo
                + "\nBody: " + cuerpo;
    }
}
