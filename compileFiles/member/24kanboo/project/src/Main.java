import vo.Member;

public class Main {
	public static void main(String[] args) {
		Member mem = new Member();
      	mem.setName("임태희");
      	mem.setAge(10);
      	System.out.println(mem.getName() + "님 안녕하세요. " + mem.getAge() + "살 이시네요^^");
	}
}