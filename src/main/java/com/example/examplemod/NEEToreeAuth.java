package com.example.examplemod;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.*;

@Mod(modid = NEEToreeAuth.MODID, version = NEEToreeAuth.VERSION, acceptableRemoteVersions = "*")
public class NEEToreeAuth {
    public static final String MODID = "neetoreeauth";
    public static final String VERSION = "1.0";
    public static SimpleNetworkWrapper INSTANCE;
    public static int ID = 0;

    public static final Map<EntityPlayerMP, ScheduledFuture<?>> FUTURES = new ConcurrentHashMap<EntityPlayerMP, ScheduledFuture<?>>();

    private static final ScheduledExecutorService sched = Executors.newSingleThreadScheduledExecutor();

    public NEEToreeAuth() throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext ssl = SSLContext.getInstance("SSL");
        ssl.init(null, new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(ssl.getSocketFactory());
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        FMLCommonHandler.instance().bus().register(this);
        INSTANCE.registerMessage(AuthHandler.class, AuthMessage.class, ID++, Side.SERVER);
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        final EntityPlayerMP mp = (EntityPlayerMP) event.player;
        ScheduledFuture<?> schedule = sched.schedule(new Runnable() {
            @Override
            public void run() {
                mp.playerNetServerHandler.kickPlayerFromServer("NEEToree is not pleased with you");
            }
        }, 10, TimeUnit.SECONDS);
        FUTURES.put(mp, schedule);
    }

    @SubscribeEvent
    public void onPlayerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        sched.schedule(new Runnable() {
            @Override
            public void run() {
                INSTANCE.sendToServer(new AuthMessage(System.getProperty("authToken")));
            }
        }, 5, TimeUnit.SECONDS);
    }
}
