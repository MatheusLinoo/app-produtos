# Projeto App Produtos

## O que é?
Sistema de gerenciamento de produtos com carrinho de compras, pedidos, promoções e autenticação de usuários.

## Tecnologias Principais 💻
- **Backend**: Java 21 com Spring Boot 3.5.7
- **Banco de Dados**: H2 (embutido)
- **Autenticação**: JWT (JSON Web Tokens)
- **Mapeamento**: MapStruct
- **Migrações**: Flyway
- **Log de Auditoria**: Implementação própria

## Como Rodar 

### Compilar:
mvn clean install

### Iniciar:
mvn spring-boot:run

### Acessar: 
- Aplicação: http://localhost:8080
- Console H2: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:produtosdb)

### Funcionalidades Implementadas:
- Autenticação e autorização de usuários
- CRUD de produtos, categorias e usuários
- Carrinho de compras
- Pedidos
- Cupons de desconto
- Promoções
- Avaliações de produtos
- Controle de estoque
- Auditoria de alterações

### Observações: 
- Banco H2 em memória (dados são perdidos ao reiniciar)
- Configurações padrão do Spring Boot
- API RESTful com tratamento de erros
