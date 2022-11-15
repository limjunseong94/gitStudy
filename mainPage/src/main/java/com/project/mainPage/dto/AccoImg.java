package com.project.mainPage.dto;
import lombok.Data;
/*
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| acco_img_no | int          | NO   | PRI | NULL    | auto_increment |
| acco_rank   | int          | NO   | MUL | NULL    |                |
| img_path    | varchar(255) | NO   |     | NULL    |                |
+-------------+--------------+------+-----+---------+----------------+
*/
@Data
public class AccoImg {
	private int acco_img_no;
	private int acco_rank;
	private String img_path;
}