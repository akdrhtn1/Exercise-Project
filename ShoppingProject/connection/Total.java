package connection;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.regex.Pattern;
import java.util.*;

import dao.*;
import vo.*;

public class Total {
	
	ManagerVO login2;
	MemberVO login;
	
	MemberDAO memberdao = new MemberDAO();
	ProductDAO productdao = new ProductDAO();
	ManagerDAO managerdao = new ManagerDAO();
	BuyDAO buydao = new BuyDAO();
	CardDAO cardao = new CardDAO();
	CartDAO cartdao = new CartDAO();
	ProductReviewDAO reviewdao = new ProductReviewDAO();
	
	DecimalFormat formatter = new DecimalFormat("###,###");
	
	int point;
	boolean isCheck;

	
	  	
	public void select(Scanner sc) {

		
		//login 에대한 정보를 login 메서드를 통해서 받고
		System.out.println("****** 쇼핑몰에 들어 오신걸 환영합니다. ******");
		
		while(true) {
	
			System.out.println(" 1. 회원가입 2. 로그인 3. 쇼핑몰 둘러보기 4. 관리자 연락처 확인 5. 종료");
			System.out.print(">>");
			String userChoice  = sc.nextLine();
			switch(userChoice) {
			case "1": resister(sc); break;

			case "2": logIn(sc); break;
					 
			case "3": search(sc); break;
			
			case "4": managerCall(); break;
			
			case "5":  
					  System.out.println("프로그램이 종료 되었습니다");
				      sc.close();
					  JDBConnection.close(); break;
			default:System.out.println("잘못된 수를 입력하셨습니다 !!"); break;
			}
			if("5".equals(userChoice)) {
				
				break;
			}
		
		}
	}
	
	//회원가입 테이블
	private void resister(Scanner sc) {
		MemberVO mvo = null;
		int result = 0;
		System.out.println(">>회원 정보 입력을 시작하겠습니다.<<");
		System.out.println("-------------------------");
		
		//패턴 형식으로 아이디는 영어,숫자만 입력가능
		String pattern = "^[a-zA-Z0-9]*$";
		System.out.println(">>아이디를 입력해주세요(영어&숫자) :");
		System.out.print("아이디 :");
		String id = verification(pattern, sc);
		
		while(true) {
			if(id.length()>=5) {
				if(id.substring(0, 5).equalsIgnoreCase("admin")){
					System.out.println("관리자 이름으로 등록하실 수 없습니다.");
					System.out.print("아이디 :");
					id = sc.nextLine();
				}else {
					break;
				}
			}else {
				break;
			}
		}
		//비밀번호 (숫자, 문자, 특수문자 포함 8~20자리 이내)
		pattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";
		System.out.println(">>비밀번호를 입력해주세요(숫자, 문자, 특수문자 포함 8~20자리 이내)");
		System.out.print(">>비밀번호 :");
		String password = verification(pattern, sc);
		
		
		System.out.print(">>이름 :");
		String name = sc.nextLine();
		
		//날짜 형식 패턴 사용 0000-00-00
		pattern = "^[1-2][0-9][0-9][0-9]\\-[0-1][0-9]\\-[0-3][0-9]$";
		System.out.print(">>생년월일을 입력해주세요(형식:1999-12-31) :");
		String birth = verification(pattern, sc);
		
		System.out.print(">>성별을 입력해주세요(1:남성, 2:여성) :");
		int gender = Integer.parseInt(sc.nextLine());
		
		//휴대폰 전화 입력형식
		pattern = "\\d{3}-\\d{4}-\\d{4}";
		System.out.print(">>휴대폰번호를 입력해주세요 :");
		String phone = verification(pattern, sc);
		//이메일 형식의 패턴만 입력가능 예) akdrhtn@kakao.com
		//@다음 . 들어가야함
		pattern = "\\w+@\\w+\\.\\w+(\\.\\w+)?"; 
		System.out.print(">>이메일을 입력해주세요 :");
		String email = verification(pattern, sc);
		
		System.out.print(">>주소를 입력해주세요 :");
		String address = sc.nextLine();
		
		mvo = new MemberVO(id, password, name, birth, gender, phone, email, address);
		System.out.println(">>회원가입을 하시겠습니까?(y/n)");
		String perfact = sc.nextLine();
		if(perfact.equalsIgnoreCase("y")) {
			result = memberdao.insert(mvo);
		}
		
		
		if(result>=1) {
			System.out.println("정상적으로 등록되셨습니다.");
		}else {
			System.out.println("회원가입이 취소 되었습니다.");
		}
	}
	
	
	
