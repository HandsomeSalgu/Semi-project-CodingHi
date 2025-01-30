package com.sinuedu.board.lecture.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.sinuedu.board.lecture.model.mapper.LectureMapper;
import com.sinuedu.board.lecture.model.vo.Chapter;
import com.sinuedu.board.lecture.model.vo.Lecture;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LectureService {
	
	private final LectureMapper cMapper;

	public ArrayList<Lecture> selectLectureList(Integer lecNo) {
		return cMapper.selectLectureList(lecNo);
	}

	public int chapterCount(int lecNo) {
		return cMapper.chapterCount(lecNo);
	}

	public ArrayList<Chapter> selectLecture(int lecNo) {
		return cMapper.selectLecture(lecNo);
	}

	public Chapter selectChapter(int chapNo) {
		return cMapper.selectChapter(chapNo);
	}

	public int rating(HashMap<String, Integer> map) {
		return cMapper.rating(map);
	}

	public int viewChapter(HashMap<String, Integer> map) {
		return cMapper.viewChapter(map);
	}

	public int dupViewChapter(HashMap<String, Integer> map) {
		return cMapper.dupViewChapter(map);
	}

	public int chapRateAvg(HashMap<String, Integer> map) {
		return cMapper.chapRateAvg(map);
	}

}
