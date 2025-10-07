package com.sinuedu.user.manager.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sinuedu.board.lecture.model.service.LectureService;
import com.sinuedu.board.lecture.model.vo.Category;
import com.sinuedu.board.lecture.model.vo.Chapter;
import com.sinuedu.board.lecture.model.vo.Image;
import com.sinuedu.board.lecture.model.vo.Lecture;
import com.sinuedu.cloudflare.model.service.R2Service;
import com.sinuedu.user.manager.exception.ManagerException;
import com.sinuedu.user.manager.model.service.ManagerService;
import com.sinuedu.user.member.model.vo.Member;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loginUser")
@RequestMapping("/manager")
public class ManagerController {

	private final ManagerService mService;
	private final LectureService lService;
	private final R2Service r2Service;

	@GetMapping("/userList")
	public String userList(Model model) {
		List<Member> list = mService.userList();
		model.addAttribute("list", list);
		return "userList";
	}

	@GetMapping("/chapterList")
	public String chapterList(Model model) {
		List<Chapter> list = mService.chapterList();
		System.out.println(list);
		Set<String> categories = list.stream().map(Chapter::getCgName).collect(Collectors.toSet());
		model.addAttribute("list", list);
		model.addAttribute("categories", categories);
		return "chapterList";
	}

	@GetMapping("/lectureAdd")
	public String lectureAddPage(Model model) {
		List<Category> categories = mService.categoryList();
		model.addAttribute("categories", categories);
		return "lectureAdd";
	}
	
	//수정해야함
	@PostMapping("lectureInsert")
	public String lectureInsert(@ModelAttribute Lecture lec, @RequestParam("file") MultipartFile file) {

		
		System.out.println(lec);
		
		int result1 = 1;
		int result2 = 1;
		
		result1 = mService.insertLecture(lec);
		
		System.out.println("insert 후 : "+lec);

		
		if(result1 + result2 == 2) {
			return "redirect:/manager/chapterList";
		}else {
			throw new ManagerException("오류 발생");
		}
		
	}

	
	@PostMapping("lectureUpdate")
	public String lectureUpdate(@RequestParam("imgName") String imgName,@ModelAttribute Lecture lec, @RequestParam("file") MultipartFile file) {
		
		Image image = new Image();
		int lecNo = lec.getLecNo();
		
		int result1 = 1;
		int result2 = 1;
		
		//글 내용 추가
		result1 = mService.updateLecture(lec);
		
		//썸네일 파일이 존재 유무
		int thumbnail = mService.lectureThumbnailYN(lecNo);
		
		//file이 아무것도 안들어왔을 때는 기본 썸네일로 대체
		if(!file.isEmpty()) {
			
			String originalFileName = file.getOriginalFilename();
			
			try {
				System.out.println(file.getOriginalFilename());
				
				String fileUrl = r2Service.uploadFile(file);

				//바뀐 이름
				String rename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
				
				image.setImgName(originalFileName);
				image.setRefLecNo(lecNo);
				image.setImgPath(fileUrl);
				image.setImgRename(rename);
				
				
				//썸네일이 이미 있을 경우 insert가 아닌 update이므로 구별하기 위해 사용
				if(thumbnail == 0) {
					result2 = mService.insertFile(image);
				}else {
						
					if(r2Service.deleteFile(mService.lectureThumbnail(lecNo).getImgRename())) {
						result2 = mService.updateFile(image);
					}else {
						throw new ManagerException("삭제 오류 발생");
					}
				}			
				if(result1 + result2 == 2) {
					return "redirect:/lecture/" + lec.getLecNo();
				}else {
					throw new ManagerException("오류 발생");
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}else {
			
			//기본 썸네일로 대체하지만, 이미 기존에 썸네일이 있을 때 
			//그리고 애초에 썸네일이 있지만 선택안하고 넘어갈 때는, 파일자체에는 담긴 게 없어서 내용만 수정해야되는데 없는 판정이되어서 이것도 걸러주기위해
			//썸네일이 있는 상태로 그냥 넘길 경우
			//- 기존 썸네일이 그대로 있어야됨

			//썸네일이 있는 상태로 '썸네일 추가'로 썸네일을 비워둘 경우
			//- 기존 썸네일이 삭제되야됨

			//- 둘 다 파일이 없는 상태
			
			if(thumbnail == 1 && imgName.equals("썸네일 추가")) {			
				//기존 썸네일 제거
				if(r2Service.deleteFile(mService.lectureThumbnail(lecNo).getImgRename())) {
					int deleteThumbnail = mService.deleteThumbnail(lecNo);
					
					if(deleteThumbnail == 1) {
						System.out.println("삭제 완료했습니다");
					}else {
						System.out.println("삭제 중 오류가 발생했습니다");
					}
				}
			}else {
				System.out.println("기존 썸네일 있는 상태에서 그대로 진행");
			}
			result2 = 0;
		}

		return "redirect:/lecture/" + lec.getLecNo();


	}
	
	
	//수정해야됨
	private void deleteFile(String imgRename) {
		String SavePath = "d:\\dev\\uploadFiles";
		
		File f = new File(SavePath + "\\" + imgRename);
		if(f.exists()) {
			f.delete();
		}
	}

	@GetMapping("/chapterAdd")
	public String chapterAdd(Model model) {
		List<Chapter> chapters = mService.chapterList();
		List<Lecture> lectures = mService.lectureList();
		Set<String> categories = chapters.stream().map(Chapter::getCgName).collect(Collectors.toSet());
		model.addAttribute("categories", categories);
		model.addAttribute("lectures", lectures);
		return "chapterAdd";
	}

	@PostMapping("/deleteChapter")
	@ResponseBody
	public int deleteChapter(@RequestParam("chapNo") int chapNo) {
		mService.deleteViewChapter(chapNo);
		return mService.deleteChapter(chapNo);
	}

	@PostMapping("/insertChapter")
	@ResponseBody
	public Map<String, Object> insertChapter(@ModelAttribute Chapter chapter) {
		Map<String, Object> response = new HashMap<>();
		try {
			int result = mService.insertChapter(chapter);
			if (result > 0) {
				response.put("success", true);
			} else {
				response.put("success", false);
				response.put("message", "챕터 추가에 실패했습니다.");
			}
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "서버 오류: " + e.getMessage());
		}
		return response;
	}

	@GetMapping("/chapterEdit")
	public String chapterEdit(@RequestParam("chapNo") int chapNo, Model model) {
		Chapter chapter = mService.getChapterByTitle(chapNo);
		List<Chapter> chapters = mService.chapterList();
		List<Lecture> lectures = mService.lectureList();
		Set<String> categories = chapters.stream().map(Chapter::getCgName).collect(Collectors.toSet());

		model.addAttribute("chapter", chapter);
		model.addAttribute("categories", categories);
		model.addAttribute("lectures", lectures);
		return "chapterEdit";
	}

	@PostMapping("/updateChapter")
	@ResponseBody
	public Map<String, Object> updateChapter(@ModelAttribute Chapter chapter) {
		Map<String, Object> response = new HashMap<>();
		try {
			int result = mService.updateChapter(chapter);
			response.put("success", result > 0);
			response.put("message", result > 0 ? "성공적으로 수정되었습니다." : "수정에 실패했습니다.");
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "서버 오류: " + e.getMessage());
		}
		return response;
	}
	
