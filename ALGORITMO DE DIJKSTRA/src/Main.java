package src;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

public class Main extends JFrame {

    // Mapa de capitais e estruturas do grafo
    private Map<String, Point> posicoesCapitais;
    private Map<String, Vertice> siglaToVertice;
    private Map<Integer, String> idToSigla;
    private AlgoritmoDijkstra grafo;

    // Capitais selecionadas para o cálculo
    private String origemSelecionada = null;
    private String destinoSelecionado = null;

    // Componentes da interface
    private JTextArea resultadoArea;
    private Image mapa;
    private JPanel painelMapa;
    private List<Vertice> caminhoAtual = new ArrayList<>();
    private JLabel posicaoMouseLabel;

    // Controle da animação do avião
    private Timer animacaoTimer;
    private int etapaAnimacao = 0;
    private List<Point> pontosInterpolados = new ArrayList<>();
    private Point posicaoAviao = null;
    private Image aviaoImagem;

    public Main() {
        setTitle("Menor Caminho entre Capitais - Dijkstra");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        inicializarDados(); // Monta o grafo, vértices, arestas e posições

        // Carrega imagens
        ImageIcon iconeOriginal = new ImageIcon("ALGORITMO DE DIJKSTRA\\img\\mapa.jpg");
        mapa = iconeOriginal.getImage().getScaledInstance(630, 727, Image.SCALE_SMOOTH);
        aviaoImagem = new ImageIcon("ALGORITMO DE DIJKSTRA\\img\\aviao.png").getImage();

        // Cria o painel com o mapa e caminho desenhado
        painelMapa = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(mapa, 0, 0, this);

                // Desenha o caminho entre as capitais
                if (caminhoAtual.size() > 1) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(Color.BLUE);
                    g2d.setStroke(new BasicStroke(2f));
                    for (int i = 0; i < caminhoAtual.size() - 1; i++) {
                        String sigla1 = idToSigla.get(caminhoAtual.get(i).getId());
                        String sigla2 = idToSigla.get(caminhoAtual.get(i + 1).getId());
                        Point p1 = posicoesCapitais.get(sigla1);
                        Point p2 = posicoesCapitais.get(sigla2);
                        if (p1 != null && p2 != null) {
                            g2d.drawLine(p1.x + 15, p1.y + 10, p2.x + 15, p2.y + 10);
                        }
                    }
                }

