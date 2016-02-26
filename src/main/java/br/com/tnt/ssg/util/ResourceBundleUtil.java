package br.com.tnt.ssg.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Classe utilitária para manipular mensagem de bundle. Contém todas as regras
 * para localizar, manipular e validar mensagens de bundle da aplicação. Esta
 * classe está de acordo com a recomendação de nomenclatura usada nos projetos
 * web.
 * 
 * @author <a href="mailto:everton@javafree.org">Everton Barros</a>
 * @version 1.0
 */
public class ResourceBundleUtil {

	/**
	 * Indica qual a produndiade que um página deve estar na estrutura de pastas
	 * da aplicação web. Este número foi retirado da arquitetura de referência,
	 * por exemplo, a recomendação é
	 * '/subsystems/empresa/pages/cadastrarAtividade.xhtml'
	 */
	public static final int PAGE_DEPTH = 4;

	/**
	 * Chave de bundle que representa a sequencia de arquivos.
	 */
	// private static final String BUNDLE_FILES_KEY = "gui.bundle.files";

	/**
	 * Percorre todos os arquivos de bundle cadastrados na aplicação. Atualmente
	 * está fixado o "ApplicationMessages" e o
	 * "/br/com/azi/component/gui/presentation/resources/messages/Message"
	 * 
	 * @author <a href="mailto:everton@javafree.org">Everton Barros</a>
	 * @param locale
	 *            Localidade corrente.
	 * @param key
	 *            Chave a ser procurada.
	 * @param params
	 *            parametros para adiconar a mensagem
	 * @return Retorna o valor da chave ou "", caso não encontrado.
	 */
	public static String getKey(Locale locale, String key, Object... params) {
		// ConfigService configService = getConfigService();
		/*
		 * String filesValue = configService.getValue(BUNDLE_FILES_KEY);
		 * 
		 * String[] files;
		 * 
		 * if (filesValue != null && filesValue.length() > 0) { files =
		 * filesValue.split(",");
		 */

		// String label = "";

		// for (String bundleName : files) {

		String bundleName = "messages";
		String label = getKeyBundle(locale, bundleName, key, params);

		if (!key.equals(label)) {
			return label;
		} else {
			return key;
		}
	}

	private static String getParameterizedMessage(String messageText,
			Locale messageLocale, Object[] params) {

		MessageFormat messageFormat = new MessageFormat(messageText,
				messageLocale);
		messageText = messageFormat.format(params, new StringBuffer(), null)
				.toString();
		return messageText;
	}

	/**
	 * Retorna a chave de bundle informando localidade, nome do arquivo de
	 * bundle e a própria chave.
	 * 
	 * @author <a href="mailto:everton@javafree.org">Everton Barros</a>
	 * @param locale
	 *            Localidade corrente.
	 * @param bundleName
	 *            Nome do arquivo de bundle sem a extensão e a sigla da lingua.
	 * @param key
	 *            Chave a ser procurada.
	 * @return Retorna o valor da chave ou "", caso não encontrado.
	 */
	public static String getKeyBundle(Locale locale, String bundleName,
			String key, Object... params) {

		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale,
				getClassLoader());
		try {
			String msg = bundle.getString(key);
			if (params != null) {
				msg = getParameterizedMessage(msg, locale, params);
			}
			return msg;
		} catch (MissingResourceException e) {
			// assume que a mensagem eh a igual a key
			return key;
		}
	}

	/**
	 * Verifica se a chave é um valor literal ou de fato uma chave de bundle.
	 * Para que uma texto seja considerado uma chave de bundle, ele deve seguir
	 * a convenção de nomenclatura adotada que define que as chaves de bundle
	 * devem: <li>Começar sempre com letra minúscula</li> <li>Não deve conter
	 * espaços em branco</li> <li>A separação das palavras são feitas por ponto</li>
	 * Poranto, se o texto passado não passar por estas regras, então ele não é
	 * considerado uma key de bundle válida.
	 * 
	 * @author <a href="mailto:everton@javafree.org">Everton Barros</a>
	 * @param value
	 *            Key de bundle que será testada.
	 * @return Retorna se é uma key válida ou não.
	 */
	public static boolean isValidKey(String value) {

		String regex = "^[a-z]+(\\.[a-z][a-zA-Z0-9]*)+(\\.[a-z][a-zA-Z0-9]*)*";
		return value.matches(regex);
	}

	/**
	 * Monta uma chave de bundle específica para exceções. Usa a recomendação do
	 * guia de nomenclatura web para definir este tipo de chave.
	 * 
	 * @author <a href="mailto:everton@javafree.org">Everton Barros</a>
	 * @param keybase
	 *            Chave base da exceção. Normalmente, se passa somente o
	 *            "<subsystem>.<page>".
	 * @param e
	 *            Exceção sobre a qual será montado a chave.
	 * @return Retorna o chave de bundle referente a exceção passada.
	 */
	public static String assembleExceptionKey(String keybase, Exception e) {

		return keybase + ".exception." + e.getClass().getSimpleName();
	}

	/**
	 * Recupera o class loader da thread corrente.
	 * 
	 * @author <a href="mailto:everton@javafree.org">Everton Barros</a>
	 * @return Class loader corrente.
	 */
	private static ClassLoader getClassLoader() {

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		if (classLoader == null) {
			return ResourceBundleUtil.class.getClassLoader();
		}
		return classLoader;
	}

}
