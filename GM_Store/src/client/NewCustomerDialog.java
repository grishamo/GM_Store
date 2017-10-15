package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONObject;

import com.sun.javafx.tk.Toolkit;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.awt.event.ActionEvent;

public class NewCustomerDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField nameField;
	private JTextField idField;
	private JTextField telField;
	private PrintStream serverRequest;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewCustomerDialog dialog = new NewCustomerDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public NewCustomerDialog(PrintStream serverRequest) {
		this.serverRequest = serverRequest;
		initialize();
	}
	public NewCustomerDialog() {
		setResizable(false);
		initialize();
	}
	
	public void initialize() {
		setBounds(100, 100, 247, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		{
			JLabel label = new JLabel("NEW CUSTOMER");
			label.setBounds(16, 19, 192, 16);
			contentPanel.add(label);
		}
		{
			JSeparator separator = new JSeparator();
			separator.setBounds(6, 44, 235, 12);
			contentPanel.add(separator);
		}
		{
			JLabel label = new JLabel("Name:");
			label.setBounds(16, 68, 128, 16);
			contentPanel.add(label);
		}
		{
			nameField = new JTextField();
			nameField.setColumns(10);
			nameField.setBounds(6, 85, 235, 26);
			contentPanel.add(nameField);
		}
		{
			JLabel label = new JLabel("Id:");
			label.setBounds(16, 117, 128, 16);
			contentPanel.add(label);
		}
		{
			idField = new JTextField();
			idField.setColumns(10);
			idField.setBounds(6, 134, 235, 26);
			contentPanel.add(idField);
		}
		{
			JLabel label = new JLabel("Tel:");
			label.setBounds(16, 168, 128, 16);
			contentPanel.add(label);
		}
		{
			telField = new JTextField();
			telField.setColumns(10);
			telField.setBounds(6, 185, 235, 26);
			contentPanel.add(telField);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JSONObject reqObj = new JSONObject();
						JSONObject newCustomerObj = new JSONObject();
						
						newCustomerObj.put("name", nameField.getText());
						newCustomerObj.put("id", idField.getText());
						newCustomerObj.put("tel", telField.getText());
						
						reqObj.put("newCustomer", newCustomerObj);
						
						serverRequest.println(reqObj.toJSONString());
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						 dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}


}
