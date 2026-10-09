# Kanban frontend

Aplicação Angular 20 standalone para o backend Spring Boot em `../backend/kanban-back`.

## Executar

1. Inicie o backend, com MySQL disponível em `localhost:3306`:
   ```bash
   cd ../backend/kanban-back
   ./mvnw spring-boot:run
   ```
2. Inicie o frontend:
   ```bash
   cd frontend
   npm install
   npm start
   ```
3. Abra `http://localhost:4200`.

O `npm start` usa `proxy.conf.json` para encaminhar `/api` a `http://localhost:8080`, removendo o prefixo `/api` antes de enviar a requisição e sem exigir CORS durante o desenvolvimento.

## Funcionalidades integradas

- Cadastro de usuário (`POST /api/users`) e login JWT (`POST /api/auth/login`).
- Armazenamento do token no navegador e envio automático em requisições autenticadas.
- Proteção da rota de quadros.
- Listagem, criação e edição de boards (`GET`, `POST` e `PUT /api/boards/{id}`).
- Definição, inclusão, remoção e ordenação de colunas no formulário de criação e edição.
- Renderização das colunas do board em ordem crescente de `posicao`.
- Estrutura de colunas com Angular CDK Drag and Drop, pronta para receber cards.

## Estado do backend avaliado

O backend possui autenticação JWT, cadastro de usuários e criação, listagem e atualização de boards com colunas. A atualização substitui a coleção de colunas enviada no payload; a tela preserva os IDs recebidos e envia `posicao` para representar corretamente a ordem das colunas existentes. Ainda não há endpoints ou entidades para cards, comentários, etiquetas, responsáveis, WIP ou exclusão de boards. Por isso, a interface não apresenta controles que aparentem persistir essas operações; ela indica a próxima etapa necessária para habilitar o fluxo completo de Kanban.

## Build

```bash
npm run build
```
