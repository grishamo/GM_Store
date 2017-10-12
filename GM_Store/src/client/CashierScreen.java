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

public class CashierScreen {

	JFrame frame;
	private JTable table;
	private JTextField textField;
	private JTextField textField_1;
	private JSONObject allCustomers;
	private JSONObject allProducts;
	private JSONObject employeeData;
	private JSONParser jsonparser = new JSONParser();

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

	/**
	 * Create the application.
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public CashierScreen(JSONObject employeeData, DataInputStream serverResponse, PrintStream serverRequest) throws IOException, ParseException {
		
		JSONObject reqObject = new JSONObject();
		String resString;
		
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
		frame.setBounds(100, 100, 745, 563);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblStore = new JLabel("Store:");
		lblStore.setBounds(340, 0, 73, 26);
		frame.getContentPane().add(lblStore);
		
		
		JLabel lblCashierName = new JLabel("Cashier Name:");
		lblCashierName.setBounds(13, 0, 137, 26);
		frame.getContentPane().add(lblCashierName);
		
		JLabel lblCustomerList = new JLabel("Customer List:");
		lblCustomerList.setBounds(13, 69, 161, 26);
		lblCustomerList.setVerticalAlignment(SwingConstants.BOTTOM);
		frame.getContentPane().add(lblCustomerList);
		
		JComboBox productsList = new JComboBox();
		productsList.setBounds(219, 104, 161, 26);
		frame.getContentPane().add(productsList);
		
		JLabel lblProducts = new JLabel("Products:");
		lblProducts.setBounds(223, 74, 160, 26);
		
		for (Object key : allProducts.keySet()) {
	        String keyStr = (String) key;
	        JSONObject products = (JSONObject) allProducts.get(keyStr);
	        productsList.addItem(products.get("name") + " - " + products.get("quantity") + " - " + products.get("cost") + '$');
		}
		frame.getContentPane().add(lblProducts);
		
		
		
		
		Button button = new Button("Add to cart");
		button.setBounds(282, 193, 99, 19);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		frame.getContentPane().add(button);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(219, 162, 97, 24);
		comboBox_1.setMaximumRowCount(10);
		for(int i = 1; i < 11; i++) {
			comboBox_1.addItem(i);
		}
		frame.getContentPane().add(comboBox_1);
		
		JLabel lblAmount = new JLabel("Amount:");
		lblAmount.setBounds(223, 132, 92, 26);
		frame.getContentPane().add(lblAmount);
		
		JLabel lblShopingCart = new JLabel("Shoping cart:");
		lblShopingCart.setBounds(429, 75, 161, 30);
		frame.getContentPane().add(lblShopingCart);
		
		JComboBox customerList = new JComboBox();
		customerList.setBounds(11, 104, 161, 26);
		frame.getContentPane().add(customerList);
		
		for (Object key : allCustomers.keySet()) {
	        String keyStr = (String) key;
	        JSONObject customer = (JSONObject) allCustomers.get(keyStr);
	        customerList.addItem(customer.get("name") + " - " + customer.get("id") + " - " + customer.get("status"));
		}
		
		
		Button customerLst = new Button("New customer");
		customerLst.setBackground(Color.WHITE);
		customerLst.addMouseListener(new MouseAdapter() {
			@Override//
			//
			//
			//
			//
			//
			//
			//
			//
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		customerLst.setBounds(72, 144, 99, 19);
		frame.getContentPane().add(customerLst);
		
		JLabel cashierName = new JLabel((String)employeeData.get("name"));
		cashierName.setBounds(111, 0, 142, 26);
		frame.getContentPane().add(cashierName);
		
		JLabel storeId = new JLabel((String)employeeData.get("storeId"));
		storeId.setBounds(388, 0, 161, 26);
		frame.getContentPane().add(storeId);
		
		table = new JTable();
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
			},
			new String[] {
				"Product", "Amount", "Price"
			}
		));
		table.setBounds(429, 108, 269, 160);
		frame.getContentPane().add(table);
		
		JLabel lblNewLabel_2 = new JLabel("Total price:");
		lblNewLabel_2.setBounds(439, 289, 92, 26);
		frame.getContentPane().add(lblNewLabel_2);
		
		textField = new JTextField();
		textField.setBounds(538, 289, 99, 26);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblEmployeeId = new JLabel("Employee ID:");
		lblEmployeeId.setBounds(439, 336, 92, 26);
		frame.getContentPane().add(lblEmployeeId);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(538, 336, 142, 26);
		frame.getContentPane().add(textField_1);
		
		JButton btnBuy = new JButton("BUY!");
		btnBuy.setBounds(561, 393, 137, 51);
		frame.getContentPane().add(btnBuy);
		
		JLabel lblNewLabel_3 = new JLabel("$");
		lblNewLabel_3.setBounds(643, 289, 41, 26);
		frame.getContentPane().add(lblNewLabel_3);
	}
}