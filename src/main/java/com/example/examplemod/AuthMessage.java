package com.example.examplemod;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

/**
 * Created by Alexander <iamtakingiteasy> Tumin on 2016-12-18.
 */
public class AuthMessage implements IMessage {
    private String authToken;

    public AuthMessage() {
    }

    public AuthMessage(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        authToken = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, authToken);
    }

}