	@PostMapping("/toggleAdmin")
	@ResponseBody
	public Map<String, Object> toggleAdmin(@RequestParam("userId") String userId, @RequestParam("adminStatus") String adminStatus) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        int result = mService.updateAdminStatus(userId, adminStatus);
	        response.put("success", result > 0);
	    } catch (Exception e) {
	        response.put("success", false);
	    }
	    return response;
	}

	@PostMapping("/toggleStatus")
	@ResponseBody
	public Map<String, Object> toggleStatus(@RequestParam("userId") String userId, @RequestParam("status") String status) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        int result = mService.updateUserStatus(userId, status);
	        response.put("success", result > 0);
	    } catch (Exception e) {
	        response.put("success", false);
	    }
	    return response;
	}
	
	@GetMapping("lectureDelete/{lNo}/{imgRename}")
	public String lectureDelete(@PathVariable("lNo") int lecNo, @PathVariable("imgRename") String imgRename) {
		mService.deleteAllChapter(lecNo);
		System.out.println(imgRename);
		deleteFile(imgRename);
		int result = mService.deleteLecture(lecNo);
		
		if(result >0) {
			return "redirect:/lecture/list";
		}else {
			throw new ManagerException("삭제 중 오류가 발생했습니다");
		}
	}
	
	@GetMapping("lectureUpdate/{lNo}")
	public ModelAndView lectureUpdate(@PathVariable("lNo") int lecNo, ModelAndView mv) {
		
		//lecNo가 없으면 리스트로 뽑히기 때문에 일단 리스트로 뽑고
		ArrayList<Lecture> lList = lService.selectLectureList(lecNo);
		
		//그 다음 lecNo가 있으면 list안에 글 한 개만 담기기 때문에 0번째 인자 가져오기
		Lecture lecture = lList.get(0);
		
		//선택할 카테고리 가져오기
		List<Category> categories = mService.categoryList();
		
		//글 정보 가져오고 없으면 Null
		Image thumbnail = mService.lectureThumbnail(lecNo);
		
		if(thumbnail == null) {
			mv.addObject("thumbnail", null);
		}else {
			mv.addObject("thumbnail", thumbnail);
		}
		
		mv.addObject("categories", categories).addObject("lec", lecture).setViewName("lectureEdit");
		return mv;
	}
}