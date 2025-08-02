# Projeto Amplicar

Sistema web e mobile para gestão completa de agendamento de consultas psicológicas, desenvolvido para auxiliar psicólogos e suas equipes no controle de pacientes, consultas, pagamentos e análises financeiras.

## 🎯 Objetivo

Digitalizar e otimizar o processo de agendamento de consultas, controle de pacientes e gestão financeira de clínicas psicológicas, proporcionando maior eficiência operacional e controle administrativo.

## 🔧 Como Executar

### Pré-requisitos
- Java 17+
- Node.js 18+
- PostgreSQL 15+
- Flutter 3.x (para mobile)
- Docker (opcional)

### Backend
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend Web
```bash
cd frontend-web
npm install
npm start
```

### Mobile
```bash
cd mobile-app
flutter pub get
flutter run
```

### Docker (Desenvolvimento)
```bash
docker-compose up -d
```

## 📝 Documentação da API

Após executar o backend, acesse a documentação Swagger em:
```
http://localhost:8080/swagger-ui.html
```

## 👥 Público-Alvo

- **Usuários Primários**: Psicólogos e equipe administrativa
- **Usuários Secundários**: Responsáveis administrativos e financeiros

## 🚀 Principais Funcionalidades

### 👤 Gestão de Usuários
- Cadastro completo de pacientes com validação de CPF
- Gestão de psicólogos e equipe administrativa
- Controle de responsáveis e pagadores

### 📅 Gestão de Agendamentos
- Criação e edição de consultas
- Visualização de agenda (diária, semanal, mensal)
- Controle de disponibilidade dos psicólogos
- Sistema de cancelamento com justificativas

### 💰 Gestão Financeira
- Registro e controle de pagamentos
- Dashboard com métricas financeiras em tempo real
- Relatórios de faturamento personalizáveis
- Controle de pendências e análise de receitas

### 🔐 Sistema de Autenticação
- Login seguro com controle de sessão
- Recuperação de senha via email
- Controle de acesso por níveis de permissão

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 📞 Contato

Para dúvidas ou sugestões, entre em contato através das issues do GitHub.

---

**Desenvolvido com ❤️ para otimizar a gestão de clínicas psicológicas**
