package com.device.manage.web.service;

import com.device.api.entity.UserOnline;
import com.device.api.entity.backrole.BackUser;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Description:
 * @Date: 2018-03-22 11:04
 */
@Service
public class SessionService {

    @Autowired
    private SessionDAO sessionDAO;

    public List<UserOnline> list() {
        List<UserOnline> list = new ArrayList<>();
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        for (Session session : sessions) {
            UserOnline userOnline = new UserOnline();
            BackUser user = new BackUser();
            SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
                continue;
            } else {
                principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                user = (BackUser) principalCollection.getPrimaryPrincipal();
                userOnline.setUserName(user.getUserName());
                userOnline.setUserId(user.getId().toString());
            }
            userOnline.setId((String) session.getId());
            userOnline.setHost(session.getHost());
            userOnline.setStartTimestamp(session.getStartTimestamp());
            userOnline.setLastAccessTime(session.getLastAccessTime());
            Long timeout = session.getTimeout();
            userOnline.setStatus(timeout == 0l ? "0" : "1");
            userOnline.setTimeOut(timeout);
            list.add(userOnline);
        }
        return list;
    }

    public boolean forceLogout(String sessionId) {
        Session session = sessionDAO.readSession(sessionId);
        if (session != null) {
            session.setTimeout(0);
        }
        return true;
    }

}
