package kr.co.bizframe.esb.mng.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MonitorThreadFactory implements ThreadFactory {
	private final String name;
	private final boolean daemon;
	private final AtomicInteger threadNumber = new AtomicInteger(1);

	public MonitorThreadFactory(String name) {
		this(name, false);
	}

	public MonitorThreadFactory(String name, boolean daemon) {
		this.name = name;
		this.daemon = daemon;
	}

	public Thread newThread(Runnable r) {
		int num = this.threadNumber.getAndIncrement();
		String threadName = this.name + "_" + num;
		Thread t = new Thread(r, threadName);
		if (this.daemon) {
			t.setDaemon(true);
		}
		return t;
	}

}
