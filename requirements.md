# Sistema de Agendamento de Consultas Psicológicas

## Índice
1. [Visão Geral do Projeto](#visão-geral-do-projeto)
2. [Requisitos Funcionais](#requisitos-funcionais)
3. [Requisitos Não Funcionais](#requisitos-não-funcionais)
4. [Arquitetura do Sistema](#arquitetura-do-sistema)
5. [Modelo de Dados](#modelo-de-dados)
6. [Tecnologias Utilizadas](#tecnologias-utilizadas)
7. [Casos de Uso](#casos-de-uso)

---

## Visão Geral do Projeto

O sistema visa automatizar e digitalizar o processo de agendamento de consultas psicológicas, oferecendo uma solução completa para gestão de pacientes, profissionais, agendamentos e pagamentos. O projeto contempla uma aplicação web para administração e uma aplicação mobile para facilitar o acesso dos pacientes.

### Objetivo Principal
Desenvolver uma plataforma integrada que permita o gerenciamento eficiente de consultas psicológicas, desde o agendamento até o controle financeiro, proporcionando uma experiência fluida tanto para os profissionais quanto para os pacientes.

### Stakeholders
- **Psicóloga proprietária**: Usuário principal, responsável pela gestão da clínica
- **Equipe de psicólogos**: Profissionais que atendem na clínica
- **Pacientes**: Usuários finais que agendam e pagam pelas consultas
- **Responsáveis pelos pacientes**: No caso de menores de idade

---

## Requisitos Funcionais

### RF01 - Gerenciamento de Usuários
- **RF01.1**: Cadastrar novos usuários (pacientes, psicólogos, responsáveis)
- **RF01.2**: Editar dados dos usuários existentes
- **RF01.3**: Visualizar lista de usuários com filtros e busca
- **RF01.4**: Inativar/excluir usuários do sistema
- **RF01.5**: Definir perfis de acesso (admin, psicólogo, paciente)

### RF02 - Gerenciamento de Agenda
- **RF02.1**: Criar e configurar agenda para cada psicólogo
- **RF02.2**: Definir horários disponíveis por profissional
- **RF02.3**: Configurar dias e horários de funcionamento
- **RF02.4**: Visualizar agenda em formato calendário
- **RF02.5**: Bloquear horários específicos

### RF03 - Agendamento de Consultas
- **RF03.1**: Visualizar horários disponíveis por psicólogo
- **RF03.2**: Agendar nova consulta
- **RF03.3**: Reagendar consulta existente
- **RF03.4**: Cancelar agendamento
- **RF03.5**: Confirmar presença do paciente
- **RF03.6**: Registrar observações da consulta

### RF04 - Sistema de Pagamentos
- **RF04.1**: Integrar pagamento via PIX para cada consulta
- **RF04.2**: Gerar QR Code para pagamento
- **RF04.3**: Confirmar recebimento do pagamento
- **RF04.4**: Registrar diferentes formas de pagamento
- **RF04.5**: Controlar status de pagamento (pendente, pago, cancelado)

### RF05 - Dashboard e Relatórios
- **RF05.1**: Exibir total de receita geral
- **RF05.2**: Apresentar receita mensal
- **RF05.3**: Mostrar receita por psicólogo
- **RF05.4**: Gerar relatórios de consultas realizadas
- **RF05.5**: Exibir métricas de ocupação da agenda
- **RF05.6**: Relatório de pacientes ativos/inativos

### RF06 - Notificações
- **RF06.1**: Enviar lembretes de consulta para pacientes
- **RF06.2**: Notificar sobre pagamentos pendentes
- **RF06.3**: Alertar sobre cancelamentos
- **RF06.4**: Confirmar agendamentos realizados

### RF07 - Autenticação e Autorização
- **RF07.1**: Login seguro para todos os tipos de usuário
- **RF07.2**: Recuperação de senha
- **RF07.3**: Controle de acesso baseado em perfis
- **RF07.4**: Logout automático por inatividade

---

## Requisitos Não Funcionais

### RNF01 - Performance
- **RNF01.1**: Tempo de resposta máximo de 3 segundos para operações comuns
- **RNF01.2**: Suporte a até 100 usuários simultâneos
- **RNF01.3**: Carregamento de páginas em menos de 2 segundos

### RNF02 - Segurança
- **RNF02.1**: Criptografia de dados sensíveis (LGPD compliance)
- **RNF02.2**: Comunicação HTTPS obrigatória
- **RNF02.3**: Autenticação JWT para APIs
- **RNF02.4**: Backup automatizado diário dos dados
- **RNF02.5**: Log de auditoria para ações críticas

### RNF03 - Usabilidade
- **RNF03.1**: Interface responsiva para web e mobile
- **RNF03.2**: Design intuitivo e acessível
- **RNF03.3**: Suporte a dispositivos touch
- **RNF03.4**: Compatibilidade com principais navegadores

### RNF04 - Confiabilidade
- **RNF04.1**: Disponibilidade de 99.5%
- **RNF04.2**: Sistema de recuperação de falhas
- **RNF04.3**: Validação de dados de entrada
- **RNF04.4**: Tratamento adequado de erros

### RNF05 - Manutenibilidade
- **RNF05.1**: Código documentado e estruturado
- **RNF05.2**: Arquitetura modular
- **RNF05.3**: Testes unitários com cobertura mínima de 70%
- **RNF05.4**: Logs detalhados para debugging

### RNF06 - Portabilidade
- **RNF06.1**: Aplicação mobile compatível com Android e iOS
- **RNF06.2**: Web responsivo para diferentes resoluções
- **RNF06.3**: Banco de dados agnóstico a plataforma

---

## Arquitetura do Sistema

### Arquitetura Geral
O sistema segue uma arquitetura em camadas com separação clara de responsabilidades, utilizando o padrão MVC tanto no frontend quanto no backend.

```
┌─────────────────┐    ┌─────────────────┐
│   Mobile App    │    │    Web App      │
│   (Flutter)     │    │    (React)      │
└─────────────────┘    └─────────────────┘
         │                       │
         └───────────┬───────────┘
                     │
         ┌───────────────────────┐
         │     REST API          │
         │   (Spring Boot)       │
         └───────────────────────┘
                     │
         ┌───────────────────────┐
         │    PostgreSQL         │
         │    Database           │
         └───────────────────────┘
```

### Backend - Spring Boot (Java)
**Estrutura MVC:**
- **Model**: Entidades JPA representando o modelo de dados
- **View**: Respostas JSON através de DTOs
- **Controller**: REST Controllers para exposição das APIs

**Camadas da Aplicação:**
```
Controllers (API Layer)
    ↓
Services (Business Logic)
    ↓
Repositories (Data Access)
    ↓
Entities (Domain Model)
```

### Frontend Web - React
**Estrutura MVC:**
- **Model**: Estado da aplicação (Redux/Context API)
- **View**: Componentes React com Tailwind CSS
- **Controller**: Hooks customizados e serviços

### Mobile - Flutter
**Estrutura MVC:**
- **Model**: Classes de dados e modelos
- **View**: Widgets Flutter
- **Controller**: Controllers e serviços

### Banco de Dados - PostgreSQL
Estrutura relacional conforme diagrama fornecido, com as seguintes entidades principais:
- Paciente
- Psicólogo  
- Responsável
- Atendimento
- Pagamento
- Relacionamentos (Responsável-Paciente)

---

## Tecnologias Utilizadas

### Backend
- **Java 17+**: Linguagem de programação
- **Spring Boot 3.x**: Framework principal
- **Spring Security**: Autenticação e autorização
- **Spring Data JPA**: Acesso a dados
- **PostgreSQL**: Banco de dados relacional
- **Maven**: Gerenciamento de dependências

### Frontend Web
- **React 18+**: Biblioteca para interfaces
- **Tailwind CSS**: Framework de estilos
- **Axios**: Cliente HTTP
- **React Router**: Roteamento
- **React Hook Form**: Gerenciamento de formulários

### Mobile
- **Flutter 3.x**: Framework de desenvolvimento mobile
- **Dart**: Linguagem de programação
- **HTTP**: Cliente para APIs
- **Provider/Bloc**: Gerenciamento de estado

### Infraestrutura
- **Docker**: Containerização
- **Git**: Controle de versão
- **PIX API**: Integração para pagamentos

---

## Casos de Uso

### UC01 - Agendar Consulta
**Atores**: Paciente, Psicólogo
**Descrição**: Permite que um paciente agende uma consulta com um psicólogo disponível
**Fluxo Principal**:
1. Paciente acessa o sistema
2. Seleciona o psicólogo desejado
3. Visualiza horários disponíveis
4. Escolhe data e horário
5. Confirma o agendamento
6. Realiza o pagamento via PIX
7. Recebe confirmação do agendamento

### UC02 - Gerenciar Agenda
**Atores**: Psicólogo, Administrador
**Descrição**: Permite configurar e gerenciar a agenda de atendimentos
**Fluxo Principal**:
1. Psicólogo acessa sua agenda
2. Define horários disponíveis
3. Bloqueia horários indisponíveis
4. Visualiza agendamentos confirmados
5. Registra observações das consultas

### UC03 - Processar Pagamento
**Atores**: Sistema, Paciente/Responsável
**Descrição**: Processa o pagamento da consulta via PIX
**Fluxo Principal**:
1. Sistema gera QR Code PIX
2. Paciente/Responsável realiza pagamento
3. Sistema confirma recebimento
4. Atualiza status do agendamento
5. Envia confirmação por email/SMS

### UC04 - Gerar Relatórios Financeiros
**Atores**: Administrador, Psicólogo
**Descrição**: Gera relatórios financeiros e de desempenho
**Fluxo Principal**:
1. Usuário acessa dashboard
2. Seleciona período desejado
3. Escolhe tipo de relatório
4. Sistema processa dados
5. Exibe relatório interativo
6. Permite exportação em PDF/Excel
