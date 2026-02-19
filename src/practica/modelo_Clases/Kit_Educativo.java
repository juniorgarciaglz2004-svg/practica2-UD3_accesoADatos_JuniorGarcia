package practica.modelo_Clases;

import java.time.LocalDate;

public class Kit_Educativo {

    // ARRAY DE EMPRESAS KIT Y PRODUCTOS KIT
    private int id;
    private String nombre;
    private String descripcion;
    private int cantidad;
    private int empresasKit;
    private int productoKit;
    private LocalDate fechaCreacion;
    private LocalDate fechaActualizacion;
    private float precio;
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getEmpresasKit() {
        return empresasKit;
    }

    public void setEmpresasKit(int empresasKit) {
        this.empresasKit = empresasKit;
    }

    public int getProductoKit() {
        return productoKit;
    }

    public void setProductoKit(int productoKit) {
        this.productoKit = productoKit;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDate fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    public Kit_Educativo(int id, String nombre, String descripcion, int cantidad, int empresasKit, int productoKit, LocalDate fechaCreacion, LocalDate fechaActualizacion, float precio, int valoracion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.empresasKit = empresasKit;
        this.productoKit = productoKit;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.precio = precio;
        this.valoracion = valoracion;
    }

    public Kit_Educativo() {
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


            if (fechaCreacion == null) {
                return false;
            }
            if (fechaActualizacion == null) {
                return false;
            }

            if (precio < 0) {
                return false;
            }
            if (valoracion < 0) {
                return false;
            }

            if (empresasKit < 0) {
                return false;
            }

            if (productoKit < 0) {
                return false;
            }
            return true;

        }
}
