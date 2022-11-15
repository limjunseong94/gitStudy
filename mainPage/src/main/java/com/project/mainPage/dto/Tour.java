package com.project.mainPage.dto;
import java.util.List;
import lombok.Data;
/*
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| tour_rank   | int          | NO   | PRI | NULL    | auto_increment |
| tourist     | varchar(255) | NO   |     | NULL    |                |
| province    | varchar(255) | NO   |     | NULL    |                |
| city        | varchar(255) | NO   |     | NULL    |                |
| address3    | varchar(255) | NO   |     | NULL    |                |
| category_id | varchar(255) | YES  | MUL | NULL    |                |
| search      | int          | NO   |     | NULL    |                |
| views       | int          | NO   |     | 0       |                |
| tour_phone  | varchar(20)  | YES  |     | NULL    |                |
| contents    | text         | YES  |     | NULL    |                |
| user_id     | varchar(45)  | NO   | MUL | NULL    |                |
+-------------+--------------+------+-----+---------+----------------+
*/
@Data
public class Tour {
	 private int tour_rank;
	 private String tourist;
	 private String province;
	 private String city;
	 private String address3;
	 private String tour_phone;
	 private String contents;
	 private Category category;
	 private int views;
	 private int search;
	 private int ranking;
	 private String img_path;
	 private UserDto user; // UsersDto.user_id : fk 
	 private List<TourImg> tourImgs; // 1:N  TourImg.tour_rank fk
}