import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class mainFrame extends JFrame {
   JTextArea textArea;   
   JPanel panel1 = new JPanel();
   JPanel panel2 = new JPanel();
   
   mainFrame(){
      super("Welcome");
        //String all = "";
        //try{
       //     all = new Scanner(new File("C:\\Users\\Husin\\Desktop\\fafa.txt")).useDelimiter("\\A").next();
       // }catch(Exception e){System.out.println(e);}
        JLabel label = new JLabel("                                Event history");
        setLocation(500,280);
        //panel1.setLayout(new BorderLayout()); 
        //panel2.setLayout(new BorderLayout());
        setLayout(new BorderLayout());
        
        textArea = new JTextArea(10,20);
        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textArea.setEditable(false);
       // textArea.setText(all);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        //textArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), 
        //        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        scrollPane.setPreferredSize(new Dimension(700,400));
        //panel2.setPreferredSize(new Dimension(100,100));
        panel1.add(scrollPane);
        Button clear = new Button("Clear History");
        panel2.add(clear);
        panel2.add(new Button("     Export History    "));
        //scrollPane.setSize(1000,3000);
        //setSize(1000,1000);     
        clear.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
            	textArea.setText(null);
              }
          }
          );
        //panel1.setBounds(0,0,250, 500);
        //panel2.setBounds(252, 0, 248, 500);
        getContentPane().add(panel1, BorderLayout.CENTER);
        getContentPane().add(panel2, BorderLayout.EAST);
        getContentPane().add(label, BorderLayout.NORTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        //setVisible(true);
    }

public void update(String str){
    if(textArea.getText() == ""){
        textArea.setText(str);
    }else{
        textArea.setText(textArea.getText() + "\n" + str);
    }
}


}
