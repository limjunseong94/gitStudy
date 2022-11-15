package com.project.mainPage.dto;
import lombok.Data;
/*
+-----------------+--------------+------+-----+---------+----------------+
| Field           | Type         | Null | Key | Default | Extra          |
+-----------------+--------------+------+-----+---------+----------------+
| reply_prefer_no | int          | NO   | PRI | NULL    | auto_increment |
| reply_no        | int          | NO   | MUL | NULL    |                |
| prefer          | tinyint(1)   | YES  |     | NULL    |                |
| user_id         | varchar(255) | NO   | MUL | NULL    |                |
+-----------------+--------------+------+-----+---------+----------------+
*/
@Data
public class ReplyPrefer {
	private int reply_prefer_no; 
	private int reply_no; // 댓글 번호
	private boolean prefer; // 좋아요 : 1, 싫어요 : 0 
	private String user_id;		
}
