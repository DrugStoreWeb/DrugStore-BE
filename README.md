
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

#### Login
회원가입한 유저의 mail과 password 정보로 로그인 할 수 있 API입니다.

```
  POST / auth / login
```

✔️ **Request**

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `email` | `String` | **Required**. 이메일 |
| `password` | `String` | **Required**. 비밀번호 |

```json
{
    "email": "lee1234@gmail.com",
    "password": "1234"
}
```

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

### Likes API
이 API는 사용자가 로그인 후 상품 목록에서 상품번호로 관심 있는 상품을 좋아요 리스트에 추가 및 삭제할 수 있습니다.
또한 로그인한 사용자가 추가한 좋아요 리스트를 조회할 수 있습니다.

#### Get My Likes

```
  GET / likes
```

✔️ **Request**

Token in the Header

✔️ **Response**

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `productId` | `Integer` | **Required**. 상품 아이디 |
| `productName` | `String` | **Required**. 상품 이름 |
| `product_img` | `String` | **Required**. 상품 이미지 |
| `price` | `Integer` | **Required**. 가격 |
| `brandName` | `String` | **Required**. 브랜드 이름 |
| `likes` | `boolean` | **Required**. 좋아요 여부 |
| `finalPrice` | `Integer` | **Required**. 최종 가격 |

```json
[
	{
    "product_id": 1,
    "product_name": "히알루로산 세럼",
    "final_price": 19000,
    "likes" : true,
    "product_img": "이미지",
    "price": 29000,
    "brand_name": "라운드어라운드"
	},
		{
    "product_id": 2,
    "product_name": "수분크림",
    "final_price": 19000,
    "likes" : false,
    "product_image": "이미지",
    "price": 29000,
    "brand_name": "피지오"
	}
]
```

#### Post likes

```
  POST / likes
```
✔️ **Request**
Token in the Header

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `product_id`| `Integer` | **Required**. 상품 아이디 |

```json
{
    "product_id": 1
}
```

#### Delete likes

```
  DELETE / likes
```
✔️ **Request**

Token in the Header

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `product_id`| `Integer` | **Required**. 상품 아이디 |

```json
{
    "product_id": 1
}
```
### Q&A API
이 API는 상품 관련 Q&A 조회, 등록, 수정, 삭제하는 시스템입니다. 
사용자는 특정 상품의 질문을 조회하고, 새로운 질문을 등록하며, 자신의 질문을 관리할 수 있습니다.
또한 questionStatus 값을 통해 관리자가 질문에 답변했는지 여부를 확인할 수 있습니다.

#### Get Product Question

```
  GET / product / question?product-id=1
```
✔️ **Request**

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `product-id`| `@RequestParam Integer` | **Required**. 상품 아이디 |

✔️ **Response**

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `question`| `String` | **Required**. 질문 |
| `answer`| `String` | **Required**. 답변 |
| `user_name`| `String` | **Required**. 유저 이름 |
| `created_at`| `localdate` | **Required**. 질문 생성일자 |
| `product_name`| `String` | **Required**. 상품 이름 |
| `brand_name`| `String` | **Required**. 브랜드 네임 |
| `question_id`| `Integer` | **Required**. 질문 번호 |
| `question_status`| `String` | **Required**. 답변대기/완료 |

```json
[
	{
		"question" : "언제 입고되나요",
		"answer" : null,
		"user_name" : "jieun",
		"created_at" : "2024-05-27",
		"product_name" : "어노브 대용량 딥 데미지 트리트먼트",
		"brand": "어노브",
		"question_id" : 1,
		"question_status" : "답변대기"
	},
	{
		"question" : "언제 입고되나요",
		"answer" : null,
		"user_name" : "jieun",
		"created_at" : "2024-05-27",
		"product_name" : "어노브 대용량 딥 데미지 트리트먼트",
		"brand": "어노브",
		"question_id" : 1,
		"question_status" : "답변대기"
	}
]
```
#### Post Product Question

```
  POST / product / question?product-id
```
✔️ **Request**

Token in the Header

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `product-id`| `@RequestParam Integer` | **Required**. 상품 아이디 |
| `question`| `Stringr` | **Required**. 질문 |

```json
{
	"question" : "언제 입고되나요?"
}
```

#### Put Product Question

```
  PUT / product / question?question-id=1
```
✔️ **Request**

Token in the Header

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `question-id`| `@RequestParam Integer` | **Required**. 상품 아이디 |
| `question`| `Stringr` | **Required**. 질문 |

