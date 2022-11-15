package com.project.mainPage.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.project.mainPage.dto.TourImg;
// com.project.mainPage.mapper.TourImgMapper
@Mapper
public interface TourImgMapper {
	// 관광지 이미지 등록 
	int insertOne(TourImg tourImg);
	// 관광지 이미지 수정
	List<TourImg> selectTourRank(int tourRank);
	TourImg selectOne(int tourImgNo);
	int selectCountTourRank(int tourRank);
	// 관광지 이미지 삭제 
	int deleteOne(int tourImgNo);
}