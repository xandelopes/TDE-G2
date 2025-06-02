// Classe Automovel
public class Automovel {
    private String placa;
    private String modelo;
    private String marca;
    private int ano;
    private double valor;

    public Automovel(String placa, String modelo, String marca, int ano, double valor) {
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.valor = valor;
    }

    public String getPlaca() { return placa; }
    public String getModelo() { return modelo; }
    public String getMarca() { return marca; }
    public int getAno() { return ano; }
    public double getValor() { return valor; }

    public void setModelo(String modelo) { this.modelo = modelo; }
    public void setMarca(String marca) { this.marca = marca; }
    public void setAno(int ano) { this.ano = ano; }
    public void setValor(double valor) { this.valor = valor; }

    @Override
    public String toString() {
        return placa + "," + modelo + "," + marca + "," + ano + "," + valor;
    }

    public static Automovel fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        return new Automovel(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), Double.parseDouble(parts[4]));
    }
}

// Classe principal
import java.io.*;
import java.util.*;

public class CadastroAutomoveis {
    private static ArrayList<Automovel> lista = new ArrayList<>();
    private static final String ARQUIVO = "automoveis.txt";

    public static void main(String[] args) throws IOException {
        carregarDados();
        Scanner sc = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("\n1 - Incluir automóvel");
            System.out.println("2 - Remover automóvel");
            System.out.println("3 - Alterar dados de automóvel");
            System.out.println("4 - Consultar automóvel por placa");
            System.out.println("5 - Listar automóveis (ordenado)");
            System.out.println("6 - Salvar e sair");
            System.out.print("Opção: ");
            opcao = sc.nextInt(); sc.nextLine();

            switch (opcao) {
                case 1 -> incluir(sc);
                case 2 -> remover(sc);
                case 3 -> alterar(sc);
                case 4 -> consultar(sc);
                case 5 -> listarOrdenado(sc);
                case 6 -> salvarDados();
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 6);
    }

    private static void incluir(Scanner sc) {
        System.out.print("Placa: ");
        String placa = sc.nextLine();
        if (buscarPorPlaca(placa) != null) {
            System.out.println("Placa já cadastrada.");
            return;
        }
        System.out.print("Modelo: ");
        String modelo = sc.nextLine();
        System.out.print("Marca: ");
        String marca = sc.nextLine();
        System.out.print("Ano: ");
        int ano = sc.nextInt();
        System.out.print("Valor: ");
        double valor = sc.nextDouble(); sc.nextLine();
        lista.add(new Automovel(placa, modelo, marca, ano, valor));
        System.out.println("Automóvel incluído com sucesso.");
    }

    private static void remover(Scanner sc) {
        System.out.print("Placa: ");
        String placa = sc.nextLine();
        Automovel a = buscarPorPlaca(placa);
        if (a != null) {
            lista.remove(a);
            System.out.println("Removido.");
        } else {
            System.out.println("Placa não encontrada.");
        }
    }

    private static void alterar(Scanner sc) {
        System.out.print("Placa: ");
        String placa = sc.nextLine();
        Automovel a = buscarPorPlaca(placa);
        if (a != null) {
            System.out.print("Novo modelo: ");
            a.setModelo(sc.nextLine());
            System.out.print("Nova marca: ");
            a.setMarca(sc.nextLine());
            System.out.print("Novo ano: ");
            a.setAno(sc.nextInt());
            System.out.print("Novo valor: ");
            a.setValor(sc.nextDouble()); sc.nextLine();
            System.out.println("Dados atualizados.");
        } else {
            System.out.println("Placa não encontrada.");
        }
    }

    private static void consultar(Scanner sc) {
        System.out.print("Placa: ");
        String placa = sc.nextLine();
        Automovel a = buscarPorPlaca(placa);
        if (a != null) System.out.println(a);
        else System.out.println("Não encontrado.");
    }

    private static void listarOrdenado(Scanner sc) {
        System.out.print("Ordenar por (placa/modelo/marca): ");
        String criterio = sc.nextLine();
        Comparator<Automovel> comp = switch (criterio.toLowerCase()) {
            case "placa" -> Comparator.comparing(Automovel::getPlaca);
            case "modelo" -> Comparator.comparing(Automovel::getModelo);
            case "marca" -> Comparator.comparing(Automovel::getMarca);
            default -> null;
        };
        if (comp == null) {
            System.out.println("Critério inválido.");
            return;
        }
        lista.stream().sorted(comp).forEach(System.out::println);
    }

    private static void salvarDados() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO));
        for (Automovel a : lista) {
            bw.write(a.toString());
            bw.newLine();
        }
        bw.close();
        System.out.println("Dados salvos. Saindo...");
    }

    private static void carregarDados() throws IOException {
        File file = new File(ARQUIVO);
        if (!file.exists()) return;
        BufferedReader br = new BufferedReader(new FileReader(file));
        String linha;
        while ((linha = br.readLine()) != null) {
            lista.add(Automovel.fromCSV(linha));
        }
        br.close();
    }

    private static Automovel buscarPorPlaca(String placa) {
        for (Automovel a : lista) {
            if (a.getPlaca().equalsIgnoreCase(placa)) return a;
        }
        return null;
    }
}
