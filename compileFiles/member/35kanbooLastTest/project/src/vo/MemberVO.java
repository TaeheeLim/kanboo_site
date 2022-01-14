package vo;

public class MemberVO {
	private Long memIdx;
  	private String memNm;
  
  	public void setMemIdx(Long memIdx) {
    	this.memIdx = memIdx;
    }
  
  	public void setMemNm(String memNm) {
      	this.memNm = memNm;
    }
  
  	public Long getMemIdx() {
    	return memIdx;
    }
  
  	public String getMemNm() {
    	return memNm;
    }
}