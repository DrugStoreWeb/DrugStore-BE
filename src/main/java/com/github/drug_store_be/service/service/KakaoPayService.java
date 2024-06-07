//package com.github.drug_store_be.service.service;
//
//import com.github.drug_store_be.repository.user.User;
//import com.github.drug_store_be.repository.user.UserJpa;
//import com.github.drug_store_be.service.exceptions.NotFoundException;
//import com.github.drug_store_be.web.DTO.kakaopay.PayInfoDto;
//import com.github.drug_store_be.web.DTO.kakaopay.request.MakePayRequest;
//import com.github.drug_store_be.web.DTO.kakaopay.request.PayRequest;
//import com.github.drug_store_be.web.DTO.kakaopay.resposne.PayApproveResDto;
//import com.github.drug_store_be.web.DTO.kakaopay.resposne.PayReadyResDto;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//@RequiredArgsConstructor
//@Service
//@Slf4j
//public class KakaoPayService {
//    private final MakePayRequest makePayRequest;
//    private final UserJpa userJpa;
//
//    @Value("${pay.admin-key}")
//    private String adminKey;
//
//
//    @Transactional
//    public PayReadyResDto getRedirectUrl(PayInfoDto payInfoDto) throws Exception{
//        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
//        String name= authentication.getName();
//
//        User user= userJpa.findByEmail(name)
//                .orElseThrow(()-> new NotFoundException("Cannot find user with email:  "+ name));
//
//        Integer id= user.getUserId();
//        HttpHeaders headers= new HttpHeaders();
//
//        String auth= "kakaoAK" + adminKey;
//        headers.set("Content-type","application/x-www-form-urlencoded;charset=utf-8");
//        headers.set("Authorization",auth);
//
//        PayRequest payRequest= makePayRequest.getReadyRequest(id, payInfoDto);
//        HttpEntity<MultiValueMap<String, String>> urlRequest= new HttpEntity<>(payRequest.getMap(),headers );
//
//        RestTemplate rt= new RestTemplate();
//        PayReadyResDto payReadyResDto= rt.postForObject(payRequest.getUrl(), urlRequest, PayReadyResDto.class);
//
//        user.updateTid(payReadyResDto.getTid());
//
//        return payReadyResDto;
//    }
//
//    @Transactional
//    public PayApproveResDto getApprove(String pgToken, Integer id) throws Exception{
//        User user= userJpa.findById(id).orElseThrow(()-> new NotFoundException("Cannot find user with ID:  "+ id));
//
//        String tid= user.getTid();
//
//        HttpHeaders headers=new HttpHeaders();
//        String auth = "KakaoAK " + adminKey;
//
//        headers.set("Content-type","application/x-www-form-urlencoded;charset=utf-8");
//        headers.set("Authorization",auth);
//
//
//        PayRequest payRequest=makePayRequest.getApproveRequest(tid, id, pgToken);
//
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(payRequest.getMap(), headers);
//
//        RestTemplate rt = new RestTemplate();
//        PayApproveResDto payApproveResDto = rt.postForObject(payRequest.getUrl(), requestEntity, PayApproveResDto.class);
//
//
//
//        return payApproveResDto;
//
//
//    }
//
//
//}
//
