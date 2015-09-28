package Vendedor;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gema.logIn.DBConnection;

/**
 * Servlet implementation class Horarios
 */
@WebServlet("/Horarios")
public class Horarios extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Horarios() {
        super();
        // TODO Auto-generated constructor stub
    }
 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		String mail = request.getParameter("Correo");
		DBConnection ping = new DBConnection();
		Connection con =  ping.makeConnection("root", "sharPedo319");
		String resp = null;
		try {
			Date date = new Date ();
			SimpleDateFormat dateF = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat datetime = new SimpleDateFormat("hh:mm:ss");
			CallableStatement stmnt = con.prepareCall("{call Tlaboral(?,?,?,?)}");
			stmnt.setString(4, mail);
			stmnt.setString(1, "99:99:99");
			stmnt.setString(2, "99:99:99");
			stmnt.setString(3, "00/00/0000");
			//stmnt.executeUpdate();
			resp = "Hello";
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
		String mail = request.getParameter("Correo");
		DBConnection ping = new DBConnection();
		Connection con =  ping.makeConnection("root", "sharPedo319");
		String resp = null;
		try {
			Date date = new Date ();
			SimpleDateFormat dateF = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat datetime = new SimpleDateFormat("hh:mm:ss");
			CallableStatement stmnt = con.prepareCall("{call Tlaboral(?,?,?,?)}");
			stmnt.setString(4, mail);
			stmnt.setString(1, datetime.format(date));
			stmnt.setString(2, datetime.format(date));
			stmnt.setString(3, dateF.format(date));
			//stmnt.executeUpdate();
			resp = "Hello";
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
