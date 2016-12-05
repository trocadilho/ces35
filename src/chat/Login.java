package chat;

import java.util.*;
import java.io.*;


public class Login implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String login;
	String pass;
	
	public Login(String login, String pass)
	{
		this.login = login;
		this.pass = pass;	

	}
	
	public static int searchLogin(String string, ArrayList<Login> list){
		int found = -1;
		for(int i = 0;i<list.size();i++){
			if(list.get(i).login.equals(string)) //searching
				found = i;
			}
		return found;
	}
	
	public ArrayList<Login> addLogin(ArrayList<Login> list, Login Login)
	{
		list.add(Login);
		return list;
	}
	
	public static void Serialize (ArrayList<Login> list)
	{
	      try
	      {
	         FileOutputStream fileOut = new FileOutputStream("Logins.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(list);
	         out.close();
	         fileOut.close();
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
	}

	public static ArrayList<Login> Load()
	{
		ArrayList<Login> list = new ArrayList<Login>();
	    try
	    {
	       FileInputStream fileIn = new FileInputStream("Logins.ser");
	       ObjectInputStream in = new ObjectInputStream(fileIn);
	       list = (ArrayList<Login>) in.readObject();
	       in.close();
	       fileIn.close();
	       return list;
	    }catch(IOException i)
	    {
	       //i.printStackTrace();
	       return list;
	    }catch(ClassNotFoundException c)
	    {
	       //System.out.println("Login class not found");
	       //c.printStackTrace();
	       return list;
	    }
	}
		
}
