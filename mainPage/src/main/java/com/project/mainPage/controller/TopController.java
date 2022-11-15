package com.project.mainPage.controller;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import com.project.mainPage.dto.Pagination;
import com.project.mainPage.dto.Restaurant;
import com.project.mainPage.dto.RestaurantImg;
import com.project.mainPage.dto.Tour;
import com.project.mainPage.dto.TourImg;
import com.project.mainPage.dto.UserDto;
import com.project.mainPage.mapper.RestaurankMapper;
import com.project.mainPage.mapper.RestaurantImgMapper;
import com.project.mainPage.mapper.TourImgMapper;
import com.project.mainPage.mapper.TourMapper;
import com.project.mainPage.service.RestaurantService;
import com.project.mainPage.service.TourService;
import com.project.mainPage.dto.Acco;
import com.project.mainPage.dto.AccoImg;
import com.project.mainPage.mapper.AccoImgMapper;
import com.project.mainPage.mapper.AccoMapper;
import com.project.mainPage.service.AccoService;
@Controller
@RequestMapping("/top")
public class TopController {

	@Autowired
	private TourMapper tourMapper;
	
	@Autowired
	private TourService tourService;
	
	@Autowired
	private TourImgMapper tourImgMapper;
	
	@Value("${spring.servlet.multipart.location}") // 파일이 임시 저장되는 경로 + 파일을 저장할 경로
	private String savePath;
	
	@Autowired
	private RestaurankMapper restaurankMapper;
	
	@Autowired
	private RestaurantService restaurantService;
	
	@Autowired
	private RestaurantImgMapper restaurantImgMapper; 
	
	@Autowired
	private AccoMapper accoMapper;
	
	@Autowired
	private AccoService accoService;
	
	@Autowired
	private AccoImgMapper accoImgMapper;

	private final static int ACCO_IMG_LIMIT = 5;

	// Tour > tour_img 의 수를 5개로 제한 
	private final static int TOUR_IMG_LIMIT = 5; 
	
	// RESTAURANK > RESTAURANK_IMG 의 수를 5개로 제한 
	private final static int RESTAURANK_IMG_LIMIT = 5; 

	// 관광지 리스트
	@GetMapping("/tour/list/{page}")
	public String tour(
			@PathVariable int page,
			Model model){
		int row = 10;
		int startRow = (page - 1) * row;
		List<Tour> tourList = tourMapper.selectListAll(startRow, row);
		int count = tourMapper.selectPageAllCount();
		Pagination pagination = new Pagination(page, count, "/top/tour/list/", row);
		model.addAttribute("pagination", pagination);
		model.addAttribute("tourList", tourList);	
		model.addAttribute("row", row);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		return "/top/tour/list";
	}
	