```json
{
    "question" : "배송은 언제 되나요?."
}
```

#### Delete Product Question

```
  DELETE / product / question?question-id=1
```
✔️ **Request**

Token in the Header

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `question-id`| `@RequestParam Integer` | **Required**. 질문 아이디 |


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

## main page API

### main page

◼️ sorting keyword

| sortby | description |
| --- | --- |
| likes | 좋아요 많은 순 |
| reviews | 후기 높은 순 |
| price | 낮은 가격 순 (할인된 가격 기준) |
| new | 신상품 순 |
| sales | 판매율 순 |

```
GET /main
```

✅ Request

| Name | Type | Requried | description          |
| --- | --- | --- |----------------------|
| sortby | String | false | 정렬 기준 (default=sales) |
| page | Integer | false | 첫 페이지 번호 (default=0) |
| size | Integer | false | 페이지 당 데이(default=24) |
| token |  | false | user의 likes가 true로 표시됨 |

✅ Response

| Name | Type | Required | Description |  |
| --- | --- | --- | --- | --- |
| product_id | Integer | * |  |  |
| brand_name | String | * |  |  |
| product_name | String | * |  |  |
| price | Integer | * |  |  |
| final_price | Integer | * |  |  |
| product_img | String | * |  |  |
| likes | boolean | * | 현재 로그인한 유저가 좋아요를 눌렀는가 여부 |  |
| sales | boolean | * | 세일중인가 여부 |  |
| best | boolean | * | 베스트 상품인가 여부 |  |

요청 예시

```
https://drugstoreproject.shop/main?sortby=likes&page=0&size=3
```

응답 예시

```json
{
  "code": 200,
  "message": "메인 페이지 조회에 성공했습니다.",
  "data": {
    "total_pages": 47,
    "total_elements": 139,
    "current_page": 0,
    "page_size": 3,
    "product_list": [
      {
        "product_id": 4,
        "product_name": "에스네이처 아쿠아 스쿠알란 수분크림",
        "brand_name": "에스네이처",
        "price": 43000,
        "final_price": 21500,
        "product_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/141.png",
        "likes": false,
        "best": false,
        "sales": true
      },
      {
        "product_id": 5,
        "product_name": "아쿠아 오아시스 수분 젤크림",
        "brand_name": "에스네이처",
        "price": 34000,
        "final_price": 23800,
        "product_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/151.png",
        "likes": false,
        "best": false,
        "sales": true
      },
      {
        "product_id": 21,
        "product_name": "메디힐 패드",
        "brand_name": "메디힐",
        "price": 39000,
        "final_price": 33150,
        "product_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/211.png",
        "likes": false,
        "best": true,
        "sales": true
      }
    ],
    "main_page_ad_img": {
      "review_top_image_url": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/161.png",
      "sales_top_image_url": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/211.png",
      "likes_top_image_url": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/321%E1%84%86%E1%85%A1%E1%84%82%E1%85%A7%E1%84%80%E1%85%A9%E1%86%BC%E1%84%8C%E1%85%A1%E1%86%BC.png"
    }
  }
}
```

### category page

◼️ category number

```
GET /main/category/{category_number}

```

| category | Name |
| --- | --- |
| 1 | 스킨케어 |
| 2 | 마스크팩 |
| 3 | 클렌징 |
| 4 | 선케어 |
| 5 | 메이크업 |
| 6 | 미용소품 |
| 7 | 더모코스메틱 |
| 8 | 멘즈케어 |
| 9 | 헤어케어 |
| 10 | 바디케어 |
| 11 | 향수 |
| 12 | 네일 |

✅ Request

| Name | Type | Requried | description          |
| --- | --- | --- |----------------------|
| category | Integer | true | 카테고리 번호              |
| sortby | String | false | 정렬 기준 (default=sales) |
| page | Integer | false | 첫 페이지 번호 (default=0) |
| size | Integer | false | 페이지 당 데이터 개수 (default=24) |
| token |  | false | user의 likes가 true로 표시됨 |

✅ Response

| Name | Backend Type | Required | Description |
| --- | --- | --- | --- |
| product_id | Integer | * |  |
| brand_name | String | * |  |
| product_name | String | * |  |
| price | Integer | * |  |
| final_price | Integer | * |  |
| product_img | String | * |  |
| likes | boolean | * | 현재 로그인한 유저가 좋아요를 눌렀는가 여부 |
| sales | boolean | * | 세일중인가 여부 |
| best | boolean | * | 베스트 상품인가 여부 |

