package com.network;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class AudioUDPServer {
    private final byte audioBuffer[] = new byte[10000];
    private TargetDataLine targetDataLine;

    public AudioUDPServer() {
        setupAudio();
        broadcastAudio();
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000F;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    private void broadcastAudio() {
        try {
            DatagramSocket socket = new DatagramSocket(8000);
            InetAddress inetAddress = InetAddress.getByName("127.0.0.1");

            while (true) {
                int count = targetDataLine.read(audioBuffer, 0, audioBuffer.length);
                if (count > 0) {
                    DatagramPacket packet = new DatagramPacket(audioBuffer, audioBuffer.length, inetAddress, 9786);
                    socket.send(packet);
                }
            }

        } catch (Exception ex) {
            System.out.println("Excepton: " + ex.getMessage());
        }
    }

    private void setupAudio() {
        try {
            AudioFormat audioFormat = getAudioFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine =  (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}
