package com.bootcamp18.training.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/v1/test")
public class TestController {
    @GetMapping("/hello")
    public String helloWorld(){
        // arrau list
        // add product 1
        // add product 2
        // add product 3
        // response keluarin product
        return "Hello, world";
    }

    // bikin api GET
    // endpoint: /products
    // bikin sebuah list dengan isi 3 product
    // response: list productnya apa aja

//    bikin api summary
//    String namaToko = "Bootcamp";
//    int totalItem = 5;
//    String statusToko = kalo total item > 3 : buka, selain itu: tutup
//    response: hashmap dengan key value:
//    key: "namaToko" -> value: namaToko
//    key: "totalItem" -> value: totalItem (string)
//    key: "statusToko" -> value: statusToko

}
