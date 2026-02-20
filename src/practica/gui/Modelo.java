package practica.gui;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import practica.hibernate.KitEducativo;
import practica.modelo_Clases.Empresa;
import practica.modelo_Clases.Kit_Educativo;
import practica.modelo_Clases.Producto;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class Modelo {
    private String ip;
    private String user;
    private String password;
    private String db;
    private Connection conexion;
    public String deletePass;
    SessionFactory sessionFactory;

    public Modelo() {
        getPropValues();
    }

    private void getPropValues() {
        InputStream inputStream = null;
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = new FileInputStream(propFileName);

            prop.load(inputStream);
            ip = prop.getProperty("ip");
            user = prop.getProperty("user");
            password = prop.getProperty("pass");
            db = prop.getProperty("db");
            deletePass = prop.getProperty("delete_pass");
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void conectar() {

        try {
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://"+ip+":3306/"+db,user, password);
        } catch (SQLException sqle) {
            try {
                conexion = DriverManager.getConnection(
                        "jdbc:mysql://"+ip+":3306/",user, password);

                PreparedStatement statement = null;

                String code = leerFichero();
                String[] query = code.split("__");
                for (String aQuery : query) {
                    statement = conexion.prepareStatement(aQuery);
                    statement.executeUpdate();
                }
                assert statement != null;
                statement.close();

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }

        Configuration configuracion = new Configuration();
        //Cargo el fichero Hibernate.cfg.xml
        configuracion.configure("hibernate.cfg.xml");

        //Indico la clase mapeada con anotaciones
        configuracion.addAnnotatedClass(practica.hibernate.Empresa.class);
        configuracion.addAnnotatedClass(KitEducativo.class);
        configuracion.addAnnotatedClass(practica.hibernate.Producto.class);


        //Creamos un objeto ServiceRegistry a partir de los parámetros de configuración
        //Esta clase se usa para gestionar y proveer de acceso a servicios
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().applySettings(
                configuracion.getProperties()).build();

        //finalmente creamos un objeto sessionfactory a partir de la configuracion y del registro de servicios
        sessionFactory = configuracion.buildSessionFactory(ssr);

    }

    private String leerFichero() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/mysql.sql")) ) ;
        String linea;
        StringBuilder stringBuilder = new StringBuilder();
        while ((linea = reader.readLine()) != null) {
            stringBuilder.append(linea);
            stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }

    void desconectar() {
        try {
            conexion.close();
            conexion = null;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        if(sessionFactory != null && sessionFactory.isOpen())
            sessionFactory.close();

    }

    //PARTE PRODUCTO

    public ResultSet obtenerProductos() throws SQLException {
     String sql =   "SELECT " +
        "id_producto as id, " +
               " nombre, " +
               " descripcion, " +
               " estado, " +
               " modelo, " +
               " marca " +
        " FROM producto";
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        sentencia = conexion.prepareStatement(sql);
        resultado = sentencia.executeQuery();
        return resultado;

    }


    public void adicionarProducto(Producto p) throws SQLException {

        practica.hibernate.Producto producto = new practica.hibernate.Producto ();
        producto.setNombre(p.getNombre());
        producto.setDescripcion(p.getDescripcion());
        producto.setEstado(p.getEstado().name());
        producto.setModelo(p.getModelo());
        producto.setMarca(p.getMarca());
        insertar(producto);

    }

    public void actualizarProducto(Producto p) throws SQLException {
        String sql = "update producto set " +
                "nombre = ?," +
                "descripcion = ?," +
                "estado = ?," +
                "modelo = ?," +
                "marca = ?" +
                "WHERE id_producto = ?";

        PreparedStatement statement = conexion.prepareStatement(sql);
        statement.setString(1,p.getNombre());
        statement.setString(2,p.getDescripcion());
        statement.setString(3,p.getEstado().name());
        statement.setString(4,p.getModelo());
        statement.setString(5,p.getMarca());
        statement.setInt(6,p.getId());
        statement.executeUpdate();
    }


    void eliminarProducto(int id) {
        String sentenciaSql = "DELETE FROM producto WHERE id_producto = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }


    //PARTE KIT EDUCATIVO

    public void adicionarKitEducativo(Kit_Educativo k , practica.hibernate.Empresa e , practica.hibernate.Producto p) throws SQLException {


        practica.hibernate.KitEducativo kitEducativo = new practica.hibernate.KitEducativo ();
        kitEducativo.setNombre(k.getNombre());
        kitEducativo.setDescripcion(k.getDescripcion());
        kitEducativo.setCantidad(k.getCantidad());
        kitEducativo.setFechaDeCreacion(Date.valueOf(k.getFechaCreacion()));
        kitEducativo.setFechaDeActualizacion(Date.valueOf(k.getFechaActualizacion()));
        kitEducativo.setPrecio(k.getPrecio());
        kitEducativo.setValoracion(k.getValoracion());
        kitEducativo.setIdProducto(p);
        kitEducativo.setIdEmpresa(e);;
        insertar(kitEducativo);


    }

    public ResultSet obtenerKitEducativo() throws SQLException {
        String sql =   "SELECT  " +
                "k.id_kit," +
                "k.nombre," +
                "k.descripcion," +
                "k.cantidad," +
                "k.fecha_de_actualizacion," +
                "k.fecha_de_creacion," +
                "k.precio," +
                "k.valoracion, " +
                "concat(e.id_empresa,'-',e.nombre) as 'empresa' ,  " +
                "concat(p.id_producto,'-',p.nombre) as 'producto'  " +
                "FROM kit_educativo k " +
                "inner join empresa e on e.id_empresa = k.id_empresa " +
                "inner join producto p on p.id_producto = k.id_producto ";

        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        sentencia = conexion.prepareStatement(sql);
        resultado = sentencia.executeQuery();
        return resultado;



    }

    public void actualizarKitEducativo(Kit_Educativo k) throws SQLException {
        String sql = "update kit_educativo set " +
                "nombre = ?, " +
                "descripcion = ?, " +
                "cantidad = ?, " +
                "fecha_de_creacion = ?, " +
                "fecha_de_actualizacion = ?, " +
                "precio = ?, " +
                "valoracion = ?, " +
                "id_producto = ?, " +
                "id_empresa = ? " +
                "WHERE id_kit = ?";

        PreparedStatement statement = conexion.prepareStatement(sql);
        statement.setString(1,k.getNombre());
        statement.setString(2,k.getDescripcion());
        statement.setInt(3,k.getCantidad());
        statement.setDate(4,Date.valueOf(k.getFechaCreacion()));
        statement.setDate(5,Date.valueOf(k.getFechaActualizacion()));
        statement.setFloat(6,k.getPrecio());
        statement.setInt(7,k.getValoracion());
        statement.setInt(8,k.getProductoKit());
        statement.setInt(9,k.getEmpresasKit());
        statement.setInt(10,k.getId());
        statement.executeUpdate();
    }

    void eliminarKit(int id) {
        String sentenciaSql = "DELETE FROM kit_educativo WHERE id_kit = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }


    //PARTE EMPRESA



    public void adicionarEmpresas(Empresa em) throws SQLException {

        practica.hibernate.Empresa empresa = new practica.hibernate.Empresa ();
        empresa.setNombre(em.getNombre());
        empresa.setDescripcion(em.getDescripcion());
        empresa.setFechaDeCreacion(Date.valueOf(em.getFechaCreacion()));
        empresa.setUbicacion(em.getUbicacion());
        empresa.setValoracion(em.getValoracion());
        insertar(empresa);

    }

    public ResultSet obtenerEmpresas() throws SQLException {
        String sql =   "SELECT " +
                "id_empresa as id, " +
                " nombre, " +
                " descripcion, " +
                " fecha_de_creacion, " +
                " ubicacion, " +
                " valoracion " +
                " FROM empresa";
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        sentencia = conexion.prepareStatement(sql);
        resultado = sentencia.executeQuery();
        return resultado;
    }


    public void actualizarEmpresa(Empresa em) throws SQLException {
        String sql = "update empresa set " +
                "nombre = ?," +
                "descripcion = ?," +
                "fecha_de_creacion = ?," +
                "ubicacion = ?," +
                "valoracion = ? " +
                "WHERE id_empresa = ?";

        PreparedStatement statement = conexion.prepareStatement(sql);
        statement.setString(1,em.getNombre());
        statement.setString(2,em.getDescripcion());
        statement.setDate(3,Date.valueOf(em.getFechaCreacion()));
        statement.setString(4,em.getUbicacion());
        statement.setInt(5,em.getValoracion());
        statement.setInt(6,em.getId());
        statement.executeUpdate();
    }

    void eliminarEmpresa(int id) {
        String sentenciaSql = "DELETE FROM empresa WHERE id_empresa = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }


    void insertar(Object o) {
        //Obtengo una session a partir de la factoria de sesiones
        Session sesion = sessionFactory.openSession();

        sesion.beginTransaction();
        sesion.save(o);
        sesion.getTransaction().commit();

        sesion.close();
    }

    /***
     * Modificar un objeto de la BBDD
     * @param o objeto a modificar en la BBDD
     */
    void modificar(Object o) {
        Session sesion = sessionFactory.openSession();
        sesion.beginTransaction();
        sesion.saveOrUpdate(o);
        sesion.getTransaction().commit();
        sesion.close();
    }

    /***
     * Eliminar un objeto de la BBDD
     * @param o objeto a eliminar en la BBDD
     */
    void eliminar(Object o) {
        Session sesion = sessionFactory.openSession();
        sesion.beginTransaction();
        sesion.delete(o);
        sesion.getTransaction().commit();
        sesion.close();
    }

}
