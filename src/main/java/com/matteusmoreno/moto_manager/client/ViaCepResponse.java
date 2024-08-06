package com.matteusmoreno.moto_manager.client;

public record ViaCepResponse(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf) {
}
