package com.cleverdeath.cryptolabsixth.service;

import java.io.File;

public interface LSBService {
    boolean processLSBForWawFile(File file, String outputFilePath, String information);
    String assembleTextFromWawFile(File file, int quantityOfSymbols);
}