	//-------------------------------------로그인 관리-----------------------------
	private void logIn(Scanner sc) {
		
		String pattern = "^[a-zA-Z0-9]*$";
		System.out.println("아이디를 입력해주세요(영어&숫자) :");
		System.out.print(">>아이디 :");
		String id = verification(pattern, sc);
		boolean check = true;
		
		//비밀번호 (숫자, 문자, 특수문자 포함 8~15자리 이내)
		pattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";
		System.out.println("비밀번호를 입력해주세요(숫자, 문자, 특수문자 포함 8~20자리 이내)");
		System.out.print(">>비밀번호 :");
		String password = verification(pattern, sc);
		//-------------------------관리자 로그인------------------------- 
		if(id.length()>=5) {
			if(id.substring(0, 5).equalsIgnoreCase("admin")){
				login2 = managerdao.logIn(id, password);
				if(login2 != null) {
					System.out.println(login2.getName() + "님께서 로그인 하셨습니다.");
				}
				while(login2 != null) {
					System.out.println(">> 1.상품 등록  2. 재고 관리 3. 정보 수정 4. 로그아웃 ");
					System.out.print(">>");
					String userChoice = sc.nextLine();
					switch(userChoice) {
					case "1": productRgstr(sc); break;
					case "2": invntMngmn(sc); break;
					case "3": managerUpdate(sc); break;
					case "4": 	check = false;
								login2 = null;
							    break;
					default: System.out.println("숫자를 잘못 입력하셨습니다.");
					break;
					
					}
				}
			}
		}
		if(check) {
			login = memberdao.logIn(id,password);
			if(login == null) {
				System.out.println("아이디, 비밀번호와 일치하는 회원이 존재하지 않습니다.");
			}else {
				isCheck = true;
			
				System.out.println(login.getName()+ "님께서 로그인 하셨습니다.");
				//로그인한 회원의 카드번호 연결!! , 금액 연결 성공
				login.setCardNumber(cardao.cardId(login.getId()));
				login.setCardPayment(cardao.balanceOne(login.getCardNumber()));
				//point 연결
				point = memberdao.point(login);
			
				login2 = new ManagerVO();
				login2.setMoney(managerdao.moneySearch("admin"));
				
				if(login.getCardNumber() == null || login.getCardNumber() == "") {
					System.out.println("-----------------------공지-----------------------");
					System.out.println("회원님 카드 개설을 하지 않으면 구매하실 때 불편함을 겪으실 수도 있으니");
					System.out.println("가장 먼저 카드 개설 메뉴에서 개설을 신청해 주시기 바랍니다.\n");
					System.out.println();
				}
			}
			while(login != null) {
				
				System.out.println(">>1. ★카드 개설★ 2. 쇼핑하기 3. 장바구니 이동 4. 회원 정보수정,확인 5. 구매 내역 조회\n  6. 카드 입금,조회 7. 포인트 조회 8. 로그 아웃 9. 회원 탈퇴 10. 관리자 연락처 확인");
				System.out.print(">>");
				String userChoice = sc.nextLine();
				switch(userChoice) {
				case "1": cardRgstr();break;
				case "2": search(sc); break;
				case "3": shpngBskt(sc); break;//장바구니 이동------------구현해야함
				case "4": update(login, sc);break;// 여기서 update 메소드 생성
				case "5": purHist(sc); break; 
				
				case "6": cardDpst(sc);
				          break;
				case "7": point = memberdao.point(login);
						  System.out.println("현재 보유중이신 포인트는 : " +point+ "포인트 입니다.");
						  break;
				
				case "8": if(isCheck) {
					  		cardao.deposit(login.getCardPayment(), login.getCardNumber());
							}
							login = null; break;
						  
						  
				case "9":   System.out.println("정말 회원을 탈퇴 하시겠습니까? (y/n)");
							String isCheck = sc.nextLine();
							if ("y".equalsIgnoreCase(isCheck)) {
									memberdao.delete(login.getId(), sc);
									System.out.println("회원 탈퇴에 성공하셨습니다.");
									login = null;
									break;	  
							}else {
								continue;
							}
				case "10": managerCall(); break;
				default : System.out.println("잘못된 번호를 입력하셨습니다"); break;
					
				
					}
				
				}
			}
		}
	
	//-----------------------------1. 관리자 (상품 등록)----------------------
	private void productRgstr(Scanner sc){
		
		System.out.println(">>제품번호를 입력해주세요: ");
		String productId = sc.nextLine();
		System.out.println(">>상품명 입력해주세요: ");
		String name = sc.nextLine();
		System.out.println(">>가격을 입력해주세요: ");
		String price = sc.nextLine();
		System.out.println(">>사이즈를 입력해주세요: ");
		String size = sc.nextLine();
		System.out.println(">>재고를 입력해주세요: ");
		String stock = sc.nextLine();
		System.out.println(">>종류를 입력해주세요(1~5): ");
		String category = sc.nextLine();
		System.out.println(">>성별을 입력주세요(M/FM): ");
		String gender  = sc.nextLine();
		ProductVO vo = new ProductVO(productId,name,Integer.parseInt(price),size,
									Integer.parseInt(stock),category,gender);
		
		int result = productdao.insert(vo);
		if(result>0) {
			System.out.println("정상적으로 상품이 등록되었습니다.");
		}else {
			System.out.println("상품 등록중 오류가 발생했습니다.");
		}
	}
	//-----------------------------2. 관리자 재고 관리 -----------------------------
	private void invntMngmn(Scanner sc){
		List<ProductVO> list = null;
		System.out.println("몇개 이하의 재고를 조회하겠습니까?");
		String number= sc.nextLine();
		
		while(true) {
	
			list = productdao.stockNumber(Integer.parseInt(number));
			
			Iterator<ProductVO> itor = list.iterator();
			System.out.println("==================================================");
			System.out.println("    상품번호             제품명          재고          ");
			System.out.println("==================================================");
			while(itor.hasNext()) {
				ProductVO pro = itor.next();
				System.out.printf("  %5s       %15s     %3s\n",pro.getProductId(),pro.getProductName(),pro.getStock());
			}
			System.out.println();
			
			System.out.println("1. 재고 추가 2. 상품 삭제 3. 뒤로 가기 ");
			String userChoice = sc.nextLine();
			if("1".equals(userChoice)) {
				stockInsert(sc);
			}if("2".equals(userChoice)) {
				stockDelete(sc);
				//삭제 만들어야함 
			}
			
			else {
				break;
			}
			
		}
	}
	// --------------------------- 2-1 관리자 재고 추가------------------------
	private void stockInsert(Scanner sc) {
		System.out.println("재고를 추가할 상품의 번호를 입력해주세요 !!");
		System.out.print(">>");
		String productId = sc.nextLine();
		
		int stock = productdao.stockView(productId);
		
		System.out.println("몇개의 재고를 추가 하시겠습니까?");
		System.out.print(">>");
		String count = sc.nextLine();
	
		stock += Integer.parseInt(count);
		int result = productdao.updateStock(stock, productId);
		
		if(result>0) {
			System.out.println("정상적으로 재고가 수정되었습니다.");
		}else {
			System.out.println("재고를 수정중 오류가 발생했습니다.");
		}
		
	
	}
	//----------------------------2. 삭제 ----------------------------------
	private void stockDelete(Scanner sc) {
		System.out.println("삭제할 상품의 번호를 입력해주세요 !!");
		System.out.print(">>");
		String productId = sc.nextLine();
		
		int result = productdao.delete(productId);
		
		if(result>0) {
			System.out.println("정상적으로 상품이 삭제되었습니다.");
		}else {
			System.out.println("삭제중 오류가 발생했습니다.");
		}
		
	}
	
