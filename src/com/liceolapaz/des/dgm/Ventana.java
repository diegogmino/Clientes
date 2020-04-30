package com.liceolapaz.des.dgm;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Ventana extends JFrame {
	
	private Dialogo dialogo;
	private String usuario;
	private String password;
	private JPanel panelCampos;
	private JTextField txtDNI;
	private JTextField txtNombre;
	private JTextField txtPrimerApellido;
	private JTextField txtSegundoApellido;
	private JTextField txtNacimiento;
	private static final String URL_BASE_DATOS = "jdbc:mysql://localhost/Acme?serverTimezone=Europe/Madrid";
	
	
	public Ventana(Dialogo dialogo, String usuario, String password) {
		super();
		this.dialogo = dialogo;
		this.usuario = usuario;
		this.password = password;
		setSize(700, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Clientes");
		setLayout(new BorderLayout());
		//setLayout(new GridLayout(2, 1, 50, 0));
		crearCampos();
		crearBotones();
		//crearMenu();
		
		
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	private void crearBotones() {
		
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new GridLayout(1, 3, 30, 0));
		panelBotones.setBorder(new EmptyBorder(20,20,20,20));
		JButton bCrear = new JButton("Crear cliente");
		bCrear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//crear();
			}
		});
		panelBotones.add(bCrear);
		JButton bActualizar = new JButton("Actualizar cliente");
		bActualizar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//actualizar();
			}
		});
		panelBotones.add(bActualizar);
		JButton bEliminar = new JButton("Eliminar cliente");
		bEliminar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//eliminar();
				
			}
		});
		panelBotones.add(bEliminar);
		add(panelBotones, BorderLayout.SOUTH);
		
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	private void crearCampos() {
		
		panelCampos = new JPanel();
		panelCampos.setLayout(new GridLayout(5, 2, 20, 10));
		panelCampos.setBorder(new EmptyBorder(20,20,20,20));
		JLabel lbDNI = new JLabel("DNI");
		panelCampos.add(lbDNI);
		txtDNI = new JTextField();
		panelCampos.add(txtDNI);
		JLabel lbNombre = new JLabel("Nombre");
		panelCampos.add(lbNombre);
		txtNombre = new JTextField();
		panelCampos.add(txtNombre);
		JLabel lbPrimerApellido = new JLabel("Primer Apellido");
		panelCampos.add(lbPrimerApellido);
		txtPrimerApellido = new JTextField();
		panelCampos.add(txtPrimerApellido);
		JLabel lbSegundoApellido = new JLabel("Segundo Apellido");
		panelCampos.add(lbSegundoApellido);
		txtSegundoApellido = new JTextField();
		panelCampos.add(txtSegundoApellido);
		JLabel lbNacimiento = new JLabel("Fecha de Nacimiento");
		panelCampos.add(lbNacimiento);
		txtNacimiento = new JTextField();
		panelCampos.add(txtNacimiento);
		add(panelCampos, BorderLayout.CENTER);
		
	}

}
