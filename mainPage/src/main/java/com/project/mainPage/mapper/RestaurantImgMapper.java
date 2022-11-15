package com.project.mainPage.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.project.mainPage.dto.RestaurantImg;
// com.project.mainPage.mapper.RestaurantImgMapper
@Mapper
public interface RestaurantImgMapper {
	// 음식점 이미지 등록 
	int insertOne(RestaurantImg restaurantImg);
	// 음식점 수정 
	int selectCountRestRank(int restRank);
	RestaurantImg selectOne(int restaurankImgNo);
	// 음식점 삭제 
	int deleteOne(int restaurankImgNo);
	List<RestaurantImg> selectRestRank(int restRank);
}