package org.naivechain.block.http.servlet;

import com.alibaba.fastjson.JSON;
import org.naivechain.block.common.factory.ServiceFactory;
import org.naivechain.block.mine.BlockService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 区块服务servlet
 *
 * @author kangshaofei
 * @date 2018-08-19
 */
public class BlocksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(JSON.toJSONString(ServiceFactory.getService(BlockService.class).getBlockChain()));
    }
}