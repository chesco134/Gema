package Cliente;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gema.logIn.DBConnection;

/**
 * Servlet implementation class PerfilVendedor
 */
@WebServlet("/PerfilVendedor")
public class PerfilVendedor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PerfilVendedor() {
        super();
        // TODO Auto-generated constructor stub
    }

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
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		String v1 = request.getParameter("vendedor");
		DBConnection ping = new DBConnection();
		Connection con = ping.makeConnection("root", "sharPedo319");
		String resp = null;
		try {
			CallableStatement stmnt = con.prepareCall("{call (?)}");
			stmnt.setString(1, v1);
			//resp = stmnt.getString(1);
			stmnt.executeUpdate();
			ResultSet rs = stmnt.getResultSet();
			while(rs.next()){
				//Orden de Brenda//
				//resp = resp.concat("\nActivo:" + rs.getString(1) + ", imagen:" + rs.getString(2) + ", nombre:" + rs.getString(3) + ", ubicacionInt" + rs.getString(4));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp = e.getMessage();
		} finally {
			ping.closeConnection();
		}
		out.print(resp);
	}

}
