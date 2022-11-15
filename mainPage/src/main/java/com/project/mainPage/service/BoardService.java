package com.project.mainPage.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.project.mainPage.dto.Board;
import com.project.mainPage.mapper.BoardMapper;
import com.project.mainPage.mapper.ReplyMapper;
import java.io.File;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.project.mainPage.dto.BoardImg;
import com.project.mainPage.dto.Reply;
import com.project.mainPage.mapper.BoardImgMapper;
@Service
public class BoardService {
	@Autowired
	private BoardMapper boardMapper;

	@Autowired
	private BoardImgMapper boardImgMapper;
	
	@Autowired
	private ReplyMapper replyMapper;
	
	@Value("${spring.servlet.multipart.location}")
	String savePath;
	
//	후기 조회수 수정
	public Board boardUpdateView(int boardNo) throws Exception {
		boardMapper.detailUpdateViews(boardNo);
		return boardMapper.selectOne(boardNo);
	}
		
//	후기 삭제
	public int removeBoard(int boardNo) throws Exception {
		int remove = 0;
		// Board를 참조하는 Reply의 이미지 삭제
		List<Reply> replies = replyMapper.selectBoardNo(boardNo);
		if(replies != null) {
			for(Reply reply : replies) {
				if(reply.getImg_path() != null) {
					File f = new File(savePath + "/" + reply.getImg_path());
					System.out.println("댓글 이미지 삭제 성공! : " + f.delete()); // 서버(static 폴더 내부에 있는 img 폴더)에서 삭제
				}
			}
		}
		// Board를 참조하는 Board의 이미지 삭제
		List<BoardImg> boardImgs = boardImgMapper.selectBoardNo(boardNo);
		if(boardImgs != null) {
			boardImgs.stream()
				.map(BoardImg::getImg_path)
				.forEach((img) -> {
					File f = new File(savePath + "/" + img);
					System.out.println("후기 이미지 삭제 성공! : " + f.delete()); // 서버(static 폴더 내부에 있는 img 폴더)에서 삭제
				});
		}
		remove = boardMapper.deleteOne(boardNo); // DB에서 후기 삭제
		return remove;
	}
	
//	후기 등록
	@Transactional
	public int registBoard(Board board) throws Exception {
		int regist = 0;
		int imgRegist = 0;
		// useGeneratedKeys = "true" keyProperty = "board_no" : 등록한 pk를 board에 저장함
		regist = boardMapper.insertOne(board);
		if(regist > 0 && board.getBoardImgs() != null) {
			for(BoardImg boardImg : board.getBoardImgs()) {
				boardImg.setBoard_no(board.getBoard_no()); // Auto Increment로 저장된 대표키 값
				imgRegist += boardImgMapper.insertOne(boardImg); // DB에 이미지 저장
			}
		}
		System.out.println("후기 이미지 등록 성공! : " + imgRegist);
		return regist;
	}
	
//	@Transactional : 함수 내부의 db 실행을 한 트랙젝션으로 보고 중간에 실패 시 db 실행을 취소 (roll back);
	@Transactional
	public int modifyBoardRemoveBoardImg(Board board, int[] boardImgNos) throws Exception {
		int update = 0;
		// 기존의 이미지 삭제
		if(boardImgNos != null) { // 선택한 삭제될 board_img.board_img_no
			for(int no : boardImgNos) {
				BoardImg boardImg = boardImgMapper.selectOne(no);
				
				File f = new File(savePath + "/" + boardImg.getImg_path());
				System.out.println("board의 이미지 파일(서버) 삭제 성공! : " + f.delete()); // 서버(static 폴더 내부에 있는 img 폴더)에서 이미지 삭제
				
				int removeBoardImg = boardImgMapper.deleteOne(no);
				System.out.println("board의 Board_img(DB) 삭제 성공! : " + removeBoardImg); // DB에서 이미지 삭제
			}			
		}
		// 새로운 이미지 등록
		if(board.getBoardImgs() != null) { // 이미지가 1개 이상 저장되면 null이 아니다.
			for(BoardImg boardImg : board.getBoardImgs()) {
				int registBoardImg = boardImgMapper.insertOne(boardImg); // DB에 이미지 저장
				System.out.println("board의 Board_img(DB) 등록 성공! : " + registBoardImg);
			}
		}
		update = boardMapper.updateOne(board); // DB에서 후기 수정
		return update;
	}
}