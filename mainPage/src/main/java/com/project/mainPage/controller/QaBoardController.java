package com.project.mainPage.controller;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import com.project.mainPage.dto.Pagination;
import com.project.mainPage.dto.QaBoard;
import com.project.mainPage.dto.QaReply;
import com.project.mainPage.dto.UserDto;
import com.project.mainPage.mapper.QaBoardMapper;
import com.project.mainPage.mapper.QaReplyMapper;
import com.project.mainPage.service.QaBoardService;
@Controller
@RequestMapping("/qaboard")
public class QaBoardController {
	@Autowired
	private QaBoardMapper qaBoardMapper;
	
	@Autowired
	private QaReplyMapper qaReplyMapper;
	
	@Autowired
	private QaBoardService qaBoardService;
	
//	고객 문의 리스트
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
		
		List<QaBoard> list = null;
		int count = 0;
		if(field != null && !field.equals("")) {
			if(sort != null && !sort.equals("")) { // 검색(o) + 정렬(o)
				list = qaBoardMapper.selectPageAll(startRow, row, field, search, sort, direct);
				count = qaBoardMapper.selectPageAllCount(field, search, sort, direct);
			} else { // 검색(o) + 정렬(x)
				list = qaBoardMapper.selectPageAll(startRow, row, field, search, null, null);
				count = qaBoardMapper.selectPageAllCount(field, search, null, null);
			}
		} else {
			if(sort != null && !sort.equals("")) { // 검색(x) + 정렬(o)
				list = qaBoardMapper.selectPageAll(startRow, row, null, null, sort, direct);
				count = qaBoardMapper.selectPageAllCount(null, null, sort, direct);
			} else { // 검색(x) + 정렬(x)
				list = qaBoardMapper.selectPageAll(startRow, row, null, null, null, null);
				count = qaBoardMapper.selectPageAllCount(null, null, null, null);
			}
		}
		Pagination pagination = new Pagination(page, count, "/qaboard/list/", row);
		model.addAttribute("pagination", pagination);
		model.addAttribute("list", list);
		model.addAttribute("row", row);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		return "/qaboard/list";
	}

