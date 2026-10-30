package br.com.alura.TabelaFipe.main;

import br.com.alura.TabelaFipe.model.DadosModelo;
import br.com.alura.TabelaFipe.model.DadosVeiculo;
import br.com.alura.TabelaFipe.model.DadosVeiculoAno;
import br.com.alura.TabelaFipe.model.ModeloWraper;
import br.com.alura.TabelaFipe.service.ConsumoAPI;
import br.com.alura.TabelaFipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();


    private final String ADDRESS = "https://parallelum.com.br/fipe/api/v1";

    public void showMenu() {
        System.out.println("""
                ***** OPÇÕES *****
                1 - Carro
                2 - Moto
                3 - Caminhão
                
                Digite o número das opções para consultar valores:
                """);

        var opcao = scanner.nextInt();
        scanner.nextLine();

        String tipoVeiculo = "";
        switch (opcao) {
            case 1 -> tipoVeiculo = "carros";
            case 2 -> tipoVeiculo = "motos";
            case 3 -> tipoVeiculo = "caminhoes";
            default -> {
                System.out.println("Opção inválida.");
                return;
            }
        }

        var marcas = ADDRESS + "/" + tipoVeiculo + "/marcas";
        var json1 = consumoAPI.obterDados(marcas);

        List<DadosVeiculo> dadosVeiculo = conversor.obterLista(json1, DadosVeiculo.class);

        if (dadosVeiculo == null | dadosVeiculo.isEmpty()) {
            System.out.println("Nenhuma marca encontrada");
            return;
        }

        dadosVeiculo.forEach(System.out::println);

        System.out.println("\nInforme o código da marca para consulta: \n");
        var codigoMarca = scanner.nextLine();

        var marca = marcas + "/" + codigoMarca + "/modelos";
        var json2 = consumoAPI.obterDados(marca);

       ModeloWraper dadosModelo = conversor.obterDados(json2, ModeloWraper.class);

        if (dadosModelo == null | dadosModelo.modelos() == null) {
            System.out.println("Veículo não encontrado.");
            return;
        }

        dadosModelo.modelos().forEach(System.out::println);

        System.out.println("\nDigite um trecho do nome do veículo para consulta: \n");
        var nome = scanner.nextLine();

        List<DadosVeiculo> modelosFiltrados = dadosModelo.modelos().stream()
                .filter(n -> n.descricao().toLowerCase().contains(nome.toLowerCase()))
                .toList();

        modelosFiltrados.forEach(System.out::println);

        System.out.println("\nDigite o código do modelo para consultar valores: ");
        var codigoModelo = scanner.nextLine();

        var modelo = marca + "/" + codigoModelo + "/anos";
        var json3 = consumoAPI.obterDados(modelo);
        List<DadosVeiculo> modeloAnos = conversor.obterLista(json3, DadosVeiculo.class);

        List<DadosVeiculoAno> veiculos = new ArrayList<>();

        for (DadosVeiculo ano : modeloAnos) {
            var veiculoAno = modelo + "/" + ano.codigo();
            var json4 = consumoAPI.obterDados(veiculoAno);
            DadosVeiculoAno veiculo = conversor.obterDados(json4, DadosVeiculoAno.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veículos com os valores por ano:");
        veiculos.forEach(System.out::println);
    }
}
