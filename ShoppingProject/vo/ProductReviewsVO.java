package vo;

public class ProductReviewsVO {
   private int id;
   private String productId;
   private String content;
   private String writer;
   private String wDate;
      
   
   
   
   public ProductReviewsVO(String productId, String content, String writer) {
	this.productId = productId;
	this.content = content;
	this.writer = writer;
}

public ProductReviewsVO(int id, String productId, String content, String writer, String wDate) {
      this.id = id;
      this.productId = productId;
      this.content = content;
      this.writer = writer;
      this.wDate = wDate;
   }
   
   public int getId() {
      return id;
   }
   public void setId(int id) {
      this.id = id;
   }
   public String getProduct_id() {
      return productId;
   }
   public void setProduct_id(String productId) {
      this.productId = productId;
   }
   public String getContent() {
      return content;
   }
   public void setContent(String content) {
      this.content = content;
   }
   public String getWriter() {
      return writer;
   }
   public void setWriter(String writer) {
      this.writer = writer;
   }
   public String getwDate() {
      return wDate;
   }
   public void setwDate(String wDate) {
      this.wDate = wDate;
   }
   
   @Override
   public String toString() {
      return "ProductReviewsVO [id=" + id + ", product_id=" + productId + ", content=" + content + ", writer="
            + writer + "]";
   }

}