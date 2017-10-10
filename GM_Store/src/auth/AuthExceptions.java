package auth;

public class AuthExceptions extends Exception {
	
	public AuthExceptions(String msg) {
		super(msg);
	}
	
	public AuthExceptions() {
		super("SignIn exception");
	}
}



