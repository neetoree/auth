package com.example.examplemod;

import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Alexander <iamtakingiteasy> Tumin on 2016-12-18.
 */
public class AuthHandler implements IMessageHandler<AuthMessage, IMessage> {
    public AuthHandler() {
    }

    @Override
    public IMessage onMessage(AuthMessage message, MessageContext ctx) {
        try {
            URL url = new URL("https://neetoree.org/user/check/" + message.getAuthToken());
            String reply = new String(ByteStreams.toByteArray(url.openConnection().getInputStream()), Charset.forName("UTF-8"));
            if ("true".equals(reply)) {
                NEEToreeAuth.FUTURES.remove(ctx.getServerHandler().playerEntity).cancel(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
