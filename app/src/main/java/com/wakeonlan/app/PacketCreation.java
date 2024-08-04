package com.wakeonlan.app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PacketCreation {
    public static void createPacket(String mac) throws MacLenghtException, IOException {

        // formatting mac
        mac = mac.replace("-", "");

        if(mac.length() != 12)
            throw new MacLenghtException();

        // converting mac to bytes
        byte[] byteMac = new byte[6];
        for(int i = 0; i < byteMac.length; i++){
            int index = i * 2;
            byteMac[i] = (byte) Integer.parseInt(mac.substring(index, index + 2), 16);
        }

        // creating packet
        byte[] magicPacket = new byte[6 + 16 * 6];

        // first 6 bytes are 0xFF
        for (int i = 0; i < 6; i++) {
            magicPacket[i] = (byte) 0xFF;
        }

        // 16 times the mac address
        for (int i = 0; i < 16; i++) {
            System.arraycopy(byteMac, 0, magicPacket, 6 + i * 6, 6);
        }

        Thread thread = new Thread(() -> {
            try {
                sendPacket(magicPacket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();

    }

    // socket creation and message sending
    private static void sendPacket(byte[] magicPacket) throws IOException {
        InetAddress address = InetAddress.getByName(WoLConstants.broadcastIP);
        DatagramPacket packet = new DatagramPacket(magicPacket, magicPacket.length, address, WoLConstants.port);
        DatagramSocket socket = new DatagramSocket();
        socket.send(packet);
        socket.close();
    }
}
