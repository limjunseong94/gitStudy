package com.project.mainPage.dto;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
/*
+-------------+--------------+------+-----+-------------------+-------------------+
| Field       | Type         | Null | Key | Default           | Extra             |
+-------------+--------------+------+-----+-------------------+-------------------+
| board_no    | int          | NO   | PRI | NULL              | auto_increment    |
| place_name  | varchar(45)  | NO   |     | NULL              |                   |
| rating      | float        | NO   |     | NULL              |                   |
| title       | varchar(255) | NO   |     | NULL              |                   |
| post_time   | datetime     | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
| contents    | text         | NO   |     | NULL              |                   |
| address1    | varchar(255) | YES  | MUL | NULL              |                   |
| address2    | varchar(255) | YES  | MUL | NULL              |                   |
| address3    | varchar(255) | YES  | MUL | NULL              |                   |
| views       | int          | NO   |     | 0                 |                   |
| good        | int          | NO   |     | 0                 |                   |
| bad         | int          | NO   |     | 0                 |                   |
| user_id     | varchar(45)  | NO   | MUL | NULL              |                   |
| category_id | varchar(45)  | NO   | MUL | NULL              |                   |
+-------------+--------------+------+-----+-------------------+-------------------+
*/
@Data
public class Board {
	private int board_no;
	private String place_name;
	private float rating;
	private String title;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date post_time;
	private String contents;
	private String address1;
	private String address2;
	private String address3;
	private int good;
	private int bad;
	private int views;
	private UserDto user; // UsersDto.userid : fk 
	private Category category;
	private String category_id;
	private int reply_size;
	private List<Reply> replys; // 1:N Reply.board_no  : fk
	private List<BoardImg> boardImgs; // 1:N  BOARD_IMG.board_no fk
	private List<BoardPrefer> good_Prefers;
	private Boolean prefer_active = null; 	
}