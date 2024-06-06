package com.github.drug_store_be.web.DTO.kakaopay.request;

import com.github.drug_store_be.web.DTO.kakaopay.PayInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

@Component
@RequiredArgsConstructor
public class MakePayRequest {

    public PayRequest getReadyRequest(Long id, PayInfoDto payInfoDto){
        LinkedMultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        String memberId=id+"";
        String orderId="point"+id;
        map.add("cid","TC0ONETIME");
        map.add("partner_order_id",orderId); //user_id, order_id는 동일해야 한다.
        map.add("partner_user_id","본인의 서비스명");

        map.add("item_name",payInfoDto.getItemName());
        map.add("quantity","1");
        map.add("total_amount",payInfoDto.getPrice()+"");
        map.add("tax_free_amount", "0");

        map.add("approval_url", "http://localhost:8080/payment/success"+"/"+id); // 성공 시 redirect url
        map.add("cancel_url", "http://localhost:8080/payment/cancel"); // 취소 시 redirect url
        map.add("fail_url", "http://localhost:8080/payment/fail"); // 실패 시 redirect url

        return new PayRequest("https://kapi.kakao.com/v1/payment/ready",map);
    }

    public PayRequest getApproveRequest(String tid, Long id,String pgToken){
        LinkedMultiValueMap<String,String> map=new LinkedMultiValueMap<>();

        String orderId="point"+id;
        map.add("cid", "TC0ONETIME");
        map.add("tid", tid);
        map.add("partner_order_id", orderId); // 주문명
        map.add("partner_user_id", "본인의 서비스명");
        map.add("pg_token", pgToken);

        return new PayRequest("https://kapi.kakao.com/v1/payment/approve",map);
    }
}