요청 예시

```
https://drugstoreproject.shop/main/category/8?sortby=likes&page=0&size=3
```

응답 예시

```json
{
  "code": 200,
  "message": "카테고리 페이지 조회에 성공했습니다.",
  "data": {
    "content": [
      {
        "product_id": 83,
        "product_name": "아이디얼 포 맨 프레시 올인원 젤 로션 1+1 한정기획",
        "brand_name": "아이디얼포맨",
        "price": 28000,
        "final_price": 21000,
        "product_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/83-1.jpg",
        "likes": false,
        "best": false,
        "sales": true
      },
      {
        "product_id": 84,
        "product_name": "[1위향수/한정기획] 포맨트 시그니처 퍼퓸 코튼허그 50ml 한정기획(미니어처 5ml 증정)",
        "brand_name": "포맨트",
        "price": 39000,
        "final_price": 35100,
        "product_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/84-1.jpg",
        "likes": false,
        "best": false,
        "sales": true
      },
      {
        "product_id": 82,
        "product_name": "[덱스 PICK] 오브제 내추럴 커버 로션 50g 단품/기획(+미니어처 10ml)",
        "brand_name": "오브제",
        "price": 27900,
        "final_price": 23715,
        "product_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/82-1.jpg",
        "likes": false,
        "best": true,
        "sales": true
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 3,
      "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 4,
    "totalElements": 10,
    "last": false,
    "size": 3,
    "number": 0,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "numberOfElements": 3,
    "first": true,
    "empty": false
  }
}
```

### find page

```
GET /main/find/keyword={keyword}

```

✅ Request

| Name | Type | Requried | description          |
| --- | --- | --- |----------------------|
| keyword | Integer | true | 검색어 (브랜드명 또는 상품명) 두 글자 일치 검색 |
| sortby | String | false | 정렬 기준 (default=sales) |
| page | Integer | false | 첫 페이지 번호 (default=0) |
| size | Integer | false | 페이지 당 데이터 개수 (default=24) |
| token |  | false | user의 likes가 true로 표시됨 |

✅ Response

| Name | Backend Type | Required | Description |
| --- | --- | --- | --- |
| product_id | Integer | * |  |
| brand_name | String | * |  |
| product_name | String | * |  |
| price | Integer | * |  |
| final_price | Integer | * |  |
| product_img | String | * |  |
| likes | boolean | * | 현재 로그인한 유저가 좋아요를 눌렀는가 여부 |
| sales | boolean | * | 세일중인가 여부 |
| best | boolean | * | 베스트 상품인가 여부 |

요청 예시

```
https://drugstoreproject.shop/main/find?keyword=ml&sortby=sales&page=0&size=3
```

응답 예시

```json
{
  "code": 200,
  "message": "검색에 성공했습니다.",
  "data": {
    "content": [
      {
        "product_id": 34,
        "product_name": "메디필 랩핑 마스크팩 70mL",
        "brand_name": "메디필",
        "price": 25000,
        "final_price": 20000,
        "product_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/2141.png",
        "likes": false,
        "best": false,
        "sales": true
      },
      {
        "product_id": 41,
        "product_name": " [덱스 PICK] 오브제 내추럴 커버 로션 50g 단품/기획(+미니어처 10ml)",
        "brand_name": "오브제",
        "price": 25400,
        "final_price": 22860,
        "product_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/121-true.jpg",
        "likes": false,
        "best": true,
        "sales": true
      },
      {
        "product_id": 63,
        "product_name": "라로슈포제 시카플라스트 밤B5+ 100ml 기획 (+시카크림 15ml 증정)",
        "brand_name": "라로슈포제",
        "price": 39000,
        "final_price": 27300,
        "product_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/63-1.jpg",
        "likes": false,
        "best": true,
        "sales": true
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 3,
      "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 9,
    "totalElements": 27,
    "last": false,
    "size": 3,
    "number": 0,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "numberOfElements": 3,
    "first": true,
    "empty": false
  }
}
```



## Authors

- [@honghyeon](https://github.com/limhhyeon)



## Skillset


<p align="center">
  <a href="https://skillicons.dev">
    <img src="https://skillicons.dev/icons?i=java,spring,aws,gradle,mysql,nginx,redis,postman,github,discord,notion&theme=light" />
  </a>
</p>


