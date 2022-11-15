package com.project.mainPage.dto;
import lombok.Data;
/*
+-----------------+--------------+------+-----+---------+----------------+
| Field           | Type         | Null | Key | Default | Extra          |
+-----------------+--------------+------+-----+---------+----------------+
| board_prefer_no | int          | NO   | PRI | NULL    | auto_increment |
| board_no        | int          | NO   | MUL | NULL    |                |
| prefer          | tinyint(1)   | YES  |     | NULL    |                |
| user_id         | varchar(255) | NO   | MUL | NULL    |                |
+-----------------+--------------+------+-----+---------+----------------+
*/
@Data
public class BoardPrefer {
	private int board_prefer_no;
	private int board_no; // 댓글 번호 
	private boolean prefer; // 댓글 좋아요 : 1 (true), 싫어요 : 0 (false)
	private String user_id;	
}