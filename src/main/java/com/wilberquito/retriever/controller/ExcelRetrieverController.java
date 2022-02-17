package com.wilberquito.retriever.controller;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@CrossOrigin("**")
public class ExcelRetrieverController {

    Logger log = LoggerFactory.getLogger(ExcelRetrieverController.class);

    @Value("${excel.location}")
    private String excelLocation;

    @GetMapping("/upload")
    public ResponseEntity<byte[]> uploadExcel(@NonNull @RequestParam("doc") String excelId) throws Exception {

        if (!excelId.contains(".xlsx") && !excelId.contains(".xls")) {
            throw new Exception("Excel name not provieded");
        }

        log.info("Excel location - {} -", this.excelLocation);
        log.info("Requesting file of type - {}", excelId.contains(".xlsx") ? ".xlsx" : ".xls");

        byte[] data = Files.readAllBytes(Paths.get(this.excelLocation + excelId));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type",
                excelId.contains(".xlsx") ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        : "application/vnd.ms-excel");

        return ResponseEntity.ok().headers(headers).body(data);
    }
}
