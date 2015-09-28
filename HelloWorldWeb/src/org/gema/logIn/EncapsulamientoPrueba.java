package org.gema.logIn;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class EncapsulamientoPrueba
 */
@WebServlet("/EncapsulamientoPrueba")
public class EncapsulamientoPrueba extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EncapsulamientoPrueba() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// / TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		String obj = request.getParameter("obj");
		DBConnection ping = new DBConnection();
		Connection con = ping.makeConnection("root", "sharPedo319");
		String resp = null;
		String resp2 = null;
		String resp3 = null;
		String resp4 = null;
		String resp5 = null;
		JSONObject jresp = new JSONObject();
		try {
			JSONObject json = new JSONObject(obj);
			String identificador = json.getString("identificador");
			CallableStatement stmnt = con
					.prepareCall("{call Recopilacion(?,?,?,?,?)}");
			stmnt.setString(1, identificador);
			stmnt.registerOutParameter(2, Types.VARCHAR);
			stmnt.registerOutParameter(3, Types.VARCHAR);
			stmnt.registerOutParameter(4, Types.VARCHAR);
			stmnt.registerOutParameter(5, Types.VARCHAR);
			stmnt.executeUpdate();
			resp2 = stmnt.getString(2);
			resp3 = stmnt.getString(3);
			resp4 = stmnt.getString(4);
			resp5 = stmnt.getString(5);
			jresp.put("Nombre", resp2);
			jresp.put("Psswd", resp3);
			jresp.put("Correo", resp4);
			jresp.put("TipoUsuario", resp5);
			resp = jresp.toString();
		} catch (SQLException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp = e.getMessage();
		} finally {
			ping.closeConnection();
		}
		out.print(resp);
	}
}
