package org.springframework.web.servlet.handler;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * A {@link HandlerExceptionResolver} that delegates to a list of other {@link HandlerExceptionResolver}s.
 *
 * @author Rossen Stoyanchev
 * @since 3.1
 */
public class HandlerExceptionResolverComposite implements HandlerExceptionResolver, Ordered {

	private List<HandlerExceptionResolver> resolvers;

	private int order = Ordered.LOWEST_PRECEDENCE;

	public void setOrder(int order) {
		this.order = order;
	}

	public int getOrder() {
		return this.order;
	}

	/**
	 * Set the list of exception resolvers to delegate to. 
	 */
	public void setExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		this.resolvers = exceptionResolvers;
	}

	/**
	 * Return the list of exception resolvers to delegate to.
	 */
	public List<HandlerExceptionResolver> getExceptionResolvers() {
		return Collections.unmodifiableList(resolvers);
	}

	/**
	 * Resolve the exception by iterating over the list of configured exception resolvers. 
	 * The first one to return a ModelAndView instance wins. Otherwise {@code null} is returned.
	 */
	public ModelAndView resolveException(HttpServletRequest request,
										 HttpServletResponse response,
										 Object handler,
										 Exception ex) {
		if (resolvers != null) {
			for (HandlerExceptionResolver handlerExceptionResolver : resolvers) {
				ModelAndView mav = handlerExceptionResolver.resolveException(request, response, handler, ex);
				if (mav != null) {
					return mav;
				}
			}
		}
		return null;
	}

}