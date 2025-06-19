package src;
import java.util.*;

public class Main {

    public void run() {
        Scanner scanner = new Scanner(System.in); 

        List<String> nomesCapitais = Arrays.asList(
                "Rio Branco", "Maceió", "Macapá", "Manaus", "Salvador", "Fortaleza", "Brasília", "Vitória",
                "Goiânia", "São Luís", "Cuiabá", "Campo Grande", "Belo Horizonte", "Belém", "João Pessoa",
                "Curitiba", "Recife", "Teresina", "Rio de Janeiro", "Natal", "Porto Alegre", "Porto Velho",
                "Boa Vista", "Florianópolis", "São Paulo", "Aracaju", "Palmas"
        );

        List<String> siglasCapitais = Arrays.asList(
                "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB",
                "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        );

        // Criar o grafo e mapas de siglas para vértices
        AlgoritmoDijkstra grafo = new AlgoritmoDijkstra();
        Map<String, Vertice> siglaToVertice = new HashMap<>();
        Map<Integer, String> idToSigla = new HashMap<>();

        System.out.println("--- Capitais e suas siglas ---");
        for (int i = 0; i < siglasCapitais.size(); i++) {
            String sigla = siglasCapitais.get(i).toLowerCase();
            Vertice v = new Vertice(i);
            grafo.adicionarVertice(i);
            siglaToVertice.put(sigla, v);
            idToSigla.put(i, siglasCapitais.get(i));
            System.out.printf("%-20s (%s)\n", nomesCapitais.get(i), siglasCapitais.get(i));
        }
        System.out.println("-------------------------------\n");

        // Criar as conexões entre os vértices
        grafo.criarAresta(siglaToVertice.get("ac").getId(), siglaToVertice.get("ro").getId(), 544);
        grafo.criarAresta(siglaToVertice.get("am").getId(), siglaToVertice.get("ro").getId(), 901); 
        grafo.criarAresta(siglaToVertice.get("am").getId(), siglaToVertice.get("rr").getId(), 785); 
        grafo.criarAresta(siglaToVertice.get("am").getId(), siglaToVertice.get("mt").getId(), 2350); 
        grafo.criarAresta(siglaToVertice.get("am").getId(), siglaToVertice.get("pa").getId(), 5293); 
        grafo.criarAresta(siglaToVertice.get("ap").getId(), siglaToVertice.get("pa").getId(), 605); 
        grafo.criarAresta(siglaToVertice.get("rr").getId(), siglaToVertice.get("pa").getId(), 1518); 
        grafo.criarAresta(siglaToVertice.get("ro").getId(), siglaToVertice.get("mt").getId(), 1450); 
        grafo.criarAresta(siglaToVertice.get("pa").getId(), siglaToVertice.get("to").getId(), 1085); 
        grafo.criarAresta(siglaToVertice.get("pa").getId(), siglaToVertice.get("ma").getId(), 806); 
        grafo.criarAresta(siglaToVertice.get("ma").getId(), siglaToVertice.get("pi").getId(), 446); 
        grafo.criarAresta(siglaToVertice.get("pi").getId(), siglaToVertice.get("ce").getId(), 598); 
        grafo.criarAresta(siglaToVertice.get("ce").getId(), siglaToVertice.get("rn").getId(), 528); 
        grafo.criarAresta(siglaToVertice.get("rn").getId(), siglaToVertice.get("pb").getId(), 185);
        grafo.criarAresta(siglaToVertice.get("pb").getId(), siglaToVertice.get("pe").getId(), 120);
        grafo.criarAresta(siglaToVertice.get("pe").getId(), siglaToVertice.get("al").getId(), 258);
        grafo.criarAresta(siglaToVertice.get("al").getId(), siglaToVertice.get("se").getId(), 278);
        grafo.criarAresta(siglaToVertice.get("se").getId(), siglaToVertice.get("ba").getId(), 356);
        grafo.criarAresta(siglaToVertice.get("pi").getId(), siglaToVertice.get("pe").getId(), 510);
        grafo.criarAresta(siglaToVertice.get("pi").getId(), siglaToVertice.get("ba").getId(), 947);
        grafo.criarAresta(siglaToVertice.get("mt").getId(), siglaToVertice.get("ms").getId(), 694);
        grafo.criarAresta(siglaToVertice.get("mt").getId(), siglaToVertice.get("go").getId(), 934); 
        grafo.criarAresta(siglaToVertice.get("go").getId(), siglaToVertice.get("ms").getId(), 935); 
        grafo.criarAresta(siglaToVertice.get("go").getId(), siglaToVertice.get("df").getId(), 209); 
        grafo.criarAresta(siglaToVertice.get("go").getId(), siglaToVertice.get("ba").getId(), 814); 
        grafo.criarAresta(siglaToVertice.get("go").getId(), siglaToVertice.get("to").getId(), 874); 
        grafo.criarAresta(siglaToVertice.get("mt").getId(), siglaToVertice.get("to").getId(), 1300); 
        grafo.criarAresta(siglaToVertice.get("mg").getId(), siglaToVertice.get("ba").getId(), 1372); 
        grafo.criarAresta(siglaToVertice.get("mg").getId(), siglaToVertice.get("go").getId(), 906);
        grafo.criarAresta(siglaToVertice.get("df").getId(), siglaToVertice.get("mg").getId(), 740); 
        grafo.criarAresta(siglaToVertice.get("mg").getId(), siglaToVertice.get("es").getId(), 524); 
        grafo.criarAresta(siglaToVertice.get("rj").getId(), siglaToVertice.get("es").getId(), 521); 
        grafo.criarAresta(siglaToVertice.get("ba").getId(), siglaToVertice.get("es").getId(),  1200); 
        grafo.criarAresta(siglaToVertice.get("mg").getId(), siglaToVertice.get("rj").getId(), 434); 
        grafo.criarAresta(siglaToVertice.get("mg").getId(), siglaToVertice.get("sp").getId(), 586);
        grafo.criarAresta(siglaToVertice.get("rj").getId(), siglaToVertice.get("sp").getId(), 429); 
        grafo.criarAresta(siglaToVertice.get("sp").getId(), siglaToVertice.get("pr").getId(), 408); 
        grafo.criarAresta(siglaToVertice.get("pr").getId(), siglaToVertice.get("sc").getId(), 300); 
        grafo.criarAresta(siglaToVertice.get("sc").getId(), siglaToVertice.get("rs").getId(), 476);
        grafo.criarAresta(siglaToVertice.get("ms").getId(), siglaToVertice.get("sp").getId(), 987);

        // Entrada do usuário
        System.out.println("Digite a sigla da capital de origem (ex: DF):");
        String origemInput = scanner.nextLine().trim().toLowerCase();

        System.out.println("Digite a sigla da capital de destino (ex: SP):");
        String destinoInput = scanner.nextLine().trim().toLowerCase();

        if (!siglaToVertice.containsKey(origemInput) || !siglaToVertice.containsKey(destinoInput)) {
            System.out.println("Sigla inválida. Tente novamente.");
            return;
        }

        Vertice origem = siglaToVertice.get(origemInput);
        Vertice destino = siglaToVertice.get(destinoInput);
        List<Vertice> caminho = grafo.caminhoMinimo(origem.getId(), destino.getId());

        if (caminho == null || caminho.isEmpty()) {
            System.out.println("Não há caminho disponível entre as capitais.");
        } else {
            System.out.print("Caminho: ");
            int distanciaTotal = 0;

            for (int i = 0; i < caminho.size(); i++) {
                System.out.print(idToSigla.get(caminho.get(i).getId()));
                if (i < caminho.size() - 1) {
                    System.out.print(" -> ");
                    distanciaTotal += grafo.getCusto(caminho.get(i).getId(), caminho.get(i + 1).getId());
                }
            }
            System.out.println("\nDistância total: " + distanciaTotal + " km");
        }

        scanner.close();
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
