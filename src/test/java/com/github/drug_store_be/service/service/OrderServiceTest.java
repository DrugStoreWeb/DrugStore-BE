package com.github.drug_store_be.service.service;


import com.github.drug_store_be.repository.option.Options;
import com.github.drug_store_be.repository.option.OptionsRepository;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserRepository;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.ResponseDto;
import com.github.drug_store_be.web.DTO.pay.OptionQuantityDto;
import com.github.drug_store_be.web.DTO.pay.PayRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private OptionsRepository optionsRepository;

    private CustomUserDetails customUserDetails;
    private PayRequestDto payRequestDto;
    private User user;
    private List<OptionQuantityDto> optionQuantityList;
    private Options option1;
    private Product product1;

    @BeforeEach
    void setUp() {
        customUserDetails = new CustomUserDetails();
        customUserDetails.setUserId(1);

        user = new User();
        user.setUserId(1);
        user.setMoney(100000);

        product1= new Product();
        product1.setProductId(1);
        product1.setProductStatus(true);


        option1 = new Options();
        option1.setOptionsId(1);
        option1.setProduct(product1);
        option1.setStock(100);


        optionQuantityList = new ArrayList<>();
        optionQuantityList.add(new OptionQuantityDto(1, 1));



        payRequestDto = new PayRequestDto();
        payRequestDto.setOptionQuantityDto(optionQuantityList);
        payRequestDto.setTotalPrice(10);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(optionsRepository.findByIdWithLock(1)).thenReturn(Optional.of(option1));
    }

    @DisplayName("Pessimistic Locking Test")
    @Test
    void testPessimisticLocking() throws InterruptedException, ExecutionException {
        // Create a thread pool to simulate concurrent access
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<ResponseDto>> futures = new ArrayList<>();

        // Simulate 100 concurrent orders
//        for (int i = 0; i < 100; i++) {
//            futures.add(executorService.submit(() -> {
//                try {
//                    return orderService.orderToPay(customUserDetails, payRequestDto);
//                } catch (NotFoundException e) {
//                    System.out.println(e.getMessage());
//                    return null;
//                }
//            }));
//        }

        CountDownLatch latch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try {
                    return orderService.orderToPay(customUserDetails, payRequestDto);
                } finally{
                    latch.countDown();
                }
            });
        }

        latch.await();

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        // Collect results
        int successCount = 0;
        int failureCount = 0;
        for (Future<ResponseDto> future : futures) {
            ResponseDto response = future.get();
            if (response != null && response.getCode() == HttpStatus.OK.value()) {
                successCount++;
            } else {package com.github.drug_store_be.service.service;


import com.github.drug_store_be.repository.option.Options;
import com.github.drug_store_be.repository.option.OptionsRepository;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserRepository;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.ResponseDto;
import com.github.drug_store_be.web.DTO.pay.OptionQuantityDto;
import com.github.drug_store_be.web.DTO.pay.PayRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

                @RunWith(SpringRunner.class)
                @SpringBootTest
                class OrderServiceTest {
                    @Autowired
                    private OrderService orderService;

                    @MockBean
                    private UserRepository userRepository;

                    @MockBean
                    private OptionsRepository optionsRepository;

                    private CustomUserDetails customUserDetails;
                    private PayRequestDto payRequestDto;
                    private User user;
                    private List<OptionQuantityDto> optionQuantityList;
                    private Options option1;
                    private Product product1;

                    @BeforeEach
                    void setUp() {
                        customUserDetails = new CustomUserDetails();
                        customUserDetails.setUserId(1);

                        user = new User();
                        user.setUserId(1);
                        user.setMoney(100000);

                        product1= new Product();
                        product1.setProductId(1);
                        product1.setProductStatus(true);


                        option1 = new Options();
                        option1.setOptionsId(1);
                        option1.setProduct(product1);
                        option1.setStock(100);


                        optionQuantityList = new ArrayList<>();
                        optionQuantityList.add(new OptionQuantityDto(1, 1));



                        payRequestDto = new PayRequestDto();
                        payRequestDto.setOptionQuantityDto(optionQuantityList);
                        payRequestDto.setTotalPrice(10);

                        when(userRepository.findById(1)).thenReturn(Optional.of(user));
                        when(optionsRepository.findByIdWithLock(1)).thenReturn(Optional.of(option1));
                    }

                    @DisplayName("Pessimistic Locking Test")
                    @Test
                    void testPessimisticLocking() throws InterruptedException, ExecutionException {
                        // Create a thread pool to simulate concurrent access
                        ExecutorService executorService = Executors.newFixedThreadPool(10);
                        List<Future<ResponseDto>> futures = new ArrayList<>();

                        // Simulate 100 concurrent orders
//        for (int i = 0; i < 100; i++) {
//            futures.add(executorService.submit(() -> {
//                try {
//                    return orderService.orderToPay(customUserDetails, payRequestDto);
//                } catch (NotFoundException e) {
//                    System.out.println(e.getMessage());
//                    return null;
//                }
//            }));
//        }

                        CountDownLatch latch = new CountDownLatch(100);

                        for (int i = 0; i < 100; i++) {
                            executorService.submit(() -> {
                                try {
                                    return orderService.orderToPay(customUserDetails, payRequestDto);
                                } finally{
                                    latch.countDown();
                                }
                            });
                        }

                        latch.await();

                        executorService.shutdown();
                        executorService.awaitTermination(1, TimeUnit.MINUTES);

                        // Collect results
                        int successCount = 0;
                        int failureCount = 0;
                        for (Future<ResponseDto> future : futures) {
                            ResponseDto response = future.get();
                            if (response != null && response.getCode() == HttpStatus.OK.value()) {
                                successCount++;
                            } else {
                                failureCount++;
                            }
                        }

                        System.out.println("Success count: " + successCount);
                        System.out.println("Failure count: " + failureCount);

                        // Assert that the number of successful orders does not exceed the total stock
                        assertTrue(option1.getStock() == 0);
                    }

                    @Transactional
                    public void optionStockChange(List<OptionQuantityDto> optionQuantityDtoList) {
                        for (OptionQuantityDto o : optionQuantityDtoList) {
                            int optionId = o.getOptionId();
                            Options options = optionsRepository.findByIdWithLock(optionId)
                                    .orElseThrow(() -> new NotFoundException("Cannot find option with ID " + optionId));
                            int originalOptionStock = options.getStock();
                            int orderedStock = o.getQuantity();
                            options.setStock(originalOptionStock - orderedStock);
                            optionsRepository.save(options);
                        }
                    }
                }

                failureCount++;
            }
        }

        System.out.println("Success count: " + successCount);
        System.out.println("Failure count: " + failureCount);

        // Assert that the number of successful orders does not exceed the total stock
        assertTrue(option1.getStock() == 0);
    }

    @Transactional
    public void optionStockChange(List<OptionQuantityDto> optionQuantityDtoList) {
        for (OptionQuantityDto o : optionQuantityDtoList) {
            int optionId = o.getOptionId();
            Options options = optionsRepository.findByIdWithLock(optionId)
                    .orElseThrow(() -> new NotFoundException("Cannot find option with ID " + optionId));
            int originalOptionStock = options.getStock();
            int orderedStock = o.getQuantity();
            options.setStock(originalOptionStock - orderedStock);
            optionsRepository.save(options);
        }
    }
}
