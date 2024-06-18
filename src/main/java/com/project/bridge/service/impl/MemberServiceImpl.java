package com.project.bridge.service.impl;

import com.project.bridge.entity.MailAuthEntity;
import com.project.bridge.entity.MemberEntity;
import com.project.bridge.repository.MailAuthRepository;
import com.project.bridge.repository.MemberRepository;
import com.project.bridge.service.EmailService;
import com.project.bridge.service.MemberService;
import com.project.bridge.util.SHA256Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    @Qualifier("emailServiceImpl")
    private EmailService emailService;

    @Autowired
    private MailAuthRepository mailAuthRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Long sendAuthMail(String email) throws RuntimeException {
        String authNo = generateAuthNo();

        String title = "회원가입 인증번호입니다.";

        StringBuilder sb = new StringBuilder();
        sb.append("인증번호는 ").append(authNo).append(" 입니다.");

        if (!emailService.sendEmail(email, title, sb.toString())) {
            throw new RuntimeException("이메일 전송에 실패했습니다.");
        }

        mailAuthRepository.updateFailStatusByEmail(email, LocalDateTime.now());

        // 인증번호를 저장하고, 인덱스를 반환
        MailAuthEntity authData = mailAuthRepository.save(MailAuthEntity.builder()
                .email(email)
                .status(false)
                .expiredDt(LocalDateTime.now().plusMinutes(30)) // 30분뒤 만료
                .authNo(authNo)
                .build());

        return authData.getIdx();
    }

    // 랜덤한 8자리 인증번호 생성
    private String generateAuthNo() {
        return String.valueOf((int) (Math.random() * 90000000) + 10000000);
    }

    @Override
    public boolean checkAuthMail(Long authIdx, String authNo) {
        // 인덱스로 인증번호 조회
        MailAuthEntity authData = mailAuthRepository.findById(authIdx).orElse(null);
        if (authData == null) {
            return false;
        }

        // 인증번호가 일치하고, 만료되지 않았다면 인증 성공
        if (authData.getAuthNo().equals(authNo) && authData.getExpiredDt().isAfter(LocalDateTime.now())) {
            mailAuthRepository.updateStatusByEmail(authData.getEmail());
            return true;
        }

        return false;
    }

    @Override
    public boolean dupNickname(String nickname) {
        // 닉네임 중복 체크
        MemberEntity memberEntity = memberRepository.findByNickname(nickname);
        return memberEntity != null;
    }

    @Override
    public boolean saveMember(String email, String password, String nickname) {
        // 회원가입
        memberRepository.save(MemberEntity.builder()
                .email(email)
                .password(SHA256Utils.getEncrypt(password, "rian123!@#"))
                .nickname(nickname)
                .build());

        return true;
    }
}
