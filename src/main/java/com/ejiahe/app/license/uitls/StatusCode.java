package com.ejiahe.app.license.uitls;

/**
 * 通用状态码
 *
 *
 * @author MaiJingFeng
 */
public class StatusCode {

    /**
     * 200 请求成功
     */
    public static final int SUCCESS = 200;


    /*************4XX为客户端请求错误码***********/

    /**
     * 400 Bad Request, 请求协议格式有误
     */
    public static final int BAD_REQUEST = 400;

    /**
     * 401 Unauthorized, 未授权
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 403 Forbidden, 禁止执行
     */
    public static final int FORBIDDEN = 403;

    /**
     * 404 Not Found, 服务或资源未找到
     */
    public static final int NOT_FOUND = 404;

    /**
     * 408 Request Timeout, 请求超时
     */
    public static final int TIME_OUT = 408;

    /**
     * 409 Conflict, 资源冲突
     */
    public static final int CONFLICT = 409;

    /**
     * 415, 不支持的媒体类型
     */
    public static final int UNSUPPORTED_MEDIA_TYPE = 415;

    /**
     * 419, 用户账号被冻结
     */
    public static final int ACCOUNT_LOCKED = 419;


    /***********5XX为系统级别错误码*************/

    /**
     * 500 Internal Server Error, 内部服务器错误
     */
    public static final int INTERNAL_SERVER_ERROR = 500;

    /**
     * 501 Not Implemented, 服务未实现
     */
    public static final int NOT_IMPL = 501;

    /**
     * 503 Service Unavailable, 服务器维护或者过载，服务器当前无法处理请求
     */
    public static final int UNAVAILABLE = 503;

    /**
     * 504 Gateway Timeout, 后台网关超时
     */
    public static final int GATEWAY_TIMEOUT = 504;


    /********6xx为应用层错误代码**************/

    /**
     * 601, 参数有误或不合法
     */
    public static final int ARGUMENT_ILLEGAL = 601;

    /**
     * 602, policy-violation, 违反业务策略
     */
    public static final int POLICY_VIOLATION = 602;

    /**
     * 603, not-allowed, 不允许执行
     */
    public static final int NOT_ALLOWED = 603;


    /**
     * 605, not-acceptable, 不允许执行
     */
    public static final int NOT_ACCEPTABLE = 605;


    /**
     * 612, 实体不存在或已被删除
     */
    public static final int ENTITY_NOT_FOUND = 612;

    /**
     * 614, 实体已存在
     */
    public static final int ALREADY_EXIST = 614;

    /**
     * 615, 登录验证错误
     */
    public static final int AUTH_ERROR = 615;


    /**
     * 616, 抽象的通用错误
     */
    public static final int COMMON_ERROR = 616;
}
