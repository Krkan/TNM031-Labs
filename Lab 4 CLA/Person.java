import java.io.Serializable;

public class Person implements Serializable{

	private String firstName;
	private String lastName;
	private int dateOfBirth;
	private int lastSSN;
	
	Person(String fn,String ln,int dof,int lssn){
		firstName=fn;
		lastName=ln;
		dateOfBirth=dof;
		lastSSN=lssn;
		}
	
	public String getName(){
		return firstName;
	}
	
	public String getLastName(){
		return lastName;
	}
	
	public String getSSN(){
		return dateOfBirth + "-" + lastSSN;
	}
	
	public boolean equals(Person p){
		boolean check = false;
		if(p.getLastName().equals(lastName) && p.getName().equals(firstName) && p.getSSN().equals(this.getSSN())){
			check = true;
		}
		return check;
	}
	
	
}
