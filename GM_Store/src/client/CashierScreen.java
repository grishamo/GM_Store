package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.border.BevelBorder;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import java.awt.SystemColor;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;

public class CashierScreen {

	JFrame frame;
	private JTextField totalProdPrice;
	private JTextField textField_1;
	private JSONObject allCustomers;
	private JSONObject allProducts;
	private JSONObject employeeData;
	private JSONParser jsonparser = new JSONParser();
	private JTable productsTable;
	private DefaultTableModel defaultTableModel = new DefaultTableModel();
	private JComboBox quantitySelect = new JComboBox();
	double totalPrice = 0;
	private DataInputStream serverResponse;
	private PrintStream serverRequest;
	private boolean releaseBlock = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CashierScreen window = new CashierScreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void FillTableData() {

		defaultTableModel.addColumn("Name");
		defaultTableModel.addColumn("Quantity");
		defaultTableModel.addColumn("Price");
		
		
		productsTable.setModel(defaultTableModel);
		
	}
	/**
	 * Create the application.
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public CashierScreen(JSONObject employeeData, DataInputStream serverResponse, PrintStream serverRequest) throws IOException, ParseException {
		
		JSONObject reqObject = new JSONObject();
		String resString;
		this.serverResponse = serverResponse;
		this.serverRequest = serverRequest;
		this.employeeData = employeeData;
		
		// Server request for all Customers
		reqObject.put("getAllCustomers", "null");
		serverRequest.println(reqObject.toJSONString());
		
		//Server response for all Customers
		resString = serverResponse.readLine();
		allCustomers = (JSONObject) jsonparser.parse(resString);
		
		// Server request for Products
		reqObject = new JSONObject();
		reqObject.put("getProductsByStore", (String) employeeData.get("storeId"));	
		serverRequest.println(reqObject.toJSONString());

		//Server response for all Products
		resString = serverResponse.readLine();
		allProducts = (JSONObject) jsonparser.parse(resString);
		
		initialize();
	}
	
	public CashierScreen() {
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 563);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblStore = new JLabel("Store:");
		lblStore.setBounds(340, 16, 73, 26);
		frame.getContentPane().add(lblStore);
		
		
		JLabel lblCashierName = new JLabel("Cashier Name:");
		lblCashierName.setBounds(25, 16, 137, 26);
		frame.getContentPane().add(lblCashierName);
		
		JLabel lblCustomerList = new JLabel("Customer List:");
		lblCustomerList.setBounds(25, 73, 161, 26);
		lblCustomerList.setVerticalAlignment(SwingConstants.BOTTOM);
		frame.getContentPane().add(lblCustomerList);
		
		JComboBox productsList = new JComboBox();
		productsList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateAmount(productsList.getSelectedItem().toString());
			}
		});
		productsList.setBounds(306, 108, 161, 26);
		frame.getContentPane().add(productsList);
		
		JLabel lblProducts = new JLabel("Products:");
		lblProducts.setBounds(312, 78, 160, 26);
		
		for (Object key : allProducts.keySet()) {
	        String keyStr = (String) key;
	        JSONObject products = (JSONObject) allProducts.get(keyStr);
	        productsList.addItem(products.get("name"));
		}
		frame.getContentPane().add(lblProducts);
		
		quantitySelect.setBounds(467, 109, 97, 24);
		quantitySelect.setMaximumRowCount(10);
		frame.getContentPane().add(quantitySelect);
		
		JLabel lblAmount = new JLabel("Amount:");
		lblAmount.setBounds(472, 79, 92, 26);
		frame.getContentPane().add(lblAmount);
		
		JComboBox customerList = new JComboBox();
		customerList.setBounds(17, 108, 161, 26);
		frame.getContentPane().add(customerList);
		
		for (Object key : allCustomers.keySet()) {
	        String keyStr = (String) key;
	        JSONObject customer = (JSONObject) allCustomers.get(keyStr);
	        customerList.addItem(customer.get("name") + " - " + customer.get("id") + " - " + customer.get("status"));
		}
		
		JLabel cashierName = new JLabel((String) employeeData.get("name"));
		cashierName.setBounds(121, 16, 142, 26);
		frame.getContentPane().add(cashierName);
		
		JLabel storeId = new JLabel((String) employeeData.get("storeId"));
		storeId.setBounds(388, 16, 161, 26);
		frame.getContentPane().add(storeId);
		
		JLabel lblNewLabel_2 = new JLabel("Total price:");
		lblNewLabel_2.setBounds(220, 375, 92, 26);
		frame.getContentPane().add(lblNewLabel_2);
		
		totalProdPrice = new JTextField();
		totalProdPrice.setEditable(false);
		totalProdPrice.setBounds(303, 375, 73, 26);
		frame.getContentPane().add(totalProdPrice);
		totalProdPrice.setColumns(10);
		
		JLabel lblEmployeeId = new JLabel("Employee ID:");
		lblEmployeeId.setBounds(25, 375, 92, 26);
		frame.getContentPane().add(lblEmployeeId);
		
		textField_1 = new JTextField();
		textField_1.setBounds(113, 374, 85, 26);
		textField_1.setColumns(10);
		frame.getContentPane().add(textField_1);
		
		JButton btnBuy = new JButton("BUY!");
		btnBuy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO: get all sellers from the server
				try {
					updateProductsInServer();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//updateEmployeeSale
			}
		});
		
		
		btnBuy.setBounds(565, 368, 117, 38);
		frame.getContentPane().add(btnBuy);
		
		JLabel dollarSign = new JLabel("$");
		dollarSign.setBounds(377, 375, 41, 26);
		frame.getContentPane().add(dollarSign);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(25, 49, 665, 12);
		frame.getContentPane().add(separator);
		
		JButton btnNewCustomer = new JButton("New Customer");
		btnNewCustomer.setBounds(177, 107, 117, 29);
		frame.getContentPane().add(btnNewCustomer);
		
		JButton addProductButton = new JButton("Add to List");
		addProductButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String prodName = (String)productsList.getSelectedItem();
				int prodQ =  (int) quantitySelect.getSelectedItem();
				double prodPrice = Double.parseDouble(getProductPrice(productsList.getSelectedItem().toString())) * prodQ;
				totalPrice += prodPrice;
				
				totalProdPrice.setText(String.valueOf(totalPrice));
				defaultTableModel.addRow(new Object[] { prodName, prodQ, prodPrice });
			}
		});
		addProductButton .setBounds(569, 107, 117, 29);
		frame.getContentPane().add(addProductButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 146, 656, 210);
		frame.getContentPane().add(scrollPane);
		
		productsTable = new JTable();
		productsTable.setBackground(new Color(240, 248, 255));
		productsTable.setBorder(new EmptyBorder(2, 2, 2, 2));
		scrollPane.setViewportView(productsTable);
		
		JButton ClearAllButton = new JButton("Clear");
		ClearAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				totalPrice = 0;
				totalProdPrice.setText(null);
				DefaultTableModel model = (DefaultTableModel) productsTable.getModel();
				model.setRowCount(0);
				
			}
		});
		ClearAllButton.setBounds(443, 368, 117, 38);
		frame.getContentPane().add(ClearAllButton);
		
		FillTableData();
	}
	
	/**
	 * Get Product Price by product name from local product list
	 * @param name
	 * @return
	 */
	private String getProductPrice(String name) {
		
		for (Object key : allProducts.keySet()) {
			String keyStr = (String) key;
	        JSONObject product = (JSONObject) allProducts.get(keyStr);
	        String prodName = (String) product.get("name");
	        if( name.equals(prodName)) {
	        		return (String) product.get("cost");
	        }
		}
		
		return "";
	}
	
