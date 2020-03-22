package camadas;

import camadas.camada_aplicacao.CamadaDeAplicacaoTransmissora;

public class AplicacaoTransmissora {

  /* **************************************************************
  Metodo: aplicacaoTransmissora*
  Funcao: chama a camada de aplicacao transmissora.*
  Parametros: nulo*
  Retorno: void*
  *************************************************************** */
  public static void aplicacaoTransmissora(String txtMensagem) {
    CamadaDeAplicacaoTransmissora.camadaDeAplicacaoTransmissora(txtMensagem);
  }
}
