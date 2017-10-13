package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
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
		if( resString.length() > 0 ) {
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
		
		// HEADER SELECTION ---------------------------------------------------------
		JComboBox employeeSelect = new JComboBox();
		employeeSelect.setBounds(241, 106, 97, 24);
		employeeSelect.setMaximumRowCount(10);
		frame.getContentPane().add(employeeSelect);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 146, 656, 296);
		frame.getContentPane().add(scrollPane);
		
		mainTable = new JTable();
		mainTable.setBackground(new Color(240, 248, 255));
		mainTable.setBorder(new EmptyBorder(2, 2, 2, 2));
		scrollPane.setViewportView(mainTable);
		
		JButton SubmitBtn = new JButton("Save to file");
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
		
		JComboBox productSelect = new JComboBox();
		productSelect.setMaximumRowCount(10);
		productSelect.setBounds(132, 106, 97, 24);
		frame.getContentPane().add(productSelect);
		
		// STORE COMBO BOX
		JComboBox storeSelect = new JComboBox();
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
				updateTable(allSalesList);
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
	
	public void updateTable(JSONObject data) {
		for (Object key : data.keySet()) {
			String keyStr = (String) key;
			
	        JSONObject product = (JSONObject) data.get(keyStr);
	        String date = keyStr;
	        String prodName = (String) product.get("product");
	        String storeId = (String) product.get("storeId");
	        String empId = (String) product.get("empId");
	        String cost = (String) product.get("sumOfSale");
	        
	        defaultTableModel.addRow(new Object[] { keyStr, prodName, storeId, empId, cost});
	      
		}
	}
	
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
}
