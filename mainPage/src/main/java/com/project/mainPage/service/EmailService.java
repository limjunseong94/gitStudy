package com.project.mainPage.service;
import com.project.mainPage.dto.UserDto;
import lombok.AllArgsConstructor;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
@AllArgsConstructor
public class EmailService {
	@Autowired
    private JavaMailSender mailSender;
    
//	이메일 보내는 메서드
    public void mailSend(UserDto user) {
    	String title = "JEJU THE BEST 비밀번호 찾기 결과"; // 제목
    	String text = user.getUser_id() + " 님의 비밀번호 : " + user.getUser_pw(); // 내용
    	
        SimpleMailMessage message = new SimpleMailMessage(); // 이메일 보내는 객체
        message.setTo(user.getUser_email());  // 이메일 받는 사람
        message.setFrom("kvpark98@naver.com"); // 이메일 보내는 사람
        message.setSubject(title); // 제목
        message.setText(text); // 내용

        mailSender.send(message); // 이메일 보내기
    }
    
//  이메일 인증번호 발송하는 객체
    public String sendEmailConfirm(String userEmail) throws Exception {
		
    	Random random = new Random();
		String key = "";

		// 입력 키를 위한 코드
		for (int i = 0; i < 3; i++) {
			int index = random.nextInt(25) + 65; // A~Z까지 랜덤 알파벳 생성
			key += (char) index;
		}
		int numIndex = random.nextInt(8999) + 1000; // 4자리 정수를 생성
		key += numIndex;

		SimpleMailMessage message = new SimpleMailMessage(); // 이메일 보내는 객체
		message.setTo(userEmail);  // 이메일 받는 사람
		message.setFrom("kvpark98@naver.com"); // 이메일 보내는 사람
		message.setSubject("JEJU THE BEST 이메일 인증"); // 제목
		message.setText("인증번호 : " + key); // 내용
		
		mailSender.send(message); // 이메일 보내기
		
		return key;
	}
}