package vo;

public class BuyVO {
   private int id;
   private String productId;
   private String memberId;
   private String bDate;
   private int count;
   
   
   
   public BuyVO(int id, String productId, String memberId, String bDate, int count) {
      this.id = id;
      this.productId = productId;
      this.memberId = memberId;
      this.bDate = bDate;
      this.count = count;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getProductId() {
      return productId;
   }

   public void setProductId(String productId) {
      this.productId = productId;
   }

   public String getMemberId() {
      return memberId;
   }

   public void setMemberId(String memberId) {
      this.memberId = memberId;
   }

   public String getbDate() {
      return bDate;
   }

   public void setbDate(String bDate) {
      this.bDate = bDate;
   }
   
   

}