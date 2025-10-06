# Arena Battle Tanks - Sistema de Gerenciamento de Batalhas

## 👥 Autores do Projeto
- **Rafael Albuquerque**
- **Daniel Henrique** 
- **Matheus Lima**
- **Cauê Milhomen**

## 🎯 Descrição do Projeto
Sistema completo de gerenciamento para uma arena de batalhas de tanques, permitindo cadastro de tanques, agendamento de partidas, simulação de batalhas PvE/PvP, ranking de desempenho e relatórios detalhados.

## 🏗️ Arquitetura e Conexões entre Classes

### **Camada Principal (`Main.java`)**
- **Função**: Controlador principal do sistema com menu interativo
- **Conexões**: 
  - `CadastroService` - para gerenciar tanques
  - `AgendamentoService` - para partidas
  - `BattleSimulator` - para simulações
  - `RankingService` - para estatísticas

### **Camada de Dados**
#### `CadastroService.java` + `TanqueDAO.java`
- **Responsabilidade**: CRUD completo de tanques
- **Conexões**:
  - `TanqueDAO` → `Conexao` (banco de dados)
  - `Tanque` e suas subclasses (`TanqueLeve`, `TanqueMedio`, `TanquePesado`)
  - `AgendamentoService` - fornece lista de tanques para partidas

#### `Conexao.java`
- **Função**: Gerenciar conexão com MySQL
- **Configuração**: `jdbc:mysql://localhost:3306/tanques`

### **Hierarquia de Tanques**
#### `Tanque.java` (Classe Abstrata)
- **Subclasses**: `TanqueLeve`, `TanqueMedio`, `TanquePesado`
- **Composição**: 
  - Lista de `Arma` (`Canhao`, `Metralhadora`, `Missil`)
  - Atributos específicos por classe

#### `TanquePadrao.java`
- **Função**: Implementação concreta para persistência no banco

### **Sistema de Armas (Polimorfismo)**
#### `Arma.java` (Classe Abstrata)
- **Subclasses**: `Canhao`, `Metralhadora`, `Missil`
- **Polimorfismo**: 
  - `calcularDano()` - implementação específica por arma
  - `calcularRecarga()` - varia por classe do tanque

### **Sistema de Partidas**
#### `AgendamentoService.java`
- **Responsabilidade**: Agendar e gerenciar partidas
- **Conexões**:
  - `Partida` - entidade principal
  - `CadastroService` - para obter tanques disponíveis
  - Verifica conflitos de horário e arena

#### `Partida.java`
- **Atributos**: modo, data/hora, arena, lista de tanques, eventos
- **Regras**: Limites por modo (1v1=2, 3v3=6, 5v5=10 tanques)

### **Sistema de Batalha**
#### `BattleSimulator.java`
- **Funcionalidade**: Simular batalhas entre tanques
- **Inteligência**: IA para tanques controlados por computador
- **Conexões**: 
  - `Tanque` - para ataques e defesas
  - `RankingService` - atualiza estatísticas

### **Sistema de Ranking e Estatísticas**
#### `RankingService.java`
- **Responsabilidade**: Coletar e exibir métricas de desempenho
- **Conexões**:
  - `EstatisticasTanque` - armazena dados individuais
  - `CadastroService` - para buscar tanques
  - Reset automático semanal/mensal

#### `EstatisticasTanque.java`
- **Métricas**: Abates, dano, precisão, vitórias
- **Cálculos**: Score baseado em múltiplos fatores

### **Exportação de Dados**
#### `ExportadorCSV.java`
- **Função**: Exportar dados dos tanques para arquivo CSV
- **Local**: Salva na pasta Documents do usuário

## 🎮 Funcionalidades Principais

### 1. **Cadastro de Tanques**
- Limite de 12 tanques simultâneos
- Três classes com características únicas
- Persistência em banco MySQL

### 2. **Agendamento de Partidas**
- Múltiplos modos (Treino, 1v1, 3v3, 5v5)
- Verificação de conflitos de horário
- Três arenas com características diferentes

### 3. **Sistema de Batalha Realista**
- Cálculo de dano considerando:
  - Tipo de arma e munição
  - Setor do alvo (frontal/lateral/traseiro)
  - Terreno e distância
  - Classe do tanque

### 4. **Estatísticas e Ranking**
- Score baseado em múltiplos fatores
- Rankings gerais e por modo
- Timeline de eventos
- Relatórios de eficiência

### 5. **Exportação e Relatórios**
- Exportação CSV dos tanques
- Mapas de calor de horários
- Análise de disponibilidade da frota

## 🔧 Tecnologias Integradas

### **Banco de Dados MySQL**
- `Conexao.java` gerencia conexão
- `TanqueDAO.java` implementa operações CRUD
- Persistência completa dos tanques

### **Exportação CSV**
- `ExportadorCSV.java` usando FileWriter
- Exporta para Documents do usuário

### **Maven para Dependências**
- **JUnit** - testes unitários
- **OpenCSV** - exportação CSV  
- **MySQL Connector** - conexão com banco

## ⚙️ Configuração do Banco
```sql
CREATE DATABASE tanques;
-- A tabela é criada automaticamente pelo sistema
```

## 🚀 Como Executar
1. Configurar MySQL na porta 3306
2. Criar database `tanques`
3. Executar `Main.java`
4. Seguir menu interativo

## 🎯 Principais Padrões OO Aplicados

### **Herança**
- `Tanque` ← `TanqueLeve`, `TanqueMedio`, `TanquePesado`
- `Arma` ← `Canhao`, `Metralhadora`, `Missil`

### **Polimorfismo**
- `calcularDano()` e `calcularRecarga()` implementados diferentemente em cada arma
- Comportamento varia por classe do tanque

### **Encapsulamento**
- Atributos privados com getters/setters
- Lógica de negócio encapsulada em serviços

### **Composição**
- Tanques compostos por múltiplas armas
- Partidas compostas por múltiplos tanques

O projeto demonstra domínio completo dos conceitos de POO com integração prática de tecnologias externas para criar um sistema robusto e escalável.
