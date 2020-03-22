/* ***************************************************************
Autor: Aleksander Santos Sousa*
Matricula: 201810825*
Inicio: 23/01/2020*
Ultima alteracao: 01/02/2020*
Nome: Simulador de Redes*
Funcao: Simular o envio de uma mensagem de texto.
*************************************************************** */
package camadas.camada_fisica;

import view.PainelEsquerdo;
import view.TelaPrincipal;
import util.Constantes;
import util.Conversao;
import util.MeioDeComunicacao;

public class CamadaFisicaTransmissora {
  /* *****************************************************************************
  Metodo: camadaFisicaTransmissora*
  Funcao: Enviar os bits da mensagem para o meio de comunicacao, escolhendo qual a
          codificao utilizada*
  Parametros: int[] quadro: vetor de inteiros com os bits*
  Retorno: void*
  ***************************************************************************** */
  public static void camadaFisicaTransmissora(int[] quadro) {
    int tipoDeCodificacao = PainelEsquerdo.cmbListaDeCodificacao.getSelectedIndex();
    int[] fluxoBrutoDeBits = null;

    if(checaViolacao(quadro)){
      if(tipoDeCodificacao == Constantes.CODIFICACAO_BINARIA){
        tipoDeCodificacao = Constantes.CODIFICACAO_MANCHESTER;
        CamadaFisicaReceptora.setTipoDeCodificacao(tipoDeCodificacao);
      }
    }

    switch(tipoDeCodificacao) {
      case Constantes.CODIFICACAO_BINARIA:
        TelaPrincipal.imprimirNaTela(
          Conversao.bitsParaString(quadro), Constantes.BIT_BRUTO
        );

        fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoBinaria(quadro);
        break;
      case Constantes.CODIFICACAO_MANCHESTER:
        TelaPrincipal.imprimirNaTela(
          Conversao.bitsParaString(quadro), Constantes.BIT_BRUTO
        );

        fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoManchester(quadro);
        break;
      case Constantes.CODIFICACAO_MANCHESTER_DIFERENCIAL:
        TelaPrincipal.imprimirNaTela(
          Conversao.bitsParaString(quadro), Constantes.BIT_BRUTO
        );

        fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoManchesterDiferencial(quadro);
        break;
    }

    MeioDeComunicacao.meioDeComunicacao(fluxoBrutoDeBits);
  }

  /* **************************************************************
  Metodo: camadaFisicaTransmissoraCodificacaoBinaria*
  Funcao: Transformar os elementos do vetor quadro em bits e colocar
          os bits na codificacao binaria*
  Parametros: int[] quadro: vetor de inteiros com os bits*
  Retorno: int[] bitsCodificados*
  *************************************************************** */
  private static int[] camadaFisicaTransmissoraCodificacaoBinaria(int[] quadro) {
    TelaPrincipal.imprimirNaTela(
      Conversao.bitsParaString(quadro), Constantes.BIT_CODIFICADO
    );

    return quadro;
  }

  /* **************************************************************
  Metodo: camadaFisicaTransmissoraCodificacaoManchester*
  Funcao: Transformar os elementos do vetor quadro em bits e colocar
          os bits na codificacao manchester*
  Parametros: int[] quadro: vetor de inteiros com os bits*
  Retorno: int[] bitsCodificados*
  *************************************************************** */
  private static int[] camadaFisicaTransmissoraCodificacaoManchester(int[] quadro) {
    int novoTamanho = 0;
    if(32 - Integer.numberOfLeadingZeros(quadro[quadro.length - 1]) <= 16){
      novoTamanho = quadro.length*2 - 1;
    }else{
      novoTamanho = quadro.length*2;
    }

    int[] bitsCodificados = new int[novoTamanho];
    int displayMask = 1 << 31;
    int valor = 0;

    for(int i=0, pos=0; i<quadro.length; i++){
      int numero = quadro[i];

      int numeroDeBits = 32 - Integer.numberOfLeadingZeros(numero);
      numeroDeBits = Conversao.arredondaBits(numeroDeBits);

      numero <<= 32-numeroDeBits;

      for(int j=1; j<=numeroDeBits; j++){
        if((numero & displayMask) == 0){
          valor <<= 1;
          valor = valor | 0;
          valor <<= 1;
          valor = valor | 1;
        }else{
          valor <<= 1;
          valor = valor | 1;
          valor <<= 1;
          valor = valor | 0;
        }
        numero <<= 1;

        if(j == 16){
          bitsCodificados[pos] = valor;
          valor = 0;
          pos++;
        }else if(j == numeroDeBits){
          bitsCodificados[pos] = valor;
          valor = 0;
          pos++;
        }
      }
    }

    TelaPrincipal.imprimirNaTela(
      Conversao.bitsParaString(bitsCodificados), Constantes.BIT_CODIFICADO
    );

    return bitsCodificados;
  }

