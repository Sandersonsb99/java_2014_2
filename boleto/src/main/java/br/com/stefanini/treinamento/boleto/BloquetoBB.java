package br.com.stefanini.treinamento.boleto;

public interface BloquetoBB {

	/**
	 * Retorna o c�digo de barras do Bloqueto
	 * 
	 * @return c�digo de barras
	 */
	public String getCodigoBarras();

	/**
	 * Retorna a linha digit�vel do Bloqueto
	 * 
	 * @return Linha digit�vel
	 */
	public String getLinhaDigitavel();
	
	

	/**
	 * Retorna o digito verificador do c�digo de barras
	 * 
	 * @return
	 */
	public int getDvCodigoBarras();

}
