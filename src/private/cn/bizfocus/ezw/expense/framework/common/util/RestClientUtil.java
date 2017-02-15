package cn.bizfocus.ezw.expense.framework.common.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class RestClientUtil {

	 public static KeyStore getKeyStore(String password, String keyStorePath)  
	            throws Exception {  
	        // ʵ������Կ��  
	        KeyStore ks = KeyStore.getInstance("JKS");  
	        // �����Կ���ļ���  
	        FileInputStream is = new FileInputStream(keyStorePath);  
	        // ������Կ��  
	        ks.load(is, password.toCharArray());  
	        // �ر���Կ���ļ���  
	        is.close();  
	        return ks;  
	    }  
	  
	    /** 
	     * ���SSLSocketFactory. 
	     * @param password 
	     *            ���� 
	     * @param keyStorePath 
	     *            ��Կ��·�� 
	     * @param trustStorePath 
	     *            ���ο�·�� 
	     * @return SSLSocketFactory 
	     * @throws Exception 
	     */  
	    public static SSLContext getSSLContext(String password,  
	            String keyStorePath, String trustStorePath) throws Exception {  
	        // ʵ������Կ��  
	        KeyManagerFactory keyManagerFactory = KeyManagerFactory  
	                .getInstance(KeyManagerFactory.getDefaultAlgorithm());  
	        // �����Կ��  
	        KeyStore keyStore = getKeyStore(password, keyStorePath);  
	        // ��ʼ����Կ����  
	        keyManagerFactory.init(keyStore, password.toCharArray());  
	  
	        // ʵ�������ο�  
	        TrustManagerFactory trustManagerFactory = TrustManagerFactory  
	                .getInstance(TrustManagerFactory.getDefaultAlgorithm());  
	        // ������ο�  
	        KeyStore trustStore = getKeyStore(password, trustStorePath);  
	        // ��ʼ�����ο�  
	        trustManagerFactory.init(trustStore);  
	        // ʵ����SSL������  
	        SSLContext ctx = SSLContext.getInstance("TLS");  
	        // ��ʼ��SSL������  
	        ctx.init(keyManagerFactory.getKeyManagers(),  
	                trustManagerFactory.getTrustManagers(), null);  
	        // ���SSLSocketFactory  
	        return ctx;  
	    }  
	  
	    /** 
	     * ��ʼ��HttpsURLConnection. 
	     * @param password 
	     *            ���� 
	     * @param keyStorePath 
	     *            ��Կ��·�� 
	     * @param trustStorePath 
	     *            ���ο�·�� 
	     * @throws Exception 
	     */  
	    public static void initHttpsURLConnection(String password,  
	            String keyStorePath, String trustStorePath) throws Exception {  
	        // ����SSL������  
	        SSLContext sslContext = null;  
	        // ʵ������������֤�ӿ�  
	        HostnameVerifier hnv = new MyHostnameVerifier();  
	        try {  
	            sslContext = getSSLContext(password, keyStorePath, trustStorePath);  
	        } catch (GeneralSecurityException e) {  
	            e.printStackTrace();  
	        }  
	        if (sslContext != null) {  
	            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext  
	                    .getSocketFactory());  
	        }  
	        HttpsURLConnection.setDefaultHostnameVerifier(hnv);  
	    }  
	
	public static String  restPosthttps(String remoteUrl,String json) throws Exception{
		HttpsURLConnection  connection=null;
		 BufferedReader reader=null;
       try {
    	   
           URL url = new URL(remoteUrl);
           connection = (HttpsURLConnection) url.openConnection();
           connection.setDoOutput(true);
           connection.setDoInput(true);
           connection.setRequestMethod("POST");
           connection.setUseCaches(false);
           connection.setInstanceFollowRedirects(true);
           connection.setRequestProperty("Content-Type","application/json");
           connection.setRequestProperty("Accept-Charset", "UTF-8");
           connection.setRequestProperty("contentType", "UTF-8");
           connection.connect();
           PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(),"utf-8")); 
           out.println(json); 
           out.flush();
           out.close();
            reader = new BufferedReader(new InputStreamReader(
                   connection.getInputStream(),"UTF-8"));
           String lines;
           StringBuffer sb = new StringBuffer("");
           while ((lines = reader.readLine()) != null) {
               lines = new String(lines.getBytes(), "UTF-8");
               sb.append(lines);
           }
           
          return sb.toString();
       } catch (Exception e) {
       	throw e;
//       	logger.error(e);
       }finally{
       	if(reader!=null){
       		try {
					reader.close();
				} catch (IOException e) {
				
				} 
       	}
       	if(connection!=null){
       		 connection.disconnect();
       	}
       	
       }
   }
}
