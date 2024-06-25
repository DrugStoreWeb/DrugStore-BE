
# DrugStore Project

This project clones a drugstore shop webpage using JAVA 17 and springboot. Runs with ubuntu on AWS EC2 and uses mariaDB on RDS. Uploads images on S3 server in AWS. 

The redis server and gmail SMTP is implemented for email verification, and nginx for HTTPS. For security, Jasypt is implemented to encrypt all the environment variables. 


## Environment Variables

To run this project, you will need to add the following environment variables to your .yaml file
The implementation of JASYPT safely encrypts the variables

`SSL` : HTTPS

`redis`: REDIS server for email verification

`oauth2` : Kakao Social login

`mail`: Google smtp server

`datasource`: RDS database

`cloud s3`: AWS S3 cloud

`JASYPT`: encrypt environment variables

`pay`: Kakao pay

## API Reference

### Auth API


#### SignUp

```
  POST /auth/sign-up
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `name` | `String` | **Required**. 이름 |
| `nickname` | `String` | **Required**. 닉네임 |
| `email` | `String` | **Required**. 이메일 |
| `password` | `String` | **Required**. 비밀번호 |
| `password_check` | `String` | **Required**. 비밀번호 체크 |
| `birthday` | `String` | **Required**. 생일 |
| `phone_number` | `String` | **Required**. 전화번호 |
| `address` | `String` | **Required**. 주소 |
| `profile_pic` | `String` | **Required**. 프로필 사진 |

#### Nickname Check

```
  GET /auth/nickname
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `nickname` | `String` | **Required**. 닉네임 중복 확인을 위한 파라미터|

#### Email Check

```
  GET /auth/email
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `email` | `String` | **Required**. 이메일 중복 확인을 위한 파라미터|

#### Find Email

```
  POST /auth/find-email
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `nickname` | `String` | **Required**. 이메일 찾기에 필요한 정보(닉네임)|
| `phone_num` | `String` | **Required**. 이메일 찾기 필요한 정보(전화번호)|

#### Reset Password

```
  PUT /auth/password
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `email` | `String` | **Required**. 이메일 인증할 때 사용되는 이메일|
| `new_password` | `String` | **Required**. 새로 설정할 비밀번호|
| `new_password_check` | `String` | **Required**. 새로 설정할 비밀번호 체크|

### Email API

#### Send email verification code

```
  POST /email/send
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `email` | `String` | **Required**. 인증 번호 받을 이메일|

#### Check verification code

```
  POST /email/auth-num
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `email` | `String` | **Required**. 인증 번호 인증할 이메일|
| `auth` | `Integer` | **Required**. 인증 번호|



### Order

#### 1. 장바구니에서 주문할 상품들 가져오기

```
  POST /order/cart-to-order
```

 ✔️ **Request**

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `cartId` | `Integer` | **Required**. CartId to order |

✔️ **Response**

```json
{
    "code": 200,
    "message": "order page show success",
    "data": {
        "user_name": "park",
        "phone_number": "01011112222",
        "address": "서울시 강남구",
        "orders_number": "3dd260a6c52b4f2a",
        "orders_at": "2024-06-23",
        "order_coupon_list": [
            {
                "coupon_name": "10% 할인 쿠폰",
                "coupon_discount": 10
            },
            {
                "coupon_name": "반가워요 쿠폰",
                "coupon_discount": 20
            }
        ],
        "order_product_list": [
            {
                "product_photo": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/Screenshot.png",
                "product_name": "레드 블레미쉬 클리어 수딩 크림",
                "brand": "닥터지",
                "option_name": "단품 70ml",
                "price": 18000,
                "final_price": 15300,
                "quantity": 1
            }
        ]
    }
}
```

#### 2. 주문, 즉 결제

```
  PUT /order/order-to-pay
```
 ✔️ **Request**
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `option_id`      | `Integer` | **Required**. Id of option to fetch |
| `quantity`      | `Integer` | **Required**. how many products to order |
| `total_price`      | `Integer` | **Required**. how much in total |

```json
{
    "option_quantity_dto": [
        {
            "option_id": 1, 
            "quantity": 1
        }
    ],
    "total_price": 10000
}
```

✔️ **Response**
- PUT : quantity of options
- PUT : user money
- DELETE : delete from cart


### Add Product

#### ADMIN 계정에서 상품 등록하기

```
  POST /admin/product
```

 ✔️ **Request**

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `category_id` | `Integer` | **Required**. Product Category |
| `product_name` | `String` | **Required**. Name of product |
| `brand` | `String` | **Required**. Brand of product |
| `price` | `Integer` | **Required**. Price of product |
| `product_discount` | `Integer` | **Required**. Percentage of discount |
| `best` | `Boolean` | **Required**. Best true, false|
| `product_status` | `Boolean` | **Required**. Sold true, false |
| `photo_url` | `String` | **Required**. S3 Url |
| `photo_type` | `Boolean` | **Required**. Main photo true, false |
| `options_name` | `String` | **Required**. Option name |
| `options_price` | `Integer` | **Required**. How much to add to the option |
| `stock` | `Integer` | **Required**. How many in stock |


```json
{
    "category_id": 12 ,
    "product_name": "상품 이름",
    "brand": "반디",
    "price": 14000,
    "product_discount": 20,
    "best": "false",
    "product_status": "true",
    "product_photo_list": [
        {
            "photo_url": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/Screenshot.png",
            "photo_type": "true"
        },  {
            "photo_url": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/Screenshot.png",
            "photo_type": "false"
        },  {
            "photo_url": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/Screenshot.png",
            "photo_type": "false"
        },  {
            "photo_url": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/Screenshot.png",
            "photo_type": "false"
        },  {
            "photo_url": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/Screenshot.png",
            "photo_type": "false"
        }
    ],
    "options_list": [
        {
            "options_name": "실버 마그넷",
            "options_price": 1000,
            "stock": 5
        },   {
            "options_name": "스카이 마그넷",
            "options_price": 1000,
            "stock": 9
        },   {
            "options_name": "베리크림 시럽",
            "options_price": 0,
            "stock": 9
        },   {
            "options_name": "아트 마그넷 플래쉬",
            "options_price": 20000,
            "stock": 14
        }
    ]
}
```

### Upload images on S3

#### 1. 사진 여러개 업로드
```
  POST /storage/multipart-files
