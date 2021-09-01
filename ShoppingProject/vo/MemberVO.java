package vo;

public class MemberVO {
	private String id;
	private String password;
	private String name;
	private String birth;
	private int gender;
	private String phone;
	private String email;
	private String address;
	private int point;
	private String rank;
	private String cardNumber;
	private int cardPayment;
	
	public MemberVO() {}
	
	
	public MemberVO(String id, String password, String name, String birth, int gender, String phone, String email,
			String address) {

		this.id = id;
		this.password = password;
		this.name = name;
		this.birth = birth;
		this.gender = gender;
		this.phone = phone;
		this.email = email;
		this.address = address;
	}
	
	
	public MemberVO(String id, String password, String name, String birth, int gender, String phone, String email,
			String address, int point) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.birth = birth;
		this.gender = gender;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.point = point;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public int getCardPayment() {
		return cardPayment;
	}
	public void setCardPayment(int cardPayment) {
		this.cardPayment = cardPayment;
	}
	
	
}
