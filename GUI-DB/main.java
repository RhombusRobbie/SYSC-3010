
public class main
{
    // instance variables - replace the example below with your own

    /**
     * Constructor for objects of class main
     */
    public static void main(String args[])
    {
           DB db = new DB();
           db.createTable();
           mainFrame mf = new mainFrame();
           Login lgn = new Login(mf);
    }

    
}
