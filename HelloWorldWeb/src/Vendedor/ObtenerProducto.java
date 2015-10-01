package Vendedor;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gema.logIn.DBConnection;
import org.json.JSONException;
import org.json.JSONObject;

public class ObtenerProducto {
	private static final long serialVersionUID = 1L;
	JSONObject json;
    /**
     * @see HttpServlet#HttpServlet()
     */
  
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// / TODO Auto-generated method stub
				PrintWriter out = response.getWriter();
				String mail = request.getParameter("Correo");
				DBConnection ping = new DBConnection();
				Connection con = ping.makeConnection("root", "sharPedo319");
				String resp = null;
				try {
					CallableStatement stmnt = con.prepareCall("{call obtenerPProducto(?)}");
					stmnt.setString(1, mail);
					//resp = stmnt.getString(1);
					stmnt.executeUpdate();
					ResultSet rs = stmnt.getResultSet();
					while(rs.next()){
						json.put("activo", rs.getString(1));
						json.put("imagen", rs.getString(2));
						json.put("nombre", rs.getString(3));
						json.put("ubicacionInt", rs.getString(4));
						//resp = resp.concat("\nActivo:" + rs.getString(1) + ", imagen:" + rs.getString(2) + ", nombre:" + rs.getString(3) + ", ubicacionInt" + rs.getString(4));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					resp = e.getMessage();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					ping.closeConnection();
				}
				out.print(resp);
		//doGet(request, response);
	}

}
