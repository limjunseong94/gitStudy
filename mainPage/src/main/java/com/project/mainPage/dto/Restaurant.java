package com.project.mainPage.dto;
import java.util.List;
import lombok.Data;
/*
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| rest_rank   | int          | NO   | PRI | NULL    | auto_increment |
| tourist     | varchar(255) | NO   |     | NULL    |                |
| province    | varchar(255) | NO   |     | NULL    |                |
| city        | varchar(255) | NO   |     | NULL    |                |
| address2    | varchar(255) | NO   |     | NULL    |                |
| rest        | varchar(255) | YES  |     | NULL    |                |
| search      | int          | NO   |     | NULL    |                |
| views       | int          | NO   |     | 0       |                |
| rest_phone  | varchar(20)  | YES  |     | NULL    |                |
| contents    | text         | YES  |     | NULL    |                |
| user_id     | varchar(45)  | NO   | MUL | NULL    |                |
| category_id | varchar(255) | YES  | MUL | NULL    |                |
| taste       | float        | YES  |     | 0       |                |
| price       | float        | YES  |     | 0       |                |
| location    | float        | YES  |     | 0       |                |
| service     | float        | YES  |     | 0       |                |
| mood        | float        | YES  |     | 0       |                |
+-------------+--------------+------+-----+---------+----------------+
*/
@Data
public class Restaurant {
	private int rest_rank;
	private String tourist;
	private String province;
	private String city;
	private String address2;
	private int views;
	private int search;
	private int ranking;	
	private String rest_phone;
	private String contents;
	private String img_path;
	private float taste;
	private float price;
	private float location;
	private float service;
	private float mood;
	private UserDto user; // UsersDto.user_id : fk 
	private Category category;
	private List<RestaurantImg> restaurantImgs;
}