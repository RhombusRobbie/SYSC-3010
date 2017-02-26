import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.JOptionPane;


public class Login {
    private class User{
        String name;
        int pass;
        User(String name, int pass){
            this.name = name;
            this.pass = pass;
        }
    }
    //User[] userArray = new User[10]; // up to 10 users
    JFrame fr;
    mainFrame mf;
    public Login(mainFrame mf){
       this.mf = mf;
       fr = new JFrame();
       JButton login = new JButton("Login");
       JButton signup = new JButton("SignUp");
       JPanel panel = new JPanel();
       JTextField txuser = new JTextField(15);
       JPasswordField pass = new JPasswordField(15);
       fr.setSize(300,200);
       fr.setLocation(500,280);
       panel.setLayout (null); 
        
        
       txuser.setBounds(70,30,150,20);
       pass.setBounds(70,65,150,20);
       login.setBounds(70,100,80,20);
       signup.setBounds(150,100,80,20);
        
       panel.add(login);
       panel.add(signup);
       panel.add(txuser);
       panel.add(pass);
        
       fr.getContentPane().add(panel);
       fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       fr.setVisible(true);
       
       login.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent ae){
              String puname = txuser.getText();
              String ppaswd = new String(pass.getPassword());
              if((new DB()).contains(puname, ppaswd) == 1){
                  // true password and username
                  //.setVisible(true);
                  mf.setVisible(true);
                  fr.dispose();
                }else{
                    // not true
                    
                    JOptionPane.showMessageDialog(null, "Wrong      Password    /   Username");
                    txuser.setText("");
                    pass.setText("");
                    txuser.requestFocus();
                    
                }
                
            }
        }
        );
        
        
        signup.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                //
                SignUp signup = new SignUp(mf);
                signup.setVisible(true);
                fr.dispose();
               
            }
            
        }
        );
        
        
        
        
       
    }
    
    
}


