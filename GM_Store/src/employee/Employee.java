package employee;
//import java.util.InputMismatchException;
//import java.util.Scanner;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public abstract class Employee  {
	
	protected String name;
	protected long id;
	protected long tel;
	protected int bankAccount;
	protected int employeeNumber;
	protected Set<Integer> workPlacesId = new LinkedHashSet<Integer>();
	
	/** ======================================================
	 * Employee Constructor
	 * @param name
	 * @throws EmployeeException
	 ====================================================== */
	public Employee(
			String inputName, 
			long id, 
			long tel, 
			int bankAccount, 
			int empNumber) throws EmployeeException
	{
		this.name = inputName;
		this.id = id;
		this.tel = tel;
		this.bankAccount = bankAccount;
		this.employeeNumber = empNumber;
	}

	 /** ------------------------------------------------------
	 * Print Employee Object to the console
	 * @return String
	 ------------------------------------------------------- */
	@Override
	public String toString() {
		String returnStr = "";
		returnStr += "\n name: \t"		+ this.name;
		returnStr += "\n Tel: \t"		+ this.tel;
		returnStr += "\n Id: \t"			+ this.id;
		returnStr += "\n Emp #: \t"		+ this.employeeNumber;
		returnStr += "\n Works in: \t"	+ this.workPlacesId.toString();
		
		return returnStr;
	}
	
	// GETERS :
	public long getId () { return this.id; }
	public int getEmployeeNumber () { return this.employeeNumber; }
	public String getName () { return this.name; }
	public Set<Integer> getAllWorkPlaces () { return this.workPlacesId; }
	
	// SETTERS :
	/** ------------------------------------------------------
	 * Set work place id of employee
	 * @param workId
	 * @throws EmployeeException
	 ------------------------------------------------------ */
	public void setWorkPlaceId(int workId) throws EmployeeException {
		if ( this.workPlacesId.add(workId) ) {}
		else {
			throw new EmployeeException("Failed to add work place to " + this.name);
		};
	}
	
	/** ------------------------------------------------------
	 * Identify Employee object by id + randomNumber + name
	 ------------------------------------------------------ */
	@Override
	public int hashCode() {
		Random rnd = new Random();
		double resault;
	
		resault = rnd.nextDouble() + this.id + this.name.codePointCount(0, this.name.length());
		return (int)resault; 
	}
	
	
	
//	public static void main(String [] args) {
//		
//		Scanner scan;
//		String userInputName;
//		
//		try {
//			scan = new Scanner(System.in);
//			System.out.println("Please enter your employee name: \n");
//			userInputName = scan.next();
//		
//			Employee newEmployee  = new Employee(
//				userInputName,
//				1,
//				333,
//				444,
//				12
//			);
//			newEmployee.setWorkPlaceId(222);
//			System.out.println(newEmployee.toString());
//		
//		} catch (InputMismatchException e) {
//			System.out.println("Invalid input: " + e.getMessage());
//		} catch(EmployeeException e) {
//			System.out.println("Employee exception: " + e.getMessage());
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//	}
}
