package com.project.mainPage.dto;
import lombok.Data;
/*
+---------------+-------------+------+-----+---------+-------+
| Field         | Type        | Null | Key | Default | Extra |
+---------------+-------------+------+-----+---------+-------+
| category_id   | varchar(45) | NO   | PRI | NULL    |       |
| category_name | varchar(45) | NO   |     | NULL    |       |
+---------------+-------------+------+-----+---------+-------+  
*/
@Data
public class Category {
	private String category_id;
	private String category_name;
}