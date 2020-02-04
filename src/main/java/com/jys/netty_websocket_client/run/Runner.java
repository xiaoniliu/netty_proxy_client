package com.jys.netty_websocket_client.run;


import com.jys.netty_websocket_client.client.NettyWebSocketClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

//容器加载完成后自启动
@Component
public class Runner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new NettyWebSocketClient().start();
    }

}
