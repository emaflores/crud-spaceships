package com.emaflores.spaceships.config;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.List;

@Component
public class CustomPageableHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final List<String> ALLOWED_SORT_PROPERTIES = Arrays.asList("id", "name", "type", "source");

    private final PageableHandlerMethodArgumentResolver delegate;

    public CustomPageableHandlerMethodArgumentResolver(PageableHandlerMethodArgumentResolver delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return delegate.supportsParameter(parameter);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, org.springframework.web.bind.support.WebDataBinderFactory binderFactory) throws Exception {
        Pageable pageable = delegate.resolveArgument(parameter, mavContainer, webRequest, binderFactory);

        if (pageable.getSort().isSorted()) {
            Sort newSort = Sort.unsorted();
            for (Sort.Order order : pageable.getSort()) {
                if (ALLOWED_SORT_PROPERTIES.contains(order.getProperty())) {
                    newSort = newSort.and(Sort.by(order.getDirection(), order.getProperty()));
                }
            }
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), newSort);
        }

        return pageable;
    }
}