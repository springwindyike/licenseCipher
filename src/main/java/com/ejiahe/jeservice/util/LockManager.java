package com.ejiahe.jeservice.util;

import com.ejiahe.jeservice.util.concurrent.CacheFactory;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Named
public class LockManager{
	
	protected static Logger LOG = LoggerFactory.getLogger(LockManager.class);
	
	private com.google.common.cache.Cache<String,Lock> localLocks;
	
	private static LockManager instance;

	public LockManager(){
		localLocks = CacheFactory.createLocalCache("Locks", 1 * JEConstants.HOUR, -1);
		instance = this;
	}
	
	public static LockManager getInstance() {
		return instance;
	}
	

	/**
	 * 产生一个本地锁; 同一时刻,相同的tag和id将返回同一把锁
	 * note: 该锁仅在本地JVM生效;
	 *  
	 * @return
	 */
	public Lock getLocalLock(String tag, String id) {
		String lockId = getLockId(tag, id);
		
		Lock lock = localLocks.getIfPresent(lockId);
		if(lock == null){
			synchronized(this){
				if(lock == null){
					lock = new ReentrantLock();
					localLocks.put(lockId, lock);
				}
			}
			
		}
		
		return lock;
	}
	
	

	private String getLockId(String tag, String id) {
		if(Strings.isNullOrEmpty(tag) && Strings.isNullOrEmpty(id)){
			throw new IllegalArgumentException("the 'tag' and 'id' can not empty at the same time");
		}
		
		StringBuilder b = new StringBuilder();
		if(!Strings.isNullOrEmpty(tag)){
			b.append(tag);
		}
		
		if(!Strings.isNullOrEmpty(id)){
			if(!Strings.isNullOrEmpty(tag)){
				b.append("_");
			}
			
			b.append(id);
		}
		
		return b.toString();
	}
}
