package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AdminDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField userNameField;
	private JTextField passwordField;
	private JTextField nameField;
	private JTextField telField;
	private JTextField idField;
	private JTextField empIdField;
	private JTable employeeTable;
	private DefaultTableModel defaultTableModel = new DefaultTableModel();
	private JTextField bankField;
	private JComboBox storeCombo;
	private JComboBox typeCombo;
	private JPanel panelPassword;
	private DataInputStream serverResponse;
	private PrintStream serverRequest;
	private JSONObject allEmployeesList;
	private JSONParser jsonparser = new JSONParser();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AdminDialog dialog = new AdminDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public AdminDialog( DataInputStream serverResponse, PrintStream serverRequest ) {
		this.serverResponse = serverResponse;
		this.serverRequest = serverRequest;
		
		
		initialize();
	}
	
	public AdminDialog() {
		initialize();
	}
	
	/**
	 * Create the dialog.
	 */
	private void initialize() {
		setBounds(100, 100, 566, 469);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(23, 49, 520, 100);
		contentPanel.add(scrollPane);
		
		employeeTable = new JTable();
		employeeTable.setBackground(new Color(240, 248, 255));
		employeeTable.setBorder(new EmptyBorder(2, 2, 2, 2));
		scrollPane.setViewportView(employeeTable);
		employeeTable.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		    		int selectedRow = employeeTable.getSelectedRow();
		    		setEmployeeInputs(selectedRow);
		    }
		});
		
		panelPassword = new JPanel();
		panelPassword.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelPassword.setBounds(23, 343, 518, 44);
		contentPanel.add(panelPassword);
		panelPassword.setLayout(null);
		
		JLabel lblUserName = new JLabel("User Name:");
		lblUserName.setBounds(33, 12, 72, 16);
		lblUserName.setHorizontalAlignment(SwingConstants.RIGHT);
		panelPassword.add(lblUserName);
		
		userNameField = new JTextField();
		userNameField.setBackground(new Color(220, 220, 220));
		userNameField.setEditable(false);
		userNameField.setBounds(110, 8, 116, 26);
		userNameField.setColumns(10);
		panelPassword.add(userNameField);
		
		JLabel lblPassword = new JLabel("New Password:");
		lblPassword.setBounds(245, 12, 103, 16);
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		panelPassword.add(lblPassword);
		
		passwordField = new JTextField();
		passwordField.setBounds(353, 8, 116, 26);
		passwordField.setColumns(10);
		panelPassword.add(passwordField);
		
		JLabel lblEmployeesTable = new JLabel("Employees Table");
		lblEmployeesTable.setBounds(21, 25, 119, 16);
		contentPanel.add(lblEmployeesTable);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					addEmployee();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSave.setBounds(426, 404, 117, 29);
		contentPanel.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(311, 404, 117, 29);
		contentPanel.add(btnCancel);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(23, 404, 117, 29);
		contentPanel.add(btnDelete);
		
		JPanel panelEmployeeInfo = new JPanel();
		panelEmployeeInfo.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Employee Info", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(128, 128, 128)));
		panelEmployeeInfo.setBounds(23, 161, 520, 170);
		contentPanel.add(panelEmployeeInfo);
		panelEmployeeInfo.setLayout(null);
		
		nameField = new JTextField();
		nameField.setColumns(10);
		nameField.setBounds(81, 27, 152, 26);
		panelEmployeeInfo.add(nameField);
		
		telField = new JTextField();
		telField.setColumns(10);
		telField.setBounds(81, 60, 152, 26);
		panelEmployeeInfo.add(telField);
		
		JLabel lblTel = new JLabel("Tel:");
		lblTel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTel.setBounds(39, 63, 40, 16);
		panelEmployeeInfo.add(lblTel);
		
		idField = new JTextField();
		idField.setColumns(10);
		idField.setBounds(81, 93, 152, 26);
		panelEmployeeInfo.add(idField);
		
		JLabel lblId = new JLabel("Id:");
		lblId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblId.setBounds(39, 98, 40, 16);
		panelEmployeeInfo.add(lblId);
		
		empIdField = new JTextField();
		empIdField.setBackground(new Color(220, 220, 220));
		empIdField.setEditable(false);
		empIdField.setColumns(10);
		empIdField.setBounds(333, 27, 152, 26);
		panelEmployeeInfo.add(empIdField);
		
		JLabel lblEmpId = new JLabel("Emp. Id:");
		lblEmpId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEmpId.setBounds(267, 32, 65, 16);
		panelEmployeeInfo.add(lblEmpId);
		
		typeCombo = new JComboBox();
		typeCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedType = typeCombo.getSelectedItem().toString();
				
				if(selectedType.equals("seller") ) {
		    			panelPassword.setVisible(false);
			    }
			    else {
			    		panelPassword.setVisible(true);
			    }
			}
		});
		typeCombo.setModel(new DefaultComboBoxModel(new String[] {"", "cashier", "manager", "seller"}));
		typeCombo.setBounds(333, 93, 152, 27);
		panelEmployeeInfo.add(typeCombo);
		
		JLabel lblStore = new JLabel("Store:");
		lblStore.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStore.setBounds(283, 63, 49, 16);
		panelEmployeeInfo.add(lblStore);
		
		JLabel lblType = new JLabel("Type:");
		lblType.setHorizontalAlignment(SwingConstants.RIGHT);
		lblType.setBounds(285, 98, 47, 16);
		panelEmployeeInfo.add(lblType);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(18, 32, 61, 16);
		panelEmployeeInfo.add(lblName);
		
		bankField = new JTextField();
		bankField.setColumns(10);
		bankField.setBounds(81, 126, 152, 26);
		panelEmployeeInfo.add(bankField);
		
		JLabel lblBank = new JLabel("Bank:");
		lblBank.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBank.setBounds(39, 131, 40, 16);
		panelEmployeeInfo.add(lblBank);
		
		storeCombo = new JComboBox();
		storeCombo.setModel(new DefaultComboBoxModel(new String[] {"", "1", "2"}));
		storeCombo.setBounds(333, 60, 152, 27);
		panelEmployeeInfo.add(storeCombo);
		
		FillTableData();
		try {
			getAllEmployeesData();
			updateTable(allEmployeesList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// METHODS ----------------------------------------------------------
	private void FillTableData() {

		defaultTableModel.addColumn("Emp Id");
		defaultTableModel.addColumn("Name");
		defaultTableModel.addColumn("Tel");
		defaultTableModel.addColumn("Id");
		defaultTableModel.addColumn("Bank");
		defaultTableModel.addColumn("Type");
		defaultTableModel.addColumn("Store");
		
		
		employeeTable.setModel(defaultTableModel);
		
	}
	
	/**
	 * Update Employees table
	 * @param data
	 */
	private void updateTable(JSONObject data) {
		
		for (Object key : data.keySet()) {
			String keyStr = (String) key;
			
	        JSONObject product = (JSONObject) data.get(keyStr);

	        String empId = (String) product.get("empId");
	        String name = (String) product.get("name");
	        String tel = (String) product.get("tel");
	        String id = (String) product.get("id");
	        String bank = (String) product.get("bank");
	        String empType = (String) product.get("empType");
	        String store = (String) product.get("storeId");
	        
        		defaultTableModel.addRow(new Object[] { empId, name, tel, id, bank, empType, store});
	        
		}
	}
	
	/**
	 * Get all employees data from the server
	 * @throws Exception 
	 */
	private void getAllEmployeesData() throws Exception {
		JSONObject reqObj = new JSONObject();
		String resString;
		reqObj.put("getAllEmployeesData", "null");
		
		serverRequest.println(reqObj.toJSONString());
		resString = serverResponse.readLine();
		if( resString != null && resString.length() > 0 ) {
			allEmployeesList = (JSONObject) jsonparser.parse(resString);
		}
		else {
			throw new Exception("Employees Data empty");
		}
	}
	
	/**
	 * Set employee info on table row select
	 * @param row
	 */
	private void setEmployeeInputs(int row) {
		TableModel tableModel = employeeTable.getModel();
		
	 	String empId = tableModel.getValueAt(row, 0).toString();
        String name = tableModel.getValueAt(row, 1).toString();
        String tel = tableModel.getValueAt(row, 2).toString();
        String id = tableModel.getValueAt(row, 3).toString();
        String bank = tableModel.getValueAt(row, 4).toString();
        String empType = tableModel.getValueAt(row, 5).toString();
        String store = tableModel.getValueAt(row, 6).toString();
        
        nameField.setText(name);
        telField.setText(tel);
        idField.setText(id);
        empIdField.setText(empId);
        bankField.setText(bank);
        userNameField.setText(id);

	    typeCombo.setSelectedItem(empType);
	    storeCombo.setSelectedItem(store);
	    
	    if(empType.equals("seller") ) {
	    		panelPassword.setVisible(false);
	    }
	    else {
	    		panelPassword.setVisible(true);
	    }
        
	}
	
	/**
	 * Add new employee to the DB
	 * @throws IOException 
	 */
	private void addEmployee() throws IOException {
		JSONObject empObj = new JSONObject();
		JSONObject reqObj = new JSONObject();
		String serverResp;
		
		String empId;
		
		if( empIdField.getText() == null  || empIdField.getText().length() == 0) {
			empId = "0";
		}
		else {
			empId = empIdField.getText();
		}
		
		
		empObj.put("name", nameField.getText());
		empObj.put("tel", telField.getText());
		empObj.put("id", idField.getText());
		empObj.put("empId", empId);
		empObj.put("bank", bankField.getText());
		empObj.put("empType", typeCombo.getSelectedItem().toString());
		empObj.put("storeId", storeCombo.getSelectedItem().toString());
		empObj.put("password", passwordField.getText());
		
		reqObj.put("addEmployee", empObj);
	    
		serverRequest.println(reqObj.toJSONString());
		serverResp = serverResponse.readLine();
		
		//Update screen view
		if( serverResp != null && serverResp.length() > 0) {
			clearFields();
			try {
				DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
				model.setRowCount(0);
				
				getAllEmployeesData();
				updateTable(allEmployeesList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "New Employee added succesfully");
		}
		else {
			JOptionPane.showMessageDialog(null, "Something went wrong, please try again later.");
		}
	}
	
	/**
	 * Clear input fields
	 */
	private void clearFields() {
		nameField.setText(null);
        telField.setText(null);
        idField.setText(null);
        empIdField.setText(null);
        bankField.setText(null);
        userNameField.setText(null);
        passwordField.setText(null);
        
	    typeCombo.setSelectedIndex(0);
	    storeCombo.setSelectedIndex(0);
	}
	
	
	
}
