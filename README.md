# Projeto_Banco_de_Dados_Distribuidos

Este projeto consiste em um Middleware para Banco de Dados Distribuído Homogêneo e Autônomo. O sistema orquestra a comunicação entre múltiplos nós de banco de dados (MySQL), garantindo a consistência dos dados através do protocolo Two-Phase Commit (2PC) e mantendo a alta disponibilidade com o Algoritmo de Eleição de Bully.

---

## Requisitos do Projeto Satisfeitos

O middleware foi desenvolvido para atender aos seguintes critérios técnicos:

1.  **Comunicação via Sockets:** Toda a troca de informações entre os nós ocorre através de Sockets TCP (porta `5000`).
2.  **Protocolo Customizado:** Implementação de um protocolo proprietário para mensagens estruturadas (`QUERY`, `PREPARE`, `COMMIT`, `HEARTBEAT`, `ELECTION`).
3.  **Configuração Dinâmica:** Possibilidade de configurar os nós através de IPs definidos no arquivo `config/nodes.json`.
4.  **DDM Homogêneo Autônomo:** Todos os nós executam a mesma lógica e possuem autonomia para processar requisições.
5.  **Replicação Total:** Qualquer alteração (`INSERT`, `UPDATE`, `DELETE`) efetuada em um nó é replicada síncronamente em todos os outros nós.
6.  **Coordenação e Alta Disponibilidade:** O sistema elege um coordenador inicial. Caso ele falhe, o **Algoritmo de Bully** entra em ação para eleger um novo líder.
7.  **Comunicação Unicast:** As mensagens são enviadas diretamente para os IPs da rede, garantindo entrega confiável via TCP.
8.  **Propriedades ACID:** Garantia de atomicidade e consistência através do protocolo **Two-Phase Commit (2PC)**.
9.  **Monitoramento (Heartbeat):** Todos os nós informam periodicamente ao coordenador que estão ativos.
10. **Integridade via Checksum:** Utilização de **MD5** para verificar a integridade de cada mensagem transmitida.
11. **Balanceamento de Carga:** As requisições de leitura podem ser distribuídas entre qualquer nó da rede.
12. **Transparência e Logs:** Cada nó loga no console as queries requisitadas e o conteúdo transmitido.

---

## Tecnologias Utilizadas

* **Linguagem:** Java 17+
* **Banco de Dados:** MySQL 8.0
* **Orquestração:** Docker & Docker Compose
* **Interface:** Java Swing (DDBClient)

---

## Arquitetura do Sistema

O sistema opera em uma rede onde cada container contém uma instância do Middleware Java e uma instância do MySQL.

---

## Como Executar

### 1. Subir Docker 

```
docker-compose up --build -d
```

### 2. Compilar o projeto e executar interface

```
javac -cp "lib/*" -d out src/br/com/ddb/common/*.java src/br/com/ddb/middleware/*.java src/br/com/ddb/client/*.java

java -cp "out;lib/*" br.com.ddb.client.DDBClient

```

## Comandos de Testes Sugeridos

1. Inserção : INSERT INTO produtos (nome, quantidade, preco) VALUES ('Notebook', 10, 3500.00)

2. Atualização : UPDATE produtos SET preco = 3200.00 WHERE nome = 'Notebook'

3. Remoção : DELETE FROM produtos WHERE nome = 'Notebook'

4. Consulta : SELECT * FROM produtos

5. Eleição : docker stop node3 (o Nó 2 deve detectar a falha e se tornar o novo coordenador)