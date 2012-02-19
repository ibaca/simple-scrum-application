package org.inftel.ssa.web.auth;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 *
 * @author ibaca
 */
public class SsaCredentialsMatcher extends SimpleCredentialsMatcher {

	private final static Logger logger = Logger.getLogger(SsaCredentialsMatcher.class.getName());

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		logger.log(Level.INFO, "doCredentialsMatch entre {0} y {1}", new Object[]{new String((char[]) token.getCredentials()), info.getCredentials()});
		return super.doCredentialsMatch(token, info);
	}
}
