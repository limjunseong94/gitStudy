package com.project.mainPage.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.project.mainPage.dto.Board;
// com.project.mainPage.mapper.BoardMapper
@Mapper
public interface BoardMapper {
	List<Board> selectPageAll(
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
	Board selectDetailOneAll(int boardNo);
	int detailUpdateViews(int boardNo);
	int deleteOne(int boardNo);
	int insertOne(Board board);
	int updateOne(Board board);
	Board selectOne(int boardNo);
	Board selectOne(int boardNo, String loginUsersId);
}