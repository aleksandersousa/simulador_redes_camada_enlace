package camadas.camada_enlace;

import camadas.camada_aplicacao.CamadaDeAplicacaoReceptora;

public class CamadaEnlaceDadosReceptora {
  public static void camadaEnlaceDadosReceptora(int[] quadro) {
    quadro = CamadaEnlaceDadosReceptoraEnquadramento.camadaDeEnlaceDeDadosReceptoraEnquadramento(quadro);
    CamadaDeAplicacaoReceptora.camadaDeAplicacaoReceptora(quadro);
  }
}
