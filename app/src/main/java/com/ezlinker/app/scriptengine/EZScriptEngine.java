package com.ezlinker.app.scriptengine;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;

/**
 * @author wangwenhai
 * @date 2019/12/8
 * File description: 脚本引擎
 */
public abstract class EZScriptEngine {

    protected abstract boolean start();

    protected abstract Object executeScript(String script) throws ScriptException;

    protected abstract boolean stop();

    protected abstract Map<String, Object> info();
}
