package Misc;

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

import org.gema.logIn.DBConnection;

/**
 * Servlet implementation class LogIN
 */
@WebServlet("/LogIN")
public class LogIN extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogIN() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
				PrintWriter out = response.getWriter();
				String tag = request.getParameter("mail");
				String tag2 = request.getParameter("psswd");
				DBConnection ping = new DBConnection();
				Connection con =  ping.makeConnection("jirachi", "sharPedo319");
				String resp = null;
				try {
					CallableStatement stmnt = con.prepareCall("{call logIn(?,?,?)}");
					stmnt.setString(1, tag);
					stmnt.setString(2, tag2);
					stmnt.registerOutParameter(3, Types.VARCHAR);
					stmnt.executeUpdate();
					resp = stmnt.getString(3);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					resp = " ñoj";
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
		String tag = request.getParameter("mail");
		String tag2 = request.getParameter("psswd");
		DBConnection ping = new DBConnection();
		Connection con =  ping.makeConnection("jirachi", "sharPedo319");
		String resp = null;
		try {
			CallableStatement stmnt = con.prepareCall("{call logIn(?,?,?)}");
			stmnt.setString(1, tag);
			stmnt.setString(2, tag2);
			stmnt.registerOutParameter(3, Types.VARCHAR);
			stmnt.executeUpdate();
			resp = stmnt.getString(3);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp = " ñoj";
		} finally {
			ping.closeConnection();
		}
	
		out.print(resp);
	}

}
