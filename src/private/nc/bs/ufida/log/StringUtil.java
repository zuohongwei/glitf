package nc.bs.ufida.log;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 关于String的一些操作
 * 
 * @author lixi
 */
public class StringUtil {

	/**
	 * 用自己写的方法将bytes数组加密
	 * 
	 * @param src
	 *            源数组
	 * @return 加密后的数组, 如果出错则返回0长数组
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
	 * 用自己写的方法解密bytes数组
	 * 
	 * @param bytes
	 *            密文的数组
	 * @return 明文的数组
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
	 * 将字符串按照指定的分割符分割成一个数组<br>
	 * 注:他有鱼String.split()方法同样的结果, 但是效率交其更高
	 * 
	 * @param src
	 *            源字符串
	 * @param regex
	 *            分割符
	 * @return 分割后的数组
	 */
	public static String[] split(String src, String regex) {
		return split(src, regex, -1);
	}

	/**
	 * 将字符串按照指定的分割符分割成一个数组<br>
	 * 注:他有鱼String.split()方法同样的结果, 但是效率交其更高
	 * 
	 * @param src
	 *            源数组
	 * @param regex
	 *            分割符
	 * @param limit
	 *            分割限制
	 * @return 分割后的数组
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
	 * 用StringBuffer将一个对象数组较快的相加成String
	 * 
	 * @param oo
	 *            源对象数组
	 * @return 若数组中含有空值, 则将这个空值替换成一个"null"
	 */
	public static String addString(Object... oo) {
		return addArray(oo);
	}

	/**
	 * 将一个Object数组相加成String
	 * 
	 * @param array
	 *            源对象数组
	 * @return 若数组中含有空值, 则将这个空值替换成一个"null"
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
	 * 将一个对象数组相加成字符串, 并在其中加入指定的分割符 <br>
	 * 
	 * <pre>
	 * String s = addSplit(&quot;:&quot;, &quot;a&quot;, &quot;b&quot;, &quot;c&quot;);
	 * System.out.println(s);
	 * 
	 * 结果将输出: a:b:c
	 * </pre>
	 * 
	 * @param split
	 *            指定的分割符
	 * @param o
	 *            数据数组
	 * @return 相加后的字符串
	 * 
	 * @see #addSplit(String, String, String, Object...)
	 */
	public static String addSplit(String split, Object... o) {
		return addSplit("", "", split, o);
	}

	/**
	 * 指定首尾和分割符, 将字符串相加
	 * 
	 * 
	 * <pre>
	 * String s = addSplit(&quot;h&quot;,&quot;t&quot;,&quot;,&quot;,a,b,c);
	 * System.out.println(s);
	 * 
	 * 结果将输出: ha:b:ct
	 * </pre>
	 * 
	 * @param head
	 *            指定的头
	 * @param tail
	 *            指定的尾
	 * @param split
	 *            指定的分割符
	 * @param o
	 *            字符串数组
	 * @return 返回相加后的字符串
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
	 * 用UTF-8编码得到字符串的url编码
	 * 
	 * @param src
	 *            源字符串
	 * @return url编码
	 * @see #urlEncode(String, String)
	 */
	public static String urlEncode(String src) {
		return urlEncode(src, "UTF-8");
	}

	/**
	 * 字符串编码为url
	 * 
	 * @param src
	 *            源字符串
	 * @param character
	 *            字符集
	 * @return 返回url编码结果
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
	 * 将一段url编码的字符串还原为明文
	 * 
	 * @param url
	 *            源url
	 * @return 原文
	 * @see #urlDecode(String, String)
	 */
	public static String urlDecode(String url) {
		return urlDecode(url, "UTF-8");
	}

	/**
	 * url解码为字符串
	 * 
	 * @param url
	 *            源url
	 * @param charset
	 *            指定的字符集
	 * @return 原文 如果出错则返回空字符串
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
	 * 将字符串的UTF-8字节编码为base64
	 * 
	 * @param src
	 *            源字符串
	 * @return 编码后的字符串
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
	 * base64解码为字节数组后按UTF-8编码后构造成字符串
	 * 
	 * @param base64
	 *            源base64编码
	 * @return 还原后的字符串
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
	 * 将一个字符串倒置
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
