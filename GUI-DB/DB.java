 /*
 * table1 -> name,pass
 * table2 -> keyword, emotion
 * history -> event
 */
import java.sql.*;
 
public class DB {
	/*
	 * Constructor does the following
	 * 1) creates Table1 used to store name, pass
	 * 2) creates Table2 used to load in a text file with keyword, emotion.
	 * 3) creates Table3 used as History, stores every event.
	 */
	Connection con;
	public DB(){
		con = getConnection();
		createTable("CREATE TABLE IF NOT EXISTS table1 (name varchar(15), pass varchar(15))");
		createTable("CREATE TABLE IF NOT EXISTS table2 (keyword varchar(15), emotion varchar(15))");
		createTable("CREATE TABLE IF NOT EXISTS history (event text)");
	}
   public void createTable(String str){
       try{
           
           PreparedStatement create = con.prepareStatement(str);
           create.executeUpdate();
        }catch(Exception e){System.out.println("Cannot create tables"); System.out.println(e);} 
       finally{System.out.println("Table created.");};
    }
   
   
   public static Connection getConnection(){
       try{
    	   Connection conn;
    	   conn = DriverManager.getConnection
    			   ("jdbc:mysql://localhost/?user=root&password=123456"); 
    	   conn.createStatement().executeUpdate("CREATE DATABASE db");
           String driver = "com.mysql.jdbc.Driver";
           String url = "jdbc:mysql://localhost:3306/db";
           String username = "husin";
           String password = "1234";
           Class.forName(driver);
           
           conn = DriverManager.getConnection(url, username, password);
           
           System.out.println("Connected");
           return conn;
        }catch(Exception e){
            System.out.println("Cannot establish connection to Database"); System.out.println(e);
        }
       return null;
    }

  public boolean contains(String user, String pass){
      try{
          PreparedStatement statement = con.prepareStatement("SELECT name,pass FROM table1");
          ResultSet result = statement.executeQuery();
          while(result.next()){
              if(result.getString("name").equals(user) && result.getString("pass").equals(pass)){
                  return true;
                }
              
            }
            return false;
        }catch(Exception e){System.out.println("contains(user,pass): Error"); System.out.println(e);}
        return false;
    }
   public String getEmotion(String keyword){
      try{
          PreparedStatement statement = con.prepareStatement("SELECT keyword,emotion FROM table2");
          ResultSet result = statement.executeQuery();
          while(result.next()){
              if(result.getString("keyword").equals(keyword)){
                  return result.getString("emotion");
                }
              
            }
            return null;
        }catch(Exception e){System.out.println("contains(keyword): Error"); System.out.println(e);}
        return null;
    }
  public void setUser(String user, String pass){
      try{
          PreparedStatement posted = con.prepareStatement("INSERT INTO table1 (name, pass) VALUES ('"+user+"', '"+pass+"')");
          posted.executeUpdate();
        }catch(Exception e){
        	System.out.println("setUser(user,pass): Error"); System.out.println(e);;
        }
        finally{
            System.out.println("Insert Complete.");};
    }
  public void setEvent(String event){
	  try{
		PreparedStatement posted = con.prepareStatement("INSERT INTO history (event) VALUES ('"+event+"')");
	  	posted.executeUpdate();
	  }catch(Exception e){ System.out.println("setEvent: Error, "+ e);
	  }finally{
		System.out.println("Insert Complete.");
	  };
  }
}
