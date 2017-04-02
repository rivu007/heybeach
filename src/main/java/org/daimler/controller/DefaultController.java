package org.daimler.controller;

import org.daimler.error.UnknownResourceException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Default controller for endpoints that doesn't exists.
 *
 * @author Abhilash Ghosh
 */
@RestController
public class DefaultController {

    @RequestMapping("*")
    public void unmappedRequest(HttpServletRequest request) throws UnknownResourceException {
        throw new UnknownResourceException("There is no resource for path " + request.getRequestURI());
    }
}
