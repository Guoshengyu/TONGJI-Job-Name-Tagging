package instance;

public class TokenInfo {

	private String token;
	private double TF;
	private double IUF;
	private double value;

	public TokenInfo(){
		
	}
	public TokenInfo(TokenInfo source){
		this.token = source.token;
		this.TF = source.getTF();
		this.IUF = source.getIUF();
	}
	public TokenInfo(String tokenInput, double TFInput, double IDFInput) {
		token = tokenInput;
		TF = TFInput;
		IUF = IDFInput;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public double getTF() {
		return TF;
	}

	public void setTF(double tF) {
		TF = tF;
	}

	public double getIUF() {
		return IUF;
	}

	public void setIUF(double iUF) {
		IUF = iUF;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
}
