import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class CLAServer {
	
	private SSLServerSocketFactory sslserversocketfactory;
	private SSLServerSocket sslserversocket;
	private SSLSocket sslsocket;
	private SSLSocket sslsocket_ctf;
	private InputStream inputstream;
	private ObjectInputStream ois;
	private Person voter;
	private ArrayList<Person> allowedVoters = new ArrayList<Person>();
	private ArrayList<String> validationCodes = new ArrayList<String>();
	private ArrayList<voterItem> voted = new ArrayList<voterItem>();
	private OutputStream outputstream;
	private ObjectOutputStream oos;
	private String vc;
	private SSLSocketFactory sslsocketfactory;
	
	
	//Populate the list of allowed voters.
	private void initiateList(){
		allowedVoters.add(new Person("Izmir", "Ibrahimovic",900723, 8733));
		allowedVoters.add(new Person("Peter", "Karlsson",900723, 2363));
		allowedVoters.add(new Person("Anna", "Johansson",670323, 2313));
		allowedVoters.add(new Person("Adam", "Lindberg",790117, 6363));
		allowedVoters.add(new Person("Lejla", "Pjanic",890214, 4367));


	}
	//Checks if the person is in the list of people allowed to vote.
	private boolean allowedToVote(Person p){
		boolean check = false;
		
		for (int i =0; i<allowedVoters.size(); i++){
			if(allowedVoters.get(i).equals(p)){
				check = true;
				break;
			}
		}
		return check;
		
	}
	//Checks if the person has voted or not.
	private boolean hasVoted(Person p){
		boolean check = false;
		
		for (int i =0; i<voted.size(); i++){
			if(voted.get(i).getVoter().equals(p)){
				check = voted.get(i).hasVoted;
				break;
			}
		}
		return check;
		
	}
	
	//Returns a SHA1 hash of a string. Used by getValidationCode.
	private String sha1(String input) throws NoSuchAlgorithmException {
	    MessageDigest mDigest = MessageDigest.getInstance("SHA1");
	    byte[] result = mDigest.digest(input.getBytes());
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < result.length; i++) {
	        sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
	    }
	     
	    return sb.toString();
	}
	
	//Return a unique hash value that's used as validation code.
	private String getValidationCode(Person p) throws NoSuchAlgorithmException{
		
		Calendar cal = Calendar.getInstance();
		Random rand = new Random();
		String validation = sha1(cal.getTimeInMillis()+rand.nextInt(512000)+p.getSSN());
		return validation;
		}
	
	
	public void startServer(){
		this.initiateList();
		System.setProperty("javax.net.ssl.keyStore", "keystore.jks");
    	System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
    	
    	System.setProperty("javax.net.ssl.trustStore", "truststore.jks");
    	System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
    	
    	
        try {
            sslserversocketfactory =(SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(9999);
            sslserversocket.setNeedClientAuth(true);
            
            while (true){
            
            System.out.println("Waiting for connection...\n");
            sslsocket = (SSLSocket) sslserversocket.accept();

           inputstream = sslsocket.getInputStream();
            ois = new ObjectInputStream(inputstream);
            voter = (Person)ois.readObject();
            
            if(allowedToVote(voter)){
            	
            System.out.println(voter.getSSN() + "\n");
            
            if(!hasVoted(voter)){
            vc = this.getValidationCode(voter);
            validationCodes.add(vc);
            voted.add(new voterItem(voter,true));
            System.out.println("Added new code to list. \n");
            sslsocketfactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
            sslsocket_ctf = (SSLSocket) sslsocketfactory.createSocket("localhost", 10000);
            outputstream = sslsocket_ctf.getOutputStream();
            oos = new ObjectOutputStream(outputstream);
            oos.writeObject(validationCodes);
            
            }else{
            	
            	vc ="VOTE_COUNTED_EARLIER";
            	
            }
            
            }
            else{
            	vc="UNAUTHORIZED_ACCESS";
            }
            outputstream = sslsocket.getOutputStream();
            oos = new ObjectOutputStream(outputstream);
            oos.writeObject(vc);
            
            }
           
            
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
	
 public static void main(String[] arstring) {
	 CLAServer c = new CLAServer();
	 c.startServer();
    }
    }
      	
