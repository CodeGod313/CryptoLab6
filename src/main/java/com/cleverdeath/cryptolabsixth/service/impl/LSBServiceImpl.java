package com.cleverdeath.cryptolabsixth.service.impl;

import com.cleverdeath.cryptolabsixth.reader.AudioReader;
import com.cleverdeath.cryptolabsixth.reader.impl.AudioReaderImpl;
import com.cleverdeath.cryptolabsixth.service.LSBService;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class LSBServiceImpl implements LSBService {

    public static final char ZERO = '0';
    public static final String EMPTY_STRING = "";

    @Override
    public boolean processLSBForWawFile(File file, String outputFilePath, String information) {
        AudioReader audioReader = new AudioReaderImpl();
        byte[] bytesFromAudio = audioReader.readBytesFromWawFile(file).get();
        int currentByteOfAudio = 44;
        int remainingBits = 3;
        for (int i = 0; i < information.length(); i++) {
            String binaryChar = Integer.toBinaryString(information.charAt(i));
            binaryChar = addLeadZerosTo16Symbols(binaryChar);
            for (int j = binaryChar.length() - 1; j >= 0; j--) {
                if(remainingBits == 0){
                    currentByteOfAudio+=1;
                    remainingBits = 3;
                }
                if (binaryChar.charAt(j) == '1') {
                    bytesFromAudio[currentByteOfAudio] = (byte) (bytesFromAudio[currentByteOfAudio] | (1 << (3 - remainingBits)));
                } else {
                    bytesFromAudio[currentByteOfAudio] = (byte) (bytesFromAudio[currentByteOfAudio] & ~(1 << (3 - remainingBits)));
                }
                if (remainingBits > 0) {
                    remainingBits--;
                }
            }
        }
        byte[] bytesWithoutHeader = new byte[bytesFromAudio.length - 44];
        for (int i = 0; i < bytesWithoutHeader.length; i++) {
            bytesWithoutHeader[i] = bytesFromAudio[i + 44];
        }
        AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(bytesWithoutHeader), audioReader.readAudioFormat(file).get(), bytesWithoutHeader.length);
        try {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(outputFilePath));
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    private String addLeadZerosTo16Symbols(String string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        while (stringBuilder.length() < 16) {
            stringBuilder.insert(0, ZERO);
        }
        return stringBuilder.toString();
    }


    @Override
    public String assembleTextFromWawFile(File file, int quantityOfSymbols) {
        AudioReader audioReader = new AudioReaderImpl();
        byte[] bytesFromAudio = audioReader.readBytesFromWawFile(file).get();
        int currentByteOfAudio = 44;
        int remainingBits = 3;
        int bitsToChange = 16;
        StringBuilder characterBinaryString = null;
        StringBuilder decryptedMessage = new StringBuilder();
        while (quantityOfSymbols > 0) {
            if (bitsToChange == 0) {
                decryptedMessage.append((char) Integer.parseInt(characterBinaryString.toString(), 2));
                quantityOfSymbols--;
                bitsToChange = 16;
            }
            if (bitsToChange == 16) {
                characterBinaryString = new StringBuilder("0000000000000000");
            }
            if (remainingBits == 0) {
                remainingBits = 3;
                currentByteOfAudio += 1;
            }
            switch (remainingBits) {
                case 3 -> {
                    byte firstBitOne = (byte) (bytesFromAudio[currentByteOfAudio] | 1);
                    changeBitInCharacterBinaryString(
                            characterBinaryString,
                            bitsToChange - 1,
                            firstBitOne == bytesFromAudio[currentByteOfAudio]);
                }
                case 2 -> {
                    byte secondBitOne = (byte) (bytesFromAudio[currentByteOfAudio] | 1 << 1);
                    changeBitInCharacterBinaryString(
                            characterBinaryString,
                            bitsToChange - 1,
                            secondBitOne == bytesFromAudio[currentByteOfAudio]);
                }
                case 1 -> {
                    byte thirdBitOne = (byte) (bytesFromAudio[currentByteOfAudio] | 1 << 2);
                    changeBitInCharacterBinaryString(
                            characterBinaryString,
                            bitsToChange - 1,
                            thirdBitOne == bytesFromAudio[currentByteOfAudio]);
                }
            }
            remainingBits--;
            bitsToChange--;
        }
        return decryptedMessage.toString();
    }

    private void changeBitInCharacterBinaryString(StringBuilder characterBinaryString, int position, boolean value) {
        if (value) {
            characterBinaryString.replace(position, position + 1, "1");
        } else {
            characterBinaryString.replace(position, position + 1, "0");
        }
    }
}
