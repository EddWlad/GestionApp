package com.tidsec.devengacion_inventarios.config;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import com.tidsec.devengacion_inventarios.service.IPermitsService;

@Component
public class PermitAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

	@Autowired
	private IPermitsService permitService;

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
		Authentication auth = authentication.get();

		if (auth != null && auth.isAuthenticated()) {
			String requestedUrl = object.getRequest().getRequestURI();
			String method = object.getRequest().getMethod();

			boolean hasPermission = permitService.checkPermission(requestedUrl, method, auth.getName());
			return new AuthorizationDecision(hasPermission);
		}

		return new AuthorizationDecision(false);
	}

}
