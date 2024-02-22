# 📱 My-SNS

## 소셜 미디어
간단한 소셜미디어를 만든 api 프로젝트 입니다.

## 개발 환경

- 에디터 : Intellij Ultimate
- 개발 툴 : SpringBoot 2.7.5
- 자바 : JAVA 11
- 빌드 : Gradle 7.6
- 서버 : AWS EC2
- 배포 : Docker
- 데이터베이스 : MySql 8.0


[//]: # (# Architecture)

[//]: # (![img.png]&#40;assert/erd.png&#41;)

# ERD
![erd.png](assert/erd.png)

### Swagger
API 자동 문서화 하여 보다 API 에 접근하기 편하게 하기 위해 Swagger를 추가 하였습니다.

[Swagger 링크](http://ec2-100-27-6-78.compute-1.amazonaws.com:8080/swagger-ui/index.html)

### GitAction CI & CD 자동 배포화
GitAction 을 통해 main에 push가 되면 자동 빌드 및 배포가 되게 하였습니다
*** 

### 회원 인증·인가
- 모든 회원은 회원가입을 통해 회원이 됩니다.
- 로그인한 회원은 글 작성, 글 수정, 글 삭제, 댓글 작성, 좋아요, 알림 기능이 가능합니다.

### 글쓰기
- 포스트를 쓰려면 회원가입 후 로그인(Token 받기)을 해야 합니다.
- 포스트의 한 페이지는 20개씩 보이고 총 몇 개의 페이지인지 표시가 됩니다.
- 로그인하지 않아도 글 목록을 조회할 수 있습니다.
- 포스트의 수정 기능은 글을 쓴 회원만이 권한을 가집니다.
- 포스트의 삭제 기능은 글을 쓴 회원만이 권한을 가집니다.

### 마이 피드
- 로그인한 회원은 자신이 작성한 글 목록을 볼 수 있습니다.

### 댓글
- 댓글 작성은 회원만이 권한을 가집니다.
- 회원은 다수의 댓글을 달 수 있습니다.

### 좋아요
- 좋아요는 회원만이 권한을 가집니다.

### 알림
- 자신이 쓴 글에 대해 다른 회원이 댓글을 작성하거나 '좋아요'를 눌렀을 때 알림을 받는 기능입니다.
- 알림 목록에서 자신이 쓴 글에 작성된 댓글과 좋아요를 확인할 수 있습니다.
***



### 회원가입과 로그인
- 회원가입
    - 회원가입 성공 시 이메일을 리턴합니다.
- 로그인
    - Spring Security와 JWT를 사용하여 구현합니다.
    - 로그인 성공 시 `token` 을 리턴합니다.
        - {”jwt”:”eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Imt5~~~”}

### 포스트 작성, 상세 조회, 리스트 조회, 수정, 삭제
- 포스트 작성
    - 회원만이 글 작성을 할 수 있습니다.
- 포스트 상세 조회 (1개 조회)
    - 회원, 비회원 모두 조회할 수 있습니다.
    - 글의 제목, 내용, 글쓴이, 작성날짜, 마지막 수정날짜가 표시됩니다.
- 포스트 리스트 조회
    - 회원, 비회원 모두 조회할 수 있습니다.
    - 글의 제목, 내용, 글쓴이, 작성날짜, 마지막 수정날짜가 표시됩니다.
    - 리스트 조회는 페이징 기능이 포함됩니다. **(Pageable 사용)**
        - 한 페이지당 default 피드 갯수는 20개
        - 총 페이지 갯수 표시
        - 작성날짜 기준으로 최신순으로 Sort
- 포스트 수정 / 삭제
    - 글을 작성한 회원만이 글에 대한 수정과 삭제의 권한을 가집니다.

### 마이 피드 조회
- 자신이 작성한 글을 모두 조회하는 기능입니다.
- 글의 제목, 내용, 글쓴이, 작성날짜가 표시됩니다.
- 피드 조회는 페이징 기능이 포함됩니다. **(Pageable 사용)**
    - 한 페이지당 default 피드 갯수는 20개
    - 총 페이지 갯수 표시
    - 작성날짜 기준으로 최신순으로 Sort

### 댓글 작성, 조회, 수정, 삭제
- 댓글 작성
    - 댓글 작성은 로그인한 사람만 권한을 가집니다.
- 댓글 조회
    - 댓글 조회는 모든 접근이 권한을 가집니다. (Token 없이 가능)
    - 댓글, 글쓴이, 글 번호, 작성날짜가 표시됩니다.
    - 댓글 조회는 페이징 기능이 포함됩니다. **(Pageable 사용)**
        - 한 페이지당 default 피드 갯수는 10개
        - 총 페이지 갯수 표시
        - 작성날짜 기준으로 최신순으로 Sort
- 댓글 수정 / 삭제
    - 댓글을 작성한 회원만이 댓글에 대한 수정과 삭제의 권한을 가집니다.

### 좋아요
- 좋아요 누르기
    - ‘좋아요’는 한 번만 누를 수 있습니다. (중복으로 누르는 경우는 에러 처리)
- 좋아요 조회는 모든 접근이 권한을 가집니다. (Token 없이 가능)

### 알림
- 알림 리스트 조회
    - 특정 User의 알림 목록을 받아옵니다.
    - 특정 포스트에 새 댓글이 달리거나 좋아요가 눌리면 알림이 등록됩니다.
    - 알림 기능은 페이징 기능이 포함됩니다. **(Pageable 사용)**
        - 한 페이지당 default 피드 갯수는 20개
        - 총 페이지 갯수 표시
        - 작성날짜 기준으로 최신순으로 Sort

### 에러 처리
1. ErrorCode에서 정의한 HttpStatus를 StatusCode로 Return 합니다.
    - ex) join 할 때 duplicated 면 409 나올 것
2. result.errorCode가 존재합니다. ex)⭐ DUPLICATED_USER_NAME
3. message가 존재합니다.

ex)
```json
{
  "resultCode":"ERROR",
  "result":{
     "errorCode":"POST_NOT_FOUND",
     "message":"존재하지 않는 포스트입니다."
  }
}
```

