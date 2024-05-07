package com.green.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.green.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // 필수입력(not null)항목 생성자
@Service
// 반드시 *****UserDetailsService interface 를 implement 해야함
public class UserDetailService implements UserDetailsService{
	
	private UserRepository userRepository;

	// 로그인에 필요한 Username 아이디(email)를 통해서
	// UserDetail 정보를 가지고 온다
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserDetails userDetails = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException(email));
		return userDetails;
	}

}
