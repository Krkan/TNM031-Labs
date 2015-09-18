import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public class SSL_Client {

	private String _TRUSTSTORE = "cacerts.jks";
	private String _PASSWORD = "changeit";
	private int _PORT = 43000;
	private int FILE_SIZE = 6022386;
	
	private SSLSocketFactory sslsocketfactory;
	private SSLSocket sslsocket; 
	private InputStream inputstream;
	private InputStreamReader inputstreamreader;
	private BufferedReader bufferedreader;
	private OutputStream outputstream;
	private OutputStreamWriter outputstreamwriter;
	private BufferedWriter bufferedwriter;
	private String choice;
	
	private void connect() {
		try {

			System.setProperty("javax.net.ssl.trustStore", _TRUSTSTORE);
			System.setProperty("javax.net.ssl.trustStorePassword", _PASSWORD);

			sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			sslsocket = (SSLSocket) sslsocketfactory.createSocket("localhost", _PORT);

			inputstream = System.in;
			inputstreamreader = new InputStreamReader(inputstream);
			bufferedreader = new BufferedReader(inputstreamreader);

			outputstream = sslsocket.getOutputStream();
			outputstreamwriter = new OutputStreamWriter(outputstream);
			bufferedwriter = new BufferedWriter(outputstreamwriter);

			System.out.println("1. Download file\n2. Upload file\n3. Delete file \n");
			
			choice = bufferedreader.readLine();
			
			bufferedwriter.write(choice + '\n');
			bufferedwriter.flush();
			
			if (choice.equals("1")){
				

			this.download();
			
			}else if(choice.equals("3")){
				
				this.delete();
			}
			sslsocket.close();

			
		} catch (Exception exception) {
			 //exception.printStackTrace();
			System.out.println("Lost connection to server.");

		}
	}
	
	
	private void download(){
		try{
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
		}catch (Exception e){
			System.out.println("Error in dowloand function.");
		}
		
		
	}
	
	private void delete(){
		
		try{
		System.out.print("Please enter a file name with extension to delete: ");

		String string = null;
		string = bufferedreader.readLine();

		bufferedwriter.write(string + '\n');
		bufferedwriter.flush();
		System.out.println("File " + string + " deleted!");
		}catch(Exception e){
			System.out.println("Error in delete function.");
		}
		
	}

	public static void main(String[] arstring) {

		SSL_Client r = new SSL_Client();
		r.connect();

	}
}
