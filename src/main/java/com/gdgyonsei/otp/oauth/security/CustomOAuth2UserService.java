package com.gdgyonsei.otp.oauth.security;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.gdgyonsei.otp.model.Member;
import com.gdgyonsei.otp.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;


import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final HttpSession session;

    public CustomOAuth2UserService(MemberRepository memberRepository, HttpSession session) {
        this.memberRepository = memberRepository;
        this.session = session;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 사용자의 구글 계정에서 받아온 정보로 name, email 값 획득
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String name = attributes.get("name").toString();
        String email = attributes.get("email").toString();

        // 신규 회원인 경우 name, email 을 구글에서 획득한 값으로 설정
        // email 은 대부분의 경우 고유하기 때문에 추후에도 수정 불가능
        // name 은 유저가 추후 수정 가능
        // email 이 (대부분의 경우) 고유하고 변경 불가능하기에 이를 기준으로 멤버를 찾는다
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) {
            Member newMember = new Member();
            newMember.setName(name);
            newMember.setEmail(email);

            // 새로운 사용자 정보를 세션에 저장하여 추가 정보 입력 페이지로 리디렉션
            // 위 정보들을 당장 DB에 저장하는 것이 아니라 세션에 저장해 놓고 추후 추가정보까지 입력받은 후 저장한다
            //
            session.setAttribute("isNewMember", Boolean.TRUE);
            session.setAttribute("newMember", newMember);
        }
        else {
            session.setAttribute("isNewMember", Boolean.FALSE);
        }


        return oAuth2User;
    }
}
