package com.project.mainPage.dto;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
/*
+-----------+--------------+------+-----+-------------------+-------------------+
| Field     | Type         | Null | Key | Default           | Extra             |
+-----------+--------------+------+-----+-------------------+-------------------+
| reply_no  | int          | NO   | PRI | NULL              | auto_increment    |
| contents  | varchar(255) | NO   |     | NULL              |                   |
| post_time | datetime     | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
| img_path  | varchar(255) | YES  |     | NULL              |                   |
| board_no  | int          | NO   | MUL | NULL              |                   |
| user_id   | varchar(255) | NO   | MUL | NULL              |                   |
| good      | int          | NO   |     | 0                 |                   |
| bad       | int          | NO   |     | 0                 |                   |
+-----------+--------------+------+-----+-------------------+-------------------+
*/
@Data
public class Reply {
	private int reply_no;
	private String contents;  // 댓글 내용 
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date post_time;   // 댓글 등록일 
	private String img_path;  // 댓글 이미지 
	private int board_no;
	private UserDto user;  // UsersDto userid : fk
	private int remove_img_check; // 댓글에서 이미지 체크하여 삭제할 수 있도록 하기 위해 추가
	private int good;
	private int bad;
	private Boolean prefer_active = null; // null : 누른 적이 없는, true : good를 누른 것, false: bad를 누른 것 
	private List<ReplyPrefer> good_prefers;
	private List<ReplyPrefer> bad_prefers;
}