	/**
	 * Update product quantity select field when selecting another product
	 * @param prodName
	 */
	private void updateAmount(String prodName) {
		quantitySelect.removeAllItems();
		int prodQuantity = getProductQuantity(prodName);
		for(int i = 1; i <= prodQuantity; i++) {
			quantitySelect.addItem(i);
		}
	}
	
	/**
	 * Get Product Quantity by product name from local products list
	 * @param name
	 * @return
	 */
	private int getProductQuantity(String name) {
		for (Object key : allProducts.keySet()) {
			String keyStr = (String) key;
	        JSONObject product = (JSONObject) allProducts.get(keyStr);
	        String prodName = (String) product.get("name");
	        if( name.equals(prodName)) {
	        		return Integer.parseInt((String)product.get("quantity"));
	        }
		}
		
		return 0;
	}
	
	/**
	 * Update product list in the server
	 * @throws IOException
	 */
	private void updateProductsInServer() throws IOException {
		//iterate product table and update each product in the server
		int rows = defaultTableModel.getRowCount();
		String prodName;
		String quantity;
		String responseStr;
		JSONObject productObj = new JSONObject();
		JSONObject reqObj = new JSONObject();
		
		for(int i = 0; i < rows; i++) {
			prodName = (String) productsTable.getValueAt(i, 0);
			quantity = String.valueOf(productsTable.getValueAt(i, 1));
			
			productObj.put("name", prodName);
			productObj.put("quantity", quantity);
			productObj.put("storeId", (String) employeeData.get("storeId"));
			
			reqObj.put("updateProductQuantity", productObj);
			
			serverRequest.println(reqObj.toJSONString());
			responseStr = serverResponse.readLine();
			
		}
	}
	public boolean cont() {
		return releaseBlock;
	}
}