	//----------------------------3. 관리자 수정-------------------------------
	private void managerUpdate(Scanner sc) {
		
		
		System.out.println("1.개인정보 확인 2. 개인정보 수정 3.뒤로 가기");
		String str = sc.nextLine();
		if("1".equals(str)) {
			System.out.println("===============================");
			System.out.println("           개인정보 확인          ");
			System.out.println("===============================");
			System.out.println("아이디  :" + login2.getId());
			System.out.println("이름    :" + login2.getName());
			System.out.println("전화번호 :" + login2.getPhone());
			System.out.println("=================================");

			System.out.println();
			
		}else if("2".equals(str)) {
			String pattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";
			System.out.println(">>비밀번호를 입력해주세요(숫자, 문자, 특수문자 포함 8~20자리 이내)");
			System.out.print(">>비밀번호 :");
			String password = verification(pattern, sc);
			
			
			System.out.print(">>이름 :");
			String name = sc.nextLine();
			
			pattern = "\\d{3}-\\d{4}-\\d{4}";
			System.out.print(">>휴대폰번호를 입력해주세요 :");
			String phone = verification(pattern, sc);
			
		
			login2.setPassword(password);
			login2.setName(name);
			login2.setPhone(phone);
			
			int result = managerdao.update(login2);
			if(result>0) {
				System.out.println(login2.getId()+ "회원님의 정보가 수정 되었습니다.");
			}
			else {
				System.out.println("수정중 오류가 발생했습니다.");
			}
		}
	
	}
	
	
	
	
	//--------------------------------"1.사용자 ★쇼핑몰 카드 개설(입금식)★ -------------------------
	private void cardRgstr() {
	
		if(login.getCardNumber() == null || login.getCardNumber() == "") {
			
		//랜덤 계좌번호 생성
		String accNumber = "032-" + (int)((Math.random()*999)+1) +"-"+(int)((Math.random()*999999)+1);
		int result = cardao.cardCreate(login, accNumber);
			
			if(result >0) {
				System.out.println("계좌가 정상적으로 개설 되었습니다.");
				login.setCardNumber(cardao.cardId(login.getId()));
			}else {
				System.out.println("계좌 개설중 오류가 발생했습니다.");
			}
		}
		else {
			System.out.println("현재 카드를 소지하고 계십니다.");
		}
		
	}

	
	
	//---------------------------------------3. 장바구니 이동------------------------
	private void shpngBskt(Scanner sc) {
		//우선 전체조회를 넣어야함
		List<CartVO> list = cartdao.selectOne(login.getId());
		if(list.isEmpty()) {
			System.out.println("장바구니안에 상품이 존재하지 않습니다.");
		}
		else {
			Iterator<CartVO> ite = list.iterator();
			System.out.println("=========================================================");
			System.out.println("  번호      계정 아이디       상품번호                상품명      ");
			System.out.println("=========================================================");
			while(ite.hasNext()) {
				CartVO cart = ite.next();
				System.out.printf("  %d       %s        %3s         %s\n",cart.getSbID(),cart.getMemberId(),cart.getProductId(),cart.getProductName());
			}
			System.out.println();
			// 장바구니 전체조회(조회하고)-> 장바구니 주문 , 장바구니 삭제
			cartMenu(sc);
		}
	}
	