| 에러 코드               | 설명              | 상태 코드        |
|---------------------|-----------------|--------------|
| INVALID_PASSWORD    | 틀린 password     | UNAUTHORIZED (401) |
| INVALID_PERMISSION  | 사용자 권한 없음       | UNAUTHORIZED (401) |
| INVALID_TOKEN       | 잘못된 token       | UNAUTHORIZED (401) |
| COMMENT_NOT_FOUND   | comment 찾을 수 없음 | NOT_FOUND (404) |
| POST_NOT_FOUND      | post 찾을 수 없음    | NOT_FOUND (404) |
| TOKEN_NOT_FOUND     | token 찾을 수 없음   | NOT_FOUND (404) |
| EMAIL_NOT_FOUND     | email 찾을 수 없음   | NOT_FOUND (404) |
| DUPLICATED_NICKNAME | nickname 중복     | CONFLICT (409) |
| DATABASE_ERROR      | DB 에러           | INTERNAL_SERVER_ERROR (500) |


[//]: # (### Post가 삭제 되었을 때 처리)

[//]: # (- Post와 Comment, Like가 관계를 맺고 있어서 댓글이나 Like가 있는 글을 지울 때 문제가 생길 수 있습니다.)

[//]: # (- Soft Delete를 사용해서 처리)

[//]: # (    - Post는 delete쿼리를 날리지 않고&#40;= 삭제하지 않고&#41; deleted_at이라는 컬럼을 false에서 true로 변경)

[//]: # (    - 조회 할 때 deleted_at 컬럼이 false인 것만 가져오게 설정)

[//]: # (***)

# 📍 엔드 포인트
기본 url: `/api/v1`

### 회원 인증·인가
회원 url: `/users`
- 회원가입: POST `/join`
    - 입력 폼 (JSON 형식)
      ```json
      {
          "email" : "email@gmail.com",
          "password" : "password",
          "nickname" : "닉네임"
      }
      ```

    - 리턴 (JSON 형식)

        ```json
        {
            "resultCode": "SUCCESS",
            "result": {
                "userId": 5,
                "email": "email@gmail.com"
            }
        }
        ```

- 로그인: POST `/login`
    - 입력 폼 (JSON 형식)
      ```json
      {
          "email": "email@gmail.com",
          "password" : "password"
      }
        ```

    - 리턴 (JSON 형식)

       ```json
       {
            "resultCode": "SUCCESS",
            "result": {
                "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6IuudvOydtOyWuCIsImlhdCI6MTY3MzIzNzkzOCwiZXhwIjoxNjczMjQxNTM4fQ.ZXXdacXMjv0orTevIsW5b0l3sHq5GOfTjr_LefPj9yc"
            }
       }
       ```

### 포스트
포스트 url: `/posts`
- 포스트 등록: POST
    - 입력 폼 (JSON 형식)

      ```json
      {
          "title" : "만나서 반가워",
          "body" : "나는 갈기가 없는 수사자 라이언이야"
      }
      ```

    - 리턴 (JSON 형식)

        ```json
        {
            "resultCode":"SUCCESS",
            "result":{
                "message":"게시글 등록 완료",
                "postId":8
            }
        }
        ```

- 포스트 리스트 조회: GET
    - 리턴 (JSON 형식)

      ```json
      {
          "resultCode": "SUCCESS",
          "result": {
              "content": [
                  {
                      "id": 1,
                      "title": "제목_1",
                      "body": "내용_1",
                      "nickname": "nick_name1",
                      "likes": 0,
                      "createdAt": "2024-02-21 20:58:06",
                      "lastModifiedAt": "2024-02-21 20:58:06"
                  },
                  {
                      "id": 8,
                      "title": "만나서 반가워",
                      "body": "나는 갈기가 없는 수사자 라이언이야",     
                      "nickname": "nick_name2",
                      "likes": 0,
                      "createdAt": "2024-02-21 20:56:51",
                      "lastModifiedAt": "2024-02-21 20:56:51"
                  }
              ],
              "pageable": {
                  "sort": {
                      "unsorted": false,
                      "sorted": true,
                      "empty": false
                  },
                  "pageNumber": 0,
                  "pageSize": 20,
                  "offset": 0,
                  "paged": true,
                  "unpaged": false
              },
              "totalPages": 1,
              "totalElements": 2,
              "last": true,
              "numberOfElements": 2,
              "sort": {
                  "unsorted": false,
                  "sorted": true,
                  "empty": false
              },
              "first": true,
              "number": 0,
              "size": 20,
              "empty": false
          }
      }
      ```

- 포스트 상세 조회: GET `/{postId}`
    - 리턴 (JSON 형식)

      ```json
      {
          "resultCode": "SUCCESS",
          "result": {
               "id": 8,
               "title": "만나서 반가워",
               "body": "나는 갈기가 없는 수사자 라이언이야",       
               "nickname": "nick",
               "likes": 0,
               "createdAt": "2024-02-21 20:56:51",
               "lastModifiedAt": "2024-02-21 20:56:51"
          }
      }
      ```
- 포스트 수정: PUT `/{postId}`
    - 입력 폼 (JSON 형식)

      ```json
      {
         "title" : "만나서 반가워. 내 이름은 라이언이야!",
         "body" : "나는 갈기가 없는 수사자야"
      }
      ```

    - 리턴 (JSON 형식)

        ```json
        {
            "resultCode":"SUCCESS",
            "result":{
                "message":"게시글 수정 완료",
                "postId":8
            }
        }
        ```

- 포스트 삭제: DELETE `/{postId}`
    - 리턴 (JSON 형식)

      ```json
      {
          "resultCode":"SUCCESS",
          "result":{
              "message":"게시글 삭제 완료",
              "postId":8
          }
      }
      ```
- 마이 피드 조회: GET `/my`
    - 리턴 (JSON 형식)

        ```json
        {
            "resultCode": "SUCCESS",
            "result": {
                "content": [
                    {
                       "id": 8,
                       "title": "만나서 반가워",
                       "body": "나는 갈기가 없는 수사자 라이언이야",       
                       "nickname": "nick",
                       "likes": 0,
                       "createdAt": "2024-02-21 20:56:51",
                       "lastModifiedAt": "2024-02-21 20:56:51"
                    }
                ],
                "pageable": {
                    "sort": {
                        "unsorted": false,
                        "sorted": true,
                        "empty": false
                    },
                    "pageNumber": 0,
                    "pageSize": 20,
                    "offset": 0,
                    "paged": true,
                    "unpaged": false
                },
                "totalPages": 1,
                "totalElements": 1,
                "last": true,
                "numberOfElements": 1,
                "sort": {
                    "unsorted": false,
                    "sorted": true,
                    "empty": false
                },
                "first": true,
                "number": 0,
                "size": 20,
                "empty": false
            }
        }
        ```

### 댓글
댓글 url: `/posts`
- 댓글 작성: POST `/{postId}/comments`
    - 입력 폼 (JSON 형식)

      ```json
      {
          "comment" : "어피치야~ 안녕!"
      }
      ```

    - 리턴 (JSON 형식)

        ```json
        {
            "resultCode": "SUCCESS",
            "result":{
                "id": 4,
                "comment": "어피치야~ 안녕!",
                "nickname": "라이언",
                "postId": 9,
                "createdAt": "2024-02-21 13:36:34"
            }
        }
        ```

- 댓글 조회: GET `/{postId}/comments`
    - 리턴 (JSON 형식)

      ```json
      {
        "resultCode": "SUCCESS",
        "result": {
            "content": [
                {
                    "id": 5,
                    "comment": "어피치야! 만나서 반가워 ㅎㅎ",
                    "nickname": "춘식",
                    "postId": 9,
                    "createdAt": "2024-02-21 13:39:53",
                    "lastModifiedAt": "2024-02-21 21:11:26"
                },
                {
                    "id": 4,
                    "comment": "어피치야~ 안녕!",
                    "nickname": "라이언",
                    "postId": 9,
                    "createdAt": "2024-02-21 13:36:34",
                    "lastModifiedAt": "2024-02-21 21:11:26"
                }
            ],
            "pageable": {
                "sort": {
                    "unsorted": false,
                    "sorted": true,
                    "empty": false
                },
                "pageNumber": 0,
                "pageSize": 10,
                "offset": 0,
                "paged": true,
                "unpaged": false
            },
            "totalPages": 1,
            "totalElements": 2,
            "last": true,
            "numberOfElements": 2,
            "sort": {
                "unsorted": false,
                "sorted": true,
                "empty": false
            },
            "first": true,
            "number": 0,
            "size": 10,
            "empty": false
        }
      }
      ```

- 댓글 수정: PUT `/{postId}/comments/{id}`
    - 입력 폼 (JSON 형식)

      ```json
      {
          "comment" : "어피치야! 만나서 반가워 ㅎㅎ 나랑 친하게 지내자!"
      }
      ```

    - 리턴 (JSON 형식)

        ```json
        {
            "resultCode": "SUCCESS",
            "result":{
                "id": 5,
                "comment": "어피치야! 만나서 반가워 ㅎㅎ 나랑 친하게 지내자!",
                "nickname": "춘식",
                "postId": 9,
                "createdAt": "2024-02-21 13:39:53",
                "lastModifiedAt": "2024-02-21 13:47:32"
            }
        }
        ```

- 댓글 삭제: DELETE `/{postId}/comments/{id}`
    - 리턴 (JSON 형식)

      ```json
      {
          "resultCode": "SUCCESS",
          "result":{
              "message": "댓글 삭제 완료",
              "id": 5
          }
      }
      ```

### 좋아요
좋아요 url: `/posts`
- 좋아요 누르기:POST `/{postId}/likes`

    - 리턴 (JSON 형식)

        ```json
        {
            "resultCode":"SUCCESS",
            "result": "좋아요를 눌렀습니다."
        }
        ```

- 좋아요 개수: GET `/{postId}/likes`
    - 리턴 (JSON 형식)

        ```json
        {
            "resultCode":"SUCCESS",
            "result": 1
        }
        ```

### 알림
알림 url: `/alarms`
- 특정 사용자의 글에 대한 알림 조회: GET
    - 리턴 (JSON 형식)
      ```json
      {
        "resultCode": "SUCCESS",
        "result": {
            "content": [
                {
                    "id": 3,
                    "fromUser": "nick",
                    "message": "좋아요",
                    "createdAt": "2024-02-21 21:25:49"
                },
                {
                    "id": 2,
                    "fromUser": "nick",
                    "message": "새로운 댓글",
                    "createdAt": "2024-02-21 21:22:49"
                },
                {
                    "id": 1,
                    "fromUser": "nick",
                    "message": "새로운 댓글",
                    "createdAt": "2024-02-21 21:20:40"
                }
            ],
            "pageable": {
                "sort": {
                    "unsorted": false,
                    "sorted": true,
                    "empty": false
                },
                "pageNumber": 0,
                "pageSize": 20,
                "offset": 0,
                "paged": true,
                "unpaged": false
            },
            "totalPages": 1,
            "totalElements": 3,
            "last": true,
            "numberOfElements": 3,
            "sort": {
                "unsorted": false,
                "sorted": true,
                "empty": false
            },
            "first": true,
            "number": 0,
            "size": 20,
            "empty": false
        }
      }
      ```

[//]: # (      ![jacoco_before.png]&#40;assert/jacoco_before.png&#41;)

[//]: # ()
[//]: # ([//]: # &#40;      ![jacoco_after.png]&#40;assert/jacoco_after.png&#41;&#41;)
