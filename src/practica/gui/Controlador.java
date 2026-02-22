package practica.gui;


import practica.hibernate.Empresa;
import practica.hibernate.EstadoProducto;
import practica.hibernate.KitEducativo;
import practica.hibernate.Producto;
import practica.util.Util;

import javax.swing.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Clase controlador para la interaccion entre la vista y el modelo
 */

public class Controlador {
    private boolean refrescar;
    private Modelo modelo;
    private Vista vista;
    private boolean conectado;

    /**
     * Constructor De La Clase
     * @param modelo Modelo con las operaciones de la base de datos
     * @param vista Vista conteniendo los elementos de gui
     */
    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.conectado = false;
        adicionarActionListenersProductos();
        adicionarActionListenersEmpresas();
        adicionarActionListenersKit();
        adicionarListenersMenu();

    }

    /**
     * Adiciona los listeners para las acciones del menu
     */
    private void adicionarListenersMenu(){


        vista.desconectar.addActionListener(e->{
            if (!conectado) {
                JOptionPane.showMessageDialog(null, "No has conectado con la BBDD",
                        "Error de conexión", JOptionPane.ERROR_MESSAGE);
                return;
            }
            vista.desconectar.setEnabled(false);
            vista.conexionItem.setEnabled(true);
            modelo.desconectar();
            JOptionPane.showMessageDialog(null, "Usted ha desconectado con la base de datos, vuelvela a conectar cuando desee");
            conectado = false;
        });
        vista.conexionItem.addActionListener(e->{

            vista.conexionItem.setEnabled(false);
            vista.desconectar.setEnabled(true);
            modelo.conectar();
            refrescarTodo();
            JOptionPane.showMessageDialog(null, "Usted ha conectado con la base de datos, todo funcionara a la perfección");
            conectado = true;});
        vista.salirItem.addActionListener(e->{
            modelo.desconectar();
            System.exit(0);});
    }


    private void adicionarActionListenersKit() {
        vista.anadir_KitsButton.addActionListener(e -> adicionarKits());
        vista.eliminar_KitsButton.addActionListener(e -> eliminarKits());
        vista.modificar_KitsButton.addActionListener(e -> modificarkits());
        ListSelectionModel kitsModelSeleccion = vista.listaKits.getSelectionModel();
        kitsModelSeleccion.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                if (e.getSource().equals(vista.listaKits.getSelectionModel())) {
                    KitEducativo k = (KitEducativo)vista.listaKits.getSelectedValue();

                    vista.nombreKit.setText(k.getNombre());
                    vista.descripcionKit.setText(k.getDescripcion());
                    vista.cantidadKit.setText(String.valueOf(k.getCantidad()) );
                    vista.fecha_CreacionKits.setDate(k.getFechaDeCreacion().toLocalDate());
                    vista.fecha_ActualizacionKits.setDate(k.getFechaDeActualizacion().toLocalDate());
                    vista.precioKit.setText(String.valueOf(k.getPrecio()));
                    vista.valoracionSliderKit.setValue( k.getValoracion());
                    vista.comboBoxEmpresaKit.setSelectedItem(k.getEmpresa());
                    vista.comboBoxProductoKits.setSelectedItem(k.getProducto());


                } else if (e.getValueIsAdjusting()
                        && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                    if (e.getSource().equals(vista.listaKits.getSelectionModel())) {
                        borrarCamposKits();
                    } else if (e.getSource().equals(vista.listaEmpresa.getSelectionModel())) {
                        borrarCamposEmpresa();
                    } else if (e.getSource().equals(vista.listaProducto.getSelectionModel())) {
                        borrarCamposProductos();
                    }
                }
            }
        });
    }

    /**
     * Actualiza el kit seleccionado
     */
    private void modificarkits() {
        if(!estaConectado())
        {
            return;
        }
        if (vista.listaKits.getSelectedIndex() == -1) {
            return;
        }

        KitEducativo p = (KitEducativo) vista.listaKits.getSelectedValue();
        p.setNombre(vista.nombreKit.getText());
        p.setDescripcion(vista.descripcionKit.getText());
        if (vista.cantidadKit.getText().trim().length() > 0) {
            p.setCantidad(Integer.parseInt(vista.cantidadKit.getText()));
        }
        p.setFechaDeCreacion(Date.valueOf(vista.fecha_CreacionKits.getDate()));
        p.setFechaDeActualizacion(Date.valueOf(vista.fecha_ActualizacionKits.getDate()));
        p.setValoracion(vista.valoracionSliderKit.getValue());
        if (vista.precioKit.getText().trim().length() > 0) {
            p.setPrecio(Float.parseFloat(vista.precioKit.getText()));
        }

        if (vista.comboBoxEmpresaKit.getSelectedIndex() >= 0) {
            p.setEmpresa((Empresa) vista.comboBoxEmpresaKit.getSelectedItem());
        }
        if (vista.comboBoxProductoKits.getSelectedIndex() >= 0) {
            p.setProducto((Producto) vista.comboBoxProductoKits.getSelectedItem());
        }

        if (p.valido()) {
            modelo.modificar(p);
            resfrecarKits();
            borrarCamposKits();

        } else {
            Util.showErrorAlert("Rellene todos los campos");
        }


    }

    /**
    Elimina el kit seleccionado
     */
    private void eliminarKits() {
        if(!estaConectado())
        {
            return;
        }
        if (vista.listaKits.getSelectedIndex() == -1) {
            return;
        }
        vista.crearDialogoSeguridad();
        vista.btnValidate.addActionListener(e -> {
            if (String.valueOf(vista.contrasenaDeEliminacion.getPassword()).equals(modelo.deletePass)) {
                vista.contrasenaDeEliminacion.setText("");
                vista.dialogoDeEliminacion.dispose();

                modelo.eliminar(vista.listaKits.getSelectedValue());
                borrarCamposKits();
                resfrecarKits();
            } else {
                Util.showErrorAlert("La contraseña es incorrecta, introduzcala de nuevo");
            }
        });
        vista.dialogoDeEliminacion.setVisible(true);




    }

    /**
    Refresca todos los campos
     */
    private void refrescarTodo() {
        resfrescarProductos();
        resfrecarEmpresa();
        resfrecarKits();
        refrescar = false;
    }


    private void adicionarActionListenersProductos() {
        vista.anadir_PRODUCTOSButton.addActionListener(e -> adicionarProducto());
        vista.eliminar_ProductosButton.addActionListener(e -> eliminarProducto());
        vista.modificar_ProductosButton.addActionListener(e -> modificarProducto());

        ListSelectionModel productoModelSeleccion = vista.listaProducto.getSelectionModel();
        productoModelSeleccion.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                if (e.getSource().equals(vista.listaProducto.getSelectionModel())) {
                    Producto k = (Producto)vista.listaProducto.getSelectedValue();
                    vista.nombreProducto.setText(k.getNombre());
                    vista.descripcionProducto.setText(k.getDescripcion());
                    if (k.getEstado() == EstadoProducto.NUEVO) {
                        vista.productosEstadoNuevos.setSelected(true);
                    } else if (k.getEstado() == EstadoProducto.USADO) {
                        vista.productoEstadoUsado.setSelected(true);
                    } else {
                        vista.productosEstadoReacondicionados.setSelected(true);
                    }
                    vista.modeloProducto.setText((k.getModelo()));
                    vista.marcaProducto.setText(k.getMarca());


                } else if (e.getValueIsAdjusting()
                        && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                    if (e.getSource().equals(vista.listaKits.getSelectionModel())) {
                        borrarCamposKits();
                    } else if (e.getSource().equals(vista.listaEmpresa.getSelectionModel())) {
                        borrarCamposEmpresa();
                    } else if (e.getSource().equals(vista.listaProducto.getSelectionModel())) {
                        borrarCamposProductos();
                    }
                }
            }
        });


    }


    private void adicionarActionListenersEmpresas() {
        vista.anadir_EmpresaButton.addActionListener(e -> adicionarEmpresas());
        vista.eliminar_EmpresaButton.addActionListener(e -> eliminarEmpresas());
        vista.modificar_EmpresaButton.addActionListener(e -> modificarEmpresas());
        ListSelectionModel empresaModelSeleccion = vista.listaEmpresa.getSelectionModel();
        empresaModelSeleccion.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                if (e.getSource().equals(vista.listaEmpresa.getSelectionModel())) {
                   Empresa a = (Empresa) vista.listaEmpresa.getSelectedValue();
                    vista.nombreEmpresa.setText(a.getNombre());
                    vista.descripcionEmpresa.setText(a.getDescripcion());

                    vista.fecha_creacion_Empresa.setDate(a.getFechaDeCreacion().toLocalDate());

                    vista.ubicacionEmpresa.setText(a.getUbicacion());
                    vista.valoracionSliderEmpresa.setValue(a.getValoracion());


                } else if (e.getValueIsAdjusting()
                        && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                    if (e.getSource().equals(vista.listaKits.getSelectionModel())) {
                        borrarCamposKits();
                    } else if (e.getSource().equals(vista.listaEmpresa.getSelectionModel())) {
                        borrarCamposEmpresa();
                    } else if (e.getSource().equals(vista.listaProducto.getSelectionModel())) {
                        borrarCamposProductos();
                    }
                }
            }
        });


    }

    /**
     * Actualiza el producto seleeccionado
     */
    private void modificarProducto() {
        if(!estaConectado())
        {
            return;
        }
        if (vista.listaProducto.getSelectedIndex() == -1) {

            return;
        }

        Producto p = (Producto) vista.listaProducto.getSelectedValue();
        p.setNombre(vista.nombreProducto.getText());
        p.setDescripcion(vista.descripcionProducto.getText());
        p.setMarca(vista.marcaProducto.getText());
        p.setModelo(vista.modeloProducto.getText());
        if (vista.productoEstadoUsado.isSelected()) {
            p.setEstado(EstadoProducto.USADO);
        }
        if (vista.productosEstadoNuevos.isSelected()) {
            p.setEstado(EstadoProducto.NUEVO);
        }
        if (vista.productosEstadoReacondicionados.isSelected()) {
            p.setEstado(EstadoProducto.REACONDICIONADO);
        }

        if (p.valido()) {
            modelo.modificar(p);
            resfrescarProductos();
            vista.nombreProducto.setText("");
            vista.descripcionProducto.setText("");
            vista.marcaProducto.setText("");
            vista.modeloProducto.setText("");
            vista.productoEstadoUsado.setSelected(true);
        } else {
            Util.showErrorAlert("Rellene todos los campos");
        }


    }

    /**
     * Elimina el producto seleccionado
     */
    private void eliminarProducto() {
        if(!estaConectado())
        {
            return;
        }
        if (vista.listaProducto.getSelectedIndex() == -1) {
            return;
        }


        vista.crearDialogoSeguridad();
        vista.btnValidate.addActionListener(e -> {
            if (String.valueOf(vista.contrasenaDeEliminacion.getPassword()).equals(modelo.deletePass)) {
                vista.contrasenaDeEliminacion.setText("");
                vista.dialogoDeEliminacion.dispose();

                modelo.eliminar(vista.listaProducto.getSelectedValue());
                borrarCamposProductos();
                resfrescarProductos();
            } else {
                Util.showErrorAlert("La contraseña es incorrecta, introduzcala de nuevo");
            }
        });
        vista.dialogoDeEliminacion.setVisible(true);
    }

    private void borrarCamposProductos() {
        vista.nombreProducto.setText("");
        vista.descripcionProducto.setText("");
        vista.productoEstadoUsado.setSelected(true);


        vista.modeloProducto.setText("");
        vista.marcaProducto.setText("");
    }

    private void borrarCamposEmpresa() {
        vista.nombreEmpresa.setText("");
        vista.descripcionEmpresa.setText("");
        vista.ubicacionEmpresa.setText("");
        vista.fecha_creacion_Empresa.setDate(LocalDate.now());
        vista.valoracionSliderEmpresa.setValue(0);

    }

    private void borrarCamposKits() {
        vista.nombreKit.setText("");
        vista.descripcionKit.setText("");
        vista.cantidadKit.setText("");
        vista.comboBoxEmpresaKit.setSelectedIndex(-1);
        vista.comboBoxProductoKits.setSelectedIndex(-1);
        vista.fecha_CreacionKits.setDate(LocalDate.now());
        vista.fecha_ActualizacionKits.setDate(LocalDate.now());
        vista.precioKit.setText("");
        vista.valoracionSliderKit.setValue(0);
    }

    //Parte de productos

    void resfrescarProductos() {

        ArrayList<Producto> productos = modelo.obtenerProductos();
        vista.dtmProductos.clear();
        vista.comboBoxProductoKits.removeAllItems();

        for (Producto p : productos) {
            vista.comboBoxProductoKits.addItem(p);
            vista.dtmProductos.addElement(p);
        }

    }


    /**
     * Adiciona un producto
     */

    private void adicionarProducto() {
        if(!estaConectado())
        {
            return;
        }
        Producto p = new Producto();
        p.setNombre(vista.nombreProducto.getText());
        p.setDescripcion(vista.descripcionProducto.getText());
        p.setMarca(vista.marcaProducto.getText());
        p.setModelo(vista.modeloProducto.getText());
        if (vista.productoEstadoUsado.isSelected()) {
            p.setEstado(EstadoProducto.USADO);
        }
        if (vista.productosEstadoNuevos.isSelected()) {
            p.setEstado(EstadoProducto.NUEVO);
        }
        if (vista.productosEstadoReacondicionados.isSelected()) {
            p.setEstado(EstadoProducto.REACONDICIONADO);
        }

        if (p.valido()) {
            modelo.insertar(p);
            resfrescarProductos();
            vista.nombreProducto.setText("");
            vista.descripcionProducto.setText("");
            vista.marcaProducto.setText("");
            vista.modeloProducto.setText("");
            vista.productoEstadoUsado.setSelected(true);
        } else {
            Util.showErrorAlert("Rellene todos los campos");
        }

    }





    //parte de empresa

    /**
     * Elimina la empresa seleccionada
     */
    private void eliminarEmpresas() {
        if(!estaConectado())
        {
            return;
        }
        if (vista.listaEmpresa.getSelectedIndex() == -1) {

            return;
        }



        vista.crearDialogoSeguridad();
        vista.btnValidate.addActionListener(e -> {
            if (String.valueOf(vista.contrasenaDeEliminacion.getPassword()).equals(modelo.deletePass)) {
                vista.contrasenaDeEliminacion.setText("");
                vista.dialogoDeEliminacion.dispose();

                modelo.eliminar(vista.listaEmpresa.getSelectedValue());
                borrarCamposEmpresa();
                resfrecarEmpresa();
            } else {
                Util.showErrorAlert("La contraseña es incorrecta, introduzcala de nuevo");
            }
        });
        vista.dialogoDeEliminacion.setVisible(true);

    }

    /**
     * Modifica la empresa seleccionada
     */
    private void modificarEmpresas() {
        if(!estaConectado())
        {
            return;
        }
        if (vista.listaEmpresa.getSelectedIndex()==-1) {
            //no se ha seleccionado ninguna fila
            return;
        }

        Empresa p = (Empresa) vista.listaEmpresa.getSelectedValue();
        p.setNombre(vista.nombreEmpresa.getText());
        p.setDescripcion(vista.descripcionEmpresa.getText());
        p.setFechaDeCreacion(Date.valueOf(vista.fecha_creacion_Empresa.getDate()));
        p.setUbicacion(vista.ubicacionEmpresa.getText());
        p.setValoracion(vista.valoracionSliderEmpresa.getValue());


        if (p.valido()) {
            modelo.modificar(p);
            resfrecarEmpresa();
            borrarCamposEmpresa();

        } else {
            Util.showErrorAlert("Rellene todos los campos");
        }

    }

    void resfrecarEmpresa() {
        vista.dtmEmpresa.clear();
        vista.comboBoxEmpresaKit.removeAllItems();

        for (Empresa e : modelo.obtenerEmpresas()){
            vista.dtmEmpresa.addElement(e);
            vista.comboBoxEmpresaKit.addItem(e);
        }

    }

    /**
     * Adiciona una empresa
     */

    private void adicionarEmpresas() {
        if(!estaConectado())
        {
            return;
        }
        Empresa p = new Empresa();
        p.setNombre(vista.nombreEmpresa.getText());
        p.setDescripcion(vista.descripcionEmpresa.getText());
        p.setFechaDeCreacion(Date.valueOf(vista.fecha_creacion_Empresa.getDate()));

        p.setUbicacion(vista.ubicacionEmpresa.getText());
        p.setValoracion(vista.valoracionSliderEmpresa.getValue());


        if (p.valido()) {
            modelo.insertar(p);
            resfrecarEmpresa();
            borrarCamposEmpresa();
        } else {
            Util.showErrorAlert("Rellene todos los campos");
        }


    }



    //parte de kits

    void resfrecarKits()  {
        vista.dtmKits.clear();

        for (KitEducativo e : modelo.obtenerKitEducativo()){
            vista.dtmKits.addElement(e);
        }

    }

    private boolean estaConectado(){

        if (!conectado) {
            JOptionPane.showMessageDialog(null, "No has conectado con la BBDD",
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Adiciona un kit educativo
     */
    private void adicionarKits() {
        if(!estaConectado())
        {
            return;
        }
        KitEducativo p = new KitEducativo();
        p.setNombre(vista.nombreKit.getText());
        p.setDescripcion(vista.descripcionKit.getText());
        if (vista.cantidadKit.getText().trim().length() > 0) {
            p.setCantidad(Integer.parseInt(vista.cantidadKit.getText()));
        }
        p.setFechaDeCreacion(Date.valueOf(vista.fecha_CreacionKits.getDate()));
        p.setFechaDeActualizacion(Date.valueOf(vista.fecha_ActualizacionKits.getDate()));

        p.setValoracion(vista.valoracionSliderKit.getValue());
        if (vista.precioKit.getText().trim().length() > 0) {
            p.setPrecio(Float.parseFloat(vista.precioKit.getText()));
        }


        if (vista.comboBoxEmpresaKit.getSelectedIndex() >= 0) {
            p.setEmpresa((Empresa) vista.comboBoxEmpresaKit.getSelectedItem());
        }
        if (vista.comboBoxProductoKits.getSelectedIndex() >= 0) {
            p.setProducto((Producto) vista.comboBoxProductoKits.getSelectedItem());
        }


        if (p.valido()) {

                modelo.insertar(p);
            resfrecarKits();
            borrarCamposKits();
        } else {
            Util.showErrorAlert("Rellene todos los campos");
        }
    }




}
