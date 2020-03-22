/* ***************************************************************
Autor: Aleksander Santos Sousa*
Matricula: 201810825*
Inicio: 23/01/2020*
Ultima alteracao: 29/01/2020*
Nome: Simulador de Redes*
Funcao: Simular o envio de uma mensagem de texto.
*************************************************************** */
package camadas.camada_aplicacao;

import util.Conversao;
import camadas.camada_enlace.CamadaEnlaceDadosTransmissora;
import util.Constantes;
import view.TelaPrincipal;

public class CamadaDeAplicacaoTransmissora {

  /* *****************************************************************************
  Metodo: camadaDeAplicacaoTransmissora*
  Funcao: Tranformar a mensagem em caracteres ASCII e enviar para a camada fisica transmissora*
  Parametros: String mensagem: mensagem a ser enviada*
  Retorno: void*
  ***************************************************************************** */
  public static void camadaDeAplicacaoTransmissora(String mensagem) {
    int[] arrayAscii = new int[mensagem.length()];
    for(int i=0; i<mensagem.length(); i++){
      arrayAscii[i] = mensagem.charAt(i);
    }

    TelaPrincipal.imprimirNaTela(
      Conversao.asciiParaString(arrayAscii, Constantes.ASCII), Constantes.ASCII
    );

    int[] quadro = Conversao.asciiParaBits(arrayAscii);

    CamadaEnlaceDadosTransmissora.camadaEnlaceDadosTransmissora(quadro);
  }
}
