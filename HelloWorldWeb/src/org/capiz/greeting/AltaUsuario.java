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

import org.inspira.database.DBConnection;

/**
 * Servlet implementation class AltaUsuario
 */
@WebServlet("/AltaUsuario")
public class AltaUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AltaUsuario() {
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
		PrintWriter out = response.getWriter();
		int tag = Integer.parseInt(request.getParameter("TipoUsuario"));
		String tag2 = request.getParameter("Mail");
		String tag3 = request.getParameter("Psswd");
		String tag4 = request.getParameter("Nombre");
		DBConnection ping = new DBConnection();
		Connection con = ping.makeConnection("root", "sharPedo319");
		String resp = null;
		try {
			CallableStatement stmnt = con.prepareCall("{call signUp(?,?,?,?,?)}");
			stmnt.setInt(4, tag);
			stmnt.setString(1, tag2);
			stmnt.setString(2, tag3);
			stmnt.setString(3, tag4);
			stmnt.registerOutParameter(5, Types.VARCHAR);
			stmnt.executeUpdate();
			resp = stmnt.getString(5);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp = e.getMessage();
		} finally {
			ping.closeConnection();
		}

		out.print(resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		int tag = Integer.parseInt(request.getParameter("TipoUsuario"));
		String tag2 = request.getParameter("Mail");
		String tag3 = request.getParameter("Psswd");
		String tag4 = request.getParameter("Nombre");
		DBConnection ping = new DBConnection();
		Connection con = ping.makeConnection("root", "sharPedo319");
		String resp = null;
		try {
			CallableStatement stmnt = con.prepareCall("{call signUp(?,?,?,?,?)}");
			stmnt.setInt(4, tag);
			stmnt.setString(1, tag2);
			stmnt.setString(2, tag3);
			stmnt.setString(3, tag4);
			stmnt.registerOutParameter(5, Types.VARCHAR);
			stmnt.executeUpdate();
			resp = stmnt.getString(5);
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
