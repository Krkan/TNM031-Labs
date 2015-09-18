import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class SSL_Server {

	private String _KEYSTORE = "keystore.jks";
	private String _PASSWORD = "changeit";
	private int _PORT = 43000;

	private SSLServerSocketFactory sslserversocketfactory;
	private SSLServerSocket sslserversocket;
	private SSLSocket sslsocket;
	private InputStream inputstream;
	private InputStreamReader inputstreamreader;
	private BufferedReader bufferedreader;
	private String choice;
	
	private void startServer() {

		System.setProperty("javax.net.ssl.keyStore", _KEYSTORE);
		System.setProperty("javax.net.ssl.keyStorePassword", _PASSWORD);
		try {
			sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory
					.getDefault();
			sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(_PORT);
			System.out.println("Awaiting conection...");

			sslsocket = (SSLSocket) sslserversocket.accept();

			inputstream = sslsocket.getInputStream();
			inputstreamreader = new InputStreamReader(inputstream);
			bufferedreader = new BufferedReader(inputstreamreader);

			choice = bufferedreader.readLine();
			
			if (choice.equals("1")){
				this.uploadFile();
			}
			else if(choice.equals("3")){
				this.deleteFile();
		
		}
			sslsocket.close();

		} catch (Exception exception) {
			// exception.printStackTrace();
			System.out.println("Client closed connection.");
		}

	}
	
	
	private void uploadFile(){
		
		try{
		String string = null;
		string = bufferedreader.readLine();

		File myFile = new File(string);
		byte[] mybytearray = new byte[(int) myFile.length()];
		FileInputStream fis = new FileInputStream(myFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		bis.read(mybytearray, 0, mybytearray.length);
		OutputStream os = sslsocket.getOutputStream();
		System.out.println("Sending " + string + "(" + mybytearray.length + " bytes)");
		os.write(mybytearray, 0, mybytearray.length);
		os.flush();
		System.out.println("Done.");

		bis.close();
		inputstream.close();
		os.close();
		}catch(Exception e){
			System.out.println("Error in upload function.");
		}
		
	}
	
	private void deleteFile(){
		
		try{
			String string = null;
			string = bufferedreader.readLine();
			File myFile = new File(string);
			myFile.delete();
			System.out.println("Deleted file " + string);
			
		}catch(Exception e){
			System.out.println("Error in delete function.");
		}
		
	}

	public static void main(String[] arstring) {
		SSL_Server r = new SSL_Server();
		r.startServer();
	}
}
