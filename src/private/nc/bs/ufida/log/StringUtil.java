package nc.bs.ufida.log;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * ����String��һЩ����
 * 
 * @author lixi
 */
public class StringUtil {

	/**
	 * ���Լ�д�ķ�����bytes�������
	 * 
	 * @param src
	 *            Դ����
	 * @return ���ܺ������, ��������򷵻�0������
	 */
	public static byte[] encodeByte(byte[] src) {
		if (src == null || src.length == 0) {
			return new byte[0];
		}

		int r = (int) (System.currentTimeMillis() % 100 + 127);
		for (int i = 0; i < src.length; i++) {
			src[i] = (byte) (src[i] + r + i);
		}

		ByteBuffer bb = ByteBuffer.allocate(src.length + 1);
		bb.put(src);
		bb.put((byte) r);

		return bb.array();
	}

	/**
	 * ���Լ�д�ķ�������bytes����
	 * 
	 * @param bytes
	 *            ���ĵ�����
	 * @return ���ĵ�����
	 */
	public static byte[] decodeByte(byte[] bytes) {
		byte[] src = new byte[bytes.length];
		System.arraycopy(bytes, 0, src, 0, src.length);

		int r = src[src.length - 1];

		for (int i = 0; i < src.length - 1; i++) {
			src[i] = (byte) (src[i] - r - i);
		}

		byte[] b = new byte[src.length - 1];
		System.arraycopy(src, 0, b, 0, b.length);
		return b;
	}

	/**
	 * ���ַ�������ָ���ķָ���ָ��һ������<br>
	 * ע:������String.split()����ͬ���Ľ��, ����Ч�ʽ������
	 * 
	 * @param src
	 *            Դ�ַ���
	 * @param regex
	 *            �ָ��
	 * @return �ָ�������
	 */
	public static String[] split(String src, String regex) {
		return split(src, regex, -1);
	}

	/**
	 * ���ַ�������ָ���ķָ���ָ��һ������<br>
	 * ע:������String.split()����ͬ���Ľ��, ����Ч�ʽ������
	 * 
	 * @param src
	 *            Դ����
	 * @param regex
	 *            �ָ��
	 * @param limit
	 *            �ָ�����
	 * @return �ָ�������
	 */
	public static String[] split(String src, String regex, int limit) {
		if (src == null || src.equals("")) {
			return new String[] { src };
		}

		if (limit == 0) {
			limit = -1;
		}

		ArrayList<String> list = new ArrayList<String>();
		String temp = src;
		int index = -1;
		while ((index = temp.indexOf(regex)) >= 0 && list.size() != limit) {
			String a = temp.substring(0, index);
			list.add(a);
			temp = temp.substring(index + regex.length());
		}

		if (temp.length() > 0) {
			list.add(temp);
		}

		return list.toArray(new String[0]);
	}

	/**
	 * ��StringBuffer��һ����������Ͽ����ӳ�String
	 * 
	 * @param oo
	 *            Դ��������
	 * @return �������к��п�ֵ, �������ֵ�滻��һ��"null"
	 */
	public static String addString(Object... oo) {
		return addArray(oo);
	}

	/**
	 * ��һ��Object������ӳ�String
	 * 
	 * @param array
	 *            Դ��������
	 * @return �������к��п�ֵ, �������ֵ�滻��һ��"null"
	 */
	public static String addArray(Object[] array) {

		StringBuffer sb = new StringBuffer();
		for (Object o : array) {
			if (o != null) {
				sb.append(o.toString());
			} else {
				sb.append("null");
			}
		}
		return sb.toString();
	}

	/**
	 * ��һ������������ӳ��ַ���, �������м���ָ���ķָ�� <br>
	 * 
	 * <pre>
	 * String s = addSplit(&quot;:&quot;, &quot;a&quot;, &quot;b&quot;, &quot;c&quot;);
	 * System.out.println(s);
	 * 
	 * ��������: a:b:c
	 * </pre>
	 * 
	 * @param split
	 *            ָ���ķָ��
	 * @param o
	 *            ��������
	 * @return ��Ӻ���ַ���
	 * 
	 * @see #addSplit(String, String, String, Object...)
	 */
	public static String addSplit(String split, Object... o) {
		return addSplit("", "", split, o);
	}

	/**
	 * ָ����β�ͷָ��, ���ַ������
	 * 
	 * 
	 * <pre>
	 * String s = addSplit(&quot;h&quot;,&quot;t&quot;,&quot;,&quot;,a,b,c);
	 * System.out.println(s);
	 * 
	 * ��������: ha:b:ct
	 * </pre>
	 * 
	 * @param head
	 *            ָ����ͷ
	 * @param tail
	 *            ָ����β
	 * @param split
	 *            ָ���ķָ��
	 * @param o
	 *            �ַ�������
	 * @return ������Ӻ���ַ���
	 */
	public static String addSplit(String head, String tail, String split,
			Object... o) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(head);
		for (int i = 0; i < o.length; i++) {
			if (i != 0) {
				list.add(split);
			}

			list.add(o[i]);
		}
		list.add(tail);
		return addArray(list.toArray(new Object[0]));
	}

	/**
	 * ��UTF-8����õ��ַ�����url����
	 * 
	 * @param src
	 *            Դ�ַ���
	 * @return url����
	 * @see #urlEncode(String, String)
	 */
	public static String urlEncode(String src) {
		return urlEncode(src, "UTF-8");
	}

	/**
	 * �ַ�������Ϊurl
	 * 
	 * @param src
	 *            Դ�ַ���
	 * @param character
	 *            �ַ���
	 * @return ����url������
	 * @see #urlEncode(String)
	 */
	public static String urlEncode(String src, String character) {
		try {
			return URLEncoder.encode(src, character);
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * ��һ��url������ַ�����ԭΪ����
	 * 
	 * @param url
	 *            Դurl
	 * @return ԭ��
	 * @see #urlDecode(String, String)
	 */
	public static String urlDecode(String url) {
		return urlDecode(url, "UTF-8");
	}

	/**
	 * url����Ϊ�ַ���
	 * 
	 * @param url
	 *            Դurl
	 * @param charset
	 *            ָ�����ַ���
	 * @return ԭ�� ��������򷵻ؿ��ַ���
	 * @see #urlEncode(String)
	 */
	public static String urlDecode(String url, String charset) {
		try {
			return URLDecoder.decode(url, charset);
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * ���ַ�����UTF-8�ֽڱ���Ϊbase64
	 * 
	 * @param src
	 *            Դ�ַ���
	 * @return �������ַ���
	 * @see #base64Decode(String)
	 */
	public static String base64Encoder(String src) {
		BASE64Encoder base = new BASE64Encoder();
		try {
			String s = base.encode(src.getBytes("UTF-8"));
			s = s.replace("\r", "");
			s = s.replace("\n", "");
			return s;
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * base64����Ϊ�ֽ������UTF-8���������ַ���
	 * 
	 * @param base64
	 *            Դbase64����
	 * @return ��ԭ����ַ���
	 * @see #base64Encoder(String)
	 */
	public static String base64Decode(String base64) {
		BASE64Decoder base = new BASE64Decoder();
		try {
			byte[] bb = base.decodeBuffer(base64);
			return new String(bb, "UTF-8");
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * ��һ���ַ�������
	 * 
	 * @param s
	 * @return
	 */
	public static String reverse(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = s.length() - 1; i >= 0; i--) {
			sb = sb.append(s.charAt(i));
		}
		return sb.toString();
	}
}
