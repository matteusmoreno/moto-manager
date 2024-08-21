package com.matteusmoreno.moto_manager.client.via_cep;

public record ViaCepResponse(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf) {
}
