package cn.bizfocus.ezw.expense.framework.common.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class MyHostnameVerifier  implements HostnameVerifier {  
    public boolean verify(String hostname, SSLSession session) {  
       /* if("10.0.3.111".equals(hostname)){  
            return true;  
        } else {  
            return false;  
        }  */
    	return true;
    }  
}
