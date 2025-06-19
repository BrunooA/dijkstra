package src;
public class Aresta {
    private Vertice origem;
    private Vertice destino;
    private int peso;

    public Aresta(Vertice origem, Vertice destino, int peso) {
        this.origem = origem;
        this.destino = destino;
        this.peso = peso;
    }

    public Vertice getOrigem() {
        return origem;
    }

    public Vertice getDestino() {
        return destino;
    }

    public int getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return "(" + origem + " -> " + destino + ", peso: " + peso + ")";
    }
}