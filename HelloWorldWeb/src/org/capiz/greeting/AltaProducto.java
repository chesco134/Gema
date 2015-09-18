package org.capiz.greeting;

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

import org.capiz.encuestas.DBConnection;

/**
 * Servlet implementation class AltaProducto
 */
@WebServlet(description = "Servicio para guardar un nuevo producto.", urlPatterns = { "/AltaProducto" })
public class AltaProducto extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AltaProducto() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		int prod1 = Integer.parseInt(request.getParameter("idProducto"));
		String prod2 = request.getParameter("nombre");
		DBConnection ping = new DBConnection();
		Connection con = ping.makeConnection("root", "sharPedo319");
		String resp = null;
		
		try {
			CallableStatement stmnt = con.prepareCall("{call nuevoProducto(?,?)}");
			stmnt.setInt(1, prod1);
			stmnt.setString(2, prod2);
			stmnt.executeUpdate();
			resp = "¡CORRECTO! :P";
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp = e.getMessage();
		}
		finally {
			ping.closeConnection();
		}
		System.out.println("Un cliente dijo: " + resp);

		out.print(resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
			PrintWriter out = response.getWriter();
			int prod = Integer.parseInt(request.getParameter("idProducto"));
			String prod2 = request.getParameter("nombre");
			DBConnection ping = new DBConnection();
			Connection con = ping.makeConnection("root", "sharPedo319");
			String resp = null;
			
			try {
				CallableStatement stmnt = con.prepareCall("{call nuevoProducto(?,?)}");
				stmnt.setInt(1, prod);
				stmnt.setString(2, prod2);
				stmnt.executeUpdate();
				resp = "¡CORRECTO! :P";
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resp = e.getMessage();
			}
			finally {
				ping.closeConnection();
			}
			System.out.println("Un cliente dijo: " + resp);

			out.print(resp);
		}
	

}