	//장바구니 주문 메뉴
	private void cartMenu(Scanner sc){
		String userChoice = "";
		
		System.out.println(">> 1. 장바구니 주문 2. 장바구니 삭제 3. 뒤로 가기");
		userChoice = sc.nextLine();
		switch(userChoice) {
		case "1":  cartOrder(sc); 
					break; //주문 만들어논 메소드 실행하고 ->장바구니 삭제 
		case "2":  deleteCart(sc); break;
		case "3":  break;
		}
	}
	//------------------------------장바구니 주문------------------------
	private void cartOrder(Scanner sc) {
		String select =""; //상품 번호
		String select2 = ""; //상품 수량
		String select3 = ""; //포인트 사용여부
		
		int count = 0;
		int price = 0;
		int totalMoney = 0;
		
		
		
		while(!"0".equals(select)){
			if(login.getCardPayment() == 0) {
				System.out.println("카드에 돈이 없습니다.!!");
				System.out.println("돈을 입금해주세요.!!");
				break;
			}
			
			System.out.println("구매하실 장바구니의 번호를 입력해주세요 (0:뒤로가기)");
			System.out.print(">>");
			
			select = sc.nextLine();
		
			//여기서 장바구니의 번호를 통해서 입력을 받고 
			//아이디를 추출 장바구니의 아이디랑 아이디랑 같은지 확인 하고 다르면 break;?
			String writer = cartdao.memIdCheck(select);
			
			if(!login.getId().equals(writer)) { //본인 아이디가 맞는지 체크 
				System.out.println("해당 상품은 장바구니에 없는 상품입니다.");
				break;
			}
			
			
			if("0".equals(select)) {
				break;
			}
			price = cartdao.selectPrice(select); //제품 가격
			
			System.out.println("구매하실 상품의 수량을 입력해주세요 ");
			System.out.print(">>");
			select2 = sc.nextLine();
			count = Integer.parseInt(select2);
			//가격 * 개수 가 30000원 이하일 경우 배송비 30000원 추가
			
			totalMoney = price * count;
			
			
			if(totalMoney<30000) {
				System.out.println("30000원 이하일 경우 3000원의 배송비가 추가 됩니다");
				price += 3000; //30000원 이하일떄 배송비 추가
				
				System.out.println("정말로 구매하시겠습니까? (y/n)");
				System.out.print(">>");
				String pCheck = sc.nextLine();
				if("n".equalsIgnoreCase(pCheck)) {
					continue;
				}
				
			}
			int logMoney = login.getCardPayment(); 

			System.out.println(totalMoney);
			if(logMoney<totalMoney) {
				System.out.println("돈이 모자르니 돈을 충전해주세요 !!");
				break;
			}else {
				//재고를 받았잖아 근데 1개씩만 살 일이 없어 수량을 입력하면서 그 값이랑 비교 ! 
				
				
				if(point !=0) {
					
					System.out.println(">>현재 가지고 계신 포인트는" + point + "입니다.");
					System.out.println(">>포인트를 사용하시겠습니까?(y,n) ");
					select3 = sc.nextLine();
				
					if("y".equalsIgnoreCase(select3)) {
						totalMoney -= point;
						point = 0;

					}
				}
				
				int stock = cartdao.selectStock(select);
				
				stock -= count;
				//재고가 없을떄 출력
				
				if(stock<0) {
					System.out.println("현재 해당 제품의 재고가 부족합니다.");
					break;
				}
				
				//(결제가 가능)
				//3개가 동시에 처리 되야 하므로 트랜잭션 처리 재고에서 commit 수동처리 실시
				//사용자 돈 차감하고 돈이 들어감
				logMoney -= totalMoney;
				login.setCardPayment(logMoney);
				//관리자 입금
				int deposit = login2.getMoney();
				int result =  deposit + totalMoney;
				login2.setMoney(result);
				
				
				
				//해당 장바구니의 제품 아이디를 셀렉트
				String selectPId = cartdao.selectProduct(select);
				//해당 제품의 재고를 줄임 update
				int transec = productdao.updateNCStock(stock, selectPId); // 커밋 x
				//구매내역에 추가 Insert
				int transec1 = buydao.insert(selectPId, login.getId(),count); // 커밋 x
				//사용자 포인트 증가 메서드 호출
				
				//point 0인데 이건 totalmoney가 0
				point += (totalMoney*0.01)+2;
				
				int transec2 = memberdao.pointUpdate(point, login.getId()); //커밋 x 
				//관리자 돈 받는 메소드 호출
				int transec3 = managerdao.moneyModify(result, "admin"); //커밋 x
				
				
				
				// 트랜잭션 처리 4개의 단위를 묶어서 정상적으로 처리되는지 확인
				boolean check = memberdao.transection(transec, transec1, transec2,transec3);
				
				cartdao.delete(Integer.parseInt(select));
				
				if(check) {
					System.out.println("구매가 정상적으로 진행되었습니다.");
				}else {
					System.out.println("구매중 오류가 발생했습니다.");
				}
				
			}
			
		}
		
	}
	
	private void deleteCart(Scanner sc) {
		System.out.println(">> 삭제 하실 장바구니 번호를 입력해주세요");
		System.out.print(">>");
		String delete = sc.nextLine();
		
		int result = cartdao.delete(Integer.parseInt(delete));
		
		if(result>0) {
			System.out.println("장바구니가 삭제 되었습니다.");
		}else {
			System.out.println("삭제중 오류가 발생 되었습니다.");
		}
	}
	
	
	//----------------4. 사용자 수정-------------------
	private void update(MemberVO user,Scanner sc) {
		
		
		
		System.out.println("1.개인정보 확인 2. 개인정보 수정 3.뒤로 가기");
		String str = sc.nextLine();
		if("1".equals(str)) {
			System.out.println("===============================");
			System.out.println("           개인정보 확인          ");
			System.out.println("===============================");
			System.out.println("아이디 :" + user.getId());
			System.out.println("이름 :" + user.getName());
			System.out.println("생년월일 :" + user.getBirth().substring(0, 10));
			if(user.getGender()==1){
				System.out.println("성별 : 남성");
			}else if(user.getGender()==2){
				System.out.println("성별 : 여성");
			}
			System.out.println("전화번호 :" + user.getPhone());
			System.out.println("이메일주소 :" + user.getEmail());
			System.out.println("주소 : "+user.getAddress());
			System.out.println("===============================");
			System.out.println();
		}else if("2".equals(str)) {
		
			String pattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";
			System.out.println(">>비밀번호를 입력해주세요(숫자, 문자, 특수문자 포함 8~20자리 이내)");
			System.out.print(">>비밀번호 :");
			String password = verification(pattern, sc);
			
			pattern = "\\d{3}-\\d{4}-\\d{4}";
			
			System.out.print(">>휴대폰번호를 입력해주세요 :");
			String phone = verification(pattern, sc);
			
			
			pattern = "\\w+@\\w+\\.\\w+(\\.\\w+)?";
			System.out.print(">>이메일형식으로 입력해주세요 :");
			String email = verification(pattern, sc);
			
			System.out.print(">>주소를 입력해주세요 :");
			String address = sc.nextLine();
			
			user.setPassword(password);
			user.setPhone(phone);
			user.setEmail(email);
			user.setAddress(address);
			
			int result = memberdao.update(user);
			if(result>0) {
				System.out.println(user.getId()+ "회원님의 정보가 수정 되었습니다.");
			}
			else {
				System.out.println("수정중 오류가 발생했습니다.");
			}
		}
	
	}

