package com.cleverdeath.cryptolabsixth.reader;

import javax.sound.sampled.AudioFormat;
import java.io.File;
import java.util.Optional;

public interface AudioReader {
    Optional<byte[]> readBytesFromWawFile(File file);
    Optional<AudioFormat> readAudioFormat(File file);
}
