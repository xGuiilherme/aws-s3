package awss3.operacoes;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

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

    public void criarBucket(final String nomeBucket) {
        if (clienteS3.doesBucketExistV2(nomeBucket)) {
            System.out.println("Nome do Bucket [" + nomeBucket + "] já foi utilizado.");
            return;
        }
        clienteS3.createBucket(nomeBucket);
    }

    public List<String> listarBuckets() {
        return clienteS3.listBuckets()
                .stream()
                .map(bucket -> bucket.getName())
                .collect(Collectors.toList());
    }

    public void deletarBucket(final String nomeBucket) {
        if (!clienteS3.doesBucketExistV2(nomeBucket)) {
            System.out.println("O bucket informado não existe [" + nomeBucket + "].");
            return;
        }
        clienteS3.deleteBucket(nomeBucket);
    }

    public void enviarArquivo(String nomeBucket, String destinoArquivo, String origemArquivo) {
        if (!clienteS3.doesBucketExistV2(nomeBucket)) {
            System.out.println("O bucket informado não existe [" + nomeBucket + "].");
        }
        clienteS3.putObject(nomeBucket, destinoArquivo, new File(origemArquivo));
    }

    // Retorna apenas o identificador unico de cada arquivo
    public List<String> listarArquivo(String nomeBucket) {
        var listObjetos = clienteS3.listObjects(nomeBucket);
        return listObjetos.getObjectSummaries()
                .stream()
                .map(sumario -> sumario.getKey())
                .collect(Collectors.toList());
    }

    public void deletarArquivo(String nomeBucket, String chaveArquivo) {
        clienteS3.deleteObject(nomeBucket, chaveArquivo);
    }
}
