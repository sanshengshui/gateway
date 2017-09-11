package com.aiyolo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aiyolo.entity.News;
import com.aiyolo.repository.NewsRepository;

@Controller
@RequestMapping("/news")
public class NewsController {

    @Autowired NewsRepository newsRepository;

    @RequestMapping("/new")
    public String add(Model model) {
        model.addAttribute("news", new News());
        return "newsForm";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        Long newsId;
        try {
            newsId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return "redirect:/404";
        }

        News news = newsRepository.findOne(newsId);
        if (news != null) {
            model.addAttribute("news", news);

            return "newsForm";
        }

        return "redirect:/404";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(News data) {
        if (data.getId() != null) {
            News news = newsRepository.findOne(data.getId());
            if (news != null) {
                news.setTitle(data.getTitle());
                news.setContent(data.getContent());
                news.setTop(data.getTop());

                newsRepository.save(news);

                return "redirect:/news";
            }
        } else {
            newsRepository.save(new News(
                    data.getTitle(),
                    data.getContent(),
                    data.getTop()));

            return "redirect:/news";
        }

        return "redirect:/404";
    }

    @RequestMapping("/switch/{id}")
    public String status(@PathVariable String id) {
        Long newsId;
        try {
            newsId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return "redirect:/404";
        }

        News news = newsRepository.findOne(newsId);
        if (news != null) {
            news.setStatus(news.getStatus() == 0 ? 1 : 0);

            newsRepository.save(news);

            return "redirect:/news";
        }

        return "redirect:/404";
    }

    @RequestMapping("/top/{id}")
    public String top(@PathVariable String id) {
        Long newsId;
        try {
            newsId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return "redirect:/404";
        }

        News news = newsRepository.findOne(newsId);
        if (news != null) {
            news.setTop(news.getTop() == 0 ? 1 : 0);

            newsRepository.save(news);

            return "redirect:/news";
        }

        return "redirect:/404";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        Long newsId;
        try {
            newsId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return "redirect:/404";
        }

        News news = newsRepository.findOne(newsId);
        if (news != null) {
            newsRepository.delete(news);

            return "redirect:/news";
        }

        return "redirect:/404";
    }

}
