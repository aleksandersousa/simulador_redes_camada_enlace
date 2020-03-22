/* ***************************************************************
Autor: Aleksander Santos Sousa*
Matricula: 201810825*
Inicio: 23/01/2020*
Ultima alteracao: 29/01/2020*
Nome: Simulador de Redes*
Funcao: Simular o envio de uma mensagem de texto.
*************************************************************** */
package camadas.camada_fisica;

import view.PainelEsquerdo;
import view.TelaPrincipal;

import camadas.camada_enlace.CamadaEnlaceDadosReceptora;

import util.Constantes;
import util.Conversao;

public class CamadaFisicaReceptora {
  private static int[] fluxoBrutoDeBits;
  private static int tipoDeDecodificacao;
  private static boolean estaViolada = false;

  /* *****************************************************************************
  Metodo: camadaFisicaReceptora*
  Funcao: Enviar o vetor com os bits decodificados para a camada de aplicacao
          receptora*
  Parametros: int[] fluxoBrutoDeBitsPontoB: vetor com os bits recebidos*
  Retorno: void*
  ***************************************************************************** */
  public static void camadaFisicaReceptora(int[] fluxoBrutoDeBitsPontoB) {
    if(estaViolada){
      estaViolada = false;
    }else{
      tipoDeDecodificacao = PainelEsquerdo.cmbListaDeCodificacao.getSelectedIndex();
    }

    switch(tipoDeDecodificacao) {
      case Constantes.CODIFICACAO_BINARIA:
        fluxoBrutoDeBits =
        camadaFisicaReceptoraDecodificacaoBinaria(fluxoBrutoDeBitsPontoB);
        break;
      case Constantes.CODIFICACAO_MANCHESTER:
        fluxoBrutoDeBits =
        camadaFisicaReceptoraDecodificacaoManchester(fluxoBrutoDeBitsPontoB);
        break;
      case Constantes.CODIFICACAO_MANCHESTER_DIFERENCIAL:
        fluxoBrutoDeBits =
        camadaFisicaReceptoraDecodificacaoManchesterDiferencial(fluxoBrutoDeBitsPontoB);
        break;
    }

    CamadaEnlaceDadosReceptora.camadaEnlaceDadosReceptora(fluxoBrutoDeBits);
  }

  /* **************************************************************
  Metodo: camadaFisicaReceptoraDecodificacaoBinaria*
  Funcao: Decodificar os bits da codificacao binaria*
  Parametros: int[] fluxoBrutoDeBits: bits a serem decodificados*
  Retorno: int[] bitsDecodificados*
  *************************************************************** */
  private static int[] camadaFisicaReceptoraDecodificacaoBinaria(int[] fluxoBrutoDeBits) {
    TelaPrincipal.imprimirNaTela(
      Conversao.bitsParaString(fluxoBrutoDeBits), Constantes.BIT_RECEBIDO
    );

    TelaPrincipal.imprimirNaTela(
      Conversao.bitsParaString(fluxoBrutoDeBits), Constantes.BIT_DECODIFICADO
    );

    return fluxoBrutoDeBits;
  }

