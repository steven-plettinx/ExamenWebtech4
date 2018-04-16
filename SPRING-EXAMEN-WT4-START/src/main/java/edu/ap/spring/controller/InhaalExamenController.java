package edu.ap.spring.controller;


import edu.ap.spring.model.InhaalExamen;
import edu.ap.spring.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.Request;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/")
public class InhaalExamenController {

    private RedisService service;
    private InhaalExamen examen;

    @Autowired
    public void setRedisService(RedisService redisService) { this.service = redisService; }

    @GetMapping("/list")
    public String getStudent(HttpServletRequest request) {
        String name = request.getParameter("student");

        String student = service.getKey(name);

        if (student != null) {
            return student;
        }
        else {
            return "";
        }

    }

    @RequestMapping("/new")
    public void addNew(@RequestParam(name = "student") String student,
                       @RequestParam(name = "exam") String exam,
                       @RequestParam(name = "reason") String reason) {

        try {
            examen.setStudent(student);
            examen.setExam(exam);
            examen.setReason(reason);

            Boolean inSet = false;
            Set<String> students = service.keys(examen.getStudent() + ":*");
            for (String s : students) {
                String key = new String(s);
                String[] parts = key.split(":");

                if (parts[0] == examen.getStudent() && parts[1] == examen.getExam() && parts[2] == examen.getReason()) {
                    inSet = true;
                }
            }
            if (inSet == false) {
                service.setKey(examen.getStudent() + ":" + examen.getExam() + ":" + examen.getReason(), "");
            }
        }
        catch (Exception error) {

        }

    }

    public void index() {

    }

}
