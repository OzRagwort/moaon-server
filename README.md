# 😺 애니멀봄의 서버

## 소개

[![Google Play Badge](https://img.shields.io/badge/애니멀봄-ffffff?style=flat)](https://play.google.com/store/apps/details?id=com.ozragwort.peton)
이라는 서비스를 운영하기 위한 API 서버입니다.

유튜브 채널, 영상에 대한 저장, 수정, 조회, 삭제 등의 기능을 합니다.

---

## 📒 API 설명

### Response Format

API가 공통으로 사용하는 Response format(`JSON`)이다.

#### 정상 응답 예제

```json
{
  "success": true,
  "response": {
    "" : "API 별 응답 JSON"
  },
  "error": false
}
```

#### 에러 응답 예제

```json
{
  "success": false,
  "response": null,
  "error": {
    "status": "status code",
    "message": "error message"
  }
}
```

### Controller 의 종류

애니멀봄 서비스에 이용되는 Controller
- `CategoriesApiController`
- `ChannelsApiController`
- `VideosApiController`

데이터 관리를 위한 관리자용 Controller
- `AdminController`
- `AdminCategoriesApiController`
- `AdminChannelsApiController`
- `AdminVideosApiController`

에러 처리를 위한 Controller
- `ErrorPageController`
- `ControllerExceptionHandler`

### Method

`CategoriesApi`, `ChannelsApi`, `VideosApi`는 Create, Read, Update, Delete를 지원합니다.

4가지 HTTP Method를 이용하여 API를 사용할 수 있습니다.

- `POST` : 데이터를 생성(Create)합니다.
- `GET` : 데이터를 조회(Read)합니다.
- `PUT` : 데이터를 수정(Update)합니다.
- `DELETE` : 데이터를 삭제(Detele)합니다. 

### API Status Code

이 API는 4가지 상태 코드를 사용하고 있다. 

|Code|설명|
|:---:|---|
|200|요청이 성공할 경우|
|400|1. 입력 데이터의 타입이 잘못된 경우<br>2. 꼭 필요한 데이터를 빠뜨린 경우<br>3. json에서 ", {, }등이 빠져 양식에 맞지 않는 경우<br>4. 헤더가 빠진 경우 등|
|404|없는 자원을 요청한 경우|
|415|content-type가 application/json이 아닌 경우|

---

## 🔍 API Controller에 대한 설명

### CategoriesApiController
카테고리에 대한 정보를 관리하는 컨트롤러

- `GET /api/moaon/v2/categories/{idx}`
- `GET /api/moaon/v2/categories/{idx}/channels`
- `GET /api/moaon/v2/categories/{idx}/tags`
- `GET /api/moaon/v2/categories/{idx}/videos`
- `GET /api/moaon/v2/categories`

Response JSON format
```json
{
  "idx" : "카테고리의 ID",
  "categoryName" : "카테고리 이름"
}
```

### ChannelsApiController
채널에 대한 정보를 관리하는 컨트롤러

- `POST /api/moaon/v2/channels`
- `PUT /api/moaon/v2/channels/{channelId}`
- `GET /api/moaon/v2/channels/{channelId}`
- `GET /api/moaon/v2/channels/{channelId}/tags`
- `GET /api/moaon/v2/channels/{channelId}/videos`
- `GET /api/moaon/v2/channels`
- `DELETE /api/moaon/v2/channels/{channelId}`

Save Request JSON format
```json
{
  "categoryId" : "채널의 카테고리 ID",
  "channelId" : "채널의 ID",
  "channelName" : "채널명",
  "channelThumbnail" : "채널의 썸네일",
  "uploadsList" : "채널의 업로드 영상 리스트 url",
  "subscribers" : "구독자 수",
  "bannerExternalUrl" : "채널의 배너 url"
}
```

Update Request JSON format
```json
{
  "channelName" : "채널명",
  "channelThumbnail" : "채널의 썸네일",
  "uploadsList" : "채널의 업로드 영상 리스트 url",
  "subscribers" : "구독자 수",
  "bannerExternalUrl" : "채널의 배너 url"
}
```

Response JSON format
```json
{
  "idx" : "채널의 idx",
  "categories" : {"" : "카테고리 응답 json"},
  "channelId" : "채널의 ID",
  "channelName" : "채널명",
  "channelThumbnail" : "채널의 썸네일",
  "uploadsList" : "채널의 업로드 영상 리스트 url",
  "subscribers" : "구독자 수",
  "bannerExternalUrl" : "채널의 배너 url"
}
```

### VideosApiController
영상에 대한 정보를 관리하는 컨트롤러

- `POST /api/moaon/v2/videos`
- `PUT /api/moaon/v2/videos/{videoId}`
- `GET /api/moaon/v2/videos/{videoId}`
- `GET /api/moaon/v2/videos`
- `DELETE /api/moaon/v2/videos/{videoId}`

Save Request JSON format
```json
{
  "channelId" : "채널의 ID",
  "videosId" : "영상의 ID",
  "videosName" : "영상의 제목",
  "videosThumbnail" : "영상의 썸네일",
  "videosDescription" : "영상의 설명",
  "videosPublishedDate" : "영상의 업로드 날짜",
  "videosDuration" : "영상의 길이(초단위)",
  "viewCount" : "조회수",
  "likeCount" : "좋아요 수",
  "dislikeCount" : "싫어요 수",
  "commentCount" : "댓글 수",
  "tags" : ["영상의 태그 들"]
}
```

Update Request JSON format
```json
{
  "videosName" : "영상의 제목",
  "videosThumbnail" : "영상의 썸네일",
  "videosDescription" : "영상의 설명",
  "videosPublishedDate" : "영상의 업로드 날짜",
  "videosDuration" : "영상의 길이(초단위)",
  "viewCount" : "조회수",
  "likeCount" : "좋아요 수",
  "dislikeCount" : "싫어요 수",
  "commentCount" : "댓글 수",
  "tags" : ["영상의 태그 들"]
}
```

Response JSON format
```json
{
  "idx" : "영상의 idx",
  "channels" : {"" : "채널의 응답 json"},
  "videosId" : "영상의 ID",
  "videosName" : "영상의 제목",
  "videosThumbnail" : "영상의 썸네일",
  "videosDescription" : "영상의 설명",
  "videosPublishedDate" : "영상의 업로드 날짜",
  "videosDuration" : "영상의 길이(초단위)",
  "tags" : ["영상의 태그 들"],
  "viewCount" : "조회수",
  "likeCount" : "좋아요 수",
  "dislikeCount" : "싫어요 수",
  "commentCount" : "댓글 수",
  "score" : "영상의 점수"
}
```

---

## 🛠️ Version

### 2.0.0-release4
|Tech|Version|
|---:|:---|
|Spring Boot|2.3.12.RELEASE|
|JAVA|1.8.0_151(25.151-b12)|
|Gradle|6.7|
|JPA|2.3.12.RELEASE|
|H2|1.4.199|