	//----------------------------전체 구매 내역(purHist)---------------------------
	private void purHist(Scanner sc) {
		List<Map<String,String>> list;
		
		list = buydao.selectAll(login);
		if(list.isEmpty()){
			System.out.println("현재 구입하신 상품이 없습니다.");
		}
		else {
		
		//상품평 쓰기.. 
		while(true) {
			
		System.out.printf("=================================================================\n");
		System.out.printf(" 상품번호\t   " + "     상품명\t   "  + "    상품 사이즈"  +"      구매일자\n" );
		System.out.printf("=================================================================\n");
		
		for(Map<String, String> map : list) {
			System.out.printf("%5s\t%15s\t%5s\t%15s\n",map.get("PRODUCT_ID"),
			map.get("PRODUCT_NAME"),map.get("PRODUCT_SIZE"),map.get("B_DATE"));
		}

		
		System.out.println("1. 상품평 쓰기 2.내가 쓴 상품평 조회 3. 상품평 수정  4.상품평 삭제 5. 환불 6. 뒤로 가기");
		System.out.print(">>");
			String str = sc.nextLine();
			switch(str) {
			case "1" : writeContents(sc); break;
			case "2" : selectMyReview(sc); break;
			case "3" : updateReview(sc); break;
			case "4" : deleteReview(sc); break;
			case "5" : productRefund(sc); break;//환불 break;
			case "6" : break;
			default : System.out.println("잘못된 값을 입력 하셨습니다.");
			}
			if("5".equals(str) || "6".equals(str) ) {
				break;
				}
			}
		}
	}
	//------------------------------------------1. 상품평쓰기 ---------------------
	private void writeContents(Scanner sc) {
		ProductReviewsVO reviewVO = null;
		
		System.out.println("상품번호를 입력해주세요!!");
		System.out.print(">>");
		String productId = sc.nextLine();
		System.out.println("상품평을 적어주세요");
		System.out.print(">>");
		String content = sc.nextLine();
		int result = 0;
		
		reviewVO = new ProductReviewsVO( productId, content, login.getId());
		
		//상품평을 쓴다 = > 
		//그건 구매목록에 있는 것들만 쓴다.
		//입력한 상품번호가 나의 구매목록에 있는지 확인을 한다. 없으면 안한다.
		//구매목록에 아이디가 나의 아이디랑 같은지 확인을 한다.
		
		//상품번호를 입력하면 => 구매 테이블에서 아이디를 반환 
		String writer = buydao.memIdCheck(productId);
		
		if(login.getId().equals(writer)) { //본인 아이디가 맞는지 체크 
			result = reviewdao.insert(reviewVO);
		}else {
			System.out.println("해당 사용자가 구매한 상품이 아닙니다.");
		}
		
		
		if(result>0) {
			System.out.println("게시글 작성을 완료했습니다.");
		}else {
			System.out.println("데이터 작성중 오류가 발생했습니다.");
		}
		
		
	}
	// -----------------------------2. 내가 쓴 상품평 보기--------------------------
	private void selectMyReview(Scanner sc){
		List<Map<String,String>> list;
		
		StringBuilder sb = new StringBuilder();
		list = reviewdao.selectId(login.getId());
		if(list.isEmpty()) {
			System.out.println("해당 상품의 리뷰가 존재하지 않습니다.");
		}else {
			Iterator<Map<String, String>> itor = list.iterator();
			sb.append("=============================================================================================\n");
			sb.append(" 리뷰번호           상품명            작성자                상품평                    구매일자         \n" );
			sb.append("=============================================================================================\n");
			while(itor.hasNext()) {
				Map<String, String> map = itor.next();
				sb.append(String.format("%5s", map.get("R.ID")) + "  \t");
				sb.append(String.format("%15s", map.get("P.PRODUCT_NAME")) + "  \t");
				sb.append(String.format("%5s", map.get("R.WRITER")) + "  \t");
				sb.append(String.format("%10s", map.get("R.CONTENT")) +"  \t");
				sb.append(String.format("%5s", map.get("R.W_DATE")) +"  \n");
		
				
			}
			System.out.println(sb.toString());
			System.out.println("=============================================================================================\n");
			System.out.println("뒤로가기 : 0번");
			System.out.print(">>");
			String str = sc.nextLine();
			if("0".equals(str)) {
				return;
			}
				

		}

	}
	//-----------------3. 상품평 수정------------------
	private void updateReview(Scanner sc) {
		
		int result = 0;
		System.out.println("수정하실 리뷰번호를 입력해주세요");
		System.out.print(">> ");
		String rivewNum = sc.nextLine();
		
		System.out.println("수정하실 내용을 입력해주세요");
		System.out.print(">> ");
		String content = sc.nextLine();
		
		String writer = reviewdao.selectUser(rivewNum);
		
		if(login.getId().equals(writer)) { //본인 아이디가 맞는지 체크 
			result = reviewdao.update(content, Integer.parseInt(rivewNum));
		}else {
			System.out.println("해당 사용자가 작성한 리뷰가 아닙니다.");
		}
		
		if(result>0) {
			System.out.println("리뷰가 정상적으로 수정 되었습니다.");
		}else {
			System.out.println("리뷰가 존재하지 않습니다.");
		}
	}
	
	
	//-----------------4. 상품평 삭제-----------------------
	private void deleteReview(Scanner sc) {
		//삭제는 본안 것에 대한 리뷰만 삭제가 되게 해야함
		int result = 0;
		System.out.println("삭제하실 리뷰번호를 입력해주세요");
		System.out.print(">> ");
		String rivewNum = sc.nextLine();
		String writer = reviewdao.selectUser(rivewNum);
		
		if(login.getId().equals(writer)) {  //본인 아이디가 맞는지 체크  
			result = reviewdao.delete(Integer.parseInt(rivewNum));
		}else {
			System.out.println("해당 사용자가 작성한 리뷰가 아닙니다.");
		}
		
		if(result>0) {
			System.out.println("리뷰가 정상적으로 삭제 되었습니다.");
		}else {
			System.out.println("리뷰가 존재하지 않습니다.");
		}
	}
	// --------------------- 5. 환불 ---------------------- 
	private void productRefund(Scanner sc) {
		
		System.out.println("환불 하시려는 상품번호를 입력해주세요");
		System.out.print(">>");
		String produtNum = sc.nextLine();
		//구매내역에 존재하는 것들만 추려야하는데 
		Map<String, String> map = buydao.proRefund(produtNum, login.getId());
		//상품 번호를 입력받고 
		if(map.isEmpty()) {
			System.out.println("환불하실 상품이 존재하지 않습니다.");
		}
		else {
			int count = Integer.parseInt(map.get("COUNT"));
			int price = Integer.parseInt(map.get("PRICE"));
			
			
			//이클립스 상에서 데이터 재고 증가
			int stock = productdao.stockView(produtNum);
			stock += Integer.parseInt(map.get("COUNT"));
			
			//재고 * 가격의 돈 합계
			int totalMoney = price * count;
			// 현재 유저가 가지고 있는 금액
			int logMoney = login.getCardPayment(); 
			
			
			int pointMoney = (int)((totalMoney*0.01)+2); 
			point +=  pointMoney;
			
			// 재고 추가 
			//(결제가 가능)
			//3개가 동시에 처리 되야 하므로 트랜잭션 처리 재고에서 commit 수동처리 실시
			int deposit = login2.getMoney();
			int result =  deposit - totalMoney + pointMoney;
			login2.setMoney(result);
			
			//포인트를 뺀 돈을 받음
			logMoney += totalMoney-pointMoney;
			login.setCardPayment(logMoney);
			
			//관리자 입금
	
			//해당 제품의 재고를 추가
			int transec = productdao.updateNCStock(stock, produtNum); // 커밋 x
			//구매내역에 삭제해야함
			int transec1 = buydao.refund(map.get("B.ID")); // 커밋 x
			//사용자 포인트 지급 메서드 호출
			int transec2 = memberdao.pointUpdate(point, login.getId()); //커밋 x 
			// 관리자 돈을 뺍니다
			int transec3 = managerdao.moneyModify(result, "admin"); //커밋 x
			
			
			
			// 트랜잭션 처리 4개의 단위를 묶어서 정상적으로 처리되는지 확인
			boolean check = memberdao.transection(transec, transec1, transec2,transec3);
			
			if(check) {
				System.out.println("환불이 정상적으로 처리되었습니다.");
			}else {
				System.out.println("환불중 오류가 발생했습니다.");
			}
	
		}
	}
	
	
	//---------------------카드 조회, 입금----------------------
	private void cardDpst(Scanner sc) {
		String check = "";
		do {
			System.out.println(" 1. 카드 조회 2. 카드 입금 3. 뒤로가기");
			System.out.print(">>");
			check = sc.nextLine();
			switch(check) {
			case "1" :  System.out.println("현재 카드 잔고 : " + login.getCardPayment() + "원 입니다.");
	          			if(login.getCardPayment()==0) {
	          			System.out.println("결제를 하기 위해서는 충전을 해주세요");
	          			}
	          			break;
	          			//카드가 있을떄 입금을 할 수 있어야함
			case "2" :  if(login.getCardNumber() != "") {
						System.out.println("입금하실 돈을 입력해주세요 >");
						String money = sc.nextLine();
						login.setCardPayment(login.getCardPayment()+Integer.parseInt(money)); 
						System.out.println("돈이 입금되었습니다.");
						break;
						}else {
							System.out.println("현재 카드를 개설하지 않았습니다.");
						}
						
			case "3" : break;
			
			default:System.out.println("잘못된 수를 입력하셨습니다 !!"); break;
			}
			
		}while(!"3".equals(check));
	}
	