```

 ✔️ **Request**

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `uploadFiles` | `MultipartFile` | **Required**. MultipartFile |
| `type` | `SaveFileType` | **Required**. small, large |


#### 2. 사진 업로드 취소(삭제)
```
  DELETE /storage/multipart-files
```

 ✔️ **Request**

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `file-url` | `String` | **Required**. file URL to delete|



#### 3. 업로드한 사진 수정
```
  PUT /storage/multipart-files
```

 ✔️ **Request**

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `file-url` | `String` | **Required**. file URL to update |
| `uploadFiles` | `MultipartFile` | **Required**. MultipartFile |
| `type` | `SaveFileType` | **Required**. small, large |


### Product Detail API

#### Get product detail

```
  GET /product?product-id=1
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `product-id`      | `@RequestParam Integer` | **Required**. 상품에 대한 id |


#### Get product review

```
  GET /product/review/{productId}?sort=reviewScoreAsc&page=0

```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `productId` | `@PathVariable Integer` | **Required**. 리뷰 가져올 상품 id |
| `criteria` | `@RequestParam String ` | **Required**. 정렬 조건(sort : 최신순, 평점 높은 순, 평점 낮은 순) |
| `pageNum` | `@RequestParam Integer ` | **Required**. 페이지(page) |

#### Response to inquiry

```
  POST /product/answer?question-id=3

```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `question-id` | `@RequestParam Integer` | **Required**. 답변을 남길 질문 id |
| `answer` | `String ` | **Required**. 질문에 대한 답변 |
| `customUserDetails` | `@AuthenticationPrincipal CustomUserDetails ` | **Required**. 해당 토큰으로 권한이 관리자인지 확인 |

## 실행결과 스크린샷

### Auth API

#### Signup
![회원가입](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/8ad9d2b1-9d8f-43cf-85f5-b8a32c0ff53a)

#### Nickname Check
-성공
![닉네임 중복 체크(사용 가능한 닉네임)](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/f3e3a273-d933-4f4e-9aa5-80dcfab7b3c1)

-실패
![닉네임 중복 체크(이미 있는 닉네임)](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/75682b83-c784-4336-ae07-2b58bbf7f672)


#### Email Check
-성공
![이메일 중복 확인(사용 가능한 이메일)](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/9b6bffdb-20d2-43c6-92c1-c7072a2ecd66)

-실패
![이메일 중복 확인(이미 있는 이메일)](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/aac0b4e7-8d3f-47d9-8c3e-62396b514ac9)

#### Find Email
-성공
![이메일 찾기(성공)](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/be1a8718-17d8-4bc5-a217-01c4b46b28a5)

-실패
![이메일 찾기(실패)](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/300a2311-2295-42a7-aa49-c9a21afebcf1)

#### Reset password
-성공
![비밀번호 변경(성공)](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/990ee330-4ddd-4fca-9503-b0386bf4c6ce)

-실패
![비밀번호 변경(실패)](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/3a396581-cf50-40d1-b883-6c5610f68b2b)

### Email API

#### Send email verification code
![인증 번호 전송](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/05179984-13a0-46b4-9164-5844ff9a91db)


#### Check verification code
-성공
![인증 번호 확인(성공)](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/5065c484-445b-4ada-8d8e-73d4d1ab7439)

-실패
![인증 번호 확인(실패)](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/32d50477-8452-42c9-b522-dcf4d9bf8602)

### Product detail API

#### Get product detail
![제품 상세 조회(이미지)](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/6c2f108c-f73e-45b5-90fb-9cb546428a25)
![제품 상세 조회(아래 부분)](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/43f770cf-2a3a-4648-b33f-7af5e3c42481)


#### Get product review
-상품 상세페이지 리뷰 최신순
![상품 리뷰 조회( 최신 리뷰 순)](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/3ce30da4-0556-4e74-bdaa-5e5ec49d1b4a)

-상품 상세페이지 리뷰 평점 높은 순
![상품 리뷰 조회(리뷰 평점 높은 순)](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/d1ca9b6f-3a2d-4b3b-8c54-14673b06c2b3)

-상품 상세페이지 리뷰 평점 낮은 순
![상품 리뷰 조회(리뷰 평점 낮은 순)](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/9ed7712b-c286-438a-8fd4-3f2f61542117)

#### Response to inquiry
-성공
![토큰을 이용해 관리자 계정만 답변(성공)](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/9cb921da-2f9e-44fd-a74d-90f66bbd51a6)

-실패
![토큰을 이용해 관리자 계정만 답변(일반 유저는 실패)](https://github.com/DrugStoreWeb/DrugStore-BE/assets/156086602/c1266708-0b4e-4ac7-b19c-dfaf82133925)




## Authors

- [@honghyeon](https://github.com/limhhyeon)



## Skillset


<p align="center">
  <a href="https://skillicons.dev">
    <img src="https://skillicons.dev/icons?i=java,spring,aws,gradle,mysql,nginx,redis,postman,github,discord,notion&theme=light" />
  </a>
</p>


