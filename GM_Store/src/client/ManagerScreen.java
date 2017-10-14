package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ManagerScreen {

	JFrame frame;
	private DefaultTableModel defaultTableModel = new DefaultTableModel();
	private JTable mainTable;
	private JSONObject employeeData;
	private DataInputStream serverResponse;
	private PrintStream serverRequest;
	private JSONObject allSalesList = new JSONObject();
	private JSONParser jsonparser = new JSONParser();
	private JComboBox productSelect = new JComboBox();
	private JComboBox employeeSelect = new JComboBox();
	private JComboBox storeSelect = new JComboBox();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagerScreen window = new ManagerScreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public ManagerScreen(JSONObject employeeData, DataInputStream serverResponse, PrintStream serverRequest) {
		this.employeeData = employeeData;
		this.serverResponse = serverResponse;
		this.serverRequest = serverRequest;
		
		try {
			getSalesList();
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		initialize();
	}
	
	public ManagerScreen() {
		initialize();
	}
	
	private void getSalesList() throws IOException, ParseException {
		JSONObject reqObj = new JSONObject();
		String resString;
		reqObj.put("getAllSalesList", "null");
		
		serverRequest.println(reqObj.toJSONString());
		resString = serverResponse.readLine();
		if( resString != null || resString.length() > 0 ) {
			allSalesList = (JSONObject) jsonparser.parse(resString);
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 700, 563);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// HEADER -------------------------------------------------------------------
		JLabel headerText = new JLabel("SALES REPORTS");
		headerText.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		headerText.setBounds(25, 16, 137, 26);
		frame.getContentPane().add(headerText);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(25, 49, 665, 12);
		frame.getContentPane().add(separator);
		
		// ---------------------------------------------------------
		
		//EMPLOYEE COMBO BOX
		employeeSelect.setBounds(241, 106, 97, 24);
		employeeSelect.setMaximumRowCount(10);
		employeeSelect.addItem(null);
		
		for(Object key : allSalesList.keySet()) {
			String keyStr = (String) key;
			JSONObject products = (JSONObject) allSalesList.get(keyStr);
			String employee = (String)products.get("empId");
			if( employee.length() > 0 && !isInCombo(employeeSelect, employee) ) {
				employeeSelect.addItem(products.get("empId"));
			}
		}
		
		frame.getContentPane().add(employeeSelect);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 146, 656, 296);
		frame.getContentPane().add(scrollPane);
		
		mainTable = new JTable();
		mainTable.setBackground(new Color(240, 248, 255));
		mainTable.setBorder(new EmptyBorder(2, 2, 2, 2));
		scrollPane.setViewportView(mainTable);
		
		JButton SubmitBtn = new JButton("Save to file");
		SubmitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currEmployee = (String) employeeSelect.getSelectedItem();
				String currProduct = (String) productSelect.getSelectedItem();
				String currStore = (String) storeSelect.getSelectedItem();
				
				String fileName = currStore + '_' + currProduct + '_' + currStore + ".txt";
				try {
					saveReport(fileName);
					clearData();
					JOptionPane.showMessageDialog(null, "Report Saved Succesfully!.");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		SubmitBtn.setBounds(564, 467, 117, 38);
		frame.getContentPane().add(SubmitBtn);
		
		JButton clearBtn = new JButton("Clear");
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearData();
			}
		});
		clearBtn.setBounds(442, 467, 117, 38);
		frame.getContentPane().add(clearBtn);
		
		//PRODUCT COMBO BOX
		productSelect.setMaximumRowCount(10);
		productSelect.setBounds(132, 106, 97, 24);
		productSelect.addItem(null);
		for(Object key : allSalesList.keySet()) {
			String keyStr = (String) key;
			JSONObject products = (JSONObject) allSalesList.get(keyStr);
			
			if(!isInCombo(productSelect, (String)products.get("product"))) {
				productSelect.addItem(products.get("product"));
			}
		}
		frame.getContentPane().add(productSelect);
		
		// STORE COMBO BOX
		storeSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object currStore  = storeSelect.getSelectedItem();
				
				updateProductSelect(currStore);
				updateEmployeeSelect(currStore);
				
			}
		});
		storeSelect.setMaximumRowCount(10);
		storeSelect.setBounds(23, 106, 97, 24);
		storeSelect.addItem(null);
		for (Object key : allSalesList.keySet()) {
	        String keyStr = (String) key;
	        JSONObject products = (JSONObject) allSalesList.get(keyStr);
	        
	        if( !isInCombo(storeSelect, (String)products.get("storeId") ) ) {
	        		storeSelect.addItem(products.get("storeId"));
	        }
		}
		frame.getContentPane().add(storeSelect);
		
		JButton btnAdminScreen = new JButton("Admin Screen");
		btnAdminScreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminDialog dialog = new AdminDialog(serverResponse, serverRequest);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		btnAdminScreen.setBounds(25, 467, 117, 38);
		frame.getContentPane().add(btnAdminScreen);
		
		JButton btnUpdateVipDiscount = new JButton("Update VIP Discount");
		btnUpdateVipDiscount.setBounds(154, 467, 156, 38);
		frame.getContentPane().add(btnUpdateVipDiscount);
		
		JLabel lblStore = new JLabel("Store");
		lblStore.setBounds(34, 90, 61, 16);
		frame.getContentPane().add(lblStore);
		
		JLabel lblProduct = new JLabel("Product");
		lblProduct.setBounds(140, 90, 61, 16);
		frame.getContentPane().add(lblProduct);
		
		JLabel lblEmployee = new JLabel("Employee");
		lblEmployee.setBounds(249, 90, 61, 16);
		frame.getContentPane().add(lblEmployee);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currEmployee = (String) employeeSelect.getSelectedItem();
				String currProduct = (String) productSelect.getSelectedItem();
				String currStore = (String) storeSelect.getSelectedItem();
				
				updateTable(allSalesList, currEmployee, currProduct, currStore);
				
			}
		});
		
		btnSubmit.setBounds(350, 104, 117, 29);
		frame.getContentPane().add(btnSubmit);
		
		JLabel lblManagerName = new JLabel("Manager Name:");
		lblManagerName.setBounds(451, 16, 105, 26);
		frame.getContentPane().add(lblManagerName);
		
		
		JLabel lblName = new JLabel("name");
		lblName.setBounds(555, 16, 105, 26);
		lblName.setText((String)employeeData.get("name"));
		frame.getContentPane().add(lblName);
		
		FillTableData();
	}
	
	/**
	 * Update Products combo box each time user change the store id
	 * @param storeId
	 */
	public void updateProductSelect(Object storeId) {
		productSelect.removeAllItems();
		productSelect.addItem(null); 
		for(Object key : allSalesList.keySet()) {
			String keyStr = (String) key;
			JSONObject products = (JSONObject) allSalesList.get(keyStr);
			String currProduct = (String)products.get("product");
			String currStore = (String)products.get("storeId");
			
			if (storeId == null) {
				productSelect.addItem(products.get("product"));
			}
			else if(!isInCombo(productSelect, currProduct) && currStore.equals(storeId.toString())) {
				productSelect.addItem(products.get("product"));
			}
		}
	}
	
	/**
	 * Update Employee combo box each time user change the store id
	 * @param storeId
	 */
	public void updateEmployeeSelect(Object storeId) {
		employeeSelect.removeAllItems();
		employeeSelect.addItem(null); 
		
		for(Object key : allSalesList.keySet()) {
			String keyStr = (String) key;
			JSONObject products = (JSONObject) allSalesList.get(keyStr);
			String currEmployee = (String)products.get("empId");
			String currStore = (String)products.get("storeId");
			
			
			if( currEmployee.length() > 0 && !isInCombo(employeeSelect, currEmployee) ) {
				if( storeId == null ) {
					employeeSelect.addItem(products.get("empId"));
				}
				else if( currStore.equals(storeId.toString()) ) {
					employeeSelect.addItem(products.get("empId"));
				}
			}
			
		}
	}
	
	/**
	 * Check if item exist in the comboBox
	 * @param storeSelect
	 * @param item
	 * @return
	 */
	private boolean isInCombo(JComboBox storeSelect, String item) {
		
		ComboBoxModel model = storeSelect.getModel();
		int size = model.getSize();
		
		for(int i = 0; i < size; ++i) {
			Object elemObj = model.getElementAt(i);
			if(elemObj != null ) {
				String element = (String) model.getElementAt(i);
		         if( element.equals(item) ) {
		        	 	return true;
		         }
			}
	     }
		
		return false;
	}
	
	/**
	 * Update table data - the grate spaghetti !!!
	 * @param data
	 * @param currEmployee
	 * @param currProduct
	 * @param currStore
	 */
	public void updateTable(JSONObject data, String currEmployee, String currProduct, String currStore) {
		DefaultTableModel model = (DefaultTableModel) mainTable.getModel();
		model.setRowCount(0);
		
		for (Object key : data.keySet()) {
			String keyStr = (String) key;
			
	        JSONObject product = (JSONObject) data.get(keyStr);
	        String date = keyStr;
	        String prodName = (String) product.get("product");
	        String storeId = (String) product.get("storeId");
	        String empId = (String) product.get("empId");
	        String cost = (String) product.get("sumOfSale");
	        
	        // Sorry for the spaghetti !!
	        if( currStore == null && currProduct == null && currEmployee == null) {
	        		defaultTableModel.addRow(new Object[] { keyStr, prodName, storeId, empId, cost});
	        }
	        else if( currStore == null && currProduct == null && currEmployee != null ) {
	        		if( currEmployee.equals(empId)) {
	        			defaultTableModel.addRow(new Object[] { keyStr, prodName, storeId, empId, cost});
	        		}
	        }
	        else if ( currStore == null && currProduct != null && currEmployee == null ) {
		        	if( currProduct.equals(prodName)) {
	        			defaultTableModel.addRow(new Object[] { keyStr, prodName, storeId, empId, cost});
	        		}
	        }
	        else if ( currStore == null && currProduct != null && currEmployee != null ) {
		        	if( currProduct.equals(prodName) && currEmployee.equals(empId)) {
	        			defaultTableModel.addRow(new Object[] { keyStr, prodName, storeId, empId, cost});
	        		}
	        }
	        else if ( currStore != null && currProduct == null && currEmployee == null ) {
		        	if( currStore.equals(storeId) ) {
	        			defaultTableModel.addRow(new Object[] { keyStr, prodName, storeId, empId, cost});
	        		}
	        }
	        else if ( currStore != null && currProduct == null && currEmployee != null ) {
		        	if( currStore.equals(storeId) && currEmployee.equals(empId)) {
	        			defaultTableModel.addRow(new Object[] { keyStr, prodName, storeId, empId, cost});
	        		}
	        }
	        else if ( currStore != null && currProduct != null && currEmployee == null ) {
		        	if( currStore.equals(storeId) && currProduct.equals(prodName)) {
	        			defaultTableModel.addRow(new Object[] { keyStr, prodName, storeId, empId, cost});
	        		}
	        }
	        else {
		        	if( currStore.equals(storeId) && currProduct.equals(prodName) && currEmployee.equals(empId)) {
	        			defaultTableModel.addRow(new Object[] { keyStr, prodName, storeId, empId, cost});
	        		}
	        }
	       
		}
	}
	
	/**
	 * Fill Table With Column
	 */
	public void FillTableData() {

		defaultTableModel.addColumn("Date");
		defaultTableModel.addColumn("Product");
		defaultTableModel.addColumn("Store");
		defaultTableModel.addColumn("Employee");
		defaultTableModel.addColumn("Price");
		
		mainTable.setModel(defaultTableModel);
		
	}
	
	/**
	 * Clear Product List + total price
	 */
	private void clearData() {
		DefaultTableModel model = (DefaultTableModel) mainTable.getModel();
		model.setRowCount(0);
	}
	
	/**
	 * Save current table view to the file
	 * @param fileName
	 * @throws IOException
	 */
	private void saveReport(String fileName) throws IOException {
		
		PrintWriter cleaner = new PrintWriter(fileName);
		cleaner.print("");
		cleaner.close();
		
		FileWriter fw = new FileWriter(fileName, true);
	    BufferedWriter bw = new BufferedWriter(fw);
	    PrintWriter out = new PrintWriter(bw);
	 
		String line;
		
		// Table headers
		line = String.format("%-30s %-15s %-15s %-15s %-15s", "Date", "Product", "Store", "Employee", "Price");
		out.println(line);
		
		// Separator
		for(int i=0; i < 100; i++) out.print("-");
		out.println("");
		
	    int rows = defaultTableModel.getRowCount();
	    
	    for(int i = 0; i < rows; i++) {
			String Date = (String) mainTable.getValueAt(i, 0);
			String Product = String.valueOf(mainTable.getValueAt(i, 1));
			String Store = String.valueOf(mainTable.getValueAt(i, 2));
			String Employee = String.valueOf(mainTable.getValueAt(i, 3));
			String Price = String.valueOf(mainTable.getValueAt(i, 4));
			
			line = String.format("%-30s %-15s %-15s %-15s %-15s", Date, Product, Store, Employee, Price);
			out.println(line);
	    }
	    
	    
	    out.close();
	}
}
