package nc.bs.ufida.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import nc.bs.framework.common.RuntimeEnv;

/**
 * 日志打印和系统线程工具<br>
 * 打印的日志, 将自动加上当前线程id,和当前系统时间<br>
 * 并在本类加载到虚拟机时, 将System.err设置成System.out <br>
 * 
 */
public class VoucherLogInfo {
	/** 默认日志文件大小 */
	private static final long maxFileSize = 10 * 1024 * 1024;

	/** 普通日志开关 */
	public static boolean DEBUG = true;

	/** 调试日志开关 */
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
	 * 获得格式化的时间字符串.
	 * 
	 * @return
	 */
	private static String getTimeString() {
		return format.format(new Date());
	}

	/**
	 * 获得本次启动的日志文件
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
	 * 获得日志文件所在目录
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
	 * 将日志重定向到指定的文件
	 * 
	 * @param log
	 */
	public static void toFile() {
		isToFile = true;
		setOut(getLogFile());
	}

	/**
	 * 将日志重定向到控制台
	 */
	public static void toConsole() {
		isToFile = false;
		setOut(System.out);
	}

	/**
	 * 设置L的输出流
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
	 * 处理普通日志.
	 * 
	 * @param info
	 *            输出的信息
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
	 * 处理普通日志.
	 * 
	 * @param info
	 *            输出的信息
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
	 * 处理普通日志.
	 * 
	 * @param info
	 *            输出的信息
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
	 * 异常的打印
	 * 
	 * @param t
	 *            异常对象
	 * @param info
	 *            附加信息
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
	 * 统一输出
	 * 
	 * @param o
	 */
	private synchronized static void print(Object o) {
		// 日志文件大小, 当日志过大时切换文件
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
	 * 打印线程内调用堆栈 一般在调试时使用
	 * 
	 * @param info
	 *            附加信息
	 */
	public static void errStack(Object... info) {
		error(new RuntimeException(), info);
	}

	/**
	 * 获得打印头
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
	 * 返回调用这个方法的那行代码的类名及行号 如: (LTest.java:12)
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
	 * 返回调用这个方法的那行代码所在的方法名 如: test
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
	 * 返回调用这个方法的方法名记行号 如: test (LTest.java:12)
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
	 * 使当前线程休眠
	 * 
	 * @param t
	 *            休眠时间的毫秒值
	 */
	public static void pauseTime(long t) {
		try {
			Thread.sleep(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 一直等待指定对象的同步操作
	 * 
	 * @param obj
	 *            需要等待的目标对象
	 */
	public static void waitObj(Object obj) {
		waitObj(obj, 0);
	}

	/**
	 * 等待指定对象的同步操作
	 * 
	 * @param obj
	 *            需要等待的目标对象
	 * @param timeout
	 *            等待的超时时间, 如果为0 则一直等待, 指导obj上的notify方法被调用
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
	 * 唤醒所有等待obj的线程
	 * 
	 * @param obj
	 *            目标对象
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
