package camadas.camada_enlace;

import java.util.ArrayList;

import util.Constantes;
import util.Conversao;

import view.PainelDireito;
import view.TelaPrincipal;

public class CamadaEnlaceDadosTransmissoraEnquadramento {
  /* *****************************************************************************
  Metodo: camadaDeEnlaceDeDadosTransmissoraEnquadramento*
  Funcao: Enviar os quadros para a camada fisica*
  Parametros: int[] quadro: vetor de inteiros com os bits*
  Retorno: int[] quadroEnquadrado = vetor com os quadros*
  ***************************************************************************** */
  static public int[] camadaDeEnlaceDeDadosTransmissoraEnquadramento(int[] quadro) {
    int tipoDeEnquadramento = PainelDireito.cmbListaDeEnquadramento.getSelectedIndex();
    int[] quadroEnquadrado = null;

    switch (tipoDeEnquadramento) {
      case 0:
        quadroEnquadrado = enquadramentoContagemDeCaracteres(quadro);

        TelaPrincipal.imprimirNaTela(
          Conversao.bitsParaString(quadroEnquadrado), Constantes.QUADRO_CODIFICADO
        );
        break;
      case 1:
        quadroEnquadrado = enquadramentoInsercaoDeBytes(quadro);

        TelaPrincipal.imprimirNaTela(
          Conversao.bitsParaString(quadroEnquadrado), Constantes.QUADRO_CODIFICADO
        );
        break;
      case 2:
        quadroEnquadrado = enquadramentoInsercaoDeBits(quadro);

        TelaPrincipal.imprimirNaTela(
          Conversao.bitsParaString(quadroEnquadrado), Constantes.QUADRO_CODIFICADO
        );
        break;
      case 3:
        quadroEnquadrado = enquadramentoViolacaoCamadaFisica(quadro);

        TelaPrincipal.imprimirNaTela(
          Conversao.bitsParaString(quadroEnquadrado), Constantes.QUADRO_CODIFICADO
        );
        break;
    }

    return quadroEnquadrado;
  }

  /* *****************************************************************************
  Metodo: enquadramentoContagemDeCaracteres*
  Funcao: Cria os quadros com a contagem de caracteres*
  Parametros: int[] quadro: vetor de inteiros com os bits*
  Retorno: int[] quadroEnquadrado = vetor com os quadros*
  ***************************************************************************** */
  private static int[] enquadramentoContagemDeCaracteres(int[] quadro) {
    int[] quadroEnquadrado;

    int numeroDeBits = 32 - Integer.numberOfLeadingZeros(quadro[quadro.length-1]);
    numeroDeBits = Conversao.arredondaBits(numeroDeBits);

    if(quadro.length == 1 && numeroDeBits <= 24){
      quadroEnquadrado = new int[quadro.length];
    }else if(numeroDeBits >= 24){
      quadroEnquadrado = new int[quadro.length + 2];
    }else{
      quadroEnquadrado = new int[quadro.length + 1];
    }

    int[] arrayAscii = Conversao.bitsParaAscii(quadro);
    int valor = 0;
    int index = 0;
    int posQuadro = 0;
    int numeroDeCaracteres = 0;

    while(index < arrayAscii.length){
      ArrayList<Integer> caracteresQuadro = new ArrayList<>();

      for(; index<arrayAscii.length; index++){
        numeroDeCaracteres++;

        if(index == arrayAscii.length-1){
          numeroDeCaracteres++;
        }

        if(arrayAscii[index] == Constantes.ESPACO){
          if(index == arrayAscii.length - 1){
            caracteresQuadro.add(arrayAscii[index]);
          }

          index++;
          break;
        }else{
          caracteresQuadro.add(arrayAscii[index]);
        }
      }

      for(int i=0; i<numeroDeCaracteres; i++){
        if(i == 0){
          valor <<= 8;
          valor = valor | numeroDeCaracteres;
        }else{
          if(i-1 < caracteresQuadro.size()){
            valor <<= 8;
            valor = valor | caracteresQuadro.get(i-1);
          }
        }

        if(i%4 == 3 || i == numeroDeCaracteres-1){
          quadroEnquadrado[posQuadro] = valor;
          posQuadro++;
          valor = 0;
        }
      }

      numeroDeCaracteres = 0;
      caracteresQuadro.clear();
    }

    return quadroEnquadrado;
  }

