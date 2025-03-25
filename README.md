
# 💊 DrugStore Project

This project clones a drugstore shop webpage using JAVA 17 and springboot. Runs with ubuntu on AWS EC2 and uses mariaDB on RDS. Uploads images on S3 server in AWS. 

The redis server and gmail SMTP is implemented for email verification, and nginx for HTTPS. For security, Jasypt is implemented to encrypt all the environment variables. 

저희 프로젝트는 JAVA 17과 Spring Boot을 사용하여 약국 웹페이지를 만들었습니다. 이 프로젝트는 AWS EC2에서 우분투 운영 체제로 실행되며, RDS에서 MariaDB를 사용합니다. 이미지는 AWS의 S3 서버를 통해서 업로드합니다.

이메일 인증을 위해 Redis 서버와 Gmail SMTP가 구현되어 있으며, HTTPS를 위해 nginx를 사용하였고, 보안을 위해 Jasypt를 사용하여 모든 환경 변수를 암호화하였습니다.


## 🛠️ Skillset


<p align="center">
  <a href="https://skillicons.dev">
    <img src="https://skillicons.dev/icons?i=java,spring,aws,gradle,mysql,nginx,redis,postman,github,discord,notion&theme=light" />
  </a>
</p>

## 🧑🏻‍💻 Authors

