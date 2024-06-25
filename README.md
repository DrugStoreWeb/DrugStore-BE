
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





## Skillset


<p align="center">
  <a href="https://skillicons.dev">
    <img src="https://skillicons.dev/icons?i=java,spring,aws,gradle,mysql,nginx,redis,postman,github,discord,notion&theme=light" />
  </a>
</p>


