import vo.MemberVO;

public class Main {
	public static void main(String[] args) {
		System.out.println("Hello World!");
      	MemberVO vo = new MemberVO();
      
      	vo.setMemIdx(123L);
      	vo.setMemNm("Kade");
      
      	System.out.println(vo.getMemNm() + "님 안녕하세요.");
      	System.out.println(vo.getMemIdx() + "번 아이디를 가지고 있습니다.");
	}
}