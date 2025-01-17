package com.sinuedu.board.lecture.model.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.sinuedu.board.lecture.model.mapper.LectureMapper;
import com.sinuedu.board.lecture.model.vo.Lecture;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LectureService {
	
	private final LectureMapper cMapper;

	public ArrayList<Lecture> selectLectureList() {
		return cMapper.selectLectureList();
	}

	public int captherCount(int lecNo) {
		return cMapper.captherCount(lecNo);
	}

}