	// 관광지 상세
	@GetMapping("/tour/detail/{tourRank}") 
	public String detail(
			@PathVariable Integer tourRank,
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model
			) {
		Tour tour = null; 
		try {
			tour = tourMapper.selectDetailOne(tourRank);
			
			// 관광지 조회수 로직
			Cookie oldCookie = null; // oldCookie 객체를 선언한 후 빈 값으로 초기화
			Cookie[] cookies = req.getCookies(); // request 객체에서 쿠키들을 가져와 Cookie 타입을 요소로 가지는 리스트에 담기
			
			if (cookies != null) { // 접속한 기록이 있을 때
				for (Cookie cookie : cookies) { // 반복문으로 하나하나 검사
					if (cookie.getName().equals("tourViews")) { // 쿠키의 이름이 tourViews인지 확인 
						oldCookie = cookie; // 맞으면 oldCookie에 해당 쿠키를 저장 
					}
				}
			}
			if (oldCookie != null) { // 이름이 tourViews인 쿠키가 있을 때
				if (!oldCookie.getValue().contains("["+ tourRank.toString() +"]")) { // 특정 관광지 아이디가 oldCookie에 포함되어 있지 않을 때 (이미 포함되어 있다면 조회수 올라가지 않음)
					this.tourService.tourUpdateView(tourRank); // 조회수 올리기
					oldCookie.setValue(oldCookie.getValue() + "_[" + tourRank + "]"); // 조회한 관광지 아이디 oldCookie에 저장
					oldCookie.setPath("/"); // 쿠키 경로 저장
					oldCookie.setMaxAge(60 * 60 * 24); // 쿠키 지속 시간 저장
					resp.addCookie(oldCookie); // response에 oldCookie를 전달
				}
			} else { // 이름이 tourViews인 쿠키가 없을 때
				this.tourService.tourUpdateView(tourRank); // 조회수 올리기
				Cookie newCookie = new Cookie("tourViews", "[" + tourRank + "]"); // tourViews라는 이름으로 쿠키를 만들고 조회한 관광지 아이디 저장
				newCookie.setPath("/"); // 쿠키 경로 저장
				newCookie.setMaxAge(60 * 60 * 24); // 쿠키 지속 시간 저장
				resp.addCookie(newCookie); // response에 newCookie를 전달
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(tour != null) {
			model.addAttribute(tour);
			return "/top/tour/detail";
		} else {
			return "redirect:/top/tour/list/1";	
		}
	}
	
	// 관광지 등록 페이지(관리자)
	@GetMapping("/tour/insert.do")
	public String insert(
			@SessionAttribute(required = false) UserDto loginUser
			) {
		if(loginUser.getAdminCk() == 1) {
			return "/top/tour/insert";
		} else {
			return "redirect:/user/login.do";
		}
	}
	
    // 관광지 등록(관리자)
	@PostMapping("/tour/insert.do")
	public String insert(
			Tour  tour,
			@RequestParam(name = "imgFile", required = false) MultipartFile [] imgFiles,
			@SessionAttribute(required = false) UserDto loginUser,
			HttpSession session) {
		int insert = 0;
		try {
			//이미지 저장 및 처리
			if(imgFiles != null) {
				List<TourImg> tourImgs = new ArrayList<TourImg>();
				// imgFiles가 null이면 여기서 오류 발생!! 
				for(MultipartFile imgFile : imgFiles) {		
					String type = imgFile.getContentType();
					if(type.split("/")[0].equals("image")) {
						// 새로운 이미지 등록 
						String newFileName = "tour_" + System.nanoTime() + "." + type.split("/")[1]; // {"image", "jpeg"}
						Path newFilePath = Paths.get(savePath + "/" + newFileName);
						imgFile.transferTo(newFilePath); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
						
						TourImg tourImg = new TourImg();
						tourImg.setImg_path(newFileName); 
						tourImgs.add(tourImg);
					}
				}
				if(tourImgs.size() > 0) {
					tour.setTourImgs(tourImgs);
				}
			}
			insert = tourService.registTour(tour); // DB에 관광지 등록
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(insert > 0) {
			System.out.println("관광지 등록 성공! : " + insert);
			return "redirect:/top/tour/list/1";
		} else {
			System.out.println("관광지 등록 실패! : " + insert);
			return "redirect:/top/tour/insert.do";
		}
	}
	
	// 관광지 수정 페이지(관리자)
	@GetMapping("/tour/update/{tourRank}")
	public String update(
			@PathVariable int tourRank, 
			Model model, 
			@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
			HttpSession session) {
		Tour tour = null;
		tour = tourMapper.selectDetailOne(tourRank);
		if(loginUser.getAdminCk() == 1) {
			model.addAttribute("tour", tour);
			return "/top/tour/update";			
		} else {
			return "redirect:/user/login.do";
		}
	}
	
	// 관광지 수정(관리자)
	@PostMapping("/tour/update.do")
	public String update(
			Tour tour,
			Model model,
			@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
			@RequestParam(name="tourImgNo", required = false ) int [] tourImgNos,
			@RequestParam(name = "imgFile", required = false) MultipartFile[] imgFiles,
			HttpSession session
			) {
		int update = 0; 
		if((loginUser).getAdminCk() == 1) {
			try {
				int tourImgCount = tourImgMapper.selectCountTourRank(tour.getTour_rank());
				int insertTourImgLength = TOUR_IMG_LIMIT - tourImgCount + ((tourImgNos != null) ? tourImgNos.length : 0);
				// 이미지 저장 
				if(imgFiles != null && insertTourImgLength > 0) {
					List<TourImg> tourImgs = new ArrayList<TourImg>();
					for(MultipartFile imgFile : imgFiles) { 
						String[] types = imgFile.getContentType().split("/");
						if(types[0].equals("image")) {
							// 새로운 이미지 등록 
							String newFileName = "tour_" + System.nanoTime() + "." + types[1];
							Path path = Paths.get(savePath + "/" + newFileName);
							imgFile.transferTo(path); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
							
							TourImg tourImg = new TourImg();
							tourImg.setTour_rank(tour.getTour_rank()); 
							tourImg.setImg_path(newFileName);
							tourImgs.add(tourImg);
							
							if(-- insertTourImgLength == 0) break; // 이미지 수가 5개면 반목문 종료 
						}
					}
					if(tourImgs.size() > 0) {
						tour.setTourImgs(tourImgs);
					}
				}
				update = tourService.updateTourRemoveTourImg(tour, tourImgNos); // DB에서 후기 수정
			} catch (Exception e) {
				e.printStackTrace();
				return "redirect:/top/tour/update/" + tour.getTour_rank();
			}
			if(update > 0) {
				System.out.println("관광지 수정 성공! : " + update);
				return "redirect:/top/tour/detail/" + tour.getTour_rank();
			} else {
				System.out.println("관광지 수정 실패! : " + update);
				return "redirect:/top/tour/update/" + tour.getTour_rank();
			}	
		} else{ 
			return "redirect:/user/login.do";
		}  
	}
	
	// 관광지 삭제(관리자)
	@SuppressWarnings("null")
	@GetMapping("/tour/delete/{tourRank}/{userId}")
	public String delete(
			@PathVariable int tourRank,
			@PathVariable String userId,
			@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
			HttpSession session
			) {
		String msg = "";
		if(loginUser != null || loginUser.getAdminCk() == 1) {
			int delete = 0;
			try {
				delete = tourService.removeTour(tourRank);
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(delete > 0) {
				msg = "관광지 삭제 성공";
				session.setAttribute("msg", msg);
				return "redirect:/top/tour/list/1";
			} else {
				msg = "관광지 삭제 실패";
				session.setAttribute("msg", msg);
				return "redirect:/top/tour/update/" + tourRank;			
			}	
		} else {
			msg = "로그인 하셔야 이용 가능합니다.";
			session.setAttribute("msg", msg);
			return "redirect:/user/login.do";
		}
	}
	
	// 음식점 리스트
	@GetMapping("/rest/list/{page}")
	public String restlist(
			@PathVariable int page,
			Model model){
		int row = 10;
		int startRow = (page - 1) * row;
		List<Restaurant> restaurantsList = restaurankMapper.selectListAll(startRow, row);
		int count = restaurankMapper.selectPageAllCount();
		Pagination pagination = new Pagination(page, count, "/top/rest/list/", row);
		model.addAttribute("pagination", pagination);
		model.addAttribute("restaurantsList", restaurantsList);	
		model.addAttribute("row", row);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		return "/top/rest/list";
	}
			
	// 음식점 상세
	@GetMapping("/rest/detail/{restRank}")
	public String restdetail(
			@PathVariable Integer restRank,
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model
			) {
		Restaurant restaurant = null; 
		try {
			restaurant = restaurankMapper.selectDetailOne(restRank);
			
			// 음식점 조회수 로직
			Cookie oldCookie = null; // oldCookie 객체를 선언한 후 빈 값으로 초기화
			Cookie[] cookies = req.getCookies(); // request 객체에서 쿠키들을 가져와 Cookie 타입을 요소로 가지는 리스트에 담기
			
			if (cookies != null) { // 접속한 기록이 있을 때
				for (Cookie cookie : cookies) { // 반복문으로 하나하나 검사
					if (cookie.getName().equals("restViews")) { // 쿠키의 이름이 restViews인지 확인 
						oldCookie = cookie; // 맞으면 oldCookie에 해당 쿠키를 저장 
					}
				}
			}
			if (oldCookie != null) { // 이름이 restViews인 쿠키가 있을 때
				if (!oldCookie.getValue().contains("["+ restRank.toString() +"]")) { // 특정 음식점 아이디가 oldCookie에 포함되어 있지 않을 때 (이미 포함되어 있다면 조회수 올라가지 않음)
					this.restaurantService.restUpdateView(restRank); // 조회수 올리기
					oldCookie.setValue(oldCookie.getValue() + "_[" + restRank + "]"); // 조회한 음식점 아이디 oldCookie에 저장
					oldCookie.setPath("/"); // 쿠키 경로 저장
					oldCookie.setMaxAge(60 * 60 * 24); // 쿠키 지속 시간 저장
					resp.addCookie(oldCookie); // response에 oldCookie를 전달
				}
			} else { // 이름이 restViews인 쿠키가 없을 때
				this.restaurantService.restUpdateView(restRank); // 조회수 올리기
				Cookie newCookie = new Cookie("restViews", "[" + restRank + "]"); // restViews라는 이름으로 쿠키를 만들고 조회한 음식점 아이디 저장
				newCookie.setPath("/"); // 쿠키 경로 저장
				newCookie.setMaxAge(60 * 60 * 24); // 쿠키 지속 시간 저장
				resp.addCookie(newCookie); // response에 newCookie를 전달
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(restaurant != null) {
			model.addAttribute("restaurant", restaurant);
			return "/top/rest/detail";
		} else {
			return "redirect:/top/rest/list/1";	
		}
	}

	// 음식점 등록 페이지(관리자)
	@GetMapping("/rest/insert.do")
	public String restinsert(
			@SessionAttribute(required = false) UserDto loginUser
			) {
		if((loginUser).getAdminCk() == 1) {
			return "/top/rest/insert";
		} else {
			return "redirect:/user/login.do";
		}
	}
		
	// 음식점 등록(관리자)
	@PostMapping("/rest/insert.do")
	public String restinsert(
			Restaurant restaurant,
			@RequestParam(name = "imgFile", required = false) MultipartFile [] imgFiles,
			@SessionAttribute(required = false) UserDto loginUser,
			HttpSession session) {
		int insert = 0;
		try {
			//이미지 저장 및 처리
			if(imgFiles != null) {
				List<RestaurantImg> restaurantImgs = new ArrayList<RestaurantImg>();
				// imgFiles가 null이면 여기서 오류 발생!! 
				for(MultipartFile imgFile : imgFiles) {		
					String type = imgFile.getContentType();
					if(type.split("/")[0].equals("image")) {
						// 새로운 이미지 등록 
						String newFileName = "rest_" + System.nanoTime() + "." + type.split("/")[1]; // {"image", "jpeg"}
						Path newFilePath = Paths.get(savePath + "/" + newFileName);
						imgFile.transferTo(newFilePath); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
						
						RestaurantImg restaurantImg = new RestaurantImg();
						restaurantImg.setImg_path(newFileName); 
						restaurantImgs.add(restaurantImg);
					}
				}
				if(restaurantImgs.size() > 0) {
					restaurant.setRestaurantImgs(restaurantImgs);
				}
			}
			insert = restaurantService.registRest(restaurant); // DB에 관광지 등록
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(insert > 0) {
			System.out.println("음식점 등록 성공! : " + insert);
			return "redirect:/top/rest/list/1";
		} else {
			System.out.println("음식점 등록 실패! : " + insert);
			return "redirect:/top/rest/insert.do";
		}
	}
	
	// 음식점 수정 페이지(관리자) 
	@SuppressWarnings("null")
	@GetMapping("/rest/update/{restRank}")
	public String restupdate(
			@PathVariable int restRank, 
			Model model, 
			@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
			HttpSession session) {
		Restaurant restaurant = null;
		restaurant = restaurankMapper.selectDetailOne(restRank);
		if(loginUser != null || loginUser.getAdminCk() == 1) {
			model.addAttribute("restaurant", restaurant);
			return "/top/rest/update";			
		} else {
			return "redirect:/user/login.do";
		}
	}
	
	// 음식점 수정(관리자)
	@PostMapping("/rest/update.do")
	public String restupdate(
			Restaurant restaurant,
			Model model,
			@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
			@RequestParam(name="restaurankImgNo", required = false ) int [] restaurantImgNos,
			@RequestParam(name = "imgFile", required = false) MultipartFile[] imgFiles,
			HttpSession session
			) {
		int update = 0; 
		if((loginUser).getAdminCk() == 1) {
			try {
				int restImgCount = restaurantImgMapper.selectCountRestRank(restaurant.getRest_rank());
				int insertRestImgLength = RESTAURANK_IMG_LIMIT - restImgCount + ((restaurantImgNos != null) ? restaurantImgNos.length : 0);
				// 이미지 저장 
				if(imgFiles != null && insertRestImgLength > 0) {
					List<RestaurantImg> restaurantImgs = new ArrayList<RestaurantImg>();
					for(MultipartFile imgFile : imgFiles) { 
						String[] types = imgFile.getContentType().split("/");
						if(types[0].equals("image")) {
							// 새로운 이미지 등록 
							String newFileName = "rest_" + System.nanoTime() + "." + types[1];
							Path path = Paths.get(savePath + "/" + newFileName);
							imgFile.transferTo(path); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
							
							RestaurantImg restaurantImg = new RestaurantImg();
							restaurantImg.setRest_rank(restaurant.getRest_rank()); 
							restaurantImg.setImg_path(newFileName);
							restaurantImgs.add(restaurantImg);
							
							if(-- insertRestImgLength == 0) break; // 이미지 수가 5개면 반목문 종료 
						}
					}
					if(restaurantImgs.size() > 0) {
						restaurant.setRestaurantImgs(restaurantImgs);
					}
				}
				update = restaurantService.updateRestRemoveRestImg(restaurant, restaurantImgNos); // DB에서 후기 수정
			} catch (Exception e) {
				e.printStackTrace();
				return "redirect:/top/rest/update/" + restaurant.getRest_rank();
			}
			if(update > 0) {
				System.out.println("음식점 수정 성공! : " + update);
				return "redirect:/top/rest/detail/" + restaurant.getRest_rank();
			} else {
				System.out.println("음식점 수정 실패! : " + update);
				return "redirect:/top/rest/update/" + restaurant.getRest_rank();
			}	
		} else { 
			return "redirect:/user/login.do";
		}  
	}
		
	// 음식점 삭제(관리자)
	@SuppressWarnings("null")
	@GetMapping("/rest/delete/{restRank}/{userId}")
	public String restdelete(
			@PathVariable int restRank,
			@PathVariable String userId,
			@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
			HttpSession session
			) {
		String msg = "";
		if(loginUser != null || loginUser.getAdminCk() == 1) {
			int delete = 0;
			try {
				delete = restaurantService.removeRest(restRank);
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(delete > 0) {
				msg = "음식점 삭제 성공";
				session.setAttribute("msg", msg);
				return "redirect:/top/rest/list/1";
			} else {
				msg = "음식점 삭제 실패";
				session.setAttribute("msg", msg);
				return "redirect:/top/rest/update/" + restRank;			
			}	
		} else {
			msg = "로그인 하셔야 이용 가능합니다.";
			session.setAttribute("msg", msg);
			return "redirect:/user/login.do";
		}
	}
	
	// 숙박 리스트
	@GetMapping("/acco/list/{page}")
	public String acco(
			@PathVariable int page,
			Model model) {
		int row = 10;
		int startRow = (page - 1) * row;
		List<Acco> accoList = accoMapper.selectListAll(startRow, row);
		int count = accoMapper.selectPageAllCount();
		Pagination pagination = new Pagination(page, count, "/top/acco/list/", row);
		model.addAttribute("pagination", pagination);
		model.addAttribute("accoList", accoList);	
		model.addAttribute("row", row);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		return "top/acco/list";
	}
	
	// 숙박 상세
	@GetMapping("/acco/detail/{accoRank}")
	public String accoDetail(
			@PathVariable Integer accoRank,
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model
			) {
		Acco acco = null; 
		try {
			acco = accoMapper.selectDetailOne(accoRank);
			
			// 숙박 조회수 로직
			Cookie oldCookie = null; // oldCookie 객체를 선언한 후 빈 값으로 초기화
			Cookie[] cookies = req.getCookies(); // request 객체에서 쿠키들을 가져와 Cookie 타입을 요소로 가지는 리스트에 담기
			
			if (cookies != null) { // 접속한 기록이 있을 때
				for (Cookie cookie : cookies) { // 반복문으로 하나하나 검사
					if (cookie.getName().equals("accoViews")) { // 쿠키의 이름이 accoViews인지 확인 
						oldCookie = cookie; // 맞으면 oldCookie에 해당 쿠키를 저장 
					}
				}
			}
			if (oldCookie != null) { // 이름이 accoViews인 쿠키가 있을 때
				if (!oldCookie.getValue().contains("["+ accoRank.toString() +"]")) { // 특정 숙박 아이디가 oldCookie에 포함되어 있지 않을 때 (이미 포함되어 있다면 조회수 올라가지 않음)
					this.accoService.accoUpdateView(accoRank); // 조회수 올리기
					oldCookie.setValue(oldCookie.getValue() + "_[" + accoRank + "]"); // 조회한 숙박 아이디 oldCookie에 저장
					oldCookie.setPath("/"); // 쿠키 경로 저장
					oldCookie.setMaxAge(60 * 60 * 24); // 쿠키 지속 시간 저장
					resp.addCookie(oldCookie); // response에 oldCookie를 전달
				}
			} else { // 이름이 accoViews인 쿠키가 없을 때
				this.accoService.accoUpdateView(accoRank); // 조회수 올리기
				Cookie newCookie = new Cookie("accoViews", "[" + accoRank + "]"); // accoViews라는 이름으로 쿠키를 만들고 조회한 숙박 아이디 저장
				newCookie.setPath("/"); // 쿠키 경로 저장
				newCookie.setMaxAge(60 * 60 * 24); // 쿠키 지속 시간 저장
				resp.addCookie(newCookie); // response에 newCookie를 전달
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(acco != null) {
			model.addAttribute("acco", acco);
			return "/top/acco/detail";
		} else {
			return "redirect:/top/acco/list/1";	
		}
	}
	
	// 숙박 등록 페이지(관리자)
	@GetMapping("/acco/insert.do")
	public String accoInsert(
			@SessionAttribute(required = false) UserDto loginUser
			) {
		if(loginUser.getAdminCk() == 1) {
			return "/top/acco/insert";
		} else {
			return "redirect:/user/login.do";
		}
	}
		
	// 숙박 등록(관리자)
	@PostMapping("/acco/insert.do")
	public String accoInsert(
			Acco acco,
			@RequestParam(name = "imgFile", required = false) MultipartFile [] imgFiles,
			@SessionAttribute(required = false) UserDto loginUser,
			HttpSession session) {
		int insert = 0;
		try {
			//이미지 저장 및 처리
			if(imgFiles != null) {
				List<AccoImg> accoImgs = new ArrayList<AccoImg>();
				// imgFiles가 null이면 여기서 오류 발생!! 
				for(MultipartFile imgFile : imgFiles) {		
					String type = imgFile.getContentType();
					if(type.split("/")[0].equals("image")) {
						// 새로운 이미지 등록 
						String newFileName = "acco_" + System.nanoTime() + "." + type.split("/")[1]; // {"image", "jpeg"}
						Path newFilePath = Paths.get(savePath + "/" + newFileName);
						imgFile.transferTo(newFilePath); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
						
						AccoImg accoImg = new AccoImg();
						accoImg.setImg_path(newFileName); 
						accoImgs.add(accoImg);
					}
				}
				if(accoImgs.size() > 0) {
					acco.setAccoImgs(accoImgs);
				}
			}
			insert = accoService.registAcco(acco); // DB에 등록
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(insert > 0) {
			System.out.println("숙박 등록 성공! : " + insert);
			return "redirect:/top/acco/list/1";
		} else {
			System.out.println("숙박 등록 실패! : " + insert);
			return "redirect:/top/acco/insert.do";
		}
	}
			
	// 숙박 수정 페이지(관리자) 
	@GetMapping("/acco/update/{accoRank}")
	public String accoUpdate(
			@PathVariable int accoRank, 
			Model model, 
			@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
			HttpSession session) {
		Acco acco = null;
		acco = accoMapper.selectDetailOne(accoRank);
		if(loginUser.getAdminCk() == 1) {
			model.addAttribute("acco", acco);
			return "/top/acco/update";			
		} else {
			return "redirect:/user/login.do";
		}
	}
	
	// 숙박 수정(관리자) 
	@PostMapping("/acco/update.do")
	public String accoUpdate(
			Acco acco,
			Model model,
			@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
			@RequestParam(name="accoImgNo", required = false ) int [] accoImgNos,
			@RequestParam(name = "imgFile", required = false) MultipartFile[] imgFiles,
			HttpSession session
			) {
		int update = 0; 
		if(loginUser.getAdminCk() == 1) {
			try {
				int accoImgCount = accoImgMapper.selectCountAccoRank(acco.getAcco_rank());
				int insertAccoImgLength = ACCO_IMG_LIMIT - accoImgCount + ((accoImgNos != null) ? accoImgNos.length : 0);
				// 이미지 저장 
				if(imgFiles != null && insertAccoImgLength > 0) {
					List<AccoImg> accoImgs = new ArrayList<AccoImg>();
					for(MultipartFile imgFile : imgFiles) { 
						String[] types = imgFile.getContentType().split("/");
						if(types[0].equals("image")) {
							// 새로운 이미지 등록 
							String newFileName = "acco_" + System.nanoTime() + "." + types[1];
							Path path = Paths.get(savePath + "/" + newFileName);
							imgFile.transferTo(path); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
							
							AccoImg accoImg = new AccoImg();
							accoImg.setAcco_rank(acco.getAcco_rank());
							accoImg.setImg_path(newFileName);
							accoImgs.add(accoImg);
							
							if(-- insertAccoImgLength == 0) break; // 이미지 수가 5개면 반목문 종료 
						}
					}
					if(accoImgs.size() > 0) {
						acco.setAccoImgs(accoImgs);
					}
				}
				update = accoService.updateAccoRemoveAccoImg(acco, accoImgNos); // DB에서 후기 수정
			} catch (Exception e) {
				e.printStackTrace();
				return "redirect:/top/acco/update/" + acco.getAcco_rank();
			}
			if(update > 0) {
				System.out.println("숙박 수정 성공! : " + update);
				return "redirect:/top/acco/detail/" + acco.getAcco_rank();
			} else {
				System.out.println("숙박 수정 실패! : " + update);
				return "redirect:/top/acco/update/" + acco.getAcco_rank();
			}	
		} else { 
			return "redirect:/user/login.do";
		}  
	}	
		
	// 숙박 삭제(관리자) 
	@SuppressWarnings("null")
	@GetMapping("/acco/delete/{accoRank}/{userId}")
	public String accoDelete(
			@PathVariable int accoRank,
			@PathVariable String userId,
			@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
			HttpSession session
			) {
		String msg = "";
		if(loginUser != null || loginUser.getAdminCk() == 1) {
			int delete = 0;
			try {
				delete = accoService.removeAcco(accoRank);
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(delete > 0) {
				msg = "숙박 삭제 성공";
				session.setAttribute("msg", msg);
				return "redirect:/top/acco/list/1";
			} else {
				msg = "숙박 삭제 실패";
				session.setAttribute("msg", msg);
				return "redirect:/top/acco/update/" + accoRank;			
			}	
		} else {
			msg = "로그인 하셔야 이용 가능합니다.";
			session.setAttribute("msg", msg);
			return "redirect:/user/login.do";
		}
	}
}