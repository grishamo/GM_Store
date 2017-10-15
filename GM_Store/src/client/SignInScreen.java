package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
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
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class SignInScreen {

	JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;
	private PrintStream outputStream;
	private JSONObject jsonData;
	String serverResponse;
	
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
		jsonData = new JSONObject();
		outputStream = request;
		initialize();
	}
	
	public SignInScreen() {
	}
	
	public void showMessage(String text) {
		JOptionPane.showMessageDialog(null, text);
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblUserName = new JLabel("Id :");
		lblUserName.setBounds(69, 95, 74, 16);
		
		textField = new JTextField();
		textField.setBounds(150, 90, 215, 26);
		textField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setBounds(69, 131, 67, 16);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(lblUserName);
		frame.getContentPane().add(textField);
		frame.getContentPane().add(lblPassword);
		
		JButton btnSignIn = new JButton("Sign In");
		btnSignIn.setBounds(248, 164, 117, 29);
		frame.getContentPane().add(btnSignIn);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(150, 126, 215, 26);
		frame.getContentPane().add(passwordField);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Sign In", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(44, 52, 362, 174);
		frame.getContentPane().add(panel);
		
		
		btnSignIn.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				JSONObject signInJson = new JSONObject();
				
				if( textField.getText().isEmpty() || (passwordField.getText().isEmpty()))
					JOptionPane.showMessageDialog(null, "Data Missing");
				else	 {
					signInJson.put("id", textField.getText());
					signInJson.put("password", passwordField.getText());
					
					jsonData.put("signin", signInJson);
					
					//send sign in data to server
					outputStream.println(jsonData.toJSONString());
					System.out.println("SignIn data sent: " + jsonData.toJSONString());
					

				}	
					
				
			}
		});
	}
	
	
}
