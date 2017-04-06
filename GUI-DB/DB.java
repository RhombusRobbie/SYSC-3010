 /*
 * table1 -> name,pass
 * table2 -> keyword, emotion
 * history -> event
 */

import java.sql.*; 
import java.util.*;
import java.io.IOException;
import java.lang.*;
import java.nio.file.Path;
import java.io.File;
public class DB {
	/*
	 * Constructor does the following
	 * 1) creates Table1 used to store name, pass
	 * 2) creates Table2 used to load in a text file with keyword, emotion.
	 * 3) creates Table3 used as History, stores every event.
	 */

	Connection con;
	public DB (){
		con = getConnection();
		createTable("CREATE TABLE IF NOT EXISTS table1 (name varchar(15), pass varchar(15))");
		createTable("CREATE TABLE IF NOT EXISTS table2 (keyword varchar(15), emotion varchar(15))");
		createTable("CREATE TABLE IF NOT EXISTS history (event TEXT)");
		// load keyword emotion to table2.
		
		
		/**
		 * This to check if we have the table loaded with keywords, if table is empty, we load keywords.
		 */
		if(!isLoaded()){
			System.out.println("Loading keywords.txt");
			String sqls = "LOAD DATA LOCAL INFILE 'D:\\keywords.txt' INTO TABLE table2 " +" FIELDS TERMINATED BY ' ' LINES TERMINATED BY '\\n'"; // E:\\
			Statement state = null;
			try{
				state = con.createStatement();
				state.executeUpdate(sqls);
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("NO need to load keywords.");
		}

		
		/**
		 * checks if there is a backup file from previous instance of the Database. if yes, it will be loaded, otherwise it will generate backup file.
		 */
		if((new File("D:\\a.sql").exists())){
			// if backup is there, load it.
			try{
				restoreTables();
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			try {
				saveTables();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
	}

	
   public void createTable(String str){
       try{
           
           PreparedStatement create = con.prepareStatement(str);
           create.executeUpdate();
        }catch(Exception e){System.out.println("Table, already created.");} 
       finally{System.out.println("Table created.");};
    }
   
   
   // determines if we need to load keywords.txt file into table2.
   public boolean isLoaded(){
		try {
			String query = "SELECT * FROM table2";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			return rs.next();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;

   }
   public static Connection getConnection(){
       try{
    	   Connection conn;
    	   conn = DriverManager.getConnection
    			   ("jdbc:mysql://localhost/?user=root&password=123456"); 
    	   conn.createStatement().executeUpdate("CREATE DATABASE IF NOT EXISTS db;");
           String driver = "com.mysql.jdbc.Driver";
           String url = "jdbc:mysql://localhost:3306/db";
           String username = "root";
           String password = "123456";
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
  
  //exports local database to file, in case of error/sudden shutdown of DB. This method to avoid getting data lost.
  public void saveTables() throws IOException{
      int processComplete=-1;
	try {
		processComplete = Runtime.getRuntime().exec("C:\\Program Files\\MySql\\MySql Server 5.7\\bin\\mysqldump.exe -u root -p123456 --add-drop-database -B db -r D:\\a.sql").waitFor();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      if (processComplete == 0) {
          System.out.println("Backup created successfully");
      } else {
    	  //System.out.println(processComplete);
          System.out.println("Could not create the backup");
      }
  }
  public void restoreTables() {
	  int processComplete=-1;
	  try {
		try {
			processComplete = Runtime.getRuntime().exec("C:\\Program Files\\MySql\\MySql Server 5.7\\bin\\mysqldump.exe -u root -p123456 -B db -r D:\\a.sql").waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    if (processComplete == 0) {
        System.out.println("Database Restored.");
    } else {
    	//System.out.println(processComplete);
        System.out.println("Could not Restore Database.");
    }
	  
  }
  
  
}
