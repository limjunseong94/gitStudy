package com.project.mainPage.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.project.mainPage.dto.UserDto;
@Mapper
public interface UserMapper {
	List<UserDto> selectPageAll(
			int startRow, 
			int pageSize,
			@Param(value = "field")String field, 
			@Param(value = "search")String search,
			@Param(value = "sort")String sort, 
			@Param(value = "direct")String direct);
	int selectPageAllCount(
			@Param(value = "field")String field, 
			@Param(value = "search")String search,
			@Param(value = "sort")String sort, 
			@Param(value = "direct")String direct);
	
	UserDto selectIdPwOne(String userId, String userPw); // 로그인
	UserDto findId(String user_name, String user_email, String user_phone); // 아이디 찾기
	UserDto findPw(String user_id, String user_name, String user_email, String user_phone); // 비밀번호 찾기
	UserDto selectId(String userId); // 아이디 중복 검사
	UserDto selectPhone(String userPhone); // 전화번호 중복 검사
	UserDto selectEmail(String userEmail); // 이메일 중복 검사
	int deleteOne(String userId);
	int updateOne(UserDto user);
	int insertOne(UserDto user);
}