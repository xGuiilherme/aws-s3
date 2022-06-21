package awss3.gerenciaArquivo;

import awss3.credenciais.Credenciais;
import awss3.operacoes.OperacoesS3;

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
