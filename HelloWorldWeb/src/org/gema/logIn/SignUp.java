package org.gema.logIn;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.gema.servicios.PublicEncryptionUtility;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class SignUp
 */
@WebServlet("/SignUp")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String usrName = "root";
    private static final String usrPsswd = "sharPedo319";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		String result = null;
		try{
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(
					PublicEncryptionUtility.PRIVATE_KEY_FILE));
			PrivateKey privateKey = (PrivateKey)input.readObject();
			input.close();
			DataInputStream entrada = new DataInputStream(request.getInputStream());
			int length;
			byte[] chunk = new byte[512];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while((length=entrada.read(chunk))!=-1)
				baos.write(chunk, 0, length);
			byte[] superChunk = baos.toByteArray();
			baos.close();
			byte[] currentChunk;
			ByteArrayOutputStream messageCollector = new ByteArrayOutputStream();
			int i;
			currentChunk = new byte[128];
			for(i=0; i < 128 ;i++)
				currentChunk[i] = superChunk[i];
			byte[] aesKey = PublicEncryptionUtility.decryptForSomeBytes(currentChunk, privateKey);
			SecretKeySpec aesKeySpec = new SecretKeySpec(aesKey, "AES");
			byte[] aesCipherText = new byte[superChunk.length-128];
			for(int j=i; j<superChunk.length; j++)
				aesCipherText[j-i] = superChunk[j];
			System.out.println("Shuckle: " + superChunk.length);
			Cipher aesCipher = Cipher.getInstance("AES");
			aesCipher.init(Cipher.DECRYPT_MODE, aesKeySpec);
			messageCollector.write(aesCipher.doFinal(aesCipherText));
			
			JSONObject json = new JSONObject(messageCollector.toString());
			messageCollector.close();
			String mail = json.getString("mail");
			String name = json.getString("name");
			String psswd = json.getString("psswd");
			String dateOfBirth = json.getString("dateOfBirth");
			DBConnection db = new DBConnection();
			Connection con = db.makeConnection(usrName, usrPsswd);
			if(con != null){
				CallableStatement call = con.prepareCall("{call registerUser(?,?,?,?,?)}");
				call.setNString(1, mail);
				call.setNString(2, name);
				call.setNString(3, psswd);
				call.setNString(4, dateOfBirth);
				call.registerOutParameter(5, Types.VARCHAR);
				call.executeUpdate();
				result = call.getString(5);
				db.closeConnection();
			}
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
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.write(result);
	}

}
