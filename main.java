import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Base64;

import org.apache.commons.*;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.omg.IOP.Encoding;

public class main {

	public static BufferedReader br;
	public static StringBuilder sb;
	public static void main(String[] args) {
		try {
			final String base64Key = "ABEiM0RVZneImaq7zN3u/w==";
			final String base64KeyHMAC = "PSgiM6BVZneImoq7zY3T/w==";
		    final String base64Iv = "AAECAwQFBgcICQoLDA0ODw==";
		    final byte[] keyBytes = DatatypeConverter.parseBase64Binary(base64Key);
		    final byte[] ivBytes = DatatypeConverter.parseBase64Binary(base64Iv);
		    
			readFile();
			
		    String base64Message = DatatypeConverter.printBase64Binary(sb.toString().getBytes());
		    final byte[] messageBytes = DatatypeConverter.parseBase64Binary(base64Message);
		    final byte[] encryptedBytes = AES.encrypt(keyBytes, ivBytes, messageBytes);
		    
		    //CREATE HMAC
		    String writeHMAC = calculateHMAC(new String(encryptedBytes, "UTF-8"), base64KeyHMAC);
		    System.out.println(writeHMAC);
		    
		    
		    /***************
		     * REMOVE THIS LINE TO NOT CHANGE THE FILE
		     */
//		    encryptedBytes[5] = 'i';		    
		    /***************
		     * REMOVE THIS LINE TO NOT CHANGE THE FILE
		     */
		    writeToFile(encryptedBytes);
		    
		    readFromFile();
		    //GET HMAC
		    String endHMAC = calculateHMAC(new String(DatatypeConverter.parseBase64Binary(sb.toString()), "UTF-8"), base64KeyHMAC);
		    System.out.println(endHMAC);
		    

		    //COMPARE HMAC
		    if(writeHMAC.equals(endHMAC)) {
		    	final byte[] decryptedBytes = AES.decrypt(keyBytes, ivBytes, DatatypeConverter.parseBase64Binary(sb.toString()));
		    	System.out.println("HMAC CORRECT");
//		    	System.out.print(new String(messageBytes, "UTF-8"));
//		    	System.out.println("Encrypted: " + DatatypeConverter.printBase64Binary(encryptedBytes));
//		    	System.out.print(new String(decryptedBytes, "UTF-8"));
		    } else {
		    	System.out.println("HMAC incorrect, the file was changed");
		    }
		    
            
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
	}
	
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	public static String calculateHMAC(String data, String key) throws java.security.SignatureException {
		String result;
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(data.getBytes());
			result = Base64.getEncoder().encodeToString(rawHmac);
		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
		}
		return result;
	}
	
	public static void readFile() throws IOException {
		br = new BufferedReader(new FileReader("src/Bible.txt"));
		sb = new StringBuilder();
		String line = br.readLine();
		
		while (line != null) {
			sb.append(line);
			sb.append("\n");
			line = br.readLine();
		}
		br.close();
	}
	
	public static void writeToFile(byte[] encryptedBytes) throws UnsupportedEncodingException, FileNotFoundException {
		PrintWriter writer = new PrintWriter("src/encrypted.txt", "UTF-8");
	    writer.print(DatatypeConverter.printBase64Binary(encryptedBytes));
	    writer.close();
	}
	
	public static void readFromFile() throws IOException {
		br = new BufferedReader(new FileReader("src/encrypted.txt"));
		sb = new StringBuilder();
		String line = br.readLine();
		
		while (line != null) {
			sb.append(line);
			line = br.readLine();
		}
		br.close();
	}
}


