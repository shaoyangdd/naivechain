package org.naivechain.block.http.servlet;

import org.java_websocket.WebSocket;
import org.naivechain.block.common.factory.ServiceFactory;
import org.naivechain.block.p2p.P2PService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 节点服务类 获取连接到本节点的其它节点
 *
 * @author kangshaofei
 * @date 2019-08-19
 */
public class PeersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        for (WebSocket socket : ServiceFactory.getService(P2PService.class).getSockets()) {
            InetSocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
            resp.getWriter().print(remoteSocketAddress.getHostName() + ":" + remoteSocketAddress.getPort());
        }
    }
}