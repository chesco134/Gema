package org.inspira.emag.services;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.inspira.database.DBConnection;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class VehicleDataReceiver
 */
@WebServlet("/VehicleDataReceiver")
public class VehicleDataReceiver extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VehicleDataReceiver() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DataInputStream entrada = new DataInputStream(request.getInputStream());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int length;
		byte[] chunk = new byte[512];
		while((length = entrada.read(chunk)) != -1)
			baos.write(chunk,0,length);
		String recv = baos.toString();
		DataOutputStream salida = new DataOutputStream(response.getOutputStream());
		try{
			JSONObject mJson = new JSONObject();
			JSONObject json = new JSONObject(baos.toString());
			baos.close();
			mJson.put("host", request.getRemoteHost());
			mJson.put("timestamp", new Date());
			try{
				mJson.put("requester_value", json);
			}catch(JSONException e){
				e.printStackTrace();
			}
			PrintWriter logPrinter = new PrintWriter(new FileWriter(new File("HistoryAccess.txt"),true));
			logPrinter.println(mJson.toString());
			logPrinter.close();
			System.out.println("A visitor sent: " + recv);
			DBConnection db = new DBConnection();
			Connection con = db.makeConnectionEmag();
			PreparedStatement stmnt;
			int id = json.getInt("idViaje");
			switch(json.getInt("action")){
			case 1: // Register new Trip
				stmnt = con.prepareStatement("select * from Viaje order by idViaje DESC");
				ResultSet rs = stmnt.executeQuery();
				int lastTrip = 1;
				if(rs.next())
					lastTrip = rs.getInt("idViaje");
				if(json.getInt("idViaje") > lastTrip)
					lastTrip = json.getInt("idViaje");
				stmnt = con.prepareStatement("insert into Viaje(idViaje,fechaInicio,fechaFin,Usuario_Mail) values(?,?,?,'hsu.emca@demonstrate.edu')");
				stmnt.setInt(1, lastTrip);
				stmnt.setDate(2, new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(json.getString("fechaInicio")).getTime()));
				try{
					stmnt.setDate(3, new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(json.getString("fechaFin")).getTime()));
				}catch(JSONException e){
					stmnt.setDate(3, null);
				}
				stmnt.executeUpdate();
				salida.write("OK".getBytes());
				break;
			case 2: // Update data for Location
				float rpm = Float.parseFloat(json.getString("RPM").split("RPM")[0]);
				stmnt = con.prepareStatement("insert into RPM(RPM,Viaje_idViaje) values(?,?)");
				stmnt.setFloat(1, rpm);
				stmnt.setInt(2, id);
				stmnt.executeUpdate();
				salida.write("OK".getBytes());
				break;
			case 3: // Update data for Velocidad
				float speed = Float.parseFloat(json.getString("SPEED").split("km/h")[0]);
				stmnt = con.prepareStatement("insert into Velocidad(Velocidad,Viaje_idViaje) values(?,?)");
				stmnt.setFloat(1, speed);
				stmnt.setInt(2, id);
				stmnt.executeUpdate();
				salida.write("OK".getBytes());
				break;
			case 4:
				String latitud = json.getString("Latitud");
				String longitud = json.getString("Longitud");
				CallableStatement cstmnt = con.prepareCall("{call insertaUbicacion(?,?,?)}");
				cstmnt.setDouble(1, Double.parseDouble(latitud));
				cstmnt.setDouble(2, Double.parseDouble(longitud));
				cstmnt.setInt(3, id);
				cstmnt.executeQuery();
				salida.write("OK".getBytes());
				break;
			case 5: // Finish Trip
				int tripId = json.getInt("idViaje");
				stmnt = con.prepareStatement("UPDATE Viaje SET fechaFin = ? where idViaje = ?");
				stmnt.setDate(1, new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(json.getString("fechaFin")).getTime()));
				stmnt.setInt(2, tripId);
				stmnt.executeUpdate();
				salida.write("OK".getBytes());
				break;
			}
			con.close();
		}catch(JSONException e){
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}