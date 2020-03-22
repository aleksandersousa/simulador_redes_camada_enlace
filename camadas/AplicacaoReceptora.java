package camadas;

import view.TelaPrincipal;
import view.PainelDireito;
import view.PainelEsquerdo;

import util.Constantes;

public class AplicacaoReceptora {
  /* *****************************************************************************
  Metodo: aplicacaoReceptora*
  Funcao: Imprimir a mensagem decodificada na tela*
  Parametros: String mensagem: mensagem a ser impressa*
  Retorno: void*
  ***************************************************************************** */
  public static void aplicacaoReceptora(String mensagem) {
    TelaPrincipal.imprimirNaTela(mensagem, Constantes.MENSAGEM_DECODIFICADA);

    PainelEsquerdo.cmbListaDeCodificacao.setEnabled(true);
    PainelDireito.cmbListaDeEnquadramento.setEnabled(true);

    PainelEsquerdo.mutexCodificacao.release();
    PainelEsquerdo.mutexEnquadramento.release();
  }
}
