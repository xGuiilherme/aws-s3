package awss3.operacoes;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class OperationsS3 {

    private final AmazonS3 clienteS3;

    public OperationsS3(String accessKey, String secreteKey) {
        var credentials = new BasicAWSCredentials(accessKey, secreteKey);
        clienteS3 = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    public void createBucket(final String nomeBucket) {
        if (clienteS3.doesBucketExistV2(nomeBucket)) {
            System.out.println("Nome do Bucket [" + nomeBucket + "] já foi utilizado.");
            return;
        }
        clienteS3.createBucket(nomeBucket);
    }

    public List<String> listBuckets() {
        return clienteS3.listBuckets()
                .stream()
                .map(bucket -> bucket.getName())
                .collect(Collectors.toList());
    }

    public void deleteBucket(final String nomeBucket) {
        if (!clienteS3.doesBucketExistV2(nomeBucket)) {
            System.out.println("O bucket informado não existe [" + nomeBucket + "].");
            return;
        }
        clienteS3.deleteBucket(nomeBucket);
    }

    public void sendFile(String nomeBucket, String destinoArquivo, String origemArquivo) {
        if (!clienteS3.doesBucketExistV2(nomeBucket)) {
            System.out.println("O bucket informado não existe [" + nomeBucket + "].");
        }
        clienteS3.putObject(nomeBucket, destinoArquivo, new File(origemArquivo));
    }

    // Retorna apenas o identificador unico de cada arquivo
    public List<String> listFile(String nomeBucket) {
        var listObjects = clienteS3.listObjects(nomeBucket);
        return listObjects.getObjectSummaries()
                .stream()
                .map(sumario -> sumario.getKey())
                .collect(Collectors.toList());
    }

    public void deleteFile(String nomeBucket, String chaveArquivo) {
        clienteS3.deleteObject(nomeBucket, chaveArquivo);
    }
}
