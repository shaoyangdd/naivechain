package org.naivechain.block;

import com.alibaba.fastjson.JSON;
import org.naivechain.block.http.HTTPService;
import org.naivechain.block.mine.BlockService;
import org.naivechain.block.p2p.P2PService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 主启动方法，启动此类可以启动整个系统
 *
 * @author kangshaofei
 * @date 2019-08-18
 */
public class MainStartup {

    private static final Logger logger = LoggerFactory.getLogger(MainStartup.class);

    /**
     * TODO 目前只支持传参数的方式根其它节点建立连接，后期需要自动建立
     * 示例，比如启动第一个，参数为：8080 6001，启动第二个跟第一个建立连接：8081 7002 ws://localhost:6001
     *
     * @param args 启动参数 第一个参数为http服务占用的端口，第二个参数为P2P占用的端口，第三个参数为
     */
    public static void main(String[] args) {
        logger.info("启动开始，启动参数:{}", JSON.toJSONString(args));
        if (args != null && (args.length == 2 || args.length == 3)) {
            try {
                int httpPort = Integer.valueOf(args[0]);
                int p2pPort = Integer.valueOf(args[1]);
                BlockService blockService = new BlockService();
                P2PService p2pService = new P2PService(blockService);
                p2pService.initP2PServer(p2pPort);
                if (args.length == 3 && args[2] != null) {
                    logger.info("连接节点:{}", args[2]);
                    p2pService.connectToPeer(args[2]);
                }
                HTTPService httpService = new HTTPService(blockService, p2pService);
                httpService.initHTTPServer(httpPort);
            } catch (Exception e) {
                logger.info("启动失败:" + e.getMessage());
            }
        } else {
            logger.info("使用参数: java -jar naivechain.jar 8080 6001");
        }
        logger.info("启动成功");
    }
}
