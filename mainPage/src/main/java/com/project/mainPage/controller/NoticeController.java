package com.project.mainPage.controller;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.project.mainPage.dto.Notice;
import com.project.mainPage.dto.NoticeImg;
import com.project.mainPage.dto.Pagination;
import com.project.mainPage.dto.UserDto;
import com.project.mainPage.mapper.NoticeImgMapper;
import com.project.mainPage.mapper.NoticeMapper;
import com.project.mainPage.service.NoticeService;
@Controller
@RequestMapping("/notice")
public class NoticeController {
	// notice > notice_img 의 수를 5개로 제한
	private final static int NOTICE_IMG_LIMIT = 5; 
	
	@Value("${spring.servlet.multipart.location}") // 파일이 임시 저장되는 경로 + 파일을 저장할 경로 
	private String savePath;
	
	@Autowired
	private NoticeMapper noticeMapper;
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private NoticeImgMapper noticeImgMapper;
	
//	공지사항 리스트
	@GetMapping("/list/{page}")
	public String list(
			@PathVariable int page, 
			@RequestParam(required = false) String field,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String sort,
			@RequestParam(required = false, defaultValue = "desc") String direct,
			Model model) {
		int row = 10;
		int startRow = (page - 1) * row;
		List<Notice> noticeList = null;
		int count = 0;
		if(field != null && !field.equals("")) {
			if(sort != null && !sort.equals("")) { // 검색(o) + 정렬(o)
				noticeList = noticeMapper.selectPageAll(startRow, row, field, search, sort, direct);
				count = noticeMapper.selectPageAllCount(field, search, sort, direct);
			} else { // 검색(o) + 정렬(x)
				noticeList = noticeMapper.selectPageAll(startRow, row, field, search, null, null);
				count = noticeMapper.selectPageAllCount(field, search, null, null);
			}
		} else {
			if(sort != null && !sort.equals("")) { // 검색(x) + 정렬(o)
				noticeList = noticeMapper.selectPageAll(startRow, row, null, null, sort, direct);
				count = noticeMapper.selectPageAllCount(null, null, sort, direct);
			} else { // 검색(x) + 정렬(x)
				noticeList = noticeMapper.selectPageAll(startRow, row, null, null, null, null);
				count = noticeMapper.selectPageAllCount(null, null, null, null);
			}
		}
			
		Pagination pagination = new Pagination(page, count, "/notice/list/", row);
		model.addAttribute("pagination", pagination);
		model.addAttribute("noticeList", noticeList);
		model.addAttribute("row", row);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		return "/notice/list";
	}

//	공지사항 상세
	@GetMapping("/detail/{noticeNo}")
	public String detail(
			@PathVariable Integer noticeNo, 
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model) {
		Notice notice = null; 
		try {
			notice = noticeMapper.selectDetailOne(noticeNo);
			
			// 공지사항 조회수 로직
			Cookie oldCookie = null; // oldCookie 객체를 선언한 후 빈값으로 초기화
			Cookie[] cookies = req.getCookies(); // request 객체에서 쿠키들을 가져와 Cookie 타입을 요소로 가지는 리스트에 담기
			
			if (cookies != null) { // 접속한 기록이 있을 때
				for (Cookie cookie : cookies) { // 반복문으로 하나하나 검사
					if (cookie.getName().equals("noticeViews")) { // 쿠키의 이름이 noticeViews인지 확인 
						oldCookie = cookie; // 맞으면 oldCookie에 해당 쿠키를 저장 
					}
				}
			}
			if (oldCookie != null) { // 이름이 noticeViews인 쿠키가 있을 때
				if (!oldCookie.getValue().contains("["+ noticeNo.toString() +"]")) { // 특정 공지사항 아이디가 oldCookie에 포함되어 있지 않을 때 (이미 포함되어 있다면 조회수 올라가지 않음)
					this.noticeService.noticeUpdateView(noticeNo); // 조회수 올리기
					oldCookie.setValue(oldCookie.getValue() + "_[" + noticeNo + "]"); // 조회한 공지사항 아이디 oldCookie에 저장
					oldCookie.setPath("/"); // 쿠키 경로 저장
					oldCookie.setMaxAge(60 * 60 * 24); // 쿠키 지속 시간 저장
					resp.addCookie(oldCookie); // response에 oldCookie를 전달
				}
			} else { // 이름이 noticeViews인 쿠키가 없을 때
				this.noticeService.noticeUpdateView(noticeNo); // 조회수 올리기
				Cookie newCookie = new Cookie("noticeViews", "[" + noticeNo + "]"); // noticeViews라는 이름으로 쿠키를 만들고 조회한 공지사항 아이디 저장
				newCookie.setPath("/"); // 쿠키 경로 저장
				newCookie.setMaxAge(60 * 60 * 24); // 쿠키 지속 시간 저장
				resp.addCookie(newCookie); // response에 newCookie를 전달
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(notice != null) {
			model.addAttribute(notice);
			return "/notice/detail";
		}else {
			return "redirect:/notice/list/1";	
		}
	}
	
//	공지사항 등록 페이지(관리자)
	@GetMapping("/insert.do")
	public String insert(HttpSession session) {
		if(session.getAttribute("loginUser") != null) {
			return "/notice/insert";
		} else {
			return "redirect:/user/login.do";
		}
	}
	
//	공지사항 등록(관리자)
	@PostMapping("/insert.do")
	public String insert(
			Notice notice, 
			List<MultipartFile> imgFiles) {
		int insert = 0; 
		try {
			if(imgFiles != null) {
				List<NoticeImg> noticeImgs = new ArrayList<NoticeImg>();
				// imgFiles가 null 이면 오류 발생! 
				for(MultipartFile imgFile : imgFiles) {
					String type = imgFile.getContentType();
					// image 만 설정 
					if(type.split("/")[0].equals("image")) {
						String newFileName = "notice_" + System.nanoTime() + "." + type.split("/")[1]; 
						Path newFilePath = Paths.get(savePath + "/" + newFileName);
						imgFile.transferTo(newFilePath); // 파일 데이터를 지정한 file로 저장
						NoticeImg noticeImg = new NoticeImg();
						noticeImg.setImg_path(newFileName);
						noticeImgs.add(noticeImg);						
					}
				}
				if(noticeImgs.size() > 0) {
					notice.setNoticeImgs(noticeImgs);
				}
			}
			insert = noticeService.NoticeAndNoticeImg(notice);
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(insert > 0) {
			System.out.println("공지사항 등록 성공! : " + insert);
			return "redirect:/notice/list/1";			
		}else {
			System.out.println("공지사항 등록 실패! : " + insert);
			return "redirect:/notice/insert.do";						
		}
	}
	
//	공지사항 삭제(관리자)
	@GetMapping("/delete/{noticeNo}/{userId}")
	public String delete(
			@PathVariable int noticeNo,
			@PathVariable String userId,
			@SessionAttribute(name="loginUser", required = false) UserDto loginUser
			) {
		// loginUsers null이 아니고 관리자일 때 삭제 가능 
			if(loginUser != null  && (loginUser.getAdminCk() == 1)) {
				int delete = 0;
				try {
					delete = noticeService.removeNotice(noticeNo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(delete > 0) {
					System.out.println("공지사항 삭제 성공! : " + delete);
					return "redirect:/notice/list/1";
				} else {
					System.out.println("공지사항 삭제 실패! : " + delete);
					return "redirect:/notice/update/" + noticeNo;
				}
			} else {
				return "redirect:/user/login.do";				
			}	
	}
	
// 공지사항 수정 페이지(관리자)
	@GetMapping("/update/{noticeNo}")
	public String update(
			@PathVariable int noticeNo,
			Model model,
			HttpSession session,
			@SessionAttribute(name="loginUser", required = false) UserDto loginUser
			) {
		Notice notice = noticeMapper.selectDetailOne(noticeNo); 
		if(loginUser != null && (loginUser.getAdminCk() == 1)) {
			model.addAttribute(notice);
			return "/notice/modify";	
		} else {
			return "redirect:/user/login.do";			
		}	
	}
	
//	공지사항 수정(관리자)
	@PostMapping("/update.do")
	public String update(
			Notice notice,
			@RequestParam(name = "noticeImgNo", required = false) int [] noticeImgNos,
			@RequestParam(name = "imgFile", required = false) MultipartFile[] imgFiles,
			HttpSession session) {
		int update = 0;
		Object loginUser_obj = session.getAttribute("loginUser");
		if(loginUser_obj != null)  {
			try {
				int noticeImgCount = noticeImgMapper.selectCountNoticeNo(notice.getNotice_no());
				int insertNoticeImgLength = NOTICE_IMG_LIMIT - noticeImgCount + ((noticeImgNos != null) ? noticeImgNos.length : 0);
					if(imgFiles!=null && insertNoticeImgLength > 0) {
						List<NoticeImg> noticeImgs = new ArrayList<NoticeImg>();
						for(MultipartFile imgFile : imgFiles) {
							String[] types = imgFile.getContentType().split("/");
							if(types[0].equals("image")) {
								String newFileName = "board_" + System.nanoTime() + "." + types[1];
								Path path = Paths.get(savePath + "/" + newFileName);
								imgFile.transferTo(path);
								NoticeImg noticeImg = new NoticeImg();
								noticeImg.setNotice_no(notice.getNotice_no());
								noticeImg.setImg_path(newFileName);
								noticeImgs.add(noticeImg);
								if(--insertNoticeImgLength == 0) break; // 이미지 수가 5개면 반복문 종료
							}
						}
						if(noticeImgs.size() > 0) {
							notice.setNoticeImgs(noticeImgs);
						}
					}
					update = noticeService.modifyBoardRemoveBoardImg(notice, noticeImgNos);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(update > 0) {
					System.out.println("공지사항 수정 성공! : " + update);
					return "redirect:/notice/detail/" + notice.getNotice_no();
				} else {
					System.out.println("공지사항 수정 실패! : " + update);
					return "redirect:/notice/update/" + notice.getNotice_no();
				}
			} else {
				return "redirect:/user/login.do";
			}	
	}
}