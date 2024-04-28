# Nexfar Report Generator REST API

### Exemplo de uso:

**POST** http://localhost:8080/report/generate
```
{
	"key": "ORDER_DETAILED",
	"format": "CSV",
	"filters": [
		{
			"key": "cnpj",
			"operation": "EQ",
			"value1": "92584796000276"
		},
		{
			"key": "createdAt",
			"operation": "INTERVAL",
			"value1": "2020-11-25 10:30",
			"value2": "2024-12-16 11:00"
		},
		{
			"key": "status",
			"operation": "EQ",
			"value1": "DELIVERED"
		},
		{
			"key": "netTotal",
			"operation": "GTE",
			"value1": "100"
		}
	]
}
```
