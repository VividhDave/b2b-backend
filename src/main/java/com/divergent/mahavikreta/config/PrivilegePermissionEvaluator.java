package com.divergent.mahavikreta.config;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import com.divergent.mahavikreta.constants.AppConstants;
import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.exception.GenricException;


@Component
public class PrivilegePermissionEvaluator implements PermissionEvaluator {

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object object) {

		if ((authentication == null) || !(object instanceof String)) {
			throw new GenricException("Either authentication object null or permission is not instance of string");
		}

		return hasPrivilege(authentication, object.toString());
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String privilege,
			Object object) {

		if ((authentication == null) || !(object instanceof String)) {
			throw new GenricException("Either authentication object null or permission is not instance of string");
		}
		return hasPrivilege(authentication, privilege);
	}

	@SuppressWarnings("unchecked")
	private boolean hasPrivilege(Authentication authentication, String object) {

		if (object.trim().isEmpty()) {
			throw new GenricException(MsgConstants.ERROR_PRIVILEGE_NOT_FOUND);
		}

		Object details = authentication.getDetails();
		if (!(details instanceof OAuth2AuthenticationDetails)) {
			return false;
		}

		OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) details;

		Map<String, Object> userMap = (Map<String, Object>) oAuth2AuthenticationDetails.getDecodedDetails();

		List<String> privileges = (List<String>) userMap.get(AppConstants.PRIVILEGES);

		if (privileges == null || privileges.isEmpty()) {
			return false;
		}

		return privileges.stream().anyMatch(privilege -> privilege.equals(object));
	}

}