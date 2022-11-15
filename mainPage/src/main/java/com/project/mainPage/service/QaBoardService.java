package com.project.mainPage.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.mainPage.dto.QaBoard;
import com.project.mainPage.mapper.QaBoardMapper;
@Service
public class QaBoardService {
	@Autowired
	private QaBoardMapper qaBoardMapper;
	
	public QaBoard qaBoardUpdateView(int qaBoardNo) throws Exception {
		qaBoardMapper.detailUpdateViews(qaBoardNo);
		return qaBoardMapper.selectOne(qaBoardNo);
	}
}