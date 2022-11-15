package com.project.mainPage.controller;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import com.project.mainPage.dto.EmailCheck;
import com.project.mainPage.dto.EmailConfirmDto;
import com.project.mainPage.dto.IdCheck;
import com.project.mainPage.dto.Pagination;
import com.project.mainPage.dto.PhoneCheck;
import com.project.mainPage.dto.PhoneConfirmDto;
import com.project.mainPage.dto.UserDto;
import com.project.mainPage.mapper.UserMapper;
import com.project.mainPage.service.EmailService;
import com.project.mainPage.service.MessageService;
import com.project.mainPage.service.UserService;
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private EmailService mailService;
	
	@Autowired
    private MessageService messageService;
	
//	회원 리스트
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
		
		List<UserDto> userList = null;
		int count = 0;
		if(field != null && !field.equals("")) {
			if(sort != null && !sort.equals("")) { // 검색(o) + 정렬(o)
				userList = userMapper.selectPageAll(startRow, row, field, search, sort, direct);
				count = userMapper.selectPageAllCount(field, search, sort, direct);
			} else { // 검색(o) + 정렬(x)
				userList = userMapper.selectPageAll(startRow, row, field, search, null, null);
				count = userMapper.selectPageAllCount(field, search, null, null);
			}
		} else {
			if(sort != null && !sort.equals("")) { // 검색(x) + 정렬(o)
				userList = userMapper.selectPageAll(startRow, row, null, null, sort, direct);
				count = userMapper.selectPageAllCount(null, null, sort, direct);
			} else { // 검색(x) + 정렬(x)
				userList = userMapper.selectPageAll(startRow, row, null, null, null, null);
				count = userMapper.selectPageAllCount(null, null, null, null);
			}
		}
		Pagination pagination = new Pagination(page, count, "/user/list/", row);
		model.addAttribute("pagination", pagination);
		model.addAttribute("userList", userList);
		model.addAttribute("row", row);
		model.addAttribute("count", count);
		model.addAttribute("page", page);	
		return "/user/list";
	}	
	
//	로그인 페이지
	@GetMapping("/login.do")
		public String loginPage(
				@SessionAttribute(required = false) UserDto loginUser,
				HttpSession session) {
		System.out.println("loginUser : " + loginUser);
		if(loginUser != null) {
			System.out.println("이미 로그인되어 있기 때문에 로그인 페이지로 이동 불가");
			return "redirect:/";
		} else {
			System.out.println("로그인되어 있지 않기 때문에 로그인 페이지로 이동 가능");
			return "user/login";
		}
	};
		
