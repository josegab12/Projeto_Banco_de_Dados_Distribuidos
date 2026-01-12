package br.com.ddb.common;

import java.io.Serializable;
import java.security.MessageDigest;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public String type; // QUERY, PREPARE, COMMIT, ABORT, HEARTBEAT, ELECTION, COORDINATOR
    public String senderIp;
    public String payload;
    public String checksum;

    public Message(String type, String senderIp, String payload) {
        this.type = type;
        this.senderIp = senderIp;
        this.payload = payload;
        this.checksum = calculateMD5(payload);
    }

    public static String calculateMD5(String input) {
        if (input == null) return "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { return ""; }
    }
}