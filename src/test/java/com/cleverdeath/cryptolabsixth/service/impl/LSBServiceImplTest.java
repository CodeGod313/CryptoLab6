package com.cleverdeath.cryptolabsixth.service.impl;

import com.cleverdeath.cryptolabsixth.service.LSBService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LSBServiceImplTest {

    LSBService lsbService;
    String message = "hello";

    @BeforeAll
    void setUp() {
        lsbService = new LSBServiceImpl();
    }

    @Test
    void processLSBForMp3File() {
        File file = new File("C:\\Proga\\Violetta\\CryptoLab6\\src\\main\\resources\\Billie Eilish, Khalid - Lovely.wav");
        Assertions.assertTrue(lsbService.processLSBForWawFile(file, "C:\\Proga\\Violetta\\CryptoLab6\\src\\main\\resources\\Billie Eilish, Khalid - LovelyModed.wav", message));
    }

    @Test
    void assembleTextFromWawFile() {
        File file = new File("C:\\Proga\\Violetta\\CryptoLab6\\src\\main\\resources\\Billie Eilish, Khalid - LovelyModed.wav");
        String actual = lsbService.assembleTextFromWawFile(file, 5);
        Assertions.assertEquals(message, actual);
    }
}