  /* *****************************************************************************
  Metodo: enquadramentoInsercaoDeBytes*
  Funcao: Cria os quadros com uma flag de inicio, final e escape*
  Parametros: int[] quadro: vetor de inteiros com os bits*
  Retorno: int[] quadroEnquadrado = vetor com os quadros*
  ***************************************************************************** */
  private static int[] enquadramentoInsercaoDeBytes(int[] quadro) {
    final char COMECO = 'S';
    final char FINAL = 'E';
    final char ESCAPE = '/';

    ArrayList<Integer> tempQuadroEnquadrado = new ArrayList<>();

    int[] arrayAscii = Conversao.bitsParaAscii(quadro);

    int tamanhoDoQuadro = 0;
    int index = 0;
    int valor = 0;
    int cont = 0;

    while(index < arrayAscii.length){
      ArrayList<Integer> caracteresQuadro = new ArrayList<>();

      for(; index<arrayAscii.length; index++){
        tamanhoDoQuadro++;

        if(index == arrayAscii.length-1){
          tamanhoDoQuadro++;
        }

        if(arrayAscii[index] == Constantes.ESPACO){
          if(index == arrayAscii.length - 1){
            caracteresQuadro.add(arrayAscii[index]);
          }

          index++;
          break;
        }else{
          caracteresQuadro.add(arrayAscii[index]);
        }
      }

      for(int i=0; i<tamanhoDoQuadro; i++){
        if(i == 0){
          valor <<= 8;
          valor = valor | COMECO;

          cont++;
        }else{
          if(i-1 < caracteresQuadro.size()){
            int carga = caracteresQuadro.get(i-1);

            if(cont == Constantes.LIMITE_DE_BYTES_INTEIRO){
              tempQuadroEnquadrado.add(valor);
              valor = 0;
              cont = 0;
            }

            if(carga == (int) COMECO || carga == (int) FINAL || carga == (int) ESCAPE){
              valor <<= 8;
              valor = valor | ESCAPE;

              cont++;
            }

            if(cont == Constantes.LIMITE_DE_BYTES_INTEIRO){
              tempQuadroEnquadrado.add(valor);
              valor = 0;
              cont = 0;
            }

            valor <<= 8;
            valor = valor | carga;

            cont++;

            if(cont == Constantes.LIMITE_DE_BYTES_INTEIRO){
              tempQuadroEnquadrado.add(valor);
              valor = 0;
              cont = 0;
            }

            if(i == tamanhoDoQuadro-1){
              valor <<= 8;
              valor = valor | FINAL;

              tempQuadroEnquadrado.add(valor);
              valor = 0;
              cont = 0;
              break;
            }
          }
        }
      }

      tamanhoDoQuadro = 0;
      caracteresQuadro.clear();
    }

    int[] quadroEnquadrado = new int[tempQuadroEnquadrado.size()];

    for(int i=0; i<tempQuadroEnquadrado.size(); i++){
      quadroEnquadrado[i] = tempQuadroEnquadrado.get(i);
    }

    return quadroEnquadrado;
  }

  /* *****************************************************************************
  Metodo: enquadramentoInsercaoDeBits*
  Funcao: Cria os quadros com uma flag de inicio, final e adiciona o bit 0 quando
          aparece uma sequencia de 5 1s*
  Parametros: int[] quadro: vetor de inteiros com os bits*
  Retorno: int[] quadroEnquadrado = vetor com os quadros*
  ***************************************************************************** */
  private static int[] enquadramentoInsercaoDeBits(int[] quadro) {
    final int byteFlag = 126;
    final int displayMask = 1 << 31;

    boolean cincoBits1 = false;
    int bits1 = 0;

    for(int i=0; i<quadro.length; i++){
      int numero = quadro[i];
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

        if(bits1 == 4){
          cincoBits1 = true;
          break;
        }

        numero <<= 1;

        if(j%8 == 0){
          bits1 = 0;
        }
      }
      bits1 = 0;
    }

    int numeroDeBits = 32 - Integer.numberOfLeadingZeros(quadro[quadro.length-1]);
    numeroDeBits = Conversao.arredondaBits(numeroDeBits);

    int[] quadroEnquadrado;
    int novoTamanho = 0;

    if(cincoBits1){
      if(numeroDeBits <= 8){
        novoTamanho = (quadro.length*4) - 3;
      }else if(numeroDeBits <= 16){
        novoTamanho = (quadro.length*4) - 2;
      }else if(numeroDeBits <= 24){
        novoTamanho = (quadro.length*4) - 1;
      }else if(numeroDeBits <= 32){
        novoTamanho = quadro.length*4;
      }
    }else if(numeroDeBits <= 16){
      novoTamanho = quadro.length*2 - 1;
    }else{
      novoTamanho = quadro.length*2;
    }

