package practica.gui;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import practica.hibernate.Empresa;
import practica.hibernate.KitEducativo;
import practica.hibernate.Producto;


import javax.persistence.Query;
import javax.persistence.criteria.CriteriaDelete;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
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

    public ArrayList<Producto> obtenerProductos(){

        Session sesion = sessionFactory.openSession();
        Query query = sesion.createQuery("FROM Producto ");
        ArrayList<Producto> productos = (ArrayList<Producto>)query.getResultList();
        sesion.close();
        return productos;
    }






    //PARTE KIT EDUCATIVO



    public ArrayList<KitEducativo> obtenerKitEducativo()  {

        Session sesion = sessionFactory.openSession();
        Query query = sesion.createQuery("FROM KitEducativo ");
        ArrayList<KitEducativo> kitEducativos = (ArrayList<KitEducativo>)query.getResultList();
        sesion.close();
        return kitEducativos;
    }




    //PARTE EMPRESA




    public ArrayList<Empresa> obtenerEmpresas() {

        Session sesion = sessionFactory.openSession();
        Query query = sesion.createQuery("FROM Empresa ");
        ArrayList<Empresa> empresas = (ArrayList<Empresa>)query.getResultList();
        sesion.close();
        return empresas;
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
