package com.project.mainPage.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.project.mainPage.dto.Notice;
// com.project.mainPage.mapper.NoticeMapper
@Mapper
public interface NoticeMapper {
	List<Notice> selectPageAll(
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
	Notice selectDetailOne(int noticeNo);
	int updateViews(int noticeNo);
	int insertOne(Notice notice);
	int updateOne(Notice notice);
	int deleteOne(int noticeNo);
}