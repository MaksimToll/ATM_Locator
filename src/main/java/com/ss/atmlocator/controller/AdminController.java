package com.ss.atmlocator.controller;

import com.ss.atmlocator.entity.User;
import com.ss.atmlocator.parser.scheduler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 11.11.14.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String error = "Internal error. See logs for details";

    @Autowired
    SchcedService service;


    @RequestMapping
    public String adminPage(ModelMap model){
        model.addAttribute("active","admin");
        List<JobTemplate> jobs = service.getJobs();
        if (jobs != null){model.addAttribute("jobs",jobs);}
        else {model.addAttribute("error",error);}
        return "admin";
    }


    @RequestMapping(value = "/addnew")
    public String addNewJob(ModelMap model) {
        model.addAttribute("add","add");
        return "jobs";
    }

    @RequestMapping(value = "/{currentJobName}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteJob(@PathVariable("currentJobName") String currentJobName
           ) {
        List<JobTemplate> jobs = service.getJobs();
        for(JobTemplate job : jobs){
            if(job.getJobName().toLowerCase().equals(currentJobName.toLowerCase())){
                    String error = service.removeJob(job);
                    if(error != null){
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "/save")
    public String saveJob(
            @ModelAttribute("job")JobModel jobModel,
            @RequestParam(value = "currentJobName", required = false) String currentJobName,
            ModelMap modelMap) {

        JobTemplate jobTemplate = JobTemplateBuilder.newJob()
                 .withJobName(jobModel.getJobName())
                 .withJobGroup(jobModel.getJobGroup())
                 .withTriggerName(jobModel.getJobGroup())
                 .withTriggerGroup(jobModel.getTriggerGroup())
                 .withCronSched(jobModel.getCronSched())
                 .withJobClass(jobModel.getJobClassName())
                 .build();

        if(currentJobName != null && !currentJobName.isEmpty()){
            List<JobTemplate> jobs = service.getJobs();
            for(JobTemplate job : jobs){
                if(job.getJobName().toLowerCase().equals(currentJobName.toLowerCase())){
                    JobTrigerHolder jobHolder = CreateJobFactory.createJob(jobTemplate);
                    if(jobHolder != null){
                        service.removeJob(jobTemplate);
                        service.addJob(jobHolder);
                        return "redirect:/admin";
                    }
                    else {
                        modelMap.addAttribute("add","add");
                        modelMap.addAttribute("job",jobModel);
                        modelMap.addAttribute("error",error);
                        return "jobs";
                    }
                }
            }
        }

        else{
            JobTrigerHolder jobHolder = CreateJobFactory.createJob(jobTemplate);
            if(jobHolder != null){
                service.addJob(jobHolder);
                return "redirect:/admin";
            }
            else {
                modelMap.addAttribute("add", "add");
                modelMap.addAttribute("job",jobModel);
                modelMap.addAttribute("error",error);

            }
        }
        return "jobs";
    }





    @RequestMapping(value = "/users")
    public String adminUsers(ModelMap model) {
        model.addAttribute("active", "adminUsers");
        return "adminUsers";
    }
}
