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

public class Controlador {
    private boolean refrescar;
    private Modelo modelo;
    private Vista vista;


    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
        modelo.conectar();
        adicionarActionListenersProductos();
        adicionarActionListenersEmpresas();
        adicionarActionListenersKit();

        refrescarTodo();
    }


    private void adicionarActionListenersKit() {
        vista.anadir_KitsButton.addActionListener(e -> adicionarKits());
        vista.eliminar_KitsButton.addActionListener(e -> eliminarKits());
        vista.modificar_KitsButton.addActionListener(e -> modificarkits());
       // vista.listaKits.setCellSelectionEnabled(true);
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
                    vista.comboBoxEmpresaKit.setSelectedItem(k.getIdEmpresa());
                    vista.comboBoxProductoKits.setSelectedItem(k.getIdProducto());


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

    private void modificarkits() {
        if (vista.listaKits.getSelectedIndex() == -1) {
            //no se ha seleccionado ninguna fila
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
            p.setIdEmpresa((Empresa) vista.comboBoxEmpresaKit.getSelectedItem());
        }
        if (vista.comboBoxProductoKits.getSelectedIndex() >= 0) {
            p.setIdProducto((Producto) vista.comboBoxProductoKits.getSelectedItem());
        }

        if (p.valido()) {
            modelo.modificar(p);
            resfrecarKits();
            borrarCamposKits();

        } else {
            Util.showErrorAlert("Rellene todos los campos");
        }


    }


    private void eliminarKits() {
        if (vista.listaKits.getSelectedIndex() == -1) {
            //no se ha seleccionado ninguna fila
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


    private void modificarProducto() {

        if (vista.listaProducto.getSelectedIndex() == -1) {
            //no se ha seleccionado ninguna fila
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

    private void eliminarProducto() {
        if (vista.listaProducto.getSelectedIndex() == -1) {
            //no se ha seleccionado ninguna fila
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



    private void adicionarProducto() {
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

    private void eliminarEmpresas() {

        if (vista.listaEmpresa.getSelectedIndex() == -1) {
            //no se ha seleccionado ninguna fila
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

    private void modificarEmpresas() {
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

    private void adicionarEmpresas() {

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


    private void adicionarKits() {
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
            p.setIdEmpresa((Empresa) vista.comboBoxEmpresaKit.getSelectedItem());
        }
        if (vista.comboBoxProductoKits.getSelectedIndex() >= 0) {
            p.setIdProducto((Producto) vista.comboBoxProductoKits.getSelectedItem());
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
