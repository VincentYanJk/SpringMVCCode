package com.pwc.springMVC.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pwc.springMVC.annotation.Controller;
import com.pwc.springMVC.annotation.RequestMapping;
import com.pwc.springMVC.util.ClassScanner;

/**
 * Servlet implementation class DispatchSeverlet
 */
/*
 * @WebServlet( urlPatterns = { "/severletTesting" }, initParams = {
 * 
 * @WebInitParam(name = "basePackage", value = "com.pwc.springMVC", description
 * = "scanner URL") })
 */
public class CustomDispatchSeverlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log=LoggerFactory.getLogger(CustomDispatchSeverlet.class);
	
	private Map<String, Object> controllers = new HashMap<String, Object>();
	private Map<String, Method> methods = new HashMap<String, Method>();

	/**
	 * Default constructor.
	 */
	public CustomDispatchSeverlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		log.debug("init Servlet to pulling target(Controllers and Mtethods)");
		
		Map<String, Class<?>> results = new HashMap<String, Class<?>>();
		results = ClassScanner.scannerClass(config.getInitParameter("basePackage"));
		log.debug("Loop the results get from util ClassScanner to create mapping relationship for Controllers(class name -> class's instance), Methods(requestMapping path -> method)");
		Iterator<String> keys = results.keySet().iterator();

		while (keys.hasNext()) {

			String key = keys.next();
			Class clazz = results.get(keys.next());

			if (clazz.isAnnotationPresent(Controller.class)) {
				System.out.println(clazz.getName() + " this class is marked Controller.");

				String path = "";
				if (clazz.isAnnotationPresent(RequestMapping.class)) {
					RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
					path = requestMapping.value();
				}

				try {
					controllers.put(clazz.getName(), clazz.newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				Method[] ms = clazz.getMethods();

				for (Method method : ms) {

					if (!method.isAnnotationPresent(RequestMapping.class)) {
						continue;
					}

					System.out.println("print each of url of method: " + path
							+ method.getAnnotation(RequestMapping.class).value());

					methods.put(path + method.getAnnotation(RequestMapping.class).value(), method);
				}
			}

		}
		
		log.debug("End Servlet to pulling target(Controllers and Mtethods)");

	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// /springMVC/abc.do
		String url = req.getRequestURI();
		String contexPath = req.getContextPath();
		String mappingPath = url.substring(url.indexOf(contexPath) + contexPath.length(), url.indexOf(".do"));

		Method method = methods.get(mappingPath);
		Object controller = controllers.get(method.getDeclaringClass().getName());
		try {
			method.invoke(controller);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
