package app.nftguy.nftapi.integration;

import app.nftguy.nftapi.nftstorage.NftStorageService;
import app.nftguy.nftapi.nftstorage.Responses.UploadResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;

@SpringBootTest
public class NftStorageTest {

    @Autowired
    NftStorageService nftStorageService;

    @Test
    void create(){
        Assertions.assertEquals(nftStorageService.getClass(), NftStorageService.class);
    }

    @Test
    void upload() {
  //      Mono<UploadResponse> responseMono = nftStorageService.upload("testdata/mountain.jpg");
  //      Assertions.assertTrue(responseMono.block().getOk());
    }
}
