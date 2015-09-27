import java.io.Serializable;

public class Vote implements Serializable {
private String id;
private String validationCode;
private int vote;


	Vote(String i, String va,int vo){
	id=i;
	validationCode=va;
	vote=vo;
	}

	
	public String getID(){
		return id;
	}
	
	public String getValidationCode(){
		return validationCode;
	}
	
	public int getVote(){
		return vote;
	}

}
