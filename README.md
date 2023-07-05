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

    public static final String ACCESS_KEY = "usuario_aqui";
    public static final String SECRET_KEY = "senha_aqui";
}
```
