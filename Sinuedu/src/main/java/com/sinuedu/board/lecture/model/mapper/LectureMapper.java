package com.sinuedu.board.lecture.model.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.sinuedu.board.lecture.model.vo.Lecture;

@Mapper
public interface LectureMapper {

	ArrayList<Lecture> selectLectureList();

}