	//------------------------2. 회원 쇼핑몰 검색----------------------
	private void search(Scanner sc) {
		

		String check = "";
		do {
			System.out.println(" 1. 전체 목록 조회 2. 상품명 조회 3. 종류별 조회 4. 성별 조회 5. BEST 상품 조회 6. 뒤로가기");
			System.out.print(">>");
			check = sc.nextLine();
			switch(check) {
			case "1" : listAll(sc); break;
			case "2" : listName(sc); break;
			case "3" : listCategory(sc); break;
			case "4" : listGender(sc); break;
			case "5" : bestItem(sc); break;
			case "6" : break;
			
			default:System.out.println("잘못된 수를 입력하셨습니다 !!"); break;
			}
			//System.out.println(1.구매 2. 장바구니 담기 3. 뒤로가기);
		}while(!"6".equals(check));
	}
	
	// --------------------------2-1조회------------------------------
	//전체 목록 조회
	private void listAll(Scanner sc) {
		List<ProductVO> list;
		list = productdao.selectAll();
		printAll(list,sc);
		
	}
	// 상품명 조회
	private void listName(Scanner sc) {
		List<ProductVO> list;
		String query = "PRODUCT_NAME";
		System.out.println("조회하실 상품명을 입력해주세요!!");
		System.out.print(">>");
		String name = sc.nextLine();
		list = productdao.selectOne(query,name);
		printAll(list,sc);
	}
	//성별 조회 

	
	private void listGender(Scanner sc) {
		List<ProductVO> list;

		while(true) {
		   String query = "PRODUCT_GENDER";
		   System.out.println("성별을 입력해주세요(M:남성 ,FM:여성)");
		   System.out.print(">>");
		   String gender = sc.nextLine();
		    if(gender.equalsIgnoreCase("fm") || gender.equalsIgnoreCase("m")) {
		        list = productdao.selectProduct(query, gender.toUpperCase());
		        printAll(list, sc);
		        break;
		      }else {
		    	  System.out.println("잘못 입력하셨습니다.");
		      }
		         
		   }   
	  }
	// 종류 조회
	private void listCategory(Scanner sc) {
		List<ProductVO> list;
		while(true) {
			String query = "CATEGORY";
			System.out.println("종류를 입력해주세요(1:상의 ,2:하의, 3:치마, 4:원피스, 5:아우터)");
			System.out.print(">>");
			String category = sc.nextLine();
		    if(Integer.parseInt(category)>=1 && Integer.parseInt(category)<=5) {
		        list = productdao.selectProduct(query, category);
		        printAll(list, sc);
		        break;
		      }else {
		    	  System.out.println("잘못 입력하셨습니다.");
		      }
		}

	}
	//잘 팔리는 top 10 
	private void bestItem(Scanner sc) { 
		 List<Map<String,String>> list;
	      StringBuilder sb = new StringBuilder();
	      list = buydao.bestProduct();
	      
	      Iterator<Map<String, String>> itor = list.iterator();
	      sb.append("==============================================\n");
	      sb.append("                 상품명(팔린 수량)              \n" );
	      sb.append("==============================================\n");
	      while(itor.hasNext()) {
	         Map<String, String> map = itor.next();
	         sb.append(String.format("     %20s", map.get("P.PRODUCT_NAME")) );
	         sb.append(String.format(" (%s)\n", map.get("COUNT")));
	      }
	      System.out.println(sb.toString());
	      
	   }
	
	
	//---------------------------------출력--------------------------------
	//리스트 전체 출력 
	private void printAll(List<ProductVO> list,Scanner sc) {
		Iterator<ProductVO> ite = list.iterator();
		
		System.out.println("============================================================================================================");
		System.out.println(" 주문번호                   상품명                  사이즈         재고         가격          성별            종류       ");
		System.out.println("============================================================================================================");
		
		while(ite.hasNext()) {
			ProductVO pro = ite.next();
			
//			sb.append(String.format("%5s|%25s             %7s    %7d        %7s     %5s     %7s\n",pro.getProductId(), pro.getProductName(), pro.getProductSize(), pro.getStock(),
//					formatter.format(pro.getPrice())+"원",pro.getProductGender(),pro.getCategory() + "  \t"));
			System.out.printf("   %3s|%25s             %7s    %7d        %7s     %5s     %7s\n",pro.getProductId(), pro.getProductName(), pro.getProductSize(), pro.getStock(),
					formatter.format(pro.getPrice())+"원",pro.getProductGender(),pro.getCategory()); 
		}
	
		System.out.println("============================================================================================================");
		
		
			midMenu(sc);	

	}
	

	
	//구매 매소드 호출
	private void midMenu(Scanner sc) {
		//구매 , 장바구니담기 , 뒤로가기
		System.out.println("1.구매, 2.장바구니추가 3.상품평 보기 4.뒤로가기");
		String choice = sc.nextLine();
		switch(choice){
		case  "1" : 
			if(login != null) {
				purchase(sc); 
			}else {
				System.out.println("=========================");
				System.out.println("비회원이라 구매를 하실 수 없습니다.");
				System.out.println("회원가입 절차에 따라 가입하시고   ");
				System.out.println("구매를 진행해주시기 바랍니다.!!  ");
				System.out.println("=========================");
				System.out.println();
			}
			break;
		case  "2" : 
			if(login != null){
				createBskt(sc);
			}else {
				System.out.println("===================================");
				System.out.println("비회원이라 장바구니 추가를 진행 하실 수 없습니다.");
				System.out.println("회원가입 절차에 따라 가입하시고             ");
				System.out.println("장바구니 추가를 진행해주시기 바랍니다.!!      ");
				System.out.println("===================================");
				System.out.println();
				
			}
			break;
		case "3" : selectReview(sc);
			break;
		case "4" : break;
		default : System.out.println("잘못된 숫자를 입력하셨습니다"); break;
		}
		
	}
	//=====================구매====================
	
