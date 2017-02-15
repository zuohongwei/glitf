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
	        // 实例化密钥库  
	        KeyStore ks = KeyStore.getInstance("JKS");  
	        // 获得密钥库文件流  
	        FileInputStream is = new FileInputStream(keyStorePath);  
	        // 加载密钥库  
	        ks.load(is, password.toCharArray());  
	        // 关闭密钥库文件流  
	        is.close();  
	        return ks;  
	    }  
	  
	    /** 
	     * 获得SSLSocketFactory. 
	     * @param password 
	     *            密码 
	     * @param keyStorePath 
	     *            密钥库路径 
	     * @param trustStorePath 
	     *            信任库路径 
	     * @return SSLSocketFactory 
	     * @throws Exception 
	     */  
	    public static SSLContext getSSLContext(String password,  
	            String keyStorePath, String trustStorePath) throws Exception {  
	        // 实例化密钥库  
	        KeyManagerFactory keyManagerFactory = KeyManagerFactory  
	                .getInstance(KeyManagerFactory.getDefaultAlgorithm());  
	        // 获得密钥库  
	        KeyStore keyStore = getKeyStore(password, keyStorePath);  
	        // 初始化密钥工厂  
	        keyManagerFactory.init(keyStore, password.toCharArray());  
	  
	        // 实例化信任库  
	        TrustManagerFactory trustManagerFactory = TrustManagerFactory  
	                .getInstance(TrustManagerFactory.getDefaultAlgorithm());  
	        // 获得信任库  
	        KeyStore trustStore = getKeyStore(password, trustStorePath);  
	        // 初始化信任库  
	        trustManagerFactory.init(trustStore);  
	        // 实例化SSL上下文  
	        SSLContext ctx = SSLContext.getInstance("TLS");  
	        // 初始化SSL上下文  
	        ctx.init(keyManagerFactory.getKeyManagers(),  
	                trustManagerFactory.getTrustManagers(), null);  
	        // 获得SSLSocketFactory  
	        return ctx;  
	    }  
	  
	    /** 
	     * 初始化HttpsURLConnection. 
	     * @param password 
	     *            密码 
	     * @param keyStorePath 
	     *            密钥库路径 
	     * @param trustStorePath 
	     *            信任库路径 
	     * @throws Exception 
	     */  
	    public static void initHttpsURLConnection(String password,  
	            String keyStorePath, String trustStorePath) throws Exception {  
	        // 声明SSL上下文  
	        SSLContext sslContext = null;  
	        // 实例化主机名验证接口  
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
