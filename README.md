# Seguradora de Veículos

Projeto acadêmico da atividade continuada de **Programação Orientada a Objetos**, desenvolvido em Java. Permite cadastrar segurados (pessoas e empresas) e registrar sinistros, verificando a cobertura da apólice e gerando números sequenciais por apólice.

## Conceitos utilizados

- **Encapsulamento:** atributos privados, acessados por métodos públicos.
- **Herança e abstração:** `Segurado` reúne características comuns de pessoas e empresas; classes abstratas definem comportamentos compartilhados.
- **Polimorfismo:** um veículo possui um proprietário do tipo `Segurado`, que pode ser pessoa ou empresa.
- **Interfaces e generics:** `Registro` padroniza a identificação das entidades, e `DAOGenerico<T>` centraliza as operações de cadastro.
- **DAOs e mediators:** os DAOs cuidam da persistência; os mediators concentram as validações e regras de negócio.
- **Singleton:** os mediators disponibilizam uma única instância para uso pela aplicação.
- **Exceções e coleções:** erros de validação são agrupados em listas; sinistros são ordenados com `Comparator` para determinar o próximo sequencial.
- **Serialização:** os objetos são armazenados em arquivos `.dat` pela biblioteca `CadastroObjetos`.

## Como executar

Use **JDK 21 ou superior**, Eclipse, Lombok e `PersistenciaObjetos.jar`. Importe o projeto, ajuste os caminhos das bibliotecas em `.classpath` e execute `br.edu.cs.poo.ac.seguro.telas.TelaPrincipal` para abrir a interface Swing.

Os testes utilizam **JUnit** e ficam no pacote `testes`. Execute-os com um diretório de trabalho separado, pois eles limpam os cadastros do diretório de execução.
