package com.hades_region.blockchain.web.handler;

import com.hades_region.blockchain.utils.JsonVo;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Robert Gerard
 */
@ControllerAdvice
public class AppExceptionHandler {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonVo handle(HttpServletRequest request, HttpServletResponse response, Exception e) {

        logger.error("ERROR ======> {}", e);
        return JsonVo.instance(JsonVo.CODE_SUCCESS, e.getMessage());
    }
}
