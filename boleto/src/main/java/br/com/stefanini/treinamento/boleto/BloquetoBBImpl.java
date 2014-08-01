package br.com.stefanini.treinamento.boleto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import br.com.stefanini.treinamento.exception.ManagerException;

public abstract class BloquetoBBImpl implements BloquetoBB {

	protected String codigoBanco;
	protected String codigoMoeda;
	protected String fatorVencimento;
	protected Date dataVencimento;
	protected Date dataBase;
	protected BigDecimal valor;
	protected String numeroConvenioBanco;
	protected String complementoNumeroConvenioBancoSemDV;
	protected String numeroAgenciaRelacionamento;
	protected String contaCorrenteRelacionamentoSemDV;
	protected String tipoCarteira;

	private int dvCodigoBarras;

	protected abstract void validaDados() throws ManagerException;

	/**
	 * Inicializa o fator de vencimento
	 */
	protected void setFatorVencimento() {

		long dias = diferencaEmDias(dataBase, dataVencimento);
		
		
		// TODO: EXPLICAR O QUE ESTE M�TODO EST� FAZENDO

		fatorVencimento = String.format("%04d", dias);

	}

	/**
	 * Inicializa os valores, formata
	 */
	protected void init() {

		setFatorVencimento();

	}

	/**
	 * Retorna o valor formatado do boleto banc�rio
	 * 
	 * @return
	 */
	protected String getValorFormatado() {

		/**
		 * O m�todo retorna o valor informado de decimal para um formato
		 * racional, sendo que seu tamanho � no m�ximo de 10 posi��es, isto �, 2
		 * casas para a parte decimal e 8 casas para o a parte inteira, caso n�o
		 * completa as 8 casas, vai colocando zero at� completar. Isso tudo em
		 * String.
		 * 
		 */

		// TODO: Explicar o que este m�todo est� fazendo FEITO
		return String.format(
				"%010d",
				Long.valueOf(valor.setScale(2, RoundingMode.HALF_UP).toString()
						.replace(".", "")));
	}

	/**
	 * Formata o n�mero do conv�nio da Linha Digit�vel
	 * 
	 * @return
	 */
	protected abstract String getLDNumeroConvenio();

	/**
	 * Retorna o c�digo de barras do Bloqueto
	 * 
	 * @return c�digo de barras
	 */
	protected abstract String getCodigoBarrasSemDigito();

	public abstract String getCodigoBarras();

	/**
	 * Campo 5 da Linha Digit�vel
	 * 
	 * @return
	 */
	private String ldCampo5() {

		StringBuilder buffer = new StringBuilder();
		buffer.append(fatorVencimento);
		buffer.append(getValorFormatado());
		// TODO: COMPLETAR FEITO
		return buffer.toString();
	}

	/**
	 * Campo 4 da Linha Digit�vel
	 * 
	 * @return
	 */
	private String ldCampo4() {
		// TODO: COMPLETAR FEITO
		return String
				.valueOf(digitoVerificadorCodigoBarras(getCodigoBarrasSemDigito()));
	}

	/**
	 * Campo 3 da Linha Digit�vel
	 * 
	 * @return
	 */
	private String ldCampo3() {

		// TODO: COMPLETAR FEITO
		return String.format("%s.%s", getCodigoBarras().substring(34, 39),
				getCodigoBarras().substring(39, 44));
	}

	/**
	 * Campo 2 da Linha Digit�vel
	 * 
	 * @return
	 */
	private String ldCampo2() {

		// TODO: COMPLETAR FEITO
		return String.format("%s.%s", getCodigoBarras().substring(24, 29),
				getCodigoBarras().substring(29, 34));
	}

	/**
	 * Calcula o digito verificador do campo
	 * 
	 * @param campo
	 * @return
	 */
	protected int digitoVerificadorPorCampo(String campo, boolean valor) {
		// TODO: COMPLETAR
		
		StringBuffer s = new StringBuffer(campo);
		String reverso = s.reverse().toString();
		int total = 0;
		
		for(int i=0; i<=s.length(); i++){
			int resultado = 0;
			if(i%2 == 0){
				 resultado = reverso.toCharArray()[i]*2;
				
				if(resultado > 9){
					char[] teste = String.valueOf(resultado).toCharArray();
					for(char c: teste){
						total += c;
					}
				}else total += resultado;
				
			}else total += resultado;
		}
		
		int total2 = total;
		
		while(total2%10 != 0){
			total2 += 1;
		}
		

		return total2-total;
	}

	/**
	 * Calcula o digito verificado do c�digo de barras
	 * 
	 * @param codigoBarras
	 * @return
	 */
	protected int digitoVerificadorCodigoBarras(String codigoBarras) {
		// TODO: COMPLETAR
		return 0;
	}

	/**
	 * Campo 1 da Linha Digit�vel
	 * 
	 * - C�digo do Banco - C�digo da Moeda - N�mero do conv�nio
	 * 
	 * @return
	 */
	private String ldCampo1() {

		StringBuilder buffer = new StringBuilder();

		buffer.append(codigoBanco);
		buffer.append(codigoMoeda);
		buffer.append(getLDNumeroConvenio());

		// TODO: COMPLETAR FEITO
		return buffer.toString();

	}

	public String getLinhaDigitavel() {

		init();

		StringBuilder buffer = new StringBuilder();
		buffer.append(ldCampo1());
		buffer.append(digitoVerificadorPorCampo(ldCampo1(),true));
		buffer.append(" ");
		buffer.append(ldCampo2());
		buffer.append(digitoVerificadorPorCampo(ldCampo2(),true));
		buffer.append(" ");
		buffer.append(ldCampo3());
		buffer.append(digitoVerificadorPorCampo(ldCampo3(),true));
		buffer.append(" ");
		buffer.append(ldCampo4());
		buffer.append(" ");
		buffer.append(ldCampo5());

		// TODO: COMPLETAR FEITO

		return buffer.toString();
	}

	/**
	 * Retorna a diferen�a em dias de duas datas
	 * 
	 * @param dataInicial
	 *            Data inicial
	 * @param dataFinal
	 *            Data final
	 * @return
	 */
	protected static long diferencaEmDias(Date dataInicial, Date dataFinal) {

		/*
		 * O m�todo retorna o c�lculo da data base definida menos a data de
		 * vencimento, o resultado este diferencia ser� em ms, pois � utilizado
		 * o m�todo "getTime", isso dividido por um dia, que em ms � 86400000 do
		 * tipo double.
		 */

		// TODO: Estude a Math e escreva aqui o que este m�todo est� fazendo

		return Math
				.round((dataFinal.getTime() - dataInicial.getTime()) / 86400000D);
	}

	public int getDvCodigoBarras() {

		getCodigoBarras();

		return dvCodigoBarras;
	}
}
