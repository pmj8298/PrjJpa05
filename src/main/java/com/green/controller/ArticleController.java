package com.green.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.green.dto.ArticleDto;
import com.green.dto.ArticleForm;
import com.green.dto.CommentDto;
import com.green.entity.Article;
import com.green.repository.ArticleRepository;
import com.green.service.CommentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ArticleController {
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private CommentService commentService;

  // data 입력
  @GetMapping("/articles/WriteForm")
  public String writeForm() {
	  return "articles/write"; // src/main/resources/templates/   articles/write .mustache
  }
  
  // data 저장
  // 405 error : method="POST" -> @GetMapping(post 로 던졌는데 get 으로 받아서 안맞음)
  // error : @GetMapping("/articles/Write")
  // FormData - title: a content: ㅁ
  // Dto 가 Vo 임 둘이 같은 것
  // console 창 : ArticleDto [title=a, content=ㅁ] -> ArticleDto.java의 toString과 같은 형태
  // article 은 java 의 class 와 동일(table 아님)
  @PostMapping("/articles/Write")
  public String write( ArticleDto articleDto) {
	  // 넘어온 데이터 확인
	  System.out.println( "결과:" + articleDto.toString() ); // 책: ArticleForm
	  // db 에 저장 h2 article 테이블에 저장
	  // Entity : db 의 테이블이다
	  // 1. Dto -> Entity : Dto 를 Entity 에 담는 작업
	  Article article = articleDto.toEntity();
	  // 2. repository (interface)를 사용하여 Entity 를 보내서 저장
	  Article saved = articleRepository.save(article); // save 라는 명령을 쓰면 INSERT 가능
	  System.out.println("saved:"+saved);
	  
	  return "redirect:/articles/List";
  }
  
  // 1번 데이터 data 조회 : PathVariable -> GET
  // java.lang.IllegalArgumentException: Name for argument of type
  // 1번 방법. @PathVariable(value="id") 추가
  // 2번 방법. sts 설정 추가 방식
  // 프로젝트의 properties 선택 -> Java Compiler -> enable project specific settings 체크-> store information... 체크
  // ✔- 이모지: window 키 + .
  // No default constructor for entity'com.green.entity.Article' error : Article 에 @NoArgsConstructor 추가
  // localhost:9090/articles/1✔
  @GetMapping("/articles/{idx}")
  public String view(@PathVariable(value="idx") Long idx, Model model) {
	  
	  // 1번 방법
	  // Article articleEntity = articleRepository.findById(id); // Error
	  // Type mismatch error
	  // Optional<Article> articleEntity = articleRepository.findById(id);
	  // 값이 있으면 Article을 리턴, 값이 없으면 null 리턴
	  
	  // 2번 방법 추천
	  // id 라는 값을 넘겨서 받고, 값이 없으면 orElse null 로 넘어가라
	  // id 에 해당하는 게시글 조회
	  Article articleEntity = articleRepository.findById(idx).orElse(null);
	  System.out.println("1번 조회 결과:" + articleEntity);
	  model.addAttribute("article",articleEntity); // 조회한 게시글 결과 -> model
	  
	  // 댓글 목록 조회 ex) 4번 게시글의 댓글 목록 -> model 에 추가
	  List<CommentDto> commentDtos = commentService.comments(idx);
	  model.addAttribute("commentDtos",commentDtos);
	  
	  
	  return "articles/view"; // articles/view.mustache
  }
  
  @GetMapping("/articles/List")
  public String list(Model model) {
	  
	  // List <Article> articleEntityList = articleRepository.findAll();
	  // 1. 오류처리 1번 방법
	  // List <Article> articleEntityList = (List<Article>) articleRepository.findAll();
	  
	  // 2. ArticleRepository interface 에 함수를 등록 -추천
	  List <Article> articleEntityList = articleRepository.findAll();
	  System.out.println("전체 목록:" + articleEntityList);
	  model.addAttribute("articleList",articleEntityList);	  
	  return "articles/list"; // /templates/articles/list  .mustache
  }
  
  // 데이터 수정페이지로 이동
  @GetMapping("/articles/{id}/EditForm")
  public String editForm(@PathVariable(value="id") Long id, Model model) {
	  // 수정할 데이터를 조회한다
	  // 한 개를 조회할 때 orElse 넣어줌 -> 값이 없을 수도 있응까
	  Article articleEntity = articleRepository.findById(id).orElse(null);
	  
	  // 조회한 데이터를 model 에 저장
	  // edit.mustache에서 <input type="text" name="title" class="form-control" value="{{ article.title }}" /> 중 article 이 "" 안에 들어가야 함
	  model.addAttribute("article",articleEntity); 
	  // 수정 페이지로 이동한다
	  
	  return "articles/edit";
  }
  
  //  데이터 수정
  // ArticleDto는 2개만 받아올 수 있어서 Article 로 함
  @PostMapping("/articles/Edit")
  // @PostMapping("/articles/{id}/Edit") Get 방식에서 @PathVariable(value="id")를 쓸 수 있는데 Post 방식 에서는 쓸 수 없어서 {id}를 뺌
  // 대신 edit.mustache 에서 <input type="hidden" name="id" value="{{ id }}" />로 id 를 넣어줌
  public String edit(ArticleForm articleForm) {
	  log.info("수정용 데이터:" + articleForm.toString());
	  
	  // db 수정
	  // 1. Dto -> Entity 로 변환 
	  /*
	  Long   id      = articleForm.getId();
	  String title   = articleForm.getTitle();
	  String content = articleForm.getContent();
	  Article articleEntity = new Article(id, title, content); 
	  */
	  Article articleEntity = articleForm.toEntity(); 
	  
	  // 2. Entity 를 db 에 수정한다
	  // 2-1. 수정할 데이터를 찾아서(db 의 data 를 가져온다)
	  Long    id      = articleForm.getId();
	  Article target  = articleRepository.findById(id).orElse(null);
	  
	  
	  // 2-2. 필요한 데이터를 변경한다
	  if(target != null) { // 자료가 있으면 저장한다(수정)
		  articleRepository.save(articleEntity);
		  
	  }
	  
	  return "redirect:/articles/List";
  }
  
  // 데이터 삭제
  // <a>를 써서 호출을 했기 때문에 Get 방식을 씀
  // 글고 {}(@PathVariable)이 주소줄에 포함되어있기 때문에 Get 방식을 써야 함
  @GetMapping("/articles/{id}/Delete")
  public String delete(@PathVariable(value="id") Long id, RedirectAttributes rttr) {
	  
	  // 1. 삭제 할 대상을 검색한다
	  Article target = articleRepository.findById(id).orElse(null);
	  
	  // 2. 대상 Entity 를 삭제한다
	  if(target != null) {
		  articleRepository.delete(target);

	  // RedirectAttributes :  리다이렉트 페이지에서 사용할 데이터를 넘겨주는 역할
	  // 한 번 쓰면 사라지는 휘발성 데이터
	  // 삭제 후 임시메시지를 list.mustache가 출력한다
	     rttr.addFlashAttribute("msg", id + "번 자료가 삭제되었습니다");
	  // header.mustache 에 출력
	  }
	  return "redirect:/articles/List";
  }
}
