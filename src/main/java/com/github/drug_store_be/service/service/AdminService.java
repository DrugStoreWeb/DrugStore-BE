package com.github.drug_store_be.service.service;

import com.github.drug_store_be.repository.category.Category;
import com.github.drug_store_be.repository.category.CategoryJpa;
import com.github.drug_store_be.repository.option.Options;
import com.github.drug_store_be.repository.option.OptionsJpa;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.product.ProductJpa;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import com.github.drug_store_be.repository.productPhoto.ProductPhotoJpa;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserJpa;
import com.github.drug_store_be.repository.userDetails.PrincipalDetails;
import com.github.drug_store_be.service.exceptions.NotAuthorizedException;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.ResponseDto;
import com.github.drug_store_be.web.DTO.order.ProductRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class AdminService {
    private final UserJpa userJpa;
    private final CategoryJpa categoryJpa;
    private final ProductJpa productJpa;
    private final ProductPhotoJpa productPhotoJpa;
    private final OptionsJpa optionsJpa;

    public ResponseDto registerProduct(PrincipalDetails UserDetails, ProductRegisterDto productRegisterDto) {
        User user= userJpa.findById(UserDetails.getUserId())
                .orElseThrow(()-> new NotFoundException("아이디가  "+ UserDetails.getUserId() +"인 유저를 찾을 수 없습니다."));
        List<String> role= user.getUserRole().stream().map(ur-> ur.getRole().getRoleName()).collect(Collectors.toList());
        if(role.stream().findFirst().get().equals("ROLE_ADMIN")){
            Category category= categoryJpa.findById(productRegisterDto.getCategoryId())
                    .orElseThrow(()-> new NotFoundException("아이디가  "+ productRegisterDto.getCategoryId() +"인 카테고리를 찾을 수 없습니다."));

            Integer caculatedFinalPrice= productRegisterDto.getPrice() * (100-productRegisterDto.getProductDiscount()) / 100;

            Integer totalStock= productRegisterDto.getOptionsList().stream().map(options -> options.getStock()).mapToInt(Integer::intValue).sum();

            Product product= Product.builder()
                    .category(category)
                    .productName(productRegisterDto.getProductName())
                    .brand(productRegisterDto.getBrand())
                    .price(productRegisterDto.getPrice())
                    .productDiscount(productRegisterDto.getProductDiscount())
                    .finalPrice(caculatedFinalPrice)
                    .best(productRegisterDto.getBest())
                    .productStatus(productRegisterDto.getProductStatus())
                    .createAt(LocalDate.now())
                    .originalStock(totalStock)
                    .productSales(0.0)
                    .reviewAvg(0.0)
                    .build();
            productJpa.save(product);

            List<ProductPhoto> productPhotoList= productRegisterDto.getProductPhotoList()
                    .stream()
                    .map((pp) -> ProductPhoto.builder()
                            .product(product)
                            .photoUrl(pp.getPhotoUrl())
                            .photoType(pp.getPhotoType())
                            .build())
                    .collect(Collectors.toList());
            productPhotoJpa.saveAll(productPhotoList);

            List<Options> optionsList= productRegisterDto.getOptionsList()
                    .stream()
                    .map((o) -> Options.builder()
                            .product(product)
                            .optionsName(o.getOptionsName())
                            .optionsPrice(o.getOptionsPrice())
                            .stock(o.getStock())
                            .build())
                    .toList();
            optionsJpa.saveAll(optionsList);

            //option추가할 때 상품 originalStock(0)바꾸기 기억하기!


            return new ResponseDto(HttpStatus.OK.value(), "상품 등록 성공");

        }else{
            throw new NotAuthorizedException("상품 등록 권한이 없습니다.");
        }
    }




}
