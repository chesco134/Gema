package org.inspira.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	/*****************************************************************************************
	 *
	 * Dependiendo del tipo de carga que se espera en la concurrencia de las consultas a la bd,
	 * es si debes usar o no un "connection pool", usando el JDBC de java. Si no piensas que
	 * vayas a tener mucha carga de conexión a la base de datos, sólo continua usando esta clase.
	 *
	 **********************************************************************************************/
	private Connection con;
	
	// Es recomendable que delegues la administración de las conexiones a base de datos a una clase.
	// El siguiente método recibe las credenciales para conectarte a la bd. Podrías implementar seguridad.
	public Connection makeConnection(String usr, String psswd) {
		con = null;
		try {
		/* Sobre todo si vas a usar esta clase en web, debes hacer una instancia dinámica con {@Class.forName} */
			Class.forName("com.mysql.jdbc.Driver");
			// La siguiente línea es la cadena de conexión a la base de datos.
			String connStatement = ("jdbc:mysql://localhost/Gema");
			// En la siguiente línea proporcionas los parámetros de conexión.
			con = DriverManager.getConnection(connStatement,usr,psswd);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("\n\n\n\t " + e.toString() + "\n\n\n");
		}
		return con;
	}
	
	public Connection makeConnectionEmag() {
		con = null;
		try {
		/* Sobre todo si vas a usar esta clase en web, debes hacer una instancia dinámica con {@Class.forName} */
			Class.forName("com.mysql.jdbc.Driver");
			// La siguiente línea es la cadena de conexión a la base de datos.
			String connStatement = ("jdbc:mysql://localhost/EMAG");
			// En la siguiente línea proporcionas los parámetros de conexión.
			con = DriverManager.getConnection(connStatement,"chesco","tUpedoNoEsM10.zukam@tul");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("\n\n\n\t " + e.toString() + "\n\n\n");
		}
		return con;
	}

	// No olvides llamar a éste método justo después de que hayas terminado de usar la conexión, normalmente es en un "finally"
	public void closeConnection() {
		try {
			con.close();			
		} catch (SQLException e) {
			System.out.println("\n\n\n\t " + e.toString() + "\n\n\n");
		}
	}
}
