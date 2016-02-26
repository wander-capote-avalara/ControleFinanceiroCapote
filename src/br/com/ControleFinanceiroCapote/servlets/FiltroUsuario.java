package br.com.ControleFinanceiroCapote.servlets;

import java.io.IOException; 
import javax.servlet.Filter; 
import javax.servlet.FilterChain; 
import javax.servlet.FilterConfig; 
import javax.servlet.ServletException; 
import javax.servlet.ServletRequest; 
import javax.servlet.ServletResponse; 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FiltroUsuario implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String context = request.getServletContext().getContextPath()+"/Login.html";

		try {			

			HttpSession sessao = ((HttpServletRequest) request).getSession();

			if (((HttpServletRequest) request).getRequestURI().equals(context) || sessao.getAttribute("login") != null) {
				sessao.setAttribute("msg", "Usu�rio logado com sucesso!");
				chain.doFilter(request, response);
			} else {
				sessao.setAttribute("msg", "VOC� NAUM TEM PERMISSAAAAUMM 9NHO!");
				((HttpServletResponse) response).sendRedirect(context);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}

}
