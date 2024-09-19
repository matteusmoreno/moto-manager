# MotoManager

**MotoManager** é uma aplicação robusta desenvolvida com Java e Spring Framework para o gerenciamento eficiente de lojas de motos. Este sistema permite que os administradores controlem o estoque de motos, gerenciem vendas e atendam clientes com facilidade. Entre suas funcionalidades principais estão o gerenciamento de inventário de motos, controle de vendas e pedidos, manutenção de registros de clientes e relatórios detalhados de desempenho da loja. MotoManager visa proporcionar uma experiência intuitiva e otimizada para gerenciar todas as operações essenciais de uma loja de motos, ajudando a maximizar a eficiência e melhorar o atendimento ao cliente.

## Tecnologias Utilizadas

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.2-brightgreen)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-%E2%9C%94-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring%20Security-%E2%9C%94-brightgreen)
![Flyway](https://img.shields.io/badge/Flyway-%E2%9C%94-blue)
![Lombok](https://img.shields.io/badge/Lombok-%E2%9C%94-blue)
![Swagger](https://img.shields.io/badge/Swagger-%E2%9C%94-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)
![Docker](https://img.shields.io/badge/Docker-%E2%9C%94-blue)
![Springdoc OpenAPI](https://img.shields.io/badge/Springdoc%20OpenAPI-2.0.2-blue)
![Maven](https://img.shields.io/badge/Maven-%E2%9C%94-green)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3-orange)
![Microsoft Azure](https://img.shields.io/badge/Microsoft%20Azure-%E2%9C%94-blue)


## Estrutura do Projeto

A estrutura do projeto é organizada da seguinte forma:

```js
moto-manager/
│
├── env/
│   └── application-properties.env
│   └── mysql.env
│   └── rabbitmq.env
├── src/
│   ├── main/
│   │   ├── java/
│   │   ├── resources/
│   │       └── application.properties 
│   │       └── private.pem
│   │       └── public.pem
│   │       └── db/
│   │           └── migration/
│   │        
│   ├── test/
│   └── ...
│
├── target/
│   └── moto-manager-0.0.1-SNAPSHOT.jar
│
│
├── Dockerfile
├── docker-compose.yml
│
├── pom.xml
│
├── .dockerignore
├── README.md
└── HELP.md
```

## Configuração do Ambiente

### Preparar a Pasta `env` e os Arquivos `.env`

Para configurar o ambiente da aplicação, você precisa criar a pasta `env` e os arquivos de configuração necessários. Siga estas etapas:

1. **Crie a pasta `env`** na raiz do projeto.
2. **Crie os arquivos de configuração** dentro da pasta `env` com os seguintes nomes e conteúdos:


   - **`application-properties.env`**: Este arquivo deve conter as variáveis de ambiente para a aplicação Spring Boot.

     ```env
     SPRING_DATASOURCE_URL=jdbc:mysql://mysql_db:3306/moto_manager  
     SPRING_DATASOURCE_USERNAME=root  
     SPRING_DATASOURCE_PASSWORD=root
     
     jwt.public.key=classpath:public.pem
     jwt.private.key=classpath:private.pem
     
     spring.rabbitmq.addresses=rabbitmq-url
     ```

   - **`mysql.env`**: Este arquivo deve conter as variáveis de ambiente para o banco de dados MySQL.

     ```env
     MYSQL_ROOT_PASSWORD=root  
     MYSQL_DATABASE=moto_manager
     ```
     
   - **`rabbitmq.env`**: Este arquivo deve conter as variáveis de ambiente para o RabbitMQ.

     ```env
     RABBITMQ_DEFAULT_USER=guest 
     RABBITMQ_DEFAULT_PASS=guest
     ```

#### Gerar Chaves

Para gerar as chaves, você pode usar os comandos OpenSSL a seguir:

1. **Gerar a chave privada**

   Execute o comando abaixo para gerar uma chave privada RSA e salvá-la em um arquivo chamado `private.pem`:

```bash
openssl genrsa -out private.pem 2048
```

- `openssl genrsa` é o comando usado para gerar uma chave RSA.
- `-out private.pem` especifica o nome do arquivo onde a chave privada será salva.
- `2048` é o tamanho da chave em bits, que é um padrão recomendado para a maioria dos casos.

2. **Gerar a chave pública a partir da chave privada**

Após gerar a chave privada, execute o seguinte comando para criar a chave pública correspondente e salvá-la em um arquivo chamado `public.pem`:

```bash
openssl rsa -in private.pem -pubout -out public.pem
```


- `openssl rsa` é o comando usado para manipular chaves RSA.
- `-in private.pem` especifica o arquivo de entrada que contém a chave privada.
- `-pubout` indica que queremos gerar a chave pública.
- `-out public.pem` especifica o nome do arquivo onde a chave pública será salva.

Certifique-se de armazenar `private.pem` e `public.pem` no diretório `src/main/resources/` do projeto.


### Docker

Para executar o projeto usando Docker, siga estes passos:

1. **Construa e Inicie os Contêineres**

   Navegue até o diretório `docker` e execute o comando:
```bash
   docker-compose up --build
```
   Este comando irá construir a imagem do Docker para a aplicação e iniciar o contêiner MySQL e a aplicação.

2. **Acessar a Aplicação**

   A aplicação estará disponível em http://localhost:8080.

3. **Parar e Remover os Contêineres**

   Para parar os contêineres, você pode usar:
```bash
   docker-compose down
```

### Swagger

A aplicação utiliza Swagger para a documentação da API, facilitando a visualização e a interação com os endpoints expostos pela aplicação. Swagger fornece uma interface gráfica para explorar a API, testar endpoints e visualizar a estrutura da API.

#### Acessar a Documentação do Swagger

Depois de iniciar a aplicação, você pode acessar a interface do Swagger em:

```bash
http://localhost:8080/swagger-ui/index.html
```
Nessa interface, você encontrará uma lista de todos os endpoints disponíveis, suas descrições e a possibilidade de testar cada um diretamente pelo navegador.

## Dependências

- **Spring Boot Starter Data JPA**: Para integração com o banco de dados e JPA.
- **Spring Boot Starter Validation**: Para validação de dados.
- **Spring Boot Starter Web**: Para criar endpoints RESTful.
- **Spring Cloud Starter OpenFeign**: Para integração com outros serviços.
- **MySQL Connector**: Para comunicação com o banco de dados MySQL.
- **Lombok**: Para reduzir o código boilerplate.
- **Spring Boot Starter Test**: Para testes de unidade.
- **Flyway**: Para migrações de banco de dados.
- **Springdoc OpenAPI Starter WebMVC UI**: Para a documentação da API com Swagger.

## Scripts e Configurações

- **Dockerfile**: Configura o ambiente de execução da aplicação.
- **docker-compose.yml**: Define e gerencia os serviços Docker necessários para a aplicação.
- **.dockerignore**: Define quais arquivos e pastas devem ser ignorados pelo Docker.

## Contribuindo

Se você deseja contribuir para o projeto, siga estas etapas:

1. **Faça um Fork do Repositório**
2. **Crie uma Branch para sua Feature ou Correção**
3. **Realize as Alterações e Teste-as**
4. **Envie um Pull Request**

## Licença

Este projeto está licenciado sob a [Licença MIT](LICENSE).
