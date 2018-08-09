# authorization-server
Spring + OAuth2

## motivation
There are strict segregation between frontend and backend in your organisation or having multiple backends(microservices), having authorization server will give us to centralise user authentication process.

## start
curl https://start.spring.io/starter.tgz -d dependencies=web,security,data-jpa,devtools | tar -xzvf -

## Authorization server configuration with custom userdetails service
* Authroization server itself is protected by basic authentication, it will be problem if authorization server's API is called directly from user browser. So it is better to do reverse proxy from frontend web application.
* Utilise exsting userdetails service.
* CORS is not relevant because API will be connected by the frontend app host not user's browser. 
* Using oauth's scope will be determined, but API is protected by the role of user.

## Resource server   
* API of resource will be called directly from the clinet's browser, so it shall allow cross origin resource sharing(CORS).

## Test
* Get token
```
$ curl -X POST -d 'grant_type=password&username=appuser&password=mysecret' --user authuser:secret localhost:18081/oauth/token

{"access_token":"3f443dad-9807-4016-9938-6769380f09a7","token_type":"bearer","refresh_token":"ff5608ed-3471-44f5-b57d-086693a83c6f","expires_in":86399,"scope":"customer"}
```

* Refresh token
```
$ curl -X POST -d 'refresh_token=ff5608ed-3471-44f5-b57d-086693a83c6f&grant_type=refresh_token' --user authuser:secret localhost:18081/oauth/token

{"access_token":"7ed00cde-aed1-4fb7-94be-d45e1089690d","token_type":"bearer","refresh_token":"ff5608ed-3471-44f5-b57d-086693a83c6f","expires_in":86399,"scope":"customer"}
```

* Access resource - preflight
```
$ curl -H "Origin: https://www.trust.com.au" \
> -H "Access-Control-Request-Method: GET" \
> -H "Access-Control-Request-Headers: X-Requested-With" \
> -H "Authorization: bearer 7ed00cde-aed1-4fb7-94be-d45e1089690d" \
> -X OPTIONS localhost:18081/secret/me -I

HTTP/1.1 200
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Access-Control-Allow-Origin: https://www.trust.com.au
Access-Control-Allow-Methods: GET
Access-Control-Allow-Headers: X-Requested-With
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Length: 0
Date: Thu, 09 Aug 2018 04:10:11 GMT
```

* Access resource with devil origin - preflight
```
$ curl -H "Origin: https://www.devil.com.au" \
> -H "Access-Control-Request-Method: GET" \
> -H "Access-Control-Request-Headers: X-Requested-With" \
> -H "Authorization: bearer 7ed00cde-aed1-4fb7-94be-d45e1089690d" \
> -X OPTIONS localhost:18081/secret/me -I

HTTP/1.1 403
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Length: 20
Date: Thu, 09 Aug 2018 04:13:31 GMT
```