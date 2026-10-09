# Gerenciador de Tarefas Kanban — Angular + Spring Boot

## 1. Requisitos Funcionais

### Board (Quadro)
- Criar, renomear e excluir quadros (suporte a múltiplos projetos/times)
- Criar, renomear, reordenar e excluir colunas (ex: To Do, Doing, Review, Done)
- Definir limite de WIP (Work In Progress) por coluna (opcional, mas comum em Kanban)

### Cards (Tarefas)
- CRUD de cards: título, descrição, prioridade, responsável, data de vencimento, labels/tags
- Drag and drop de cards entre colunas
- Reordenar cards dentro da mesma coluna
- Comentários e histórico de atividades no card
- Anexos (opcional)
- Checklist de subtarefas dentro do card

### Usuários e Permissões
- Autenticação (login/registro)
- Autorização por papel (admin do quadro, membro, visualizador)
- Atribuição de cards a usuários

### Extras comuns
- Filtros e busca (por responsável, label, prioridade)
- Notificações (prazo próximo, atribuição)
- Atualização em tempo real (via WebSocket) se múltiplos usuários editam o mesmo board

## 2. Requisitos Não Funcionais
- API REST (ou GraphQL) documentada com OpenAPI/Swagger
- Persistência com PostgreSQL ou MySQL
- Segurança com Spring Security + JWT
- Responsividade (mobile/desktop)
- Testes unitários e de integração (JUnit/Mockito no back, Jasmine/Karma ou Jest no front)
- Deploy containerizado (Docker)

## 3. Arquitetura Sugerida

### Backend (Spring Boot)
- `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-security`
- Entidades: `Board`, `Column`, `Card`, `User`, `Comment`, `Label`
- WebSocket com `spring-boot-starter-websocket` (STOMP) se quiser atualização em tempo real entre usuários

### Frontend (Angular)
- Angular standalone components (Angular 17+) ou módulos tradicionais
- State management: `NgRx`, `Signals` (nativo do Angular) ou serviços simples com RxJS, dependendo da complexidade
- Drag and drop: **Angular CDK** (`@angular/cdk/drag-drop`) — escolha definida para o projeto

```bash
ng add @angular/cdk
```

Uso previsto:
- `cdkDropListGroup` para conectar as colunas entre si
- `cdkDropList` em cada coluna
- `cdkDrag` em cada card
- Persistir a nova ordem/coluna no backend via evento `(cdkDropListDropped)`

### Componentes visuais (cards, colunas, badges, avatares)
- **Angular Material** — cards (`mat-card`), chips para labels (`mat-chip`), avatares, tooltips, diálogos para editar card
- Alternativa: Tailwind CSS para estilização customizada sem impor um design system

### Tempo real (se implementado)
- `@stomp/rx-stomp` ou `ngx-stomp-websocket` no frontend, para consumir o WebSocket do Spring Boot

## 4. Escopo de MVP sugerido
1. CRUD de boards e colunas
2. CRUD de cards
3. Drag and drop entre colunas e reordenação (Angular CDK)
4. Autenticação básica (login/registro com JWT)
5. Persistência em PostgreSQL

*Itens como tempo real, anexos, notificações e checklist podem ficar para uma segunda fase.*
