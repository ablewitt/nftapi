package app.nftguy.nftapi.nftstorage;

import app.nftguy.nftapi.NftCreateService;
import app.nftguy.nftapi.nftstorage.Responses.UploadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.net.MalformedURLException;

@Service
public class NftStorageService {

    private final WebClient client;


    NftStorageService(Environment environment, WebClient.Builder builder){
    String apiKey = environment.getProperty("nft.storage.api_key");
    String baseUrl = environment.getProperty("nft.storage.url");

    this.client = builder
        .baseUrl(baseUrl)
        .defaultHeader("Authorization", String.format("Bearer %s", apiKey))
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    }

    public Mono<UploadResponse> upload(MultipartFile file) throws MalformedURLException {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file.getResource());
       return client
               .post()
               .uri("/upload")
               .body(BodyInserters.fromMultipartData(builder.build()))
               .retrieve()
               .bodyToMono(UploadResponse.class);
    }

    public Mono<UploadResponse> upload(File file) {
        FileSystemResource resource = new FileSystemResource(file.getAbsolutePath());
        return client
                .post()
                .uri("/upload")
                .body(BodyInserters.fromResource(resource))
                .retrieve()
                .bodyToMono(UploadResponse.class);
    }

}
