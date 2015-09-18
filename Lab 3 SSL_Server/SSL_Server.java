
//Izmir labb3
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class SSL_Server {

	private String _KEYSTORE = "keystore.jks";
	private String _PASSWORD = "changeit";
	private int _PORT = 43000;
	private int FILE_SIZE = 6022386;

	private SSLServerSocketFactory sslserversocketfactory;
	private SSLServerSocket sslserversocket;
	private SSLSocket sslsocket;
	private InputStream inputstream;
	private InputStreamReader inputstreamreader;
	private BufferedReader bufferedreader;
	private String choice;

	private void startServer() {
		// Set the keystore and password settings

		System.setProperty("javax.net.ssl.keyStore", _KEYSTORE);
		System.setProperty("javax.net.ssl.keyStorePassword", _PASSWORD);
		try {
			// Create an SSL socket that listens
			sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(_PORT);
			System.out.println("Awaiting conection...");

			sslsocket = (SSLSocket) sslserversocket.accept();

			// Get a choice from the client
			inputstream = sslsocket.getInputStream();
			inputstreamreader = new InputStreamReader(inputstream);
			bufferedreader = new BufferedReader(inputstreamreader);
			// Gets an operation type from the client.
			choice = bufferedreader.readLine();

			if (choice.equals("1")) {
				this.uploadFile();
			} else if (choice.equals("3")) {
				this.deleteFile();

			} else if (choice.equals("2")) {
				this.download();

			}
			// Close the connection when done with the operation.
			sslsocket.close();

		} catch (Exception exception) {
			// exception.printStackTrace();
			System.out.println("Client closed connection.");
		}

	}

	private void uploadFile() {
		// Sends the requested file to the client.
		try {
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
		} catch (Exception e) {
			System.out.println("Error in upload function.");
		}

	}

	private void deleteFile() {
		// Deletes a file from the server, requested by the client.
		try {
			String string = null;
			string = bufferedreader.readLine();
			File myFile = new File(string);
			myFile.delete();
			System.out.println("Deleted file " + string);

		} catch (Exception e) {
			System.out.println("Error in delete function.");
		}

	}

	private void download() {

		// Downloads a file from the client.
		try {
			String string = null;
			string = bufferedreader.readLine();

			byte[] mybytearray = new byte[FILE_SIZE];
			InputStream is = sslsocket.getInputStream();
			FileOutputStream fos = new FileOutputStream(string);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			int bytesRead = is.read(mybytearray, 0, mybytearray.length);
			int current = bytesRead;

			do {
				bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
				if (bytesRead >= 0)
					current += bytesRead;
			} while (bytesRead > -1);

			bos.write(mybytearray, 0, current);
			bos.flush();
			System.out.println("File " + string + " downloaded (" + current + " bytes read)");
			fos.close();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in download function.");
		}

	}

	public static void main(String[] arstring) {
		SSL_Server r = new SSL_Server();
		r.startServer();
	}
}
