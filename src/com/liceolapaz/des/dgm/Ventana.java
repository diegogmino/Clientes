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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.mysql.cj.xdevapi.Statement;

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
		// Constructor de la ventana
		super();
		this.dialogo = dialogo;
		this.usuario = usuario;
		this.password = password;
		setSize(700, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Clientes");
		setLayout(new BorderLayout());
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
	
	// Creamos la conexión
	private Connection crearConexion(String url) throws SQLException {
		return DriverManager.getConnection(url, this.usuario, this.password);
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	private void crearBotones() {
		// Método que crea los botones en la parte inferior de la ventana
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new GridLayout(1, 3, 30, 0));
		panelBotones.setBorder(new EmptyBorder(20,20,20,20));
		JButton bCrear = new JButton("Crear cliente");
		bCrear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				crearCliente();
			}
		});
		panelBotones.add(bCrear);
		JButton bActualizar = new JButton("Actualizar cliente");
		bActualizar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actualizar();
			}
		});
		panelBotones.add(bActualizar);
		JButton bEliminar = new JButton("Eliminar cliente");
		bEliminar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				eliminar();
				
			}
		});
		panelBotones.add(bEliminar);
		add(panelBotones, BorderLayout.SOUTH);
		
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	protected void actualizar() {
		// Método que acualiza la información de un cliente
		Connection conexion = null;
		
		try {
			conexion = crearConexion(URL_BASE_DATOS);
			PreparedStatement ps = conexion.prepareStatement("UPDATE clientes SET Nombre = ?, Ape1 = ?, Ape2 = ?, Fec_Nac = ? WHERE DNI = ?");
			if (!rellenarActualizar(conexion, ps)) {
				return;
			}
			int filas = ps.executeUpdate();
			JOptionPane.showMessageDialog(this, "Se han actualizado correctamente " + filas + " fila(s)", "Actualizar cliente", JOptionPane.INFORMATION_MESSAGE);
			conexion.close();
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error al actualizar un cliente", JOptionPane.ERROR_MESSAGE);
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e1) {}
		}
		
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	private boolean rellenarActualizar(Connection conexion, PreparedStatement ps) {
		
		//Rellenar el campo nombre
		String nombre = txtNombre.getText();
		if (nombre.equals("")) {
			JOptionPane.showMessageDialog(this, "Introduzca un valor para el nombre", "Campo obligatorio", JOptionPane.ERROR_MESSAGE);
			try {
				conexion.close();
			} catch (SQLException e) {}
					
			return false;
		}
		try {
			ps.setString(1, nombre);
		} catch (SQLException e) {}
		// Rellenar el campo apellido 1
		String ape1 = txtPrimerApellido.getText();
		if (ape1.equals("")) {
			JOptionPane.showMessageDialog(this, "Introduzca un valor para el primer apellido", "Campo Obligatorio", JOptionPane.ERROR_MESSAGE);
			try {
				conexion.close();
			} catch (SQLException e) {}
					
			return false;
		}
		try {
			ps.setString(2, ape1);
		} catch (SQLException e) {}
		// Rellenar el campo apellido 2
		String ape2 = txtSegundoApellido.getText();
		if (ape2.equals("")) {
			try {
				ps.setObject(3, null);
			} catch (SQLException e) {}
		} else {
			try {
				ps.setString(3, ape2);
			} catch (SQLException e) {
				return false;
			}
		}
		// Rellenar fecha de nacimiento
		String fechaTxt = txtNacimiento.getText();
		if (fechaTxt.equals("")) {
			JOptionPane.showMessageDialog(this, "Introduzca una fecha de nacimiento", "Campo obligatorio", JOptionPane.ERROR_MESSAGE);
			try {
				conexion.close();
			} catch (SQLException e) {}
				
			return false;
		}
		// Creo el formato de fecha requerido
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
		// Creo una variable fecha 
		Date fechaDate = null;
		// Paso la fecha en formato String a formato Date de java
		try {
			fechaDate = formatoFecha.parse(fechaTxt);
		} catch (ParseException e) {}
		// Paso el formato de Date de java al formato de Date de sql
		java.sql.Date sql = new java.sql.Date(fechaDate.getTime());
				
		try {
			ps.setDate(4, sql);
		} catch (SQLException e) {}
		// Rellenar el campo DNI
		String dni = txtDNI.getText();
		if (dni.equals("")) {
			JOptionPane.showMessageDialog(this, "Introduzca un valor para el DNI", "Campo obligatorio", JOptionPane.ERROR_MESSAGE);
			try {
				conexion.close();
			} catch (SQLException e) {}
					
			return false;
		}
		try {
			ps.setString(5, dni);
		} catch (SQLException e) {}
		
		
	
	return true;
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	protected void eliminar() {
		// Método que elimina un cliente un DNI concreto
		Connection conexion = null;
		
		try {
			conexion = crearConexion(URL_BASE_DATOS);
			PreparedStatement ps = conexion.prepareStatement("DELETE FROM clientes WHERE dni = ?");
			
			ps.setString(1, txtDNI.getText());
			int filas = ps.executeUpdate();
			
			if (filas == 0) {
				JOptionPane.showMessageDialog(this, "No existe ningún cliente con DNI " + txtDNI.getText(), "Error al eliminar", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Se han eliminado correctamente " + filas + " fila(s)", "Eliminar cliente", JOptionPane.INFORMATION_MESSAGE);
			}	
			conexion.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error al eliminar un cliente", JOptionPane.ERROR_MESSAGE);
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e1) {}
		}
		
		limpiar();
		
		
		
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	protected void crearCliente() {
		// Método que crea un cliente con los datos introdicidos en los campos correspondientes
		
		Connection conexion = null;
		
		try {
			conexion = crearConexion(URL_BASE_DATOS);
			PreparedStatement ps = conexion.prepareStatement("INSERT INTO clientes (DNI, Nombre, Ape1, Ape2, Fec_Nac) VALUES (?, ?, ?, ?, ?)");
			if (!rellenarPS(conexion, ps)) {
				return;
			}
			int filas = ps.executeUpdate();
			JOptionPane.showMessageDialog(this, "Se han insertado correctamente " + filas + " fila(s)", "Crear cliente", JOptionPane.INFORMATION_MESSAGE);
			conexion.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error al insertar un cliente", JOptionPane.ERROR_MESSAGE);
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e1) {}
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	private boolean rellenarPS(Connection conexion, PreparedStatement ps) {
		
		// Método que rellena un ps dado con los datos introducidos en los campos correspondientes
		// Rellenar el campo DNI
		String dni = txtDNI.getText();
		if (dni.equals("")) {
			JOptionPane.showMessageDialog(this, "Introduzca un valor para el DNI", "Campo obligatorio", JOptionPane.ERROR_MESSAGE);
			try {
				conexion.close();
			} catch (SQLException e) {}
			
			return false;
		}
		try {
			ps.setString(1, dni);
		} catch (SQLException e) {}
		//Rellenar el campo nombre
		String nombre = txtNombre.getText();
		if (nombre.equals("")) {
			JOptionPane.showMessageDialog(this, "Introduzca un valor para el nombre", "Campo obligatorio", JOptionPane.ERROR_MESSAGE);
			try {
				conexion.close();
			} catch (SQLException e) {}
			
			return false;
		}
		try {
			ps.setString(2, nombre);
		} catch (SQLException e) {}
		// Rellenar el campo apellido 1
		String ape1 = txtPrimerApellido.getText();
		if (ape1.equals("")) {
			JOptionPane.showMessageDialog(this, "Introduzca un valor para el primer apellido", "Campo Obligatorio", JOptionPane.ERROR_MESSAGE);
			try {
				conexion.close();
			} catch (SQLException e) {}
			
			return false;
		}
		try {
			ps.setString(3, ape1);
		} catch (SQLException e) {}
		// Rellenar el campo apellido 2
		String ape2 = txtSegundoApellido.getText();
		if (ape2.equals("")) {
			try {
				ps.setObject(4, null);
			} catch (SQLException e) {}
		} else {
			try {
				ps.setString(4, ape2);
			} catch (SQLException e) {
				return false;
			}
		}
		// Rellenar fecha de nacimiento
		String fechaTxt = txtNacimiento.getText();
		if (fechaTxt.equals("")) {
			JOptionPane.showMessageDialog(this, "Introdizca una fecha de nacimiento", "Campo obligatorio", JOptionPane.ERROR_MESSAGE);
			try {
				conexion.close();
			} catch (SQLException e) {}
			
			return false;
		}
		// Creo el formato de fecha requerido
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
		// Creo una variable fecha 
		Date fechaDate = null;
		// Paso la fecha en formato String a formato Date de java
		try {
			fechaDate = formatoFecha.parse(fechaTxt);
		} catch (ParseException e) {}
		// Paso el formato de Date de java al formato de Date de sql
		java.sql.Date sql = new java.sql.Date(fechaDate.getTime());
		
		try {
			ps.setDate(5, sql);
		} catch (SQLException e) {}
		
		
		return true;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	private void crearCampos() {
		// Método que crea los campos necesarios y sus JLabel
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
		// Método que crea el menu superior con todas sus opciones
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
				mostrarFacturasFiltradas();
				
			}
		});
		menuInformes.add(facturas);
		
		JMenuItem clientes = new JMenuItem("Clientes");
		clientes.setMnemonic(KeyEvent.VK_I);
		clientes.setAccelerator(KeyStroke.getKeyStroke("ctrl I"));
		clientes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarClientes();
				
			}
		});
		menuInformes.add(clientes);
		
		menuBar.add(menuInformes);
		
		setJMenuBar(menuBar);
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	protected void mostrarClientes() {
		// Método que muestra una tabla con todos los clientes
		Connection conexion = null;
		
		try {
			conexion = crearConexion(URL_BASE_DATOS);
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM clientes");
			ResultSet rs = ps.executeQuery();
			Informe informe = new Informe(rs);
			informe.setVisible(true);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error al mostrar los datos", JOptionPane.ERROR_MESSAGE);
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e1) {} 
		}
	
	
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	protected void mostrarFacturasFiltradas() {
		// Método que muestra una tabla con las facturas filtradas por cliente
		String dni = txtDNI.getText();
		if (dni.equals("")) {
			
			mostrarFacturas();
			
		} else {
		
			Connection conexion = null;
	
			try {
				conexion = crearConexion(URL_BASE_DATOS);
				PreparedStatement ps = conexion.prepareStatement("SELECT * FROM facturas WHERE facturas.Cliente = ?");
				ps.setString(1, dni);
				ResultSet rs = ps.executeQuery();
				Informe informe = new Informe(rs);
				informe.setVisible(true);
			
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error al mostrar los datos", JOptionPane.ERROR_MESSAGE);
				try {
					if (conexion != null) {
						conexion.close();
					}
				} catch (SQLException e1) {} 
			}
		
		}
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	private void mostrarFacturas() {
		// Método quue muestra todas las facturas insertadas en la base de datos
		Connection conexion = null;
		
		try {
			conexion = crearConexion(URL_BASE_DATOS);
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM facturas");
			ResultSet rs = ps.executeQuery();
			Informe informe = new Informe(rs);
			informe.setVisible(true);
		
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error al mostrar los datos", JOptionPane.ERROR_MESSAGE);
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e1) {} 
		}
	
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	protected void limpiar() {
		// Método que limpia los campos de texto de la ventana y los deja en blanco
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
		// Método que carga toda la información insertada de un cliente con un DNI concreto
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