//	로그인
	@PostMapping("/login.do")
		public String login(
				@RequestParam(value="user_id") String userId, 
				@RequestParam(value="user_pw") String userPw,
				Model model,
				HttpSession session) {
			UserDto user = null;
			String msg = "";
			try {
				user = userMapper.selectId(userId); // 아이디만으로 유저 정보 불러 오기
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(user != null) { // 아이디가 존재할 때
				if(user.getUser_pw().equals(userPw)) { // 아이디로 불러 온 유저의 실제 비밀번호와 유저가 로그인 폼에서 입력한 비밀번호 값이 같을 때
					session.setAttribute("loginUser", user);
					Object redirectPage = session.getAttribute("redirectPage"); // 이전 페이지
					session.removeAttribute("redirectPage");
					System.out.println("로그인 성공! " + user); 
					if(redirectPage != null) {
						return "redirect:" + redirectPage; // 이전 페이지로 이동
					} else {
						return "redirect:/";
					}
				} else { // 아이디로 불러 온 유저의 실제 비밀번호와 유저가 로그인 폼에서 입력한 비밀번호 값이 같지 않을 때
					msg = "잘못된 비밀번호입니다.";
					model.addAttribute("msg", msg);
					return "user/login";	
				}
			} else { // 아이디가 존재하지 않을 때
				msg = "존재하지 않는 아이디입니다.";
				model.addAttribute("msg", msg);
				return "user/login";					
			}
	}
	
//	아이디 찾기
	@PostMapping("/findId.do")
	public String findId(
			String user_name, 
			String user_email, 
			String user_phone,
			Model model) {
		UserDto user = null;
		String msg = "";
		try {
			user = userMapper.findId(user_name, user_email, user_phone);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(user != null) {
			msg = "당신의 아이디는 " + user.getUser_id() + "입니다.";
			model.addAttribute("msg", msg);
			return "user/login";
		} else {
			msg = "아이디를 찾을 수 없습니다.";
			model.addAttribute("msg", msg);
			return "user/login";
		}
	}
	
//	비밀번호 찾기
	@PostMapping("/findPw.do")
	public String findPw(
			String user_id,
			String user_name, 
			String user_email, 
			String user_phone,
			Model model) {
		UserDto user = null;
		String msg = "";
		try {
			user = userMapper.findPw(user_id, user_name, user_email, user_phone);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(user != null) {
			msg = "비밀번호를 이메일로 발송했습니다.";
			System.out.println("user_pw : " + user.getUser_pw());
			model.addAttribute("msg", msg);
			mailService.mailSend(user);
			return "user/login";
		} else {
			msg = "비밀번호를 찾을 수 없습니다.";
			model.addAttribute("msg", msg);
			return "user/login";
		}
	}
	
//	회원 로그아웃
	@GetMapping("/logout.do")
	public String logout(HttpSession session) {
		System.out.println("로그아웃 성공!"); 
		session.invalidate();
		return "redirect:/";
	}
	
//	회원가입 페이지
	@GetMapping("/signup.do")
	public String signupPage(
			@SessionAttribute(required = false) UserDto loginUser,
			HttpSession session) {
		System.out.println("loginUser : " + loginUser);
		if(loginUser != null) {
			System.out.println("이미 로그인되어 있기 때문에 회원가입 페이지로 이동 불가");
			return "redirect:/";
		} else {
			System.out.println("로그인되어 있지 않기 때문에 회원가입 페이지로 이동 가능");
			return "user/signup";
		}
	}
	
//	회원가입
	@PostMapping("/signup.do")
	public String signup(
			UserDto user,
			HttpSession session) {
		int insert = 0;
		try {
			insert = userMapper.insertOne(user); // 회원가입 쿼리 실행
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(insert > 0) {
			System.out.println("회원가입 성공! : " + insert);
			session.setAttribute("loginUser", user); // 회원가입하면 로그인되어 있도록 설정
			return "redirect:/";
		} else {
			System.out.println("회원가입 실패! : " + insert);
			return "redirect:/user/signup.do";
		}
	}
	
//	회원 상세
	@GetMapping("/detail/{userId}")
	public String detail(
			@PathVariable String userId, 
			Model model,
			@SessionAttribute(required = false) UserDto loginUser, 
			HttpSession session) {
		UserDto user = userMapper.selectId(userId);
		if(loginUser == null) { // 로그인이 안 되어 있는 경우
			System.out.println("로그인하세요.");
			return "redirect:/user/login.do";			
		} else if(user.getUser_id().equals(loginUser.getUser_id()) || (loginUser.getAdminCk() == 1)) { // 로그인된 일반 회원이 본인의 상세 페이지로 이동 / 관리자는 모든 회원 조회 가능
			System.out.println("회원 상세 페이지로 이동 성공!");
			model.addAttribute("user", user);
			return "/user/detail";	
		} else { // 로그인된 일반 회원이 다른 회원의 상세 페이지로 이동할 수 없음
			System.out.println("다른 회원의 정보를 조회할 수 없습니다.");
			return "redirect:/";	
		}	
	} 
	
//	회원 수정 페이지
	@GetMapping("/update/{userId}")
	public String update(
			@PathVariable String userId,
			Model model,
			@SessionAttribute(required = false) UserDto loginUser, 
			HttpSession session
			) {
		UserDto user = userMapper.selectId(userId); 
		if(loginUser == null) { // 로그인이 안 되어 있는 경우
			System.out.println("로그인하세요.");
			return "redirect:/user/login.do";			
		} else if(user.getUser_id().equals(loginUser.getUser_id()) || (loginUser.getAdminCk() == 1)) { // 로그인된 일반 회원이 본인의 수정 페이지로 이동 / 관리자는 모든 회원 수정 가능
			System.out.println("회원 수정 페이지로 이동 성공!");
			model.addAttribute("user", user);
			return "/user/update";	
		} else { // 로그인된 일반 회원이 다른 회원의 수정 페이지로 이동할 수 없음
			System.out.println("다른 회원의 정보를 수정할 수 없습니다.");
			return "redirect:/";	
		}
	}
	
//	회원 정보 수정
	@PostMapping("/update.do")
	public String update(
			UserDto user,
			HttpSession session) {
		int update = 0;
		try {
			update = userMapper.updateOne(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(update > 0) {
			System.out.println("회원 수정 성공! : " + update);
			return "redirect:/user/detail/" + user.getUser_id();
		} else {
			System.out.println("회원 수정 실패! : " + update);
			return "redirect:/user/update/"+ user.getUser_id();
		}
	}
	
//	회원가입 중복 체크 (id)
	@GetMapping("/idCheck/{userId}")
	//ResponseBody가 들어가야 ajax가 작동한다
	@ResponseBody public IdCheck idCheck(
			@PathVariable String userId) {
		IdCheck idCheck = new IdCheck();
		UserDto user = userMapper.selectId(userId);
		if(user != null) { // 중복된 아이디가 있다
			idCheck.idCheck = true;
			idCheck.user = user;
		}
		return idCheck;
	}
	
//	회원가입 중복 체크 (email)
	@GetMapping("/emailCheck/{userEmail}")
	@ResponseBody public EmailCheck emailCheck(
			@PathVariable String userEmail) {
		EmailCheck emailCheck = new EmailCheck();
		UserDto user = userMapper.selectEmail(userEmail);
		if(user != null) { // 중복된 이메일이 있다
			emailCheck.emailCheck = true;
			emailCheck.user = user;
		}
		return emailCheck;	
	}
	
//	회원가입 중복 체크 (phone)
	@GetMapping("/phoneCheck/{userPhone}")
	@ResponseBody public PhoneCheck phoneCheck(
			@PathVariable String userPhone) {
		PhoneCheck phoneCheck = new PhoneCheck();
		UserDto user = userMapper.selectPhone(userPhone);
		if(user != null) { // 중복된 전화번호가 있다
			phoneCheck.phoneCheck = true;
			phoneCheck.user = user;
		}
		return phoneCheck;	
	}
	
//	회원가입 이메일 인증 버튼 누를 시 이메일로 인증번호 발송
	@GetMapping("/emailConfirm/{userEmail}")
	@ResponseBody public EmailConfirmDto emailConfirm(
			@PathVariable String userEmail) throws Exception {
		EmailConfirmDto emailConfirm = new EmailConfirmDto();
		String code = mailService.sendEmailConfirm(userEmail);
		if(code != null) {
			emailConfirm.authCode = code;
		}
		return emailConfirm;
	}
	
//	회원가입 전화번호 인증 버튼 누를 시 휴대폰으로 인증번호 발송
	@GetMapping("/phoneConfirm/{userPhone}")
	@ResponseBody public PhoneConfirmDto phoneConfirm(
			@PathVariable String userPhone) throws Exception {
		PhoneConfirmDto phoneConfirm = new PhoneConfirmDto();
		String code = messageService.PhoneNumberCheck(userPhone);
		if(code != null) {
			phoneConfirm.authCode = code;
		}
		return phoneConfirm;
	}
	
//	회원 삭제
	@GetMapping("/delete/{userId}")
	public String delete(
			@PathVariable String userId,
			@SessionAttribute(required = false) UserDto loginUser, 
			HttpSession session) {
		int delete = 0;
		if(loginUser.getAdminCk() == 1 && !loginUser.getUser_id().equals(userId)) { // 관리자가 본인이 아닌 다른 회원을 삭제 성공 시 로그아웃되지 않고 회원 리스트로 이동
			try {
				delete = userService.removeUser(userId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(delete > 0) {
				System.out.println("회원 삭제 성공!(관리자) : " + delete);
				return "redirect:/user/list/1";
			} else {
				System.out.println("회원 삭제 실패!(관리자) : " + delete);
				return "redirect:/user/update/" + userId;
			}
		} else if(loginUser.getAdminCk() == 0 && loginUser.getUser_id().equals(userId)) { // 일반 회원이 본인을 삭제 성공 시 로그아웃되면서 메인 화면으로 이동
			try {
				delete = userService.removeUser(userId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(delete > 0) {
				System.out.println("회원 삭제 성공!(일반 회원) : " + delete);
				session.invalidate(); // 세션 강제 만료
				return "redirect:/";
			} else {
				System.out.println("회원 삭제 실패!(일반 회원) : " + delete);
				return "redirect:/user/update/" + userId;
			}
		} else { // 관리자가 본인을 삭제하거나 일반 회원이 다른 회원을 삭제하는 것 불가
			System.out.println("관리자는 본인 삭제 불가");
			return "redirect:/user/update/" + userId;
		}
	} 
	
//	푸터 연결용
	@GetMapping("/agreement")
	public void agreement() {}; 
	
	@GetMapping("/privacy")
	public void privacy() {};

	@GetMapping("/emailRejection")
	public void emailRejection() {};	
}