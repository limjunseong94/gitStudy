package com.project.mainPage.dto;
import lombok.Data;
/*
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| tour_img_no | int          | NO   | PRI | NULL    | auto_increment |
| tour_rank   | int          | NO   | MUL | NULL    |                |
| img_path    | varchar(255) | NO   |     | NULL    |                |
+-------------+--------------+------+-----+---------+----------------+
*/
@Data
public class TourImg {
	private int tour_img_no;
	private int tour_rank;
	private String img_path;
}