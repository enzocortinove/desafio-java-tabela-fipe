package br.com.alura.TabelaFipe.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosVeiculo(@JsonAlias("codigo") String codigo,
                         @JsonAlias("nome") String descricao) {

    @Override
    public String toString() {
        return String.format("Cód: %s - Descrição: %s", codigo, descricao);
    }
}
