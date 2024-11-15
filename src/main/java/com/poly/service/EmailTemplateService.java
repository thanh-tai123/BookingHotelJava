package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.poly.entity.Book;
import com.poly.entity.BookDetail;

@Service
public class EmailTemplateService {

    @Autowired
    private TemplateEngine templateEngine;

    public String buildEmailContent(Book book, List<BookDetail> bookDetails) {
        Context context = new Context();
        context.setVariable("book", book);
        context.setVariable("bookDetails", bookDetails);
        return templateEngine.process("emailTemplate", context);
    }
}