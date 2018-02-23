package com.piotrwalkusz.scriptrepository.cucumber.stepdefs;

import com.piotrwalkusz.scriptrepository.ScriptRepositoryApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = ScriptRepositoryApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
