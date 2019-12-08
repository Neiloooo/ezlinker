package com.ezlinker.app.scriptengine.luaengine;

import com.ezlinker.app.scriptengine.EZScriptEngine;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwenhai
 * @date 2019/12/8
 * description Lua引擎
 */
@Component
@Log
public class LuaEngine extends EZScriptEngine {

    @Override
    protected boolean start() {
        return false;
    }

    @Override
    protected Object executeScript(String script) throws ScriptException {
        return null;
    }

    @Override
    protected boolean stop() {
        return false;
    }

    @Override
    protected Map<String, Object> info() {
        return null;
    }
}