    quadroEnquadrado = new int[novoTamanho];

    int limiteInteiro = 0;
    int posQuadro = 0;
    boolean comecoDoQuadro = true;
    int valor = 0;

    for(int i=0; i<quadro.length; i++){
      int numero = quadro[i];
      numeroDeBits = 32 - Integer.numberOfLeadingZeros(numero);
      numeroDeBits = Conversao.arredondaBits(numeroDeBits);

      numero <<= 32 - numeroDeBits;

      bits1 = 0;
      int bit = -1;
      for(int j=1; j<=numeroDeBits; j++){
        if(comecoDoQuadro){
          valor <<= 8;
          valor |= byteFlag;
          limiteInteiro++;
        }

        int bitAnterior = bit;
        bit = (numero & displayMask) == 0 ? 0 : 1;

        if(bit == 1 && bitAnterior == 1){
          bits1++;
        }

        if(bits1 == 4){
          valor <<= 1;
          valor |= bit;
          valor <<= 1;
          bits1 = 0;
        }else{
          valor <<= 1;
          valor |= bit;
        }
        numero <<= 1;

        if(j%8 == 0){
          limiteInteiro++;
          bits1 = 0;

          if(cincoBits1){
            valor <<= 8;
            valor |= byteFlag;
            limiteInteiro++;

            quadroEnquadrado[posQuadro] = valor;
            valor = 0;
            posQuadro++;

            comecoDoQuadro = true;
          }else{
            if(limiteInteiro == Constantes.LIMITE_DE_BYTES_INTEIRO - 1 || j == numeroDeBits){
              valor <<= 8;
              valor |= byteFlag;
              limiteInteiro++;


              quadroEnquadrado[posQuadro] = valor;
              valor = 0;
              posQuadro++;

              comecoDoQuadro = true;
            }
          }
        }else{
          comecoDoQuadro = false;
        }
      }
    }

    return quadroEnquadrado;
  }

  /* *****************************************************************************
  Metodo: enquadramentoViolacaoCamadaFisica*
  Funcao: Cria os quadros violando a camada fisica, utilizando como flag um padrao
          de bits invalido para a codificacao manchester*
  Parametros: int[] quadro: vetor de inteiros com os bits*
  Retorno: int[] quadroEnquadrado = vetor com os quadros*
  ***************************************************************************** */
  private static int[] enquadramentoViolacaoCamadaFisica(int[] quadro) {
    final int byteFlag = 255;

    int[] arrayAscii = Conversao.bitsParaAscii(quadro);
    int valor = 0;
    int index = 0;
    int tamanhoDoQuadro = 0;

    ArrayList<Integer> tempQuadro = new ArrayList<>();

    int limiteDeBytes = 0;

    while(index < arrayAscii.length){
      ArrayList<Integer> caracteresQuadro = new ArrayList<>();

      for(; index<arrayAscii.length; index++){
        tamanhoDoQuadro++;

        if(index == arrayAscii.length-1){
          tamanhoDoQuadro++;
        }

        if(arrayAscii[index] == Constantes.ESPACO){
          if(index == arrayAscii.length - 1){
            caracteresQuadro.add(arrayAscii[index]);
          }

          index++;
          break;
        }else{
          caracteresQuadro.add(arrayAscii[index]);
        }
      }

      for(int i=0; i<tamanhoDoQuadro; i++){
        if(i == 0){
          valor <<= 8;
          valor = valor | byteFlag;

          limiteDeBytes++;
        }else{
          if(limiteDeBytes == Constantes.LIMITE_DE_BYTES_INTEIRO){
            tempQuadro.add(valor);
            valor = 0;
            limiteDeBytes = 0;
          }

          if(i-1 < caracteresQuadro.size()){
            valor <<= 8;
            valor = valor | caracteresQuadro.get(i-1);
            limiteDeBytes++;
          }

          if(limiteDeBytes == Constantes.LIMITE_DE_BYTES_INTEIRO){
            tempQuadro.add(valor);
            valor = 0;
            limiteDeBytes = 0;
          }

          if(i == tamanhoDoQuadro - 1){
            valor <<= 8;
            valor |= byteFlag;

            tempQuadro.add(valor);
            valor = 0;
          }
        }
      }

      tamanhoDoQuadro = 0;
      caracteresQuadro.clear();
    }

    int[] quadroEnquadrado = new int[tempQuadro.size()];

    for(int i=0; i<tempQuadro.size(); i++){
      quadroEnquadrado[i] = tempQuadro.get(i);
    }

    return quadroEnquadrado;
  }
}
