/**
 * 
 */
package com.vernon.webspider.core.task;

/**
 * 任务调度接口
 * 
 * @author Vernon.Chen
 *
 */
public interface Task {
	/**
	 * task processore method, task control thread execute it.
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception;

}
