package practica.gui;


import org.dom4j.rule.Mode;
import practica.hibernate.Empresa;
import practica.hibernate.EstadoProducto;
import practica.hibernate.KitEducativo;
import practica.hibernate.Producto;
import practica.util.Util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
        vista.tablaKits.setCellSelectionEnabled(true);
        ListSelectionModel kitsModelSeleccion = vista.tablaKits.getSelectionModel();
        kitsModelSeleccion.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                if (e.getSource().equals(vista.tablaKits.getSelectionModel())) {
                    int row = vista.tablaKits.getSelectedRow();
                    vista.nombreKit.setText(String.valueOf(vista.tablaKits.getValueAt(row, 1)));
                    vista.descripcionKit.setText(String.valueOf(vista.tablaKits.getValueAt(row, 2)));
                    vista.cantidadKit.setText(String.valueOf(vista.tablaKits.getValueAt(row, 3)));
                    vista.fecha_CreacionKits.setDate(((Date) vista.tablaKits.getValueAt(row, 4)).toLocalDate());
                    vista.fecha_ActualizacionKits.setDate(((Date) vista.tablaKits.getValueAt(row, 5)).toLocalDate());
                    vista.precioKit.setText(String.valueOf(vista.tablaKits.getValueAt(row, 6)));
                    vista.valoracionSliderKit.setValue((Integer) vista.tablaKits.getValueAt(row, 7));
                    vista.comboBoxEmpresaKit.setSelectedItem(String.valueOf(vista.tablaKits.getValueAt(row, 8)));
                    vista.comboBoxProductoKits.setSelectedItem(String.valueOf(vista.tablaKits.getValueAt(row, 9)));


                } else if (e.getValueIsAdjusting()
                        && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                    if (e.getSource().equals(vista.tablaKits.getSelectionModel())) {
                        borrarCamposKits();
                    } else if (e.getSource().equals(vista.tablaEmrpresa.getSelectionModel())) {
                        borrarCamposEmpresa();
                    } else if (e.getSource().equals(vista.tablaProductos.getSelectionModel())) {
                        borrarCamposProductos();
                    }
                }
            }
        });
    }

    private void modificarkits() {
        if (vista.tablaKits.getSelectedRow() == -1) {
            //no se ha seleccionado ninguna fila
            return;
        }

        KitEducativo p = new KitEducativo();
        p.setIdKit((Integer) vista.tablaKits.getValueAt(vista.tablaKits.getSelectedRow(), 0));
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
        if (vista.tablaKits.getSelectedRow() == -1) {
            //no se ha seleccionado ninguna fila
            return;
        }
        vista.crearDialogoSeguridad();
        vista.btnValidate.addActionListener(e -> {
            if (String.valueOf(vista.contrasenaDeEliminacion.getPassword()).equals(modelo.deletePass)) {
                vista.contrasenaDeEliminacion.setText("");
                vista.dialogoDeEliminacion.dispose();


                int id = Integer.parseInt(String.valueOf(vista.tablaKits.getValueAt(vista.tablaKits.getSelectedRow(), 0)));
                modelo.eliminarKit(id);
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
        vista.tablaProductos.setCellSelectionEnabled(true);
        ListSelectionModel productoModelSeleccion = vista.tablaProductos.getSelectionModel();
        productoModelSeleccion.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                if (e.getSource().equals(vista.tablaProductos.getSelectionModel())) {
                    int row = vista.tablaProductos.getSelectedRow();
                    vista.nombreProducto.setText(String.valueOf(vista.tablaProductos.getValueAt(row, 1)));
                    vista.descripcionProducto.setText(String.valueOf(vista.tablaProductos.getValueAt(row, 2)));
                    EstadoProducto estadoProducto = EstadoProducto.valueOf(String.valueOf(vista.tablaProductos.getValueAt(row, 3)));
                    if (estadoProducto == EstadoProducto.NUEVO) {
                        vista.productosEstadoNuevos.setSelected(true);
                    } else if (estadoProducto == EstadoProducto.USADO) {
                        vista.productoEstadoUsado.setSelected(true);
                    } else {
                        vista.productosEstadoReacondicionados.setSelected(true);
                    }
                    vista.modeloProducto.setText(String.valueOf(vista.tablaProductos.getValueAt(row, 4)));
                    vista.marcaProducto.setText(String.valueOf(vista.tablaProductos.getValueAt(row, 5)));


                } else if (e.getValueIsAdjusting()
                        && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                    if (e.getSource().equals(vista.tablaKits.getSelectionModel())) {
                        borrarCamposKits();
                    } else if (e.getSource().equals(vista.tablaEmrpresa.getSelectionModel())) {
                        borrarCamposEmpresa();
                    } else if (e.getSource().equals(vista.tablaProductos.getSelectionModel())) {
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
        vista.tablaEmrpresa.setCellSelectionEnabled(true);
        ListSelectionModel empresaModelSeleccion = vista.tablaEmrpresa.getSelectionModel();
        empresaModelSeleccion.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                if (e.getSource().equals(vista.tablaEmrpresa.getSelectionModel())) {
                    int row = vista.tablaEmrpresa.getSelectedRow();
                    vista.nombreEmpresa.setText(String.valueOf(vista.tablaEmrpresa.getValueAt(row, 1)));
                    vista.descripcionEmpresa.setText(String.valueOf(vista.tablaEmrpresa.getValueAt(row, 2)));

                    vista.fecha_creacion_Empresa.setDate(((Date) vista.tablaEmrpresa.getValueAt(row, 3)).toLocalDate());

                    vista.ubicacionEmpresa.setText(String.valueOf(vista.tablaEmrpresa.getValueAt(row, 4)));
                    vista.valoracionSliderEmpresa.setValue((Integer) vista.tablaEmrpresa.getValueAt(row, 5));


                } else if (e.getValueIsAdjusting()
                        && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                    if (e.getSource().equals(vista.tablaKits.getSelectionModel())) {
                        borrarCamposKits();
                    } else if (e.getSource().equals(vista.tablaEmrpresa.getSelectionModel())) {
                        borrarCamposEmpresa();
                    } else if (e.getSource().equals(vista.tablaProductos.getSelectionModel())) {
                        borrarCamposProductos();
                    }
                }
            }
        });


    }


    private void modificarProducto() {

        if (vista.tablaProductos.getSelectedRow() == -1) {
            //no se ha seleccionado ninguna fila
            return;
        }

        Producto p = new Producto();
        p.setIdProducto((Integer) vista.tablaProductos.getValueAt(vista.tablaProductos.getSelectedRow(), 0));
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
        if (vista.tablaProductos.getSelectedRow() == -1) {
            //no se ha seleccionado ninguna fila
            return;
        }


        vista.crearDialogoSeguridad();
        vista.btnValidate.addActionListener(e -> {
            if (String.valueOf(vista.contrasenaDeEliminacion.getPassword()).equals(modelo.deletePass)) {
                vista.contrasenaDeEliminacion.setText("");
                vista.dialogoDeEliminacion.dispose();


                int id = Integer.parseInt(String.valueOf(vista.tablaProductos.getValueAt(vista.tablaProductos.getSelectedRow(), 0)));
                modelo.eliminarProducto(id);
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
        try {
            ArrayList<Producto> productos = modelo.obtenerProductos();
            vista.tablaProductos.setModel(construirTableModelProductos(productos));
            vista.comboBoxProductoKits.removeAllItems();
            for (Producto p : productos) {
                vista.comboBoxProductoKits.addItem(p);
            }


        } catch (SQLException e) {
            e.printStackTrace();
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

    private DefaultTableModel construirTableModelProductos(ArrayList<Producto> productos)
            throws SQLException {

        Vector<String> columnNames = new Vector<>();
        columnNames.add("id");
        columnNames.add("nombre");
        columnNames.add("descripcion");
        columnNames.add("estado");
        columnNames.add("modelo_Clases");
        columnNames.add("marca");
        Vector<Vector<Object>> data = new Vector<>();

        for (Producto e : productos){
            Vector<Object> vector = new Vector<>();
            vector.add(e.getIdProducto());
            vector.add(e.getNombre());
            vector.add(e.getDescripcion());
            vector.add(e.getEstado());
            vector.add(e.getModelo());
            vector.add(e.getMarca());

            data.add(vector);
        }


        vista.dtmProductos.setDataVector(data, columnNames);

        return vista.dtmProductos;

    }

    //parte de empresa

    private void eliminarEmpresas() {

        if (vista.tablaEmrpresa.getSelectedRow() == -1) {
            //no se ha seleccionado ninguna fila
            return;
        }



        vista.crearDialogoSeguridad();
        vista.btnValidate.addActionListener(e -> {
            if (String.valueOf(vista.contrasenaDeEliminacion.getPassword()).equals(modelo.deletePass)) {
                vista.contrasenaDeEliminacion.setText("");
                vista.dialogoDeEliminacion.dispose();


                int id = Integer.parseInt(String.valueOf(vista.tablaEmrpresa.getValueAt(vista.tablaEmrpresa.getSelectedRow(), 0)));
                modelo.eliminarEmpresa(id);
                borrarCamposEmpresa();
                resfrecarEmpresa();
            } else {
                Util.showErrorAlert("La contraseña es incorrecta, introduzcala de nuevo");
            }
        });
        vista.dialogoDeEliminacion.setVisible(true);

    }

    private void modificarEmpresas() {
        if (vista.tablaEmrpresa.getSelectedRow() == -1) {
            //no se ha seleccionado ninguna fila
            return;
        }

        Empresa p = new Empresa();
        p.setIdEmpresa((Integer) vista.tablaEmrpresa.getValueAt(vista.tablaEmrpresa.getSelectedRow(), 0));
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
        try {
            ArrayList<Empresa> empresas = modelo.obtenerEmpresas();
            vista.tablaEmrpresa.setModel(construirTableModelEmpresa(empresas));
            vista.comboBoxEmpresaKit.removeAllItems();

            for (Empresa e : empresas) {
                vista.comboBoxEmpresaKit.addItem(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

    private DefaultTableModel construirTableModelEmpresa(ArrayList<Empresa> empresas) throws SQLException {
        Vector<String> columnNames = new Vector<>();
        columnNames.add("id");
        columnNames.add("nombre");
        columnNames.add("descripcion");
        columnNames.add("fecha_de_creacion");
        columnNames.add("ubicacion");
        columnNames.add("valoracion");
        Vector<Vector<Object>> data = new Vector<>();

        for (Empresa e : empresas){
            Vector<Object> vector = new Vector<>();
            vector.add(e.getIdEmpresa());
            vector.add(e.getNombre());
            vector.add(e.getDescripcion());
            vector.add(e.getUbicacion());
            vector.add(e.getValoracion());

            data.add(vector);
        }


        vista.dtmEmpresa.setDataVector(data, columnNames);

        return vista.dtmEmpresa;
    }

    //parte de kits

    void resfrecarKits() {
        try {
            vista.tablaKits.setModel(construirTableModelKits(modelo.obtenerKitEducativo()));

        } catch (SQLException e) {
            e.printStackTrace();
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

    private DefaultTableModel construirTableModelKits(ArrayList<KitEducativo> kitEducativos) throws SQLException {
        Vector<String> columnNames = new Vector<>();
        columnNames.add("id");
        columnNames.add("nombre");
        columnNames.add("descripcion");
        columnNames.add("cantidad");
        columnNames.add("fecha_de_creacion");
        columnNames.add("fecha_de_actualizacion");
        columnNames.add("precio");
        columnNames.add("valoracion");
        columnNames.add("empresa");
        columnNames.add("producto");

        Vector<Vector<Object>> data = new Vector<>();

        for (KitEducativo e : kitEducativos){
            Vector<Object> vector = new Vector<>();
            vector.add(e.getIdKit());
            vector.add(e.getNombre());
            vector.add(e.getDescripcion());
            vector.add(e.getCantidad());
            vector.add(e.getFechaDeCreacion());
            vector.add(e.getFechaDeActualizacion());
            vector.add(e.getPrecio());
            vector.add(e.getValoracion());
            vector.add(e.getIdEmpresa().getNombre());
            vector.add(e.getIdProducto().getNombre());

            data.add(vector);
        }
        vista.dtmKits.setDataVector(data, columnNames);
        return vista.dtmKits;
    }



}
