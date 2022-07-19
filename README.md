## Serviço para enviar arquivos para o Bucket no S3 AWS:
:gear: Tecnologias

- Java 11
- Framework: Spring Boot
- Amazon AWS - S3

## Projeto
Está aplicação tem como objetivo demonstrar um dos exemplo de implementação para fazer o envio de arquivos para o bucket no S3 da AWS.

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
Nesse **método** também preciso estar instanciando as credenciais de acesso e em seguida armazeno os valores dentro da variável **operacoesS3**, logo abaixo estou criando uma variável chamada **nomeBucket** e passando para ela como vai se chamar o meu arquivo dentro do bucket nesse exemplo é **"test-fotos"*, em seguida crio uma variável chamada **origemArquivo** onde passo o path/caminho completo de onde está vindo meu arquivo nesse caso do meu PC,em seguida crio uma outra variável chamada **destinoArquivo** precisa passar o path completo do destino que será enviado para o S3 e em seguida crio as operações que vai enviar, listar e deletar os arquivos dentro do bucket.

```java
private static void manipularArquivo() {
        var operacoesS3 = new OperacoesS3(Credenciais.ACCESS_KEY, Credenciais.SECRET_KEY);
        var nomeBucket = "test-fotos";
        var origemArquivo = "/home/temp/s3/foto.jpg";
        var destinoArquivo = "fotos/foto.jpg";
        operacoesS3.enviarArquivo(nomeBucket, destinoArquivo, origemArquivo);
        operacoesS3.listarArquivo(nomeBucket).forEach(System.out::println);

        operacoesS3.deletarArquivo(nomeBucket, destinoArquivo);
        operacoesS3.listarArquivo(nomeBucket).forEach(System.out::println);
    }
}
```
#
Na classe **OperacoesS3** encapsulamos os métodos do **AmazonS3** onde passamos o objeto **clienteS3** para receber esses dados, já no método **OperacoesS3** vamos receber 2 parâmetros dentro do construtor sendo o **accessKey** e **secreteKey** que já foi definido dentro da Classe **Credenciais**, logo abaixo criamos um novo objeto com nome **credenciais** que vai conectar esse acesso na AWS-S3 e logo depois vamos montar o build passando o objeto que foi criado e definimos a região de acesso.

> Obs: withRegion: Estamos definindo qual é a região, mas podemos também parametrizar alguma outra região de preferencia.
``` java
 public class OperacoesS3 {

    private final AmazonS3 clienteS3;

    public OperacoesS3(String accessKey, String secreteKey) {
        var credenciais = new BasicAWSCredentials(accessKey, secreteKey);
        clienteS3 = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credenciais))
                .withRegion(Regions.US_EAST_1)
                .build();
    }
```
#
Nesse **método** vamos criar um bucket especifico no S3, passamos como parâmetro o **nomeBucket**, logo abaixo faço uma verificação para caso o bucket já exista na AWS ele pega o nome do bucket que está tentando criar e me retornar uma mensagem que ele já existe, caso ele não exista no S3 ele chama o **clienteS3** e cria o bucket com o nome desejado.
```java
public void criarBucket(final String nomeBucket) {
        if (clienteS3.doesBucketExistV2(nomeBucket)) {
            System.out.println("Nome do Bucket [" + nomeBucket + "] já foi utilizado.");
            return;
        }
        clienteS3.createBucket(nomeBucket);
    }
```
#
Nesse **método** vamos mapear para cada bucket que existir vamos extrair apenas os nomes deles, para isso utilizamos a **coleção stream** e **expressão lambda** para listar os nomes dos buckets que foram criados no S3, nesse caso quando a gente chama o **clienteS3.listBuckets()** a gente já tem uma lista dos buckets, o que estamos fazendo nessa operação é apenas listar os nomes dos buckets que já existem dentro da **AWS-S3**.

