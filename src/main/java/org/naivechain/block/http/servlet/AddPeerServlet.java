package org.naivechain.block.http.servlet;

import org.naivechain.block.common.factory.ServiceFactory;
import org.naivechain.block.p2p.P2PService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 连接到某个指定的节点
 *
 * @author kangshaofei
 * @date 2018-08-19
 */
public class AddPeerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        String peer = req.getParameter("peer");
        ServiceFactory.getService(P2PService.class).connectToPeer(peer);
        resp.getWriter().print("ok");
    }
}