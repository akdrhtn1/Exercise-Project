package vo;

public class CartVO {

	private int sbID;
	private String memberId;
	private String productId;
	private String productName;
	
	
	
	
	
	
	public CartVO(int sbID, String memberId, String productId, String productName) {
		this.sbID = sbID;
		this.memberId = memberId;
		this.productId = productId;
		this.productName = productName;
	}

	public int getSbID() {
		return sbID;
	}
	
	public void setSbID(int sbID) {
		this.sbID = sbID;
	}
	
	public String getMemberId() {
		return memberId;
	}
	
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	public String getProductId() {
		return productId;
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
}
