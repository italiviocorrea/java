package eti.italivio.correa.api.mdfes.hadoop.exceptions;

public class PlacaNotFoundException extends RuntimeException {
    public PlacaNotFoundException() {
        super("Não foi encontrada registro para a placa informada");
    }
}
