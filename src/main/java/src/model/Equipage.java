package src.model;

import java.util.List;
import java.util.ArrayList;

public class Equipage {
    private int numero;
    private List<String> agents;

    public Equipage() {
        this.agents = new ArrayList<>();
    }

    public Equipage(int numero, List<String> agents) {
        this.numero = numero;
        this.agents = agents;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public List<String> getAgents() {
        return agents;
    }

    public void setAgents(List<String> agents) {
        this.agents = agents;
    }

    public void addAgent(String agent) {
        this.agents.add(agent);
    }
}
