package com.sinuedu.board.qna.model.service;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.sinuedu.board.qna.model.mapper.QnaMapper;
import com.sinuedu.board.qna.model.vo.PageInfo;
import com.sinuedu.board.qna.model.vo.Qna;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnaService {

	private final QnaMapper mapper;
	
	public int getListCount() {
		return mapper.getListCount();
	}

	public ArrayList<Qna> selectBoardList(PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return mapper.selectBoardList(rowBounds);
	}

}
