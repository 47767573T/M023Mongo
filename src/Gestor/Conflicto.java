package Gestor;

import java.util.ArrayList;

/**
 * Created by enric on 20/4/16.
 */
public class Conflicto {
    private int _id;
    private String nombre;
    private String zona;
    private int heridos;

    private ArrayList<GrupoArmado> listaGruposArmados = new ArrayList<>();

    public Conflicto(int id, String nombre, String zona, int heridos) {
        this._id = id;
        this.nombre = nombre;
        this.zona = zona;
        this.heridos = heridos;
    }

    public Conflicto() {
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public int getHeridos() {
        return heridos;
    }

    public void setHeridos(int heridos) {
        this.heridos = heridos;
    }

    public ArrayList<GrupoArmado> getListaGruposArmados() {
        return listaGruposArmados;
    }

    public void setListaGruposArmados(ArrayList<GrupoArmado> listaGruposArmados) {
        this.listaGruposArmados = listaGruposArmados;
    }
}