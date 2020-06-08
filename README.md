# POC PRODF JWT COM LDAP

Para executar a POC efetue os passos abaixo:

Entre o diretior back e digite:

```bash
mvn spring-boot:run
```

Entre no diretorio front e digita:

caso ainda naõ tenha instalado:

```bash
npm install
```

e despois execute o servidor fronte-end

```bash
npm start
```
## Observações
Login e senha estão fixo no codigo, não precisa escrever user/pass no login, vai sempre como ben/benspassword

Abra a inspersão do codigo para ver, os dados estão a maioria no console log.

Os dados do LDAP poder ser visto no arquivo: test-server.ldif
