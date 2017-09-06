package employee;

public class EmployeeException extends Exception{
	public EmployeeException(String msg) {
		super(msg);
	}
	
	public EmployeeException() {
		super("Employee exception");
	}
}

