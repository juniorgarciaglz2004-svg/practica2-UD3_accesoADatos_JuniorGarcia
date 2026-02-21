package practica.gui;

import javax.swing.*;
import java.awt.*;

public class OptionDialog extends JDialog{
    private JPanel panel1;
    JTextField txtIP;
    JTextField txtUsuario;
    JButton btnOpcionesGuardar;
    JPasswordField pfPass;
    JPasswordField pfAdmin;
    private Frame owner;

    public OptionDialog(Frame owner) {
        //modal true, desactiva el resto de ventanas
        super(owner,"Opciones",true);
        this.owner=owner;
        initDialog();
    }
    private void initDialog() {
        panel1 = new JPanel(new GridLayout(0, 2, 10, 10));

        txtIP = new JTextField(15);
        txtUsuario = new JTextField(15);
        pfPass = new JPasswordField(15);
        pfAdmin = new JPasswordField(15);
        btnOpcionesGuardar = new JButton("Guardar");

        panel1.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel1.add(new JLabel("IP:"));
        panel1.add(txtIP);
        panel1.add(new JLabel("Usuario:"));
        panel1.add(txtUsuario);
        panel1.add(new JLabel("Password:"));
        panel1.add(pfPass);
        panel1.add(new JLabel("Admin:"));
        panel1.add(pfAdmin);
        panel1.add(new JLabel(""));
        panel1.add(btnOpcionesGuardar);

        setContentPane(panel1);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(owner);
    }
}
