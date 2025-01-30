package com.sinuedu.home.service;

import com.sinuedu.board.lecture.model.mapper.LectureMapper;
import com.sinuedu.board.qna.model.mapper.QnaMapper;

import org.springframework.stereotype.Service;
import java.util.Map; // ✅ 추가
import java.util.HashMap; // ✅ 추가

@Service // <== 이 부분이 꼭 필요함
public class HomeService {

    private final LectureMapper lectureMapper;
    private final QnaMapper qnaMapper;


    public HomeService(LectureMapper lectureMapper, QnaMapper qnaMapper) {
        this.lectureMapper = lectureMapper;
        this.qnaMapper = qnaMapper;
    }

    public Map<String, Object> getHomeData(int userNo) {
        Map<String, Object> homeData = new HashMap<>();
        homeData.put("popularLectures", lectureMapper.PopularLectures());
        homeData.put("popularQna", qnaMapper.PopularQna());

        homeData.put("myRecentQna", qnaMapper.getRecentQnaByUser(userNo)); // 내가 쓴 질문
        homeData.put("myRecentComments", qnaMapper.getRecentCommentsByUser(userNo));
        return homeData;
    }
}
