package com.webapp.microservice.services;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/exceptionhadler")
public class GlobalExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    public String handleMultipart(MultipartException e, RedirectAttributes attributes){
        attributes.addFlashAttribute("message", e.getCause().getMessage());
        return "redirect:/status";
    }
}
