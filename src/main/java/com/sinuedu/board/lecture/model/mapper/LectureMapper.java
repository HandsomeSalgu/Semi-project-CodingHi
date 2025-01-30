package com.sinuedu.board.lecture.model.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.sinuedu.board.lecture.model.vo.Chapter;
import com.sinuedu.board.lecture.model.vo.Lecture;

@Mapper
public interface LectureMapper {

	ArrayList<Lecture> selectLectureList(Integer lecNo);

	int chapterCount(int lecNo);

	ArrayList<Chapter> selectLecture(int lecNo);

	Chapter selectChapter(int chapNo);

	// 아래로 추가한거

	ArrayList<Lecture> PopularLectures();
}
