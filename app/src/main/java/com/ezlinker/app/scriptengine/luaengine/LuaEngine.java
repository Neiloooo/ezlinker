package com.ezlinker.app.scriptengine.luaengine;

import com.ezlinker.app.scriptengine.ScriptEngine;

/**
 * @author wangwenhai
 * @date 2019/12/8
 * description Lua引擎
 */
public class LuaEngine implements ScriptEngine {
    @Override
    public boolean start() {
        return false;
    }

    @Override
    public Object run(String script) {
        return null;
    }

    @Override
    public boolean stop() {
        return false;
    }
}
