package com.project.mainPage.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.project.mainPage.dto.Tour;
// com.project.mainPage.mapper.TourMapper
@Mapper
public interface TourMapper {
	// 리스트
	List<Tour> selectListAll(int startRow, int pageSize);
	int selectPageAllCount();
	// 상세 
	Tour selectDetailOne(Integer tourRank);
	// 조회수 
	int updateViews(int tourRank);
	// 등록
	int insertOne(Tour tour);
	// 수정
	int updateOne(Tour tour);
	// 삭제 
	int deleteOne(int tourRank);
	// 메인 화면에 출력
	List<Tour> mainPageTour();
	// 관광지 추천
	List<Tour> selectRecommendation(
			@Param(value = "city") String city);
}