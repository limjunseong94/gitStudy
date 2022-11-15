package com.project.mainPage.dto;
import lombok.Data;
/*
+-------------------+--------------+------+-----+---------+----------------+
| Field             | Type         | Null | Key | Default | Extra          |
+-------------------+--------------+------+-----+---------+----------------+
| restaurank_img_no | int          | NO   | PRI | NULL    | auto_increment |
| rest_rank         | int          | NO   | MUL | NULL    |                |
| img_path          | varchar(255) | NO   |     | NULL    |                |
+-------------------+--------------+------+-----+---------+----------------+
*/
@Data
public class RestaurantImg {
	private int restaurank_img_no;
	private int rest_rank;
	private String img_path;
}