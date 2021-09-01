package vo;



public class ProductVO {
	private String productId;
	private String productName;
	private int price;
	private String productSize;
	private int stock;
	private String category;
	private String productGender;

	
	
	public ProductVO() {}
	


	public ProductVO(String productName, String productId, int price, int stock, String category, String productGender) {
		this.productName = productName;
		this.productId = productId;
		this.price = price;
		this.stock = stock;
		this.category = category;
		this.productGender = productGender;
	}

	public ProductVO(String productId, String productName, int price, String productSize, int stock,
			  String productGender, String category) {
		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.productSize = productSize;
		this.stock = stock;
		this.productGender = productGender;
		this.category = category;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getProductSize() {
		return productSize;
	}

	public void setProductSize(String productSize) {
		this.productSize = productSize;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getProductGender() {
		return productGender;
	}

	public void setProductGender(String productGender) {
		this.productGender = productGender;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "ProductVO [productId=" + productId + ", productName=" + productName + ", price=" + price
				+ ", productSize=" + productSize + ", stock=" + stock + ", productGender=" + productGender
				+ ", category=" + category + "]";
	}
	


}