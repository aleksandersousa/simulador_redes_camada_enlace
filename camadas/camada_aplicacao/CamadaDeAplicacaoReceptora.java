/* ***************************************************************
Autor: Aleksander Santos Sousa*
Matricula: 201810825*
Inicio: 23/01/2020*
Ultima alteracao: 29/01/2020*
Nome: Simulador de Redes*
Funcao: Simular o envio de uma mensagem de texto.
*************************************************************** */
package camadas.camada_aplicacao;

import camadas.AplicacaoReceptora;
import util.Constantes;
import util.Conversao;
import view.TelaPrincipal;

public class CamadaDeAplicacaoReceptora {

  /* *****************************************************************************
  Metodo: camadaDeAplicacaoReceptora*
  Funcao: Converter o array de numeros ascii em string*
  Parametros: int[] quadro: vetor com os numeros em ASCII*
  Retorno: void*
  ***************************************************************************** */
  public static void camadaDeAplicacaoReceptora(int[] quadro) {
    int[] asciiArray = Conversao.bitsParaAscii(quadro);

    TelaPrincipal.imprimirNaTela(
      Conversao.asciiParaString(asciiArray, Constantes.ASCII_DECODIFICADO),
      Constantes.ASCII_DECODIFICADO
    );

    String mensagem = Conversao.asciiParaMensagem(asciiArray);
    AplicacaoReceptora.aplicacaoReceptora(mensagem);
  }
}
