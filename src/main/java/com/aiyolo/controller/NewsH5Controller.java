package com.aiyolo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aiyolo.common.StringHelper;
import com.aiyolo.entity.News;
import com.aiyolo.repository.NewsRepository;

@Controller
public class NewsH5Controller {

    @Autowired NewsRepository newsRepository;

    @RequestMapping("/web/news")
    public String list(Model model) {
        List<News> news = newsRepository.findAllOrderByTopDescAndCreatedAtDesc();
        for (int i = 0; i < news.size(); i++) {
            news.get(i).setContent(StringHelper.stripHTMLTag(news.get(i).getContent()));
        }
        model.addAttribute("news", news);

        return "newsH5";
    }

    @RequestMapping("/web/news/{id}")
    public String view(@PathVariable String id, Model model) {
        Long newsId;
        try {
            newsId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return "redirect:/404";
        }

        News news = newsRepository.findOne(newsId);
        if (news != null && news.getStatus() == 1) {
            model.addAttribute("news", news);

            return "newsH5Detail";
        }

        return "redirect:/404";
    }

}
