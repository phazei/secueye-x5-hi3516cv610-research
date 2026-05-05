package com.aliyun.alink.linksdk.channel.core.persistent.mqtt.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.PKIXParameters;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/* JADX INFO: compiled from: MqttTrustManager.java */
/* JADX INFO: loaded from: classes2.dex */
public class d implements X509TrustManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final X509TrustManager f4147a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final KeyStore f4148b;

    public d(InputStream inputStream) throws NoSuchAlgorithmException, KeyStoreException {
        this.f4148b = a(inputStream);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
        trustManagerFactory.init((KeyStore) null);
        this.f4147a = (X509TrustManager) trustManagerFactory.getTrustManagers()[0];
    }

    public final KeyStore a(InputStream inputStream) throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);
        X509Certificate x509Certificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(inputStream);
        keyStore.setCertificateEntry(x509Certificate.getSubjectX500Principal().getName(), x509Certificate);
        return keyStore;
    }

    public final X509Certificate[] b(X509Certificate[] x509CertificateArr) {
        X509Certificate[] x509CertificateArr2 = new X509Certificate[x509CertificateArr.length];
        List<X509Certificate> listAsList = Arrays.asList(x509CertificateArr);
        int length = x509CertificateArr.length - 1;
        X509Certificate x509CertificateA = a(listAsList);
        x509CertificateArr2[length] = x509CertificateA;
        while (true) {
            x509CertificateA = a(x509CertificateA, listAsList);
            if (x509CertificateA == null || length <= 0) {
                break;
            }
            length--;
            x509CertificateArr2[length] = x509CertificateA;
        }
        return x509CertificateArr2;
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        a(x509CertificateArr);
        try {
            this.f4147a.checkServerTrusted(x509CertificateArr, str);
        } catch (CertificateException e) {
            try {
                X509Certificate[] x509CertificateArrB = b(x509CertificateArr);
                CertPathValidator certPathValidator = CertPathValidator.getInstance("PKIX");
                CertPath certPathGenerateCertPath = CertificateFactory.getInstance("X509").generateCertPath(Arrays.asList(x509CertificateArrB));
                PKIXParameters pKIXParameters = new PKIXParameters(this.f4148b);
                pKIXParameters.setRevocationEnabled(false);
                certPathValidator.validate(certPathGenerateCertPath, pKIXParameters);
            } catch (CertificateNotYetValidException e2) {
                com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttTrustManager", "CertificateNotYetValidException " + e2);
            } catch (Exception e3) {
                if (e3.getCause() instanceof CertificateNotYetValidException) {
                    com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttTrustManager", "validate cert failed.because system is early than cert valid . wsf will ignore this exception," + e3);
                    return;
                }
                com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttTrustManager", "checkServerTrusted faied." + e);
                com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttTrustManager", "validate cert failed." + e3);
                throw e;
            }
        }
    }

    @Override // javax.net.ssl.X509TrustManager
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public final void a(X509Certificate[] x509CertificateArr) throws CertificateNotYetValidException, CertificateExpiredException {
        if (x509CertificateArr != null) {
            if (x509CertificateArr.length >= 0) {
                for (X509Certificate x509Certificate : x509CertificateArr) {
                    x509Certificate.checkValidity();
                }
                return;
            }
            throw new IllegalArgumentException("check Server x509Certificates is empty");
        }
        throw new IllegalArgumentException("check Server x509Certificates is null");
    }

    public final X509Certificate b(X509Certificate x509Certificate, List<X509Certificate> list) {
        for (X509Certificate x509Certificate2 : list) {
            if (x509Certificate2.getSubjectDN().equals(x509Certificate.getIssuerDN())) {
                return x509Certificate2;
            }
        }
        return null;
    }

    public final X509Certificate a(List<X509Certificate> list) {
        Iterator<X509Certificate> it = list.iterator();
        while (it.hasNext()) {
            X509Certificate next = it.next();
            X509Certificate x509CertificateB = b(next, list);
            if (x509CertificateB == null || x509CertificateB.equals(next)) {
                return next;
            }
        }
        return null;
    }

    public final X509Certificate a(X509Certificate x509Certificate, List<X509Certificate> list) {
        for (X509Certificate x509Certificate2 : list) {
            if (x509Certificate2.getIssuerDN().equals(x509Certificate.getSubjectDN()) && !x509Certificate2.equals(x509Certificate)) {
                return x509Certificate2;
            }
        }
        return null;
    }
}
