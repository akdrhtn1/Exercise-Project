package vo;

public class CardVO {
	private String cardId;
	private String memberId;
	private int balance;
	
	
	public CardVO(String cardId, String memberId, int balance) {
		this.cardId = cardId;
		this.memberId = memberId;
		this.balance = balance;
	}
	
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	
}
