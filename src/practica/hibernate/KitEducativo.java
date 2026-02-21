package practica.hibernate;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "kit_educativo", schema = "practica2_3ud", catalog = "")
public class KitEducativo {
    private int idKit;
    private String nombre;
    private String descripcion;
    private int cantidad;
    private Date fechaDeCreacion;
    private Date fechaDeActualizacion;
    private float precio;
    private int valoracion;
    private Empresa empresa;
    private Producto producto;

    @Id
    @Column(name = "id_kit", nullable = false)
    public int getIdKit() {
        return idKit;
    }

    public void setIdKit(int idKit) {
        this.idKit = idKit;
    }

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
    @Column(name = "cantidad", nullable = false)
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Basic
    @Column(name = "fecha_de_creacion", nullable = true)
    public Date getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(Date fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    @Basic
    @Column(name = "fecha_de_actualizacion", nullable = true)
    public Date getFechaDeActualizacion() {
        return fechaDeActualizacion;
    }

    public void setFechaDeActualizacion(Date fechaDeActualizacion) {
        this.fechaDeActualizacion = fechaDeActualizacion;
    }

    @Basic
    @Column(name = "precio", nullable = false, precision = 2)
    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    @Basic
    @Column(name = "valoracion", nullable = false)
    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KitEducativo that = (KitEducativo) o;
        return idKit == that.idKit &&
                cantidad == that.cantidad &&
                valoracion == that.valoracion &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(descripcion, that.descripcion) &&
                Objects.equals(fechaDeCreacion, that.fechaDeCreacion) &&
                Objects.equals(fechaDeActualizacion, that.fechaDeActualizacion) &&
                Objects.equals(precio, that.precio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idKit, nombre, descripcion, cantidad, fechaDeCreacion, fechaDeActualizacion, precio, valoracion);
    }

    @ManyToOne
    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa", nullable = false)
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa idEmpresa) {
        this.empresa = idEmpresa;
    }

    @ManyToOne
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto", nullable = false)
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto idProducto) {
        this.producto = idProducto;
    }

    public boolean valido () {

        if (nombre.trim().length() == 0) {
            return false;
        }
        if (descripcion.trim().length() == 0) {
            return false;
        }

        if (cantidad < 0
        ) {
            return false;
        }


        if (fechaDeCreacion == null) {
            return false;
        }
        if (fechaDeActualizacion == null) {
            return false;
        }

        if (precio < 0) {
            return false;
        }
        if (valoracion < 0) {
            return false;
        }

        if (empresa == null) {
            return false;
        }

        if (producto == null) {
            return false;
        }
        return true;

    }

    @Override
    public String toString() {
        return idKit +
                "-" + nombre +
                "-" + descripcion;
    }
}
