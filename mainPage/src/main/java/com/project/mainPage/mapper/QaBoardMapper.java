package com.project.mainPage.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.project.mainPage.dto.QaBoard;
// com.project.mainPage.mapper.QaBoardMapper
@Mapper
public interface QaBoardMapper {
	List<QaBoard> selectPageAll(
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
	QaBoard selectOne(int qaBoardNo);
	int insertOne(QaBoard qaBoard);
	int updateOne(QaBoard qaBoard);
	int deleteOne(int qaBoardNo);
	int answerOne(QaBoard qaBoard);
	int detailUpdateViews(int qaBoardNo);
}