	private void purchase(Scanner sc) {
		String select =""; //상품 번호
		String select2 = ""; //상품 수량
		String select3 = ""; //포인트 사용여부
		
		int count = 0;
		int price = 0;
		int totalMoney = 0;
		
		while(!"0".equals(select)){
			if(login.getCardPayment() == 0) {
				System.out.println("카드에 돈이 없습니다.!!");
				System.out.println("돈을 입금해주세요.!!");
				break;
			}
			
			System.out.println("구매하실 상품의 번호를 입력해주세요 (0:뒤로가기)");
			System.out.print(">>");
			//카드 금액을 먼저 불러오고 결제가 가능한지 확인
			select = sc.nextLine();
			//번호를 입력하고 상품의 가격을 갖고와라...!
			if("0".equals(select)) {
				break;
			}
			price = productdao.selectPrice(select); //상품가격
			
			System.out.println("구매하실 상품의 수량을 입력해주세요 ");
			System.out.print(">>");
			select2 = sc.nextLine();
			count = Integer.parseInt(select2);
			//가격 * 개수 가 30000원 이하일 경우 배송비 30000원 추가
			
			totalMoney = price * count;
				
			
			if(totalMoney<30000) {
				System.out.println("30000원 이하일 경우 3000원의 배송비가 추가 됩니다");
				price += 3000; //30000원 이하일떄 배송비 추가
				
				System.out.println("정말로 구매하시겠습니까? (y/n)");
				System.out.print(">>");
				String pCheck = sc.nextLine();
				if("n".equalsIgnoreCase(pCheck)) {
					continue;
				}
				
			}
			int logMoney = login.getCardPayment(); 

			
			if(logMoney<totalMoney) {
				System.out.println("돈이 모자르니 돈을 충전해주세요 !!");
				break;
			}else {
				//재고를 받았잖아 근데 1개씩만 살 일이 없어 수량을 입력하면서 그 값이랑 비교 ! 그러니까 위에서
				// 수량도 입력받아야지
				
				if(point !=0) {
					
					System.out.println(">>현재 가지고 계신 포인트는" + point + "입니다.");
					System.out.println(">>포인트를 사용하시겠습니까?(y,n) ");
					select3 = sc.nextLine();
				
					if("y".equalsIgnoreCase(select3)) {
						totalMoney -= point;
						point = 0;

					}
				}
				
				int stock = productdao.stockView(select);
				
				stock -= count;
				//재고가 없을떄 출력
				
				if(stock<0) {
					System.out.println("현재 해당 제품의 재고가 부족합니다.");
					break;
				}
				
				//(결제가 가능)
				//3개가 동시에 처리 되야 하므로 트랜잭션 처리 재고에서 commit 수동처리 실시
				//사용자 돈 차감하고 돈이 들어감
				logMoney -= totalMoney;
				login.setCardPayment(logMoney);
				//관리자 입금
				int deposit = login2.getMoney();
				int result =  deposit + totalMoney;
				login2.setMoney(result);
				
				
				//해당 제품의 재고를 줄임 update
				int transec = productdao.updateNCStock(stock, select); // 커밋 x
				//구매내역에 추가 Insert
				
				int transec1 = buydao.insert(select, login.getId(),count); // 커밋 x
				//사용자 포인트 증가 메서드 호출
				point += (totalMoney*0.01)+2;
				int transec2 = memberdao.pointUpdate(point, login.getId()); //커밋 x 
				//관리자 돈 받는 메소드 호출
				int transec3 = managerdao.moneyModify(result, "admin"); //커밋 x
				
				
				
				// 트랜잭션 처리 4개의 단위를 묶어서 정상적으로 처리되는지 확인
				boolean check = memberdao.transection(transec, transec1, transec2, transec3);
				
				if(check) {
					System.out.println("구매가 정상적으로 진행되었습니다.");
				}else {
					System.out.println("구매중 오류가 발생했습니다.");
				}
				
			}
			
		}
		
	}
	

