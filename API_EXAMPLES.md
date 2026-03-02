# API Examples

## Raw Materials Endpoints

### Create a Raw Material

POST http://localhost:8080/api/raw-materials
Content-Type: application/json

{
"code": "FLOUR",
"name": "Wheat Flour",
"stockQuantity": 1000,
"unit": "g",
"description": "High quality wheat flour"
}

### Get All Raw Materials

GET http://localhost:8080/api/raw-materials

### Get Raw Material by ID

GET http://localhost:8080/api/raw-materials/1

### Update Raw Material

PUT http://localhost:8080/api/raw-materials/1
Content-Type: application/json

{
"code": "FLOUR",
"name": "Premium Wheat Flour",
"stockQuantity": 1500,
"unit": "g",
"description": "Updated description"
}

### Delete Raw Material

DELETE http://localhost:8080/api/raw-materials/1

---

## Products Endpoints

### Create a Product

POST http://localhost:8080/api/products
Content-Type: application/json

{
"code": "BREAD",
"name": "White Bread",
"price": 10.00,
"description": "Soft white bread",
"composition": [
{
"rawMaterialId": 1,
"quantityRequired": 200
},
{
"rawMaterialId": 2,
"quantityRequired": 50
}
]
}

### Get All Products

GET http://localhost:8080/api/products

### Get Product by ID

GET http://localhost:8080/api/products/1

### Update Product

PUT http://localhost:8080/api/products/1
Content-Type: application/json

{
"code": "BREAD",
"name": "Premium White Bread",
"price": 12.00,
"description": "Updated description",
"composition": [
{
"rawMaterialId": 1,
"quantityRequired": 250
}
]
}

### Delete Product

DELETE http://localhost:8080/api/products/1

---

## Production Optimization Endpoint

### Get Production Suggestions

GET http://localhost:8080/api/production/optimize

Response Example:
{
"suggestions": [
{
"productId": 2,
"productCode": "CAKE",
"productName": "Chocolate Cake",
"price": 25.00,
"quantityToProce": 2,
"totalValue": 50.00
},
{
"productId": 1,
"productCode": "BREAD",
"productName": "White Bread",
"price": 10.00,
"quantityToProce": 3,
"totalValue": 30.00
}
],
"totalValue": 80.00,
"message": "Production optimized successfully"
}
