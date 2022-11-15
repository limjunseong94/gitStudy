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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.project.mainPage.dto.BoardImg;
import com.project.mainPage.service.BoardService;
import com.project.mainPage.dto.Board;
import com.project.mainPage.dto.BoardPrefer;
import com.project.mainPage.dto.Pagination;
import com.project.mainPage.dto.Reply;
import com.project.mainPage.dto.ReplyPrefer;
import com.project.mainPage.dto.UserDto;
import com.project.mainPage.mapper.BoardImgMapper;
import com.project.mainPage.mapper.BoardMapper;
import com.project.mainPage.mapper.BoardPreferMapper;
import com.project.mainPage.mapper.ReplyMapper;
@Controller
@RequestMapping("/board")
public class BoardController {	
	// board > board_img 의 수를 5개로 제한 
	private final static int BOARD_IMG_LIMIT = 5; 
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private BoardImgMapper boardImgMapper;
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private BoardPreferMapper boardPreferMapper;
	
	@Autowired
	private ReplyMapper replyMapper;
	
	@Value("${spring.servlet.multipart.location}") // 파일이 임시 저장되는 경로 + 파일을 저장할 경로
	private String savePath;
	
//	후기 리스트
	@GetMapping("/list/{page}")
	public String list(
			@PathVariable int page, 
			Model model, 
			@RequestParam(required = false) String field,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String sort,
			@RequestParam(required = false, defaultValue = "desc") String direct,
			@SessionAttribute(required = false) UserDto loginUser) {
		int row = 10;
		int startRow = (page - 1) * row;		
		List<Board> boardList = null;
		int count = 0;
		if(field != null && !field.equals("")) {
			if(sort != null && !sort.equals("")) { // 검색(o) + 정렬(o)
				boardList = boardMapper.selectPageAll(startRow, row, field, search, sort, direct);
				count = boardMapper.selectPageAllCount(field, search, sort, direct);
			} else { // 검색(o) + 정렬(x)
				boardList = boardMapper.selectPageAll(startRow, row, field, search, null, null);
				count = boardMapper.selectPageAllCount(field, search, null, null);
			}
		} else {
			if(sort != null && !sort.equals("")) { // 검색(x) + 정렬(o)
				boardList = boardMapper.selectPageAll(startRow, row, null, null, sort, direct);
				count = boardMapper.selectPageAllCount(null, null, sort, direct);
			} else { // 검색(x) + 정렬(x)
				boardList = boardMapper.selectPageAll(startRow, row, null, null, null, null);
				count = boardMapper.selectPageAllCount(null, null, null, null);
			}
		}
		
		Pagination pagination = new Pagination(page, count, "/board/list/", row);
		model.addAttribute("pagination", pagination);
		model.addAttribute("list", boardList);
		model.addAttribute("row", row);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		return "/board/list";
	}
	
//	후기 상세
	@GetMapping("/detail/{boardNo}")
	public String detail(
			@PathVariable Integer boardNo,
			Model model,
			@SessionAttribute(required = false) UserDto loginUser, 
			@RequestParam(defaultValue = "1") int replyPage,
			@RequestParam(required = false) String sort,
			@RequestParam(required = false, defaultValue = "desc") String direct,
			@RequestParam(required = false) String writer,
			HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		String loginUsersId = null;
		
		Board board = null;		
		BoardPrefer boardPrefer = null; // 로그인이 안 되면 null
		
		int replySize = 0; // 전체 댓글 수
		int repl = 0; // 내가 작성한 댓글 수
		
		try {
			board = boardMapper.selectOne(boardNo);
			
			// 후기 조회수 로직
			Cookie oldCookie = null; // oldCookie 객체를 선언한 후 빈 값으로 초기화
			Cookie[] cookies = req.getCookies(); // request 객체에서 쿠키들을 가져와 Cookie 타입을 요소로 가지는 리스트에 담기
			
			if (cookies != null) { // 접속한 기록이 있을 때
				for (Cookie cookie : cookies) { // 반복문으로 하나하나 검사
					if (cookie.getName().equals("boardViews")) { // 쿠키의 이름이 boardViews인지 확인 
						oldCookie = cookie; // 맞으면 oldCookie에 해당 쿠키를 저장 
					}
				}
			}
			if (oldCookie != null) { // 이름이 boardViews인 쿠키가 있을 때
				if (!oldCookie.getValue().contains("["+ boardNo.toString() +"]")) { // 특정 후기 아이디가 oldCookie에 포함되어 있지 않을 때 (이미 포함되어 있다면 조회수 올라가지 않음)
					this.boardService.boardUpdateView(boardNo); // 조회수 올리기
					oldCookie.setValue(oldCookie.getValue() + "_[" + boardNo + "]"); // 조회한 후기 아이디 oldCookie에 저장
					oldCookie.setPath("/"); // 쿠키 경로 저장
					oldCookie.setMaxAge(60 * 60 * 24); // 쿠키 지속 시간 저장
					resp.addCookie(oldCookie); // response에 oldCookie를 전달
				}
			} else { // 이름이 boardViews인 쿠키가 없을 때
				this.boardService.boardUpdateView(boardNo); // 조회수 올리기
				Cookie newCookie = new Cookie("boardViews", "[" + boardNo + "]"); // boardViews라는 이름으로 쿠키를 만들고 조회한 후기 아이디 저장
				newCookie.setPath("/"); // 쿠키 경로 저장
				newCookie.setMaxAge(60 * 60 * 24); // 쿠키 지속 시간 저장
				resp.addCookie(newCookie); // response에 newCookie를 전달
			}
			
			if(loginUser != null) { // 로그인되어 있는 상태
				loginUsersId = loginUser.getUser_id();
				
				boardPrefer = boardPreferMapper.selectFindUserIdAndBoardNo(loginUser.getUser_id(), boardNo);
				System.out.println(boardPrefer);
				
				replySize = replyMapper.selectBoardNoCount(boardNo); // 전체 댓글 수
				repl = replyMapper.selectBoardNoAndUserId(boardNo, loginUsersId); // 내가 작성한 댓글 수
				
				// 후기 좋아요/싫어요
				if(boardPrefer != null && boardPrefer.getUser_id().equals(loginUser.getUser_id())) {
					if(boardPrefer.isPrefer()) { // 좋아요
						board.setPrefer_active(true);
					} else { // 싫어요
						board.setPrefer_active(false);
					}
				}
				
				// 댓글 좋아요/싫어요
				for(Reply reply : board.getReplys()) {
					for (ReplyPrefer prefer : reply.getGood_prefers()) { // 좋아요
						if(prefer.getUser_id().equals(loginUser.getUser_id())) {
							reply.setPrefer_active(true);
						}
					}
					for (ReplyPrefer prefer : reply.getBad_prefers()) { // 싫어요
						if(prefer.getUser_id().equals(loginUser.getUser_id())) {
							reply.setPrefer_active(false);
						}
					}
				}				
				// 나의 댓글 & 댓글 정렬
				if(writer != null && !writer.equals("")) {
					if(sort != null && !sort.equals("")) { // 나의 댓글(o) + 정렬(o)
						List<Reply> replies = replyMapper.selectBoardNoPage(boardNo, sort, direct, writer, loginUsersId);
						board.setReplys(replies);
					} else { // 나의 댓글(o) + 정렬(x)
						List<Reply> replies = replyMapper.selectBoardNoPage(boardNo, null, null, writer, loginUsersId);
						board.setReplys(replies);
					}
				} else {
					if(sort != null && !sort.equals("")) { // 나의 댓글(x) + 정렬(o)
						List<Reply> replies = replyMapper.selectBoardNoPage(boardNo, sort, direct, null, loginUsersId);
						board.setReplys(replies);
					} else { // 나의 댓글(x) + 정렬(x)
						List<Reply> replies = replyMapper.selectBoardNoPage(boardNo, null, null, null, loginUsersId);
						board.setReplys(replies);
					}
				}
			} else { // 로그인 안 되어 있는 상태
				replySize = replyMapper.selectBoardNoCount(boardNo);
				// 댓글 정렬
				if(sort != null && !sort.equals("")) { // 정렬(o)
					List<Reply> replies = replyMapper.selectBoardNoPage(boardNo, sort, direct, null, null);
					board.setReplys(replies);
				} else { // 정렬(x)
					List<Reply> replies = replyMapper.selectBoardNoPage(boardNo, null, null, null, null);
					board.setReplys(replies);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(board != null) {
			model.addAttribute("board", board);
			model.addAttribute("boardPrefer", boardPrefer);
			model.addAttribute("replySize", replySize);
			model.addAttribute("repl", repl);
			
			System.out.println("전체 댓글 : " + replySize);
			System.out.println("나의 댓글 : " + repl);
			
			return "/board/detail";			
		} else {
			return "redirect:/board/list/1";
		}
	}
	
//	후기 등록 페이지(로그인한 사람만)
	@GetMapping("/insert.do")
	public String insert(HttpSession session) {
		if(session.getAttribute("loginUser") != null) {
			return "/board/insert";
		} else {
			return "redirect:/user/login.do";
		}
	}
		
//	후기 등록
	@PostMapping("/insert.do")
	public String insert(
				Board board,
				@RequestParam(name = "imgFile", required = false) MultipartFile [] imgFiles,
				@SessionAttribute(required = false) UserDto loginUser,
				HttpSession session) {
		int insert = 0;
		try {
			//이미지 저장 및 처리
			if(imgFiles != null) {
				List<BoardImg> boardImgs = new ArrayList<BoardImg>();
				// imgFiles가 null이면 여기서 오류 발생!! 
				for(MultipartFile imgFile : imgFiles) {		
					String type = imgFile.getContentType();
					if(type.split("/")[0].equals("image")) {
						// 새로운 이미지 등록 
						String newFileName = "board_" + System.nanoTime() + "." + type.split("/")[1]; // {"image", "jpeg"}
						Path newFilePath = Paths.get(savePath + "/" + newFileName);
						imgFile.transferTo(newFilePath); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
						
						BoardImg boardImg = new BoardImg();
						boardImg.setImg_path(newFileName); 
						boardImgs.add(boardImg);
					}
				}
				if(boardImgs.size() > 0) {
					board.setBoardImgs(boardImgs);
				}
			}
			insert = boardService.registBoard(board); // DB에 후기 등록
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(insert > 0) {
			System.out.println("후기 등록 성공! : " + insert);
			return "redirect:/board/list/1";
		} else {
			System.out.println("후기 등록 실패! : " + insert);
			return "redirect:/board/insert.do";
		}
	}
	
//	후기 삭제 
	@GetMapping("/delete/{boardNo}/{userId}")
	public String delete(
			@PathVariable int boardNo,
			@PathVariable String userId,
			@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
			HttpSession session) {
		int delete = 0;
		if(loginUser == null) { // 로그인이 안 되어 있는 경우
			System.out.println("로그인하세요.");
			return "redirect:/user/login.do";
		} else if((loginUser != null && loginUser.getUser_id().equals(userId)) || loginUser.getAdminCk() == 1) { // 로그인된 일반 회원이 본인의 후기 삭제 / 관리자는 모든 회원 후기 삭제 가능
			try {
				delete = boardService.removeBoard(boardNo); // DB에서 후기 삭제
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(delete > 0) {
				System.out.println("후기 삭제 성공! : " + delete);
				return "redirect:/board/list/1";
			} else {
				System.out.println("후기 삭제 실패! : " + delete);
				return "redirect:/board/update/" + boardNo;			
			}
		} else { // 로그인된 일반 회원이 다른 회원의 후기를 삭제할 수 없음
			System.out.println("다른 회원이 작성한 후기 글을 삭제할 수 없습니다.");
			return "redirect:/";	
		}
	}
	
//	후기 수정 페이지
	@GetMapping("/update/{boardNo}")
	public String update(
			@PathVariable int boardNo, 
			Model model, 
			@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
			HttpSession session) {
		Board board = null;
		board = boardMapper.selectDetailOneAll(boardNo);
		if(loginUser == null) { // 로그인이 안 되어 있는 경우
			System.out.println("로그인하세요.");
			return "redirect:/user/login.do";
		} else if((loginUser != null && loginUser.getUser_id().equals(board.getUser().getUser_id())) || ((loginUser).getAdminCk() == 1)) { // 로그인된 일반 회원이 본인의 후기 수정 페이지로 이동 / 관리자는 모든 회원 후기 수정 가능
			model.addAttribute("board", board);
			return "/board/modify";		
		} else { // 로그인된 일반 회원이 다른 회원의 후기 수정 페이지로 이동할 수 없음
			System.out.println("다른 회원이 작성한 후기 글을 수정할 수 없습니다.");
			return "redirect:/";	
		}
	}
	
//	후기 수정 
	@PostMapping("/update.do")
	public String update(
			Board board, 
			Model model,
			@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
			@RequestParam(name = "boardImgNo", required = false) int [] boardImgNos, // required = false : 아무 것도 안 올 수 있는 경우
			@RequestParam(name = "imgFile", required = false) MultipartFile[] imgFiles,
 			HttpSession session
			) { 
		int update = 0; 
		if((loginUser != null && loginUser.getUser_id().equals(board.getUser().getUser_id())) || ((loginUser).getAdminCk() == 1)) {
			try {
				int boardImgCount = boardImgMapper.selectCountBoardNo(board.getBoard_no());  // baordImg 등록된 개수 
				int insertBoardImgLength = BOARD_IMG_LIMIT - boardImgCount + ((boardImgNos != null) ? boardImgNos.length : 0); // 5 - boardImgCount + 삭제할 이미지 개수 
				// 이미지 저장 
				if(imgFiles != null && insertBoardImgLength > 0) {
					List<BoardImg> boardImgs = new ArrayList<BoardImg>();
					for(MultipartFile imgFile : imgFiles) { 
						String[] types = imgFile.getContentType().split("/");
						if(types[0].equals("image")) {
							// 새로운 이미지 등록 
							String newFileName = "board_" + System.nanoTime() + "." + types[1];
							Path path = Paths.get(savePath + "/" + newFileName);
							imgFile.transferTo(path); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
							
							BoardImg boardImg = new BoardImg();
							boardImg.setBoard_no(board.getBoard_no()); 
							boardImg.setImg_path(newFileName);
							boardImgs.add(boardImg);
							
							if(-- insertBoardImgLength == 0) break; // 이미지 수가 5개면 반목문 종료 
						}
					}
					if(boardImgs.size() > 0) {
						board.setBoardImgs(boardImgs);
					}
				}
				update = boardService.modifyBoardRemoveBoardImg(board, boardImgNos); // DB에서 후기 수정
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(update > 0) {
				System.out.println("후기 수정 성공! : " + update);
				return "redirect:/board/detail/" + board.getBoard_no();
			} else {
				System.out.println("후기 수정 실패! : " + update);
				return "redirect:/board/update/" + board.getBoard_no();
			}				
		} else { 
			return "redirect:/user/login.do";
		}     
	}
	
//	후기 좋아요 싫어요
	@GetMapping("/prefer/{boardNo}/{prefer}")
	public String preferModify(
			Model model,
			@PathVariable int boardNo,
			@PathVariable boolean prefer,
			@SessionAttribute(required = false) UserDto loginUser,
			HttpSession session) {
		int modify = 0;
		try {
			BoardPrefer boardPrefer = boardPreferMapper.selectFindUserIdAndBoardNo(loginUser.getUser_id(), boardNo);
			if(boardPrefer == null) { // 좋아요 싫어요를 한 번도 한 적이 없을 때
				boardPrefer = new BoardPrefer();
				boardPrefer.setBoard_no(boardNo);
				boardPrefer.setPrefer(prefer);
				boardPrefer.setUser_id(loginUser.getUser_id());
				modify = boardPreferMapper.insertOne(boardPrefer);
			} else if(prefer == boardPrefer.isPrefer()) { // 좋아요 싫어요를 한 번 더 눌러서 삭제할 때
				boardPrefer.setPrefer(prefer);
				modify = boardPreferMapper.deleteOne(boardPrefer.getBoard_prefer_no());
			} else if(prefer != boardPrefer.isPrefer()) { // 좋아요에서 싫어요로 바꿀 때 or 싫어요에서 좋아요로 바꿀 때
				boardPrefer.setPrefer(prefer);
				modify = boardPreferMapper.updateOne(boardPrefer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(modify > 0) {
			System.out.println("성공!");
		} else {
			System.out.println("실패!");
		}
		return "redirect:/board/detail/" + boardNo;
	}
}