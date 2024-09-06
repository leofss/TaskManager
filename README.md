# TaskManager

Este repositório contém a aplicação TaskManager em Java e Spring Boot.

## Descrição do Projeto

Desenvolver uma aplicação de gerenciamento de tarefas (todo list) com as seguintes funcionalidades:

1. **Cadastro de Usuários**:
   - Criar, editar, excluir e listar usuários.
   - Cada usuário deve ter um nome e nível (admin, user).
2. **Gerenciamento de Tarefas**:
   - Criar, editar, excluir e listar tarefas.
   - Cada tarefa deve ter um título, descrição, data de criação, data de vencimento, status (pendente, em andamento, concluída) e um usuário associado.
3. **Filtros e Ordenação**:
   - Permitir que as tarefas sejam filtradas por status.
   - Permitir que as tarefas sejam ordenadas por data de vencimento.
4. **Associação de Tarefas a Usuários**
   - Permitir que tarefas sejam atribuídas a usuários específicos.
   - Permitir que as tarefas de um usuário específico sejam listadas.


## Instruções para Implementação Com Docker

### Passos para Configuração
 
1. Clone o repositório:   
    ```bash
    git clone git@github.com:leofss/TaskManager.git
    cd TaskManager
    ```
2. Suba o container (**verifique se a porta 8081 está disponível**)
   ```bash
    docker-compose up
    ```
3. Acesse [Swagger local](http://localhost:8081/swagger-ui/index.html) para verificar os endpoints
   
4. Siga as Instruções de utilização mais a baixo  

## Instruções para Implementação Sem Docker

1. [Instale Java SDK 21](https://www.oracle.com/br/java/technologies/downloads/#java21)
2. [Instale Maven](https://maven.apache.org/install.html)
3. [Instale PostgreSQL](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)
4. [Instale PgAdmin](https://www.pgadmin.org/download/)

### Passos para Configuração

1. Clone o repositório:
   
    ```bash
    git clone git@github.com:leofss/TaskManager.git
    cd TaskManager
    ```
    
3. Crie em seu PgAdmin um banco de dados com nome "taskmanager"
4. Abra o arquivo application.properties localizado em TaskManager/src/main/resources/application
5. Altere as propriedades de acordo com as credenciais do seu banco:
   
    ```bash
    spring.datasource.url = jdbc:postgresql://{host}:{porta}/taskmanager
    spring.datasource.username = {db_username}
    spring.datasource.password = {db_pass}
    ```
    
6. Em seu cmd acesse a pasta contendo o código fonte clonado do github e execute o comando para instalar as depêndencias do projeto
   
    ```bash
    mvn clean install
    ```
    
7. Ainda em seu cmd rode o projeto (**verifique se a porta 8080 e a do banco de dados estão disponíveis**)
   
    ```bash
    mvn spring-boot:run
    ```
    
## Passos para Utilização

1. O sistema irá criar um usuário com permissão de admin para começar as requisições
2. Todos os endpoints exigem a inserção de um token no header como um Bearer Token 
   
    ```bash
    //Credenciais
    {
    "email":"admin@gmail.com",
    "password":"12345"
    }
    ```
3. Acesse [Swagger local](http://localhost:8080/swagger-ui/index.html) para verificar os endpoints (Caso tenha usado docker estará na porta 8081)
   
   3.1 Lembre-se de após logar como admin autenticar seu token no botão "Authorize" no Swagger pois
   os endpoints requerem um token, e alguns apenas permitem tokens com permissão de ADMIN

### JSON Utils

Aqui anexarei alguns objetos json para a utlização dos endpoints

1. Usuários
     ```bash
    {
        "username":"ciclano",
        "email":"ciclano@gmail.com",
        "password":"12345",
        "role":"USER"
    }
    ```
2. Task
   ```bash
     {
        "title":"Tarefa 3",
        "description":"Tarefa 3",
        "due_date":"2026-09-01T15:30",
        "status":"PENDENTE", //PENDENTE, CONCLUIDA, EM_ANDAMENTO
        "users_ids":[3, 5] //Ids acessíveis via endpoint de listagem de usuários
      }
    ```

    

   
