package io.datalayer.devops.jclouds;

import static org.jclouds.aws.s3.blobstore.options.AWSS3PutObjectOptions.Builder.withAcl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.jclouds.ContextBuilder;
import org.jclouds.aws.s3.AWSS3Client;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.logging.log4j.config.Log4JLoggingModule;
import org.jclouds.s3.domain.CannedAccessPolicy;
import org.jclouds.s3.domain.S3Object;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

public class S3Main {
    
    public static final String IDENTITY = "";
    public static final String CREDENTIALS = "";
    public static final String BUCKET_NAME = "wherevana";
    public static final String IMAGE_NAME = "black-background.jpg";

    public static void main(String... args) throws IOException {
        System.out.println("Starting...");
//        putWithBlogStore();
        putWithS3Client();
//        get();
        System.out.println("Done...");
    }
        
    private static void putWithBlobStore() throws IOException {

        BlobStoreContext context = ContextBuilder. //
                newBuilder("aws-s3"). //
                credentials(IDENTITY, CREDENTIALS). //
                modules(ImmutableSet.<Module> of(
                        new Log4JLoggingModule())). //
                buildView(BlobStoreContext.class);
        
        BlobStore store = context.getBlobStore();
        
        Blob blob = store.blobBuilder(BUCKET_NAME). //
                name(IMAGE_NAME). //
                payload(new File("src/main/resources/" + IMAGE_NAME)). //
                // or byte[], InputStream...
                contentDisposition("attachment; filename=" + IMAGE_NAME). //
                contentType("image/jpeg"). //
                calculateMD5(). //
                build();
        
        store.putBlob(BUCKET_NAME, blob);
                
        context.close();

    }

    private static void putWithS3Client() throws IOException {

        AWSS3Client s3Client = ContextBuilder. //
                newBuilder("aws-s3"). //
                credentials(IDENTITY, CREDENTIALS). //
                modules(ImmutableSet.<Module> of(
                        new Log4JLoggingModule())). //
                buildApi(AWSS3Client.class);
        S3Object s3Object = s3Client.newS3Object();
        s3Object.setPayload(new File("src/main/resources/" + IMAGE_NAME));
        s3Object.getMetadata().setKey(IMAGE_NAME);
        s3Object.getMetadata().getContentMetadata().setContentType("image/jpeg"); 
        s3Client.putObject(BUCKET_NAME, s3Object, withAcl(CannedAccessPolicy.PUBLIC_READ));
        s3Client.close();

    }

    private static void get() throws IOException {

        BlobStoreContext context = ContextBuilder. //
                newBuilder("aws-s3"). //
                credentials(IDENTITY, CREDENTIALS). //
                buildView(BlobStoreContext.class);

        Map<String, InputStream> mapIs = context.createInputStreamMap(BUCKET_NAME);
        InputStream is = mapIs.get(IMAGE_NAME);
        try {

        } 
        finally {
          if (is != null) {
              is.close();
          }
          context.close();
        }

    }

}
