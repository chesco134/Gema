package org.capiz.encuestas;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.capiz.greeting.PublicEncryptionUtility;
import org.inspira.database.DBConnection;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class SignIn
 */
 
 
 /****************************************************************************************************************
  *
  *	La siguiente clase es un "Servlet". Un servlet es una clase de java que se encarga de manejar las
  * diferentes peticiones del protocolo HTTP; POST, GET, UPDATE...
  *
  *	Cada una de estas clases en un proyecto JEE, se encargan de dar algún servicio y pueden ser consumidos
  * a través de conexiones a determinadas URL, que comúnmente llevan el nombre de la clase.
  *
  *	El punto de la presente clase no es explicar cómo hacer aplicaciones JEE, sino ejemplificar el uso del
  * acceso a la base de datos y la implementación de la clase DBConnection.
  *
  ***************************************************************************************************************/
@WebServlet("/SignIn")
public class SignIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String usrName = "root"; // Parámetro usado para ejemplo de la conexión a BD.
	private static final String usrPsswd = "sharPedo319"; // Parámetro usado para ejemplo de la conexión con BD.
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignIn() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/* A continuación se inicia la lógica de atención del presente Servlet en respuesta a una petición POST */
		PrintWriter out = response.getWriter();
		String result = null; // Objeto que contendrá el resultado del servicio; "éxito" o "error", y lo devolverá al cliente.
		DBConnection db = null; // Inicializamos un objeto de conexión con la base de datos.
		try{
			// Inicia obtención de los datos que mandó el cliente.
			DataInputStream entrada = new DataInputStream(request.getInputStream());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] chunk = new byte[512];
			int length;
			while((length=entrada.read(chunk))!=-1)
				baos.write(chunk,0,length);
			entrada.close();
			// Finaliza obtención de datos que mandó el cliente. (Puros bytes)
			
			// Inicia código de descifrado de los datos que mandó el cliente.
			ObjectInputStream keyReader = new ObjectInputStream(new FileInputStream(new File(PublicEncryptionUtility.PRIVATE_KEY_FILE))); // Preparamos el flujo de entrada para leer la llave privada.
			PrivateKey privateKey = (PrivateKey)keyReader.readObject(); // Obtenemos la llave privada de un archivo.
			keyReader.close();
			// El formato esperado es un json, por lo que intentamos armar uno con los datos descifrados.
			JSONObject json = new JSONObject(PublicEncryptionUtility.decrypt(baos.toByteArray(), privateKey));
			baos.close();
			// Obtenemos los parámetros encapsulados por el json. (Se han acordado "userKey" y "userPsswd")
			String name = json.getString("userKey");
			String psswd = json.getString("userPsswd");
			
			// Inicia tramo de interacción con la base de datos.
			db = new DBConnection(); // Obtenemos objeto manejador de conexión con base de datos.
			Connection con = db.makeConnection(usrName, usrPsswd); // Obtenemos una conexión usando las credenciales.
			// Si se devuelve un objeto no nulo, quiere decir que logramos una conexión satisfactoria con la bd.
			if(con != null){
				// La siguiente línea prepara una consulta. (Esto es lo que evita el "SQL Injection")
				// En éste caso vamos a ejecutar un "stored procedure" (SP) llamado "validateUser".
				CallableStatement call = con.prepareCall("{call validateUser(?,?,?)}");
				// Se deben colocar "?" en donde vayamos a poner variables de java.
				call.setNString(1, name); // colocamos la variable java en la posición del "?" indicada por el primer num.
				call.setNString(2, psswd);// Lo mismo pero para el segundo signo "?".
				call.registerOutParameter(3, Types.BOOLEAN);// El SP manda que el tercer parámetro es de salida y de tipo boolean.
				call.executeUpdate(); // Ya que colocamos todas las variables en su lugar, ejecutamos la consulta.
				if(call.getBoolean(3)) // Verificamos el valor que puso la bd en el tercer parámetro (de salida)
					result = ("Existe");
				else
					result = URLEncoder.encode("Usuario o contraseña incorrecto.","utf8");
			}
		// Cualquier cosa que no se haya podido hacer, terminará automáticamente el algoritmo.
		} catch(IOException e){
			e.printStackTrace();
			result = e.getMessage();
		} catch(ClassNotFoundException e){
			e.printStackTrace();
			result = e.getMessage();
		} catch(JSONException e){
			e.printStackTrace();
			result = e.getMessage();
		} catch (SQLException e) {
			e.printStackTrace();
			result = e.getMessage();
		} finally {
			db.closeConnection();// NO NOS OLVIDAMOS DE CERRAR LA CONEXIÓN
		}
		out.write(result);
	}

}
