package com.green.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.green.dto.CommentDto;
import com.green.entity.Article;
import com.green.entity.Comments;
import com.green.repository.ArticleRepository;
import com.green.repository.CommentRepository;

import jakarta.transaction.Transactional;

@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private ArticleRepository articleRepository;
	
	
	
	// 1. 댓글 조회
	public List<CommentDto> comments(Long articleId) {
		
		/*
		// 1. 댓글을 db 에서 조회, Entity 에 담는다
		List<Comments> commentList = commentRepository.findByArticleId(articleId);
		
		// 2. Entity -> Dto 로 변환
		List<CommentDto> dtos = new ArrayList<CommentDto>();
		for (int i = 0; i < commentList.size(); i++) {
			Comments c = commentList.get(i);
			CommentDto dto = CommentDto.createCommentDto(c);
			dtos.add(dto);
		}
		// 3. 결과를 반환
		
		return dtos;
	}
	 */
	
	/*
	 ArrayList.stream()
	 .map((comment) -> {
	   // 집합.map : stream 전용함수
	    // 집합(Collection) 에 대해 element 들을 반복적으로 조작 
	     // .map() vs .forEach() - 둘 다 배열을 모두 조작한다
	      // .map() : 내부의 element 의 값이나 사이즈를 변경할 때 사용 : ex) 모두 대문자로 변경
	       // .forEach() : 내부의 element 의 값이나 사이즈가 변경되지 않을 때 사용 : ex) 모두 대문자로 변경
		 
	 CommentDto.createCommentDto(comment)
	 })
	 

	  */
	return commentRepository.findByArticleId(articleId)
			.stream()
			.map(comment -> CommentDto.createCommentDto(comment))
			.collect(Collectors.toList()); // stream 을 다시 ArrayList 로 변경
	}

// Article-게시글, Comment-댓글
	
	// 2. 댓글 생성
	@Transactional // 오류발생 시 db 를 rollback 하기 위해서(throw 를 사용하는 이유가 @Transactional을 발생시키기 위해)
	public CommentDto create(Long articleId, CommentDto dto) {
		// 1. 게시글 조회 및 조회실패 시 예외 발생
		// 게시글에 존재하지 않는 articleId 가 넘어오면 조회 결과가 없다 즉 throw 한다
		Article article = articleRepository.findById(articleId).orElseThrow( () -> new IllegalArgumentException("댓글 생성 실패! 대상 게시물이 없습니다")); // 조회와 예외처리를 동시에 함
		
		// 2. 댓글을 저장하는 Entity 생성 -> 저장할 데이터(comments)를 만든다
		Comments comments = Comments.createComment(dto, article);
		
		// 3. 댓글 Entity 를 db 에 저장
		Comments created = commentRepository.save(comments);
		
		// 4. 저장된 Comments type created -> dto 로 변환 후 Controller 로 return
		// 변환하는 이유가 Controller 에서 Entity type 을 사용하지 않기 위해
		return CommentDto.createCommentDto(created);
	}

	// 3. 댓글 수정
	@Transactional
	public CommentDto update(Long id, CommentDto dto) {
		// 1. 기존 댓글 조회 및 예외 발생(수정할거 없어요)
		Comments target = commentRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("댓글 수정 실패! 수정할 댓글이 없습니다")); // id : 댓글의 id
		
		// 2. 댓글 수정 : 조회한 데이터의 내용을 수정(class 안의 내용을 변경, 실제로 db 를 고치는 것이 아님)
		// dto    : 수정할 입력받은 데이터
		// target : 수정할 원본 데이터
		target.patch(dto); // target 안에다가 dto(nickname, body) 를 넣어줌
		
		// 3. db 정보를 수정
		Comments updated = commentRepository.save(target);
		
		// 4. updated -> commentDto로 변경하여 결과 return
		return CommentDto.createCommentDto(updated);
		
	}
	
	// 4. 댓글 삭제
	@Transactional
	public CommentDto delete(Long id) {
		// 1. 삭제할 댓글 조회 및 예외 처리
		Comments target = commentRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("댓글 삭제 실패! 대상이 없습니다"));
		// 2. 실제 db 에서 삭제
		commentRepository.delete(target);
		// 3. 삭제 댓글을 dto 로 변환 후 리턴
		return CommentDto.createCommentDto(target);
	}
}
