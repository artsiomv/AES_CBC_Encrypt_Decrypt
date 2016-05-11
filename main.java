import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class main {

	public static BufferedReader br;
	public static StringBuilder sb;
	public static void main(String[] args) {
		try {
			final String base64Key = "ABEiM0RVZneImaq7zN3u/w==";
		    final String base64Iv = "AAECAwQFBgcICQoLDA0ODw==";
		    final byte[] keyBytes = DatatypeConverter.parseBase64Binary(base64Key);
		    final byte[] ivBytes = DatatypeConverter.parseBase64Binary(base64Iv);
		    
		    //Want to modify encoded txt? outcomment these lines
			readFile();
		    String base64Message = DatatypeConverter.printBase64Binary(sb.toString().getBytes());
		    final byte[] messageBytes = DatatypeConverter.parseBase64Binary(base64Message);
		    final byte[] encryptedBytes = AES.encrypt(keyBytes, ivBytes, messageBytes);
		    writeToFile(encryptedBytes);
		    ///////////////////
		    
		    readFromFile();
			
		    final byte[] decryptedBytes = AES.decrypt(keyBytes, ivBytes, DatatypeConverter.parseBase64Binary(sb.toString()));//encryptedBytes);        

		    System.out.println("Message  : " + new String(messageBytes, "UTF-8"));
		    System.out.println("Encrypted: " + DatatypeConverter.printBase64Binary(encryptedBytes));
		    System.out.println("Decrypted: " + new String(decryptedBytes, "UTF-8"));
            
            
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
		}
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
			sb.append("\n");
			line = br.readLine();
		}
		br.close();
	}
}