  /* **************************************************************
  Metodo: camadaFisicaTransmissoraCodificacaoManchesterDiferencial*
  Funcao: Transformar os elementos do vetor quadro em bits e colocar
          os bits na codificacao manchester difenrencial*
  Parametros: int[] quadro: vetor de inteiros com os bits*
  Retorno: int[] bitsCodificados*
  *************************************************************** */
  private static int[] camadaFisicaTransmissoraCodificacaoManchesterDiferencial(int[] quadro) {
    int novoTamanho = 0;
    if(32 - Integer.numberOfLeadingZeros(quadro[quadro.length-1]) <= 16){
      novoTamanho = (quadro.length*2)-1;
    }else{
      novoTamanho = quadro.length*2;
    }

    int[] bitsCodificados = new int[novoTamanho];
    int displayMask = 1 << 31;
    int valor = 0;

    boolean transicao = false;

    for(int i=0, pos=0; i<quadro.length; i++){
      int numero = quadro[i];

      int numeroDeBits = 32 - Integer.numberOfLeadingZeros(numero);
      numeroDeBits = Conversao.arredondaBits(numeroDeBits);

      numero <<= 32-numeroDeBits;

      for(int j=1; j<=numeroDeBits; j++){
        if((numero & displayMask) == 0){
          if(transicao){
            valor <<= 1;
            valor = valor | 1;
            valor <<= 1;
            valor = valor | 0;
          }else{
            valor <<= 1;
            valor = valor | 0;
            valor <<= 1;
            valor = valor | 1;
          }
        }else{
          transicao = !transicao; //houve transicao

          if(transicao){
            valor <<= 1;
            valor = valor | 1;
            valor <<= 1;
            valor = valor | 0;
          }else{
            valor <<= 1;
            valor = valor | 0;
            valor <<= 1;
            valor = valor | 1;
          }
        }
        numero <<= 1;

        if(j == 16){
          bitsCodificados[pos] = valor;
          valor = 0;
          pos++;
        }else if(j == numeroDeBits){
          bitsCodificados[pos] = valor;
          valor = 0;
          pos++;
        }
      }
    }

    TelaPrincipal.imprimirNaTela(
      Conversao.bitsParaString(bitsCodificados), Constantes.BIT_CODIFICADO
    );

    return bitsCodificados;
  }

  /* **************************************************************
  Metodo: checaViolacao*
  Funcao: checa se a camada fisica foi violada*
  Parametros: int[] fluxoBrutoDeBits: vetor de inteiros com os bits*
  Retorno: boolean*
  *************************************************************** */
  private static boolean checaViolacao(int[] fluxoBrutoDeBits) {
    final int displayMask = 1 << 31;
    final int byteFlag = 255;
    int valor = 0;

    for(int i=0; i<fluxoBrutoDeBits.length; i++){
      int numero = fluxoBrutoDeBits[i];
      int numeroDeBits = 32 - Integer.numberOfLeadingZeros(numero);
      numeroDeBits = Conversao.arredondaBits(numeroDeBits);

      numero <<= 32 - numeroDeBits;

      for(int j=1; j<=numeroDeBits; j++){
        int bit = (numero & displayMask) == 0 ? 0 : 1;
        valor <<= 1;
        valor |= bit;
        numero <<= 1;

        if(j%8 == 0){
          if(valor == byteFlag){
            return true;
          }
        }
      }
    }

    return false;
  }
}
