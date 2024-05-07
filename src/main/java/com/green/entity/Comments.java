package com.green.entity;

import com.green.dto.CommentDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity             // 클래스를 테이블로 생성한다
@Data               // @Getter+@Setter+@ToString+HashCode+equals
@NoArgsConstructor  // 기본생성자
@AllArgsConstructor // 모든 인자 생성자
@SequenceGenerator(name="COMMENTS_SEQ_GENERATOR", sequenceName="COMMENTS_SEQ", initialValue   = 1, allocationSize = 1 )
public class Comments {
	
	@Id               // 기본키(import jakarta.persistence.Column)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="COMMENTS_SEQ_GENERATOR")
	private Long    id;            // id
	
	@ManyToOne                     // 관계 : 다대일 설정(Comments<->Article)
	@JoinColumn(name="article_id") // 외래키 이름이 아니고 외래키 칼럼임 (부모키 Article id 칼럼)- 외래키 이름은 FKnwlc4xv9fm2swgesuas3uf3mo
	private Article article;       // 연결될 entity 객체의 이름
	
	// @Column(name="nick_name", nullable=false(== notnull), length = 255)
	// oracle 11g varchar2 최대 4000  byte
	// oracle 12c varchar2 최대 32767 byte
	@Column
	private String  nickname;      // nikcname
	
	@Column
	private String  body;          // body

	public static Comments createComment(CommentDto dto, Article article) {
		
		if(dto.getId() != null)    // 입력된 댓글에 id 가 존재하면(외래키 위반 사유가 될 수 있어서)
			throw new IllegalArgumentException(  // 에러가 발생하면 리턴문으로 가지 않음
					"댓글 생성 실패! 댓글의 id가 없어야 합니다");
		
		// dto.getArticleId() : 입력받은 게시글의 id
		// article.getId()    : 조회한 게시글의 id 
		System.out.println("article:" + article);
		System.out.println("dto:" + dto);
		if(dto.getArticleId() != article.getId())
			throw new IllegalArgumentException(
					"댓글 생성 실패! 게시글의 id가 잘못되었습니다");
		
		return new Comments( 
				dto.getId(),          // 입력받은 댓글 아이디(dto 는 입력받은 것임)
				article,              // 조회한 부모 게시글 정보
				dto.getNickname(),    // 입력받은 댓글 닉네임
				dto.getBody()         // 입력받은 댓글 내용
				);
	}

	public void patch(CommentDto dto) {
		if(this.id != dto.getId())
			throw new IllegalArgumentException("댓글 수정 실패! 잘못된 아이디가 입력되었습니다");
		if(dto.getNickname() != null)      // 수정할(입력받은) 닉네임이 존재하면
		this.nickname = dto.getNickname(); 
		if(dto.getBody() != null)          // 입력받은(수정할) 댓글 내용이 존재하면
		this.body = dto.getBody();
	}
}
