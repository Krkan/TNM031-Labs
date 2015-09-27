import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class Client {
	
	private SSLSocketFactory sslsocketfactory;
	private SSLSocket sslsocket;
	private SSLSocket sslsocket2;
	private OutputStream outputstream;
	private ObjectOutputStream os;
	private Person user;
	private InputStream inputstream;
	private ObjectInputStream ois;
	private Vote theVote;
	
	private String sha1(String input) throws NoSuchAlgorithmException {
	    MessageDigest mDigest = MessageDigest.getInstance("SHA1");
	    byte[] result = mDigest.digest(input.getBytes());
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < result.length; i++) {
	        sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
	    }
	     
	    return sb.toString();
	}
	
	private String generateID(String cla) throws NoSuchAlgorithmException{
		
		Calendar cal = Calendar.getInstance();
		return sha1(cla + cal.getTimeInMillis());
		
	}
	
	
	public void startClient(){
		
		System.setProperty("javax.net.ssl.keyStore", "clientkeystore.jks");
    	System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
		
	  	System.setProperty("javax.net.ssl.trustStore", "truststore.jks");
    	System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
    	
        sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        
 
        try {
            sslsocket = (SSLSocket) sslsocketfactory.createSocket("localhost", 9999);
            outputstream = sslsocket.getOutputStream();
       
            os = new ObjectOutputStream(outputstream);
            //user = new Person("Izmir", "Ibrahimovic",880722, 8633);
            //user = new Person("Peter", "Karlsson",900723, 2363);
            //user = new Person("Anna", "Johansson",670323, 2313);
            //user = new Person("Adam", "Lindberg",790117, 6363);
            //user = new Person("Lejla", "Pjanic",890214, 4367);
            //user = new Person("Erik", "Persson",000217, 5377);
            
           
            os.writeObject(user);
            
            inputstream = sslsocket.getInputStream();
            ois = new ObjectInputStream(inputstream);
            String cla_result = (String)ois.readObject();
            System.out.println("Returned data from CLA: " + cla_result+"\n");
            
            
            if(!cla_result.equals("VOTE_COUNTED_EARLIER") && !cla_result.equals("UNAUTHORIZED_ACCESS") ){
            theVote = new Vote(this.generateID(cla_result),cla_result,1);
            
            
            sslsocket2 = (SSLSocket) sslsocketfactory.createSocket("localhost", 10001);
            outputstream = sslsocket2.getOutputStream();
            os = new ObjectOutputStream(outputstream);
            os.writeObject(theVote);
            sslsocket2.close();

            }
            sslsocket.close();
            
        } catch (Exception exception) {
            exception.printStackTrace();
        }
	}
	
	
	public static void main(String[] arstring) {
Client c = new Client();
c.startClient();
  
    }
}
      