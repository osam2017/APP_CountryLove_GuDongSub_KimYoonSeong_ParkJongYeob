# 다음 지도 API

[다음 지도 Web API](http://apis.map.daum.net/web/)를 Node.js에서 간편하게 이용할 수 있도록 하는 모듈입니다.

[![npm](https://img.shields.io/npm/v/daum-map-api.svg?style=flat-square)](https://www.npmjs.com/package/daum-map-api)


## 설치

```sh
$ npm install daum-map-api
```

## 예제

### 지도 검색결과 링크

```javascript
var daum = require('daum-map-api')

daum.map('서울역', function(res) {
  console.log(res)
}
```
[http://map.daum.net/link/map/9113903](http://map.daum.net/link/map/9113903) 를 출력합니다. 

### 정적 이미지 지도 생성

```javascript
var daum = require('daum-map-api')

daum.mapImage('서울역, function(res) {
  console.log(res)
}
```

[http://map2.daum.net/map/imageservice?IW=550&IH=350&MX=493508&MY=1126575&CX=493508&CY=1126575&SCALE=2.5&service=open#.png](http://map2.daum.net/map/imageservice?IW=550&IH=350&MX=493508&MY=1126575&CX=493508&CY=1126575&SCALE=2.5&service=open#.png) 를 출력합니다.

![](http://map2.daum.net/map/imageservice?IW=550&IH=350&MX=493508&MY=1126575&CX=493508&CY=1126575&SCALE=2.5&service=open#.png)


