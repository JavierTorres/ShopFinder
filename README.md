# ShopFinder

The following project is an RESTful API to find the nearest shop by given coordinates comparing to the shops in-memory.

## Endpoints

* Add a shop. This endpoint creates a shop in-memory and search for the coordinates using Google Mas Geolocation API. The action of finding the coordinates for the specific shop is executed asynchronous to return a quick HTTP response without waiting for the response from the Geolocation API service.
    - Request
        - /
        - POST
        - Content-Type: application/json
        - parameters: name, address, postcode
        - example: {"name":"Torres","address":"1 High Street","postCode":"sg12 42u"}
    - Response
        - Header: contains the location of the resource created (HATEOS)
        - Content: Empty
        - Status: 201

* Nearest. Find the nearest shop for a given coordinates (latitude and longitude).
    - Request
        - /nearest
        - GET
        - URL parameters for the request: customerLongitude, customerLatitude
        - Example: http://localhost/shops/nearest?customerLongitude=-0.026374&customerLatitude=52.802037
    - Response. When there are one or more shops in-memory.
        - Header: contains the location of the resource (HATEOS)
        - Content: {"name":"Torres","address":"1 High Street","postCode":"sg12 42u","latitude":52.8020367,"longitude":-0.0263735}
        - Status: 200
    - Response. When there is 0 shops in-memory.
            - Status: 204

* Find the shop by name
     - Request
        - /{shopName}
        - GET
        - Example: http://localhost/shops/Torres
     - Response. Shop Found
        - Content: {"name":"Torres","address":"1 High Street","postCode":"sg12 42u","latitude":52.8020367,"longitude":-0.0263735}
        - Status: 200
     - Response. Shop Not Found
        - Content: Empty
        - Status: 204

## Requirements
* Java 1.7 or later.
* A Google Maps API key.

### API keys

Each Google Maps Web Service request requires an API key or client ID. API keys are freely available with a Google Account at https://developers.google.com/console. The type of API key you need is a Server key.

To get an API key:

Visit https://developers.google.com/console and log in with a Google Account.
Important: This key should be kept secret on your server.

##### Please replace your API key on: src/main/resources/application.properties api.key=[REPLACE]