//package com.github.drug_store_be.web.controller;
//
//import com.github.drug_store_be.service.service.KakaoPayService;
//import com.github.drug_store_be.web.DTO.ResponseDto;
//import com.github.drug_store_be.web.DTO.kakaopay.PayInfoDto;
//import com.github.drug_store_be.web.DTO.kakaopay.resposne.PayApproveResDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/payment")
//public class KakaoPayController {
//
//    private final KakaoPayService kakaoPayService;
//
//    @GetMapping("/ready")
//    public ResponseDto getRedirectUrl(@RequestBody PayInfoDto payInfoDto) {
//        try {
//            return new ResponseDto(HttpStatus.OK.value(), "get Redirect URL Success", kakaoPayService.getRedirectUrl(payInfoDto));
//        }
//        catch(Exception e){
//            return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
//
//        }
//    }
//
//    @GetMapping("/success/{id}")
//    public ResponseDto afterGetRedirectUrl(@PathVariable("id")Integer id,
//                                                 @RequestParam("pg_token") String pgToken) {
//        try {
//            PayApproveResDto kakaoApprove = kakaoPayService.getApprove(pgToken,id);
//
//            return new ResponseDto(HttpStatus.OK.value(),"after get redirect URL Success" , kakaoApprove);
//        }
//        catch(Exception e){
//            return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
//        }
//    }
//
//    @GetMapping("/cancel")
//    public ResponseDto cancel() {
//        return new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), "사용자가 결제를 취소하였습니다.");
//    }
//
//    @GetMapping("/fail")
//    public ResponseDto fail() {
//
//        return new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), "결제가 실패하였습니다.");
//
//    }
//
//}
