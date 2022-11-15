package com.project.mainPage.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.project.mainPage.dto.AccoImg;
// com.project.mainPage.mapper.TourImgMapper
@Mapper
public interface AccoImgMapper {
	int insertOne(AccoImg accoImg);
	List<AccoImg> selectAccoRank(int accoRank);
	AccoImg selectOne(int accoImgNo);
	int deleteOne(int accoImgNo);
	int selectCountAccoRank(int accoRank);
}
