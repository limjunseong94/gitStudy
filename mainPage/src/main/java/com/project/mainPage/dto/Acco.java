package com.project.mainPage.dto;
import java.util.List;
import lombok.Data;
/*
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| acco_rank   | int          | NO   | PRI | NULL    | auto_increment |
| tourist     | varchar(255) | NO   |     | NULL    |                |
| province    | varchar(255) | NO   |     | NULL    |                |
| city        | varchar(255) | NO   |     | NULL    |                |
| address1    | varchar(255) | NO   |     | NULL    |                |
| search      | int          | NO   |     | NULL    |                |
| views       | int          | NO   |     | 0       |                |
| acco_phone  | varchar(20)  | YES  |     | NULL    |                |
| contents    | text         | YES  |     | NULL    |                |
| user_id     | varchar(45)  | NO   | MUL | NULL    |                |
| category_id | varchar(255) | YES  | MUL | NULL    |                |
| price       | float        | YES  |     | 0       |                |
| location    | float        | YES  |     | 0       |                |
| service     | float        | YES  |     | 0       |                |
| room        | float        | YES  |     | 0       |                |
| clean       | float        | YES  |     | 0       |                |
| bedding     | float        | YES  |     | 0       |                |
+-------------+--------------+------+-----+---------+----------------+
*/
@Data
public class Acco {
	 private int acco_rank;
	 private String tourist;
	 private String province;
	 private String city;
	 private String address1;
	 private String acco_phone;
	 private String contents;
	 private Category category;
	 private int views;
	 private int search;
	 private int ranking;
	 private String img_path;
	 private float price;
	 private float location;
	 private float service;
	 private float room;
	 private float clean;
	 private float bedding;
	 private UserDto user; // UsersDto.userid : fk 
	 private List<AccoImg> accoImgs; // 1:N  TourImg.tour_rank fk
}