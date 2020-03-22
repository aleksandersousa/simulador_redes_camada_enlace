package camadas.camada_enlace;

import java.util.ArrayList;

import util.Constantes;
import util.Conversao;
import view.PainelDireito;
import view.TelaPrincipal;

public class CamadaEnlaceDadosReceptoraEnquadramento {
  /* *****************************************************************************
  Metodo: camadaDeEnlaceDeDadosReceptoraEnquadramento*
  Funcao: Desfazer o que a camada de enlace transmissora fez*
  Parametros: int[] quadro: vetor de inteiros com os bits*
  Retorno: int[] quadroDesenquadrado = vetor com os quadros decodificados*
  ***************************************************************************** */
  static public int[] camadaDeEnlaceDeDadosReceptoraEnquadramento(int[] quadro){
    int tipoDeEnquadramento = PainelDireito.cmbListaDeEnquadramento.getSelectedIndex();
    int[] quadroDesenquadrado = null;

    switch (tipoDeEnquadramento) {
      case 0:
        quadroDesenquadrado = enquadramentoReceptoraContagemDeCaracteres(quadro);

        TelaPrincipal.imprimirNaTela(
          Conversao.bitsParaString(quadroDesenquadrado), Constantes.QUADRO_DECODIFICADO
        );
        break;
      case 1:
        quadroDesenquadrado = enquadramentoReceptoraInsercaoDeBytes(quadro);

        TelaPrincipal.imprimirNaTela(
          Conversao.bitsParaString(quadroDesenquadrado), Constantes.QUADRO_DECODIFICADO
        );
        break;
      case 2:
        quadroDesenquadrado = enquadramentoReceptoraInsercaoDeBits(quadro);

        TelaPrincipal.imprimirNaTela(
          Conversao.bitsParaString(quadroDesenquadrado), Constantes.QUADRO_DECODIFICADO
        );
        break;
      case 3:
        quadroDesenquadrado = enquadramentoReceptoraViolacaoCamadaFisica(quadro);

        TelaPrincipal.imprimirNaTela(
          Conversao.bitsParaString(quadroDesenquadrado), Constantes.QUADRO_DECODIFICADO
        );
        break;
    }

    return quadroDesenquadrado;
  }

  /* *****************************************************************************
  Metodo: enquadramentoReceptoraContagemDeCaracteres*
  Funcao: Desfazer o que a contagem de caracteres transmissora fez*
  Parametros: int[] quadro: vetor de inteiros com os bits*
  Retorno: int[] quadroDesenquadrado = vetor com os quadros decodificados*
  ***************************************************************************** */
  private static int[] enquadramentoReceptoraContagemDeCaracteres(int[] quadro) {
    ArrayList<Integer> tempQuadro = new ArrayList<>();
    int[] tempAsciiArray = Conversao.bitsParaAscii(quadro);
    ArrayList<Integer> asciiArray = new ArrayList<>();

    for(int i=0; i<tempAsciiArray.length; i++){
      if(tempAsciiArray[i] != 0){
        asciiArray.add(tempAsciiArray[i]);
      }
    }

    int index = 0;
    while(index < asciiArray.size()){
      int tamanhoQuadro = asciiArray.get(index);

      if(index > 0){
        tempQuadro.add(Constantes.ESPACO);
      }

      index++;
      for(int i=1; i<tamanhoQuadro; i++){
        tempQuadro.add(asciiArray.get(index));
        index++;
      }
    }

    int[] tempQuadro2 = new int[tempQuadro.size()];
    for(int i=0; i<tempQuadro.size(); i++){
      tempQuadro2[i] = tempQuadro.get(i);
    }

    int[] quadroDesenquadrado = Conversao.asciiParaBits(tempQuadro2);

    return quadroDesenquadrado;
  }

  /* *****************************************************************************
  Metodo: enquadramentoReceptoraInsercaoDeBytes*
  Funcao: Desfazer o que a insercao de bytes transmissora fez*
  Parametros: int[] quadro: vetor de inteiros com os bits*
  Retorno: int[] quadroDesenquadrado = vetor com os quadros decodificados*
  ***************************************************************************** */
  private static int[] enquadramentoReceptoraInsercaoDeBytes(int[] quadro) {
    final char COMECO = 'S';
    final char FINAL = 'E';
    final char ESCAPE = '/';

    ArrayList<Integer> tempQuadro = new ArrayList<>();
    int[] tempAsciiArray = Conversao.bitsParaAscii(quadro);
    ArrayList<Integer> asciiArray = new ArrayList<>();

    for(int i=0; i<tempAsciiArray.length; i++){
      if(tempAsciiArray[i] != 0){
        asciiArray.add(tempAsciiArray[i]);
      }
    }

    int index = 0;
    while(index < asciiArray.size()){
      if(asciiArray.get(index) == (int) COMECO){
        int carga = asciiArray.get(++index);

        while(carga != (int) FINAL){
          if(carga == (int) ESCAPE){
            carga = asciiArray.get(++index);
          }

          tempQuadro.add(carga);
          carga = asciiArray.get(++index);
        }

        if(carga == (int) FINAL && index != asciiArray.size() - 1){
          tempQuadro.add(Constantes.ESPACO);
        }

        index++;
      }
    }

    int[] tempQuadro2 = new int[tempQuadro.size()];
    for(int i=0; i<tempQuadro.size(); i++){
      tempQuadro2[i] = tempQuadro.get(i);
    }

    int[] quadroDesenquadrado = Conversao.asciiParaBits(tempQuadro2);

    return quadroDesenquadrado;
  }

