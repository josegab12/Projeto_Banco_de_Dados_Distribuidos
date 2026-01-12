package br.com.ddb.common;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private List<String> allNodes;
    private String dbUser;
    private String dbPass;
    private int heartbeatInterval;

    public Config(String filePath) {
        this.allNodes = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath)) {
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
            
            // Carrega os IPs dos nós
            JSONArray nodesArray = jsonObject.getJSONArray("nodes");
            for (int i = 0; i < nodesArray.length(); i++) {
                allNodes.add(nodesArray.getJSONObject(i).getString("ip"));
            }

            // Carrega credenciais e intervalos
            this.dbUser = jsonObject.getString("db_user");
            this.dbPass = jsonObject.getString("db_pass");
            this.heartbeatInterval = jsonObject.getInt("heartbeat_interval");

        } catch (Exception e) {
            System.err.println("Erro ao carregar nodes.json: " + e.getMessage());
            // Fallback para valores padrão caso o arquivo falhe
            this.dbUser = "root";
            this.dbPass = "root";
        }
    }

    public List<String> getAllNodes() { return allNodes; }
    public String getDbUser() { return dbUser; }
    public String getDbPass() { return dbPass; }
    public int getHeartbeatInterval() { return heartbeatInterval; }
}