                // Desenha o avião durante a animação
                if (posicaoAviao != null && aviaoImagem != null) {
                    g.drawImage(aviaoImagem, posicaoAviao.x, posicaoAviao.y, 32, 32, null);
                }
            }
        };
        painelMapa.setPreferredSize(new Dimension(630, 727));
        painelMapa.setLayout(null);

        // Cria botões das capitais no mapa
        for (String sigla : posicoesCapitais.keySet()) {
            Point ponto = posicoesCapitais.get(sigla);
            JButton botao = new JButton(sigla);
            botao.setBounds(ponto.x, ponto.y, 30, 20);
            botao.setFont(new Font("Arial", Font.BOLD, 10));
            botao.setForeground(Color.RED);
            botao.setContentAreaFilled(false);
            botao.setBorderPainted(false);
            botao.setFocusPainted(false);
            botao.setMargin(new Insets(0, 0, 0, 0));
            botao.addActionListener(e -> selecionarCapital(sigla, botao));
            painelMapa.add(botao);
        }

        // Resultado do caminho e distância
        resultadoArea = new JTextArea(5, 40);
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        // Mostra coordenadas do mouse sobre o mapa
        posicaoMouseLabel = new JLabel("Posição do mouse: ");
        posicaoMouseLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        add(posicaoMouseLabel, BorderLayout.NORTH);

        painelMapa.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent e) {
                posicaoMouseLabel.setText("Posição do mouse: (" + e.getX() + ", " + e.getY() + ")");
            }
        });

        add(new JScrollPane(painelMapa), BorderLayout.CENTER);
        add(new JScrollPane(resultadoArea), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    // Controla a seleção de origem e destino
    private void selecionarCapital(String sigla, JButton botao) {
        if (origemSelecionada == null) {
            origemSelecionada = sigla;
            botao.setForeground(Color.BLUE);
        } else if (destinoSelecionado == null && !sigla.equals(origemSelecionada)) {
            destinoSelecionado = sigla;
            botao.setForeground(Color.GREEN);
            calcularCaminho(); // Executa o Dijkstra
        } else {
            origemSelecionada = sigla;
            destinoSelecionado = null;
            resetarCores();
            botao.setForeground(Color.BLUE);
            resultadoArea.setText("");
            caminhoAtual.clear();
            posicaoAviao = null;
            painelMapa.repaint();
        }
    }

    private void resetarCores() {
        for (Component b : painelMapa.getComponents()) {
            if (b instanceof JButton) {
                ((JButton) b).setForeground(Color.RED);
            }
        }
    }

    // Executa o algoritmo de Dijkstra e mostra resultado
    private void calcularCaminho() {
        Vertice origem = siglaToVertice.get(origemSelecionada.toLowerCase());
        Vertice destino = siglaToVertice.get(destinoSelecionado.toLowerCase());
        List<Vertice> caminho = grafo.caminhoMinimo(origem.getId(), destino.getId());

        if (caminho == null || caminho.isEmpty()) {
            resultadoArea.setText("❌ Caminho não encontrado.");
            caminhoAtual.clear();
            posicaoAviao = null;
        } else {
            caminhoAtual = caminho;
            StringBuilder sb = new StringBuilder("🧭 Caminho: ");
            int distanciaTotal = 0;

            for (int i = 0; i < caminho.size(); i++) {
                sb.append(idToSigla.get(caminho.get(i).getId()));
                if (i < caminho.size() - 1) {
                    sb.append(" → ");
                    distanciaTotal += grafo.getCusto(caminho.get(i).getId(), caminho.get(i + 1).getId());
                }
            }

            sb.append("\n📏 Distância total: ").append(distanciaTotal).append(" km");
            resultadoArea.setText(sb.toString());
            animarAviao(); // Inicia a animação
        }

        painelMapa.repaint();
    }

    // Move o avião no caminho gerado
    private void animarAviao() {
        if (animacaoTimer != null && animacaoTimer.isRunning()) animacaoTimer.stop();
        pontosInterpolados.clear();

        for (int i = 0; i < caminhoAtual.size() - 1; i++) {
            Point p1 = posicoesCapitais.get(idToSigla.get(caminhoAtual.get(i).getId()));
            Point p2 = posicoesCapitais.get(idToSigla.get(caminhoAtual.get(i + 1).getId()));
            int passos = 20;

            for (int j = 0; j <= passos; j++) {
                int x = (int) (p1.x + (p2.x - p1.x) * (j / (float) passos));
                int y = (int) (p1.y + (p2.y - p1.y) * (j / (float) passos));
                pontosInterpolados.add(new Point(x + 10, y + 10));
            }
        }

        etapaAnimacao = 0;
        animacaoTimer = new Timer(50, e -> {
            if (etapaAnimacao >= pontosInterpolados.size()) {
                animacaoTimer.stop();
                posicaoAviao = null;
            } else {
                posicaoAviao = pontosInterpolados.get(etapaAnimacao);
                etapaAnimacao++;
                painelMapa.repaint();
            }
        });

        animacaoTimer.start();
    }

    // Inicializa capitais, vértices, arestas e coordenadas no mapa
    private void inicializarDados() {
        grafo = new AlgoritmoDijkstra();
        siglaToVertice = new HashMap<>();
        idToSigla = new HashMap<>();
        posicoesCapitais = new HashMap<>();

        List<String> siglas = Arrays.asList("AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
                "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO");

        for (int i = 0; i < siglas.size(); i++) {
            String sigla = siglas.get(i);
            Vertice v = new Vertice(i);
            grafo.adicionarVertice(i);
            siglaToVertice.put(sigla.toLowerCase(), v);
            idToSigla.put(i, sigla);
        }

        Object[][] arestas = {
            { "ac", "ro", 544 }, { "am", "ro", 901 }, { "am", "rr", 785 }, { "am", "mt", 2350 },
            { "am", "pa", 5293 }, { "ap", "pa", 605 }, { "rr", "pa", 1518 }, { "ro", "mt", 1450 },
            { "pa", "to", 1085 }, { "pa", "ma", 806 }, { "ma", "pi", 446 }, { "pi", "ce", 598 },
            { "ce", "rn", 528 }, { "rn", "pb", 185 }, { "pb", "pe", 120 }, { "pe", "al", 258 },
            { "al", "se", 278 }, { "se", "ba", 356 }, { "pi", "pe", 510 }, { "pi", "ba", 947 },
            { "mt", "ms", 694 }, { "mt", "go", 934 }, { "go", "ms", 935 }, { "go", "df", 209 },
            { "go", "ba", 814 }, { "go", "to", 874 }, { "mt", "to", 1300 }, { "mg", "ba", 1372 },
            { "mg", "go", 906 }, { "df", "mg", 740 }, { "mg", "es", 449 }, { "rj", "es", 521 },
            { "ba", "es", 1200 }, { "mg", "rj", 434 }, { "mg", "sp", 586 }, { "rj", "sp", 429 },
            { "sp", "pr", 408 }, { "pr", "sc", 300 }, { "sc", "rs", 476 }, { "ms", "sp", 987 }
        };

        for (Object[] a : arestas) {
            String origem = (String) a[0];
            String destino = (String) a[1];
            int distancia = (int) a[2];
            grafo.criarAresta(siglaToVertice.get(origem).getId(), siglaToVertice.get(destino).getId(), distancia);
        }

        posicoesCapitais.put("AC", new Point(55, 274));
        posicoesCapitais.put("AL", new Point(604, 285));
        posicoesCapitais.put("AP", new Point(349, 79));
        posicoesCapitais.put("AM", new Point(151, 176));
        posicoesCapitais.put("BA", new Point(500, 330));
        posicoesCapitais.put("CE", new Point(539, 196));
        posicoesCapitais.put("DF", new Point(396, 374));
        posicoesCapitais.put("ES", new Point(526, 465));
        posicoesCapitais.put("GO", new Point(371, 410));
        posicoesCapitais.put("MA", new Point(457, 194));
        posicoesCapitais.put("MT", new Point(305, 335));
        posicoesCapitais.put("MS", new Point(305, 475));
        posicoesCapitais.put("MG", new Point(459, 452));
        posicoesCapitais.put("PA", new Point(326, 189));
        posicoesCapitais.put("PB", new Point(599, 238));
        posicoesCapitais.put("PR", new Point(351, 550));
        posicoesCapitais.put("PE", new Point(569, 262));
        posicoesCapitais.put("PI", new Point(483, 254));
        posicoesCapitais.put("RJ", new Point(493, 512));
        posicoesCapitais.put("RN", new Point(590, 218));
        posicoesCapitais.put("RS", new Point(331, 640));
        posicoesCapitais.put("RO", new Point(177, 301));
        posicoesCapitais.put("RR", new Point(199, 62));
        posicoesCapitais.put("SC", new Point(371, 598));
        posicoesCapitais.put("SP", new Point(395, 508));
        posicoesCapitais.put("SE", new Point(579, 306));
        posicoesCapitais.put("TO", new Point(406, 298));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
