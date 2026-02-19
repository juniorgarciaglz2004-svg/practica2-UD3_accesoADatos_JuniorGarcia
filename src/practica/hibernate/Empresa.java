package practica.hibernate;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class Empresa {
    private int idEmpresa;
    private String nombre;
    private String descripcion;
    private Date fechaDeCreacion;
    private String ubicacion;
    private int valoracion;
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
    @Column(name = "fecha_de_creacion", nullable = true)
    public Date getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(Date fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    @Basic
    @Column(name = "ubicacion", nullable = false, length = 100)
    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
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
        Empresa empresa = (Empresa) o;
        return valoracion == empresa.valoracion &&
                Objects.equals(nombre, empresa.nombre) &&
                Objects.equals(descripcion, empresa.descripcion) &&
                Objects.equals(fechaDeCreacion, empresa.fechaDeCreacion) &&
                Objects.equals(ubicacion, empresa.ubicacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, descripcion, fechaDeCreacion, ubicacion, valoracion);
    }

    //
    @OneToMany(mappedBy = "idEmpresa")
    public List<KitEducativo> getKitEducativos() {
        return kitEducativos;
    }

    public void setKitEducativos(List<KitEducativo> kitEducativos) {
        this.kitEducativos = kitEducativos;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
    @Id
    public int getIdEmpresa() {
        return idEmpresa;
    }
}
