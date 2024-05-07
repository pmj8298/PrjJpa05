package com.green.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.green.dto.CommentDto;
import com.green.service.CommentService;

// 댓글 조최, 추가, 수정, 삭제
@RestController
public class CommentApiController {
	
	@Autowired
	 private CommentService commentService;
	
	// 1. 댓글 조회(GET)
	@GetMapping("/api/articles/{articleId}/comments")
	public ResponseEntity<List<CommentDto>> comments(@PathVariable Long articleId){
		
		// 정보조회를 서비스에게 위임
		List<CommentDto> dtos = commentService.comments(articleId);
		
		// ResponseEntity : Status.OK + dtos(ArrayList -> 나중에 json 으로 출력(이유: @RestController 라서))를 return
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}
	
	 
	
	// 2. 댓글 생성(추가)(POST)
	// Post http://localhost:9090/api/articles/4/comments
	// 입력 data  : {"article_id":4, "nickname":"Hoon", "body":"이프 온리"} - 새 댓글
	// 결과       : {"id": null,"article_id": 4,"nickname": "Hoon","body": "이프 온리"}
	// error 입력 : {id: 4,"article_id": 4,"nickname": "Hoon","body": "이프 온리"}
	// 결과       : 400(Bad Request) error 발생 - id: 4 -> "id:: 4 로 변경해주어야 함 즉, 입력데이터 키가 json type 으로 ""안에 저장되어있어야 한다
	// error 입력 : {"id": 4,"article_id": 4,"nickname": "Hoon","body": "이프 온리"}
	// 결과       : 500 error 발생 - 댓글 생성 실패 댓글의 id가 없어야 합니다
	// error 입력 : {"id":null,"article_id":3, "nickname":"Hoon", "body":"이프 온리"}
	// 결과       : 500 error 발생 - "댓글 생성 실패! 게시글의 id가 잘못되었습니다",
	@PostMapping("/api/articles/{articleId}/comments")
	
	public ResponseEntity<CommentDto> create(
			@PathVariable Long articleId, // {articleId}- 게시글 번호
			@RequestBody CommentDto dto   // 입력된 자료들 - input, select : JS fetch 의 body 안에 있는 것(_new.mustache)
			){
		CommentDto createdDto = commentService.create(articleId,dto);
		 // 결과 응답
			return  ResponseEntity.status(HttpStatus.OK).body(dto);
		
	}
	
	// 3. 댓글 수정(Patch)
	// Patch http://localhost:9090/api/comments/7
	// 수정 전 데이터 {"article_id": 6,	"id":7,	"body":"조깅", "nickname":"Park"}
	// 입력데이터{ "article_id": 6,	"id":7,	"body":"수영", "nickname":"Park2" }
	 @PatchMapping("/api/comments/{id}")
	 public ResponseEntity<CommentDto> update(
			 @PathVariable Long id, 
			 @RequestBody CommentDto dto  // 수정할 데이터를 가지고 있다
			 ) {
		 
		 CommentDto updateDto = commentService.update(id, dto);
		 
		 return ResponseEntity.status(HttpStatus.OK).body(updateDto);
	 }
	
	// 4. 댓글 삭제(Delete)
	// Delete http://localhost:9090/api/comments/7
	@DeleteMapping("/api/comments/{id}")
	 public ResponseEntity<CommentDto> delete(@PathVariable Long id){
		CommentDto deletedDto = commentService.delete(id);
		
		return ResponseEntity.status(HttpStatus.OK).body(deletedDto);
	}
	
	
}