  /* **************************************************************
  Metodo: camadaFisicaReceptoraDecodificacaoManchester*
  Funcao: Decodificar os bits da codificacao manchester*
  Parametros: int[] fluxoBrutoDeBits: bits a serem decodificados*
  Retorno: int[] bitsDecodificados*
  *************************************************************** */
  public static int[] camadaFisicaReceptoraDecodificacaoManchester(int[] fluxoBrutoDeBits) {
    TelaPrincipal.imprimirNaTela(
      Conversao.bitsParaString(fluxoBrutoDeBits), Constantes.BIT_RECEBIDO
    );

    int novoTamanho = 0;
    int numeroDeBits =
    32 - Integer.numberOfLeadingZeros(fluxoBrutoDeBits[fluxoBrutoDeBits.length-1]);

    //calcula novo tamanho do vetor quadro
    if(numeroDeBits <= 16){
      novoTamanho = (fluxoBrutoDeBits.length*2)-1;
    }else{
      novoTamanho = fluxoBrutoDeBits.length*2;
    }

    int[] asciiArray = new int[novoTamanho];
    int displayMask = 1 << 31;
    int valor = 0;

    for(int i=0, pos=0; i<fluxoBrutoDeBits.length; i++){
      int numero = fluxoBrutoDeBits[i];
      numeroDeBits = 32 - Integer.numberOfLeadingZeros(numero);

      //arredondando o numero de bits
      if(numeroDeBits <= 16){
        numeroDeBits = 16;
      }else{
        numeroDeBits = 32;
      }

      numero <<= 32-numeroDeBits;

      for(int j=1; j<=numeroDeBits/2; j++){
        if((numero & displayMask) == 0){ //representa o bit 0
          valor <<= 1;
          valor = valor | 0;
        }else{ //representa o bit 1
          valor <<= 1;
          valor = valor | 1;
        }
        numero <<= 2;

        if(j%8 == 0){
          asciiArray[pos] = valor;
          valor = 0;
          pos++;
        }
      }
    }

    int[] bitsDecodificados = Conversao.asciiParaBits(asciiArray);

    TelaPrincipal.imprimirNaTela(
      Conversao.bitsParaString(bitsDecodificados), Constantes.BIT_DECODIFICADO
    );

    return bitsDecodificados;
  }

  /* **************************************************************
  Metodo: camadaFisicaReceptoraDecodificacaoManchesterDiferencial*
  Funcao: Decodificar os bits da codificacao manchester diferencial*
  Parametros: int[] fluxoBrutoDeBits: bits a serem decodificados*
  Retorno: int[] bitsDecodificados*
  *************************************************************** */
  public static int[] camadaFisicaReceptoraDecodificacaoManchesterDiferencial(int[] fluxoBrutoDeBits) {
    TelaPrincipal.imprimirNaTela(
      Conversao.bitsParaString(fluxoBrutoDeBits), Constantes.BIT_RECEBIDO
    );

    int novoTamanho = 0;
    int numeroDeBits =
    32 - Integer.numberOfLeadingZeros(fluxoBrutoDeBits[fluxoBrutoDeBits.length-1]);

    //calcula novo tamanho do vetor fluxoBrutoDeBits
    if(numeroDeBits <= 16){
      novoTamanho = (fluxoBrutoDeBits.length*2)-1;
    }else{
      novoTamanho = fluxoBrutoDeBits.length*2;
    }

    int[] asciiArray = new int[novoTamanho];
    int displayMask = 1 << 31;
    int valor = 0;
    boolean transicao = false;

    for(int i=0, pos=0; i<fluxoBrutoDeBits.length; i++){
      int numero = fluxoBrutoDeBits[i];
      numeroDeBits = 32 - Integer.numberOfLeadingZeros(numero);

      //arredondando o numero de bits
      if(numeroDeBits <= 16){
        numeroDeBits = 16;
      }else{
        numeroDeBits = 32;
      }

      numero <<= 32-numeroDeBits;

      for(int j=1; j<=numeroDeBits/2; j++){
        if((numero & displayMask) == 0){
          if(transicao){
            valor <<= 1;
            valor = valor | 1;

            transicao = !transicao; //reseta a transicao
          }else{
            valor <<= 1;
            valor = valor | 0;
          }
        }else{
          transicao = !transicao; //houve transicao

          if(transicao){
            valor <<= 1;
            valor = valor | 1;
          }else{
            valor <<= 1;
            valor = valor | 0;

            transicao = !transicao; //reseta a transicao
          }
        }
        numero <<= 2;

        if(j%8 == 0){
          asciiArray[pos] = valor;
          valor = 0;
          pos++;
        }
      }
    }

    int[] bitsDecodificados = Conversao.asciiParaBits(asciiArray);

    TelaPrincipal.imprimirNaTela(
      Conversao.bitsParaString(bitsDecodificados), Constantes.BIT_DECODIFICADO
    );

    return bitsDecodificados;
  }

  /* **************************************************************
  Metodo: setTipoDeCodificacao*
  Funcao: seta o tipo de codificacao para manchester quando a camada
          fisica for violada*
  Parametros: int codificacao: codificacao manchester*
  Retorno: void*
  *************************************************************** */
  public static void setTipoDeCodificacao(int codificacao){
    tipoDeDecodificacao = codificacao;
    estaViolada = true;
  }
}
