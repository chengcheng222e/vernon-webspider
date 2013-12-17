package com.vernon.webspider.core;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 抓取的任务调度
 * 
 * @author Vernon.Chen
 *
 */
public abstract class SpiderJob
	implements Job {

	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public final void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		if (!jobDataMap.getBoolean("concurrent") && jobDataMap.getBoolean("run")) {
			logger.info(context.getJobDetail().getKey() + "not support concurrent and this  is run.  ");
			return;
		}
		jobDataMap.put("run", true);
		this.doExecute();// 调用doExecute
		jobDataMap.put("run", false);
	}

	/** 任务实现的方法 */
	abstract protected void doExecute();

}
