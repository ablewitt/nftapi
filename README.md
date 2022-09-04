# nftapi
nftguy api

## Build cardano wallet appi dependancy
1. Run gradle task to generate source code / build files from openapi spec file ```cardano-wallet-openapi.yaml```
```bash
./gradlew generateSwaggerCodeCardanowallet
```
2. Give gradlew in cardano wallet build execute permissions
```bash
chmod +x build/swagger-code-cardanowallet/gradlew
```
3. Create cardano wallet jar file
```bash
cd build/swagger-code-cardanowallet
./gradlew jar
```
