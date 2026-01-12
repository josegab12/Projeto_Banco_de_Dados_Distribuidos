package br.com.ddb.middleware;

import br.com.ddb.common.Message;

public class BullyElection {
    private DDBNode node;

    public BullyElection(DDBNode node) {
        this.node = node;
    }

    public void startElection() {
        System.out.println("[BULLY] Iniciando eleição...");
        boolean higherNodeOk = false;
        int myId = node.getIdFromIp(node.getMyIp());

        for (String ip : node.getAllNodes()) {
            if (node.getIdFromIp(ip) > myId) {
                if (node.sendMessage(ip, new Message("ELECTION", node.getMyIp(), ""))) {
                    higherNodeOk = true;
                }
            }
        }

        if (!higherNodeOk) {
            node.setCoordinator(node.getMyIp());
            for (String ip : node.getAllNodes()) {
                node.sendMessage(ip, new Message("COORDINATOR", node.getMyIp(), node.getMyIp()));
            }
        }
    }
}