	//---------------------------장바구니 추가---------
	private void createBskt(Scanner sc) {
		//insert 인데 사용자 이름이랑 상품번호 보내야하거든.
		String select="";
		
		while(true){
			System.out.println(">>장바구니에 담을 상품의 번호를 입력해주세요 (0:뒤로가기)");
			select = sc.nextLine();
			if("0".equals(select)) {
				break;
			}
			//PRODUCT ID로 이름 갖고 오기 
			String name = productdao.selectName(select);
			//장바구니 추가
			cartdao.insert(login.getId(), select, name);
		
			
		}
	}

	//-------------------------------리뷰 조회(테스트 x)---------------------------
	
	private void selectReview(Scanner sc){
		List<Map<String,String>> list;
		
		System.out.println("리뷰를 보고 싶은 상품번호를 입력해주세요");
		System.out.print(">>");
		String reviewNum = sc.nextLine();
		//제품번호로 리뷰 전체를 검색할 수 있는 걸 만들어야 함 
		
		
		
		StringBuilder sb = new StringBuilder();
		list = reviewdao.selectOne(reviewNum);
		if(list.isEmpty()) {
			System.out.println("해당 상품의 리뷰가 존재하지 않습니다.");
		}else {
			Iterator<Map<String, String>> itor = list.iterator();
			sb.append("=============================================================================================\n");
			sb.append(" 리뷰번호           상품명            작성자                상품평                    구매일자         \n" );
			sb.append("=============================================================================================\n");
			while(itor.hasNext()) {
				Map<String, String> map = itor.next();
				sb.append(String.format("%5s", map.get("R.ID")) + "  \t");
				sb.append(String.format("%15s", map.get("P.PRODUCT_NAME")) + "  \t");
				sb.append(String.format("%5s", map.get("R.WRITER")) + "  \t");
				sb.append(String.format("%5s", map.get("R.CONTENT")) +"  \t");
				sb.append(String.format("%5s", map.get("R.W_DATE")) +"  \n");
			}
			System.out.println(sb.toString());
			System.out.println();
			
		

		}

	}
	

	// 아이디 패턴에 맞는 형식인지 구분 
	private String verification(String pattern, Scanner sc) {
		
		String value = sc.nextLine();
		
		
		while(!Pattern.matches(pattern, value)) {
		
			System.out.println("형식이 올바르지 않습니다.");
			System.out.print(">>다시 입력해주세요 :");
			value = sc.nextLine();
				
		}
		return value;
	}
	//매니저와 연락
	 
	private void managerCall() {

		LocalTime now = LocalTime.now();
		int hour = now.getHour();
		int minute = now.getMinute();
		int second = now.getSecond();
		
		if(hour==13) {
			System.out.println("================================");
			System.out.println("========= 13:00 ~ 14:00 ========");
			System.out.println("             점심시간             ");
			System.out.println("================================");
		}
		
		System.out.println("현재시간 :" + hour +":" + minute +":"+ second); 
		
		String adminPhone = managerdao.managerPhone("admin");
		System.out.println("연락처 :" + adminPhone   );
		System.out.println();
	 }
	
}
