package awss3.gerenciaArquivo;

import awss3.credenciais.Credentials;
import awss3.operacoes.OperationsS3;

public class ManageFile {

    public static void main(String[] args) {
        manipulateBucket();
        manipulateFile();
    }

    private static void manipulateBucket() {
        var operationsS3 = new OperationsS3(Credentials.ACCESS_KEY, Credentials.SECRET_KEY);
        var bucketName = "test-backup";
        operationsS3.createBucket(bucketName);
        operationsS3.listBuckets().forEach(System.out::println);
        operationsS3.deleteBucket(bucketName);
    }

    private static void manipulateFile() {
        var operationsS3 = new OperationsS3(Credentials.ACCESS_KEY, Credentials.SECRET_KEY);
        var bucketName = "test-fotos";
        var sourceFile = "/home/temp/s3/foto.jpg";
        var destinationFile = "fotos/foto.jpg";
        operationsS3.sendFile(bucketName, destinationFile, sourceFile);
        operationsS3.listFile(bucketName).forEach(System.out::println);

        operationsS3.deleteFile(bucketName, destinationFile);
        operationsS3.listFile(bucketName).forEach(System.out::println);
    }
}
