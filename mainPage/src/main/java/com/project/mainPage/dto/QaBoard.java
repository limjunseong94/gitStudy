package com.project.mainPage.dto;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
/*
+-----------------+--------------+------+-----+-------------------+-------------------+
| Field           | Type         | Null | Key | Default           | Extra             |
+-----------------+--------------+------+-----+-------------------+-------------------+
| qaBoardNo       | int          | NO   | PRI | NULL              | auto_increment    |
| qaBoardTitle    | varchar(255) | NO   |     | NULL              |                   |
| qaBoardContents | text         | YES  |     | NULL              |                   |
| qaBoardAnswer   | tinyint(1)   | YES  |     | 0                 |                   |
| qaBoardDate     | datetime     | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
| category_id     | varchar(45)  | NO   | MUL | NULL              |                   |
| user_id         | varchar(45)  | NO   | MUL | NULL              |                   |
| views           | int          | NO   |     | 0                 |                   |
+-----------------+--------------+------+-----+-------------------+-------------------+
*/
@Data
public class QaBoard {
	private int qaBoardNo;
	private String qaBoardTitle;
	private String qaBoardContents;
	private int qaBoardAnswer;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date qaBoardDate;	
	private Category category;
	private String category_id;
	private UserDto user;  // fk 
	private int views;
	private QaReply qaReply;
}
