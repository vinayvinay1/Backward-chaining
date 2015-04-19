import java.io.*;
import java.util.*;
import java.lang.*;

public class Agent {

	
	public static void main(String []args) throws IOException{
		Scanner s = new Scanner (new BufferedReader(new FileReader("input.txt")));
		PrintWriter out = new PrintWriter(new FileWriter("output.txt"));
		
		// parse Goal into string array
		String goal = s.nextLine();
		goal = goal.replaceAll("[(]", ",");
		goal = goal.replaceAll("[)]", "");
		
		
		
		int constructs = s.nextInt();
		
		ArrayList<String[]> clauses = new ArrayList<String[]>();
		ArrayList<String> facts = new ArrayList<String>();
		
		for (int i=1;i<=constructs;i++){
			String temp = s.next();
			temp = temp.replaceAll("[(]", ",");
			temp = temp.replaceAll("[)]", "");
			if(temp.contains("=>")){
				String[] temp1;
				temp1 = temp.split("=>");
				
				String[] temp2 = {temp1[0],temp1[1]};
				
				clauses.add(temp2);	
			}
			else{
				facts.add(temp);	
			}
		}
		
		s.close();
		
		class BC{
			ArrayList<String> goals;
			ArrayList<String> facts;
			ArrayList<String[]> clauses;
			ArrayList<String> visited;
			
			BC(String g,ArrayList<String> f,ArrayList<String[]> c){
				goals = new ArrayList<String>(); 
				visited = new ArrayList<String>();
				goals.add(g);
				facts = f;
				clauses = c;
			}
			
			boolean factUnifyCheck (String temp){
				String[] temp1 = temp.split(",");
				for(int i=0;i<facts.size();i++){
					String[] temp2 = facts.get(i).split(",");
					
					if((temp1.length == temp2.length) && (temp1[0].equals(temp2[0]))){  // Proceed to further unify check only if fact and the predicate have same number of parameters
						
						// Check for unification when there is only one parameter
						if((temp1.length == 2) && ((temp1[1].equals("x")) || (temp1[1].equals(temp2[1])))){
							return true;
						}
						
						// Check for unification when there are two parameters
						if(temp1.length == 3){
							if(temp1[1].equals("x") && temp1[2].equals("x"))
								return true;
							if(temp1[1].equals("x") && temp1[2].equals(temp2[2]) )
								return true;
							if(temp1[2].equals("x") && temp1[1].equals(temp2[1]) )
								return true;
							if(temp1[1].equals(temp2[1]) && temp1[2].equals(temp2[2]))
								return true;
						}	
					}
				}
				return false;
			}
			
			
			
			boolean predicateUnifyCheck (String c, String q){
				String[] temp1 = c.split(",");
				String[] temp2 = q.split(",");
				
				if((temp1.length == temp2.length) && (temp1[0].equals(temp2[0]))){  // Proceed to further unify check only if fact and the predicate have same number of parameters
					
					// Check for unification when there is only one parameter
					if((temp1.length == 2) && ((temp1[1].equals("x")) || (temp1[1].equals(temp2[1])))){
						return true;
					}
					
					// Check for unification when there are two parameters
					if(temp1.length == 3){
						if(temp1[1].equals("x") && temp1[2].equals("x"))
							return true;
						if(temp1[1].equals("x") && temp1[2].equals(temp2[2]) )
							return true;
						if(temp1[2].equals("x") && temp1[1].equals(temp2[1]) )
							return true;
						if(temp1[1].equals(temp2[1]) && temp1[2].equals(temp2[2]))
							return true;
					}	
				}
				return false;	
			}
			
			
			boolean bcCheck(){
				while(!goals.isEmpty()){   //As long as Stack is not empty
					
					String q = goals.remove(goals.size()-1);
					visited.add(q);  //keep track of explored predicates to prevent searching them again
			 
					
					if (!factUnifyCheck(q)){
						ArrayList<String> p = new ArrayList<String>(); // Arraylist to store new predicates that will be found in this loop
						for(int i=0;i<clauses.size();i++){
								if (predicateUnifyCheck(q,clauses.get(i)[1]) || predicateUnifyCheck(clauses.get(i)[1],q)){
										String[] temp = clauses.get(i)[0].split("&");
										for(int j=0;j<temp.length;j++){
											if(!goals.contains(temp[j]))
												p.add(temp[j]);
										}
								}
								
								
						}
						
						
						if (p.size()==0){
							return false;
						}
						else{
								for(int k=0;k<p.size();k++){
										if (!visited.contains(p.get(k)))
												goals.add(p.get(k));		
								}
						}
					}
			 
				}
				return true;
			}	
		}
		
		
		BC bc1 = new BC(goal,facts,clauses);
		String output = bc1.bcCheck()?"TRUE":"FALSE";
		
		out.print(output);
		out.close();
	}
}




