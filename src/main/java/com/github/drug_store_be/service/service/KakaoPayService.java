package com.github.drug_store_be.service.service;

import com.github.drug_store_be.repository.user.UserJpa;
import com.github.drug_store_be.web.DTO.kakaopay.PayInfoDto;
import com.github.drug_store_be.web.DTO.kakaopay.request.MakePayRequest;
import com.github.drug_store_be.web.DTO.kakaopay.resposne.PayReadyResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class KakaoPayService {
    private final MakePayRequest makePayRequest;
    private final UserJpa userJpa;

    @Value("${pay.admin-key}")
    private String adminKey;


    @Transactional
    public PayReadyResDto getRedirectUrl(PayInfoDto payInfoDto) {
    }
}