- [@soheeparklee](https://github.com/soheeparklee)
- [@honghyeon](https://github.com/limhhyeon)
- [@Hyunjun](https://www.github.com/awear321)

## 📹 Video
You can view a demonstation video of this application [here](https://youtu.be/reaXKuAoctQ)

## 🔷 Environment Variables

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

## 💡 API Reference

### Auth API


#### SignUp

```
  POST /auth/sign-up
```
✔️ **Request**

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

✔️ **Response**

```json
{
    "code": 200,
    "message": "user heyon님 회원 가입에 성공하셨습니다."
}
```

#### Nickname Check

```
  POST /auth/nickname
```
✔️ **Request**

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `nickname` | `String` | **Required**. 닉네임 중복 확인을 위한 파라미터|

✔️ **Response**

***성공***

```json
{
    "code": 200,
    "message": "중복 여부 확인",
    "data": {
        "message": "userhyeon(는)은 사용하실 수 있는 닉네임입니다.",
        "check": false
    }
}
```
***실패***

```json
{
    "code": 409,
    "message": "중복 여부 확인",
    "data": {
        "message": "user hyeon(는)은 이미 존재하는 닉네임입니다. 다른 닉네임을 이용해주세요.",
        "check": true
    }
}
```

#### Email Check

```
  POST /auth/email
```
✔️ **Request**

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `email` | `String` | **Required**. 이메일 중복 확인을 위한 파라미터|

✔️ **Response**

***성공***

```json
{
    "code": 200,
    "message": "중복 여부 확인",
    "data": {
        "message": "Lim10@naver.com(는)은 사용하실 수 있는 이메일입니다.",
        "check": false
    }
}
```
***실패***

```json
{
    "code": 409,
    "message": "중복 여부 확인",
    "data": {
        "message": "Lim100@naver.com(는)은 이미 존재하는 이메일입니다. 다른 이메일을 이용해주세요.",
        "check": true
    }
}
```

#### Find Email

```
  POST /auth/find-email
```
✔️ **Request**

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `nickname` | `String` | **Required**. 이메일 찾기에 필요한 정보(닉네임)|
| `phone_num` | `String` | **Required**. 이메일 찾기 필요한 정보(전화번호)|

✔️ **Response**

```json
{
    "code": 200,
    "message": "email : lim1007@naver.com"
}
```

#### Reset Password

```
  PUT /auth/password
```
✔️ **Request**

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `email` | `String` | **Required**. 이메일 인증할 때 사용되는 이메일|
| `new_password` | `String` | **Required**. 새로 설정할 비밀번호|
| `new_password_check` | `String` | **Required**. 새로 설정할 비밀번호 체크|

✔️ **Response**

```json
{
    "code": 200,
    "message": "변경된 비밀번호로 다시 로그인해주세요"
}
```

### Email API

#### Send email verification code

```
  POST /email/send
```
✔️ **Request**

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `email` | `String` | **Required**. 인증 번호 받을 이메일|

✔️ **Response**

```
{
    "code": 200,
    "message": "code : 150374" 이메일에도 따로 전송됨
}
```

#### Check verification code

```
  POST /email/auth-num
```
✔️ **Request**

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `email` | `String` | **Required**. 인증 번호 인증할 이메일|
| `auth` | `Integer` | **Required**. 인증 번호|

✔️ **Response**

```json
{
    "code": 200,
    "message": "인증에 성공하셨습니다."
}
```

#### Login
회원가입한 유저의 mail과 password 정보로 로그인 할 수 있는 API입니다.

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

✔️Request**

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
✔️ **Request**

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `product-id`      | `@RequestParam Integer` | **Required**. 상품에 대한 id |

✔️ **Response**

```json
{
    "code": 200,
    "message": "조회 성공",
    "data": {
        "product_id": 1,
        "product_name": "레드 블레미쉬 클리어 수딩 크림",
        "sales": 15,
        "price": 18000,
        "final_price": 15300,
        "product_img": [
            {
                "img_id": 1,
                "img_main": true,
                "img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/Screenshot+2024-06-16+at+19.52.00.png"
            },
            {
                "img_id": 2,
                "img_main": false,
                "img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/Screenshot+2024-06-16+at+19.51.51.png"
            },
            {
                "img_id": 3,
                "img_main": false,
                "img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/Screenshot+2024-06-16+at+19.51.41.png"
            },
            {
                "img_id": 4,
                "img_main": false,
                "img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/Screenshot+2024-06-16+at+19.51.29.png"
            },
            {
                "img_id": 5,
                "img_main": false,
                "img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/Screenshot+2024-06-16+at+19.51.17.png"
            }
        ],
        "review_count": 11,
        "review_avg": 3.9,
        "is_like": false,
        "best": true,
        "brand_name": "닥터지",
        "product_options": [
            {
                "option_id": 1,
                "option": "단품 70ml",
                "option_price": 0,
                "option_stock": 4
            },
            {
                "option_id": 2,
                "option": "세트 1+1 기획",
                "option_price": 10000,
                "option_stock": 7
            }
        ]
    }
}
```


#### Get product review

```
  GET /product/review/{productId}?sort=reviewScoreAsc&page=0

```
✔️ **Request**

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `productId` | `@PathVariable Integer` | **Required**. 리뷰 가져올 상품 id |
| `criteria` | `@RequestParam String ` | **Required**. 정렬 조건(sort : 최신순, 평점 높은 순, 평점 낮은 순) |
| `pageNum` | `@RequestParam Integer ` | **Required**. 페이지(page) |

✔️ **Response**

```json
{
    "code": 200,
    "message": "조회성공",
    "data": {
        "content": [
            {
                "nickname": "park1234",
                "profile_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/profile3.png",
                "review_score": 5,
                "review_content": "다음에 또 사려고요!!!",
                "product_name": "레드 블레미쉬 클리어 수딩 크림",
                "option_name": "단품 70ml",
                "create_at": "2024-06-27"
            },
            {
                "nickname": "현준",
                "profile_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/profile4.png",
                "review_score": 4,
                "review_content": "좋아요좋아요",
                "product_name": "레드 블레미쉬 클리어 수딩 크림",
                "option_name": "세트 1+1 기획",
                "create_at": "2024-06-25"
            },
            {
                "nickname": "park1234",
                "profile_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/profile3.png",
                "review_score": 5,
                "review_content": "좋아요",
                "product_name": "레드 블레미쉬 클리어 수딩 크림",
                "option_name": "단품 70ml",
                "create_at": "2024-06-23"
            },
            {
                "nickname": "현아2",
                "profile_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/ff7ecedb-a45e-42d6-a419-791f89ae4029.jpg",
                "review_score": 5,
                "review_content": "다음에 또 사려고요",
                "product_name": "레드 블레미쉬 클리어 수딩 크림",
                "option_name": "단품 70ml",
                "create_at": "2024-06-22"
            },
            {
                "nickname": "현아",
                "profile_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/5f7ca547-e82e-4787-8282-c21d20f83857.jpg",
                "review_score": 4,
                "review_content": "너무 잘쓰고 있어서 다음에도 사용할게요",
                "product_name": "레드 블레미쉬 클리어 수딩 크림",
                "option_name": "단품 70ml",
                "create_at": "2024-06-20"
            },
            {
                "nickname": "현아2",
                "profile_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/ff7ecedb-a45e-42d6-a419-791f89ae4029.jpg",
                "review_score": 5,
                "review_content": "다음에 또 사려고요",
                "product_name": "레드 블레미쉬 클리어 수딩 크림",
                "option_name": "단품 70ml",
                "create_at": "2024-06-20"
            },
            {
                "nickname": "현아2",
                "profile_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/ff7ecedb-a45e-42d6-a419-791f89ae4029.jpg",
                "review_score": 1,
                "review_content": "안녕하세요",
                "product_name": "레드 블레미쉬 클리어 수딩 크림",
                "option_name": "단품 70ml",
                "create_at": "2024-06-19"
            },
            {
                "nickname": "현아2",
                "profile_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/ff7ecedb-a45e-42d6-a419-791f89ae4029.jpg",
                "review_score": 3,
                "review_content": "안녕하세요",
                "product_name": "레드 블레미쉬 클리어 수딩 크림",
                "option_name": "단품 70ml",
                "create_at": "2024-06-19"
            },
            {
                "nickname": "현아2",
                "profile_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/ff7ecedb-a45e-42d6-a419-791f89ae4029.jpg",
                "review_score": 5,
                "review_content": "좋은데요",
                "product_name": "레드 블레미쉬 클리어 수딩 크림",
                "option_name": "단품 70ml",
                "create_at": "2024-06-19"
            },
            {
                "nickname": "현아",
                "profile_img": "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/5f7ca547-e82e-4787-8282-c21d20f83857.jpg",
                "review_score": 5,
                "review_content": "캐시 테스트",
                "product_name": "레드 블레미쉬 클리어 수딩 크림",
                "option_name": "단품 70ml",
                "create_at": "2024-06-19"
            }
```

#### 문의 글 답변(관리자 전용)

```
  POST /product/answer?question-id=3

```
✔️ **Request**

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

✔️ **Request**

| Name | Type | Requried | description          |
| --- | --- | --- |----------------------|
| sortby | String | false | 정렬 기준 (default=sales) |
| page | Integer | false | 첫 페이지 번호 (default=0) |
| size | Integer | false | 페이지 당 데이(default=24) |
| token |  | false | user의 likes가 true로 표시됨 |

✔️ **Response**

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

✔️ **Request**

| Name | Type | Requried | description          |
| --- | --- | --- |----------------------|
| category | Integer | true | 카테고리 번호              |
| sortby | String | false | 정렬 기준 (default=sales) |
| page | Integer | false | 첫 페이지 번호 (default=0) |
| size | Integer | false | 페이지 당 데이터 개수 (default=24) |
| token |  | false | user의 likes가 true로 표시됨 |

✔️ **Response**

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

✔️ 요청 예시

```
https://drugstoreproject.shop/main/category/8?sortby=likes&page=0&size=3
```

✔️ 응답 예시

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

✔️ **Request**

| Name | Type | Requried | description          |
| --- | --- | --- |----------------------|
| keyword | Integer | true | 검색어 (브랜드명 또는 상품명) 두 글자 일치 검색 |
| sortby | String | false | 정렬 기준 (default=sales) |
| page | Integer | false | 첫 페이지 번호 (default=0) |
| size | Integer | false | 페이지 당 데이터 개수 (default=24) |
| token |  | false | user의 likes가 true로 표시됨 |

✔️ **Response**

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

✔️ 요청 예시

```
https://drugstoreproject.shop/main/find?keyword=ml&sortby=sales&page=0&size=3
```

✔️ 응답 예시

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
### MyPage API

#### 리뷰 작성

```
  POST /mypage/review/{orders_id}
```
✔️ **Request**

Token in the Header

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `orders_id` | `@PathVariable Integer` | 주문 id |
| `review_content` | `String` |  후기 내용 |
| `review_score`      | `Integer` | 후기 점수(1~5점)

✔️ **Response**

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `review_id` | `Integer` | 리뷰 번호 |
| `content` | `String` | 후기 내용 |
| `score`      | `Integer` | 후기 점수 |
| `option_name` | `String` | 옵션 이름 |
| `create_at` | `LocalDate` | 리뷰 작성일 |
| `product_name` | `String` |  상품 이름 |
| `product_img` | `String` |  상품 이미지 |

```json
{
   "review_id" : "1",
   "content" : "좋아요",
   "score" : "3",
   "option_name" : "단품 70mL",
   "create_at" : "2024-06-28",
   "product_name" : "레드 블레미쉬 클리어 수딩 크림",
   "product_img" : "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/Screenshot+2024-06-16+at+19.52.00.png"
}
```

#### 리뷰 수정

```
  PUT /mypage/review/{orders_id}
```
✔️ **Request**

Token in the Header

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `orders_id` | `@PathVariable Integer` | 주문 id |
| `review_content` | `String` | 후기 내용 |
| `review_score`      | `Integer` | 후기 점수(1~5점)

✔️ **Response**

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `review_id` | `Integer` |  리뷰 번호 |
| `content` | `String` |  후기 내용 |
| `score`      | `Integer` | 후기 점수 |
| `option_name` | `String` |  옵션 이름 |
| `create_at` | `LocalDate` |  리뷰 작성일 |
| `product_name` | `String` |  상품 이름 |
| `product_img` | `String` |  상품 이미지 |

```json
{
   "review_id" : "1",
   "content" : "엄청 좋아요",
   "score" : "5",
   "option_name" : "단품 70mL",
   "create_at" : "2024-06-28",
   "product_name" : "레드 블레미쉬 클리어 수딩 크림",
   "product_img" : "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/Screenshot+2024-06-16+at+19.52.00.png"
}
```

#### 리뷰 삭제

```
  DELETE /mypage/review/{orders_id}
```
✔️ **Request**

Token in the Header

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `orders_id` | `@PathVariable Integer` | 주문 id |

✔️ **Response**

```json
{
   "message" : "리뷰가 삭제되었습니다."
}

```

#### 구매한 상품 목록 조회

```
  GET /mypage/order
```
✔️ **Request**

Token in the Header

✔️ **Response**

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `orders_id` | `Integer` | Required. 주문 id |
| `product_img` | `String` | Required. 상품 이미지 |
| `price`      | `Integer` | Required. 가격 |
| `product_name` | `String` | Required. 상품 이름 |
| `option_name` | `String` | Required. 옵션 이름 |
| `brand` | `String` | Required. 상품 브랜드 |
| `review_status` | `Boolean` | Required. 리뷰 작성 여부 |
| `review_deadline` | `LocalDate` | Required. 리뷰 작성 기간 |

```json
{
   "orders_id":"3",
   "product_img":"https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/Screenshot+2024-06-16+at+19.52.00.png"
   "price":"15000",
   "product_name":"레드 블레미쉬 클리어 수딩 크림",
   "option_name":"단품 70mL",
   "brand":"닥터지",
   "review_status":"Y",
   "review_deadline":"2024-07-27(1달)"
}
```

#### 회원 정보 조회

```
  GET /mypage/userInfo
```
✔️ **Request**

Token in the Header

✔️ **Response**

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `name` | `Integer` | Required. 이름 |
| `nickname` | `String` | Required. 별명 |
| `phone_number` | `String` | Required. 전화번호 |
| `profile_pic` | `String` | Required. 사진 |
| `birthday` | `String` | Required. 생년월일 |
| `address` | `Boolean` | Required. 주소 |
| `email` | `LocalDate` | Required. 이메일 |

```json
{
   "name": "현준",
   "nickname": "leelee",
   "birthday": "2000-01-01",
   "phone_number": "01012345678",
   "address" : "seoul",
   "profile_pic" : "사진 URL",
   "email" : "super@gmail.com"
}
```

#### 작성한 QnA 조회

```
  GET /mypage/question
```
✔️ **Request**

Token in the Header

✔️ **Response**

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `question` | `String` | Required. 질문 내용 |
| `answer` | `String` | Required. 답변 내용 |
| `user_name` | `String` | Required. 회원명 |
| `create_at` | `LocalDate` | Required. 작성일 |
| `product_name` | `String` | Required. 상품 이름 |
| `brand` | `String` | Required. 상품 브랜드 |
| `question_answer_id` | `Integer` | Required. QnA id |
| `question_status` | `Boolean` | Required. 답변 여부  |

```json
{
	 "Question":"효과 있나요?",
	 "Answer":"답변",
	 "user_name":"현준",
	 "created_at":"2024-05-01",
	 "product_name":"어성초 77 수딩 토너",
	 "brand" : "아누아",
	 "question_status" : "F",
	 "question_answer_id": "1"
}
```
#### 작성한 리뷰 조회

```
  GET /mypage/reviews
```
✔️ **Request**

Token in the Header

✔️ **Response**

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `review_id` | `Integer` | Required. 리뷰 id |
| `orders_id` | `Integer` | Required. 주문 id |
| `product_image` | `String` | Required. 상품 이미지 URL |
| `product_name` | `String` | Required. 상품 이름 |
| `price` | `Integer` | Required. 가격 |
| `brand` | `String` | Required. 상품 브랜드 |
| `review_score` | `Integer` | Required. 후기 점수 |
| `review_content` | `String` | Required. 후기 내용  |
| `option_name` | `String` | Required. 옵션 이름 |
| `create_at` | `LocalDate` | Required. 작성일 |

```json
{
   "review_id" : 1,
   "orders_id" : 1,
   "product_image" : "https://drugstorebucket.s3.ap-northeast-2.amazonaws.com/Screenshot+2024-06-16+at+19.52.00.png",
   "product_name" : "레드 블레미쉬 클리어 수딩 크림",
   "price" : 10000,
   "brand" : "닥터지",
   "review_score" : 5,
   "review_content" : "엄청 좋아요",
   "option_name" : "단품 70mL",
   "create_at" : 2020-05-04
}
```

#### 보유한 쿠폰 조회

```
  GET /mypage/coupon
```
✔️ **Request**

Token in the Header

✔️ **Response**

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `money` | `String` | Required. 포인트 |
| `coupon_name` | `String` | Required. 쿠폰 이름 |
| `coupon_percent` | `Integer` | Required. 쿠폰 할인율 |

```json
{
   "money" : "100000",
   "coupon_name" : "행복한 쿠폰",
   "coupon_percent" : 10
}
```
## 📌 Lessons Learned

### Lessons

#### **이메일 인증 코드 API 구현 경험**

***JavaMailSender***

프로젝트를 진행하면서 이메일 인증 코드 API를 구현하기 위해 구글 SMTP와 JavaMailSender를 사용하는 방법을 배웠습니다. 이는 사용자들에게 직접 이메일을 보내는 기능을 구현하는 데 유용했습니다.

배운 점:

	1. 구글 SMTP 사용: 구글 SMTP를 생성하여 JavaMailSender와 함께 사용하면 사용자에게 직접 메일을 보낼 수 있다는 것을 알게 되었습니다.
	2. 설정 방법:
		- 라이브러리를 추가합니다.
		- SMTP 설정을 생성하고, 이를 YAML 파일에 설정합니다.
		- JavaMailSender를 사용하여 메일을 보냅니다.
  
***Redis***

이메일 인증 API를 구현하면서 인증 번호를 처리하는 효율적인 방법을 찾기 위해 여러 가지를 고려했습니다. 처음에는 인증 번호를 데이터베이스에 저장하는 방법을 생각했지만, 이는 비효율적이라는 결론을 내렸습니다. 그 과정에서 Redis를 알게 되었고, 이를 활용한 인증 번호 처리 방식을 구현했습니다.

배운 점:

	1. Redis 활용: 인증 번호는 일시적으로 사용되고 버려지기 때문에 데이터베이스에 저장하는 것보다 Redis에 저장하는 것이 더 효율적이라는 것을 알게 되었습니다.	
	2. 구현 방법:
		- 인증 번호 전송 API가 실행되면, Redis 서버에 이메일을 키(key)로, 인증 번호를 값(value)으로 저장합니다.
		- Redis 설정을 통해 인증 번호는 일정 시간 동안만 저장되도록 설정합니다.
		- 사용자가 이메일과 인증 번호를 입력하면, Redis에 저장된 키와 값을 비교하여 인증을 처리합니다.
이 경험을 통해 Redis가 일시적인 데이터 저장 및 처리에 매우 유용하다는 것을 배웠습니다. 이는 데이터베이스의 부하를 줄이고 성능을 향상시키는 데 큰 도움이 됩니다.

#### **Spring Cache 적용**

***Cacheable/CacheEvict***

스프링 캐시를 적용하여 매번 같은 데이터를 반복적으로 불러오는 SQL 쿼리 실행을 줄이기로 했지만, 단순히 key와 value만 설정해주면 적용될 줄 알았습니다. 그러나 다른 부분에서 변경이 일어나면 캐시된 데이터가 업데이트되지 않는 문제를 발견했습니다. 이를 해결하기 위해 @CacheEvict를 사용했지만, 왜 이런 일이 발생하는지를 깊이 고민해보았습니다.

배운 점:

	1. 다양한 키값 설정: 해당 서비스는 페이지와 정렬 순서에 따라 결과가 달라지기 때문에 단순한 상품 ID만으로는 충분하지 않다는 것을 깨달았습니다.
	2. 복합 키 사용: #productId + '_' + #criteria + '_' + #pageNum과 같이 여러 개의 키값을 조합하여 캐시를 저장할 수 있다는 것을 배웠습니다. 이를 통해 다양한 조건에 따라 캐시된 데이터를 정확히 관리할 수 있게 되었습니다.
	3. 적용 시 고려사항: 새롭게 배운 스프링 캐시를 적용할 때는 데이터의 변경 가능성을 고려하여 적절한 키값을 설정해야 한다는 것을 깨달았습니다.

이 경험을 통해 스프링 캐시의 효율적인 사용 방법을 배우게 되었고, 앞으로의 프로젝트에서는 데이터의 동적 변화에 대응할 수 있는 적절한 캐시 전략을 적용할 계획입니다.


### Laearned

***Jasypt***

개발 중 보안이 필요한 변수가 점점 늘어나면서 환경 변수에 하나씩 저장하는 데 어려움을 겪었습니다. 그러던 중, 팀원의 Jasypt 코드를 통해 하나의 환경 변수를 사용하여 여러 보안 변수를 효율적으로 관리할 수 있다는 것을 배웠습니다.

이 프로젝트에서 Jasypt의 유용성을 깨달았으며, 다음 프로젝트에서는 제가 직접 Jasypt를 적용해 보려 합니다.

***S3***

이전 프로젝트에서는 이미지를 저장하고 불러오는데 처음이다보니 많은 어려움을 겪었습니다 결국 최선의 방법으로 GitHub에 이미지를 저장하여 불러오는데 성공은 하였지만 깃허브에 저장하는 방법은 유용하다고 생각이 들지 않아, 이번 프로젝트에서는 이미지를 저장하고 불러오는 방식을 제대로 구현하기로 했습니다. 제가 담당하지 않은 부분이었지만, 팀원이 개발한 S3 코드를 통해 많은 것을 배웠습니다.

팀원은 S3 서버를 구축하여 이미지를 저장하고, 저장된 이미지를 URL로 받아 데이터베이스에 저장하는 방식을 사용했습니다. 이 접근 방식이 매우 효율적임을 깨달았고, 다음 프로젝트에서는 제가 직접 S3를 적용해 볼 계획입니다.


## 👍🏻 Feedback

✔️ API url restful

✔️ interface name Jpa -> Repository

✔️ service 안에 내용들은 다른 개발자도 보기 쉽게 메소드로 따로 빼서 가독성 높이기

✔️ class에서 생성자 static 메소드 만들어 service 부담 줄이기, 가독성 높이기




