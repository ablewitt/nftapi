# nftapi
nftguy api

## Build cardano wallet api dependency
1. Run gradle task to generate source code / build files from openapi spec file ```cardano-wallet-openapi.yaml```
```bash
./gradlew generateSwaggerCodeCardanowallet
```
2. Build the cardano wallet jar (using -p to switch project)
```bash
./gradlew -p build/swagger-code-cardanowallet jar
```


## Endpoint health
Spring actuator endpoint health is available from ```/actuator/health```.
Cardano wallet progress can be null if 100% or unknown

```json
{
  "status": "UP",
  "components": {
    "cardanoWalletAPI": {
      "status": "UP",
      "details": {
        "ready": "null"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 389000699904,
        "free": 180503797760,
        "threshold": 10485760,
        "exists": true
      }
    },
    "mongo": {
      "status": "UP",
      "details": {
        "version": "6.1.0"
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```