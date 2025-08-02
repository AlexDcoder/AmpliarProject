# Sistema de Agendamento de Consultas Psicológicas

## 1. Visão Geral do Projeto

### 1.1 Descrição
Sistema web e mobile para gestão de agendamento de consultas psicológicas, desenvolvido para auxiliar psicólogos e suas equipes no controle de pacientes, consultas, pagamentos e análises financeiras.

### 1.2 Objetivo
Digitalizar e otimizar o processo de agendamento de consultas, controle de pacientes e gestão financeira de uma clínica psicológica, proporcionando maior eficiência operacional e controle administrativo.

### 1.3 Público-Alvo
- **Usuários Primários**: Psicólogos e equipe administrativa da clínica
- **Usuários Secundários**: Responsáveis administrativos e financeiros

## 2. Requisitos Funcionais

### 2.1 Gestão de Usuários (RF001-RF010)

**RF001 - Cadastro de Pacientes**
- O sistema deve permitir o cadastro completo de pacientes com dados pessoais
- Campos obrigatórios: nome completo, CPF, telefone, data de nascimento
- Validação de CPF único no sistema

**RF002 - Edição de Dados do Paciente**
- O sistema deve permitir a edição de informações dos pacientes cadastrados
- Histórico de alterações deve ser mantido

**RF003 - Exclusão de Pacientes**
- O sistema deve permitir exclusão lógica de pacientes
- Pacientes com histórico de consultas não podem ser excluídos fisicamente

**RF004 - Listagem e Busca de Pacientes**
- O sistema deve exibir lista paginada de pacientes
- Funcionalidade de busca por nome, CPF ou telefone

**RF005 - Cadastro de Psicólogos**
- O sistema deve permitir cadastro de psicólogos da equipe
- Campos: nome completo, CPF, telefone, email, senha de acesso

**RF006 - Gestão de Responsáveis**
- O sistema deve permitir cadastro de responsáveis pelos pacientes
- Vinculação entre responsável e paciente através de relacionamento

**RF007 - Cadastro de Pagadores**
- O sistema deve permitir cadastro de pessoas responsáveis pelo pagamento
- Pode ser o próprio paciente ou terceiros

### 2.2 Gestão de Agendamentos (RF011-RF020)

**RF011 - Criação de Atendimentos**
- O sistema deve permitir agendamento de consultas
- Seleção de paciente, psicólogo, data/hora e observações
- Validação de conflitos de horário

**RF012 - Edição de Agendamentos**
- O sistema deve permitir reagendamento de consultas
- Notificação automática das alterações

**RF013 - Cancelamento de Consultas**
- O sistema deve permitir cancelamento com justificativa
- Registro do motivo do cancelamento

**RF014 - Visualização de Agenda**
- O sistema deve exibir agenda do psicólogo em formato de calendário
- Visualizações: diária, semanal e mensal

**RF015 - Gestão de Disponibilidade**
- O sistema deve permitir configuração de horários disponíveis por psicólogo
- Bloqueio de horários indisponíveis

### 2.3 Gestão Financeira (RF021-RF030)

**RF021 - Registro de Pagamentos**
- O sistema deve permitir registro de pagamentos das consultas
- Vinculação entre pagamento e atendimento específico
- Informações: valor, data, forma de pagamento

**RF022 - Controle de Pendências**
- O sistema deve identificar consultas não pagas
- Listagem de pendências por paciente/responsável

**RF023 - Dashboard Financeiro**
- O sistema deve exibir métricas financeiras em tempo real
- Total apurado, receita mensal, receita por psicólogo

**RF024 - Relatórios de Faturamento**
- O sistema deve gerar relatórios financeiros personalizáveis
- Filtros por período, psicólogo, paciente

**RF025 - Análise de Receitas**
- O sistema deve calcular estatísticas de faturamento
- Comparativos mensais e anuais

### 2.4 Sistema de Autenticação (RF031-RF035)

**RF031 - Login de Usuários**
- O sistema deve autenticar psicólogos através de email/senha
- Controle de sessão e timeout automático

**RF032 - Recuperação de Senha**
- O sistema deve permitir recuperação de senha via email
- Geração de token temporário para redefinição

**RF033 - Controle de Acesso**
- O sistema deve implementar níveis de permissão
- Acesso diferenciado para psicólogos e administradores

## 3. Requisitos Não Funcionais

### 3.1 Performance (RNF001-RNF005)

**RNF001 - Tempo de Resposta**
- O sistema deve responder em até 2 segundos para operações básicas
- Operações de relatório devem executar em até 5 segundos

**RNF002 - Capacidade**
- O sistema deve suportar até 1000 pacientes ativos
- Suporte a até 50 agendamentos simultâneos por dia

**RNF003 - Disponibilidade**
- O sistema deve ter disponibilidade de 99% durante horário comercial
- Manutenções programadas em horários de menor uso

### 3.2 Segurança (RNF006-RNF010)

**RNF006 - Proteção de Dados**
- Implementação de criptografia para dados sensíveis
- Conformidade com LGPD para proteção de dados pessoais

**RNF007 - Autenticação Segura**
- Senhas devem seguir política de segurança (mínimo 8 caracteres)
- Bloqueio de conta após tentativas de login falhadas

**RNF008 - Auditoria**
- O sistema deve registrar logs de todas as operações
- Rastreabilidade de alterações em dados críticos

### 3.3 Usabilidade (RNF011-RNF015)

**RNF011 - Interface Responsiva**
- Interface web deve ser responsiva para diferentes resoluções
- Compatibilidade com navegadores modernos

