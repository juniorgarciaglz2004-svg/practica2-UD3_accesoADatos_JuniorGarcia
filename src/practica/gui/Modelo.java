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
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Clase modelo que permite manipular la base de datos y configuracion
 */
public class Modelo {

    private String ip;
    private String user;
    private String password;
    private String db;
    public String deletePass;
    SessionFactory sessionFactory;

    /**
     * Constructor de la clase que inicializa las propiedades de conexion
     */
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


        Configuration configuracion = new Configuration();
        //Cargo el fichero Hibernate.cfg.xml
        configuracion.configure("hibernate.cfg.xml");

        //Indico la clase mapeada con anotaciones
        configuracion.addAnnotatedClass(practica.hibernate.Empresa.class);
        configuracion.addAnnotatedClass(KitEducativo.class);
        configuracion.addAnnotatedClass(Producto.class);
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().applySettings(
                configuracion.getProperties()).build();
        sessionFactory = configuracion.buildSessionFactory(ssr);

    }


    void desconectar() {

        if(sessionFactory != null && sessionFactory.isOpen())
            sessionFactory.close();

    }

    //PARTE PRODUCTO

    /**
     * Obtiene el listado de productos
     * @return un listado de productos
     */
    public ArrayList<Producto> obtenerProductos(){

        Session sesion = sessionFactory.openSession();
        Query query = sesion.createQuery("FROM Producto ");
        ArrayList<Producto> productos = (ArrayList<Producto>)query.getResultList();
        sesion.close();
        return productos;
    }






    //PARTE KIT EDUCATIVO

    /**
     * Obtiene el listado de kits educativo
     * @return un listado de kits educativo
     */

    public ArrayList<KitEducativo> obtenerKitEducativo()  {

        Session sesion = sessionFactory.openSession();
        Query query = sesion.createQuery("FROM KitEducativo ");
        ArrayList<KitEducativo> kitEducativos = (ArrayList<KitEducativo>)query.getResultList();
        sesion.close();
        return kitEducativos;
    }




    //PARTE EMPRESA


    /**
     * Obtiene el listado de empresas
     * @return un listado de empresas
     */

    public ArrayList<Empresa> obtenerEmpresas() {

        Session sesion = sessionFactory.openSession();
        Query query = sesion.createQuery("FROM Empresa ");
        ArrayList<Empresa> empresas = (ArrayList<Empresa>)query.getResultList();
        sesion.close();
        return empresas;
    }


    /**
     * Inserta un objeto en la base de datos
     * @param o Objeto a insertar
     */

    void insertar(Object o) {

        Session sesion = sessionFactory.openSession();

        sesion.beginTransaction();
        sesion.save(o);
        sesion.getTransaction().commit();

        sesion.close();
    }



    /**
     * actualiza un objeto en la base de datos
     * @param o Objeto a actualizar
     */
    void modificar(Object o) {
        Session sesion = sessionFactory.openSession();
        sesion.beginTransaction();
        sesion.saveOrUpdate(o);
        sesion.getTransaction().commit();
        sesion.close();
    }

    /**
     * elimina un objeto en la base de datos
     * @param o Objeto a eliminar
     */
    void eliminar(Object o) {
        Session sesion = sessionFactory.openSession();
        sesion.beginTransaction();
        sesion.delete(o);
        sesion.getTransaction().commit();
        sesion.close();
    }

}
