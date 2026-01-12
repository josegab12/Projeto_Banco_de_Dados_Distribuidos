package br.com.ddb.middleware;

import br.com.ddb.common.Message;
import java.util.*;

public class Replication {
    private DDBNode node;

    public Replication(DDBNode node) {
        this.node = node;
    }

    public String executeTwoPhaseCommit(String sql) {
        System.out.println("\n[2PC - INÍCIO] Iniciando transação para: " + sql);
        List<String> confirmed = new ArrayList<>();
        List<String> nodes = node.getAllNodes();

        // --- FASE 1: PREPARE ---
        System.out.println("[2PC - FASE 1] Solicitando PREPARE para todos os nós...");

        for (String ip : nodes) {
            System.out.print("[2PC] Verificando nó " + ip + "... ");
            Message resp = node.sendAndReceive(ip, new Message("PREPARE", node.getMyIp(), sql));

            if (resp != null && "READY".equals(resp.payload)) {
                confirmed.add(ip);
                System.out.println("OK (READY)");
            } else {
                System.out.println("FALHOU! (Causa: " + (resp == null ? "Timeout/Conexão" : "Resposta: " + resp.payload) + ")");
            }
        }

        // --- FASE 2: DECISÃO GLOBAL ---
        if (confirmed.size() == nodes.size()) {
            System.out.println("[2PC - SUCESSO] Todos os nós prontos. Enviando COMMIT...");

            for (String ip : nodes) {
                // Envia para os outros via rede
                node.sendMessage(ip, new Message("COMMIT", node.getMyIp(), sql));
            }

            // --- O PULO DO GATO: GRAVAÇÃO LOCAL NO COORDENADOR ---
            System.out.println("[2PC] Executando gravação local no Coordenador...");
            String statusLocal = node.getDatabase().executeLocal(sql);

            return "Transação concluída com sucesso! Status local: " + statusLocal;
        } else {
            // ... lógica de ABORT (permanece igual)
            for (String ip : nodes) {
                node.sendMessage(ip, new Message("ABORT", node.getMyIp(), ""));
            }
            return "Erro: Transação abortada.";
        }
    }
}