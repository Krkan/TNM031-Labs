import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public class SSL_Client {

	private String _TRUSTSTORE = "cacerts.jks";
	private String _PASSWORD = "changeit";
	private int _PORT = 43000;
	private int FILE_SIZE = 6022386;

	private void connect() {
		try {

			System.setProperty("javax.net.ssl.trustStore", _TRUSTSTORE);
			System.setProperty("javax.net.ssl.trustStorePassword", _PASSWORD);

			SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket("localhost", _PORT);

			InputStream inputstream = System.in;
			InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
			BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

			OutputStream outputstream = sslsocket.getOutputStream();
			OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
			BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);

			System.out.print("Please enter a file name with extension: ");

			String string = null;
			string = bufferedreader.readLine();

			bufferedwriter.write(string + '\n');
			bufferedwriter.flush();

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

			inputstream.close();
			outputstream.close();
			fos.close();
			bos.close();
			sslsocket.close();
		} catch (Exception exception) {
			 exception.printStackTrace();
			System.out.println("Lost connection to server.");

		}
	}

	public static void main(String[] arstring) {

		SSL_Client r = new SSL_Client();
		r.connect();

	}
}