**RNF012 - Experiência Mobile**
- Aplicativo mobile deve seguir padrões de UX nativos
- Interface otimizada para uso em smartphones e tablets

**RNF013 - Acessibilidade**
- Interface deve seguir diretrizes básicas de acessibilidade
- Contraste adequado e navegação por teclado

### 3.4 Manutenibilidade (RNF016-RNF020)

**RNF016 - Código Limpo**
- Implementação seguindo clean code e SOLID principles
- Documentação adequada do código

**RNF017 - Testes Automatizados**
- Cobertura mínima de 70% em testes unitários
- Testes de integração para funcionalidades críticas

## 4. Arquitetura do Sistema

### 4.1 Visão Geral da Arquitetura

O sistema segue uma arquitetura em camadas baseada no padrão MVC (Model-View-Controller), distribuída em múltiplas plataformas:

```
┌─────────────────┬─────────────────┐
│   Frontend Web  │  Mobile App     │
│   (React +      │  (Flutter)      │
│   Tailwind)     │                 │
└─────────────────┴─────────────────┘
           │              │
           └──────┬───────┘
                  │ REST API
         ┌────────▼────────┐
         │   Backend       │
         │ (Spring Boot)   │
         │   - Controller  │
         │   - Service     │
         │   - Repository  │
         └─────────────────┘
                  │
         ┌────────▼────────┐
         │   PostgreSQL    │
         │   Database      │
         └─────────────────┘
```

### 4.2 Stack Tecnológica

**Backend:**
- **Framework**: Spring Boot 3.x
- **Linguagem**: Java 17+
- **Arquitetura**: MVC + Repository Pattern
- **ORM**: JPA/Hibernate
- **Segurança**: Spring Security + JWT
- **Documentação API**: OpenAPI/Swagger

**Frontend Web:**
- **Framework**: React 18+
- **Linguagem**: TypeScript
- **Estilização**: Tailwind CSS
- **Gerenciamento de Estado**: Redux Toolkit ou Zustand
- **HTTP Client**: Axios

**Mobile:**
- **Framework**: Flutter 3.x
- **Linguagem**: Dart
- **Gerenciamento de Estado**: Provider/Riverpod
- **HTTP Client**: Dio

**Banco de Dados:**
- **SGBD**: PostgreSQL 15+
- **Migrations**: Flyway
- **Connection Pool**: HikariCP

### 4.3 Estrutura do Backend (Spring Boot)

```
src/
├── main/
│   ├── java/com/clinica/agendamento/
│   │   ├── config/          # Configurações
│   │   ├── controller/      # Controladores REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # Entidades JPA
│   │   ├── repository/     # Repositórios
│   │   ├── service/        # Serviços de negócio
│   │   ├── security/       # Configurações de segurança
│   │   └── exception/      # Tratamento de exceções
│   └── resources/
│       ├── db/migration/   # Scripts Flyway
│       └── application.yml # Configurações
└── test/                   # Testes unitários e integração
```

### 4.4 Modelo de Dados

Baseado no diagrama fornecido, o modelo contempla:

**Entidades Principais:**
- **Paciente**: Dados pessoais dos pacientes
- **Psicologo**: Informações dos profissionais
- **Atendimento**: Registros de consultas agendadas
- **Pagamento**: Controle financeiro das consultas
- **Responsavel**: Responsáveis pelos pacientes
- **Pagador**: Responsáveis pelos pagamentos

**Relacionamentos:**
- Paciente ← 1:N → Atendimento
- Psicologo ← 1:N → Atendimento  
- Atendimento ← 1:1 → Pagamento
- Responsavel ← N:M → Paciente
- Pagador ← 1:N → Pagamento

### 4.5 APIs RESTful

**Endpoints Principais:**

```
/api/v1/pacientes
├── GET    /              # Listar pacientes
├── POST   /              # Criar paciente
├── GET    /{id}          # Buscar por ID
├── PUT    /{id}          # Atualizar paciente
└── DELETE /{id}          # Excluir paciente

/api/v1/atendimentos
├── GET    /              # Listar agendamentos
├── POST   /              # Criar agendamento
├── GET    /{id}          # Buscar por ID
├── PUT    /{id}          # Atualizar agendamento
└── DELETE /{id}          # Cancelar agendamento

/api/v1/pagamentos
├── GET    /              # Listar pagamentos
├── POST   /              # Registrar pagamento
├── GET    /pendentes     # Listar pendências
└── GET    /dashboard     # Métricas financeiras

/api/v1/psicologos
├── GET    /              # Listar psicólogos
├── POST   /              # Cadastrar psicólogo
├── GET    /{id}/agenda   # Agenda do psicólogo
└── PUT    /{id}          # Atualizar dados
```

### 4.6 Segurança

**Autenticação:**
- JWT tokens para autenticação stateless
- Refresh tokens para renovação automática
- Criptografia BCrypt para senhas

**Autorização:**
- Role-based access control (RBAC)
- Roles: ADMIN, PSICOLOGO, SECRETARIA

**Proteção de Dados:**
- Criptografia AES-256 para dados sensíveis
- HTTPS obrigatório em produção
- Sanitização de inputs para prevenir XSS/SQL Injection

### 4.7 Deploy e Infraestrutura

**Ambientes:**
- **Desenvolvimento**: Local (Docker Compose)
- **Homologação**: Cloud (AWS/Heroku)
- **Produção**: Cloud com backup automático

**Containerização:**
- Docker para backend e banco de dados
- Nginx como proxy reverso
- SSL/TLS certificado
