import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class CTFServer {

	private InputStream in;
	private ObjectInputStream ois;
	private ArrayList<String> validationCodes;
	private ArrayList<String> usedCodes = new ArrayList<String>();
	private ArrayList<countedVote> recievedVotes = new ArrayList<countedVote>();
	private SSLServerSocketFactory sslserversocketfactory;
	private SSLServerSocket sslserversocket;
	private SSLSocket sslsocket_cla;
	
	private SSLServerSocket sslserversocket2;
	private SSLSocket sslsocket_client;
	
	private Vote aVote;
	private int count=0;
	
	//Checks if the received validation code is valid.
	private byte checkValidationCode(Vote v){
		
		byte result = 0;
		boolean found = false;
		
		for (int i = 0; i<validationCodes.size(); i++){
			
			if(validationCodes.get(i).equals(v.getValidationCode())){
				
				result = 1;
				found=true;
				
				for (int j =0; j<usedCodes.size(); j++){
					
					if(usedCodes.get(j).equals(v.getValidationCode())){
						result = 2;
						
						break;
					}
					
				}
		
			}
			
			if(found){
				break;
			}
			
		}
		
		return result;
	}
	
	private void printResults(){
		
		
		for(int i=0; i<recievedVotes.size(); i++){
			
			
			System.out.println(recievedVotes.get(i).getID() + " voted for " + recievedVotes.get(i).getVote());
			
		}
		int c = count+1;
		
		System.out.println("\nNumber of people voted: " + c +"\n");
	
	}
	
	
	@SuppressWarnings("unchecked")
	public void startServer(){
		
		System.setProperty("javax.net.ssl.keyStore", "ctfkeystore.jks");
    	System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
    	
    	System.setProperty("javax.net.ssl.trustStore", "truststore.jks");
    	System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
    	
    	try{
    	sslserversocketfactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
    	sslserversocket = (SSLServerSocket)sslserversocketfactory.createServerSocket(10000);
    	sslserversocket.setNeedClientAuth(true);
    	
    	
    	sslserversocket2 = (SSLServerSocket)sslserversocketfactory.createServerSocket(10001);
    	sslserversocket2.setNeedClientAuth(true);
    	
    	while (true){
    	System.out.println("Waiting for connection...\n");
    	sslsocket_cla = (SSLSocket)sslserversocket.accept();
    	
    	in = sslsocket_cla.getInputStream();
    	ois = new ObjectInputStream(in);
    	validationCodes = (ArrayList<String>)ois.readObject();
    	System.out.println("Recieved new code list.\n");
    	sslsocket_client = (SSLSocket)(SSLSocket)sslserversocket2.accept();
    	in=sslsocket_client.getInputStream();
    	ois = new ObjectInputStream(in);
    	aVote = (Vote)ois.readObject();
    	byte temp = this.checkValidationCode(aVote);
    	
    	if (temp == 0){
    		System.out.println("Error, invalid validation code.\n");
    	}
    	else if (temp == 1){
    		System.out.println("Valid code, you are allowed to vote.\n");
    		recievedVotes.add(new countedVote(aVote));
    		usedCodes.add(aVote.getValidationCode());
    		
    		if(count >= 4){
        		this.printResults();
        	}
        	
    		
    		count++;

    	}
    	else if(temp == 2){
    		System.out.println("Code has already been used.\n");

    	}
    	
    
    	
    	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	
    	
	}
	
	
	
	public static void main(String[] args){
		CTFServer r = new CTFServer();
		r.startServer();
	}
	
	
	
}
