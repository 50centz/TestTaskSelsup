package ru.selsup.testtask;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import ru.selsup.testtask.CrptApi.Document;


@SpringBootTest
class CrptApiTest {


    @DisplayName("CrptApi : Method(sendDocument)")
    @Test
    void shouldHaveSendDocumentWithMethod() throws URISyntaxException {

        Document document = new Document();
        String signature = "signature";

        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 5);

        for (int i = 0; i < 10; i++) {
            crptApi.sendDocument(document, signature);
        }
    }

}