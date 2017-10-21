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
import java.awt.Font;

public class VipCustomersDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable customersTable;
	private DefaultTableModel defaultTableModel = new DefaultTableModel();
	private DataInputStream serverResponse;
	private PrintStream serverRequest;
	private JSONObject allCustomersList;
	private JSONObject discountInfo;
	private JSONParser jsonparser = new JSONParser();
	private JTextField discountField = new JTextField();
	private JTextField purchasesField = new JTextField();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			VipCustomersDialog dialog = new VipCustomersDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public VipCustomersDialog( DataInputStream serverResponse, PrintStream serverRequest ) {
		this.serverResponse = serverResponse;
		this.serverRequest = serverRequest;
		
		
		initialize();
	}
	
	public VipCustomersDialog() {
		setResizable(false);
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
		scrollPane.setBounds(23, 49, 520, 151);
		contentPanel.add(scrollPane);
		
		customersTable = new JTable();
		customersTable.setBackground(new Color(240, 248, 255));
		customersTable.setBorder(new EmptyBorder(2, 2, 2, 2));
		scrollPane.setViewportView(customersTable);
		
		JLabel lblEmployeesTable = new JLabel("Vip List");
		lblEmployeesTable.setBounds(21, 25, 119, 16);
		contentPanel.add(lblEmployeesTable);
		
		JButton btnSave = new JButton("Update");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					updateVipDiscount();
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
		
		JPanel panelEmployeeInfo = new JPanel();
		panelEmployeeInfo.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Vip Discount Info", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(128, 128, 128)));
		panelEmployeeInfo.setBounds(23, 212, 520, 128);
		contentPanel.add(panelEmployeeInfo);
		panelEmployeeInfo.setLayout(null);
		
		discountField.setBounds(112, 47, 61, 26);
		panelEmployeeInfo.add(discountField);
		discountField.setColumns(10);
		
		JLabel lblDiscount = new JLabel("Discount:");
		lblDiscount.setBounds(46, 52, 61, 16);
		panelEmployeeInfo.add(lblDiscount);
		
		JLabel label = new JLabel("%");
		label.setBounds(176, 52, 26, 16);
		panelEmployeeInfo.add(label);
		
		purchasesField.setColumns(10);
		purchasesField.setBounds(357, 47, 115, 26);
		panelEmployeeInfo.add(purchasesField);
		
		JLabel lblSumOfPurchases = new JLabel("Sum of Purchases:");
		lblSumOfPurchases.setBounds(237, 52, 124, 16);
		panelEmployeeInfo.add(lblSumOfPurchases);
		
		JLabel lblCustumerMustBuy = new JLabel("Custumer must buy products in total price \nof “Sum of Purchases”, in order to be the VIP");
		lblCustumerMustBuy.setHorizontalAlignment(SwingConstants.CENTER);
		lblCustumerMustBuy.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblCustumerMustBuy.setVerticalAlignment(SwingConstants.TOP);
		lblCustumerMustBuy.setBounds(37, 100, 445, 13);
		panelEmployeeInfo.add(lblCustumerMustBuy);
		
		FillTableData();
		try {
			setVIPdiscount();
			
			getAllCustomersData();
			updateTable(allCustomersList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// METHODS ----------------------------------------------------------
	private void FillTableData() {

		defaultTableModel.addColumn("Name");
		defaultTableModel.addColumn("Tel");
		defaultTableModel.addColumn("Id");
		defaultTableModel.addColumn("Purchases");
		defaultTableModel.addColumn("Sum of purch.");
		defaultTableModel.addColumn("Status");
		
		
		customersTable.setModel(defaultTableModel);
		
	}
	
	/**
	 * Update Employees table
	 * @param data
	 * @throws Exception 
	 */
	private void updateTable(JSONObject data) throws Exception {
		DefaultTableModel model = (DefaultTableModel) customersTable.getModel();
		model.setRowCount(0);
		
		for (Object key : data.keySet()) {
			String keyStr = (String) key;
			
	        JSONObject product = (JSONObject) data.get(keyStr);

	        String name = (String) product.get("name");
	        String tel = (String) product.get("tel");
	        String id = (String) product.get("id");
	        String numOfDeals = (String) product.get("numOfDeals");
	        String sumOfDeals = (String) product.get("sumOfDeals") + '$';
	        String status = (String) product.get("status");
	        
        		defaultTableModel.addRow(new Object[] { name, tel, id, numOfDeals, sumOfDeals, status});
	        
		}
	}
	
	/**
	 * Get all employees data from the server
	 * @throws Exception 
	 */
	private void getAllCustomersData() throws Exception {
		JSONObject reqObj = new JSONObject();
		String resString;
		reqObj.put("getAllCustomers", "null");
		
		serverRequest.println(reqObj.toJSONString());
		resString = serverResponse.readLine();
		if( resString != null && resString.length() > 0 ) {
			allCustomersList = (JSONObject) jsonparser.parse(resString);
		}
		else {
			throw new Exception("Customers Data empty");
		}
	}
	
	/**
	 * Set VIP discount information
	 * @throws Exception
	 */
	private void setVIPdiscount() throws Exception {
		getVipDiscountInfo();
		
		if ( discountInfo.get("sumOfPurchases") != null) {
			purchasesField.setText(discountInfo.get("sumOfPurchases").toString());
		}
		
		if ( discountInfo.get("discount") != null) {
			discountField.setText(discountInfo.get("discount").toString());
		}
	}
	
	/**
	 * Get VIP discount information
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
	 * Update VIP discount
	 * @throws IOException
	 */
	private void updateVipDiscount() throws IOException {
		JSONObject reqObj = new JSONObject();
		JSONObject discountInfo = new JSONObject();
		String respString;
		
		discountInfo.put("discount", discountField.getText());
		discountInfo.put("sumOfPurchases", purchasesField.getText());
		
		reqObj.put("updateVipDiscountInfo", discountInfo);
		
		serverRequest.println(reqObj.toJSONString());
		respString = serverResponse.readLine();
		
		if( respString != null && respString.equals("done")) {
			JOptionPane.showMessageDialog(null, "Discount updated succesfully");
		} else {
			JOptionPane.showMessageDialog(null, "Something went wrong, please try again later");
		}
	}
	
}
