package br.com.alura.TabelaFipe.service;

import java.util.List;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe); // T é genérico, eu não sei qual entidade será devolvida

    <T> List<T> obterLista(String json, Class<T> classe);
}