> Obs: Monta uma coleção e retorna uma lista do tipo String contendo apenas os nomes dos buckets.
```java
public List<String> listarBuckets() {
        return clienteS3.listBuckets()
                .stream()
                .map(bucket -> bucket.getName())
                .collect(Collectors.toList());
    }
```
#
Nesse **método** vamos deletar um bucket, recebendo como parâmetros apenas o **nome do bucket**, antes de fazer o delete é importante saber que se tentar deletar um bucket que não existe vamos ter uma exceção do **AmazonS3** para isso não ocorrer vamos fazer uma validação, logo abaixo verifica se o **nomeBucket** existe e caso esse nome não exista ele vai me retornar uma mensagem de aviso **concatenando** com o **nomeBucket** que ele tentou deletar e sai do método sem fazer nada, caso o bucket existir ele chama o **clienteS3** e o método **deleteBucket** passando o nome e ele sera deletado.

> Obs: Nesse caso estamos verificando se essa condição é falsa, então estou passando o operador lógico ! negando essa validação.
```java
public void deletarBucket(final String nomeBucket) {
        if (!clienteS3.doesBucketExistV2(nomeBucket)) {
            System.out.println("O bucket informado não existe [" + nomeBucket + "].");
            return;
        }
        clienteS3.deleteBucket(nomeBucket);
    }
```
#
Nesse **método** vamos receber como parâmetro o **nome do bucket**, **destino do arquivo** e a **origem do arquivo** ou seja o **path/caminho** completo do arquivo que iremos enviar para o S3, logo abaixo também faz a verificação se o **nomeBucket** existe e caso esse nome não exista ele vai me retornar uma mensagem de aviso **concatenando** com o **nomeBucket** que ele tentou enviar, caso ele exista eu chamo o **clienteS3** e o método **putObject** passando como parâmetro o **nome do bucket**, **destino do arquivo** e a **origem do arquivo**.

> Obs: Importante: A origem do arquivo é sempre o path completo.
```java
public void enviarArquivo(String nomeBucket, String destinoArquivo, String origemArquivo) {
        if (!clienteS3.doesBucketExistV2(nomeBucket)) {
            System.out.println("O bucket informado não existe [" + nomeBucket + "].");
        }
        clienteS3.putObject(nomeBucket, destinoArquivo, new File(origemArquivo));
    }
```
#
Nesse **método** vamos mapear os arquivos e para cada sumario vamos pegar apenas o identificador único dele, vamos listar os arquivos recebendo como parâmetro apenas o **nome do bucket**, logo abaixo também faz a verificação se o **nome do bucket** existe e caso esse nome não exista ele vai me retornar uma mensagem de aviso **concatenando** com o **nomeBucket** que ele tentou listar, caso ele exista eu chamo o **clienteS3.listObjects** passando o **nomeBucket** depois de receber a lista eu armazeno esses nomes dentro de uma variável chamada **listObjetos** em seguida eu retorno a lista chamando o método **getObjectSummaries** para isso utilizamos a **coleção stream** e **expressão lambda** para listar os nomes dos arquivos que estão no S3.


> Obs: Monta uma coleção e retorna uma lista do tipo String contendo apenas os identificador único de cada arquivo.
```java
 public List<String> listarArquivo(String nomeBucket) {
         if (!clienteS3.doesBucketExistV2(nomeBucket)) { 
             System.out.println("O bucket informado não existe [" + nomeBucket + "].");
        }
        var listObjetos = clienteS3.listObjects(nomeBucket);
        return listObjetos.getObjectSummaries()
                .stream()
                .map(sumario -> sumario.getKey())
                .collect(Collectors.toList());
    }
```
#
Nesse **método** vamos deletar um arquivo, recebendo como parâmetro o **nome do bucket** e a **chave do arquivo**, sendo a chave do arquivo o nome completo dele, logo abaixo também faz a verificação se o **nomeBucket** existe e caso esse nome não exista ele vai me retornar uma mensagem de aviso **concatenando** com o **nomeBucket** que ele tentou deletar e sai do método sem fazer nada, caso o bucket existir ele chama o **clienteS3** e o método **deleteObject** passando como parâmetro o nome e nome que será deletado.

> Obs: A chave do arquivo é o nome completo dele.
```java
public void deletarArquivo(String nomeBucket, String chaveArquivo) {
         if (!clienteS3.doesBucketExistV2(nomeBucket)) {
            System.out.println("O bucket informado não existe [" + nomeBucket + "].");
            return;
        }
        clienteS3.deleteObject(nomeBucket, chaveArquivo);
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
