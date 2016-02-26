package br.com.tnt.ssg.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

public class FacesUtils {

	/*
	 * Este metodo recebe uma classe de enum e um valor key e retorna o valor
	 * I18n.
	 */
	public static String internationalizeEnum(Class<?> clazz, Enum<?> key) {
		return FacesUtils.getMessageByKey(clazz.getSimpleName() + "." + key);
	}

	/**
	 * Retorna o contexto do servlet.
	 * 
	 * @author <a href="mailto:everton@javafree.org">Everton Barros</a>
	 * @return O contexto do servlet
	 */
	public static ServletContext getServletContext() {
		return (ServletContext) getFacesContext().getExternalContext()
				.getContext();
	}

	/**
	 * Retorna a request do contexto externo do faces.
	 * 
	 * @author <a href="mailto:everton@javafree.org">Everton Barros</a>
	 * @return Request do contexto externo do faces.
	 */
	public static HttpServletRequest getRequestScope() {
		return (HttpServletRequest) getFacesContext().getExternalContext()
				.getRequest();
	}

	/**
	 * Retorna o response do contexto externo do faces.
	 * 
	 * @author <a href="mailto:everton@javafree.org">Everton Barros</a>
	 * @return Request do contexto externo do faces.
	 */
	public static HttpServletResponse getResponseScope() {
		return (HttpServletResponse) getFacesContext().getExternalContext()
				.getResponse();
	}

	/**
	 * Retorna a sessão do contexto externo do faces.
	 * 
	 * @author <a href="mailto:everton@javafree.org">Everton Barros</a>
	 * @return HttpSession do contexto externo do faces.
	 */
	public static HttpSession getSessionScope() {
		return getRequestScope().getSession();
	}

	/**
	 * Este metodo recebe uma classe de enum e um valor I18n e retorna o enum
	 * correspondente. Funciona ao contrario do metodo internacionalizeEnum.
	 * Sera util quando o combo foi populado com o metodo
	 * enumSelectItemsWithI18nValue.
	 * 
	 */
	@SuppressWarnings(value = "rawtypes")
	public static Enum unInternationalizeEnum(Class<? extends Enum> type,
			String I18nValue) {

		for (Enum e : type.getEnumConstants()) {
			if (I18nValue.equals(FacesUtils.getMessageByKey(type
					.getSimpleName() + "." + e.name()))) {
				return e;
			}
		}

		return null;
	}

	public static String getMessageByKey(String key) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

