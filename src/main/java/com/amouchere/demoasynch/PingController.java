package com.amouchere.demoasynch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
public class PingController {


    @GetMapping(value = "ping")
    public String testChainFuture() {
        log.info("ping");


        CompletableFuture.supplyAsync(this::findReceiver)
                .thenApply(this::buildMsg)
                .thenAccept(this::notify);

        log.info("pong");
        return "pong";
    }

    @GetMapping(value = "pong")
    public String testComposeFuture() {
        log.info("pong");


        CompletableFuture<String> firstFuture =
                CompletableFuture.supplyAsync(this::findByFirstFuture);

        CompletableFuture<String> secondFuture =
                CompletableFuture.supplyAsync(this::findBySecondFuture);

        firstFuture.thenCombine(secondFuture, this::sendMsg);
        log.info("ping");
        return "ping";
    }

    private String sendMsg(String s, String s1) {
        log.info("then {} and then {}", s, s1);
        return s + s1;
    }

    private String findBySecondFuture() {
        sleep(2500);
        return "second future";
    }

    private String findByFirstFuture() {
        sleep(5000);
        return "first future";
    }

    private void notify(String s) {
        sleep(1500);
        log.info("notify {}", s);
        System.out.println(s);
    }


    private String buildMsg(String s) {
        sleep(1500);
        s = "Hello " + s + " !";
        log.info("buildMsg {}", s);
        return s;
    }

    private String findReceiver() {
        sleep(1500);
        String toto = "Toto";
        log.info("findReceiver {}", toto);
        return toto;
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
