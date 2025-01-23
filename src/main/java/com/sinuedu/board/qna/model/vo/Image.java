package com.sinuedu.board.qna.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Image {
	private int imgNo;
	private String imgPath;
	private String imgName;
	private String imgRename;
	private String boardType;
	private int boardId;
}
