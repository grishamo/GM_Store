package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.sql.Date;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JSeparator;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import org.json.simple.JSONObject;

public class SignInScreen {

	JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;
	private PrintStream outputStream;
	private JSONObject jsonData;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//					SignInScreen window = new SignInScreen();
//					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	public SignInScreen(PrintStream request) {
		initialize(request);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(PrintStream output) {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblUserName = new JLabel("Id :");
		lblUserName.setBounds(70, 70, 74, 16);
		
		textField = new JTextField();
		textField.setBounds(151, 65, 215, 26);
		textField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setBounds(70, 106, 67, 16);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(lblUserName);
		frame.getContentPane().add(textField);
		frame.getContentPane().add(lblPassword);
		
		JButton btnSignIn = new JButton("Sign In");
		btnSignIn.setBounds(249, 139, 117, 29);
		frame.getContentPane().add(btnSignIn);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(151, 101, 215, 26);
		frame.getContentPane().add(passwordField);
		
		jsonData = new JSONObject();
		outputStream = output;
		
		btnSignIn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
				JSONObject signInJson = new JSONObject();
				
				if( textField.getText().isEmpty() || (passwordField.getText().isEmpty()))
					JOptionPane.showMessageDialog(null, "Data Missing");
				else	 {
					signInJson.put("id", textField.getText());
					signInJson.put("password", passwordField.getText());
					
					jsonData.put("signin", signInJson);
					
					outputStream.println(jsonData.toJSONString());
					System.out.println("SignIn data sent: " + jsonData.toJSONString());
					
					JOptionPane.showMessageDialog(null, "Data Sent:\n" + "Id: " + signInJson.get("id")  + " \nPassword: " + signInJson.get("password"));
				}	
					
				
			}
		});
	}
	
	
}
