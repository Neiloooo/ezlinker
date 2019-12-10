package com.ezlinker.app.scriptengine.luaengine;

import com.ezlinker.app.scriptengine.EZScriptEngine;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import javax.script.ScriptException;

/**
 * @author wangwenhai
 * @date 2019/12/8
 * description Lua引擎
 */
@Component
@Log
public class LuaEngine extends EZScriptEngine {

    @Override
    protected Object executeScript(String script) throws ScriptException {
        return null;
    }

}
