package com.sinuedu.board.qna.model.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.sinuedu.board.qna.model.vo.Qna;

@Mapper
public interface QnaMapper {

	int getListCount();

	ArrayList<Qna> selectBoardList(RowBounds rowBounds);

}
