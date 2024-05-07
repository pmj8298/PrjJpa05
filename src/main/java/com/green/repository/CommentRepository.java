package com.green.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.green.entity.Comments;


public interface CommentRepository 
	extends JpaRepository<Comments, Long> {

	// @Query annotation 을 통해 findByArticleId()을 실행한다
	// @Query annotation 은 jpa 의 기능을 사용하지 않고 @Query 안의 sql 을 사용한다
	// findByArticleId() 함수는 comments table 에 있는 column 을 사용해서 만든다
	// Native Query Method - oracle 문법으로 작성된 쿼리를 입력하여 조회
	// nativeQuery = true  : 오라클 전용 함수
	// nativeQuery = false : JPQL(JPA 안에 있는 sql 문을 의미) 문번, JPA 함수
	// :articleId - parameter(넘어온 파라미터) 로 조회한다, ? 자리에 : 으로 대체해준다, : + 변수명
	// JPQL - jpa 용 sql - db 종류와 상관없이 사용가능
	// QueryDsl - JPA 에서 oracle 전용 sql 문을 실행하는 기술
	@Query(value="SELECT * FROM comments WHERE article_id=:articleId",
			nativeQuery=true)
	List<Comments> findByArticleId(Long articleId);
	
	// native query xml : 
	// src/main/resources/META-INF/orm.xml -폴더와 파일 생성해야 한다
	// orm.xml 에 sql 을 저장해서 findByNickname() 함수 호출
	List<Comments> findByNickname(String nickname);
	
}