//	고객 문의 상세
	@GetMapping("/detail/{qaBoardno}")
	public String detail(
			@PathVariable Integer qaBoardno, 
			Model model,
			HttpServletRequest req,
			HttpServletResponse resp,
			@SessionAttribute(required = false) UserDto loginUser) {
		QaBoard qaBoard = null;
		try {
			qaBoard = qaBoardMapper.selectOne(qaBoardno);
			
			// 고객 문의 조회수 로직
			Cookie oldCookie = null; // oldCookie 객체를 선언한 후 빈 값으로 초기화
			Cookie[] cookies = req.getCookies(); // request 객체에서 쿠키들을 가져와 Cookie 타입을 요소로 가지는 리스트에 담기
			
			if (cookies != null) { // 접속한 기록이 있을 때
				for (Cookie cookie : cookies) { // 반복문으로 하나하나 검사
					if (cookie.getName().equals("qaBoardViews")) { // 쿠키의 이름이 qaBoardViews인지 확인 
						oldCookie = cookie; // 맞으면 oldCookie에 해당 쿠키를 저장 
					}
				}
			}
			if (oldCookie != null) { // 이름이 qaBoardViews인 쿠키가 있을 때
				if (!oldCookie.getValue().contains("["+ qaBoardno.toString() +"]")) { // 특정 고객 문의 아이디가 oldCookie에 포함되어 있지 않을 때 (이미 포함되어 있다면 조회수 올라가지 않음)
					this.qaBoardService.qaBoardUpdateView(qaBoardno); // 조회수 올리기
					oldCookie.setValue(oldCookie.getValue() + "_[" + qaBoardno + "]"); // 조회한 고객 문의 아이디 oldCookie에 저장
					oldCookie.setPath("/"); // 쿠키 경로 저장
					oldCookie.setMaxAge(60 * 60 * 24); // 쿠키 지속 시간 저장
					resp.addCookie(oldCookie); // response에 oldCookie를 전달
				}
			} else { // 이름이 qaBoardViews인 쿠키가 없을 때
				this.qaBoardService.qaBoardUpdateView(qaBoardno); // 조회수 올리기
				Cookie newCookie = new Cookie("qaBoardViews", "[" + qaBoardno + "]"); // qaBoardViews라는 이름으로 쿠키를 만들고 조회한 고객 문의 아이디 저장
				newCookie.setPath("/"); // 쿠키 경로 저장
				newCookie.setMaxAge(60 * 60 * 24); // 쿠키 지속 시간 저장
				resp.addCookie(newCookie); // response에 newCookie를 전달
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("qaBoard", qaBoard);
		return "/qaboard/detail";
	}
	
//	고객 문의 등록 페이지
	@GetMapping("/insert.do")
	public String insert(
			Model model,
			@SessionAttribute(required = false) UserDto loginUser) {
		return "/qaboard/insert";
	}
	
//	고객 문의 등록
	@PostMapping("/insert.do")
	public String insert(QaBoard qaBoard) {
		int insert = 0;
		try {
			insert = qaBoardMapper.insertOne(qaBoard);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(insert > 0) {
			System.out.println("qaBoard 등록 성공! : " + insert);
			return "redirect:/qaboard/list/1";
		}else {
			System.out.println("qaBoard 등록 실패! : " + insert);
			return "redirect:/qaboard/insert.do";
		}
	}
	
//	고객 문의 삭제
	@GetMapping("/delete/{qaBoardNo}")
	public String delete(
			@PathVariable int qaBoardNo,
			HttpSession session,
			@SessionAttribute(required = false) UserDto loginUser) {
		int delete = 0;
		QaBoard qaBoard = qaBoardMapper.selectOne(qaBoardNo);
		if(loginUser == null) { // 로그인이 안 되어 있는 경우
			System.out.println("로그인하세요.");
			return "redirect:/user/login.do";			
		} else if(qaBoard.getUser().getUser_id().equals(loginUser.getUser_id()) || (loginUser.getAdminCk() == 1)) { // 로그인된 일반 회원이 본인이 작성한 고객 문의 글을 삭제 / 관리자는 모든 글 삭제 가능
			try {
				delete = qaBoardMapper.deleteOne(qaBoardNo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(delete > 0) {
				System.out.println("qaBoard 삭제 성공! : " + delete);
				return "redirect:/qaboard/list/1";
			} else {
				System.out.println("qaBoard 삭제 실패! : " + delete);
				return "redirect:/qaboard/detail/" + qaBoardNo;
			}
		} else { // 로그인된 일반 회원이 다른 회원이 작성한 고객 문의 글을 삭제할 수 없음
			System.out.println("다른 회원이 작성한 고객 문의 글을 삭제할 수 없습니다.");
			return "redirect:/";	
		}
	} 
	
//	고객 문의 수정 페이지
	@GetMapping("/update/{qaBoardno}")
	public String update(
			@PathVariable int qaBoardno, 
			Model model, 
			HttpSession session,
			@SessionAttribute(required = false) UserDto loginUser) {
		QaBoard qaBoard = null;
		qaBoard = qaBoardMapper.selectOne(qaBoardno);
		if(loginUser == null) { // 로그인이 안 되어 있는 경우
			System.out.println("로그인하세요.");
			return "redirect:/user/login.do";			
		} else if(qaBoard.getUser().getUser_id().equals(loginUser.getUser_id()) || (loginUser.getAdminCk() == 1)) { // 로그인된 일반 회원이 본인이 작성한 고객 문의 수정 페이지로 이동 / 관리자는 모든 글 조회 가능
			System.out.println("고객 문의 수정 페이지로 이동 성공!");
			model.addAttribute("qaBoard", qaBoard);
			return "/qaboard/modify";	
		} else { // 로그인된 일반 회원이 다른 회원이 작성한 고객 문의 수정 페이지로 이동할 수 없음
			System.out.println("다른 회원이 작성한 고객 문의 글을 수정할 수 없습니다.");
			return "redirect:/";	
		}
	}
	
//	고객 문의 수정
	@PostMapping("/update.do")
	public String update(QaBoard qaBoard) {
		int update = 0;
		try {
			update = qaBoardMapper.updateOne(qaBoard);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(update > 0) {
			System.out.println("qaBoard 수정 성공! : " + update);
			return "redirect:/qaboard/detail/" + qaBoard.getQaBoardNo();
		} else {
			System.out.println("qaBoard 수정 실패! : " + update);
			return "redirect:/qaboard/update/" + qaBoard.getQaBoardNo();
		}
	}
	
//	고객 문의 답변 등록(관리자)
	@PostMapping("/replyInsert.do")
	public String replyInsert(
			QaReply qaReply, 
			QaBoard qaBoard) {
		int insert = 0;
		int update = 0; // 답변 여부 1로 바꾸기
		try {
			insert = qaReplyMapper.insertOne(qaReply);
			update = qaBoardMapper.answerOne(qaBoard);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(insert > 0) {
			System.out.println("qaBoard 답변 등록 성공! : " + insert);
			System.out.println("qaBoard 답변 여부 1로 바꾸기 성공! : " + update);
			return "redirect:/qaboard/detail/" + qaBoard.getQaBoardNo();
		} else {
			System.out.println("qaBoard 답변 등록 실패! : " + insert);
			System.out.println("qaBoard 답변 여부 1로 바꾸기 실패! : " + update);
			return "redirect:/qaboard/detail/" + qaBoard.getQaBoardNo();
		}
	}
	
//	고객 문의 답변 수정 페이지(관리자)
	@GetMapping("/replyUpdate/{qaBoardNo}")
	public String replyUpdate(
			@PathVariable int qaBoardNo, 
			Model model, 
			HttpSession session) {
		Object loginUser_obj = session.getAttribute("loginUser");
		QaBoard qaBoard = null;
		try {
			qaBoard = qaBoardMapper.selectOne(qaBoardNo);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		if((((UserDto)loginUser_obj).getAdminCk() == 1)) { 
			model.addAttribute("qaBoard", qaBoard);
			return "/qaboard/modifyReply";			
		} else { 
			return "redirect:/user/login.do";
		}
	}
	
//	고객 문의 답변 수정(관리자)
	@PostMapping("/replyUpdate.do")
	public String replyUpdate(QaReply qaReply) {
		int update = 0;
		try {
			update = qaReplyMapper.updateOne(qaReply);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		if(update > 0) {
			System.out.println("qaBoard 답변 수정 성공! : " + update);
			return "redirect:/qaboard/detail/" + qaReply.getQaBoardNo();
		} else {
			System.out.println("qaBoard 답변 수정 실패! : " + update);
			return "redirect:/qaboard/replyUpdate/" + qaReply.getQaBoardNo();
		}
	}
	
//	고객 문의 답변 삭제(관리자)
	@GetMapping("/replyDelete/{qaBoardNo}")
	public String replyDelete(
			@PathVariable int qaBoardNo, 
			QaBoard qaBoard) {
		int delete = 0;
		int update = 0; // 답변 여부 다시 0으로 바꿔서 새로 등록할 수 있게 하기
		try {
			delete = qaReplyMapper.deleteOne(qaBoardNo);
			update = qaBoardMapper.answerOne(qaBoard);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(delete > 0) {
			System.out.println("qaBoard 답변 삭제 성공! : " + delete);
			System.out.println("qaBoard 답변 여부 다시 0으로 바꾸기 성공! : " + update);
			return "redirect:/qaboard/detail/" + qaBoardNo;
		} else {
			System.out.println("qaBoard 답변 삭제 실패! : " + delete);
			System.out.println("qaBoard 답변 여부 다시 0으로 바꾸기 실패! : " + update);
			return "redirect:/qaboard/replyUpdate/" + qaBoardNo;
		}
	}
}