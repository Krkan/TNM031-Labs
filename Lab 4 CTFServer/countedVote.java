
public class countedVote {
	
	private String id;
	private int vote;
	
	countedVote(String i, int v){
		id=i;
		vote=v;
	}
	
	countedVote(Vote v){
		id = v.getID();
		vote=v.getVote();
	}
	
	
	public String getID(){
		return id;
	}
	
	public int getVote(){
		return vote;
	}

}
