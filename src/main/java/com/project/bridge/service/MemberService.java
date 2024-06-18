package com.project.bridge.service;

public interface MemberService {
    public Long sendAuthMail(String email);

    public boolean checkAuthMail(Long authIdx, String authNo);

    public boolean dupNickname(String nickname);

    public boolean saveMember(String email, String password, String nickname);
}
