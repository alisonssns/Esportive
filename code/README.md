# Projeto Acadêmico - Sistema de Reservas de Espaços Esportivos Públicos

## Descrição
Estes codigos foi desenvolvido principalmente por **Alisson Luis Cordeiro de Arruda** e **Guilherme Kazuya Mizutani** a partir das análises de requisitos e histórias de usuário definidas pela equipe. O objetivo é fornecer uma plataforma digital para facilitar o acesso a espaços esportivos públicos em Pinhais, promovendo a prática de esportes e melhorando a saúde da população.

## Estrutura do Projeto
O projeto foi estruturado utilizando o modelo **MVC (Model-View-Controller)**, separando as responsabilidades em três componentes principais:

### Model (Modelos)
As **Models** são responsáveis por representar e gerenciar os dados da aplicação. Elas fazem a comunicação com o banco de dados, realizando operações de CRUD (Create, Read, Update, Delete), e implementam a lógica de negócio necessária para o funcionamento da aplicação.

### View (Visão)
As **Views** são responsáveis pela apresentação das informações ao usuário. Elas gerenciam a interface gráfica e garantem a interação com os dados processados, exibindo as informações de maneira clara e acessível.

### Controller (Controladores)
Os **Controllers** atuam como intermediários entre as **Models** e as **Views**, gerenciando as requisições do usuário, processando os dados e direcionando as respostas apropriadas às **Views**.

## Pacotes Adicionais
Além da estrutura básica do MVC, o projeto inclui dois pacotes adicionais: **Utils** e **Validations**.

### Utils
O pacote **Utils** contém classes utilitárias que auxiliam na manipulação de dados dentro da aplicação. Algumas das principais classes e suas funções são:
- **AdminUtils**: Contém métodos para manipulação de dados do administrador, como login de administradores e realização de reservas.
- **CRUD**: Implementação genérica das operações de banco de dados (select, insert, update, delete), utilizando `PreparedStatement` para interagir com o banco de dados e executar comandos SQL.

Essas classes utilitárias são essenciais para a manipulação e persistência de dados na aplicação.

### Validations
O pacote **Validations** contém funções específicas para validar entradas do usuário. Exemplos de validações incluem:
- CPF
- E-mail
- Senha

Essas funções garantem que os dados inseridos atendam aos requisitos necessários para a operação correta do sistema, contribuindo para a segurança e integridade dos dados.

