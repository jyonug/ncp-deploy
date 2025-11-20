package com.lgdx.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lgdx.web.entity.MsgMember;
import com.lgdx.web.mapper.MsgMemberMapper;

import jakarta.servlet.http.HttpSession;

@Controller
public class MyController {
	// DB를 연결해서 데이터를 조회, 생성하는 기능을 처리하는 컨트롤러
	
	// 데이터 확인하는 용도로 console 출력할 도구
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	// Spring Container가 해당하는 인터페이스 구현체를 직접 생성해서 주입
	@Autowired
	private MsgMemberMapper mapper;
	@PostMapping("/join")
	public String join(MsgMember mem, Model model) {
		// 1. 요청 데이터 수집하기
		// -> 수집해야 할 데이터가 4가지네? -> 낱개로 수집하면 불편함
		// -> 내가 해당하는 데이터를 하나로 표현 할 수 있는 자료형을 만들자!
		// -> MsgMember생성 -> 요청 데이터를 매개변수로 수집
		logger.info("수집한 데이터 확인 >> " + mem);
		// 2. DB에 연결해서 데이터 추가
		mapper.join(mem); // join메소드 안에 mem이라는 변수 만들겠다
		// model 영역에 회원가입한 email 값만 담아서 다음페이지로 넘겨주기
		model.addAttribute("email", mem.getEmail()); // 가벼운 기능
		
		return "JoinSuccess";
	}
	
	// 로그인에 대한 작업 작성하기
	// 1. 들어온 요청을 판단할 수 있는 작업 수행
	// 2. 해당 요청에 따라 응답할 수 있는 작업 수행
	
	//	  - DB에 연결하여 회원의 정보가 있는지 없는지 확인 필요!
	@PostMapping("/login")
	public String login(MsgMember mem, HttpSession session) {
		
		// 로그인의 기능은 DB에서 회원에 대한 정보가 체크된다면 main 페이지로 이동시, 해당 회원에 대한 정보를 담아서 이동할 수 있도록 한다
		MsgMember member = mapper.login(mem);
		logger.info("로그인 된 회원정보 >> " + member.toString());
		
		// 모든 페이지 기능에서 활용할 수 있는 Session Scope 영역 사용! 좀 더 무거운 정보들을 불러올 수 있음,,
		session.setAttribute("member", member); // session에 member데이터 저장한다
		return "Main";
	}
	
	// GET 요청
	// 요청 키워드 : logout
	// 결과 화면 : Main
	// +) Session 영역에 저장된 member 데이터 삭제!
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
		session.removeAttribute("member");
		return "Main";
	}
	
	// 업데이트 기능을 위한 요청 진행하기
	// 1. 요청 Mapping
	@PostMapping("/update")
	// 2. 응답의 구조 생성(form태그를 통해 입력받은 데이터를 활용)
	// 3. DB에 해당 데이터를 전달 -> update sql구문 실행!
	// --> email이 일치할 때 pw, tel, address가 수정되도록!
	// 4. 결과 화면은 main으로 이동
	public String update(MsgMember mem, HttpSession session) {
		
//		MsgMember mem2 = (MsgMember) session.getAttribute("member"); // member를 가지고 오겠다
//		mem.setEmail(mem2.getEmail()); // 이메일에 대한 정보를 수정하겠다
		
		mapper.update(mem); // form에서 받아온 데이테를 sql구문으로 넘겨준다
		
		session.setAttribute("member", mem); // 세션을 바꿔주겠다 
		
		return "Main";
	}
	
	@GetMapping("/showMember")
	public String showMember(Model model) {
		
		List<MsgMember> list = mapper.showmember();
		model.addAttribute("list", list); // 정보를 model에서 불러오겠당
		
		return "ShowMember"; // 파일이름과 같아야함!
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("email") String email) { // email값을 가지고 올거고 스트링 타입의 email에 저장을 할거야
		
		// @RequestParam() : 쿼리 파라미터 값을 받아오기 위해 사용
		// 쿼리 파라미터 중 email 값을 String email 변수 바인딩
		mapper.delete(email);
		// *redirect : 클라이언트에게 새로운 url로 다시 요청을 보내도록 지시할 때 사용
		return "redirect:showMember"; 
	}
}

