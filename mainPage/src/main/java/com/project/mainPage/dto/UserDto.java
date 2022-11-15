package com.project.mainPage.dto;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
/*
+------------+--------------+------+-----+-------------------+-------------------+
| Field      | Type         | Null | Key | Default           | Extra             |
+------------+--------------+------+-----+-------------------+-------------------+
| user_id    | varchar(45)  | NO   | PRI | NULL              |                   |
| user_name  | varchar(45)  | NO   |     | NULL              |                   |
| user_pw    | varchar(45)  | NO   |     | NULL              |                   |
| user_phone | varchar(20)  | NO   | UNI | NULL              |                   |
| user_email | varchar(45)  | NO   | UNI | NULL              |                   |
| birth      | date         | NO   |     | NULL              |                   |
| adminCk    | int          | YES  |     | 0                 |                   |
| add1       | varchar(100) | NO   |     | NULL              |                   |
| add2       | varchar(100) | NO   |     | NULL              |                   |
| add3       | varchar(100) | YES  |     | NULL              |                   |
| add4       | varchar(100) | YES  |     | NULL              |                   |
| signup     | datetime     | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
+------------+--------------+------+-----+-------------------+-------------------+
*/
@Data
public class UserDto {
	private String user_id; // 회원 아이디
	private String user_name; // 회원 이름
	private String user_pw; // 회원 패스워드
	private String user_phone; // 회원 전화번호
 	private String user_email; // 회원 이메일
 	private String post_user_phone; // 수정할 회원 전화번호
 	private String post_user_email; // 수정할 회원 이메일
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birth; // 회원 생일
	private int adminCk; // 관리자 체크
	private String add1; // 회원 주소(우편번호)
	private String add2; // 회원 주소(주소)
	private String add3; // 회원 주소(상세 주소)
	private String add4; // 회원 주소(참고 항목)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date signup; //회원 가입일	
}