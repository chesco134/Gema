package org.capiz.greeting;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.inspira.database.DBConnection;

/**
 * Servlet implementation class Comprador
 */
@WebServlet("/Comprador")
public class AltaTipoUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AltaTipoUsuario() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		int tag = Integer.parseInt(request.getParameter("tipoUsuario"));
		String tag2 = request.getParameter("usuario");
		DBConnection ping = new DBConnection();
		Connection con =  ping.makeConnection("root", "sharPedo319");
		String resp = null;
		try {
			CallableStatement stmnt = con.prepareCall("{call insertaTipoUsuario(?,?)}");
			stmnt.setInt(1, tag);
			stmnt.setString(2, tag2);
			stmnt.executeUpdate();
			resp = "Corrrecto";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp = " Error";
		} finally {
			ping.closeConnection();
		}
	
		out.print(resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		int tag = Integer.parseInt(request.getParameter("tipoUsuario"));
		String tag2 = request.getParameter("usuario");
		DBConnection ping = new DBConnection();
		Connection con =  ping.makeConnection("root", "sharPedo319");
		String resp = null;
		try {
			CallableStatement stmnt = con.prepareCall("{call insertaTipoUsuario(?,?)}");
			stmnt.setInt(1, tag);
			stmnt.setString(2, tag2);
			stmnt.executeUpdate();
			resp = "Corrrecto";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp = " Error";
		} finally {
			ping.closeConnection();
		}
	
		out.print(resp);
	}

}
