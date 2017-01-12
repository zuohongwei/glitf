package nc.bs.ufida.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import nc.bs.framework.common.RuntimeEnv;

/**
 * ��־��ӡ��ϵͳ�̹߳���<br>
 * ��ӡ����־, ���Զ����ϵ�ǰ�߳�id,�͵�ǰϵͳʱ��<br>
 * ���ڱ�����ص������ʱ, ��System.err���ó�System.out <br>
 * 
 */
public class VoucherLogInfo {
	/** Ĭ����־�ļ���С */
	private static final long maxFileSize = 10 * 1024 * 1024;

	/** ��ͨ��־���� */
	public static boolean DEBUG = true;

	/** ������־���� */
	public static boolean TRACE = true;

	private static SimpleDateFormat format = new SimpleDateFormat(
			"dd_HH:mm:ss:SSS");
	private static boolean isToFile = false;

	private static File file = null;

	private static PrintStream sysout = System.out;

	 static {
	    toFile();
	 }

	/**
	 * ��ø�ʽ����ʱ���ַ���.
	 * 
	 * @return
	 */
	private static String getTimeString() {
		return format.format(new Date());
	}

	/**
	 * ��ñ�����������־�ļ�
	 */
	private synchronized static OutputStream getLogFile() {
		if (file == null) {
			try {
				String dir = getLogPath();
				String name = "ncVoucherLog.log";

				file = new File(dir + name);
				file.getParentFile().mkdirs();

				return new FileOutputStream(file, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * �����־�ļ�����Ŀ¼
	 * 
	 * @return
	 */
	private static String getLogPath() {
		String cfg = RuntimeEnv.getInstance().getNCHome();
		if (cfg == null) {
			cfg = "./";
		}
		String dir = cfg + "/nclogs/voucher/";
		return dir;
	}

	private static void backupLog() {
		if (file == null) {
			return;
		}

		sysout.close();
		String dir = getLogPath();
		String now = new DateTime().format("MM_dd_HH_mm_ss_SSS");
		file.renameTo(new File(dir + now + ".log"));
		file = null;
	}
	/**
	 * ����־�ض���ָ�����ļ�
	 * 
	 * @param log
	 */
	public static void toFile() {
		isToFile = true;
		setOut(getLogFile());
	}

	/**
	 * ����־�ض��򵽿���̨
	 */
	public static void toConsole() {
		isToFile = false;
		setOut(System.out);
	}

	/**
	 * ����L�������
	 * 
	 * @param out
	 */
	public static void setOut(OutputStream out) {
		if (out == null) {
			return;
		}

		PrintStream print = null;
		if (out instanceof PrintStream) {
			print = (PrintStream) out;
		} else {
			print = new PrintStream(out);
		}
		sysout = print;
	}

	/**
	 * ������ͨ��־.
	 * 
	 * @param info
	 *            �������Ϣ
	 */
	public static void info(Object... info) {
		if (!DEBUG) {
			return;
		}

		StringBuffer sb = getHead();
		sb.append("    : ");
		sb.append(StringUtil.addSplit(", ", info));

		print(sb);
	}

	/**
	 * ������ͨ��־.
	 * 
	 * @param info
	 *            �������Ϣ
	 */
	public static void entry(Object... info) {
		if (!DEBUG) {
			return;
		}

		StringBuffer sb = getHead();
		sb.append("====: ");
		sb.append(StringUtil.addSplit(", ", info));

		print(sb);
	}

	public static void in(Object... info) {
		if (!DEBUG) {
			return;
		}

		StringBuffer sb = getHead();
		sb.append("<---: ");
		sb.append(StringUtil.addSplit(", ", info));
		print(sb);
	}

	public static void out(Object... info) {
		if (!DEBUG) {
			return;
		}

		StringBuffer sb = getHead();
		sb.append("--->: ");
		sb.append(StringUtil.addSplit(", ", info));

		print(sb);
	}

	/**
	 * ������ͨ��־.
	 * 
	 * @param info
	 *            �������Ϣ
	 */
	public static void trace(Object... info) {
		if (!TRACE) {
			return;
		}

		String md = getInvokeMethodLine();
		StringBuffer sb = getHead();
		sb.append("====: ");
		sb.append(md);
		sb.append(": ");
		sb.append(StringUtil.addSplit(", ", info));
		print(sb);
	}

	/**
	 * �쳣�Ĵ�ӡ
	 * 
	 * @param t
	 *            �쳣����
	 * @param info
	 *            ������Ϣ
	 */
	public static void error(Throwable t, Object... info) {
		if (!TRACE) {
			return;
		}

		VoucherLogInfo.trace(info);
		if (t != null) {
			print(t);
		}
	}

	/**
	 * ͳһ���
	 * 
	 * @param o
	 */
	private synchronized static void print(Object o) {
		// ��־�ļ���С, ����־����ʱ�л��ļ�
		if (isToFile && file != null && file.length() > maxFileSize) {
			backupLog();
			toFile();
		}

		if (o instanceof Throwable) {
			((Throwable) o).printStackTrace(sysout);
		} else {
			sysout.println(o);
		}
	}

	/**
	 * ��ӡ�߳��ڵ��ö�ջ һ���ڵ���ʱʹ��
	 * 
	 * @param info
	 *            ������Ϣ
	 */
	public static void errStack(Object... info) {
		error(new RuntimeException(), info);
	}

	/**
	 * ��ô�ӡͷ
	 * 
	 * @return
	 */
	private static StringBuffer getHead() {
		long id = Thread.currentThread().getId();
		String tid = String.format("%-5d,", id);
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(tid);
		sb.append(getTimeString());
		sb.append(" ]:");
		return sb;
	}

	/**
	 * ���ص���������������д�����������к� ��: (LTest.java:12)
	 * 
	 * @return
	 */
	public static String getInvokeLine() {
		StackTraceElement[] e = new RuntimeException().getStackTrace();
		StackTraceElement st = e[1];
		for (StackTraceElement s : e) {
			if (!s.getClassName().equals(VoucherLogInfo.class.getName())) {
				st = s;
				break;
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" (");
		sb.append(st.getFileName());
		sb.append(":");
		sb.append(st.getLineNumber());
		sb.append(")");
		return sb.toString();
	}

	/**
	 * ���ص���������������д������ڵķ����� ��: test
	 * 
	 * @return
	 */
	public static String getInvokeMethod() {
		Exception e = new Exception();
		StackTraceElement[] sts = e.getStackTrace();
		StackTraceElement st = sts[1];
		for (StackTraceElement s : sts) {
			if (!s.getClassName().equals(VoucherLogInfo.class.getName())) {
				st = s;
				break;
			}
		}
		return st.getMethodName();
	}

	/**
	 * ���ص�����������ķ��������к� ��: test (LTest.java:12)
	 * 
	 * @return
	 */
	public static String getInvokeMethodLine() {
		StringBuffer sb = new StringBuffer();
		sb.append(getInvokeMethod());
		sb.append(getInvokeLine());
		return sb.toString();
	}

	/**
	 * ʹ��ǰ�߳�����
	 * 
	 * @param t
	 *            ����ʱ��ĺ���ֵ
	 */
	public static void pauseTime(long t) {
		try {
			Thread.sleep(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * һֱ�ȴ�ָ�������ͬ������
	 * 
	 * @param obj
	 *            ��Ҫ�ȴ���Ŀ�����
	 */
	public static void waitObj(Object obj) {
		waitObj(obj, 0);
	}

	/**
	 * �ȴ�ָ�������ͬ������
	 * 
	 * @param obj
	 *            ��Ҫ�ȴ���Ŀ�����
	 * @param timeout
	 *            �ȴ��ĳ�ʱʱ��, ���Ϊ0 ��һֱ�ȴ�, ָ��obj�ϵ�notify����������
	 */
	public static void waitObj(Object obj, long timeout) {
		synchronized (obj) {
			try {
				obj.wait(timeout);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * �������еȴ�obj���߳�
	 * 
	 * @param obj
	 *            Ŀ�����
	 */
	public static void notifyObj(Object obj) {
		synchronized (obj) {
			obj.notifyAll();
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100000; i++) {
			VoucherLogInfo.out("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		}
	}
}
