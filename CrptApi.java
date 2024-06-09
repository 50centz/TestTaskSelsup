package ru.selsup.testtask;

import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class CrptApi {

    private final TimeUnit timeUnit;

    private final int requestLimit;

    private int requestCounter = 0;

    private Date timePassed;

    private final RestTemplate restTemplate;


    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        if (requestLimit <= 0) {
            throw new IllegalArgumentException("Request limit cannot be negative");
        }

        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
        this.timePassed = new Date();
        this.restTemplate = new RestTemplate();
    }

    public synchronized void sendDocument(Document document, String signature) throws URISyntaxException {

        checkTime();

        System.out.println(requestCounter); // Это строчка нужна для тестов

       //URI uri = new URI("https://ismp.crpt.ru/api/v3/lk/documents/create");
        URI uri = new URI("http://localhost:8083/api/job/create/test"); // поднял RESTApi для теста счётчика

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Signature", signature);

        HttpEntity<Document> httpEntity = new HttpEntity<>(document, headers);

        ResponseEntity<String> result = restTemplate.postForEntity(uri, httpEntity, String.class);

        requestCounter++;
    }

    private void checkTime()  {
        long currentTime = System.currentTimeMillis();

        if (timeUnit.toMillis(1L) < currentTime - timePassed.getTime()) {
            timePassed = new Date(currentTime);
            requestCounter = 0;
            return;
        }


        if (timeUnit.toMillis(1L) > currentTime - timePassed.getTime() && requestLimit <= requestCounter) {

            try {
                TimeUnit.MILLISECONDS.sleep(timeUnit.toMillis(1L) - (currentTime - timePassed.getTime()));

                timePassed = new Date(System.currentTimeMillis());
                requestCounter = 0;
                System.out.println("\n\nRequest limit exceeded\n\n"); // Это строчка нужна для тестов

            } catch (InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
    }

    @Data
    public static class Description {

        private String participantInn;
    }

    @Data
    public static class Product {

        private String certificate_document;
        private String certificate_document_date;
        private String certificate_document_number;
        private String owner_inn;
        private String producer_inn;
        private String production_date;
        private String tnved_code;
        private String uit_code;
        private String uitu_code;
    }

    @Data
    public static class Document {

        private Description description;
        private String doc_id;
        private String doc_status;
        private String doc_type;
        private boolean importRequest;
        private String owner_inn;
        private String participant_inn;
        private String producer_inn;
        private String production_date;
        private String production_type;
        private List<Product> products;
        private String reg_date;
        private String reg_number;
    }


}
