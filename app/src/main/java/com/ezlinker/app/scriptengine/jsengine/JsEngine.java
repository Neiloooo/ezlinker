package com.ezlinker.app.scriptengine.jsengine;

import com.ezlinker.app.scriptengine.EZScriptEngine;
import delight.nashornsandbox.NashornSandbox;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.script.ScriptException;

/**
 * @author wangwenhai
 * @date 2019/12/8
 * File description: Javascript 引擎
 */
@Component
@Log
public class JsEngine extends EZScriptEngine {

    @Resource
    NashornSandbox jsSandbox;

    @Override
    protected Object executeScript(String script) throws ScriptException {
        return jsSandbox.eval(script);
    }

}
