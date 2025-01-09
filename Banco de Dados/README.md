# Dumps do Banco de Dados

Esta pasta contém os dumps do banco de dados que representam a estrutura e os dados relacionados aos usuários, reservas, locais e eventos. Os arquivos contidos aqui são úteis para a configuração e teste do banco de dados no sistema.

## Estrutura do Banco de Dados

### Tabelas

- **Usuário**
  - **Atributos**: 
    - Cpf
    - Nome
    - Email
    - Senha
    - Status
    - Endereço (rua, bairro, cidade, estado, cep, número)

 - **telefoneUsuário**
  - **Atributos**: 
    - ID
    - Numero
    - cpfUsuario

- **Reserva**
  - **Atributos**:
    - idReserva
    - cpfUsuario
    - data
    - horario_inicio
    - horario_fim
    - data_registro
    - hora_registro
    - status
    - idLocal

- **Local**
  - **Atributos**:
    - idLocal
    - Nome
    - Tipo
    - Cep
    - Numero
    - tempo_maximo
    - horario_abertura
    - horario_fechamento

- **Evento**
  - **Atributos**:
    - idEvento
    - nome
    - data
    - horario
    - capacidade
    - descricao
    - quantReservados
    - idLocal

- **reservaEvento**
  - **Atributos**:
    - ID
    - cpfUsuario
    - idEvento
    - Status

## Arquivos

Os dumps estão organizados de acordo com a estrutura do banco de dados. Cada arquivo contém instruções SQL para criar as tabelas e inserir os dados de teste necessários para o funcionamento do sistema.

## Observação

Para mais informações, veja o arquivo "DiagramaLogico" na pasta Documentos/Diagramas que mostra o diagrama feito no WORKBENCH;

### Como Usar

**Importar os Dumps**:
   Para importar os dumps para o seu banco de dados, execute os arquivos SQL no seu sistema de gerenciamento de banco de dados (por exemplo, MySQL, PostgreSQL, etc.).


