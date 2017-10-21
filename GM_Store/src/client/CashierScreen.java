package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JDialog;
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
	private JSONObject allCustomers;
	private JSONObject allProducts;
	private JSONObject allSellers;
	private JSONObject currentCustomer = new JSONObject();
	private JSONObject discountInfo;
	private JSONObject employeeData;
	private JSONParser jsonparser = new JSONParser();
	private JTable productsTable;
	private DefaultTableModel defaultTableModel = new DefaultTableModel();
	private JComboBox quantitySelect = new JComboBox();
	private JComboBox productsList = new JComboBox();
	private JComboBox sellerComboBox = new JComboBox();
	private JComboBox customerList = new JComboBox();
	double totalPrice = 0;
	double discountPrice = 0;
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
	 * @throws Exception 
	 */
	public CashierScreen(JSONObject employeeData, DataInputStream serverResponse, PrintStream serverRequest) throws Exception {
		
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
		
		// Get All Sellers by Store Id
		reqObject.put("getSellersByStore", (String) employeeData.get("storeId"));
		serverRequest.println(reqObject.toJSONString());
		
		//Server response for getSellersByStore
		resString = serverResponse.readLine();
		allSellers = (JSONObject) jsonparser.parse(resString);
		
		// Server request for Products
		getAllProducts();
		getVipDiscountInfo();
		
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
		frame.setResizable(false);
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
		
		productsList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object selectedProduct  = productsList.getSelectedItem();
				if( selectedProduct != null) {
					updateAmount(selectedProduct.toString());
				}
			}
		});
		productsList.setBounds(306, 108, 161, 26);
		frame.getContentPane().add(productsList);
		
		JLabel lblProducts = new JLabel("Products:");
		lblProducts.setBounds(312, 78, 160, 26);
		productsList.addItem(null);
		
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
		customerList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectCustomerAction();
			}
		});
		
		customerList.setBounds(17, 108, 161, 26);
		frame.getContentPane().add(customerList);
		
		customerList.addItem("");
		for (Object key : allCustomers.keySet()) {
	        String keyStr = (String) key;
	        JSONObject customer = (JSONObject) allCustomers.get(keyStr);
	        customerList.addItem(String.format("%-15s %-15s %s", customer.get("name"), customer.get("id"), customer.get("status") ));
		}
		
		JLabel cashierName = new JLabel((String) employeeData.get("name"));
		cashierName.setBounds(121, 16, 142, 26);
		frame.getContentPane().add(cashierName);
		
		JLabel storeId = new JLabel((String) employeeData.get("storeId"));
		storeId.setBounds(388, 16, 161, 26);
		frame.getContentPane().add(storeId);
		
		JLabel lblNewLabel_2 = new JLabel("Total price:");
		lblNewLabel_2.setBounds(243, 477, 92, 26);
		frame.getContentPane().add(lblNewLabel_2);
		
		totalProdPrice = new JTextField();
		totalProdPrice.setEditable(false);
		totalProdPrice.setBounds(318, 477, 73, 26);
		frame.getContentPane().add(totalProdPrice);
		totalProdPrice.setColumns(10);
		
		JLabel lblEmployeeId = new JLabel("Seller ID:");
		lblEmployeeId.setBounds(25, 477, 92, 26);
		frame.getContentPane().add(lblEmployeeId);
		
		JButton btnBuy = new JButton("BUY!");
		btnBuy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					purchaseAction();
					
					JOptionPane.showMessageDialog(null, "Thank You!\nDeal complete.");
					clearData();
					
				} catch (IOException | ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//updateEmployeeSale
			}
		});
		
		
		btnBuy.setBounds(565, 471, 117, 38);
		frame.getContentPane().add(btnBuy);
		
		JLabel dollarSign = new JLabel("$");
		dollarSign.setBounds(392, 477, 41, 26);
		frame.getContentPane().add(dollarSign);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(25, 49, 665, 12);
		frame.getContentPane().add(separator);
		
		JButton btnNewCustomer = new JButton("New Customer");
		btnNewCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewCustomerDialog dialog = new NewCustomerDialog(serverRequest, serverResponse);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		btnNewCustomer.setBounds(177, 107, 117, 29);
		frame.getContentPane().add(btnNewCustomer);
		
		JButton addProductButton = new JButton("Add to List");
		addProductButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String prodName = (String) productsList.getSelectedItem();
				Object prodQobj = quantitySelect.getSelectedItem();
				
				if( prodName == null || prodQobj == null ) return;
				
				int prodQ =  (int) quantitySelect.getSelectedItem();
				double prodPrice = Double.parseDouble(getProductPrice(productsList.getSelectedItem().toString())) * prodQ;
				totalPrice += prodPrice;
				
				updateTotalPrice();
				defaultTableModel.addRow(new Object[] { prodName, prodQ, prodPrice });
			}
		});
		addProductButton .setBounds(569, 107, 117, 29);
		frame.getContentPane().add(addProductButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 146, 656, 296);
		frame.getContentPane().add(scrollPane);
		
		productsTable = new JTable();
		productsTable.setBackground(new Color(240, 248, 255));
		productsTable.setBorder(new EmptyBorder(2, 2, 2, 2));
		scrollPane.setViewportView(productsTable);
		
		JButton ClearAllButton = new JButton("Clear");
		ClearAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearData();
			}
		});
		ClearAllButton.setBounds(443, 471, 117, 38);
		frame.getContentPane().add(ClearAllButton);
		
	
		sellerComboBox.setBounds(81, 478, 117, 26);
		sellerComboBox.addItem(null);
		for (Object key : allSellers.keySet()) {
	        String keyStr = (String) key;
	        JSONObject seller = (JSONObject) allSellers.get(keyStr);
	        sellerComboBox.addItem(seller.get("empId"));
		}
		frame.getContentPane().add(sellerComboBox);
		
		FillTableData();
	}
	
	/**
	 * Clear Product List + total price
	 */
	private void clearData() {
		totalPrice = 0;
		discountPrice = 0;
		totalProdPrice.setText(null);
		DefaultTableModel model = (DefaultTableModel) productsTable.getModel();
		model.setRowCount(0);
		sellerComboBox.setSelectedIndex(0);
		productsList.setSelectedIndex(0);
		quantitySelect.removeAllItems();
		customerList.setSelectedIndex(0);
		
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
	 * Purchase Action
	 * - Update product list in the server
	 * - Update seller information
	 * - Add sale to report list
	 * @throws IOException
	 * @throws ParseException 
	 */
	private void purchaseAction() throws IOException, ParseException {
		//iterate product table and update each product in the server
		int rows = defaultTableModel.getRowCount();
		String prodName;
		String quantity;
		String prodCost;
		String responseStr;
		String empId;
		JSONObject productObj = new JSONObject();
		JSONObject reqObj = new JSONObject();
		
		for(int i = 0; i < rows; i++) {
			prodName = (String) productsTable.getValueAt(i, 0);
			quantity = String.valueOf(productsTable.getValueAt(i, 1));
			prodCost = String.valueOf(productsTable.getValueAt(i, 2));
			
			Object emIdObj = sellerComboBox.getSelectedItem();
			if(emIdObj == null) {
				empId = "";
			}
			else {				
				empId = (String)sellerComboBox.getSelectedItem();
			}
			
			productObj.put("empId", empId);
			productObj.put("prodCost", prodCost);
			productObj.put("name", prodName);
			productObj.put("quantity", quantity);
			productObj.put("storeId", (String) employeeData.get("storeId"));
			productObj.put("customer", currentCustomer);
			
			reqObj.put("purchaseAction", productObj);
			
			serverRequest.println(reqObj.toJSONString() + '\n');
			responseStr = serverResponse.readLine();
			
			getAllProducts();
//			updateProductsList();
		}
	}
	
	/**
	 * Get All Products from the server;
	 * @throws ParseException
	 * @throws IOException
	 */
	public void getAllProducts() throws ParseException, IOException {
		JSONObject reqObject = new JSONObject();
		reqObject = new JSONObject();
		reqObject.put("getProductsByStore", (String) employeeData.get("storeId"));	
		serverRequest.println(reqObject.toJSONString());

		//Server response for all Products
		String resString = serverResponse.readLine();
		allProducts = (JSONObject) jsonparser.parse(resString);
	}
	
	/**
	 * Get VIP discount 
	 * @throws Exception
	 */
	private void getVipDiscountInfo() throws Exception {
		JSONObject reqObj = new JSONObject();
		String resString;
		reqObj.put("getVipDiscountInfo", "null");
		
		serverRequest.println(reqObj.toJSONString());
		resString = serverResponse.readLine();
		if( resString != null && resString.length() > 0 ) {
			discountInfo = (JSONObject) jsonparser.parse(resString);
		}
		else {
			throw new Exception("Customers Data empty");
		}
	}
	
	/**
	 * Update total price
	 */
	private void updateTotalPrice() {
		double discount = Double.parseDouble((String)discountInfo.get("discount"));
		String currCustomerStatus = (String)currentCustomer.get("status");
		
		if( currCustomerStatus != null && currCustomerStatus.equals("vip") && discount != 0 ) {
			discountPrice = totalPrice - (totalPrice / discount);
			totalProdPrice.setText(String.valueOf(discountPrice));
		}
		else {
			totalProdPrice.setText(String.valueOf(totalPrice));
		}
		
		
	}
	
	/**
	 * Update total price by customer status and update current customer
	 */
	private void selectCustomerAction() {
		String customer = (String) customerList.getSelectedItem();
		customer = customer.replaceAll("\\s+"," ");
		String[] customerArray = customer.split(" ");
		int length = customerArray.length-1;
		
		if(length >= 2) {
			currentCustomer.put("name", customerArray[0]);
			currentCustomer.put("id", customerArray[1]);
			currentCustomer.put("status", customerArray[2]);
		}
		
		if(!customer.equals("") && totalPrice > 0) {
			updateTotalPrice();
		}
	}
	
	
	/**
	 * Block Client.java - save socket connection
	 * @return
	 */
	public boolean cont() {
		return releaseBlock;
	}
}