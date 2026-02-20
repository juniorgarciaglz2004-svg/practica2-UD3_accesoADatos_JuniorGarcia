package practica.gui;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import practica.hibernate.KitEducativo;


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
