package com.ejiahe.jeservice.web.uitls;

import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCError;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCResponse;
import com.ejiahe.jeservice.jsonrpc.util.JSONRPCUtil;
import com.ejiahe.jeservice.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 缺省 异常处理器,JSON异常提示输出,适用于JSON处理的Controller
 *
 *
 * @author MaiJingFeng
 */
public class AbstractJSONRPCExceptionHandlerController {

    private static Logger LOG = LoggerFactory.getLogger(AbstractJSONRPCExceptionHandlerController.class);


    @ExceptionHandler(JSONRPCError.class)
    @ResponseBody
    public String handleIllegalJSONRPC2Error(JSONRPCError exception){
        JSONRPCResponse resp = new JSONRPCResponse(exception ,null);
        return resp.toString();
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public String handleIllegalArgumentException(Exception exception){
        JSONRPCResponse result = JSONRPCUtil.createError(StatusCode.ARGUMENT_ILLEGAL, exception.getMessage());
        return result.toJSONString();
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public String handleRuntimeException(Exception exception){
        LOG.error(exception.getMessage(),exception);
        JSONRPCResponse result = JSONRPCUtil.createError(StatusCode.INTERNAL_SERVER_ERROR, "internal errors");
        return result.toJSONString();
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    protected String handleException(HttpServletRequest request, HttpServletResponse response,
                                     Exception ex) {

        try {
            if (ex instanceof NoSuchRequestHandlingMethodException) {
                return handleNoSuchRequestHandlingMethod((NoSuchRequestHandlingMethodException) ex, request, response);
            }
            else if (ex instanceof HttpRequestMethodNotSupportedException) {
                return handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException) ex, request,
                        response);
            }
            else if (ex instanceof HttpMediaTypeNotSupportedException) {
                return handleHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException) ex, request, response);
            }
            else if (ex instanceof HttpMediaTypeNotAcceptableException) {
                return handleHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException) ex, request, response);
            }
            else if (ex instanceof MissingServletRequestParameterException) {
                return handleMissingServletRequestParameter((MissingServletRequestParameterException) ex, request,
                        response);
            }
            else if (ex instanceof ServletRequestBindingException) {
                return handleServletRequestBindingException((ServletRequestBindingException) ex, request, response);
            }
            else if (ex instanceof ConversionNotSupportedException) {
                return handleConversionNotSupported((ConversionNotSupportedException) ex, request, response);
            }
            else if (ex instanceof TypeMismatchException) {
                return handleTypeMismatch((TypeMismatchException) ex, request, response);
            }
            else if (ex instanceof HttpMessageNotReadableException) {
                return handleHttpMessageNotReadable((HttpMessageNotReadableException) ex, request, response);
            }
            else if (ex instanceof HttpMessageNotWritableException) {
                return handleHttpMessageNotWritable((HttpMessageNotWritableException) ex, request, response);
            }
            else if (ex instanceof MethodArgumentNotValidException) {
                return handleMethodArgumentNotValidException((MethodArgumentNotValidException) ex, request, response);
            }
            else if (ex instanceof MissingServletRequestPartException) {
                return handleMissingServletRequestPartException((MissingServletRequestPartException) ex, request, response);
            }
            else if (ex instanceof BindException) {
                return handleBindException((BindException) ex, request, response);
            }
        }
        catch (Exception handlerException) {
            LOG.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
        }

        LOG.error(ex.getMessage(),ex);
        JSONRPCResponse result = JSONRPCUtil.createError(StatusCode.INTERNAL_SERVER_ERROR, ex.getMessage());
        return result.toJSONString();
    }

    /**
     * Handle the case where no request handler method was found.
     * <p>The default implementation logs a warning, sends an HTTP 404 error, and returns
     * an empty {@code ModelAndView}. Alternatively, a fallback view could be chosen,
     * or the NoSuchRequestHandlingMethodException could be rethrown as-is.
     * @param ex the NoSuchRequestHandlingMethodException to be handled
     * @param request current HTTP request
     * @param response current HTTP response
     * at the time of the exception (for example, if multipart resolution failed)
     * @return an empty ModelAndView indicating the exception was handled
     * @throws IOException potentially thrown from response.sendError()
     */
    protected String handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex,
                                                       HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.warn(ex.getMessage());
        JSONRPCResponse result = JSONRPCUtil.createError(StatusCode.NOT_FOUND, ex.getMessage());
        return result.toJSONString();
    }

    /**
     * Handle the case where no request handler method was found for the particular HTTP request method.
     * <p>The default implementation logs a warning, sends an HTTP 405 error, sets the "Allow" header,
     * and returns an empty {@code ModelAndView}. Alternatively, a fallback view could be chosen,
     * or the HttpRequestMethodNotSupportedException could be rethrown as-is.
     * @param ex the HttpRequestMethodNotSupportedException to be handled
     * @param request current HTTP request
     * @param response current HTTP response
     */
    protected String handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                         HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.warn(ex.getMessage());
        JSONRPCResponse result = JSONRPCUtil.createError(StatusCode.NOT_ALLOWED, ex.getMessage());
        return result.toJSONString();
    }

    /**
     * Handle the case where no {@linkplain org.springframework.http.converter.HttpMessageConverter message converters}
     * were found for the PUT or POSTed content. <p>The default implementation sends an HTTP 415 error,
     * sets the "Accept" header, and returns an empty {@code ModelAndView}. Alternatively, a fallback
     * view could be chosen, or the HttpMediaTypeNotSupportedException could be rethrown as-is.
     * @param ex the HttpMediaTypeNotSupportedException to be handled
     * @param request current HTTP request
     * @param response current HTTP response
     */
    protected String handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                     HttpServletRequest request, HttpServletResponse response) throws IOException {

        JSONRPCResponse result = JSONRPCUtil.createError(StatusCode.UNSUPPORTED_MEDIA_TYPE, "UNSUPPORTED_MEDIA_TYPE");

        List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            response.setHeader("Accept", MediaType.toString(mediaTypes));
        }
        return result.toJSONString();

    }

    /**
     * Handle the case where no {@linkplain org.springframework.http.converter.HttpMessageConverter message converters}
     * were found that were acceptable for the client (expressed via the {@code Accept} header.
     * <p>The default implementation sends an HTTP 406 error and returns an empty {@code ModelAndView}.
     * Alternatively, a fallback view could be chosen, or the HttpMediaTypeNotAcceptableException
     * could be rethrown as-is.
     * @param ex the HttpMediaTypeNotAcceptableException to be handled
     * @param request current HTTP request
     * @param response current HTTP response
     */
    protected String handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                      HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONRPCResponse result = JSONRPCUtil.createError(StatusCode.NOT_ACCEPTABLE, "NOT_ACCEPTABLE");
        return result.toJSONString();
    }

    /**
     * Handle the case when a required parameter is missing.
     * <p>The default implementation sends an HTTP 400 error, and returns an empty {@code ModelAndView}.
     * Alternatively, a fallback view could be chosen, or the MissingServletRequestParameterException
     * could be rethrown as-is.
     * @param ex the MissingServletRequestParameterException to be handled
     * @param request current HTTP request
     * @param response current HTTP response
     */
    protected String handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                          HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.warn(ex.getMessage());
        JSONRPCResponse result = JSONRPCUtil.createError(StatusCode.BAD_REQUEST, ex.getMessage());
        return result.toJSONString();
    }

    /**
     * Handle the case when an unrecoverable binding exception occurs - e.g. required header, required cookie.
     * <p>The default implementation sends an HTTP 400 error, and returns an empty {@code ModelAndView}.
     * Alternatively, a fallback view could be chosen, or the exception could be rethrown as-is.
     * @param ex the exception to be handled
     * @param request current HTTP request
     * @param response current HTTP response
     */
    protected String handleServletRequestBindingException(ServletRequestBindingException ex,
                                                          HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.warn(ex.getMessage());
        JSONRPCResponse result = JSONRPCUtil.createError(StatusCode.BAD_REQUEST, ex.getMessage());
        return result.toJSONString();
    }

    /**
     * Handle the case when a {@link org.springframework.web.bind.WebDataBinder} conversion cannot occur.
     * <p>The default implementation sends an HTTP 500 error, and returns an empty {@code ModelAndView}.
     * Alternatively, a fallback view could be chosen, or the TypeMismatchException could be rethrown as-is.
     * @param ex the ConversionNotSupportedException to be handled
     * @param request current HTTP request
     * @param response current HTTP response
     */
    protected String handleConversionNotSupported(ConversionNotSupportedException ex,
                                                  HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.error(ex.getMessage(),ex);
        JSONRPCResponse result = JSONRPCUtil.createError(StatusCode.INTERNAL_SERVER_ERROR, ex.getMessage());
        return result.toJSONString();
    }

    /**
     * Handle the case when a {@link org.springframework.web.bind.WebDataBinder} conversion error occurs.
     * <p>The default implementation sends an HTTP 400 error, and returns an empty {@code ModelAndView}.
     * Alternatively, a fallback view could be chosen, or the TypeMismatchException could be rethrown as-is.
     * @param ex the TypeMismatchException to be handled
     * @param request current HTTP request
     * @param response current HTTP response
     */
    protected String handleTypeMismatch(TypeMismatchException ex,
                                        HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.warn(ex.getMessage());
        JSONRPCResponse result = JSONRPCUtil.createError(StatusCode.BAD_REQUEST, ex.getMessage());
        return result.toJSONString();
    }

    /**
     * Handle the case where a {@linkplain org.springframework.http.converter.HttpMessageConverter message converter}
     * cannot read from a HTTP request.
     * <p>The default implementation sends an HTTP 400 error, and returns an empty {@code ModelAndView}.
     * Alternatively, a fallback view could be chosen, or the HttpMediaTypeNotSupportedException could be
     * rethrown as-is.
     * @param ex the HttpMessageNotReadableException to be handled
     * @param request current HTTP request
     * @param response current HTTP response
     */
    protected String handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                  HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.warn(ex.getMessage());
        JSONRPCResponse result = JSONRPCUtil.createError(StatusCode.BAD_REQUEST, ex.getMessage());
        return result.toJSONString();
    }

    /**
     * Handle the case where a {@linkplain org.springframework.http.converter.HttpMessageConverter message converter}
     * cannot write to a HTTP request.
     * <p>The default implementation sends an HTTP 500 error, and returns an empty {@code ModelAndView}.
     * Alternatively, a fallback view could be chosen, or the HttpMediaTypeNotSupportedException could be
     * rethrown as-is.
     * @param ex the HttpMessageNotWritableException to be handled
     * @param request current HTTP request
     * @param response current HTTP response
     */
    protected String handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
                                                  HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.error(ex.getMessage(),ex);
        JSONRPCResponse result = JSONRPCUtil.createError(StatusCode.INTERNAL_SERVER_ERROR, ex.getMessage());
        return result.toJSONString();
    }

    /**
     * Handle the case where an argument annotated with {@code @Valid} such as
     * an {@link RequestBody} or {@link RequestPart} argument fails validation.
     * An HTTP 400 error is sent back to the client.
     * @param request current HTTP request
     * @param response current HTTP response
     */
    protected String handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                           HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.warn(ex.getMessage());
        JSONRPCResponse result = JSONRPCUtil.createError(StatusCode.BAD_REQUEST, ex.getMessage());
        return result.toJSONString();
    }

    /**
     * Handle the case where an {@linkplain RequestPart @RequestPart}, a {@link MultipartFile},
     * or a {@code javax.servlet.http.Part} argument is required but is missing.
     * An HTTP 400 error is sent back to the client.
     * @param request current HTTP request
     * @param response current HTTP response
     */
    protected String handleMissingServletRequestPartException(MissingServletRequestPartException ex,
                                                              HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.warn(ex.getMessage());
        JSONRPCResponse result = JSONRPCUtil.createError(StatusCode.BAD_REQUEST, ex.getMessage());
        return result.toJSONString();
    }

    /**
     * Handle the case where an {@linkplain ModelAttribute @ModelAttribute} method
     * argument has binding or validation errors and is not followed by another
     * method argument of type {@link BindingResult}.
     * By default an HTTP 400 error is sent back to the client.
     * @param request current HTTP request
     * @param response current HTTP response
     */
    protected String handleBindException(BindException ex, HttpServletRequest request,
                                         HttpServletResponse response) throws IOException {
        LOG.warn(ex.getMessage());
        JSONRPCResponse result = JSONRPCUtil.createError(StatusCode.BAD_REQUEST, ex.getMessage());
        return result.toJSONString();
    }
}
