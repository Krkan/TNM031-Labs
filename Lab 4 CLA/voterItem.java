
public class voterItem {

	private Person somePerson;
	public boolean hasVoted = false;
	
	voterItem (Person sp){
		somePerson = sp;
	}
	
	voterItem (Person sp, boolean hv){
		somePerson = sp;
		hasVoted = hv;
	}
	
	public Person getVoter(){
		return somePerson;
	}
	
}
