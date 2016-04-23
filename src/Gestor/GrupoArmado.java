package Gestor;

/**
 * Created by Moises on 22/4/16.
 */
public class GrupoArmado {
    private int _id;
    private String nombre;
    private int bajas;

    public GrupoArmado() {
    }

    public GrupoArmado(int id, String nombre, int bajas) {
        this._id = id;
        this.nombre = nombre;
        this.bajas = bajas;
    }

    public int getBajas() {
        return bajas;
    }

    public void setBajas(int bajas) {
        this.bajas = bajas;
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
}