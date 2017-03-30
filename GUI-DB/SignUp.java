import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class SignUp extends JFrame {
    
   JLabel welcome = new JLabel("Please fill the fields below.");
   JPanel panel = new JPanel();
   JTextField txuser;
   JPasswordField pass;
   Login lgn;
   mainFrame mf;
   DB db;
SignUp(mainFrame mf, DB db){
    super("Sign Up");
    this.mf = mf;
    this.db = db;
    JButton signup = new JButton("SignUp");
    txuser = new JTextField(15);
    pass = new JPasswordField(15);
    JLabel label1 = new JLabel("Username:");
    JLabel label2 = new JLabel("Password:");
    
    setSize(300,300);
    setLocation(500,280);
    panel.setLayout (null); 
    
    welcome.setBounds(70,1,150,60);
    txuser.setBounds(70,50,150,20);
    pass.setBounds(70,115,150,20);
    signup.setBounds(110,200,80,20);
    label1.setBounds(5,50,150,20);
    label2.setBounds(5,115,150,20);
    
    panel.add(welcome);
    panel.add(txuser);
    panel.add(pass);
    panel.add(signup);
    panel.add(label1);
    panel.add(label2);
    
    getContentPane().add(panel);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    
    
    signup.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent ae){
        //
        Login lgn = new Login(mf,db);
        String str = new String(pass.getPassword());
        db.setUser(txuser.getText(), str);
        dispose();
       
    }
    
   }
   );
}

}
