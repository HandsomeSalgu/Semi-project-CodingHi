package com.sinuedu.board.qna.model.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.sinuedu.board.lecture.model.vo.Category;
import com.sinuedu.board.qna.model.vo.Qna;
import com.sinuedu.board.qna.model.vo.reply;

@Mapper
public interface QnaMapper {

	int getListCount();

	ArrayList<Qna> selectBoardList(RowBounds rowBounds);

	ArrayList<reply> selectReply(int rNo);
	
	Qna selectBoard(int qNo);

	int insertBoard(Qna q);

	int updateCount(int qNo);

	int insertReply(reply r);

	ArrayList<Category> selectCategory();

	int updateBoard(Qna q);





}
