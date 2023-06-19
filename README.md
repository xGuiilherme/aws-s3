## Serviço para enviar arquivos ao Bucket no AWS S3:
:gear: Tecnologias

- Java 11
- Framework: Spring Boot
- Amazon AWS - S3

## Projeto
Esta aplicação tem como objetivo demonstrar um dos exemplo de implementação para fazer o envio de arquivos ao bucket no S3 da AWS.

Nesta implementação utilizamos alguns dos exemplos abaixo para manipular os arquivos e o bucket que foram criados:
- Criar um nome do Bucket.
- Deletar um Bucket específico.
- Enviar um arquivo para o Bucket que está criado no S3/AWS.
- Listar todos os arquivos que foram enviados.
- Deletar um arquivo específico.

## Exemplos
Na classe **Credenciais** temos duas constantes onde definimos as credencias da AWS para autorizar o acesso:

> Obs: Essa não é a melhor forma de se fazer o acesso, podemos criar alguns metodos específicos caso necessário para obter essas credencias.

```java
public class Credenciais {

    public static final String ACCESS_KEY = "usuario";
    public static final String SECRET_KEY = "senha";
}
```
#
## Classe que faz as manipulações dos Arquivos e Buckets

Nessa classe temos dois **métodos** um para manipular o bucket e outro para manipular o arquivo, logo abaixo no método **manipularBucket** estou instanciando as credenciais de acessos e armazenado os valores dentro da variável **operacoesS3** em seguida estou criando uma variável **nomeBucket** e passando para ela como vai se chamar o meu bucket no S3 nesse exemplo é **"test-backup"**, em seguida crio as operações para criar, listar e deletar os buckets,

```java
public class GerenciaArquivo {

    public static void main(String[] args) {
        manipularBucket();
        manipularArquivo();
    }

    private static void manipularBucket() {
        var operacoesS3 = new OperacoesS3(Credenciais.ACCESS_KEY, Credenciais.SECRET_KEY);
        var nomeBucket = "test-backup";
        operacoesS3.criarBucket(nomeBucket);
        operacoesS3.listarBuckets().forEach(System.out::println);
        operacoesS3.deletarBucket(nomeBucket);
    }
```
#
Dependências necessárias para trabalhar com AmazonAws.

> Obs: Dependência javax.xml.bind: Necessária para quem estiver utilizando a partir da versão 9 do Java:
```java
<dependency>
  <groupId>com.amazonaws</groupId>
  <artifactId>aws-java-sdk</artifactId>
  <version>1.12.239</version>
</dependency>
	
<dependency>
  <groupId>javax.xml.bind</groupId>
  <artifactId>jaxb-api</artifactId>
  <version>2.3.1</version>
</dependency>
```
