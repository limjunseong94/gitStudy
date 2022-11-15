package com.project.mainPage.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.project.mainPage.dto.Reply;
@Mapper
public interface ReplyMapper {
	public Reply selectOneJoinPrefers(int reply_no);
	public Reply selectOne(int reply_no);
	public List<Reply> selectBoardNo(int board_no);
	int selectBoardNoCount(int boardNo);
	public int selectBoardNoAndUserId(int boardNo, String user_id);
	List<Reply> selectBoardNoPage(
			int boardNo, 
			@Param(value = "sort")String sort, 
			@Param(value = "direct")String direct);
	List<Reply> selectBoardNoPage(
			int boardNo, 
			@Param(value = "sort")String sort, 
			@Param(value = "direct")String direct,
			@Param(value = "writer")String writer,
			String loginUsersId);
	public List<Reply> selectUserId(String user_id);
	public int insertOne(Reply reply);
	public int updateOne(Reply reply);
	public int deleteOne(int reply_no);
}