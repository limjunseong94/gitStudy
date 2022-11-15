package com.project.mainPage.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.project.mainPage.dto.Acco;
import com.project.mainPage.dto.Restaurant;
import com.project.mainPage.dto.Tour;
import com.project.mainPage.mapper.AccoMapper;
import com.project.mainPage.mapper.RestaurankMapper;
import com.project.mainPage.mapper.TourMapper;
@Controller
public class MainController {
	@Autowired
	private TourMapper tourMapper;
	
	@Autowired
	private RestaurankMapper restaurankMapper;
	
	@Autowired
	private AccoMapper accoMapper;
	
//	홈 화면
	@GetMapping("/")
	public String index(Model model) {
		try {
			List<Tour> tourList = tourMapper.mainPageTour();
			List<Acco> accoList = accoMapper.mainPageAcco();
			List<Restaurant> restList = restaurankMapper.mainPageRest();
			
			model.addAttribute("tourList", tourList);
			model.addAttribute("accoList", accoList);
			model.addAttribute("restList", restList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}
	
//	나의 여행 추천
	@GetMapping("/recommendation")
	public String recommendation(
			@RequestParam(required = false) String city, // 지역
			@RequestParam(required = false) String acco, // 숙박 최애포인트
			@RequestParam(required = false) String rest, // 음식점 최애포인트
			Model model) {
		try {
			List<Tour> tourList = tourMapper.selectRecommendation(city); // 관광지 추천
			List<Acco> accoList = accoMapper.selectRecommendation(city, acco); // 숙박 추천
			List<Restaurant> restList = restaurankMapper.selectRecommendation(city, rest); // 음식점 추천
			
			model.addAttribute("tourList", tourList);
			model.addAttribute("accoList", accoList);
			model.addAttribute("restList", restList);
			
			System.out.println("관광지 추천 결과 : " + tourList);
			System.out.println("숙박 추천 결과 : " + accoList);
			System.out.println("음식점 추천 결과 : " + restList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "recommendation";
	}
}