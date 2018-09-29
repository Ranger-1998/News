package com.example.chen.news.network;

import android.content.Context;

import com.example.chen.news.view.activity.ActivityManager;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * okHttp的管理类
 * 单一实例,所以其中的 OkHttpClient 也是单一实例
 * 类名前不加public,因为该类只在自己所在的包使用
 *
 * @see #getHttpClient() 获取一个OkHttpClient 实例,通常情况下该方法返回一个添加了证书的OkHttpClient实例
 * 当在读取证书产生IO异常,或者产生安全方面的异常时,将返回一个不带证书的kHttpClient实例
 *
 * Created by chen on 2018/9/15.
 */

class NetworkManager {
    private static NetworkManager mInstance;    //单一实例
    private OkHttpClient mHttpClient;           //单一实例

    private static final int CONNECT_TIMEOUT = 30;   //连接超时(秒)
    private static final int READ_TIMEOUT = 30;      //响应超时(秒)

    private NetworkManager() {

    }

    /**
     * 整个对象是单一实例,所以使用synchronized来保持同步
     *
     * @return 返回NetworkManager的单一实例
     */
    static synchronized NetworkManager getNetWorkManager() {
        if (mInstance == null) {
            mInstance = new NetworkManager();
        }
        return mInstance;
    }

    /**
     * 整个对象是单一实例,所以使用synchronized来保持同步
     *
     * @return 返回一个OkHttpClient实例, 这个实例应该是唯一的
     */
    synchronized OkHttpClient getHttpClient() {
        if (mHttpClient == null) {
            mHttpClient = newOkHttpClient();
        }
        return mHttpClient;
    }

    /**
     * 获取一个OkHttpClient实例
     *
     * @return OkHttpClient
     */
    private OkHttpClient newOkHttpClient() {
        Context context = ActivityManager.getActivityManager().peekActivity();
        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory;
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open("srca.cer");     //证书输入流
            trustManager = trustManagerForCertificates(inputStream);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
            return new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, trustManager)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)    //设置连接超时
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)          //设置响应超时
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return new OkHttpClient.Builder().build();  //读取证书发生IO异常,返回没有证书的OkHttpClient
        } catch (GeneralSecurityException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            return new OkHttpClient.Builder().build();  //发生安全类异常,返回一个没有证书的OkHttpClient
        }
    }

    /**
     * Returns a trust manager that trusts {@code certificates} and none other. HTTPS services whose
     * certificates have not been signed by these certificates will fail with a {@code
     * SSLHandshakeException}.
     * <p>
     * This can be used to replace the host platform's built-in trusted certificates with a custom
     * set. This is useful in development where certificate authority-trusted certificates aren't
     * available. Or in production, to avoid reliance on third-party certificate authorities.
     * <p>
     * Warning: Customizing Trusted Certificates is Dangerous!
     * <p>
     * Relying on your own trusted certificates limits your server team's ability to update their
     * TLS certificates. By installing a specific set of trusted certificates, you take on additional
     * operational complexity and limit your ability to migrate between certificate authorities. Do
     * not use custom trusted certificates in production without the blessing of your server's TLS
     * administrator.
     */
    private X509TrustManager trustManagerForCertificates(InputStream inputStream)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates =
                certificateFactory.generateCertificates(inputStream);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }
        char[] password = "password".toCharArray();
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }
        KeyManagerFactory keyManagerFactory = KeyManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            //添加自定义密码,使用默认密码
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, password);      // By convention, 'null' creates an empty key store.
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
