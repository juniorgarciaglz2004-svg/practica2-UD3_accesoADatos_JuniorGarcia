package practica.gui;

import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Vista extends JFrame{

     JPanel panel1;
     JTextField nombreProducto;
     JTextField descripcionProducto;
     JButton anadir_PRODUCTOSButton;
     JButton eliminar_ProductosButton;
     JButton modificar_ProductosButton;
     JButton anadir_KitsButton;
     JButton eliminar_KitsButton;
     JButton modificar_KitsButton;
     JButton anadir_EmpresaButton;
     JButton eliminar_EmpresaButton;
     JButton modificar_EmpresaButton;
     JTextField nombreKit;
     JTextField descripcionKit;
     JTextField nombreEmpresa;
     JTextField descripcionEmpresa;
     JTextField cantidadKit;
    DatePicker fecha_creacion_Empresa;
    DatePicker fecha_CreacionKits;
     DatePicker fecha_ActualizacionKits;
     JComboBox comboBoxEmpresaKit;
     JComboBox comboBoxProductoKits;
     JCheckBox productoEstadoUsado;
     JCheckBox productosEstadoNuevos;
     JSlider valoracionSliderEmpresa;
     JCheckBox productosEstadoReacondicionados;
     JTable tablaProductos;
     JTable tablaKits;
     JTable tablaEmrpresa;
     JTextField modeloProducto;
     JTextField marcaProducto;
     JSlider valoracionSliderKit;
     JTextField precioKit;
     JTextField ubicacionEmpresa;


    DefaultTableModel dtmProductos;
    DefaultTableModel dtmKits;
    DefaultTableModel dtmEmpresa;

    public JButton btnValidate;
    public JDialog dialogoDeEliminacion;
    public JPasswordField contrasenaDeEliminacion;

    public Vista() {
        super("TIENDA DE ROBOTICA Y KITS EDUCATIVOS PARA NIÑOS");
        initFrame();
    }

    public void initFrame() {
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        this.setSize(new Dimension(this.getWidth()+100,this.getHeight()));
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        setTableModels();
        crearDialogoSeguridad();


    }

    public void crearDialogoSeguridad() {
        btnValidate = new JButton("Validar");
        contrasenaDeEliminacion = new JPasswordField();
        contrasenaDeEliminacion.setPreferredSize(new Dimension(100, 26));
        Object[] options = new Object[]{contrasenaDeEliminacion, btnValidate};
        JOptionPane jop = new JOptionPane("Introduce la contraseña", JOptionPane.WARNING_MESSAGE,
                JOptionPane.YES_NO_OPTION, null, options);
        dialogoDeEliminacion = new JDialog(this, "Opciones", true);
        dialogoDeEliminacion.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogoDeEliminacion.setContentPane(jop);
        dialogoDeEliminacion.pack();
        dialogoDeEliminacion.setLocationRelativeTo(this);

    }


    private void setTableModels() {

        this.dtmProductos=new DefaultTableModel();
        this.tablaProductos.setModel(dtmProductos);

        this.dtmKits=new DefaultTableModel();
        this.tablaKits.setModel(dtmKits);

        this.dtmEmpresa=new DefaultTableModel();
        this.tablaEmrpresa.setModel(dtmEmpresa);
    }
}
