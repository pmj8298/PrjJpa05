package com.green.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 실제 database 의 table 구조를 만든다 : Create Table 을 실행함
// import jakarta.persistence.
// Entity 에서 column 을 잡을 때 첫글자 대문자로 씀 ex-Long / 왜냐면 long 을 쓰게 되면 null 입력안됨 -> Long
// table 도 만들고 insert 할 대 저장하는 역할
// @nonnull, @null 사용도 가능
// @NoArgsConstructor : 기본생성자 - default constructor
// Getter / Setter 없으면 toString이 값을 꺼낼때 문제 생겨서 수정용 데이터를 못받아옴
// @Entity - vo 역할 함
@Entity
@NoArgsConstructor
@Setter
@Getter
@SequenceGenerator(name="ARTICLE_SEQ_GENERATOR", 
	sequenceName   = "ARTICLE_SEQ", 
	initialValue   = 1,   // 초기값
	allocationSize = 1)   // 증가치
public class Article {
	// primary key        : @Id
	// 값을 자동으로 채움 : @GeneratedValue
	@Id
	@GeneratedValue (strategy = GenerationType.SEQUENCE, generator="ARTICLE_SEQ_GENERATOR")
	//@GeneratedValue 
	// create sequence article_seq start with 1 increment by 50
	private Long id;
	@Column
	private String title;
	@Column
	private String content;
	
	// 자바에 사용하기 위해 만든 내용들
	//  이거 ArticleDto 에서 쓰려고 만듦
	// 생성자(@AllArgsConstructor를 위에 적든지, 걍 생성자를 적든지 둘 중 하나만)
	public Article(Long id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}
	
	// ToString()
	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", content=" + content + "]";
	}

	// 수정하기 위한 용도로 추가
	public void patch(Article article) {
		if(article.title != null)
			this.title = article.title;
		if(article.content != null)
			this.content = article.content;
	}

	
	
}
