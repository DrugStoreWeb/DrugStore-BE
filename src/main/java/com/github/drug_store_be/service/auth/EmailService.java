package com.github.drug_store_be.service.auth;

import com.github.drug_store_be.service.auth.redis.RedisUtil;
import com.github.drug_store_be.web.DTO.Auth.EmailAuthNumCheck;
import com.github.drug_store_be.web.DTO.ResponseDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;
    private static int number;
    private final RedisUtil redisUtil;
    @Transactional
    public ResponseDto emailSendResult(String email) {
        MimeMessage mimeMessage=createMessage(email);
        javaMailSender.send(mimeMessage);

        redisUtil.setDataExpire(email,Integer.toString(number),60*5L);

        return new ResponseDto(HttpStatus.OK.value(),"code : " +number);
    }
    //대체로 Map으로 sotrage만들어서 진행 주말에 redis로 수정 예정
    public ResponseDto authNumCheckResult(EmailAuthNumCheck emailAuthNumCheck) {
        String authNum = Integer.toString(emailAuthNumCheck.getAuthNum());
        if (redisUtil.getData(emailAuthNumCheck.getEmail())==null){
            return new ResponseDto(HttpStatus.NOT_FOUND.value(),"이메일에 대한 인증 번호를 발급 받은 적이 없습니다.");
        }else if (redisUtil.getData(emailAuthNumCheck.getEmail()).equals(authNum)) {
            return new ResponseDto(HttpStatus.OK.value(),"인증에 성공하셨습니다.");
        }else return new ResponseDto(HttpStatus.UNAUTHORIZED.value(),"인증 번호가 동일하지 않습니다.");
    }
//랜덤 숫자 생성 메소드
    public  static void getNumber(){
        number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }
    //메시지 생성 메소드
    public MimeMessage createMessage(String email)  {
        getNumber();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            mimeMessage.setFrom(senderEmail);
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO,email);
            mimeMessage.setSubject("[drug-Store] 회원가입을 위한 이메일 인증");  // 제목 설정
            String body = "";
            body += "<h1>" + "안녕하세요." + "</h1>";
            body += "<h1>" + "drug-Store 입니다." + "</h1>";
            body += "<h3>" + "이메일 인증을 위한 인증 번호입니다." + "</h3><br>";
            body += "<h2>" + "아래 코드를 이메일 인증 칸으로 돌아가 입력해주세요." + "</h2>";

            body += "<div align='center' style='border:1px solid black; font-family:verdana;'>";
            body += "<h2>" + "이메일 인증 코드입니다." + "</h2>";
            body += "<h1 style='color:blue'>" + number + "</h1>";
            body += "</div><br>";
            body += "<h3>" + "감사합니다." + "</h3>";
            mimeMessage.setText(body,"UTF-8", "html");
        }catch (MessagingException messageE){
            messageE.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return mimeMessage;
    }

}
