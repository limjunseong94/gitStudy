package com.project.mainPage.service;
import java.io.File;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.mainPage.dto.Restaurant;
import com.project.mainPage.dto.RestaurantImg;
import com.project.mainPage.mapper.RestaurankMapper;
import com.project.mainPage.mapper.RestaurantImgMapper;
@Service
public class RestaurantService {
	@Autowired
	private RestaurankMapper restaurankMapper;
	
	@Autowired
	private RestaurantImgMapper restaurantImgMapper; 
	
	@Value("${spring.servlet.multipart.location}")
	String savePath;
	
	// 음식점 리스트
	public Restaurant restUpdateView(Integer restRank) throws Exception {
		restaurankMapper.updateViews(restRank);
		return restaurankMapper.selectDetailOne(restRank);
	}
	
	//음식점 등록
	@Transactional
	public int registRest(Restaurant restaurant) throws Exception {
		int regist = 0;
		int imgRegist = 0;
		regist = restaurankMapper.insertOne(restaurant);
		if(regist > 0 && restaurant.getRestaurantImgs() != null) {
			for(RestaurantImg restaurantImg : restaurant.getRestaurantImgs()) {
				restaurantImg.setRest_rank(restaurant.getRest_rank()); // Auto Increment로 저장된 대표키 값
				imgRegist += restaurantImgMapper.insertOne(restaurantImg); // DB에 이미지 저장
			}
		}
		System.out.println("음식점 이미지 등록 성공! : " + imgRegist);
		return regist;
	}
		
	// 음식점 수정
	@Transactional
	public int updateRestRemoveRestImg(Restaurant restaurant, int [] restaurantImgNos) throws Exception {
		int update = 0;
		// 기존 이미지 삭제 
		if(restaurantImgNos != null) {
			for(int no : restaurantImgNos) {
				RestaurantImg restaurantImg = restaurantImgMapper.selectOne(no);
				
				File f = new File(savePath + "/" + restaurantImg.getImg_path());
				System.out.println("rest의 이미지 파일(서버) 삭제 성공! : " + f.delete());
				
				int removeRestImg = restaurantImgMapper.deleteOne(no);
				System.out.println("rest의 rest_img(DB) 삭제 성공! : " + removeRestImg);
			}
		}
		// 새로운 이미지 등록
		if(restaurant.getRestaurantImgs() != null) { // 이미지가 1개 이상 저장되면 null이 아니다.
			for(RestaurantImg restaurantImg : restaurant.getRestaurantImgs()) {
				int registTourImg = restaurantImgMapper.insertOne(restaurantImg); // DB에 이미지 저장
				System.out.println("rest의 rest_img(DB) 등록 성공! : " + registTourImg);
			}
		}
		update = restaurankMapper.updateOne(restaurant); 
		System.out.println("service update : " + update);
		return update;
	}
		
	// 음식점 삭제 
	public int removeRest(int restRank) throws Exception {
		int remove = 0;
		List<RestaurantImg> restaurantImgs = restaurantImgMapper.selectRestRank(restRank);
		if(restaurantImgs != null) {
			restaurantImgs.stream()
				.map(RestaurantImg :: getImg_path)
				.forEach((img) -> {
					File f = new File(savePath + "/" + img);
					System.out.println("음식점 이미지 삭제 : " + f.delete());
				});
		}
		remove = restaurankMapper.deleteOne(restRank);
		return remove;
	}
}