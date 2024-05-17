# TCP Chat in Java (EN)

This is a project for a TCP-based chat system using the Java language. The system allows multiple users to exchange messages in a group chat, where the entry of new users must be approved by the already connected participants, except when the user is entering alone.

## Features

- **Message Exchange**: All connected users can exchange messages in real-time.
- **Controlled Entry**: New users need approval from others to enter the chat, except when they are the first to join.
- **User Identification**: Messages are identified with the username of the sender.
- **Exit Command**: Users can exit the chat by typing "sair" (Portuguese for "exit").

## Project Structure

The project consists of two main classes:

1. **ChatClient.java**: Responsible for managing the client interface, including connection to the server, sending and receiving messages, and user interaction.
2. **ChatServer.java**: Manages the server logic, including accepting new clients, broadcasting messages, and controlling entry of new users.

## How to Run

### Prerequisites

- Installed JDK (version 8 or higher)
- Java development environment (IDE like IntelliJ, Eclipse, or using the terminal)

### Running the Server

1. Compile the server code:

    ```sh
    javac ChatServer.java
    ```

2. Run the server:

    ```sh
    java ChatServer
    ```

### Running the Client

1. Compile the client code:

    ```sh
    javac ChatClient.java
    ```

2. Run the client:

    ```sh
    java ChatClient
    ```

3. When prompted, enter a username to join the chat.

## Code Structure

### ChatClient.java

- **Constructor**: Sets up the connection to the server and input/output streams.
- **start() Method**: Manages user entry into the chat and ongoing interaction.
- **Listener Class**: Runs on a separate thread to listen for and display messages received from the server.

### ChatServer.java

- **main() Method**: Initializes the server and waits for client connections.
- **ClientHandler Class**: Manages communication with a specific client, including entry approval, message broadcasting, and client exit.

## Example Usage

Upon starting the server, it will wait for client connections. Each client, upon connecting, needs to enter a username. If there are other users already connected, they will need to approve the entry of the new user. Messages sent by any user are transmitted to all other connected users.

## Future Enhancements

- **Private Messages**: Add support for sending private messages between users.
- **Online User List**: Display a list of all connected users.
- **Chat Logs**: Store message history in a file.
- **Graphical Interface**: Implement a graphical interface to enhance user experience.

## License

This project is distributed under the MIT license. See the LICENSE file for more details.


Thank you for using the TCP chat system in Java!

# Chat TCP em Java (PT-BR)

Este é um projeto de um sistema de chat baseado em TCP utilizando a linguagem Java. O sistema permite que vários usuários troquem mensagens em um bate-papo coletivo, onde a entrada de novos usuários deve ser aprovada pelos participantes já conectados, exceto quando o usuário está entrando sozinho.

## Funcionalidades

- **Troca de Mensagens**: Todos os usuários conectados podem trocar mensagens em tempo real.
- **Entrada Controlada**: Novos usuários precisam de aprovação dos demais para entrar no chat, exceto quando são os primeiros a entrar.
- **Identificação de Usuários**: As mensagens são identificadas com o nome do usuário que as enviou.
- **Comando de Saída**: Usuários podem sair do chat digitando "sair".

## Estrutura do Projeto

O projeto é composto por duas classes principais:

1. **ChatClient.java**: Responsável por gerenciar a interface do cliente, incluindo a conexão com o servidor, envio e recebimento de mensagens, e interação com o usuário.
2. **ChatServer.java**: Gerencia a lógica do servidor, incluindo a aceitação de novos clientes, transmissão de mensagens e controle de entrada de novos usuários.

## Como Executar

### Pré-requisitos

- JDK instalado (versão 8 ou superior)
- Ambiente de desenvolvimento Java (IDE como IntelliJ, Eclipse, ou uso do terminal)

### Executando o Servidor

1. Compile o código do servidor:

    ```sh
    javac ChatServer.java
    ```

2. Execute o servidor:

    ```sh
    java ChatServer
    ```

### Executando o Cliente

1. Compile o código do cliente:

    ```sh
    javac ChatClient.java
    ```

2. Execute o cliente:

    ```sh
    java ChatClient
    ```

3. Quando solicitado, insira um nome de usuário para participar do chat.

## Estrutura do Código

### ChatClient.java

- **Construtor**: Configura a conexão com o servidor e os fluxos de entrada e saída.
- **Método start()**: Gerencia a entrada do usuário no chat e a interação contínua.
- **Classe Listener**: Executa em uma thread separada para ouvir e exibir mensagens recebidas do servidor.

### ChatServer.java

- **main()**: Inicializa o servidor e aguarda conexões de clientes.
- **Classe ClientHandler**: Gerencia a comunicação com um cliente específico, incluindo aprovação de entrada, transmissão de mensagens e saída.

## Exemplo de Uso

Ao iniciar o servidor, ele aguardará conexões de clientes. Cada cliente, ao se conectar, precisará inserir um nome de usuário. Se houver outros usuários já conectados, eles precisarão aprovar a entrada do novo usuário. As mensagens enviadas por qualquer usuário são transmitidas para todos os demais conectados.

## Melhorias Futuras

- **Mensagens Privadas**: Adicionar suporte para envio de mensagens privadas entre usuários.
- **Lista de Usuários Online**: Exibir uma lista de todos os usuários conectados.
- **Logs de Chat**: Armazenar o histórico de mensagens em um arquivo.
- **Interface Gráfica**: Implementar uma interface gráfica para melhorar a experiência do usuário.

## Licença

Este projeto é distribuído sob a licença MIT. Consulte o arquivo LICENSE para obter mais detalhes.


