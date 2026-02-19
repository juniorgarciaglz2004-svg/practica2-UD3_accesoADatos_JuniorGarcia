package practica.modelo_Clases;

import javax.persistence.*;

@Entity
@Table(name = "producto", schema = "practica1ud3", catalog = "")
public class Producto {
private int id;
private String nombre;
private String descripcion;
private String modelo;
private String marca;
private EstadoProducto estado;

    public EstadoProducto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProducto estado) {
        this.estado = estado;
    }
    @Id
    @Column(name = "id_producto", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Basic
    @Column(name = "nombre", nullable = false, length = 50)
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Producto(int id, String nombre, String descripcion, String modelo, String marca, EstadoProducto estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.modelo = modelo;
        this.marca = marca;
        this.estado = estado;
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

        if (modelo.trim().length()==0)
        {
            return false;
        }

        if (marca.trim().length()==0)
        {
            return false;
        }
        if (estado==null)
        {
            return false;
        }


        return true;
    }

    public Producto() {
    }
}
