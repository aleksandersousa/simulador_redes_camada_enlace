package camadas.camada_enlace;

import camadas.camada_fisica.CamadaFisicaTransmissora;

public class CamadaEnlaceDadosTransmissora {
  public static void camadaEnlaceDadosTransmissora(int[] quadro) {
    quadro = CamadaEnlaceDadosTransmissoraEnquadramento.camadaDeEnlaceDeDadosTransmissoraEnquadramento(quadro);
    CamadaFisicaTransmissora.camadaFisicaTransmissora(quadro);
  }
}
