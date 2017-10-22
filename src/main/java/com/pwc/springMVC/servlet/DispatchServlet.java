package com.pwc.springMVC.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pwc.springMVC.annotation.Controller;
import com.pwc.springMVC.annotation.RequestMapping;
import com.pwc.springMVC.util.ClassScanner;

/**
 * Servlet implementation class DispatchServlet
 */
// for below's annotation information can be config into web.xml as well.
// @WebServlet(urlPatterns= {"*.do"},initParams= {@WebInitParam(name =
// "basePackage",value="com.pwc.springMVC")})
public class DispatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> controllers = new HashMap<String, Object>();
	private Map<String, Method> methods = new HashMap<String, Method>();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DispatchServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {

		Map<String, Class<?>> results = new HashMap<String, Class<?>>();
		results = ClassScanner.scannerClass(config.getInitParameter("basePackage"));

		Iterator<String> keys = results.keySet().iterator();

		while (keys.hasNext()) {
			Class clazz = results.get(keys.next());

			if (clazz.isAnnotationPresent(Controller.class)) {
				System.out.println(clazz.getName() + " this class is marked Controller.");

				String path = "";
				if (clazz.isAnnotationPresent(RequestMapping.class)) {
					RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
					path = requestMapping.value();
				}
				
				try {
					controllers.put(keys.next(), clazz.newInstance());
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
					
					methods.put(path+method.getAnnotation(RequestMapping.class).value(), method);
				}
			}
			
			System.out.println("8888");

		}
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
/*	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			System.out.println("99999");
			System.out.println("10000");
		    System.out.println(request);

	}*/

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
		System.out.println("1000");
	}

}
