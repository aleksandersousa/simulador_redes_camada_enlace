/* ***************************************************************
Autor: Aleksander Santos Sousa*
Matricula: 201810825*
Inicio: 23/01/2020*
Ultima alteracao: 01/02/2020*
Nome: Simulador de Redes*
Funcao: Simular o envio de uma mensagem de texto.
*************************************************************** */
package view;

import util.Constantes;
import util.Formatacao;

import java.awt.Dimension;
import java.awt.Color;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class PainelDireito extends JPanel {
  public static ArrayList<JTextArea> arrayCaixasDeTexto;
  public static JComboBox<String> cmbListaDeEnquadramento;

  private ArrayList<JPanel> arrayPaineis;

  private JTextArea txtLabelMensagemDecodificada;
  private JTextArea txtLabelNumerosAciiDecodificados;
  private JTextArea txtLabelBitsDecodificados;
  private JTextArea txtLabelBitsRecebidos;
  private JTextArea txtLabelQuadrosDecodificados;

  /* **************************************************************
  Metodo: PainelDireito*
  Funcao: Construtor da classe PainelDireito.*
  Parametros: nulo*
  Retorno: void*
  *************************************************************** */
  public PainelDireito() {
    PainelDireito.arrayCaixasDeTexto = Formatacao.inicializarCaixasDeTexto();

    this.arrayPaineis = new ArrayList<>();
    this.txtLabelMensagemDecodificada = new JTextArea("Mensagem: ");
    this.txtLabelNumerosAciiDecodificados = new JTextArea("Numero Ascii: ");
    this.txtLabelBitsDecodificados = new JTextArea("Bits decodificados: ");
    this.txtLabelBitsRecebidos = new JTextArea("Bits recebidos: ");
    this.txtLabelQuadrosDecodificados = new JTextArea("Quadros Decod: ");

    for(int i=0; i<6; i++){
      arrayPaineis.add(new JPanel());
    }

    this.initGUIComponents();
  }

  /* **************************************************************
  Metodo: initGUIComponents*
  Funcao: inicializar os componentes do painel.*
  Parametros: nulo*
  Retorno: void*
  *************************************************************** */
  private void initGUIComponents() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.formatarLabels();
    this.iniciarComboBox();
    this.adicionarComponentes();
  }

  /* **************************************************************
  Metodo: formatarLabels*
  Funcao: formata os labels.*
  Parametros: nulo*
  Retorno: void*
  *************************************************************** */
  private void formatarLabels() {
    this.txtLabelMensagemDecodificada.setBackground(this.getBackground());
    this.txtLabelNumerosAciiDecodificados.setBackground(this.getBackground());
    this.txtLabelBitsDecodificados.setBackground(this.getBackground());
    this.txtLabelBitsRecebidos.setBackground(this.getBackground());
    this.txtLabelQuadrosDecodificados.setBackground(this.getBackground());

    Formatacao.inicializarLabels(
      txtLabelMensagemDecodificada,
      Constantes.LARGURA_LABELS_DIREITO,
      Constantes.ALTURA_LABELS
    );

    Formatacao.inicializarLabels(
      txtLabelNumerosAciiDecodificados,
      Constantes.LARGURA_LABELS_DIREITO,
      Constantes.ALTURA_LABELS
    );

    Formatacao.inicializarLabels(
      txtLabelBitsDecodificados,
      Constantes.LARGURA_LABELS_DIREITO,
      Constantes.ALTURA_LABELS
    );

    Formatacao.inicializarLabels(
      txtLabelBitsRecebidos,
      Constantes.LARGURA_LABELS_DIREITO,
      Constantes.ALTURA_LABELS
    );

    Formatacao.inicializarLabels(
      txtLabelQuadrosDecodificados,
      Constantes.LARGURA_LABELS_DIREITO,
      Constantes.ALTURA_LABELS
    );
  }

  private void iniciarComboBox() {
    String[] tiposDeEnquadramento = {
      "Contagem de caracteres",
      "Insercao de bytes",
      "Insercao de bits",
      "Violacao da camada fisica"
    };

    cmbListaDeEnquadramento = new JComboBox<>(tiposDeEnquadramento);
    cmbListaDeEnquadramento.setForeground(Color.RED);
    cmbListaDeEnquadramento.setSelectedIndex(0);
    cmbListaDeEnquadramento.setPreferredSize(
      new Dimension(420, Constantes.ALTURA_COMPONENTES)
    );
  }

  /* **************************************************************
  Metodo: adicionarComponentes*
  Funcao: inicializa e adiciona os componentes ao painel.*
  Parametros: nulo*
  Retorno: void*
  *************************************************************** */
  private void adicionarComponentes() {
    arrayPaineis.get(0).add(txtLabelMensagemDecodificada);
    arrayPaineis.get(0).add(
      Formatacao.inicializarBarraDeRolagem(
        PainelDireito.arrayCaixasDeTexto.get(4),
        Constantes.LARGURA_COMPONENTES,
        Constantes.ALTURA_COMPONENTES
      )
    );

    arrayPaineis.get(1).add(txtLabelNumerosAciiDecodificados);
    arrayPaineis.get(1).add(
      Formatacao.inicializarBarraDeRolagem(
        PainelDireito.arrayCaixasDeTexto.get(1),
        Constantes.LARGURA_COMPONENTES,
        Constantes.ALTURA_COMPONENTES*2
      )
    );

    arrayPaineis.get(2).add(txtLabelBitsDecodificados);
    arrayPaineis.get(2).add(
      Formatacao.inicializarBarraDeRolagem(
        PainelDireito.arrayCaixasDeTexto.get(5),
        Constantes.LARGURA_COMPONENTES,
        Constantes.ALTURA_COMPONENTES
      )
    );

    arrayPaineis.get(3).add(txtLabelBitsRecebidos);
    arrayPaineis.get(3).add(
      Formatacao.inicializarBarraDeRolagem(
        Formatacao.arrayCaixasDeTexto.get(6),
        Constantes.LARGURA_COMPONENTES,
        Constantes.ALTURA_COMPONENTES
      )
    );

    arrayPaineis.get(5).add(txtLabelQuadrosDecodificados);
    arrayPaineis.get(5).add(
      Formatacao.inicializarBarraDeRolagem(
        Formatacao.arrayCaixasDeTexto.get(8),
        Constantes.LARGURA_COMPONENTES,
        Constantes.ALTURA_COMPONENTES
      )
    );

    arrayPaineis.get(4).add(cmbListaDeEnquadramento);

    this.add(arrayPaineis.get(0));
    this.add(arrayPaineis.get(1));
    this.add(arrayPaineis.get(5));
    this.add(arrayPaineis.get(2));
    this.add(arrayPaineis.get(3));
    this.add(arrayPaineis.get(4));
  }

  /* **************************************************************
  Metodo: getPreferredSize*
  Funcao: seta o tamanho deste painel.*
  Parametros: nulo*
  Retorno: void*
  *************************************************************** */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(600, 300);
  }
}
