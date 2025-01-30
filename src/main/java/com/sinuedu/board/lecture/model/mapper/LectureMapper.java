package com.sinuedu.board.lecture.model.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.sinuedu.board.lecture.model.vo.Chapter;
import com.sinuedu.board.lecture.model.vo.Lecture;

@Mapper
public interface LectureMapper {

	ArrayList<Lecture> selectLectureList(Integer lecNo);

	int chapterCount(int lecNo);

	ArrayList<Chapter> selectLecture(int lecNo);

	Chapter selectChapter(int chapNo);

	int rating(HashMap<String, Integer> map);

	int viewChapter(HashMap<String, Integer> map);

	int dupViewChapter(HashMap<String, Integer> map);

	int chapRateAvg(HashMap<String, Integer> map);

	// 아래로 추가한거
	ArrayList<Lecture> PopularLectures();

}
