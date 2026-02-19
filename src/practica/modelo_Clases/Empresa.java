package practica.modelo_Clases;

import java.time.LocalDate;

public class Empresa {

    private int id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaCreacion;
    private String ubicacion;
    private int valoracion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    public Empresa(int id, String nombre, String descripcion, LocalDate fechaCreacion, String ubicacion, int valoracion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.ubicacion = ubicacion;
        this.valoracion = valoracion;
    }

    public Empresa() {
    }


    public boolean valido () {

        if (nombre.trim().length()==0)
        {
            return false;
        }
        if (descripcion.trim().length()==0)
        {
            return false;
        }

        if (fechaCreacion==null)
        {
            return false;
        }

        if (ubicacion.trim().length()==0)
        {
            return false;
        }
        if (valoracion<0)
        {
            return false;
        }


        return true;
    }



}