		try {
			return resourceBundle.getString(key);
		} catch (Exception e) {
			return key;
		}
	}

	@SuppressWarnings("rawtypes")
	public static List<SelectItem> enumSelectItens(Class<? extends Enum> type,
			boolean sort) {
		ArrayList<SelectItem> result = new ArrayList<SelectItem>();

		for (Enum e : type.getEnumConstants()) {
			result.add(new SelectItem(e, FacesUtils.getMessageByKey(type
					.getSimpleName() + "." + e.name())));
		}

		if (sort) {
			Collections.sort(result, new Comparator<SelectItem>() {

				@Override
				public int compare(SelectItem item1, SelectItem item2) {
					return item1.getLabel().compareTo(item2.getLabel());
				}

			});
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	public static List<SelectItem> enumSelectItens(Class<? extends Enum> type) {
		return enumSelectItens(type, true);
	}

	@SuppressWarnings("rawtypes")
	public static List<SelectItem> enumSelectItens(Class<? extends Enum> type,
			Object... excluded) {
		List<Object> excludedList = Arrays.asList(excluded);

		ArrayList<SelectItem> result = new ArrayList<SelectItem>();
		for (Enum e : type.getEnumConstants()) {
			if (!excludedList.contains(e)) {
				result.add(new SelectItem(e, FacesUtils.getMessageByKey(type
						.getSimpleName() + "." + e.name())));
			}
		}

		Collections.sort(result, new Comparator<SelectItem>() {

			@Override
			public int compare(SelectItem item1, SelectItem item2) {
				return item1.getLabel().compareTo(item2.getLabel());
			}

		});

		return result;
	}

	public static <T> T findManagedBean(Class<T> beanClass) {
		FacesContext faces = FacesContext.getCurrentInstance();

		ManagedBean mbAnnotation = beanClass.getAnnotation(ManagedBean.class);

		String expression = null;
		if (mbAnnotation.name() != null && !mbAnnotation.name().isEmpty()) {
			expression = "#{" + mbAnnotation.name() + "}";
		} else {
			expression = "#{" + beanClass.getSimpleName() + "}";
		}

		T managedBean = (T) faces.getApplication().evaluateExpressionGet(faces,
				expression, beanClass);

		return managedBean;
	}

	public static Object getValueExpression(String expression) {
		if (expression == null) {
			return null;
		}

		if (expression.trim().indexOf("#") != 0) {
			expression = "#{" + expression + "}";
		}

		FacesContext faces = FacesContext.getCurrentInstance();

		return faces.getApplication().evaluateExpressionGet(faces, expression,
				Object.class);
	}

	public static void setValueExpression(String expression, Object newValue) {
		if (expression == null) {
			return;
		}

		if (expression.trim().indexOf("#") != 0) {
			expression = "#{" + expression + "}";
		}

		FacesContext faces = FacesContext.getCurrentInstance();
		ExpressionFactory elFactory = faces.getApplication()
				.getExpressionFactory();
		ELContext elContext = faces.getELContext();
		ValueExpression valueExp = elFactory.createValueExpression(elContext,
				expression, Object.class);
		Class<?> bindClass = valueExp.getType(elContext);

		if (bindClass.isPrimitive() || bindClass.isInstance(newValue)) {
			valueExp.setValue(elContext, newValue);
		}
	}

	public static void redirect(String page) {
		try {
			String contextPath = (String) FacesContext.getCurrentInstance()
					.getExternalContext().getRequestContextPath();
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(contextPath + page);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public static String getViewRoot() {
		return getFacesContext().getViewRoot().getViewId();
	}

	/**
	 * Recupera a localidade da aplicação, ou seja, se atualmente está sendo
	 * utilizada a lingua português, inglês ou espanhol, por exemplo.
	 * 
	 * @author <a href="mailto:everton@javafree.org">Everton Barros</a>
	 * @return Retorna a localidade corrente.
	 */
	public static Locale getLocale() {
		return FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}

	public static void cleanSubmittedValues(UIComponent component) {
		if (component instanceof EditableValueHolder) {
			EditableValueHolder evh = (EditableValueHolder) component;
			evh.setSubmittedValue(null);
			evh.setValue(null);
			evh.setLocalValueSet(false);
			evh.setValid(true);
		}
		if (component.getChildCount() > 0) {
			for (UIComponent child : component.getChildren()) {
				cleanSubmittedValues(child);
			}
		}
	}

	public static void limparForm(String formId) {
		FacesContext ctx = FacesUtils.getFacesContext();
		UIViewRoot view = ctx.getViewRoot();
		UIForm form = (UIForm) view.findComponent(formId != null ? formId
				: "formEdicao");

		if (form != null) {
			FacesUtils.cleanSubmittedValues(form);
		}
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<? extends Enum> enumArrayList(Class<? extends Enum> type) {
		ArrayList<Enum> result = new ArrayList<Enum>();
		for (Enum e : type.getEnumConstants()) {
			result.add(e);
		}
		return result;
	}

	/**
	 * 
	 * @param type
	 * @param excluded
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<? extends Enum> enumArrayList(
			Class<? extends Enum> type, Object... excluded) {

		List<Object> excludedList = Arrays.asList(excluded);
		ArrayList<Enum> result = new ArrayList<Enum>();

		for (Enum e : type.getEnumConstants()) {
			if (!excludedList.contains(e)) {
				result.add(e);
			}
		}

		return result;
	}

	/**
	 * 
	 * @param level
	 * @param msg
	 */
	public static void addFacesMessage(Severity severity, String summary,
			String detail) {
		addFacesMessage(null, severity, summary, detail);
	}

	/**
	 * 
	 * @param clientId
	 * @param level
	 * @param msg
	 */
	public static void addFacesMessage(String clientId, Severity severity,
			String summary, String detail) {
		getFacesContext().addMessage(null,
				new FacesMessage(severity, summary, detail));
	}

	/**
	 * 
	 * @return
	 */
	public static boolean hasFacesMessage() {
		return !getFacesContext().getMessageList().isEmpty();
	}

	/**
	 * 
	 * @return
	 */
	public static boolean hasMessageError() {
		boolean result = false;
		for (FacesMessage msg : getFacesContext().getMessageList()) {
			if (msg.getSeverity().equals(FacesMessage.SEVERITY_ERROR)) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * Recebe uma lista de beans como parametro e devolve uma lista de
	 * selectItem do JSF
	 * 
	 * @author <a href="mailto:everton@javafree.org">Everton Barros</a>
	 * @param lista
	 *            = Uma lista de beans qualquer.
	 * @param fieldLabel
	 *            = O atributo dos beans que serao exibidos no label do
	 *            selectItem
	 * @return Retorna uma lista de SelectItem para ser usado em paginas JSF
	 */
	public static List<SelectItem> getSelectItems(List<? extends Object> lista,
			String... fieldLabel) {
		return createSelectItemListwithSeparator(lista, " ", fieldLabel);
	}

	/**
	 * Recebe uma lista de beans como parametro e devolve uma lista de
	 * selectItem do JSF
	 * 
	 * @author <a href="mailto:everton@javafree.org">Everton Barros</a>
	 * @param lista
	 *            = Uma lista de beans qualquer.
	 * @param fieldLabel
	 *            = O atributo dos beans que serao exibidos no label do
	 *            selectItem
	 * @param separator
	 *            = O caracter de separacao entre os atributos exibidos,
	 *            exemplos: ' - ' ou ' : ' ou ' / '
	 * @return Retorna uma lista de SelectItem para ser usado em paginas JSF
	 */
	public static List<SelectItem> createSelectItemListwithSeparator(
			List<? extends Object> lista, String separator,
			String... fieldLabel) {
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		Iterator<? extends Object> iter = lista.iterator();
		while (iter.hasNext()) {
			Object element = iter.next();
			try {
				SelectItem selectItem = null;
				List<String> fields = new ArrayList<String>();
				for (String field : fieldLabel) {
					BeanUtils.getNestedProperty(element, field);
					Object label = BeanUtils.getNestedProperty(element, field);
					if (label != null) {
						fields.add(String.valueOf(label));
					} else {
						fields.add("");
					}
				}
				StringBuffer concat = new StringBuffer();
				for (String field : fields) {
					concat.append(field + separator);
				}
				String pronta = concat.substring(0,
						concat.length() - separator.length());
				if (element != null) {
					selectItem = new SelectItem(element, pronta);
				} else {
					selectItem = new SelectItem("", "");
				}
				retorno.add(selectItem);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return retorno;
	}

	/**
	 * Retorna os parametros da requisicao da URL.
	 * 
	 * @author <a href="mailto:everton@javafree.org">Everton Barros</a>
	 * @return Map com par de chaves e valores da requisicao.
	 */
	public static Map<String, String> getRequestParam() {
		return getFacesContext().getExternalContext().getRequestParameterMap();
	}

	/**
	 * Adiciona uma mensagem de erro específica para um componente.
	 * 
	 * @author <a href="mailto:everton@javafree.org">Everton Barros</a>
	 * @param clientId
	 *            O client id do componente.
	 * @param key
	 *            de bundle da mensagem.
	 */
	public static void addErrorMessage(String clientId, String key) {
		String keyvalue = ResourceBundleUtil.getKey(getLocale(), key);
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				keyvalue, keyvalue);
		FacesContext.getCurrentInstance().addMessage(clientId, facesMsg);
	}

	/**
	 * 
	 * @param navigationRule
	 */
	public static void redirectHandleNavigation(String navigationRule) {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		NavigationHandler navigationHandler = application
				.getNavigationHandler();
		navigationHandler.handleNavigation(facesContext, null, navigationRule);
		facesContext.renderResponse();
	}
}
