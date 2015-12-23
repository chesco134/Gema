package org.inspira.emag.season;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URLEncoder;
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
import org.capiz.greeting.PublicEncryptionUtility;
import org.inspira.database.DBConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class GetUserInfo
 */
@WebServlet("/GetUserInfoEMAG")
public class GetUserInfoEMAG extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String usrName = "jirachi";
	private static final String usrPsswd = "sharPedo319";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetUserInfoEMAG() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String result = null;
		try {
			ObjectInputStream input = new ObjectInputStream(
					new FileInputStream(PublicEncryptionUtility.PRIVATE_KEY_FILE));
			PrivateKey privateKey = (PrivateKey) input.readObject();
			input.close();
			DataInputStream entrada = new DataInputStream(request.getInputStream());
			int length;
			byte[] chunk = new byte[512];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((length = entrada.read(chunk)) != -1)
				baos.write(chunk, 0, length);
			byte[] superChunk = baos.toByteArray();
			baos.close();
			int numberOfChunks = (int) superChunk.length / 128;
			byte[] currentChunk;
			byte[] aesCipherText = null;
			ByteArrayOutputStream messageCollector = new ByteArrayOutputStream();
			SecretKeySpec aesKeySpec = null;
			int j;
			for (int i = 0; i < numberOfChunks; i++) {
				currentChunk = new byte[128];
				for (j = 0; j < 128 && (128 * i + j) < superChunk.length; j++)
					currentChunk[j] = superChunk[(128 * i + j)];
				switch (i) {
				case 0:
					byte[] aesKey = PublicEncryptionUtility.decryptForSomeBytes(currentChunk, privateKey);
					aesKeySpec = new SecretKeySpec(aesKey, "AES");
					break;
				case 1:
					aesCipherText = PublicEncryptionUtility.decryptForSomeBytes(currentChunk, privateKey);
					break;
				case 2:
					i = numberOfChunks;
					break;
				}
			}
			Cipher aesCipher = Cipher.getInstance("AES");
			aesCipher.init(Cipher.DECRYPT_MODE, aesKeySpec);
			messageCollector.write((aesCipher.doFinal(aesCipherText)));

			String usrKey = messageCollector.toString();

			DBConnection db = new DBConnection();
			Connection con = db.makeConnection(usrName, usrPsswd);
			if (con != null) {
				CallableStatement call = con.prepareCall("{call getUserInfo(?,?,?,?)}");
				call.setString(1, usrKey);
				call.registerOutParameter(2, Types.VARCHAR);
				call.registerOutParameter(3, Types.VARCHAR);
				call.registerOutParameter(4, Types.VARCHAR);
				call.executeUpdate();
				result = new JSONObject()
						.put("response",
								new JSONArray().put(call.getString(2)).put(call.getString(3)).put(call.getString(4)))
						.toString();
				db.closeConnection();
			} else
				result = new JSONObject().put("content", "Servicio no disponible temporalmente.").toString();
			aesCipher.init(Cipher.ENCRYPT_MODE, aesKeySpec);
			messageCollector.close();
			messageCollector = new ByteArrayOutputStream();
			messageCollector.write(aesCipher.doFinal(URLEncoder.encode(result, "utf8").getBytes()));
			DataOutputStream salida = new DataOutputStream(response.getOutputStream());
			salida.write(messageCollector.toByteArray());
			salida.flush();
			messageCollector.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
