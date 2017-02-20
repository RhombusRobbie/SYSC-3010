 
import java.sql.*;
 
public class DB {

   public static void createTable() throws Exception{
       try{
           Connection con = getConnection();
           PreparedStatement create = con.prepareStatement("CREATE TABLE IF NOT EXISTS table1 (name varchar(15), pass varchar(15))");
           create.executeUpdate();
        }catch(Exception e){System.out.println(e);} 
       finally{System.out.println("Function complete.");};
    }
   
   public static Connection getConnection() throws Exception{
       try{
           String driver = "com.mysql.jdbc.Driver";
           String url = "jdbc:mysql://localhost:3306/testdb";
           String username = "husin";
           String password = "1234";
           Class.forName(driver);
           
           Connection conn = DriverManager.getConnection(url, username, password);
           
           System.out.println("Connected");
           return conn;
        }catch(Exception e){
            System.out.println(e);
        }
       return null;
    }

  public int contains(String user, String pass){
      try{
          Connection con = getConnection();
          PreparedStatement statement = con.prepareStatement("SELECT name,pass FROM table1");
          ResultSet result = statement.executeQuery();
          while(result.next()){
              if(result.getString("name").equals(user) && result.getString("pass").equals(pass)){
                  return 1;
                }
              
            }
            return 0;
        }catch(Exception e){System.out.println(e);}
        return 0;
    }
  public void setUser(String user, String pass){
      try{
          Connection con = getConnection();
          PreparedStatement posted = con.prepareStatement("INSERT INTO table1 (name, pass) VALUES ('"+user+"', '"+pass+"')");
          posted.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
        }
        finally{
            System.out.println("Insert Complete.");};
    }
}