  /* *****************************************************************************
  Metodo: enquadramentoReceptoraInsercaoDeBits*
  Funcao: Desfazer o que a insercao de bites transmissora fez*
  Parametros: int[] quadro: vetor de inteiros com os bits*
  Retorno: int[] quadroDesenquadrado = vetor com os quadros decodificados*
  ***************************************************************************** */
  private static int[] enquadramentoReceptoraInsercaoDeBits(int[] quadro) {
    ArrayList<Integer> tempQuadro = new ArrayList<>();

    final int displayMask = 1 << 31;
    final int byteFlag = 126;
    final int byteeFlag2 = 252;

    int[] arrayInteiros = Conversao.bitsParaAscii(quadro);

    ArrayList<Integer> cargaUtil = new ArrayList<>();

    for(int i=0; i<arrayInteiros.length; i++){
      if(arrayInteiros[i] != byteFlag && arrayInteiros[i] != byteeFlag2){
        cargaUtil.add(arrayInteiros[i]);
      }
    }

    int bits1 = 0;
    int valor = 0;
    int limiteBytes = 0;

    for(int i=0; i<cargaUtil.size(); i++){
      int numero = cargaUtil.get(i);
      int numeroDeBits = 32 - Integer.numberOfLeadingZeros(numero);
      numeroDeBits = Conversao.arredondaBits(numeroDeBits);

      numero <<= 32 - numeroDeBits;

      int bit = -1;
      for(int j=1; j<=numeroDeBits; j++){
        int bitAnterior = bit;
        bit = (numero & displayMask) == 0 ? 0 : 1;

        if(bit == 1 && bitAnterior == 1){
          bits1++;
        }

        if(bits1 == 4 && bit == 0){
          bits1 = 0;
        }else{
          valor <<= 1;
          valor |= bit;
          limiteBytes++;
        }
        numero <<= 1;

        if(j%8 == 0){
          bits1 = 0;

          if(limiteBytes == Constantes.LIMITE_DE_BYTES_INTEIRO || j == numeroDeBits){
            tempQuadro.add(valor);
            valor = 0;
          }
        }
      }
    }

    int reduzTamanho = 0;
    for(int i=0; i<tempQuadro.size(); i++){
      if(tempQuadro.get(i) == 0){
        reduzTamanho++;
      }
    }

    int[] tempQuadro2 = new int[tempQuadro.size()-reduzTamanho];
    for(int i=0; i<tempQuadro2.length; i++){
      if(tempQuadro.get(i) != 0){
        tempQuadro2[i] = tempQuadro.get(i);
      }
    }

    int[] quadroDesenquadrado = Conversao.asciiParaBits(tempQuadro2);

    return quadroDesenquadrado;
  }

  /* *****************************************************************************
  Metodo: enquadramentoReceptoraViolacaoCamadaFisica*
  Funcao: Desfazer o que a violacao da camada fisica transmissora fez*
  Parametros: int[] quadro: vetor de inteiros com os bits*
  Retorno: int[] quadroDesenquadrado = vetor com os quadros decodificados*
  ***************************************************************************** */
  private static int[] enquadramentoReceptoraViolacaoCamadaFisica(int[] quadro) {
    final int byteFlag = 255;

    int[] arrayInteiros = Conversao.bitsParaAscii(quadro);
    ArrayList<Integer> cargaUtil = new ArrayList<>();
    ArrayList<Integer> tempQuadro = new ArrayList<>();

    int contaFlag = 0;

    int reducaoTamanho = 0;
    for(int i=0; i<arrayInteiros.length; i++){
      if(arrayInteiros[i] == 0 || arrayInteiros[i] == 128){
        reducaoTamanho++;
      }
    }

    int[] arrayInteirosSemZeros = new int[arrayInteiros.length - reducaoTamanho];
    for(int i=0; i<arrayInteirosSemZeros.length; i++){
      if(arrayInteiros[i] != 0 || arrayInteiros[i] == 128){
        arrayInteirosSemZeros[i] = arrayInteiros[i];
      }
    }

    for(int i=0; i<arrayInteirosSemZeros.length; i++){
      if(arrayInteirosSemZeros[i] != byteFlag){
        cargaUtil.add(arrayInteirosSemZeros[i]);
      }else{
        contaFlag++;

        if(contaFlag == 2){
          for(int j=0; j<cargaUtil.size(); j++){
            tempQuadro.add(cargaUtil.get(j));
          }

          if(i != arrayInteirosSemZeros.length - 1){
            tempQuadro.add(Constantes.ESPACO);
          }

          cargaUtil.clear();
          contaFlag = 0;
        }
      }
    }

    int[] tempQuadro2 = new int[tempQuadro.size()];
    for(int i=0; i<tempQuadro2.length; i++){
      tempQuadro2[i] = tempQuadro.get(i);
    }

    int[] quadroDesenquadrado = Conversao.asciiParaBits(tempQuadro2);

    return quadroDesenquadrado;
  }
}
