package com.project.mainPage.service;
import java.util.HashMap;
import java.util.Random;
import org.springframework.stereotype.Service;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
@Service
public class MessageService {
	public String PhoneNumberCheck(String userPhone) throws CoolsmsException {
		String api_key = "NCSKR4WQQA5LDNHC";
		String api_secret = "VYJECVKCLCUD6PQKGEOKRMTYMMEA2MRU";
		Message coolsms = new Message(api_key, api_secret);
		
		Random rand  = new Random();
	    String key = "";
	    for(int i = 0; i < 4; i++) {
	       String random = Integer.toString(rand.nextInt(10));
	       key += random;
	    }          
	
	    HashMap<String, String> params = new HashMap<String, String>();
	    params.put("to", userPhone); // 수신 전화번호 (ajax로 view 화면에서 받아 온 값으로 넘김)
	    params.put("from", "010-6737-3912"); // 발신 전화번호. 테스트 시에는 발신, 수신 둘 다 본인 번호로 하면 됨
	    params.put("type", "sms"); 
	    params.put("text", "인증번호는 [" + key + "] 입니다.");
	
	    coolsms.send(params); // 메시지 전송
	        
	    return key; // 인증번호 반환
	}
}