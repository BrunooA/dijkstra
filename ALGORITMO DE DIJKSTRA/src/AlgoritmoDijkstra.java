package src;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class AlgoritmoDijkstra {

    private Map<Integer, List<Aresta>> listaAdjacencia;
    private Set<Integer> vertices;

    public AlgoritmoDijkstra() {
        this.listaAdjacencia = new HashMap<>();
        this.vertices = new HashSet<>();
    }

    public void adicionarVertice(int id) {
        this.vertices.add(id);
        this.listaAdjacencia.put(id, new ArrayList<>());
    }

    public void criarAresta(int idOrigem, int idDestino, int peso) {
        if (peso < 0) {
            System.out.println("Peso negativo não permitido.");
            return;
        }

        if (vertices.contains(idOrigem) && vertices.contains(idDestino)) {
            Aresta arestaIda = new Aresta(new Vertice(idOrigem), new Vertice(idDestino), peso);
            Aresta arestaVolta = new Aresta(new Vertice(idDestino), new Vertice(idOrigem), peso); // Para grafo não direcionado

            listaAdjacencia.get(idOrigem).add(arestaIda);
            listaAdjacencia.get(idDestino).add(arestaVolta);
        } else {
            System.out.println("Vértice de origem ou destino não encontrado.");
        }
    }

    public List<Vertice> caminhoMinimo(int idOrigem, int idDestino) {
        if (!vertices.contains(idOrigem) || !vertices.contains(idDestino)) {
            System.out.println("Vértice de origem ou destino não encontrado.");
            return null;
        }

        Map<Integer, Integer> distancia = new HashMap<>();
        Map<Integer, Integer> predecessor = new HashMap<>();
        Set<Integer> visitados = new HashSet<>();
        PriorityQueue<Integer> filaPrioridade = new PriorityQueue<>(Comparator.comparingInt(distancia::get));

        for (int id : vertices) {
            distancia.put(id, Integer.MAX_VALUE);
        }
        distancia.put(idOrigem, 0);
        filaPrioridade.add(idOrigem);

        while (!filaPrioridade.isEmpty()) {
            int u = filaPrioridade.poll();
            visitados.add(u);

            if (u == idDestino) {
                break;
            }

            for (Aresta aresta : listaAdjacencia.getOrDefault(u, new ArrayList<>())) {
                int vizinhoId = aresta.getDestino().getId();
                int peso = aresta.getPeso();
                if (!visitados.contains(vizinhoId) && distancia.get(u) + peso < distancia.get(vizinhoId)) {
                    distancia.put(vizinhoId, distancia.get(u) + peso);
                    predecessor.put(vizinhoId, u);
                    filaPrioridade.remove(vizinhoId);
                    filaPrioridade.add(vizinhoId);
                }
            }
        }

        return reconstruirCaminho(predecessor, idDestino);
    }

    private List<Vertice> reconstruirCaminho(Map<Integer, Integer> predecessor, int destinoId) {
        List<Vertice> caminho = new ArrayList<>();
        Integer atualId = destinoId;
        while (atualId != null) {
            caminho.add(new Vertice(atualId));
            atualId = predecessor.get(atualId);
        }
        Collections.reverse(caminho);
        return caminho;
    }

    public int getCusto(int idOrigem, int idDestino) {
        if (listaAdjacencia.containsKey(idOrigem)) {
            for (Aresta aresta : listaAdjacencia.get(idOrigem)) {
                if (aresta.getDestino().getId() == idDestino) {
                    return aresta.getPeso();
                }
            }
        }
        return Integer.MAX_VALUE; // Indica que não há aresta direta
    }

    public static String normalizar(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'normalizar'");
    }
}