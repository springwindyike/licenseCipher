package com.ejiahe.jeservice.jsonrpc.server.annotation;

/**
 * 第三方应用权限;
 * 目前分9个级别的权限,其中9为最高权限;
 * 
 * 
 * @author MaiJingFeng
 */
public enum JSONRPCClientPermission {
	
	ZERO(0),
	ONE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7),
	EIGHT(8),
	NINE(9)
	
	;
	private int level;
	
	private JSONRPCClientPermission(int level){
		this.level = level;
	}
	
	
	public int getValue(){
		return this.level;
	}
	
	public boolean isEqualThan(JSONRPCClientPermission targetPermission){
		int targetLevel = targetPermission.getValue();
		
		if(this.level == targetLevel){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 当前权限是否大于指定权限;
	 * @param targetPermission 要进行比较的权限
	 * @return 若大于指定权限,则返回true
	 *
	 */
	public boolean isLagerThan(JSONRPCClientPermission targetPermission){
		int targetLevel = targetPermission.getValue();
		
		if(this.level > targetLevel){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 当前权限是否大于或等于指定权限;
	 * @param targetPermission 要进行比较的权限
	 * @return 若大于或等于指定权限,则返回true
	 *
	 */
	public boolean isLagerEqualThan(JSONRPCClientPermission targetPermission){
		int targetLevel = targetPermission.getValue();
		
		if(this.level >= targetLevel){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 当前权限是否小于指定权限;
	 * @param targetPermission 要进行比较的权限
	 * @return 若小于指定权限,则返回true
	 *
	 */
	public boolean isLessThan(JSONRPCClientPermission targetPermission){
		int targetLevel = targetPermission.getValue();
		
		if(this.level < targetLevel){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 当前权限是否小于或等于指定权限;
	 * @param targetPermission 要进行比较的权限
	 * @return 若小于或等于指定权限,则返回true
	 *
	 */
	public boolean isLessEqualThan(JSONRPCClientPermission targetPermission){
		int targetLevel = targetPermission.getValue();
		
		if(this.level <= targetLevel){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 将int转化为ExternalAppPermission枚举对象
	 * @param permission
	 * @return
	 *
	 */
	public static JSONRPCClientPermission parsePermission(int permission){
		
		if(permission > JSONRPCClientPermission.NINE.getValue()){
			return JSONRPCClientPermission.NINE;
		}
		
		switch(permission){
			case 0:
				return JSONRPCClientPermission.ZERO;
			
			case 1:
				return JSONRPCClientPermission.ONE;
			
			case 2:
				return JSONRPCClientPermission.TWO;
			
			case 3: 
				return JSONRPCClientPermission.THREE;
				
			case 4:
				return JSONRPCClientPermission.FOUR;
			
			case 5:
				return JSONRPCClientPermission.FIVE;
			
			case 6: 
				return JSONRPCClientPermission.SIX;
				
			case 7:
				return JSONRPCClientPermission.SEVEN;
			
			case 8:
				return JSONRPCClientPermission.EIGHT;
			
			case 9: 
				return JSONRPCClientPermission.NINE;
			
			default:
				return JSONRPCClientPermission.ZERO;
		}
	}
	
}
	