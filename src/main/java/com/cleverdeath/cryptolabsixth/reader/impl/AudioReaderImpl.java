package com.cleverdeath.cryptolabsixth.reader.impl;

import com.cleverdeath.cryptolabsixth.reader.AudioReader;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Optional;

public class AudioReaderImpl implements AudioReader {
    @Override
    public Optional<byte[]> readBytesFromWawFile(File file) {
        try {
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, baout);
            audioInputStream.close();
            baout.close();
            byte[] data = baout.toByteArray();
            return Optional.of(data);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AudioFormat> readAudioFormat(File file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            return Optional.of(audioInputStream.getFormat());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
