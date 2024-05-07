package com.green.repository;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import com.green.entity.Article;

// extends CrudRepository<Article, Long>
// JPA의 Crud 기능을 동작시키는 class
// findAll - return 값이 원래는 Iterable 값임 -> ArrayList 값으로 리턴받고 싶어서 바꾼 것(Iterable은 부모임 -> 부모를 자식에 넣지 못 함)
// -able : Interface 
public interface ArticleRepository 
  extends CrudRepository<Article, Long>{
	
	
	
	// CrudRepository 안에 saved 가 만들어져 있어서 따라 안만들어도 되는거임
	// 다만 타입이 맞지 않는 경우가 생길 수 있음

	   // ArticleController 83 line 형변환 해결방법
	   // 상속관계를 이용하여 List 를 Iterable 로 UpCasting 하여 형변하지 않음
	   // Iterable(I) <- Collection(C) <- List(I) <- ArrayList(C)
	   // 아래 내용 추가
	
	// alt + shift + s : Override/Implement method -> 사용가능한 함수 목록 확인 가능
	
	   @Override
	   ArrayList<Article> findAll();
	   
}
