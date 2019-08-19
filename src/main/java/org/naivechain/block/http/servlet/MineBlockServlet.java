package org.naivechain.block.http.servlet;

import com.alibaba.fastjson.JSON;
import org.naivechain.block.common.factory.ServiceFactory;
import org.naivechain.block.mine.Block;
import org.naivechain.block.mine.BlockService;
import org.naivechain.block.p2p.P2PService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 挖矿服务servlet
 *
 * @author kangshaofei
 * @date 2018-08-19
 */
public class MineBlockServlet extends HttpServlet {

    private BlockService blockService = ServiceFactory.getService(BlockService.class);

    private P2PService p2pService = ServiceFactory.getService(P2PService.class);

    private static final Logger logger = LoggerFactory.getLogger(MineBlockServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        String data = req.getParameter("data");
        Block newBlock = blockService.generateNextBlock(data);
        blockService.addBlock(newBlock);
        p2pService.broatcast(p2pService.responseLatestMsg());
        String s = JSON.toJSONString(newBlock);
        logger.info("block added: " + s);
        resp.getWriter().print(s);
    }
}