package practica.hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Producto {
    private String nombre;
    private String descripcion;
    private String estado;
    private String modelo;
    private String marca;
    private int idProducto;
    private List<KitEducativo> kitEducativos;

    @Basic
    @Column(name = "nombre", nullable = false, length = 50)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "descripcion", nullable = false, length = 200)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "estado", nullable = false, length = 50)
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Basic
    @Column(name = "modelo", nullable = false, length = 50)
    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    @Basic
    @Column(name = "marca", nullable = false, length = 50)
    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(nombre, producto.nombre) &&
                Objects.equals(descripcion, producto.descripcion) &&
                Objects.equals(estado, producto.estado) &&
                Objects.equals(modelo, producto.modelo) &&
                Objects.equals(marca, producto.marca);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, descripcion, estado, modelo, marca);
    }

    @OneToMany(mappedBy = "idProducto")
    public List<KitEducativo> getKitEducativos() {
        return kitEducativos;
    }

    public void setKitEducativos(List<KitEducativo> kitEducativos) {
        this.kitEducativos = kitEducativos;
    }
    @Id
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
}
