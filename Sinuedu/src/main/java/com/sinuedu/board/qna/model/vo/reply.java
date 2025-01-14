package com.sinuedu.board.qna.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class reply {
	   private int repNo;
	   private String repComment;
	   private int userNo;
	   private int qnaNo;
}
