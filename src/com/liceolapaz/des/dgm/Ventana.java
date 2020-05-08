package com.liceolapaz.des.dgm;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import com.mysql.cj.jdbc.Driver;
import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.xdevapi.PreparableStatement;

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
		crearMenu();
		
		try {
			DriverManager.registerDriver(new Driver());
		}catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error al registrar el driver", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	private Connection crearConexion(String url) throws SQLException {
		return DriverManager.getConnection(url, this.usuario, this.password);
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
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void crearMenu() {
		
		JMenuBar menuBar = new JMenuBar();
		// Creamos la pestaña de Opciones
		JMenu menuOpciones = new JMenu("Opciones");
		
		JMenuItem cambiarUsuario = new JMenuItem("Cambiar Usuario");
		cambiarUsuario.setMnemonic(KeyEvent.VK_U);
		cambiarUsuario.setAccelerator(KeyStroke.getKeyStroke("ctrl U"));
		cambiarUsuario.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				setVisible(false);
				dialogo.setVisible(true);
				
			}
		});
		menuOpciones.add(cambiarUsuario);
		
		JMenuItem cargarDatos = new JMenuItem("Cargar Datos");
		cargarDatos.setMnemonic(KeyEvent.VK_D);
		cargarDatos.setAccelerator(KeyStroke.getKeyStroke("ctrl D"));
		cargarDatos.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cargarDatos();
				
			}
		});
		menuOpciones.add(cargarDatos);
		
		JMenuItem limpiarDatos = new JMenuItem("Limpiar Datos");
		limpiarDatos.setMnemonic(KeyEvent.VK_L);
		limpiarDatos.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
		limpiarDatos.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				limpiar();
				
			}
		});
		menuOpciones.add(limpiarDatos);
		
		menuBar.add(menuOpciones);
		// Creamos la pesaña de Informes
		JMenu menuInformes = new JMenu("Informes");
		
		JMenuItem facturas = new JMenuItem("Facturas");
		facturas.setMnemonic(KeyEvent.VK_T);
		facturas.setAccelerator(KeyStroke.getKeyStroke("ctrl T"));
		facturas.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		menuInformes.add(facturas);
		
		JMenuItem clientes = new JMenuItem("Clientes");
		clientes.setMnemonic(KeyEvent.VK_I);
		clientes.setAccelerator(KeyStroke.getKeyStroke("ctrl I"));
		clientes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		menuInformes.add(clientes);
		
		menuBar.add(menuInformes);
		
		setJMenuBar(menuBar);
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	protected void limpiar() {
	
		Component[] componentes = panelCampos.getComponents();
		for (int i = 0; i < componentes.length; i++) {
			try {
				JTextField textField = (JTextField) componentes[i];
				textField.setText("");
			} catch(ClassCastException ex) {}
			
		}
		
	
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	protected void cargarDatos() {
		
		Connection conexion = null;
		try {
			conexion = crearConexion(URL_BASE_DATOS);
			PreparedStatement ps = conexion.prepareStatement("SELECT Nombre, Ape1, Ape2, Fec_Nac FROM clientes WHERE DNI = ?");
			String dni = JOptionPane.showInputDialog(this, "Introduzca el DNI:", "Cargar datos", JOptionPane.INFORMATION_MESSAGE);
			if (dni == null || dni.equals("")) {
				conexion.close();
				return;
			}
			try {
				
				ps.setString(1, dni);
				ResultSet rs = ps.executeQuery();
				if (rs.first()) {
					txtDNI.setText("" + dni);
					txtNombre.setText(rs.getString(1));
					txtPrimerApellido.setText(rs.getString(2));
					if (rs.getObject(3) == null) {
						txtSegundoApellido.setText("");
					} else {
						txtSegundoApellido.setText("" + rs.getString(3));
					}
					txtNacimiento.setText("" + rs.getString(4));
				} else {
					JOptionPane.showMessageDialog(this, "No existe ningún cliente con DNI " + dni +".", "Error", JOptionPane.ERROR_MESSAGE);
					conexion.close();
					return;
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "El DNI del cliente tiene que ser un DNI válido", "Error", JOptionPane.ERROR_MESSAGE);
				conexion.close();
				return;
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error al cargar datos", JOptionPane.ERROR_MESSAGE);
			try {
				if (conexion != null) {
					conexion.close();
				}	
			} catch (SQLException e1) {}
		}
	}

}
