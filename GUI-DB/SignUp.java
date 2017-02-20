import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class SignUp extends JFrame {

public static void main(String[] args) {
    SignUp frameTabe2 = new SignUp();
}
    
   JLabel welcome = new JLabel("Please fill the fields below.");
   JPanel panel = new JPanel();
   JTextField txuser;
   JPasswordField pass;
   Login lgn;
SignUp(){
    
    super("Sign Up");
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
        Login lgn = new Login();
        String str = new String(pass.getPassword());
        (new DB()).setUser(txuser.getText(), str);
        lgn.setVisible(true);
        dispose();
       
    }
    
   }
   );
}

}
