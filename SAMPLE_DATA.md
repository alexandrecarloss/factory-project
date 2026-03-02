## Sample Data for Testing

Este arquivo contém exemplos de dados JSON que podem ser usados para popular o banco de dados para testes.

### Raw Materials

```json
[
  {
    "code": "FLOUR",
    "name": "Wheat Flour",
    "stockQuantity": 1000,
    "unit": "g",
    "description": "High quality wheat flour for baking"
  },
  {
    "code": "SUGAR",
    "name": "White Sugar",
    "stockQuantity": 500,
    "unit": "g",
    "description": "Refined white sugar"
  },
  {
    "code": "EGGS",
    "name": "Eggs",
    "stockQuantity": 50,
    "unit": "unit",
    "description": "Fresh chicken eggs"
  },
  {
    "code": "BUTTER",
    "name": "Butter",
    "stockQuantity": 300,
    "unit": "g",
    "description": "Unsalted butter"
  },
  {
    "code": "CHOCOLATE",
    "name": "Chocolate Powder",
    "stockQuantity": 200,
    "unit": "g",
    "description": "High cocoa chocolate powder"
  }
]
```

### Products

```json
[
  {
    "code": "BREAD",
    "name": "White Bread",
    "price": 10.0,
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
  },
  {
    "code": "CAKE",
    "name": "Chocolate Cake",
    "price": 25.0,
    "description": "Rich chocolate cake",
    "composition": [
      {
        "rawMaterialId": 1,
        "quantityRequired": 300
      },
      {
        "rawMaterialId": 2,
        "quantityRequired": 200
      },
      {
        "rawMaterialId": 3,
        "quantityRequired": 4
      },
      {
        "rawMaterialId": 4,
        "quantityRequired": 100
      },
      {
        "rawMaterialId": 5,
        "quantityRequired": 50
      }
    ]
  },
  {
    "code": "COOKIES",
    "name": "Butter Cookies",
    "price": 8.0,
    "description": "Crispy butter cookies",
    "composition": [
      {
        "rawMaterialId": 1,
        "quantityRequired": 150
      },
      {
        "rawMaterialId": 2,
        "quantityRequired": 80
      },
      {
        "rawMaterialId": 4,
        "quantityRequired": 100
      }
    ]
  },
  {
    "code": "MUFFIN",
    "name": "Chocolate Muffin",
    "price": 6.0,
    "description": "Delicious chocolate muffin",
    "composition": [
      {
        "rawMaterialId": 1,
        "quantityRequired": 100
      },
      {
        "rawMaterialId": 2,
        "quantityRequired": 60
      },
      {
        "rawMaterialId": 3,
        "quantityRequired": 2
      },
      {
        "rawMaterialId": 5,
        "quantityRequired": 30
      }
    ]
  }
]
```

### Importando Dados via API

#### 1. Criar Matérias-Primas

```bash
# Usar arquivo JSON com Postman ou curl
curl -X POST http://localhost:8080/api/raw-materials \
  -H "Content-Type: application/json" \
  -d '{
    "code": "FLOUR",
    "name": "Wheat Flour",
    "stockQuantity": 1000,
    "unit": "g",
    "description": "High quality wheat flour"
  }'
```

#### 2. Criar Produtos

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "code": "BREAD",
    "name": "White Bread",
    "price": 10.00,
    "description": "Soft white bread",
    "composition": [
      {
        "rawMaterialId": 1,
        "quantityRequired": 200
      }
    ]
  }'
```

#### 3. Testar Otimização

```bash
curl -X GET http://localhost:8080/api/production/optimize
```

### Dados Esperados na Resposta

A resposta da otimização de produção deve retornar algo como:

```json
{
  "suggestions": [
    {
      "productId": 2,
      "productCode": "CAKE",
      "productName": "Chocolate Cake",
      "price": 25.0,
      "quantityToProduce": 1,
      "totalValue": 25.0
    },
    {
      "productId": 1,
      "productCode": "BREAD",
      "productName": "White Bread",
      "price": 10.0,
      "quantityToProduce": 3,
      "totalValue": 30.0
    }
  ],
  "totalValue": 55.0,
  "message": "Production optimized successfully"
}
```

### Usando o Frontend para Inserir Dados

1. Abra http://localhost:3000
2. Vá para aba "Raw Materials"
3. Clique em "Add Raw Material"
4. Preencha o formulário e clique em "Save"
5. Repita para todos os insumos
6. Vá para aba "Products"
7. Clique em "Add Product"
8. Preencha os dados e adicione a composição
9. Clique em "Save"
10. Vá para "Production Optimization"
11. Clique em "Optimize Production"

### Limpando Dados

Para apagar todos os dados e começar do zero:

```bash
# Backend: Base de dados será resetada com restart
docker-compose down -v
docker-compose up

# Frontend: Limpar localStorage
localStorage.clear()
```
